package com.cleanPhone.mobileCleaner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.utility.BuildConfig;

public class StartActivity extends AppCompatActivity {
    public RelativeLayout share, security, rate;
    private RelativeLayout start_now;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        this.rate = (RelativeLayout) findViewById(R.id.rl_rate);
        this.share = (RelativeLayout) findViewById(R.id.rl_share);
        this.security = (RelativeLayout) findViewById(R.id.rl_security);
        this.start_now = (RelativeLayout) findViewById(R.id.start_now);


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


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wifi Calling And Wifi Tethering");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                }
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    return;
                } catch (ActivityNotFoundException unused2) {
                    Toast.makeText(StartActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://phonecleaneranti.blogspot.com/2023/09/phone-cleaner-virus-detector.html")));
            }
        });

        this.start_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                            Intent intent = new Intent(StartActivity.this, RealTimeActivity.class);
                            startActivity(intent);
                            finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
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

        ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_dialog_logo));
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.str_exit_title));
        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(getResources().getString(R.string.str_exit_app));
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
//                startActivity(new Intent(StartActivity.this, ThankYouActivity.class));
                finishAffinity();
            }
        });
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}