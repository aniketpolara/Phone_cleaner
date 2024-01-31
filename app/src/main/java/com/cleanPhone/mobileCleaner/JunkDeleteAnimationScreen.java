package com.cleanPhone.mobileCleaner;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.cleanPhone.mobileCleaner.utility.DetermineTextSize;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.TimerView;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class JunkDeleteAnimationScreen extends ParentActivity {
    public TextView j;
    public TextView k;
    public TextView l;
    int click = 0;
    int numOfClick = 3;
    AdRequest adRequest;
    int admob = 2;
    View space;
    private LinearLayout clippedLayout;
    private ImageView imgBin;
    private boolean isfirsttime;
    private InterstitialAd mInterstitialAd;
    private float mmmmmm;
    private String size;
    private TimerView tView;
    private TextView txtSize;
    private String type;

    private void init() {

        this.clippedLayout = findViewById(R.id.delete_layout);
        this.imgBin = findViewById(R.id.delete_icon_junkanim);
        this.txtSize = findViewById(R.id.tv_size_junkdelete);
        this.l = findViewById(R.id.cpu_temp_text);
        this.j = findViewById(R.id._temp);
        this.k = findViewById(R.id._usage);
        this.tView = findViewById(R.id.tView);
        if (this.type.equalsIgnoreCase("COOLER")) {
            this.imgBin.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_anim_cooler));
        } else if (this.type.equalsIgnoreCase("Boost")) {
            this.imgBin.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_anim_boost));
        } else if (this.type.equalsIgnoreCase("Social")) {
            this.imgBin.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_anim_turbo_cleaner));
        } else if (this.type.equalsIgnoreCase("AV")) {
            this.imgBin.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shield_white));
        }
    }

    private void loadInterstitialORnot() {
        String str = this.type;
        str.hashCode();
        char c2 = 65535;
        switch (str.hashCode()) {
            case -1813183603:
                if (str.equals("Social")) {
                    c2 = 0;
                    break;
                }
                break;
            case -1382453013:
                if (str.equals("NOTIFICATION")) {
                    c2 = 1;
                    break;
                }
                break;
            case 2288712:
                if (str.equals("JUNK")) {
                    c2 = 2;
                    break;
                }
                break;
            case 63384451:
                if (str.equals("BOOST")) {
                    c2 = 3;
                    break;
                }
                break;
            case 1993540022:
                if (str.equals("COOLER")) {
                    c2 = 4;
                    break;
                }
                break;
        }
        switch (c2) {
            case 0:
            case 1:
                switchScreen();
                return;
            case 2:
                try {
                    switchScreen();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            case 3:
                try {
                    switchScreen();
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            case 4:
                Log.e("!!!!!!CPU", "wwwww" + this.type);
                sendAnalytics("SCREEN_VISIT_F_CPU_COOLER");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        JunkDeleteAnimationScreen.this.switchScreen();
                    }
                }, 3000L);
                return;
            default:
                try {
                    switchScreen();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
        }
    }


    public float readUsage() {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("/proc/stat", "r");
            String[] split = randomAccessFile.readLine().split(" +");
            long parseLong = Long.parseLong(split[4]);
            long parseLong2 = Long.parseLong(split[2]) + Long.parseLong(split[3]) + Long.parseLong(split[5]) + Long.parseLong(split[6]) + Long.parseLong(split[7]) + Long.parseLong(split[8]);
            try {
                Thread.sleep(360L);
            } catch (Exception unused) {
            }
            randomAccessFile.seek(0L);
            String readLine = randomAccessFile.readLine();
            randomAccessFile.close();
            String[] split2 = readLine.split(" +");
            long parseLong3 = Long.parseLong(split2[4]);
            long parseLong4 = Long.parseLong(split2[2]) + Long.parseLong(split2[3]) + Long.parseLong(split2[5]) + Long.parseLong(split2[6]) + Long.parseLong(split2[7]) + Long.parseLong(split2[8]);
            float f = ((float) (parseLong4 - parseLong2)) / ((float) ((parseLong4 + parseLong3) - (parseLong2 + parseLong)));
            this.mmmmmm = f;
            return f;
        } catch (IOException e2) {
//            e = e2;
        }
        return 0;
    }

    private void setAnimation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JunkDeleteAnimationScreen.this.clippedLayout.setVisibility(View.VISIBLE);
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(JunkDeleteAnimationScreen.this.imgBin, "rotationY", 0.0f, 180.0f);
                ofFloat.setDuration(1500L);
                ofFloat.setInterpolator(new AccelerateDecelerateInterpolator());
                ofFloat.start();
                if (JunkDeleteAnimationScreen.this.type.equalsIgnoreCase("junk")) {
                    JunkDeleteAnimationScreen.this.txtSize.setVisibility(View.VISIBLE);
                    JunkDeleteAnimationScreen.this.txtSize.setAnimation(AnimationUtils.loadAnimation(JunkDeleteAnimationScreen.this.getApplicationContext(), R.anim.zoom_in));
                    JunkDeleteAnimationScreen junkDeleteAnimationScreen = JunkDeleteAnimationScreen.this;
                    junkDeleteAnimationScreen.l.setText(junkDeleteAnimationScreen.getResources().getString(R.string.mbc_space_recover));
                    if (JunkDeleteAnimationScreen.this.size == null || !JunkDeleteAnimationScreen.this.size.toLowerCase().contains("folder")) {
                        return;
                    }
                    JunkDeleteAnimationScreen.this.l.setVisibility(View.GONE);
                } else if (JunkDeleteAnimationScreen.this.type.equalsIgnoreCase("Social")) {
                    JunkDeleteAnimationScreen.this.txtSize.setVisibility(View.VISIBLE);
                    JunkDeleteAnimationScreen.this.txtSize.setAnimation(AnimationUtils.loadAnimation(JunkDeleteAnimationScreen.this.getApplicationContext(), R.anim.zoom_in));
                    JunkDeleteAnimationScreen junkDeleteAnimationScreen2 = JunkDeleteAnimationScreen.this;
                    junkDeleteAnimationScreen2.l.setText(junkDeleteAnimationScreen2.getResources().getString(R.string.mbc_space_recover));
                } else if (JunkDeleteAnimationScreen.this.type.equalsIgnoreCase(JunkCleanScreen.BOOSTFILES)) {
                    JunkDeleteAnimationScreen.this.txtSize.setVisibility(View.VISIBLE);
                    JunkDeleteAnimationScreen.this.txtSize.setAnimation(AnimationUtils.loadAnimation(JunkDeleteAnimationScreen.this.getApplicationContext(), R.anim.zoom_in));
                    TextView textView = JunkDeleteAnimationScreen.this.l;
                    textView.setText("" + JunkDeleteAnimationScreen.this.getResources().getString(R.string.mbc_phone_boosted));
                } else if (JunkDeleteAnimationScreen.this.type.equalsIgnoreCase("AV")) {
                    JunkDeleteAnimationScreen.this.txtSize.setVisibility(View.VISIBLE);
                    JunkDeleteAnimationScreen.this.txtSize.setAnimation(AnimationUtils.loadAnimation(JunkDeleteAnimationScreen.this.getApplicationContext(), R.anim.zoom_in));
                } else if (JunkDeleteAnimationScreen.this.type.equalsIgnoreCase("NOTIFICATION")) {
                    JunkDeleteAnimationScreen.this.txtSize.setVisibility(View.VISIBLE);
                    if (JunkDeleteAnimationScreen.this.size.equalsIgnoreCase("1")) {
                        JunkDeleteAnimationScreen.this.txtSize.setText(JunkDeleteAnimationScreen.this.getResources().getString(R.string.mbc_cleaning));
                    } else {
                        TextView textView2 = JunkDeleteAnimationScreen.this.txtSize;
                        textView2.setText("" + JunkDeleteAnimationScreen.this.size + " " + JunkDeleteAnimationScreen.this.getString(R.string.mbc_notifications));
                    }
                    JunkDeleteAnimationScreen.this.txtSize.setAnimation(AnimationUtils.loadAnimation(JunkDeleteAnimationScreen.this.getApplicationContext(), R.anim.zoom_in));
                } else {
                    JunkDeleteAnimationScreen.this.findViewById(R.id.tv_size_junkdelete).setVisibility(View.GONE);
                    JunkDeleteAnimationScreen.this.findViewById(R.id.cpucollerfirst_delete).setVisibility(View.VISIBLE);
                    TextView textView3 = JunkDeleteAnimationScreen.this.j;
                    textView3.setText("" + JunkDeleteAnimationScreen.this.size);
                    JunkDeleteAnimationScreen.this.txtSize.setVisibility(View.INVISIBLE);
                }
            }
        }, 1000L);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JunkDeleteAnimationScreen.this.tView.start(1000);
            }
        }, 200L);
    }


    public void switchScreen() {
        Log.e("----------MMMMM", "switchScreen " + this.type);
        if (this.type.equalsIgnoreCase("COOLER")) {
            Intent intent = new Intent(this, CommonResultActivity.class);
            intent.putExtra("DATA", "" + this.size);
            intent.putExtra(GlobalData.REDIRECTNOTI, true);
            intent.putExtra("TYPE", "COOLER");
            startActivity(intent);
            finish();


        } else if (this.type.equalsIgnoreCase("JUNK")) {
            Intent intent2 = new Intent(this, CommonResultActivity.class);
            this.isfirsttime = false;
            intent2.putExtra("DATA", "" + this.size);
            intent2.putExtra("TYPE", "JUNK");
            intent2.putExtra(GlobalData.REDIRECTNOTI, true);

                startActivity(intent2);
                finish();


            Log.d("TAG", "The interstitial wasn't loaded yet.");
        } else if (this.type.equalsIgnoreCase("AV")) {
            Intent intent3 = new Intent(this, CommonResultActivity.class);
            intent3.putExtra("DATA", "" + this.size);
            intent3.putExtra("TYPE", "AV");
                startActivity(intent3);
                finish();


        } else if (this.type.equalsIgnoreCase("NOTIFICATION")) {
            Intent intent4 = new Intent(this, CommonResultActivity.class);
            intent4.putExtra("DATA", "" + this.size);
            intent4.putExtra("TYPE", "NOTIFICATION");
            intent4.putExtra(GlobalData.REDIRECTNOTI, true);
                startActivity(intent4);
                finish();


        } else if (this.type.equalsIgnoreCase("Social")) {
            Intent intent5 = new Intent(this, CommonResultActivity.class);
            intent5.putExtra("DATA", "" + this.size);
            intent5.putExtra(GlobalData.REDIRECTNOTI, getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false));
            intent5.putExtra("FROMNOTI", getIntent().getBooleanExtra("FROMNOTI", false));
            intent5.putExtra("TYPE", "SOCIAL");

                startActivity(intent5);
                finish();



        } else {
            Intent intent6 = new Intent(this, CommonResultActivity.class);
            this.isfirsttime = false;
            intent6.putExtra("DATA", "" + this.size);
            intent6.putExtra("TYPE", "BOOST");
            intent6.putExtra(GlobalData.REDIRECTNOTI, true);
                startActivity(intent6);
                finish();


        }
    }


    @Override
    public void onBackPressed() {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_junk_delete_animation_screen);

        this.isfirsttime = true;
        try {
            SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(this);
            sharedPrefUtil.saveString(SharedPrefUtil.LASTTIMEJUNKCLEANED, System.currentTimeMillis() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.type = getIntent().getStringExtra("TYPE");
        this.size = getIntent().getStringExtra("DATA");
        init();


        findViewById(R.id.activity_junk_delete_animation_screen).setPadding(0, (displaymetrics.heightPixels * 20) / 100, 0, 0);
        TextView textView = this.txtSize;
        textView.setTextSize(0, DetermineTextSize.determineTextSize(textView.getTypeface(), (displaymetrics.heightPixels * 5) / 100));
        if (this.type.equalsIgnoreCase("AV")) {
            this.txtSize.setText(this.size);
            this.txtSize.setTextSize(15.0f);
        } else {
            String[] split = this.size.split(" ");
            if (!this.type.equalsIgnoreCase("COOLER")) {
                try {
                    String str = this.size;
                    if (str != null && str.toLowerCase().contains("folder")) {
                        TextView textView2 = this.txtSize;
                        textView2.setText(this.size + "");
                    } else {
                        TextView textView3 = this.txtSize;
                        textView3.setText(Html.fromHtml(split[0] + "<sup><small><small>" + split[1] + "<small></small></sup>"));
                    }
                } catch (Exception e2) {
                    TextView textView4 = this.txtSize;
                    textView4.setText("" + this.size);
                    e2.printStackTrace();
                }
            } else {
                TextView textView5 = this.txtSize;
                textView5.setText("" + this.size);
            }
            Log.e("------", "" + this.size);
        }

        setAnimation();
        new Timer().schedule(new TimerTask() { // from class: com.mobiclean.phoneclean.JunkDeleteAnimationScreen.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                JunkDeleteAnimationScreen.this.readUsage();
                JunkDeleteAnimationScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String replace = new DecimalFormat(".0#").format(JunkDeleteAnimationScreen.this.mmmmmm).replace(".", "");
                            if (replace.startsWith("-")) {
                                JunkDeleteAnimationScreen.this.k.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                            } else {
                                TextView textView9 = JunkDeleteAnimationScreen.this.k;
                                textView9.setText("" + replace);
                            }
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    }
                });
            }
        }, 3000L, 5000L);
        loadInterstitialORnot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
