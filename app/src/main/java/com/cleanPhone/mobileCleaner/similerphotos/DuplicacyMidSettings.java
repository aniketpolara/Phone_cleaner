package com.cleanPhone.mobileCleaner.similerphotos;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.tabs.TabLayout;
import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.filestorage.DialogProperties;
import com.cleanPhone.mobileCleaner.filestorage.DialogSelectionListener;
import com.cleanPhone.mobileCleaner.filestorage.FilePickerDialog;
import com.cleanPhone.mobileCleaner.filestorage.ListItem;
import com.cleanPhone.mobileCleaner.utility.GetMountPoints;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.PermitActivity;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DuplicacyMidSettings extends PermitActivity implements View.OnClickListener {
    private static final int REQUEST_PERMISSIONS = 696;
    private static final String TAG = "DuplicacyMidSettings";
    private static final int TRANS_COLOR = 1426063360;
    public static ArrayList<ListItem> listItem;
    private long cCount;
    private ImageButton customSettingBtn;
    private CheckBox default_chkBox;
    private DuplicacyRefreshAsyncTask duplicacyRefreshAsyncTask;
    private RelativeLayout hiddenPermissionLayout;
    private TextView hinttext;
    public String[] j;
    public LinearLayout k;
    public TextView l;
    public Context m;
    public ImageView setting, back;
    public TextView n;
    private boolean noti_result_back;
    public TextView o;
    private LinearLayout okbtn;
    public TextView p;
    public ProgressBar q;
    private RadioButton rbtn1;
    private RadioButton rbtn2;
    private RadioButton rbtn3;
    private boolean redirectToNoti;
    public FrameLayout s;
    private SeekBar seekBar;
    int admob = 3;

    public FrameLayout t;
    private TextView tvdupcount;
    private TextView tvselection;
    private TextView tvunit;
    public Dialog u;
    public Button v;
    public int x;
    public int y;
    public long w = 0;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    private long cursorCount_camera = 0;
    private long cursorCount_folder = 0;

    public void O(ArrayList arrayList, DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        DialogConfigs.DEFAULT_DIR = ((File) arrayList.get(((AlertDialog) dialogInterface).getListView().getCheckedItemPosition())).getAbsolutePath();
        M();
    }

    public String checkIdOf(TabLayout.Tab tab) {
        return "cb" + tab.getPosition() + "";
    }

    private void checkIfFromBoostSuggestion() {
        if (getIntent().getBooleanExtra("FROMNOTI", false)) {
            @SuppressLint("StaticFieldLeak") DuplicacyRefreshAsyncTask duplicacyRefreshAsyncTask = new DuplicacyRefreshAsyncTask(this) {
                @Override
                public void onPreExecute() {
                    super.onPreExecute();
                    DuplicacyMidSettings.this.rbtn2.setChecked(true);
                    MobiClean.getInstance().duplicatesData.fromCamera = false;
                    MobiClean.getInstance().duplicatesData.fromselectedFolder = false;
                    Util.appendLogmobiclean(DuplicacyMidSettings.TAG, " DuplicacyRefreshAsyncTask  preexecute", "");
                    DuplicacyMidSettings.this.u = new Dialog(DuplicacyMidSettings.this.m);
                    DuplicacyMidSettings.this.u.requestWindowFeature(1);
                    DuplicacyMidSettings.this.u.getWindow().setBackgroundDrawable(new ColorDrawable(DuplicacyMidSettings.TRANS_COLOR));
                    DuplicacyMidSettings.this.u.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    DuplicacyMidSettings.this.u.setContentView(R.layout.custam_progrssbars);
                    DuplicacyMidSettings.this.u.setCancelable(false);
                    DuplicacyMidSettings.this.u.setCanceledOnTouchOutside(false);
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(DuplicacyMidSettings.this.u.getWindow().getAttributes());
                    layoutParams.dimAmount = 0.8f;
                    DuplicacyMidSettings.this.u.getWindow().setAttributes(layoutParams);
                    DuplicacyMidSettings duplicacyMidSettings = DuplicacyMidSettings.this;
                    duplicacyMidSettings.o = (TextView) duplicacyMidSettings.u.findViewById(R.id.progress_title);
                    DuplicacyMidSettings duplicacyMidSettings2 = DuplicacyMidSettings.this;
                    duplicacyMidSettings2.q = (ProgressBar) duplicacyMidSettings2.u.findViewById(R.id.progressBar_cust);
                    DuplicacyMidSettings duplicacyMidSettings3 = DuplicacyMidSettings.this;
                    duplicacyMidSettings3.n = (TextView) duplicacyMidSettings3.u.findViewById(R.id.progress_percent);
                    DuplicacyMidSettings duplicacyMidSettings4 = DuplicacyMidSettings.this;
                    duplicacyMidSettings4.p = (TextView) duplicacyMidSettings4.u.findViewById(R.id.progress_max);
                    DuplicacyMidSettings duplicacyMidSettings5 = DuplicacyMidSettings.this;
                    duplicacyMidSettings5.s = (FrameLayout) duplicacyMidSettings5.u.findViewById(R.id.frame_mid_laysss);
                    DuplicacyMidSettings.this.q.getProgressDrawable().setColorFilter(DuplicacyMidSettings.this.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                    DuplicacyMidSettings duplicacyMidSettings6 = DuplicacyMidSettings.this;
                    duplicacyMidSettings6.o.setText(duplicacyMidSettings6.getResources().getString(R.string.mbc_fetching_images));
                    DuplicacyMidSettings duplicacyMidSettings7 = DuplicacyMidSettings.this;
                    duplicacyMidSettings7.cCount = duplicacyMidSettings7.getCursorCount();
                    if (DuplicacyMidSettings.this.cCount > 0) {
                        TextView textView = DuplicacyMidSettings.this.p;
                        textView.setText("" + ((int) DuplicacyMidSettings.this.cCount));
                    }
                    DuplicacyMidSettings.this.u.show();
                }

                @Override
                public void onPostExecute(String str) {
                    Util.appendLogmobiclean(DuplicacyMidSettings.TAG, " DuplicacyRefreshAsyncTask  onPostExecute", "");
                    DuplicacyMidSettings.this.getWindow().clearFlags(128);
                    Dialog dialog = DuplicacyMidSettings.this.u;
                    if (dialog != null && dialog.isShowing()) {
                        DuplicacyMidSettings.this.u.dismiss();
                    }
                    if (DuplicacyMidSettings.this.cCount > GlobalData.photocount) {
                        Intent intent = new Intent(DuplicacyMidSettings.this, DuplicateDivisionScreen.class);
                        intent.putExtra("FROMCAMERA", true);
                        intent.putExtra(GlobalData.REDIRECTNOTI, true);
                        intent.putExtra("FROMNOTI", DuplicacyMidSettings.this.getIntent().getBooleanExtra("FROMNOTI", false));
                        new SharedPrefUtil(DuplicacyMidSettings.this).saveBooleanToggle("FCAMERA", true);

                            DuplicacyMidSettings.this.finish();
                            DuplicacyMidSettings.this.startActivity(intent);


                    } else {
                        Intent intent2 = new Intent(DuplicacyMidSettings.this, DuplicatesActivity.class);
                        intent2.putExtra("FROMCAMERA", true);
                        intent2.putExtra("FROMNOTI", DuplicacyMidSettings.this.getIntent().getBooleanExtra("FROMNOTI", false));
                        intent2.putExtra(GlobalData.REDIRECTNOTI, true);
                        new SharedPrefUtil(DuplicacyMidSettings.this).saveBooleanToggle("FCAMERA", true);

                            DuplicacyMidSettings.this.finish();
                            DuplicacyMidSettings.this.startActivity(intent2);


                    }
                    super.onPostExecute(str);
                }

                @Override
                public void onProgressUpdate(final String... strArr) {
                    super.onProgressUpdate(strArr);
                    try {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                long j = 0;
                                int parseInt = Integer.parseInt(strArr[0]);
                                if (DuplicacyMidSettings.this.cCount == 0) {
                                    DuplicacyMidSettings.this.cCount = 1L;
                                }
                                DuplicacyMidSettings duplicacyMidSettings = DuplicacyMidSettings.this;
                                duplicacyMidSettings.q.setProgress((int) ((parseInt * 100) / duplicacyMidSettings.cCount));
                                DuplicacyMidSettings.this.n.setText("" + ((int) (j / DuplicacyMidSettings.this.cCount)) + "%");
                                DuplicacyMidSettings.this.p.setText("" + parseInt + DialogConfigs.DIRECTORY_SEPERATOR + ((int) DuplicacyMidSettings.this.cCount));
                            }
                        }, 1000L);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            };
            this.duplicacyRefreshAsyncTask = duplicacyRefreshAsyncTask;
            duplicacyRefreshAsyncTask.execute("", "", "");
        }
    }

    private void clearNotification() {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                try {
                    notificationManager.cancel(623);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (notificationManager != null) {
                try {
                    notificationManager.cancel(300);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public int defaultValueOf(TabLayout.Tab tab) {
        int position = tab.getPosition();
        if (position != 0) {
            return (position == 1 || position == 2) ? 72 : 0;
        }
        return 25;
    }

    public void loadinit() {
        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(DuplicacyMidSettings.this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        return;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

    public long getCursorCount() {
        Cursor cursor = null;
        try {
            try {
                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, null, null, "datetaken DESC");
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor == null) {
                    return 0L;
                }
            }
            if (cursor != null) {
                long count = cursor.getCount();
                if (cursor != null) {
                    cursor.close();
                }
                return count;
            }
            if (cursor == null) {
                return 0L;
            }
            cursor.close();
            return 0L;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public long getCursorCountCamera() {
        Cursor cursor = null;
        try {
            try {
                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data like ? OR _data like ? OR _data like ?", new String[]{"%/dcim/camera/%", "%/dcim/100MEDIA/%", "%/dcim/100andro/%"}, "datetaken DESC");
                if (cursor != null) {
                    Log.i("CURSORCOUNT1", "" + cursor.getCount());
                }
                if (cursor != null) {
                    Util.appendLog("imagecount =" + cursor.getCount(), "cameracount.txt");
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor == null) {
                    return 0L;
                }
            }
            if (cursor != null) {
                long count = cursor.getCount();
                if (cursor != null) {
                    cursor.close();
                }
                return count;
            }
            if (cursor == null) {
                return 0L;
            }
            cursor.close();
            return 0L;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public long getFolderCursorCount() {
        Cursor cursor = null;
        try {
            try {
                String[] strArr = {"_id"};
                String[] strArr2 = new String[listItem.size()];
                for (int i = 0; i < listItem.size(); i++) {
                    strArr2[i] = "%" + listItem.get(i).getPath() + "%";
                }
                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, "_data like ? OR _data like ? OR _data like ?", strArr2, "datetaken DESC");
                if (cursor != null) {
                    Log.i("CURSORCOUNT1", "" + cursor.getCount());
                }
                Util.appendLog("imagecount =" + cursor.getCount(), "cameracount.txt");
                long count = cursor.getCount();
                if (cursor != null) {
                    cursor.close();
                }
                return count;
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                    return 0L;
                }
                return 0L;
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public static int getProgressLevelDist(int i) {
        if (i <= 7) {
            return 0;
        }
        if (i <= 7 || i > 21) {
            if (i <= 21 || i > 36) {
                if (i <= 36 || i > 50) {
                    if (i <= 50 || i > 65) {
                        if (i <= 65 || i > 79) {
                            if (i <= 79 || i > 93) {
                                return i > 93 ? 100 : 0;
                            }
                            return 50;
                        }
                        return 20;
                    }
                    return 10;
                }
                return 5;
            }
            return 2;
        }
        return 1;
    }

    public static int getProgressLevelTime(int i) {
        if (i <= 7) {
            return 0;
        }
        if (i <= 7 || i > 21) {
            if (i <= 21 || i > 36) {
                if (i <= 36 || i > 50) {
                    if (i <= 50 || i > 65) {
                        if (i <= 65 || i > 79) {
                            if (i <= 79 || i > 93) {
                                return i > 93 ? 86400 : 0;
                            }
                            return 3600;
                        }
                        return 600;
                    }
                    return 300;
                }
                return 60;
            }
            return 5;
        }
        return 1;
    }

    public void getSelectedTabSizeText(int i, TextView textView) {
        if (i == 0) {
            manageLevelSeekBar(this.seekBar.getProgress(), textView);
        } else if (i == 1) {
            manageTimeSeekBar(this.seekBar.getProgress(), textView);
        } else if (i != 2) {
        } else {
            manageGpsSeekBar(this.seekBar.getProgress(), textView);
        }
    }

    private void init() {
        Util.appendLogmobiclean(TAG, " init  ", "");
        this.tvdupcount = (TextView) findViewById(R.id.dupdisplay_sizetv);
        this.tvunit = (TextView) findViewById(R.id.junkdisplay_sizetv_unit);
        this.hiddenPermissionLayout = (RelativeLayout) findViewById(R.id.hiddenpermissionlayout);
        this.hinttext = (TextView) findViewById(R.id.tvhint);
        this.okbtn = (LinearLayout) findViewById(R.id.okbtn);
        this.customSettingBtn = (ImageButton) findViewById(R.id.recommended_setting_btn);
        this.tvselection = (TextView) findViewById(R.id.tv_settingtitle);
        this.rbtn1 = (RadioButton) findViewById(R.id.radiobtn_overrite1);
        this.rbtn2 = (RadioButton) findViewById(R.id.radiobtn_overrite2);
        this.rbtn3 = (RadioButton) findViewById(R.id.radiobtn_overrite3);
        TextView textView = (TextView) findViewById(R.id.img_count);
        ScrollView scrollView = (ScrollView) findViewById(R.id.sv);
        back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setting = (ImageView) findViewById(R.id.iv_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    showFilterDialog();


            }
        });
        ((RelativeLayout) findViewById(R.id.rl_permission_close_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DuplicacyMidSettings.this.finish();
            }
        });
        closebtnClick();
        listItem = new ArrayList<>();
        this.rbtn1.isChecked();
    }

    @SuppressLint("ResourceType")
    private void initFolderDialog() {
        final ArrayList<File> returnAllMountPoints = new GetMountPoints(this).returnAllMountPoints();
        if (returnAllMountPoints.size() > 1) {
            new AlertDialog.Builder(this).setSingleChoiceItems(this.j, 0, (DialogInterface.OnClickListener) null).setCancelable(false).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: d.c.a.a.l.a
                @Override
                public final void onClick(DialogInterface dialogInterface, int i) {
                    DuplicacyMidSettings.this.O(returnAllMountPoints, dialogInterface, i);
                }
            }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
            return;
        }
        DialogConfigs.DEFAULT_DIR = returnAllMountPoints.get(0).getAbsolutePath();
        M();
    }

    public void manageGpsSeekBar(int i, TextView textView) {
        if (i <= 7) {
            this.seekBar.setProgress(0);
            textView.setText(R.string.mbc_ignorefilter);
        } else if (i <= 21) {
            this.seekBar.setProgress(14);
            textView.setText(String.format(getString(R.string.mbc_one_meters), 1));
        } else if (i <= 36) {
            this.seekBar.setProgress(29);
            textView.setText(String.format(getString(R.string.mbc_two_meter), 2));
        } else if (i <= 50) {
            this.seekBar.setProgress(43);
            textView.setText(String.format(getString(R.string.mbc_two_meter), 5));
        } else if (i <= 65) {
            this.seekBar.setProgress(57);
            textView.setText(String.format(getString(R.string.mbc_two_meter), 10));
        } else if (i <= 79) {
            textView.setText(String.format(getString(R.string.mbc_two_meter), 20));
            this.seekBar.setProgress(72);
        } else if (i <= 93) {
            this.seekBar.setProgress(86);
            textView.setText(String.format(getString(R.string.mbc_two_meter), 50));
        } else {
            this.seekBar.setProgress(100);
            textView.setText(String.format(getString(R.string.mbc_two_meter), 100));
        }
        MySharedPreference.m(this, this.seekBar.getProgress());
    }

    public void manageLevelSeekBar(int i, TextView textView) {
        if (i <= 12) {
            this.seekBar.setProgress(0);
            textView.setText(String.format(getString(R.string.mbc_leastsimilar), getString(R.string.mbc_least)));
        } else if (i <= 37) {
            this.seekBar.setProgress(25);
            textView.setText(String.format(getString(R.string.mbc_leastsimilar), getString(R.string.mbc_silightly)));
        } else if (i <= 62) {
            this.seekBar.setProgress(50);
            textView.setText(String.format(getString(R.string.mbc_leastsimilar), getString(R.string.mbc_almostsimilar)));
        } else if (i <= 87) {
            this.seekBar.setProgress(75);
            textView.setText(String.format(getString(R.string.mbc_leastsimilar), getString(R.string.mbc_verysimilar)));
        } else {
            this.seekBar.setProgress(100);
            textView.setText(getString(R.string.mbc_exact));
        }
        MySharedPreference.l(this, this.seekBar.getProgress());
    }

    public void manageTimeSeekBar(int i, TextView textView) {
        if (i <= 7) {
            this.seekBar.setProgress(0);
            textView.setText(getString(R.string.mbc_ignorefilter));
        } else if (i <= 21) {
            this.seekBar.setProgress(14);
            textView.setText(String.format(getString(R.string.mbc_onesec), 1));
        } else if (i <= 36) {
            this.seekBar.setProgress(29);
            textView.setText(String.format(getString(R.string.mbc_fivesec), 5));
        } else if (i <= 50) {
            this.seekBar.setProgress(43);
            textView.setText(String.format(getString(R.string.mbc_onemin), 1));
        } else if (i <= 65) {
            this.seekBar.setProgress(57);
            textView.setText(String.format(getString(R.string.mbc_fivemin), 5));
        } else if (i <= 79) {
            textView.setText(String.format(getString(R.string.mbc_fivemin), 10));
            this.seekBar.setProgress(72);
        } else if (i <= 93) {
            this.seekBar.setProgress(86);
            textView.setText(String.format(getString(R.string.mbc_one_hr), 1));
        } else {
            this.seekBar.setProgress(100);
            textView.setText(String.format(getString(R.string.mbc_tewntyfourhr), 24));
        }
        MySharedPreference.n(this, this.seekBar.getProgress());
    }

    private void redirectToNoti() {
        this.redirectToNoti = getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false);
        this.noti_result_back = getIntent().getBooleanExtra(GlobalData.NOTI_RESULT_BACK, false);
    }

    @SuppressLint("StaticFieldLeak")
    public void scanForDuplicates() {
        new DuplicacyRefreshAsyncTask(this) {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onPreExecute() {
                Util.appendLogmobiclean(DuplicacyMidSettings.TAG, "DuplicacyRefreshAsyncTask called onpre", "");
                DuplicacyMidSettings.this.getWindow().addFlags(2097280);
                MobiClean.getInstance().duplicatesData.fromCamera = DuplicacyMidSettings.this.rbtn1.isChecked();
                MobiClean.getInstance().duplicatesData.fromselectedFolder = true;
                DuplicacyMidSettings.this.u = new Dialog(DuplicacyMidSettings.this.m);
                DuplicacyMidSettings.this.u.requestWindowFeature(1);
                DuplicacyMidSettings.this.u.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                DuplicacyMidSettings.this.u.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                DuplicacyMidSettings.this.u.setContentView(R.layout.custam_progrssbars);
                DuplicacyMidSettings.this.u.setCancelable(false);
                DuplicacyMidSettings.this.u.setCanceledOnTouchOutside(false);
                DuplicacyMidSettings.this.u.getWindow().setLayout(-1, -1);
                DuplicacyMidSettings.this.u.getWindow().setGravity(17);
                DuplicacyMidSettings duplicacyMidSettings = DuplicacyMidSettings.this;
                duplicacyMidSettings.o = (TextView) duplicacyMidSettings.u.findViewById(R.id.progress_title);
                DuplicacyMidSettings duplicacyMidSettings2 = DuplicacyMidSettings.this;
                duplicacyMidSettings2.q = (ProgressBar) duplicacyMidSettings2.u.findViewById(R.id.progressBar_cust);
                DuplicacyMidSettings duplicacyMidSettings3 = DuplicacyMidSettings.this;
                duplicacyMidSettings3.n = (TextView) duplicacyMidSettings3.u.findViewById(R.id.progress_percent);
                DuplicacyMidSettings duplicacyMidSettings4 = DuplicacyMidSettings.this;
                duplicacyMidSettings4.p = (TextView) duplicacyMidSettings4.u.findViewById(R.id.progress_max);
                DuplicacyMidSettings duplicacyMidSettings5 = DuplicacyMidSettings.this;
                duplicacyMidSettings5.s = (FrameLayout) duplicacyMidSettings5.u.findViewById(R.id.frame_mid_laysss);
                DuplicacyMidSettings.this.q.getProgressDrawable().setColorFilter(DuplicacyMidSettings.this.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                DuplicacyMidSettings duplicacyMidSettings6 = DuplicacyMidSettings.this;
                duplicacyMidSettings6.o.setText(duplicacyMidSettings6.getResources().getString(R.string.mbc_fetching_images));
                Log.e("=======", "onPreExecute");
                DuplicacyMidSettings duplicacyMidSettings7 = DuplicacyMidSettings.this;
                duplicacyMidSettings7.cursorCount_folder = duplicacyMidSettings7.getFolderCursorCount();
                if (DuplicacyMidSettings.this.cursorCount_folder > 0) {
                    TextView textView = DuplicacyMidSettings.this.p;
                    textView.setText("" + ((int) DuplicacyMidSettings.this.cursorCount_folder));
                }
                DuplicacyMidSettings.this.u.show();
                super.onPreExecute();
            }

            @Override
            public void onPostExecute(String str) {
                super.onPostExecute(str);
                Dialog dialog = DuplicacyMidSettings.this.u;
                if (dialog != null && dialog.isShowing()) {
                    DuplicacyMidSettings.this.u.dismiss();
                }
                if (DuplicacyMidSettings.this.cursorCount_folder > GlobalData.photocount) {
                    Log.d("CRITERIA0", GlobalData.duplicacyDist + " " + GlobalData.duplicacyLevel + "  " + GlobalData.duplicacyTime);
                    Util.appendLogmobiclean(DuplicacyMidSettings.TAG, "DuplicacyRefreshAsyncTask called onpost", "");
                    DuplicacyMidSettings.this.getWindow().clearFlags(128);
                    Intent intent = new Intent(DuplicacyMidSettings.this, DuplicateDivisionScreen.class);
                    intent.putExtra("FROMCAMERA", DuplicacyMidSettings.this.rbtn1.isChecked());
                    intent.putExtra("FROMNOTI", DuplicacyMidSettings.this.getIntent().getBooleanExtra("FROMNOTI", false));
                    intent.putExtra("CURSOR_COUNT", DuplicacyMidSettings.this.cursorCount_folder);
                    Util.appendLogmobiclean(DuplicacyMidSettings.TAG, "DuplicacyRefreshAsyncTask onpost screen called " + DuplicacyMidSettings.this.rbtn1.isChecked(), "");
                    new SharedPrefUtil(DuplicacyMidSettings.this).saveBooleanToggle("FCAMERA", DuplicacyMidSettings.this.rbtn1.isChecked());
                    if (MobiClean.getInstance().duplicatesData.imageList.size() == 0) {
                        DuplicacyMidSettings.this.showNoImgDialog();
                        return;
                    }
                        if (GlobalData.fromSpacemanager) {
                            DuplicacyMidSettings.this.finish();
                        }
                        DuplicacyMidSettings.this.startActivity(intent);


                    return;
                }
                Log.d("CRITERIA0", GlobalData.duplicacyDist + " " + GlobalData.duplicacyLevel + "  " + GlobalData.duplicacyTime);
                Util.appendLogmobiclean(DuplicacyMidSettings.TAG, "DuplicacyRefreshAsyncTask called onpost", "");
                DuplicacyMidSettings.this.getWindow().clearFlags(128);
                Intent intent2 = new Intent(DuplicacyMidSettings.this, DuplicatesActivity.class);
                intent2.putExtra("FROMCAMERA", DuplicacyMidSettings.this.rbtn1.isChecked());
                intent2.putExtra("FROMNOTI", DuplicacyMidSettings.this.getIntent().getBooleanExtra("FROMNOTI", false));
                intent2.putExtra("CURSOR_COUNT", DuplicacyMidSettings.this.cursorCount_folder);
                Util.appendLogmobiclean(DuplicacyMidSettings.TAG, "DuplicacyRefreshAsyncTask onpost screen called " + DuplicacyMidSettings.this.rbtn1.isChecked(), "");
                new SharedPrefUtil(DuplicacyMidSettings.this).saveBooleanToggle("FCAMERA", DuplicacyMidSettings.this.rbtn1.isChecked());
                if (MobiClean.getInstance().duplicatesData.imageList.size() == 0) {
                    DuplicacyMidSettings.this.showNoImgDialog();
                    return;
                }


                    if (GlobalData.fromSpacemanager) {
                        DuplicacyMidSettings.this.finish();
                    }
                    DuplicacyMidSettings.this.startActivity(intent2);


            }

            @Override
            public void onProgressUpdate(final String... strArr) {
                super.onProgressUpdate(strArr);
                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            long j = 0;
                            int parseInt = Integer.parseInt(strArr[0]);
                            if (DuplicacyMidSettings.this.cursorCount_folder == 0) {
                                DuplicacyMidSettings.this.cursorCount_folder = 1L;
                            }
                            DuplicacyMidSettings duplicacyMidSettings = DuplicacyMidSettings.this;
                            duplicacyMidSettings.q.setProgress((int) ((parseInt * 100) / duplicacyMidSettings.cursorCount_folder));
                            DuplicacyMidSettings.this.n.setText("" + ((int) (j / DuplicacyMidSettings.this.cursorCount_folder)) + "%");
                            DuplicacyMidSettings.this.p.setText("" + parseInt + DialogConfigs.DIRECTORY_SEPERATOR + ((int) DuplicacyMidSettings.this.cursorCount_folder));
                        }
                    }, 1000L);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[0]);
    }

    public String seekIdOf(TabLayout.Tab tab) {
        return "sb" + tab.getPosition() + "";
    }

    private void setDeviceDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        int i = displayMetrics.heightPixels;
        this.x = i;
        int i2 = displayMetrics.widthPixels;
        this.y = i2;
        GlobalData.deviceHeight = i;
        GlobalData.deviceWidth = i2;
    }


    private void setListners() {
            okbtn.setOnClickListener(DuplicacyMidSettings.this);


        this.customSettingBtn.setOnClickListener(this);
        this.rbtn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    DuplicacyMidSettings.this.hinttext.setText(R.string.mbc_strhintduplicates);
                }
            }
        });
        this.rbtn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            }
        });
        this.rbtn1.setChecked(true);
    }


    private void showFilterDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setContentView(R.layout.dialog_size_filter);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(-1, -1);
        dialog.getWindow().setGravity(17);
        dialog.show();
        dialog.findViewById(R.id.lay_matching_level).setVisibility(View.GONE);
        View space;
        final TabLayout tabLayout = (TabLayout) dialog.findViewById(R.id.tabLayout);
        final TextView textView = (TextView) dialog.findViewById(R.id.tv_size);
        SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.seekBar);
        this.seekBar = seekBar;
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        this.default_chkBox = (CheckBox) dialog.findViewById(R.id.default_chkBox);
        final HashMap hashMap = new HashMap();
        final HashMap hashMap2 = new HashMap();
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                DuplicacyMidSettings.this.default_chkBox.setChecked(((Boolean) hashMap2.get(Integer.valueOf(tab.getPosition()))).booleanValue());
                DuplicacyMidSettings.this.seekBar.setProgress(((Integer) hashMap.get(Integer.valueOf(tab.getPosition()))).intValue());
                DuplicacyMidSettings.this.getSelectedTabSizeText(tab.getPosition(), textView);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
        });
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tabAt = tabLayout.getTabAt(i);
            if (tabAt != null) {
                hashMap.put(Integer.valueOf(i), Integer.valueOf(MySharedPreference.f(seekIdOf(tabAt), defaultValueOf(tabAt))));
                hashMap2.put(Integer.valueOf(i), Boolean.valueOf(MySharedPreference.a(checkIdOf(tabAt), true)));
            }
        }
        this.default_chkBox.setChecked(((Boolean) hashMap2.get(0)).booleanValue());
        this.seekBar.setProgress(((Integer) hashMap.get(0)).intValue());
        getSelectedTabSizeText(0, textView);
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar2, int i2, boolean z) {
                hashMap.put(Integer.valueOf(tabLayout.getSelectedTabPosition()), Integer.valueOf(i2));
                TabLayout tabLayout2 = tabLayout;
                TabLayout.Tab tabAt2 = tabLayout2.getTabAt(tabLayout2.getSelectedTabPosition());
                if (tabAt2 != null) {
                    int position = tabAt2.getPosition();
                    if (position == 0) {
                        DuplicacyMidSettings.this.manageLevelSeekBar(i2, textView);
                    } else if (position == 1) {
                        DuplicacyMidSettings.this.manageTimeSeekBar(i2, textView);
                    } else if (position != 2) {
                    } else {
                        DuplicacyMidSettings.this.manageGpsSeekBar(i2, textView);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar2) {
            }
        });
        this.default_chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                hashMap2.put(Integer.valueOf(tabLayout.getSelectedTabPosition()), Boolean.valueOf(z));
                TabLayout tabLayout2 = tabLayout;
                TabLayout.Tab tabAt2 = tabLayout2.getTabAt(tabLayout2.getSelectedTabPosition());
                if (tabAt2 != null) {
                    DuplicacyMidSettings.this.seekBar.setProgress(DuplicacyMidSettings.this.defaultValueOf(tabAt2));
                }
            }
        });
        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DuplicacyMidSettings.this.multipleClicked()) {
                    return;
                }
                for (int i2 = 0; i2 < tabLayout.getTabCount(); i2++) {
                    TabLayout.Tab tabAt2 = tabLayout.getTabAt(i2);
                    if (tabAt2 != null) {
                        MySharedPreference.k(DuplicacyMidSettings.this.checkIdOf(tabAt2), ((Boolean) hashMap2.get(Integer.valueOf(i2))).booleanValue());
                        MySharedPreference.o(DuplicacyMidSettings.this.seekIdOf(tabAt2), ((Integer) hashMap.get(Integer.valueOf(i2))).intValue());
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DuplicacyMidSettings.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
            }
        });
    }

    public void showNoImgDialog() {
        if (Util.isConnectingToInternet(this.m) && !Util.isAdsFree(this.m)) {
            Dialog dialog = new Dialog(this.m);
            this.u = dialog;
            dialog.requestWindowFeature(1);
            if (this.u.getWindow() != null) {
                this.u.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                this.u.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            }
            this.u.setContentView(R.layout.no_image_dialog_internet);
            this.u.setCancelable(false);
            this.u.setCanceledOnTouchOutside(false);
            this.u.getWindow().setLayout(-1, -1);
            this.u.getWindow().setGravity(17);
            View space;
            this.v = (Button) this.u.findViewById(R.id.btn_oks);
            this.t = (FrameLayout) this.u.findViewById(R.id.frame_mid_no_image);
            this.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog2;
                    if (DuplicacyMidSettings.this.multipleClicked() || (dialog2 = DuplicacyMidSettings.this.u) == null || !dialog2.isShowing()) {
                        return;
                    }
                    DuplicacyMidSettings.this.u.dismiss();
                }
            });
            this.u.show();
            return;
        }
        Dialog dialog2 = new Dialog(this.m);
        this.u = dialog2;
        dialog2.requestWindowFeature(1);
        if (this.u.getWindow() != null) {
            this.u.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            this.u.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }
        this.u.setContentView(R.layout.no_image_dialog_internet);
        this.u.setCancelable(false);
        this.u.setCanceledOnTouchOutside(false);
        this.u.getWindow().setLayout(-1, -1);
        this.u.getWindow().setGravity(17);
        this.v = (Button) this.u.findViewById(R.id.btn_oks);
        this.t = (FrameLayout) this.u.findViewById(R.id.frame_mid_no_image);
        this.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog3;
                if (DuplicacyMidSettings.this.multipleClicked() || (dialog3 = DuplicacyMidSettings.this.u) == null || !dialog3.isShowing()) {
                    return;
                }
                DuplicacyMidSettings.this.u.dismiss();
            }
        });
        this.u.show();
    }

    private void trackIfFromNotification() {
        if (getIntent().getBooleanExtra("FROMNOTI", false)) {
            GlobalData.fromSpacemanager = false;
        }
    }

    public void M() {
        DialogProperties dialogProperties = new DialogProperties();
        FilePickerDialog filePickerDialog = new FilePickerDialog(this, dialogProperties);
        filePickerDialog.setTitle("" + getResources().getString(R.string.mbc_select_folder));
        filePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
        dialogProperties.selection_type = 1;
        dialogProperties.selection_mode = 1;
        filePickerDialog.setCanceledOnTouchOutside(true);
        filePickerDialog.setCancelable(true);
        filePickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] strArr) {
                if (strArr == null) {
                    return;
                }
                DuplicacyMidSettings.listItem.clear();
                for (String str : strArr) {
                    File file = new File(str);
                    ListItem listItem2 = new ListItem();
                    listItem2.setName(file.getName());
                    listItem2.setPath(file.getAbsolutePath());
                    DuplicacyMidSettings.listItem.add(listItem2);
                }
                if (DuplicacyMidSettings.listItem.size() > 0) {
                    DuplicacyMidSettings.this.scanForDuplicates();
                }
            }
        });
        filePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!this.redirectToNoti && !this.noti_result_back) {
            GlobalData.backfrom_settings = true;
            Util.appendLogmobiclean(TAG, " onBackPressed  ", "");
        } else {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            finish();
            startActivity(intent);
        }
        super.onBackPressed();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.okbtn) {
            return;
        }
        GlobalData.duplicacyLevel = MySharedPreference.c(this);
        GlobalData.duplicacyTime = getProgressLevelTime(MySharedPreference.e(this));
        GlobalData.duplicacyDist = getProgressLevelDist(MySharedPreference.d(this));
        if (this.rbtn1.isChecked()) {
            @SuppressLint("StaticFieldLeak") DuplicacyRefreshAsyncTask duplicacyRefreshAsyncTask = new DuplicacyRefreshAsyncTask(this) {
                @Override
                public void onPreExecute() {
                    super.onPreExecute();
                    MobiClean.getInstance().duplicatesData.fromCamera = true;
                    MobiClean.getInstance().duplicatesData.fromselectedFolder = false;
                    Util.appendLogmobiclean(DuplicacyMidSettings.TAG, " DuplicacyRefreshAsyncTask  preexecute", "");
                    DuplicacyMidSettings.this.u = new Dialog(DuplicacyMidSettings.this.m, R.style.AppTheme);
                    DuplicacyMidSettings.this.u.requestWindowFeature(1);
                    DuplicacyMidSettings.this.u.getWindow().setBackgroundDrawable(new ColorDrawable(DuplicacyMidSettings.TRANS_COLOR));
                    DuplicacyMidSettings.this.u.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    DuplicacyMidSettings.this.u.setContentView(R.layout.custam_progrssbars);
                    DuplicacyMidSettings.this.u.setCancelable(false);
                    DuplicacyMidSettings.this.u.setCanceledOnTouchOutside(false);
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(DuplicacyMidSettings.this.u.getWindow().getAttributes());
                    DuplicacyMidSettings.this.u.getWindow().setAttributes(layoutParams);
                    DuplicacyMidSettings duplicacyMidSettings = DuplicacyMidSettings.this;
                    duplicacyMidSettings.o = (TextView) duplicacyMidSettings.u.findViewById(R.id.progress_title);
                    DuplicacyMidSettings duplicacyMidSettings2 = DuplicacyMidSettings.this;
                    duplicacyMidSettings2.q = (ProgressBar) duplicacyMidSettings2.u.findViewById(R.id.progressBar_cust);
                    DuplicacyMidSettings duplicacyMidSettings3 = DuplicacyMidSettings.this;
                    duplicacyMidSettings3.n = (TextView) duplicacyMidSettings3.u.findViewById(R.id.progress_percent);
                    DuplicacyMidSettings duplicacyMidSettings4 = DuplicacyMidSettings.this;
                    duplicacyMidSettings4.p = (TextView) duplicacyMidSettings4.u.findViewById(R.id.progress_max);
                    DuplicacyMidSettings duplicacyMidSettings5 = DuplicacyMidSettings.this;
                    duplicacyMidSettings5.s = (FrameLayout) duplicacyMidSettings5.u.findViewById(R.id.frame_mid_laysss);
                    DuplicacyMidSettings.this.q.getProgressDrawable().setColorFilter(DuplicacyMidSettings.this.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                    DuplicacyMidSettings duplicacyMidSettings6 = DuplicacyMidSettings.this;
                    duplicacyMidSettings6.o.setText(duplicacyMidSettings6.getResources().getString(R.string.mbc_fetching_images));
                    Log.e("=======", "onPreExecute");
                    DuplicacyMidSettings duplicacyMidSettings7 = DuplicacyMidSettings.this;
                    duplicacyMidSettings7.cursorCount_camera = duplicacyMidSettings7.getCursorCountCamera();
                    if (DuplicacyMidSettings.this.cursorCount_camera > 0) {
                        TextView textView = DuplicacyMidSettings.this.p;
                        textView.setText("" + ((int) DuplicacyMidSettings.this.cursorCount_camera));
                    }
                    DuplicacyMidSettings.this.u.show();
                }

                @Override
                public void onPostExecute(String str) {
                    Util.appendLogmobiclean(DuplicacyMidSettings.TAG, " DuplicacyRefreshAsyncTask  onPostExecute", "");
                    DuplicacyMidSettings.this.getWindow().clearFlags(128);
                    Dialog dialog = DuplicacyMidSettings.this.u;
                    if (dialog != null && dialog.isShowing()) {
                        DuplicacyMidSettings.this.u.dismiss();
                    }
                    if (DuplicacyMidSettings.this.cursorCount_camera > GlobalData.photocount) {
                        if (MobiClean.getInstance().duplicatesData.imageList.size() == 0) {
                            DuplicacyMidSettings.this.showNoImgDialog();
                            return;
                        }
                        Intent intent = new Intent(DuplicacyMidSettings.this, DuplicateDivisionScreen.class);
                        intent.putExtra("FROMCAMERA", true);
                        intent.putExtra("FROMNOTI", DuplicacyMidSettings.this.getIntent().getBooleanExtra("FROMNOTI", false));
                        intent.putExtra("CURSOR_COUNT", DuplicacyMidSettings.this.cursorCount_camera);
                        new SharedPrefUtil(DuplicacyMidSettings.this).saveBooleanToggle("FCAMERA", true);

                            if (GlobalData.fromSpacemanager) {
                                DuplicacyMidSettings.this.finish();

                            DuplicacyMidSettings.this.startActivity(intent);
                        }


                    } else if (MobiClean.getInstance().duplicatesData.imageList.size() == 0) {
                        DuplicacyMidSettings.this.showNoImgDialog();
                        return;
                    } else {
                        Intent intent2 = new Intent(DuplicacyMidSettings.this, DuplicatesActivity.class);
                        intent2.putExtra("FROMCAMERA", true);
                        intent2.putExtra("FROMNOTI", DuplicacyMidSettings.this.getIntent().getBooleanExtra("FROMNOTI", false));
                        new SharedPrefUtil(DuplicacyMidSettings.this).saveBooleanToggle("FCAMERA", true);

                            if (GlobalData.fromSpacemanager) {
                                DuplicacyMidSettings.this.finish();
                            }
                            DuplicacyMidSettings.this.startActivity(intent2);



                    }
                    super.onPostExecute(str);
                }

                @Override
                public void onProgressUpdate(final String... strArr) {
                    super.onProgressUpdate(strArr);
                    try {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                long j = 0;
                                int parseInt = Integer.parseInt(strArr[0]);
                                DuplicacyMidSettings duplicacyMidSettings = DuplicacyMidSettings.this;
                                duplicacyMidSettings.q.setProgress((int) ((parseInt * 100) / duplicacyMidSettings.cursorCount_camera));
                                DuplicacyMidSettings.this.n.setText("" + ((int) (j / DuplicacyMidSettings.this.cursorCount_camera)) + "%");
                                DuplicacyMidSettings.this.p.setText(parseInt + DialogConfigs.DIRECTORY_SEPERATOR + ((int) DuplicacyMidSettings.this.cursorCount_camera));
                            }
                        }, 1000L);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            };
            this.duplicacyRefreshAsyncTask = duplicacyRefreshAsyncTask;
            duplicacyRefreshAsyncTask.execute("", "", "");
        } else if (this.rbtn2.isChecked()) {
            new DuplicacyRefreshAsyncTask(this) {
                @Override
                public void onPreExecute() {
                    Util.appendLogmobiclean(DuplicacyMidSettings.TAG, "DuplicacyRefreshAsyncTask called onpre", "");
                    DuplicacyMidSettings.this.getWindow().addFlags(2097280);
                    MobiClean.getInstance().duplicatesData.fromCamera = DuplicacyMidSettings.this.rbtn1.isChecked();
                    MobiClean.getInstance().duplicatesData.fromselectedFolder = false;
                    DuplicacyMidSettings.this.u = new Dialog(DuplicacyMidSettings.this.m, R.style.AppTheme);
                    DuplicacyMidSettings.this.u.requestWindowFeature(1);
                    DuplicacyMidSettings.this.u.getWindow().setBackgroundDrawable(new ColorDrawable(DuplicacyMidSettings.TRANS_COLOR));
                    DuplicacyMidSettings.this.u.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    DuplicacyMidSettings.this.u.setContentView(R.layout.custam_progrssbars);
                    DuplicacyMidSettings.this.u.setCancelable(false);
                    DuplicacyMidSettings.this.u.setCanceledOnTouchOutside(false);
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(DuplicacyMidSettings.this.u.getWindow().getAttributes());
                    layoutParams.dimAmount = 0.8f;
                    DuplicacyMidSettings.this.u.getWindow().setAttributes(layoutParams);
                    DuplicacyMidSettings duplicacyMidSettings = DuplicacyMidSettings.this;
                    duplicacyMidSettings.o = (TextView) duplicacyMidSettings.u.findViewById(R.id.progress_title);
                    DuplicacyMidSettings duplicacyMidSettings2 = DuplicacyMidSettings.this;
                    duplicacyMidSettings2.q = (ProgressBar) duplicacyMidSettings2.u.findViewById(R.id.progressBar_cust);
                    DuplicacyMidSettings duplicacyMidSettings3 = DuplicacyMidSettings.this;
                    duplicacyMidSettings3.n = (TextView) duplicacyMidSettings3.u.findViewById(R.id.progress_percent);
                    DuplicacyMidSettings duplicacyMidSettings4 = DuplicacyMidSettings.this;
                    duplicacyMidSettings4.p = (TextView) duplicacyMidSettings4.u.findViewById(R.id.progress_max);
                    DuplicacyMidSettings duplicacyMidSettings5 = DuplicacyMidSettings.this;
                    duplicacyMidSettings5.s = (FrameLayout) duplicacyMidSettings5.u.findViewById(R.id.frame_mid_laysss);
                    DuplicacyMidSettings.this.q.getProgressDrawable().setColorFilter(DuplicacyMidSettings.this.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                    DuplicacyMidSettings duplicacyMidSettings6 = DuplicacyMidSettings.this;
                    duplicacyMidSettings6.o.setText(duplicacyMidSettings6.getResources().getString(R.string.mbc_fetching_images));
                    Log.e("=======", "onPreExecute");
                    DuplicacyMidSettings duplicacyMidSettings7 = DuplicacyMidSettings.this;
                    duplicacyMidSettings7.w = duplicacyMidSettings7.getCursorCount();
                    DuplicacyMidSettings duplicacyMidSettings8 = DuplicacyMidSettings.this;
                    if (duplicacyMidSettings8.w > 0) {
                        TextView textView = duplicacyMidSettings8.p;
                        textView.setText("" + ((int) DuplicacyMidSettings.this.w));
                    }
                    DuplicacyMidSettings.this.u.show();
                    super.onPreExecute();
                }

                @Override
                public void onPostExecute(String str) {
                    super.onPostExecute(str);
                    Dialog dialog = DuplicacyMidSettings.this.u;
                    if (dialog != null && dialog.isShowing()) {
                        DuplicacyMidSettings.this.u.dismiss();
                    }
                    if (DuplicacyMidSettings.this.w > GlobalData.photocount) {
                        Log.d("CRITERIA0_Count > 100", GlobalData.duplicacyDist + " " + GlobalData.duplicacyLevel + "  " + GlobalData.duplicacyTime);
                        Util.appendLogmobiclean(DuplicacyMidSettings.TAG, "DuplicacyRefreshAsyncTask called onpost", "");
                        Intent intent = new Intent(DuplicacyMidSettings.this, DuplicateDivisionScreen.class);
                        intent.putExtra("FROMCAMERA", DuplicacyMidSettings.this.rbtn1.isChecked());
                        intent.putExtra("FROMNOTI", DuplicacyMidSettings.this.getIntent().getBooleanExtra("FROMNOTI", false));
                        intent.putExtra("CURSOR_COUNT", DuplicacyMidSettings.this.w);
                        Util.appendLogmobiclean(DuplicacyMidSettings.TAG, "DuplicacyRefreshAsyncTask onpost screen called " + DuplicacyMidSettings.this.rbtn1.isChecked(), "");
                        new SharedPrefUtil(DuplicacyMidSettings.this).saveBooleanToggle("FCAMERA", DuplicacyMidSettings.this.rbtn1.isChecked());
                        if (GlobalData.fromSpacemanager) {
                            DuplicacyMidSettings.this.finish();
                        }
                        if (MobiClean.getInstance().duplicatesData.imageList.size() == 0) {
                            DuplicacyMidSettings.this.showNoImgDialog();
                            return;
                        } else {

                                DuplicacyMidSettings.this.startActivity(intent);
                                return;



                        }
                    }
                    Log.d("CRITERIA0", GlobalData.duplicacyDist + " " + GlobalData.duplicacyLevel + "  " + GlobalData.duplicacyTime);
                    Util.appendLogmobiclean(DuplicacyMidSettings.TAG, "DuplicacyRefreshAsyncTask called onpost", "");
                    DuplicacyMidSettings.this.getWindow().clearFlags(128);


                        Intent intent2 = new Intent(DuplicacyMidSettings.this, DuplicatesActivity.class);
                        intent2.putExtra("FROMNOTI", DuplicacyMidSettings.this.getIntent().getBooleanExtra("FROMNOTI", false));
                        intent2.putExtra("FROMCAMERA", DuplicacyMidSettings.this.rbtn1.isChecked());
                        Util.appendLogmobiclean(DuplicacyMidSettings.TAG, "DuplicacyRefreshAsyncTask onpost screen called " + DuplicacyMidSettings.this.rbtn1.isChecked(), "");
                        new SharedPrefUtil(DuplicacyMidSettings.this).saveBooleanToggle("FCAMERA", DuplicacyMidSettings.this.rbtn1.isChecked());
                        if (MobiClean.getInstance().duplicatesData.imageList.size() == 0) {
                            DuplicacyMidSettings.this.showNoImgDialog();
                            return;
                        }


                            if (GlobalData.fromSpacemanager) {
                                DuplicacyMidSettings.this.finish();
                            }
                            DuplicacyMidSettings.this.startActivity(intent2);



                }

                @Override
                public void onProgressUpdate(final String... strArr) {
                    super.onProgressUpdate(strArr);
                    try {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                long j = 0;
                                int parseInt = Integer.parseInt(strArr[0]);
                                DuplicacyMidSettings duplicacyMidSettings = DuplicacyMidSettings.this;
                                duplicacyMidSettings.q.setProgress((int) ((parseInt * 100) / duplicacyMidSettings.w));
                                DuplicacyMidSettings.this.n.setText("" + ((int) (j / DuplicacyMidSettings.this.w)) + "%");
                                DuplicacyMidSettings.this.p.setText(parseInt + DialogConfigs.DIRECTORY_SEPERATOR + ((int) DuplicacyMidSettings.this.w));
                            }
                        }, 1000L);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(new String[0]);
        } else {
            initFolderDialog();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_duplicacy_mid_settings);
        GlobalData.backfrom_settings = false;
        Util.appendLogmobiclean(TAG, " onCreate  ", "");
        super.requestAppPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, R.string.mbc_runtime_permissions_txt, REQUEST_PERMISSIONS);
        init();
        this.m = this;
        this.j = new String[]{getString(R.string.mbc_in_storage), getString(R.string.mbc_ex_storage)};
        setDeviceDimensions();
        redirectToNoti();
        if (GlobalData.permissionForStorageGiven(this)) {
            setListners();
        }
        this.tvselection.setText(Html.fromHtml("<Html><body>Find and delete duplicates to recover space.</body><Html>"));
        this.hiddenPermissionLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        showInterstialAds(DuplicacyMidSettings.this);

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


        clearNotification();
        trackIfFromNotification();
        GlobalData.duplicacyLevel = MySharedPreference.c(this);
        GlobalData.duplicacyTime = getProgressLevelTime(MySharedPreference.e(this));
        GlobalData.duplicacyDist = getProgressLevelDist(MySharedPreference.d(this));
        checkIfFromBoostSuggestion();
        new SharedPrefUtil(this).saveLastTimeUsed(SharedPrefUtil.LUSED_SIMILAR, System.currentTimeMillis());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settingnoti, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDestroy() {
        try {
            if (Build.VERSION.SDK_INT > 23) {
                DuplicacyRefreshAsyncTask.stopExecuting = true;
                Log.d("CALLLLLED", "2222" + GlobalData.shouldContinue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Dialog dialog = this.u;
        if (dialog != null && dialog.isShowing()) {
            this.u.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != 16908332) {
            if (itemId == R.id.action_reset) {
                    showFilterDialog();


            }
            return super.onOptionsItemSelected(menuItem);
        }
        if (!this.redirectToNoti && !this.noti_result_back) {
            finish();
            GlobalData.backfrom_settings = true;
        } else {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            finish();
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onPermissionsGranted(int i) {
        RelativeLayout relativeLayout = this.hiddenPermissionLayout;
        if (relativeLayout != null) {
            relativeLayout.setVisibility(View.GONE);
            setListners();
        }
    }

    @Override
    public void onResume() {
        RelativeLayout relativeLayout;
        LinearLayout linearLayout;
        if (GlobalData.proceededToAd) {
            finish();
        } else {
            if (GlobalData.backfrom_settings) {
                finish();
            } else if (GlobalData.permissionForStorageGiven(this) && (relativeLayout = this.hiddenPermissionLayout) != null) {
                relativeLayout.setVisibility(View.GONE);
                setListners();
                GlobalData.imageSpace(this);
                long h = MySharedPreference.h("img_space", this);
                int g = MySharedPreference.g("img_count", this);
                this.tvdupcount.setText("" + Util.convertBytes_only(h));
                this.tvunit.setText("" + Util.convertBytes_unit(h));
                ((TextView) findViewById(R.id.tv_images_count)).setText(Html.fromHtml(getString(R.string.space_occupied) + " " + ("<b>" + String.valueOf(g) + "</b> ") + " " + getString(R.string.images_found)));
            }
        }
        if (Util.isAdsFree(this) && (linearLayout = this.k) != null) {
            linearLayout.setVisibility(View.GONE);
            this.l.setVisibility(View.GONE);
        }
        super.onResume();
    }
}