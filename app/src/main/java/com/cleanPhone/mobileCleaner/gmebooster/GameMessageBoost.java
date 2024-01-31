package com.cleanPhone.mobileCleaner.gmebooster;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.github.silvestrpredko.dotprogressbar.DotProgressBarBuilder;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.jackandphantom.circularprogressbar.CircleProgressbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class GameMessageBoost extends AppCompatActivity {
    private Drawable applicationIcon;
    private CircleImageView circleImageView;
    private CircleProgressbar circleProgressbar;
    private int deviceHeight;
    private int deviceWidth;
    private RelativeLayout.LayoutParams frame_progress_bar_params_left;
    private RelativeLayout.LayoutParams frame_progress_bar_params_right;
    public ImageView h;
    private String pkg;
    private FrameLayout progressBarContainer_left;
    private FrameLayout progressBarContainer_right;
    private TextView tv_boosting;
    private TextView tv_gameName;
    int admob = 2;

    View space;
    private TextView tvsize;

    private void blinkAnimation() {
        this.h.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink_animation));
    }

    private void dotAnimation() {
        leftDotAnimation();
        rightDotAnimation();
    }

    public void finishAnimation() {
        this.circleImageView.setBorderWidth(0);
        this.circleProgressbar.clearAnimation();
        this.circleProgressbar.setVisibility(View.GONE);
        this.tv_boosting.setVisibility(View.GONE);
        this.progressBarContainer_left.setVisibility(View.GONE);
        this.progressBarContainer_right.setVisibility(View.GONE);
        this.h.clearAnimation();
        this.h.setVisibility(View.GONE);
    }

    private void getId() {
        this.circleImageView = (CircleImageView) findViewById(R.id.profile_image);
        this.h = (ImageView) findViewById(R.id.iv_blink);
        this.circleProgressbar = (CircleProgressbar) findViewById(R.id.circularprogressbar);
        this.progressBarContainer_left = (FrameLayout) findViewById(R.id.left_container);
        this.progressBarContainer_right = (FrameLayout) findViewById(R.id.right_container);
        this.tv_gameName = (TextView) findViewById(R.id.tv_gameName);
        this.tv_boosting = (TextView) findViewById(R.id.tv_boosting);
    }


    public void launchGame() {
        try {
            Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(this.pkg);
            finish();
            startActivity(launchIntentForPackage);
        } catch (Exception e) {
            Toast.makeText(this, "" + getResources().getString(R.string.mbc_not_installed), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void leftDotAnimation() {
        DotProgressBarBuilder dotProgressBarBuilder = new DotProgressBarBuilder(this);
        dotProgressBarBuilder.setStartColor(getResources().getColor(R.color.white)).setEndColor(getResources().getColor(R.color.white)).setAnimationDirection(DotProgressBar.RIGHT_DIRECTION).setDotAmount(3);
        this.progressBarContainer_left.addView(dotProgressBarBuilder.build(), this.frame_progress_bar_params_left);
    }

    private void progressAnimation() {
        this.circleProgressbar.setForegroundProgressColor(ContextCompat.getColor(this, R.color.white));
        this.circleProgressbar.setBackgroundProgressColor(ContextCompat.getColor(this, R.color.colorPrimary));
        this.circleProgressbar.setBackgroundProgressWidth((this.deviceWidth * 2) / 100);
        this.circleProgressbar.setForegroundProgressWidth((this.deviceWidth * 2) / 100);
        this.circleProgressbar.setRoundedCorner(true);
        this.circleProgressbar.setClockwise(true);
        this.circleProgressbar.setProgressWithAnimation(65.0f);
        rotate();
    }

    private void rightDotAnimation() {
        DotProgressBarBuilder dotProgressBarBuilder = new DotProgressBarBuilder(this);
        dotProgressBarBuilder.setStartColor(getResources().getColor(R.color.white)).setEndColor(getResources().getColor(R.color.white)).setAnimationDirection(DotProgressBar.RIGHT_DIRECTION).setDotAmount(3);
        this.progressBarContainer_right.addView(dotProgressBarBuilder.build(), this.frame_progress_bar_params_right);
    }

    private void rotate() {
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(5000L);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        this.circleProgressbar.startAnimation(rotateAnimation);
    }


    public void scaleUpAnimation() {
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_fade_out);
        loadAnimation.setDuration(300L);
        loadAnimation.setFillAfter(true);
        loadAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                GameMessageBoost.this.launchGame();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                GameMessageBoost gameMessageBoost = GameMessageBoost.this;
                Toast.makeText(gameMessageBoost, "" + GameMessageBoost.this.getResources().getString(R.string.mbc_game_boosted), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.frame).startAnimation(loadAnimation);
    }

    private void setDimension() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.h.getLayoutParams();
        layoutParams.width = (this.deviceWidth * 16) / 100;
        layoutParams.height = (this.deviceHeight * 16) / 100;
        this.h.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.progressBarContainer_left.getLayoutParams();
        this.frame_progress_bar_params_left = layoutParams2;
        layoutParams2.width = (this.deviceWidth * 10) / 100;
        layoutParams2.height = (this.deviceHeight * 10) / 100;
        this.progressBarContainer_left.setLayoutParams(layoutParams2);
        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) this.progressBarContainer_right.getLayoutParams();
        this.frame_progress_bar_params_right = layoutParams3;
        layoutParams3.width = (this.deviceWidth * 10) / 100;
        layoutParams3.height = (this.deviceHeight * 10) / 100;
        this.progressBarContainer_right.setLayoutParams(layoutParams3);
        RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) this.tv_boosting.getLayoutParams();
        layoutParams4.setMargins(0, 0, 0, (this.deviceHeight * 12) / 100);
        this.tv_boosting.setLayoutParams(layoutParams4);
        RelativeLayout.LayoutParams layoutParams5 = (RelativeLayout.LayoutParams) this.tv_gameName.getLayoutParams();
        layoutParams5.setMargins(0, (this.deviceHeight * 8) / 100, 0, 0);
        this.tv_gameName.setLayoutParams(layoutParams5);
    }

    private void setDrawable() {
        this.pkg = getIntent().getStringExtra("PKG");
        try {
            this.applicationIcon = getPackageManager().getApplicationIcon(this.pkg);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        this.circleImageView.setBorderWidth((int) ((this.deviceHeight * 1.1d) / 100.0d));
        try {
            this.circleImageView.setImageDrawable(this.applicationIcon);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_game_boost_message);
        Util.saveScreen(getClass().getName(), this);
        getId();

        setDimension();
        progressAnimation();
        blinkAnimation();
        dotAnimation();
        setDrawable();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GameMessageBoost.this.finishAnimation();
                GameMessageBoost.this.scaleUpAnimation();
            }
        }, 4000L);
    }
}
