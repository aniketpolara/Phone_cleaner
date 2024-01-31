package com.cleanPhone.mobileCleaner.notifimanager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.GlobalData;


public class SettingOverlayScreen extends Activity {
    private Context context;
    private int deviceHeight;
    private int deviceWidth;

    private void setdimenstion() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_setting_overlay_screen);
        this.context = this;
        setdimenstion();
        String stringExtra = getIntent().getStringExtra("PERM_TEXT");
        ((TextView) findViewById(R.id.tv_perm)).setText("" + stringExtra);
        final ImageView imageView = (ImageView) findViewById(R.id.fingerimg);
        imageView.setVisibility(View.GONE);
        findViewById(R.id.parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingOverlayScreen.this.finish();
            }
        });
        findViewById(R.id.permission_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingOverlayScreen.this.finish();
            }
        });
        final SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.toggle_permission);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            }
        });
        switchCompat.setClickable(false);
        switchCompat.setFocusable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switchCompat.setChecked(true);
                imageView.setVisibility(View.VISIBLE);
                TranslateAnimation translateAnimation = new TranslateAnimation(((-SettingOverlayScreen.this.deviceWidth) * 8) / 100, 0.0f, (SettingOverlayScreen.this.deviceHeight * 3) / 100, (SettingOverlayScreen.this.deviceHeight * 3) / 100);
                translateAnimation.setDuration(500L);
                translateAnimation.setFillAfter(true);
                imageView.startAnimation(translateAnimation);
            }
        }, 2000L);
        GlobalData.fromDatausageSetting = true;
    }
}
