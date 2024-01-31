package com.cleanPhone.mobileCleaner;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import com.cleanPhone.mobileCleaner.animate.CircleProgressView;
import com.cleanPhone.mobileCleaner.animate.JunkData;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.similerphotos.MySharedPreference;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.util.ArrayList;
import java.util.Collections;


public class JunkCleaningScreen extends ParentActivity implements View.OnClickListener, JunkData.ProUpdate {
    private MobiClean app;
    private CircleProgressView circleProgressView;
    private Context context;
    private RelativeLayout iv_circle_bg;
    private LinearLayout parent_layout;
    private TextView tv_filecount;
    private TextView tv_status;

    private void init() {
        this.context = this;
        this.circleProgressView = (CircleProgressView) findViewById(R.id.circleView);
        this.tv_status = (TextView) findViewById(R.id.tv_status);
        this.tv_filecount = (TextView) findViewById(R.id.tv_filecount);
        this.iv_circle_bg = (RelativeLayout) findViewById(R.id.circulerlayout);
        this.app = MobiClean.getInstance();
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_mid_laysss);
        this.parent_layout = (LinearLayout) findViewById(R.id.parent_layout);
    }

    private void setDeviceDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.iv_circle_bg.getLayoutParams();
        int i2 = (i * 45) / 100;
        layoutParams.width = i2;
        layoutParams.height = i2;
        this.iv_circle_bg.setLayoutParams(layoutParams);
        int i3 = (i * 2) / 100;
        this.iv_circle_bg.setPadding(i3, i3, i3, i3);
        this.circleProgressView.setBarWidth(i3);
        this.circleProgressView.setRimWidth(i3);
        this.circleProgressView.setRimColor(Color.parseColor("#e1ebf3"));
        this.circleProgressView.setBarColor(Color.parseColor("#1c58b1"));
        this.circleProgressView.setTextSize((displaymetrics.heightPixels * 8) / 100);
        this.circleProgressView.setTextColor(Color.parseColor("#1c58b1"));
    }

    private void setFontSize() {
        MySharedPreference.getLngIndex(this);
    }

    private void setKeysList() {
        String[] stringArray;
        if (this.app.junkData == null) {
            return;
        }
        if (Build.VERSION.SDK_INT > 19) {
            stringArray = getResources().getStringArray(R.array.junkmodulesaboven);
            if (this.app.junkData.isAlreadyBoosted(this)) {
                stringArray = getResources().getStringArray(R.array.junkmodulesabovenlessboost);
            }
        } else {
            stringArray = getResources().getStringArray(R.array.junkmodules);
            if (this.app.junkData.isAlreadyBoosted(this)) {
                stringArray = getResources().getStringArray(R.array.junkmodulesaboveseven);
            }
        }
        this.app.junkData.listDataHeader = new ArrayList<>();
        this.app.junkData.listDataHeader.clear();
        Collections.addAll(this.app.junkData.listDataHeader, stringArray);
    }

    private void setLayoutParams() {
    }

    private void switchScreen() {
        try {
            JunkData junkData = this.app.junkData;
            long j = junkData.totalsize;
            if (j > 0) {
                long sizeAsPerOS = junkData.getSizeAsPerOS(j);
                if (sizeAsPerOS == 0) {
                    if (this.app.junkData.totalemptyfoldersDeleted > 0) {
                        String replace = getResources().getString(R.string.mbc_after_upgrade_msg).replace("DO_NOT_TRANSLATE", "%s");
                        String format = String.format(replace, "" + this.app.junkData.totalemptyfoldersDeleted);
                        if (this.app.junkData.totalemptyfoldersDeleted == 1) {
                            format = getResources().getString(R.string.mbc_cleanonefolder);
                        }
                        Intent intent = new Intent(this, CommonResultActivity.class);
                        intent.putExtra("DATA", "" + format);
                        intent.putExtra("TYPE", "JUNK");
                        intent.putExtra(GlobalData.REDIRECTNOTI, true);
                        finish();
                        startActivity(intent);
                        return;
                    }
                    Intent intent2 = new Intent(this, CommonResultActivity.class);
                    intent2.putExtra("DATA", "" + getResources().getString(R.string.mbc_cleaning_aborted));
                    intent2.putExtra("TYPE", "JUNK");
                    intent2.putExtra(GlobalData.REDIRECTNOTI, true);
                    finish();
                    startActivity(intent2);
                    return;
                }
                Intent intent3 = new Intent(this, CommonResultActivity.class);
                intent3.putExtra("DATA", "" + Util.convertBytes(sizeAsPerOS));
                intent3.putExtra("TYPE", "JUNK");
                intent3.putExtra(GlobalData.REDIRECTNOTI, true);
                finish();
                startActivity(intent3);
            } else if (junkData.totalemptyfoldersDeleted > 0) {
                String replace2 = getResources().getString(R.string.mbc_after_upgrade_msg).replace("DO_NOT_TRANSLATE", "%s");
                String format2 = String.format(replace2, "" + this.app.junkData.totalemptyfoldersDeleted);
                if (this.app.junkData.totalemptyfoldersDeleted == 1) {
                    format2 = getResources().getString(R.string.mbc_cleanonefolder);
                }
                Intent intent4 = new Intent(this, CommonResultActivity.class);
                intent4.putExtra("DATA", "" + format2);
                intent4.putExtra("TYPE", "JUNK");
                intent4.putExtra(GlobalData.REDIRECTNOTI, true);
                finish();
                startActivity(intent4);
            } else {
                Intent intent5 = new Intent(this, CommonResultActivity.class);
                intent5.putExtra("DATA", "" + Util.convertBytes(this.app.junkData.totalsize));
                intent5.putExtra("TYPE", "JUNK");
                intent5.putExtra(GlobalData.REDIRECTNOTI, true);
                finish();
                startActivity(intent5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        Util.appendLogmobiclean("", " backpressed ", "");
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
        }
        dialog.setContentView(R.layout.new_dialog_junk_cancel);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(-1, -1);
        dialog.getWindow().setGravity(17);
        View space;

         ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_junk_cleaner));
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_junk_title));
        if (MySharedPreference.getLngIndex(this) != 0) {
            ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(String.format(getResources().getString(R.string.mbc_simple_back_press), getResources().getString(R.string.mbc_junk_result_txt)));
        } else {
            ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(String.format(getResources().getString(R.string.mbc_simple_back_press), getResources().getString(R.string.mbc_skiptext)));
        }
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (JunkCleaningScreen.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (JunkCleaningScreen.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
                Util.appendLogmobiclean("", " backpressed pDialog finish", "");
                JunkCleaningScreen.this.app.junkData.cancelCleaning();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ads_btn_cancel) {
            try {
                this.parent_layout.setVisibility(View.VISIBLE);
                findViewById(R.id.layout_two).setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (view.getId() == R.id.ads_btn_countinue) {
            this.parent_layout.setVisibility(View.VISIBLE);
            findViewById(R.id.layout_two).setVisibility(View.GONE);
            Util.appendLogmobiclean("", " backpressed pDialog finish", "");
            this.app.junkData.cancelCleaning();
        } else {
            this.app.junkData.deleteJunk(this, this, true);
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_junk_reward_screen);

        Log.e("TAG", "onCreate:  JunkCleaningScreen-----");
        init();
        this.context = this;
        GlobalData.SETAPPLAnguage(this);
        setDeviceDimensions();
        setLayoutParams();
        setFontSize();
        setKeysList();
        new Handler().postDelayed(new Runnable() {
            @Override // java.lang.Runnable
            public void run() {
                try {
                    JunkData junkData = JunkCleaningScreen.this.app.junkData;
                    JunkCleaningScreen junkCleaningScreen = JunkCleaningScreen.this;
                    junkData.deleteJunk(junkCleaningScreen, junkCleaningScreen, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000L);
        sendAnalytics("SCREEN_VISIT_F_" + getClass().getSimpleName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (this.isNotShown) {
//            return;
//        }
        switchScreen();
    }

    @Override
    public void onUpdate(long j, long j2, long j3, long j4) {
        if (j == -1) {
            String string = getString(R.string.mbc_remain_junk_file);
            StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(" ");
            JunkData junkData = this.app.junkData;
            sb.append(junkData.totselectedTodelete - junkData.totalDeletedCount);
            String sb2 = sb.toString();
            new SpannableString(sb2).setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), sb2.toLowerCase().indexOf(string.toLowerCase()), sb2.toLowerCase().indexOf(string.toLowerCase()) + string.length(), 0);
            String str = getString(R.string.mbc_app_backup_deleted) + " ";
            JunkData junkData2 = this.app.junkData;
            long j5 = junkData2.totSelectedToDeleteSize - junkData2.totalDeletedSize;
            String str2 = str + " " + Util.convertBytes(j5);
            new SpannableString(str2).setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), str2.toLowerCase().indexOf(str.toLowerCase()), str2.toLowerCase().indexOf(str.toLowerCase()) + str.length(), 0);
            JunkData junkData3 = this.app.junkData;
            long j6 = junkData3.totalDeletedSize;
            long j7 = junkData3.totalsize;
            if (j6 > j7) {
                junkData3.totalDeletedSize = j7;
            }
            int i = junkData3.totalDeletedCount;
            int i2 = junkData3.totselectedTodelete;
            if (i > i2) {
                junkData3.totalDeletedCount = i2;
            }
        } else if (j == -2) {
            switchScreen();
        } else {
            if (j == -3) {
                finish();
            }
            JunkData junkData4 = this.app.junkData;
            try {
                long j8 = junkData4.totalsize;
                if (j2 > j8) {
                    j2 = j8;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            int i3 = junkData4.totselectedTodelete;
            if (j3 > i3) {
                j3 = i3;
            }
            this.tv_status.setText("" + Util.convertBytes(j2) + DialogConfigs.DIRECTORY_SEPERATOR + Util.convertBytes(j4) + " ");
            this.tv_filecount.setText("" + j3 + DialogConfigs.DIRECTORY_SEPERATOR + this.app.junkData.totselectedTodelete + " " + getString(R.string.mbc_junk_items_cleaned));
            this.circleProgressView.setValue((float) j);
        }
    }
}
