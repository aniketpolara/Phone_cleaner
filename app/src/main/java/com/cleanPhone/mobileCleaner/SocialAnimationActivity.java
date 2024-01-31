package com.cleanPhone.mobileCleaner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.socialmedia.SocialMediaApp;
import com.cleanPhone.mobileCleaner.socialmedia.SocialMediaNew;
import com.cleanPhone.mobileCleaner.socialmedia.SocialScanResultActivity;
import com.cleanPhone.mobileCleaner.socialmedia.SocialmediaModule;
import com.cleanPhone.mobileCleaner.tools.LargeFile;
import com.cleanPhone.mobileCleaner.tools.SocialCleanerListActivity;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.PanicAnimation;
import com.cleanPhone.mobileCleaner.utility.PermitActivity;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppJunk;
import com.cleanPhone.mobileCleaner.wrappers.BigSizeFilesWrapper;
import com.cleanPhone.mobileCleaner.wrappers.MediaAppModule;
import com.cleanPhone.mobileCleaner.wrappers.MediaJunkData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


public class SocialAnimationActivity extends PermitActivity implements View.OnClickListener {
    private static final int REQUEST_PERMISSIONS = 800;
    public static int totalProgressCount;
    private static SocialAnimationActivity activity;
    public boolean isAborted;
    public boolean isWaitScreenShown;
    public AnimationDrawable j;
    public TextView k;
    public RelativeLayout l;
    public RelativeLayout m;
    public FrameLayout n;
    public TextView o;
    public TextView p;
    public volatile boolean isRunning = true;
    int admob = 3;
    int click = 0;
    int numOfClick = 3;
    AdRequest adRequest;
    private TextView ads_message;
    private TextView ads_title;
    private CalculateToalSizeTask calculateTotalSizeTask;
    private Context context;
    private int deviceHeight;
    private int deviceWidth;
    private ProgressDialog dialogStopWait;
    private FragmentTransaction fragmentTransaction;
    private GetLargeFilesData getfilesAsync;
    private Handler handlerProgress;
    private RelativeLayout hiddenPermissionLayout;
    private RelativeLayout linear_main_layout;
    private boolean noti_result_back;
    private PanicAnimation panicAnimation;
    private int pro;
    private ProgressBar progressBar;
    private boolean redirectToNoti;
    private ImageView rotate_image;
    private TextView tvsize;
    private final String progressStatus = "";
    private boolean isInbackground = false;
    private InterstitialAd mInterstitialAd;
    private boolean isExecuting = false;
    private final String TAG = "SocialAnimationActivity";
    private final Runnable r = new Runnable() {
        @Override
        public void run() {

            SocialAnimationActivity.this.next();

        }
    };

    public static SocialAnimationActivity getInstance() {
        return activity;
    }

    private boolean appInstalledOrNot(String str) {
        try {
            getPackageManager().getPackageInfo(str, 1);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private void clearNotification() {
        try {
            Log.e("", "clearNotification()");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.cancel(700);
                notificationManager.cancel(832);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void continueButtonCall() {
        this.m.setVisibility(View.VISIBLE);
        this.l.setVisibility(View.GONE);
        this.handlerProgress.removeCallbacks(this.r);
        try {
            CalculateToalSizeTask calculateToalSizeTask = this.calculateTotalSizeTask;
            if (calculateToalSizeTask != null && calculateToalSizeTask.getStatus() == AsyncTask.Status.RUNNING) {
                this.isAborted = true;
                this.calculateTotalSizeTask.cancel(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            GetLargeFilesData getLargeFilesData = this.getfilesAsync;
            if (getLargeFilesData != null && getLargeFilesData.getStatus() == AsyncTask.Status.RUNNING) {
                this.isAborted = true;
                this.getfilesAsync.cancel(true);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        RelativeLayout relativeLayout = this.m;
        if (relativeLayout != null && relativeLayout.getVisibility() == View.VISIBLE) {
            this.l.setVisibility(View.VISIBLE);
            this.m.setVisibility(View.GONE);
        }
        GetLargeFilesData getLargeFilesData2 = this.getfilesAsync;
        if (getLargeFilesData2 != null) {
            if (getLargeFilesData2.getStatus() != AsyncTask.Status.RUNNING && this.calculateTotalSizeTask.getStatus() != AsyncTask.Status.RUNNING) {
                finish();
            } else {
                showStopWaitdialog();
            }
        }
    }

    private void displayProgress() {
        Util.appendLogmobiclean(this.TAG, "GetLargeFilesData---AsyncTask---displayProgress", GlobalData.FILE_NAME);
        ProgressDialog.show(this, "Loading...", "Please wait...", false, false);
    }

    private long getVideoIdFromFilePath(String str, ContentResolver contentResolver) {
        String[] strArr = {"_id"};
        Cursor query = contentResolver.query(MediaStore.Video.Media.getContentUri("external"), strArr, "_data LIKE ?", new String[]{str}, null);
        if (query != null) {
            query.moveToFirst();
            @SuppressLint("Range") long j = query.getLong(query.getColumnIndex(strArr[0]));
            query.close();
            return j;
        }
        return 0L;
    }

    private void gettingTotalCount() {
        Util.appendLogmobiclean(this.TAG, "method---gettingTotalCount", GlobalData.FILE_NAME);
        if (this.getfilesAsync.getStatus() == AsyncTask.Status.RUNNING || this.getfilesAsync.getStatus() == AsyncTask.Status.FINISHED || this.calculateTotalSizeTask.getStatus() == AsyncTask.Status.RUNNING || this.calculateTotalSizeTask.getStatus() == AsyncTask.Status.FINISHED || this.isExecuting) {
            return;
        }
        this.calculateTotalSizeTask.execute();
    }

    private void init() {
        this.context = this;
        this.l = findViewById(R.id.layout_one);
        this.m = findViewById(R.id.layout_two);
        this.n = findViewById(R.id.frame_mid_laysss);
        this.o = findViewById(R.id.ads_btn_countinue);
        this.p = findViewById(R.id.ads_btn_cancel);
        this.ads_title = findViewById(R.id.dialog_title);
        this.ads_message = findViewById(R.id.dialog_msg);
        this.p.setOnClickListener(this);
        this.o.setOnClickListener(this);
        this.dialogStopWait = new ProgressDialog(this.context);
        this.handlerProgress = new Handler();
        this.tvsize = findViewById(R.id.boostsize);
        TextView textView = findViewById(R.id.booststatus);
        this.panicAnimation = findViewById(R.id.content);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        this.progressBar = progressBar;
        progressBar.getProgressDrawable().setColorFilter(-1, PorterDuff.Mode.SRC_IN);
        this.rotate_image = findViewById(R.id.rotate_image);
        RelativeLayout relativeLayout = findViewById(R.id.arclayout);
        this.linear_main_layout = findViewById(R.id.content_boost_animation);
        this.hiddenPermissionLayout = findViewById(R.id.hiddenpermissionlayout);
        findViewById(R.id.rl_permission_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SocialAnimationActivity.this.redirectToNoti) {
                    SocialAnimationActivity.this.finish();
                    SocialAnimationActivity.this.startActivity(new Intent(SocialAnimationActivity.this, HomeActivity.class));
                    return;
                }
                SocialAnimationActivity.this.finish();
            }
        });
        getIntent().getBooleanExtra(ShareConstants.TITLE, false);
        closebtnClick();
    }

    private void initToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
        }
        if (getIntent().getBooleanExtra(ShareConstants.TITLE, false)) {
            ((TextView) findViewById(R.id.toolbar_title)).setText(getString(R.string.mbc_turbo_cleaner_social));
        }
    }

    public void next() {
        String str = this.TAG;
        Util.appendLogmobiclean(str, "GetLargeFilesData---AsyncTask---after delay next called " + this.isInbackground, GlobalData.FILE_NAME);
        if (this.isInbackground) {
            return;
        }
        if (MobiClean.getInstance().mediaAppModule.totalSize > 0) {
            Util.appendLogmobiclean(this.TAG, "GetLargeFilesData---AsyncTask---onPostExecute--SocialScanResultActivity Go", GlobalData.FILE_NAME);
            if (!this.noti_result_back && !this.redirectToNoti) {
                startActivity(new Intent(this, SocialScanResultActivity.class).putExtra(ShareConstants.TITLE, getIntent().getBooleanExtra(ShareConstants.TITLE, false)).putExtra(GlobalData.REDIRECTNOTI, this.redirectToNoti).putExtra("FROMHOME", getIntent().getBooleanExtra("FROMHOME", false)));
            } else {
                startActivity(new Intent(this, SocialScanResultActivity.class).putExtra(ShareConstants.TITLE, getIntent().getBooleanExtra(ShareConstants.TITLE, false)).putExtra(GlobalData.NOTI_RESULT_BACK, true).putExtra(GlobalData.REDIRECTNOTI, true).putExtra("FROMHOME", getIntent().getBooleanExtra("FROMHOME", false)));
            }
        } else if (!appInstalledOrNot("com.facebook.android") && !appInstalledOrNot("com.whatsapp") && !appInstalledOrNot("com.twitter.android")) {
            Util.appendLogmobiclean(this.TAG, "GetLargeFilesData---AsyncTask---onPostExecute--no_social_app in phone", GlobalData.FILE_NAME);
            Intent putExtra = new Intent(this, CommonResultActivity.class).putExtra(GlobalData.REDIRECTNOTI, this.redirectToNoti);
            if (getIntent() != null && getIntent().getStringExtra("FROM") != null && getIntent().getStringExtra("FROM").equalsIgnoreCase("JUNK_MEDIA_CLEANER")) {
                putExtra.putExtra("DATA", "" + getResources().getString(R.string.mbc_no_junk_media));
            } else {
                putExtra.putExtra("DATA", "" + getResources().getString(R.string.mbc_msg_social));
            }
            putExtra.putExtra("TYPE", "SOCIAL");
            startActivity(putExtra);
        } else {
            Intent putExtra2 = new Intent(this, CommonResultActivity.class).putExtra(GlobalData.REDIRECTNOTI, this.redirectToNoti);
            if (getIntent() != null && getIntent().getStringExtra("FROM") != null && getIntent().getStringExtra("FROM").equalsIgnoreCase("JUNK_MEDIA_CLEANER")) {
                putExtra2.putExtra("DATA", "" + getResources().getString(R.string.mbc_no_junk_media));
            } else {
                putExtra2.putExtra("DATA", "" + getResources().getString(R.string.mbc_msg_social));
            }
            putExtra2.putExtra("TYPE", "SOCIAL");
            startActivity(putExtra2);
        }
        finish();
    }

    private boolean permissionForStorageGiven() {
        return super.checkStoragePermissions();
    }

    private void redirectToNoti() {
        this.noti_result_back = getIntent().getBooleanExtra(GlobalData.NOTI_RESULT_BACK, false);
        this.redirectToNoti = getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false);
    }

    private void rippleAnimation() {
        this.panicAnimation.startRippleAnimation();
        this.rotate_image.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.rotation));
    }

    private void setAnimation() {
        Util.appendLogmobiclean(this.TAG, "background animation ", GlobalData.FILE_NAME);
        TransitionDrawable transitionDrawable = (TransitionDrawable) this.linear_main_layout.getBackground();
        transitionDrawable.startTransition(100);
        this.linear_main_layout.setBackground(transitionDrawable);
    }

    private void setDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressLint("WrongConstant") WindowManager windowManager = (WindowManager) getSystemService("window");
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
        Util.appendLogmobiclean(this.TAG, "method : setDimensions", GlobalData.FILE_NAME);
        try {
            int i = ParentActivity.displaymetrics.heightPixels;
            if ((i <= 780 || i >= 1000) && (i <= 1000 || i > 1442)) {
            }
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.panicAnimation.getLayoutParams();
            layoutParams.height = (this.deviceHeight * 50) / 100;
            this.panicAnimation.setLayoutParams(layoutParams);
            this.rotate_image.getLayoutParams().height = (this.deviceHeight * 30) / 100;
            this.panicAnimation.setLayoutParams(layoutParams);
            DisplayMetrics displayMetrics2 = ParentActivity.displaymetrics;
            int i2 = displayMetrics2.heightPixels;
            if (i2 == 1280 && displayMetrics2.widthPixels == 720) {
                return;
            }
            int i3 = displayMetrics2.widthPixels;
            if (i3 != 1200 || i2 < 1800) {
                if ((i3 < 1536 || i3 > 1600) && i2 != 1280) {
                    if ((i3 != 1800 || i2 < 2400) && i2 != 1920) {
                        if (i2 <= 1750 || i2 >= 1900) {
                            if (i2 < 782 || i2 >= 1000) {
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showStopWaitdialog() {
        Util.appendLogmobiclean(this.TAG, "Show stop waiting pDialog", GlobalData.FILE_NAME);
        this.isWaitScreenShown = true;
        this.dialogStopWait.setCancelable(false);
        this.dialogStopWait.setCanceledOnTouchOutside(false);
        ProgressDialog progressDialog = this.dialogStopWait;
        progressDialog.setMessage("" + getResources().getString(R.string.mbc_stopping_scan) + ", " + getResources().getString(R.string.mbc_please_wait));
        this.dialogStopWait.show();
        this.dialogStopWait.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(SocialAnimationActivity.this.context, "testing ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void trackIfFromNotification() {
        getIntent().getBooleanExtra("FROMNOTI", false);
    }

    @Override
    public void onBackPressed() {
        Util.appendLogmobiclean(this.TAG, "method : onBackPressed calling", GlobalData.FILE_NAME);
        if (this.isAborted && this.isWaitScreenShown) {
            Util.appendLogmobiclean(this.TAG, "returning as onBackPressed wait stop already open", GlobalData.FILE_NAME);
            return;
        }
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        if (dialog.getWindow() != null) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
        }
        dialog.setContentView(R.layout.new_dialog_junk_cancel);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(-1, -1);
        dialog.getWindow().setGravity(17);


        if (getIntent().getBooleanExtra("FROMHOME", false)) {
            ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_turbo_cleaner));
            ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_social_cleaner));
        } else {
            ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_turbo_cleaner_social));
            ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.secure_back));
        }
        if (permissionForStorageGiven()) {
            ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(String.format(getResources().getString(R.string.mbc_simple_back_press), getResources().getString(R.string.mbc_social_scan_txt)));
        } else {
            ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(String.format(getResources().getString(R.string.mbc_simple_back_press), ""));
        }
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SocialAnimationActivity.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SocialAnimationActivity.this.multipleClicked()) {
                    return;
                }
                SocialAnimationActivity.this.handlerProgress.removeCallbacks(SocialAnimationActivity.this.r);
                dialog.dismiss();
                try {
                    if (SocialAnimationActivity.this.calculateTotalSizeTask != null && SocialAnimationActivity.this.calculateTotalSizeTask.getStatus() == AsyncTask.Status.RUNNING) {
                        SocialAnimationActivity socialAnimationActivity = SocialAnimationActivity.this;
                        socialAnimationActivity.isAborted = true;
                        socialAnimationActivity.calculateTotalSizeTask.cancel(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (SocialAnimationActivity.this.getfilesAsync != null && SocialAnimationActivity.this.getfilesAsync.getStatus() == AsyncTask.Status.RUNNING) {
                        SocialAnimationActivity socialAnimationActivity2 = SocialAnimationActivity.this;
                        socialAnimationActivity2.isAborted = true;
                        socialAnimationActivity2.getfilesAsync.cancel(true);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (SocialAnimationActivity.this.getfilesAsync.getStatus() == AsyncTask.Status.RUNNING || SocialAnimationActivity.this.calculateTotalSizeTask.getStatus() == AsyncTask.Status.RUNNING) {
                    SocialAnimationActivity.this.showStopWaitdialog();
                } else {
                    SocialAnimationActivity.this.finish();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (multipleClicked()) {
            return;
        }
        if (view.getId() == R.id.ads_btn_cancel) {
            this.m.setVisibility(View.GONE);
            this.l.setVisibility(View.VISIBLE);
        }
        if (view.getId() == R.id.ads_btn_countinue) {
            continueButtonCall();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.new_fragment_turbo_cleaner);

        getWindow().addFlags(2097280);
        GlobalData.backPressedResult = false;
        activity = this;
        Util.isHome = false;
        this.isAborted = false;
        this.isWaitScreenShown = false;
        Util.appendLogmobiclean(this.TAG, "onCreate Call", GlobalData.FILE_NAME);
        super.requestAppPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, R.string.mbc_runtime_permissions_txt, REQUEST_PERMISSIONS);
        init();
        initToolbar();
        rippleAnimation();
        redirectToNoti();


        LinearLayout llBanner = findViewById(R.id.llBanner);
        DH_GllobalItem.fbAdaptiveBanner(this, llBanner, new DH_GllobalItem.BannerCallback() {
            @Override
            public void onAdLoad() {
                llBanner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFail() {
                llBanner.setVisibility(View.GONE);
            }
        });

        try {
            clearNotification();
            trackIfFromNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            setDimensions();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        GlobalData.fromSocialCleaning = true;
        this.getfilesAsync = new GetLargeFilesData();
        this.calculateTotalSizeTask = new CalculateToalSizeTask();
        this.hiddenPermissionLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        if (permissionForStorageGiven()) {
            gettingTotalCount();
        }
        new SharedPrefUtil(this).saveLastTimeUsed(SharedPrefUtil.LUSED_SOCIAL, System.currentTimeMillis());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onPause() {
        super.onPause();
        Util.appendLogmobiclean(this.TAG, "method : onPause calling", GlobalData.FILE_NAME);
        this.isInbackground = true;
    }

    @Override
    public void onPermissionsGranted(int i) {
        RelativeLayout relativeLayout = this.hiddenPermissionLayout;
        if (relativeLayout != null) {
            relativeLayout.setVisibility(View.GONE);
            gettingTotalCount();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Util.appendLogmobiclean(this.TAG, "method : onResume", GlobalData.FILE_NAME);
        Util.isHome = false;
        if (GlobalData.backPressedResult) {
            finish();
            return;
        }
        this.isInbackground = false;
        if (!permissionForStorageGiven() || this.isInbackground) {
            return;
        }
        if (this.pro >= 100) {
            AnimationDrawable animationDrawable = this.j;
            if (animationDrawable != null) {
                animationDrawable.stop();
            }
            Log.e("size is : ", "" + LargeFile.totalSocialSize);
            if (MobiClean.getInstance().mediaAppModule.totalSize > 0) {
                startActivity(new Intent(this, SocialScanResultActivity.class).putExtra(GlobalData.REDIRECTNOTI, this.redirectToNoti).putExtra(ShareConstants.TITLE, getIntent().getBooleanExtra(ShareConstants.TITLE, false)).putExtra("FROMHOME", getIntent().getBooleanExtra("FROMHOME", false)));
                finish();
            } else if ((!appInstalledOrNot("com.facebook.android") || !appInstalledOrNot("com.facebook.katana")) && !appInstalledOrNot("com.whatsapp") && !appInstalledOrNot("com.twitter.android")) {
                Intent putExtra = new Intent(this, CommonResultActivity.class).putExtra(GlobalData.REDIRECTNOTI, this.redirectToNoti);
                putExtra.putExtra("DATA", "" + getResources().getString(R.string.mbc_msg_social));
                putExtra.putExtra("TYPE", "SOCIAL");
                startActivity(putExtra);
            } else {
                Intent putExtra2 = new Intent(this, CommonResultActivity.class).putExtra(GlobalData.REDIRECTNOTI, this.redirectToNoti);
                putExtra2.putExtra("DATA", "" + getResources().getString(R.string.mbc_msg_social));
                putExtra2.putExtra("TYPE", "SOCIAL");
                startActivity(putExtra2);
            }
        } else if (permissionForStorageGiven()) {
            RelativeLayout relativeLayout = this.hiddenPermissionLayout;
            if (relativeLayout != null && relativeLayout.getVisibility() == View.VISIBLE) {
                this.hiddenPermissionLayout.setVisibility(View.GONE);
            }
            gettingTotalCount();
        }
    }

    public enum FileTypes {
        Image,
        Video,
        Audio,
        Document,
        GIF,
        Others,
        APK,
        ALL
    }

    public enum SocialType {
        WhatsApp,
        Facebook,
        Twitter,
        Instagram,
        Messenger,
        Skype,
        tumbler
    }

    public class CalculateToalSizeTask extends AsyncTask<String, String, String> {
        public CalculateToalSizeTask() {
        }

        private void calculateTotalProgressCount(File file) {
            if (file == null || file.listFiles() == null || file.listFiles().length == 0 || isCancelled()) {
                return;
            }
            SocialAnimationActivity.totalProgressCount += file.listFiles().length;
        }

        @Override
        public void onCancelled() {
            super.onCancelled();
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "CalculateToalSizeTask---AsyncTask---onCancelled", GlobalData.FILE_NAME);
            SocialAnimationActivity socialAnimationActivity = SocialAnimationActivity.this;
            if (socialAnimationActivity.isAborted) {
                socialAnimationActivity.dialogStopWait.dismiss();
                SocialAnimationActivity.this.finish();
            }
        }

        @Override
        public void onPreExecute() {
            SocialAnimationActivity.this.isExecuting = true;
            super.onPreExecute();
            Log.i("SocialScanning", "Calculation Start");
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "CalculateToalSizeTask---AsyncTask---onPreExecute", GlobalData.FILE_NAME);
            SocialAnimationActivity.this.progressBar.setProgress(1);
        }

        @Override
        public String doInBackground(String... strArr) {
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "CalculateToalSizeTask---AsyncTask---doInBackground", GlobalData.FILE_NAME);
            if (SocialAnimationActivity.this.isAborted) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images");
            arrayList.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images/Sent");
            arrayList.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Animated Gifs");
            arrayList.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Animated Gifs/Sent");
            arrayList.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Audio");
            arrayList.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Audio/Sent");
            arrayList.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Video");
            arrayList.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Video/Sent");
            arrayList.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Documents");
            arrayList.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Documents/Sent");
            arrayList.add(Environment.getExternalStorageDirectory() + "/DCIM/Facebook");
            arrayList.add(Environment.getExternalStorageDirectory() + "/Pictures/Twitter");
            arrayList.add(Environment.getExternalStorageDirectory() + "/Pictures/Instagram");
            arrayList.add(Environment.getExternalStorageDirectory() + "/Pictures/Messenger");
            arrayList.add(Environment.getExternalStorageDirectory() + "/Pictures/Skype");
            arrayList.add(Environment.getExternalStorageDirectory() + "/Pictures/Facebook");
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                try {
                    Thread.sleep(5L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (SocialAnimationActivity.this.isAborted) {
                    break;
                }
                try {
                    calculateTotalProgressCount(new File(str));
                } catch (OutOfMemoryError e2) {
                    String str2 = SocialAnimationActivity.this.TAG;
                    Util.appendLogmobiclean(str2, "<<<<<OutOfMemoryError>>>>>>>>", "" + e2.getMessage());
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(String str) {
            SocialAnimationActivity socialAnimationActivity = null;
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "CalculateToalSizeTask---AsyncTask---onPostExecute", GlobalData.FILE_NAME);
            try {
                try {
                    SocialAnimationActivity.this.progressBar.setMax(100);
                    socialAnimationActivity = SocialAnimationActivity.this;
                } catch (Exception e) {
                    e.printStackTrace();
                    if (SocialAnimationActivity.this.dialogStopWait.isShowing()) {
                        SocialAnimationActivity.this.dialogStopWait.dismiss();
                    }
                    SocialAnimationActivity socialAnimationActivity2 = SocialAnimationActivity.this;
                    if (!socialAnimationActivity2.isAborted) {
                        socialAnimationActivity2.getfilesAsync.execute();
                    }
                }
                if (socialAnimationActivity.isAborted) {
                    if (socialAnimationActivity.dialogStopWait.isShowing()) {
                        SocialAnimationActivity.this.dialogStopWait.dismiss();
                    }
                    SocialAnimationActivity socialAnimationActivity3 = SocialAnimationActivity.this;
                    if (!socialAnimationActivity3.isAborted) {
                        socialAnimationActivity3.getfilesAsync.execute();
                    }
                    super.onPostExecute(str);
                } else if (socialAnimationActivity.getfilesAsync.getStatus() == Status.RUNNING) {
                    if (SocialAnimationActivity.this.dialogStopWait.isShowing()) {
                        SocialAnimationActivity.this.dialogStopWait.dismiss();
                    }
                    SocialAnimationActivity socialAnimationActivity4 = SocialAnimationActivity.this;
                    if (!socialAnimationActivity4.isAborted) {
                        socialAnimationActivity4.getfilesAsync.execute();
                    }
                    super.onPostExecute(str);
                } else {
                    SocialAnimationActivity.this.getfilesAsync.getStatus();
                    Status status = Status.FINISHED;
                    if (SocialAnimationActivity.this.dialogStopWait.isShowing()) {
                        SocialAnimationActivity.this.dialogStopWait.dismiss();
                    }
                    SocialAnimationActivity socialAnimationActivity5 = SocialAnimationActivity.this;
                    if (!socialAnimationActivity5.isAborted) {
                        socialAnimationActivity5.getfilesAsync.execute();
                    }
                    super.onPostExecute(str);
                }
            } catch (Throwable th) {
                if (SocialAnimationActivity.this.dialogStopWait.isShowing()) {
                    SocialAnimationActivity.this.dialogStopWait.dismiss();
                }
                SocialAnimationActivity socialAnimationActivity6 = SocialAnimationActivity.this;
                if (!socialAnimationActivity6.isAborted) {
                    socialAnimationActivity6.getfilesAsync.execute();
                }
                super.onPostExecute(str);
                throw th;
            }
        }
    }

    public class GetLargeFilesData extends AsyncTask<String, Integer, String> implements MediaJunkData.updateProgress {
        private long startTime = 0;
        private long endTime = 0;
        private boolean isColorChange = false;
        private boolean isColorChangeSecond = true;

        public GetLargeFilesData() {
        }


        public void b(String str) {
            ((TextView) SocialAnimationActivity.this.findViewById(R.id.current_media)).setText(str);
        }

        private void getFacebookMedia() {
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "method : GetLargeFilesData---AsyncTask---doInBackground---getFacebookMedia", GlobalData.FILE_NAME);
            AppJunk appJunk = new AppJunk("Facebook");
            MediaJunkData mediaJunkData = new MediaJunkData(0, SocialAnimationActivity.this.getResources().getString(R.string.mbc_photos));
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(Environment.getExternalStorageDirectory() + "/DCIM/Facebook");
            arrayList.add(Environment.getExternalStorageDirectory() + "/Pictures/Facebook");
            mediaJunkData.getFiles(arrayList, SocialAnimationActivity.this.getfilesAsync, "Facebook");
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData.totSize;
            appJunk.mediaJunkData.add(mediaJunkData);
            MediaAppModule mediaAppModule = MobiClean.getInstance().mediaAppModule;
            mediaAppModule.socialApp.add(appJunk);
            mediaAppModule.totalSize += appJunk.appJunkSize;
            updateModuleName("Facebook");
            for (int i = 0; i < appJunk.mediaJunkData.size(); i++) {
                try {
                    Collections.sort(appJunk.mediaJunkData.get(i).dataList, new Comparator<BigSizeFilesWrapper>() { // from class: com.mobiclean.phoneclean.SocialAnimationActivity.GetLargeFilesData.2
                        @Override
                        public int compare(BigSizeFilesWrapper bigSizeFilesWrapper, BigSizeFilesWrapper bigSizeFilesWrapper2) {
                            int i2 = bigSizeFilesWrapper2.lastModified > bigSizeFilesWrapper.lastModified ? 1 : -1;
                            if (i2 == 1) {
                                if (bigSizeFilesWrapper.hashCode() < bigSizeFilesWrapper2.hashCode()) {
                                    return 1;
                                }
                                if (bigSizeFilesWrapper.hashCode() > bigSizeFilesWrapper2.hashCode()) {
                                    return -1;
                                }
                            }
                            return i2;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void getInstagramMedia() {
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "method : GetLargeFilesData---AsyncTask---doInBackground---getInstagramMedia", GlobalData.FILE_NAME);
            AppJunk appJunk = new AppJunk("Instagram");
            MediaJunkData mediaJunkData = new MediaJunkData(0, SocialAnimationActivity.this.getResources().getString(R.string.mbc_photos));
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(Environment.getExternalStorageDirectory() + "/Pictures/Instagram");
            mediaJunkData.getFiles(arrayList, SocialAnimationActivity.this.getfilesAsync, "Instagram");
            appJunk.mediaJunkData.add(mediaJunkData);
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData.totSize;
            MediaAppModule mediaAppModule = MobiClean.getInstance().mediaAppModule;
            mediaAppModule.socialApp.add(appJunk);
            mediaAppModule.totalSize += appJunk.appJunkSize;
            updateModuleName("Instagram");
            for (int i = 0; i < appJunk.mediaJunkData.size(); i++) {
                try {
                    Collections.sort(appJunk.mediaJunkData.get(i).dataList, new Comparator<BigSizeFilesWrapper>() { // from class: com.mobiclean.phoneclean.SocialAnimationActivity.GetLargeFilesData.4
                        @Override
                        public int compare(BigSizeFilesWrapper bigSizeFilesWrapper, BigSizeFilesWrapper bigSizeFilesWrapper2) {
                            int i2 = bigSizeFilesWrapper2.lastModified > bigSizeFilesWrapper.lastModified ? 1 : -1;
                            if (i2 == 1) {
                                if (bigSizeFilesWrapper.hashCode() < bigSizeFilesWrapper2.hashCode()) {
                                    return 1;
                                }
                                if (bigSizeFilesWrapper.hashCode() > bigSizeFilesWrapper2.hashCode()) {
                                    return -1;
                                }
                            }
                            return i2;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void getMessangerMedia() {
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "method : GetLargeFilesData---AsyncTask---doInBackground---getMessangerMedia", GlobalData.FILE_NAME);
            AppJunk appJunk = new AppJunk("Messenger");
            MediaJunkData mediaJunkData = new MediaJunkData(0, SocialAnimationActivity.this.getResources().getString(R.string.mbc_photos));
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(Environment.getExternalStorageDirectory() + "/Pictures/Messenger");
            mediaJunkData.getFiles(arrayList, SocialAnimationActivity.this.getfilesAsync, "Messenger");
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData.totSize;
            appJunk.mediaJunkData.add(mediaJunkData);
            MediaAppModule mediaAppModule = MobiClean.getInstance().mediaAppModule;
            mediaAppModule.socialApp.add(appJunk);
            mediaAppModule.totalSize += appJunk.appJunkSize;
            updateModuleName("Messenger");
            for (int i = 0; i < appJunk.mediaJunkData.size(); i++) {
                try {
                    Collections.sort(appJunk.mediaJunkData.get(i).dataList, new Comparator<BigSizeFilesWrapper>() { // from class: com.mobiclean.phoneclean.SocialAnimationActivity.GetLargeFilesData.5
                        @Override
                        public int compare(BigSizeFilesWrapper bigSizeFilesWrapper, BigSizeFilesWrapper bigSizeFilesWrapper2) {
                            int i2 = bigSizeFilesWrapper2.lastModified > bigSizeFilesWrapper.lastModified ? 1 : -1;
                            if (i2 == 1) {
                                if (bigSizeFilesWrapper.hashCode() < bigSizeFilesWrapper2.hashCode()) {
                                    return 1;
                                }
                                if (bigSizeFilesWrapper.hashCode() > bigSizeFilesWrapper2.hashCode()) {
                                    return -1;
                                }
                            }
                            return i2;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void getSkypeMedia() {
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "method : GetLargeFilesData---AsyncTask---doInBackground---getSkypeMedia", GlobalData.FILE_NAME);
            AppJunk appJunk = new AppJunk("Skype");
            MediaJunkData mediaJunkData = new MediaJunkData(0, SocialAnimationActivity.this.getResources().getString(R.string.mbc_photos));
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(Environment.getExternalStorageDirectory() + "/Pictures/Skype");
            mediaJunkData.getFiles(arrayList, SocialAnimationActivity.this.getfilesAsync, "Skype");
            appJunk.mediaJunkData.add(mediaJunkData);
            MediaAppModule mediaAppModule = MobiClean.getInstance().mediaAppModule;
            mediaAppModule.socialApp.add(appJunk);
            mediaAppModule.totalSize += appJunk.appJunkSize;
            updateModuleName("Skype");
            for (int i = 0; i < appJunk.mediaJunkData.size(); i++) {
                try {
                    Collections.sort(appJunk.mediaJunkData.get(i).dataList, new Comparator<BigSizeFilesWrapper>() { // from class: com.mobiclean.phoneclean.SocialAnimationActivity.GetLargeFilesData.6
                        @Override
                        public int compare(BigSizeFilesWrapper bigSizeFilesWrapper, BigSizeFilesWrapper bigSizeFilesWrapper2) {
                            int i2 = bigSizeFilesWrapper2.lastModified > bigSizeFilesWrapper.lastModified ? 1 : -1;
                            if (i2 == 1) {
                                if (bigSizeFilesWrapper.hashCode() < bigSizeFilesWrapper2.hashCode()) {
                                    return 1;
                                }
                                if (bigSizeFilesWrapper.hashCode() > bigSizeFilesWrapper2.hashCode()) {
                                    return -1;
                                }
                            }
                            return i2;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void getTumblerMedia() {
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "method : GetLargeFilesData---AsyncTask---doInBackground---getMessangerMedia", GlobalData.FILE_NAME);
            AppJunk appJunk = new AppJunk("Tumbler");
            MediaJunkData mediaJunkData = new MediaJunkData(0, SocialAnimationActivity.this.getResources().getString(R.string.mbc_photos));
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(Environment.getExternalStorageDirectory() + "/DCIM/camera");
            mediaJunkData.getFiles(arrayList, SocialAnimationActivity.this.getfilesAsync, "Messenger");
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData.totSize;
            appJunk.mediaJunkData.add(mediaJunkData);
            MediaAppModule mediaAppModule = MobiClean.getInstance().mediaAppModule;
            mediaAppModule.socialApp.add(appJunk);
            mediaAppModule.totalSize += appJunk.appJunkSize;
            updateModuleName("Tumbler");
            for (int i = 0; i < appJunk.mediaJunkData.size(); i++) {
                try {
                    Collections.sort(appJunk.mediaJunkData.get(i).dataList, new Comparator<BigSizeFilesWrapper>() { // from class: com.mobiclean.phoneclean.SocialAnimationActivity.GetLargeFilesData.7
                        @Override
                        public int compare(BigSizeFilesWrapper bigSizeFilesWrapper, BigSizeFilesWrapper bigSizeFilesWrapper2) {
                            int i2 = bigSizeFilesWrapper2.lastModified > bigSizeFilesWrapper.lastModified ? 1 : -1;
                            if (i2 == 1) {
                                if (bigSizeFilesWrapper.hashCode() < bigSizeFilesWrapper2.hashCode()) {
                                    return 1;
                                }
                                if (bigSizeFilesWrapper.hashCode() > bigSizeFilesWrapper2.hashCode()) {
                                    return -1;
                                }
                            }
                            return i2;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void getTwitterMedia() {
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "method : GetLargeFilesData---AsyncTask---doInBackground---getTwitterMedia", GlobalData.FILE_NAME);
            AppJunk appJunk = new AppJunk("Twitter");
            MediaJunkData mediaJunkData = new MediaJunkData(0, SocialAnimationActivity.this.getResources().getString(R.string.mbc_photos));
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(Environment.getExternalStorageDirectory() + "/Pictures/Twitter");
            mediaJunkData.getFiles(arrayList, SocialAnimationActivity.this.getfilesAsync, "Twitter");
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData.totSize;
            appJunk.mediaJunkData.add(mediaJunkData);
            MediaAppModule mediaAppModule = MobiClean.getInstance().mediaAppModule;
            mediaAppModule.socialApp.add(appJunk);
            mediaAppModule.totalSize += appJunk.appJunkSize;
            updateModuleName("Twitter");
            for (int i = 0; i < appJunk.mediaJunkData.size(); i++) {
                try {
                    Collections.sort(appJunk.mediaJunkData.get(i).dataList, new Comparator<BigSizeFilesWrapper>() { // from class: com.mobiclean.phoneclean.SocialAnimationActivity.GetLargeFilesData.3
                        @Override
                        public int compare(BigSizeFilesWrapper bigSizeFilesWrapper, BigSizeFilesWrapper bigSizeFilesWrapper2) {
                            int i2 = bigSizeFilesWrapper2.lastModified > bigSizeFilesWrapper.lastModified ? 1 : -1;
                            if (i2 == 1) {
                                if (bigSizeFilesWrapper.hashCode() < bigSizeFilesWrapper2.hashCode()) {
                                    return 1;
                                }
                                if (bigSizeFilesWrapper.hashCode() > bigSizeFilesWrapper2.hashCode()) {
                                    return -1;
                                }
                            }
                            return i2;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void getWhatsAppMedia() {
            AppJunk appJunk = new AppJunk("WhatsApp");
            MediaJunkData mediaJunkData = new MediaJunkData(0, SocialAnimationActivity.this.getResources().getString(R.string.mbc_photos));
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images");
            arrayList.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images/Sent");
            mediaJunkData.getFiles(arrayList, SocialAnimationActivity.this.getfilesAsync, "WhatsApp");
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData.totSize;
            appJunk.mediaJunkData.add(mediaJunkData);
            MediaJunkData mediaJunkData2 = new MediaJunkData(1, SocialAnimationActivity.this.getResources().getString(R.string.mbc_viewmore_video));
            ArrayList<String> arrayList2 = new ArrayList<>();
            arrayList2.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Video");
            arrayList2.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Video/Sent");
            mediaJunkData2.getFiles(arrayList2, SocialAnimationActivity.this.getfilesAsync, "WhatsApp");
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData2.totSize;
            appJunk.mediaJunkData.add(mediaJunkData2);
            MediaJunkData mediaJunkData3 = new MediaJunkData(2, SocialAnimationActivity.this.getResources().getString(R.string.mbc_myaudios));
            ArrayList<String> arrayList3 = new ArrayList<>();
            arrayList3.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Audio");
            arrayList3.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Audio/Sent");
            mediaJunkData3.getFiles(arrayList3, SocialAnimationActivity.this.getfilesAsync, "WhatsApp");
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData3.totSize;
            appJunk.mediaJunkData.add(mediaJunkData3);
            MediaJunkData mediaJunkData4 = new MediaJunkData(3, SocialAnimationActivity.this.getResources().getString(R.string.mbc_viewmore_document));
            ArrayList<String> arrayList4 = new ArrayList<>();
            arrayList4.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Documents");
            arrayList4.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Documents/Sent");
            mediaJunkData4.getFiles(arrayList4, SocialAnimationActivity.this.getfilesAsync, "WhatsApp");
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData4.totSize;
            appJunk.mediaJunkData.add(mediaJunkData4);
            MediaJunkData mediaJunkData5 = new MediaJunkData(4, "GIF");
            ArrayList<String> arrayList5 = new ArrayList<>();
            arrayList5.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Animated Gifs");
            arrayList5.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Animated Gifs/Sent");
            mediaJunkData5.getFiles(arrayList5, SocialAnimationActivity.this.getfilesAsync, "WhatsApp");
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData5.totSize;
            appJunk.mediaJunkData.add(mediaJunkData5);
            MediaAppModule mediaAppModule = MobiClean.getInstance().mediaAppModule;
            mediaAppModule.socialApp.add(appJunk);
            mediaAppModule.totalSize += appJunk.appJunkSize;
            new SocialMediaApp().name = "Whatsapp";
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "method : GetLargeFilesData---AsyncTask---doInBackground---getWhatsAppMedia", GlobalData.FILE_NAME);
            updateModuleName("Whatsapp");
            for (int i = 0; i < appJunk.mediaJunkData.size(); i++) {
                try {
                    try {
                        Collections.sort(appJunk.mediaJunkData.get(i).dataList, new Comparator<BigSizeFilesWrapper>() {
                            @Override
                            public int compare(BigSizeFilesWrapper bigSizeFilesWrapper, BigSizeFilesWrapper bigSizeFilesWrapper2) {
                                int i2 = bigSizeFilesWrapper2.lastModified > bigSizeFilesWrapper.lastModified ? 1 : -1;
                                if (i2 == 1) {
                                    if (bigSizeFilesWrapper.hashCode() < bigSizeFilesWrapper2.hashCode()) {
                                        return 1;
                                    }
                                    if (bigSizeFilesWrapper.hashCode() > bigSizeFilesWrapper2.hashCode()) {
                                        return -1;
                                    }
                                }
                                return i2;
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            }
        }

        private void updateModuleName(final String str) {
            SocialAnimationActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GetLargeFilesData.this.b(str);
                }
            });
        }

        @Override
        public void onCancelled() {
            super.onCancelled();
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "GetLargeFilesData---AsyncTask---onCancelled", GlobalData.FILE_NAME);
            SocialAnimationActivity socialAnimationActivity = SocialAnimationActivity.this;
            if (socialAnimationActivity.isAborted) {
                socialAnimationActivity.dialogStopWait.dismiss();
                if (SocialAnimationActivity.this.redirectToNoti) {
                    SocialAnimationActivity.this.finish();
                    SocialAnimationActivity.this.startActivity(new Intent(SocialAnimationActivity.this, HomeActivity.class));
                    return;
                }
                SocialAnimationActivity.this.finish();
            }
        }

        @Override
        public void onPreExecute() {
            this.startTime = System.currentTimeMillis();
            LargeFile.totalSocialSize = 0L;
            super.onPreExecute();
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "GetLargeFilesData---AsyncTask---onPreExecute", GlobalData.FILE_NAME);
        }

        @Override
        public void update(String str) {
            SocialCleanerListActivity.fillCount++;
            if (SocialAnimationActivity.totalProgressCount != 0) {
                publishProgress(Integer.valueOf((SocialCleanerListActivity.fillCount * 100) / SocialAnimationActivity.totalProgressCount));
            }
        }

        @Override
        public String doInBackground(String... strArr) {
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "GetLargeFilesData---AsyncTask---doInBackground Scaning Start", GlobalData.FILE_NAME);
            SocialAnimationActivity.this.isRunning = true;
            MobiClean.getInstance().socialModule = null;
            MobiClean.getInstance().socialModule = new SocialmediaModule();
            MobiClean.getInstance().socialModuleNew = null;
            MobiClean.getInstance().socialModuleNew = new SocialMediaNew();
            MobiClean.getInstance().mediaAppModule = new MediaAppModule();
            if (!SocialAnimationActivity.this.isAborted) {
                getWhatsAppMedia();
            }
            if (!SocialAnimationActivity.this.isAborted) {
                getFacebookMedia();
            }
            if (!SocialAnimationActivity.this.isAborted) {
                getTwitterMedia();
            }
            if (!SocialAnimationActivity.this.isAborted) {
                getInstagramMedia();
            }
            if (!SocialAnimationActivity.this.isAborted) {
                getSkypeMedia();
            }
            if (!SocialAnimationActivity.this.isAborted) {
                getMessangerMedia();
            }
            MobiClean.getInstance().socialModule.updateSelf();
            MobiClean.getInstance().mediaAppModule.refresh();
            SocialAnimationActivity.this.isRunning = true;
            SocialAnimationActivity socialAnimationActivity = SocialAnimationActivity.this;
            if (!socialAnimationActivity.isAborted) {
                socialAnimationActivity.pro = 100;
                publishProgress(Integer.valueOf(SocialAnimationActivity.this.pro));
                long currentTimeMillis = System.currentTimeMillis();
                this.endTime = currentTimeMillis;
                long j = currentTimeMillis - this.startTime;
                String str = SocialAnimationActivity.this.TAG;
                Util.appendLogmobiclean(str, "GetLargeFilesData---AsyncTask---onPostExecute Time difference " + j, GlobalData.FILE_NAME);
                if (j <= 1000) {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "GetLargeFilesData---AsyncTask---doInBackground Scaning finish", GlobalData.FILE_NAME);
            return null;
        }

        @Override
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            if (SocialAnimationActivity.this.dialogStopWait.isShowing()) {
                SocialAnimationActivity.this.dialogStopWait.dismiss();
            }
            Util.appendLogmobiclean(SocialAnimationActivity.this.TAG, "GetLargeFilesData---AsyncTask---onPostExecute", GlobalData.FILE_NAME);
            if (isCancelled()) {
                return;
            }
            SocialAnimationActivity.this.progressBar.setProgress(100);
            SocialAnimationActivity.this.pro = 100;


            SocialAnimationActivity.this.next();

        }

        @Override
        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate(numArr);
            int i = SocialAnimationActivity.totalProgressCount;
            if (i > 2000) {
                if (i <= 5000) {
                    if (numArr[0].intValue() > 40 && !this.isColorChange) {
                        this.isColorChange = true;
                    }
                } else if (numArr[0].intValue() > 40 && !this.isColorChange) {
                    this.isColorChange = true;
                } else if (numArr[0].intValue() > 70 && this.isColorChangeSecond) {
                    this.isColorChangeSecond = false;
                }
            }
            if (numArr[0].intValue() > 1) {
                SocialAnimationActivity.this.progressBar.setProgress(numArr[0].intValue());
                SocialAnimationActivity.this.tvsize.setText(String.valueOf(numArr[0]));
            }
        }
    }
}
