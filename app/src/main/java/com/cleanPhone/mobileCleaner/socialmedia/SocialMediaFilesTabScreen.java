package com.cleanPhone.mobileCleaner.socialmedia;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.JunkDeleteAnimationScreen;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.similerphotos.AsyncTask;
import com.cleanPhone.mobileCleaner.tools.LargeFile;
import com.cleanPhone.mobileCleaner.utility.FileUtil;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.MediaStoreUtil;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppJunk;
import com.cleanPhone.mobileCleaner.wrappers.BigSizeFilesWrapper;
import com.cleanPhone.mobileCleaner.wrappers.MediaAppModule;
import com.cleanPhone.mobileCleaner.wrappers.MediaJunkData;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SocialMediaFilesTabScreen extends ParentActivity implements View.OnClickListener {
    private static final String TAG = "SocialMediaFilesTabScreen";
    private LinearLayout adView;
    private TextView ads_message_deletion;
    private TextView ads_title_deletion;
    private int deviceHeight;
    private int deviceWidth;
    private DisplayMetrics displaymetrics;
    private int index;
    public LinearLayout j;
    public RelativeLayout k;
    public RelativeLayout l;
    public FrameLayout m;
    public FrameLayout n;
    public TextView o;
    public TextView p;
    public TextView q;
    public TextView s;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public class CleanSocialData extends AsyncTask<String, Integer, String> {
        public ProgressDialog f5138a;

        public CleanSocialData() {
        }

        private boolean isDeleted(BigSizeFilesWrapper bigSizeFilesWrapper) {
            if (bigSizeFilesWrapper.file.exists()) {
                bigSizeFilesWrapper.file.delete();
                return !bigSizeFilesWrapper.file.exists();
            }
            return true;
        }

        private void updateMediaScannerPath(File file) {
            if (FileUtil.isKitKat()) {
                Uri uriFromFile = MediaStoreUtil.getUriFromFile(SocialMediaFilesTabScreen.this, file.getAbsolutePath());
                if (uriFromFile != null) {
                    SocialMediaFilesTabScreen.this.getContentResolver().delete(uriFromFile, null, null);
                }
            } else if (!FileUtil.isSystemAndroid5()) {
                MediaScannerConnection.scanFile(SocialMediaFilesTabScreen.this, new String[]{file.getAbsolutePath()}, null, null);
            } else {
                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                intent.setData(Uri.fromFile(file));
                SocialMediaFilesTabScreen.this.sendBroadcast(intent);
            }
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            Util.appendLogmobiclean(SocialMediaFilesTabScreen.TAG, "CleanSocialData-----onPreExecute", GlobalData.FILE_NAME);
            this.f5138a = new ProgressDialog(SocialMediaFilesTabScreen.this);
            SocialMediaFilesTabScreen.this.getWindow().addFlags(2097280);
            ProgressDialog progressDialog = this.f5138a;
            progressDialog.setTitle("" + SocialMediaFilesTabScreen.this.getResources().getString(R.string.mbc_cleaning));
            this.f5138a.setCanceledOnTouchOutside(false);
            this.f5138a.setProgressStyle(1);
            this.f5138a.setCancelable(false);
            this.f5138a.setMax((int) MobiClean.getInstance().mediaAppModule.socialApp.get(GlobalData.APP_INDEX).selectedCount);
            this.f5138a.show();
        }

        @Override
        public String doInBackground(String... strArr) {
            Util.appendLogmobiclean(SocialMediaFilesTabScreen.TAG, "CleanSocialData-----doInBackground", GlobalData.FILE_NAME);
            try {
                MediaAppModule mediaAppModule = MobiClean.getInstance().mediaAppModule;
                Iterator<MediaJunkData> it = mediaAppModule.socialApp.get(GlobalData.APP_INDEX).mediaJunkData.iterator();
                int i = 0;
                while (it.hasNext()) {
                    MediaJunkData next = it.next();
                    int size = next.dataList.size();
                    int i2 = 0;
                    while (i2 < size) {
                        BigSizeFilesWrapper bigSizeFilesWrapper = next.dataList.get(i2);
                        if (bigSizeFilesWrapper.ischecked) {
                            if (isDeleted(bigSizeFilesWrapper)) {
                                next.dataList.remove(i2);
                                size--;
                                i++;
                                publishProgress(Integer.valueOf(i));
                                updateMediaScannerPath(bigSizeFilesWrapper.file);
                            } else {
                                i2++;
                            }
                            mediaAppModule.recoveredSize += bigSizeFilesWrapper.size;
                        } else {
                            i2++;
                        }
                    }
                }
                mediaAppModule.socialApp.get(GlobalData.APP_INDEX).refresh();
                long j = mediaAppModule.recoveredSize;
                return j > 0 ? Util.convertBytes(j) : "";
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        public void onPostExecute(String str) {
            super.onPostExecute( str);
            Util.appendLogmobiclean(SocialMediaFilesTabScreen.TAG, "CleanSocialData-----onPostExecute", GlobalData.FILE_NAME);
            if (this.f5138a.isShowing()) {
                this.f5138a.dismiss();
            }
            try {
                SocialMediaFilesTabScreen.this.getWindow().clearFlags(2097280);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(str)) {
                return;
            }
            LargeFile.totalSocialSize = 0L;
            try {
                MobiClean.getInstance().mediaAppModule.recoveredSize = 0L;
                MobiClean.getInstance().mediaAppModule.socialApp.get(GlobalData.APP_INDEX).selectedSize = 0L;
                MobiClean.getInstance().mediaAppModule.socialApp.get(GlobalData.APP_INDEX).selectedCount = 0L;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            boolean z = true;
            GlobalData.returnedAfterSocialDeletion = true;
            SocialMediaFilesTabScreen socialMediaFilesTabScreen = SocialMediaFilesTabScreen.this;
            Intent putExtra = new Intent(SocialMediaFilesTabScreen.this, JunkDeleteAnimationScreen.class).putExtra("TYPE", "Social").putExtra(GlobalData.REDIRECTNOTI, SocialMediaFilesTabScreen.this.getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false) || SocialMediaFilesTabScreen.this.getIntent().getBooleanExtra("WHATS", false));
            if (!SocialMediaFilesTabScreen.this.getIntent().getBooleanExtra("FROMNOTI", false) && !SocialMediaFilesTabScreen.this.getIntent().getBooleanExtra("WHATS", false)) {
                z = false;
            }
            socialMediaFilesTabScreen.startActivity(putExtra.putExtra("FROMNOTI", z).putExtra("DATA", str));
        }

        @Override
        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate( numArr);
            this.f5138a.setProgress(numArr[0].intValue());
        }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList;
        private final List<String> mFragmentTitleList;

        public ViewPagerAdapter(SocialMediaFilesTabScreen socialMediaFilesTabScreen, FragmentManager fragmentManager) {
            super(fragmentManager);
            this.mFragmentList = new ArrayList();
            this.mFragmentTitleList = new ArrayList();
        }

        public void b(Fragment fragment, String str) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(str);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        @Override
        public Fragment getItem(int i) {
            return this.mFragmentList.get(i);
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return this.mFragmentTitleList.get(i);
        }
    }

    private void getids() {
        this.displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(this.displaymetrics);
        }
        DisplayMetrics displayMetrics = this.displaymetrics;
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
        this.j = (LinearLayout) findViewById(R.id.layout_one);
        this.k = (RelativeLayout) findViewById(R.id.layout_two);
        this.l = (RelativeLayout) findViewById(R.id.layout_three);
        this.m = (FrameLayout) findViewById(R.id.frame_mid_laysss);
        this.n = (FrameLayout) findViewById(R.id.frame_mid_laysss_deletion);
        this.o = (TextView) findViewById(R.id.ads_btn_countinue);
        this.p = (TextView) findViewById(R.id.ads_btn_cancel);
        this.q = (TextView) findViewById(R.id.ads_btn_countinue_deletion);
        this.s = (TextView) findViewById(R.id.ads_btn_cancel_deletion);
        this.ads_title_deletion = (TextView) findViewById(R.id.dialog_title_deletion);
        this.ads_message_deletion = (TextView) findViewById(R.id.dialog_msg_deletion);
        this.p.setOnClickListener(this);
        this.o.setOnClickListener(this);
        this.s.setOnClickListener(this);
        this.q.setOnClickListener(this);
        this.tabLayout = (TabLayout) findViewById(R.id.tabs);

        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void refresh() {
        int currentItem = this.viewPager.getCurrentItem();
        try {
            setupViewPager(this.viewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.tabLayout.setupWithViewPager(this.viewPager);
        this.viewPager.setCurrentItem(currentItem);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        this.index = getIntent().getIntExtra(FirebaseAnalytics.Param.INDEX, 0);
        if (MobiClean.getInstance().mediaAppModule == null) {
            return;
        }
        AppJunk appJunk = MobiClean.getInstance().mediaAppModule.socialApp.get(this.index);
        if (this.index != 0) {
            if (appJunk.mediaJunkData.get(0).totSize > 0) {
                ImageFragment imageFragment = new ImageFragment();
                viewPagerAdapter.b(imageFragment, "" + getString(R.string.mbc_photos));
                this.tabLayout.setVisibility(View.GONE);
            }
        } else {
            VideoFragment videoFragment = new VideoFragment();
            viewPagerAdapter.b(videoFragment, "" + getString(R.string.videos));
            ImageFragment imageFragment2 = new ImageFragment();
            viewPagerAdapter.b(imageFragment2, "" + getString(R.string.mbc_photos));
            AudioFragment audioFragment = new AudioFragment();
            viewPagerAdapter.b(audioFragment, "" + getString(R.string.audios));
            DOCFragment dOCFragment = new DOCFragment();
            viewPagerAdapter.b(dOCFragment, "" + getString(R.string.documents));
            GIFFragment gIFFragment = new GIFFragment();
            viewPagerAdapter.b(gIFFragment, "" + getString(R.string.gifs));
        }
        viewPager.setAdapter(viewPagerAdapter);
    }


    public void showConfirmDialog() {
        if (Util.isConnectingToInternet(this) && !Util.isAdsFree(this)) {
            if (getIntent().getBooleanExtra("FROMHOME", false) && !getIntent().getBooleanExtra("WHATS", false)) {
                TextView textView = this.ads_title_deletion;
                textView.setText("" + getString(R.string.mbc_turbo_cleaner));
            } else {
                TextView textView2 = this.ads_title_deletion;
                textView2.setText("" + getString(R.string.mbc_turbo_cleaner_social));
            }
            TextView textView3 = this.ads_message_deletion;
            textView3.setText("" + getResources().getString(R.string.mbc_delete_large_file));
            this.k.setVisibility(View.GONE);
            this.l.setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_social_cleaner));
            return;
        }
        Util.appendLogmobiclean(TAG, "showConfirmDialog--- clean item ", GlobalData.FILE_NAME);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
        }
        dialog.setContentView(R.layout.backpress_ads_screen_deletion);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(-1, -1);
        dialog.getWindow().setGravity(17);
        if (getIntent().getBooleanExtra("FROMHOME", false)) {
            ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_turbo_cleaner));
            ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_social_cleaner));
        } else {
            ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_turbo_cleaner_social));
            ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_junk_cleaner));
        }
        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(getResources().getString(R.string.mbc_delete_large_file));
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SocialMediaFilesTabScreen.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_countinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SocialMediaFilesTabScreen.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
                if (MobiClean.getInstance().mediaAppModule == null) {
                    return;
                }
                new CleanSocialData().execute(new String[0]);
            }
        });
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        Util.appendLogmobiclean("SocialScanResultAct", "CalculateToalSizeTask---AsyncTask---onPreExecute", GlobalData.FILE_NAME);
        GlobalData.backPressedResult = true;
        try {
            MobiClean.getInstance().mediaAppModule.socialApp.get(GlobalData.APP_INDEX).selectedCount = 0L;
            MobiClean.getInstance().mediaAppModule.socialApp.get(GlobalData.APP_INDEX).selectedSize = 0L;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false)) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
            return;
        }
        finish();
    }

    @Override
    public void onClick(View view) {
        if (multipleClicked()) {
            return;
        }
        if (view.getId() == R.id.ads_btn_cancel) {
            this.k.setVisibility(View.GONE);
            this.j.setVisibility(View.VISIBLE);
        }
        if (view.getId() == R.id.ads_btn_countinue) {
            MobiClean.getInstance().socialModule = null;
            try {
                MobiClean.getInstance().mediaAppModule.socialApp.get(GlobalData.APP_INDEX).selectedCount = 0L;
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.runFinalization();
            Runtime.getRuntime().runFinalization();
            System.gc();
            RelativeLayout relativeLayout = this.k;
            if (relativeLayout != null && relativeLayout.getVisibility() == View.VISIBLE) {
                this.j.setVisibility(View.VISIBLE);
                this.k.setVisibility(View.GONE);
            }
            if (getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false)) {
                finish();
                startActivity(new Intent(this, HomeActivity.class));
            } else {
                finish();
            }
        }
        if (view.getId() == R.id.ads_btn_countinue_deletion) {
            try {
                MobiClean.getInstance().mediaAppModule.socialApp.get(GlobalData.APP_INDEX).selectedCount = 0L;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            RelativeLayout relativeLayout2 = this.l;
            if (relativeLayout2 != null && relativeLayout2.getVisibility() == View.VISIBLE) {
                this.j.setVisibility(View.VISIBLE);
                this.l.setVisibility(View.GONE);
            }
            if (MobiClean.getInstance().mediaAppModule == null) {
                return;
            }
            new CleanSocialData().execute(new String[0]);
        }
        if (view.getId() == R.id.ads_btn_cancel_deletion) {
            this.j.setVisibility(View.VISIBLE);
            this.l.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.social_media_tab_screen);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ((TextView) findViewById(R.id.toolbar_title)).setText("" + getIntent().getStringExtra("NAME"));
        getids();
        try {
            setupViewPager(this.viewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.tabLayout.setupWithViewPager(this.viewPager);
        long longExtra = getIntent().getLongExtra("SIZE", 0L);
        TextView textView = (TextView) findViewById(R.id.junkdisplay_sizetv_unit);
        String convertBytes_unit = Util.convertBytes_unit(longExtra);
        textView.setText("" + convertBytes_unit);
        findViewById(R.id.btncleannow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (MobiClean.getInstance().mediaAppModule.socialApp.get(GlobalData.APP_INDEX).selectedCount == 0) {
                        SocialMediaFilesTabScreen socialMediaFilesTabScreen = SocialMediaFilesTabScreen.this;
                        Toast.makeText(socialMediaFilesTabScreen, "" + SocialMediaFilesTabScreen.this.getResources().getString(R.string.mbc_at_leastone), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (Exception unused) {
                }
                try {
                    Toast.makeText(SocialMediaFilesTabScreen.this, "there", Toast.LENGTH_SHORT).show();
                    SocialMediaFilesTabScreen.this.showConfirmDialog();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        clearNotification(new int[]{832});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.social_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_sortByName) {
            MobiClean.getInstance().mediaAppModule.socialApp.get(this.index).sort(0);
            refresh();
        } else if (menuItem.getItemId() == R.id.action_sortBySize) {
            MobiClean.getInstance().mediaAppModule.socialApp.get(this.index).sort(1);
            refresh();
        } else if (menuItem.getItemId() == R.id.action_sortByDate) {
            MobiClean.getInstance().mediaAppModule.socialApp.get(this.index).sort(2);
            refresh();
        } else {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
