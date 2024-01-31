package com.cleanPhone.mobileCleaner;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.work.WorkRequest;

import com.facebook.appevents.AppEventsConstants;
import com.cleanPhone.mobileCleaner.antimalware.AVIgnorelist;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.SmallBang;
import com.cleanPhone.mobileCleaner.utility.TimerView;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.JunkListWrapper;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.text.Typography;

public class CommonResultActivity extends ShareActivity implements View.OnClickListener {
    private static final String TAG = "CommonResultVertiseScr";
    private LinearLayout adLayout;
    private Context context;
    private ImageView ic_like_heart;
    private ImageView imgBin;
    private ImageView imgOk;
    private ImageView iv_cirle_layout;
    private SmallBang mSmallBang;

    private float mmmmmm;
    private int notDeleted = 0;
    private boolean redirectTonoti;
    private SharedPrefUtil sharedPrefUtil;
    private TimerView tView;
    private TextView t_cpuuses;
    private Timer timer;
    private LinearLayout toplayout;
    private LinearLayout toplayoutCooler;
    private TextView tv_data;
    private TextView tv_message, tv_result_cpu;
    private TextView tv_temp;
    public TextView tool_title;

    private ArrayList<JunkListWrapper> filterSmall(ArrayList<JunkListWrapper> arrayList) {
        ArrayList<JunkListWrapper> arrayList2 = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).size > 12288) {
                arrayList2.add(arrayList.get(i));
            }
        }
        return arrayList2;
    }

    private void findids() {
        this.imgBin = (ImageView) findViewById(R.id.delete_icon_junkanim);
        this.imgOk = (ImageView) findViewById(R.id.ok_icon_junkanim);
        this.tView = (TimerView) findViewById(R.id.tView);
        this.mSmallBang = SmallBang.attach2Window(this);
        this.tv_temp = (TextView) findViewById(R.id.cpucollerfirst_temp);
        this.t_cpuuses = (TextView) findViewById(R.id.cpucoolerfirst_usage);
        this.tv_data = (TextView) findViewById(R.id.tv_resultdata);
        this.tv_message = (TextView) findViewById(R.id.tv_resultmessage);
        this.tv_result_cpu = (TextView) findViewById(R.id.tv_result_cpu);
        this.toplayoutCooler = (LinearLayout) findViewById(R.id.cpucollerfirst_toplayout);
        this.toplayout = (LinearLayout) findViewById(R.id.upper_layout_top);
        this.tool_title = (TextView) findViewById(R.id.toolbar_title);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.tView.getLayoutParams();
        int i = displaymetrics.widthPixels;
        layoutParams.width = (i * 22) / 100;
        layoutParams.height = (i * 22) / 100;
        this.tView.setLayoutParams(layoutParams);
        ImageView imageView = (ImageView) findViewById(R.id.iv_cirle_layout);
        this.iv_cirle_layout = imageView;
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        int i2 = displaymetrics.widthPixels;
        layoutParams2.width = (i2 * 17) / 100;
        layoutParams2.height = (i2 * 17) / 100;
        this.iv_cirle_layout.setLayoutParams(layoutParams2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((Animatable) CommonResultActivity.this.iv_cirle_layout.getDrawable()).start();
            }
        }, 1000L);
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

    private void redirectToNoti() {
        Log.e(TAG, "" + this.redirectTonoti);
        this.redirectTonoti = getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false);
    }

    private void setAnimation() {
        Util.appendLogmobiclean(TAG, "method:setAnimation ", "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonResultActivity.this.tView.start(700);
            }
        }, 200L);
    }


    private void setTopLayoutAsPerType() {
        String stringExtra = getIntent().getStringExtra("DATA");
        this.toplayout.setVisibility(View.VISIBLE);
        this.toplayoutCooler.setVisibility(View.GONE);
        String stringExtra2 = getIntent().getStringExtra("TYPE");
        Util.appendLogmobiclean(TAG, "method:setTopLayoutAsPerType " + stringExtra2, "");
        stringExtra2.hashCode();
        char c2 = '\uffff';
        switch (stringExtra2.hashCode()) {
            case -1843721363:
                if (stringExtra2.equals("SOCIAL")) {
                    c2 = 0;
                    break;
                }
                break;
            case -1382453013:
                if (stringExtra2.equals("NOTIFICATION")) {
                    c2 = 1;
                    break;
                }
                break;
            case -1293709085:
                if (stringExtra2.equals("Recovered")) {
                    c2 = 2;
                    break;
                }
                break;
            case -1287687863:
                if (stringExtra2.equals("NODOWNLOAD")) {
                    c2 = 3;
                    break;
                }
                break;
            case -306684693:
                if (stringExtra2.equals("DUPLICATE")) {
                    c2 = 4;
                    break;
                }
                break;
            case 2101:
                if (stringExtra2.equals("AV")) {
                    c2 = 5;
                    break;
                }
                break;
            case 2288712:
                if (stringExtra2.equals("JUNK")) {
                    c2 = 6;
                    break;
                }
                break;
            case 63384451:
                if (stringExtra2.equals("BOOST")) {
                    c2 = 7;
                    break;
                }
                break;
            case 183360354:
                if (stringExtra2.equals("COMPRESS")) {
                    c2 = '\b';
                    break;
                }
                break;
            case 256754361:
                if (stringExtra2.equals("JUNK_ALLREADY")) {
                    c2 = '\t';
                    break;
                }
                break;
            case 1993540022:
                if (stringExtra2.equals("COOLER")) {
                    c2 = '\n';
                    break;
                }
                break;
        }
        switch (c2) {
            case 0:
                if (getIntent().getStringExtra("DATA").equalsIgnoreCase(getResources().getString(R.string.mbc_msg_social))) {
                    TextView textView = this.tv_message;
                    tool_title.setText("Social Media Scanning");
                    textView.setText("" + getIntent().getStringExtra("DATA"));
                    GlobalData.loadAds = false;
                    this.tv_data.setVisibility(View.GONE);
                    break;
                } else if (getIntent().getStringExtra("DATA").equalsIgnoreCase(getResources().getString(R.string.mbc_no_junk_media))) {
                    GlobalData.loadAds = false;
                    TextView textView2 = this.tv_message;
                    tool_title.setText("Social Media Scanning");
                    textView2.setText("" + getIntent().getStringExtra("DATA"));
                    this.tv_data.setVisibility(View.GONE);
                    break;
                } else {
                    tool_title.setText("Large Files");
                    this.tv_message.setText(R.string.mbc_space_recover);
                    TextView textView3 = this.tv_data;
                    textView3.setText("" + getIntent().getStringExtra("DATA"));
                    break;
                }
            case 1:
                tool_title.setText("Notification Cleaner");
                this.tv_message.setText(R.string.mbc_notificationcleansuccessful);
                break;
            case 2:
                if (this.notDeleted > 0) {
                    this.tv_message.setText(String.format(getResources().getString(R.string.mbc_deletionmsg_two).replace("DO_NOT_TRANSLATE", "%d"), Integer.valueOf(this.notDeleted)));
                    break;
                } else {
                    tool_title.setText("Large Files");
                    this.tv_message.setText(getResources().getString(R.string.mbc_space_recover));
                    break;
                }
            case 3:
                this.tv_message.setText("");
                break;
            case 4:
                if (this.notDeleted > 0) {
                    tool_title.setText("Large Files");
                    this.tv_message.setText(String.format(getResources().getString(R.string.mbc_deletionmsg_two).replace("DO_NOT_TRANSLATE", "%d"), Integer.valueOf(this.notDeleted)));
                    break;
                } else {
                    tool_title.setText("Large Files");
                    this.tv_message.setText(getString(R.string.mbc_space_recover));
                    break;
                }
            case 5:
                tool_title.setText("Junk Clean");
                TextView textView4 = this.tv_message;
                textView4.setText("" + getIntent().getStringExtra("DATA"));
                this.tv_data.setVisibility(View.GONE);
                break;
            case 6:
                if ((getIntent().getStringExtra("DATA") + "").toLowerCase().contains("folder")) {
                    this.tv_message.setText("");
                    tool_title.setText("Junk Clean");
                    break;
                } else {
                    this.tool_title.setText("Junk Clean");
                    this.tv_message.setText(getString(R.string.mbc_junkcleansuccesful));
                    break;
                }
            case 7:
                if (!getIntent().getStringExtra("DATA").equalsIgnoreCase("2131886549")) {
                    String stringExtra3 = getIntent().getStringExtra("DATA");
                    if (!stringExtra3.equalsIgnoreCase("" + getString(R.string.mbc_boosted)) && !getIntent().getStringExtra("DATA").equalsIgnoreCase("Internet Speed Boosted")) {
                        String stringExtra4 = getIntent().getStringExtra("DATA");
                        if (stringExtra4.equalsIgnoreCase("" + getString(R.string.mbc_one_battery_boost))) {
                            this.tool_title.setText("Battery Saver");
                            this.tv_message.setText(R.string.mbc_one_battery_boost);
                        } else {
                            this.tool_title.setText("Phone Boost");
                            this.tv_message.setText(R.string.mbc_phoneboostsuccesful);
                        }
                        this.tv_data.setVisibility(View.GONE);
                        break;
                    }
                }
                this.tv_message.setText("");
                this.tv_data.setVisibility(View.GONE);
                break;
            case '\b':
                tool_title.setText("Large Files");
                this.tv_message.setText(getResources().getString(R.string.mbc_space_recover));
                break;
            case '\t':
                tool_title.setText("Junk Clean");
                this.tv_message.setText(getString(R.string.mbc_junkalreadyclean));
                break;
            case '\n':
                this.tool_title.setText("CPU Cooler");
                this.tv_message.setText("");
                tv_result_cpu.setVisibility(View.VISIBLE);
                this.tv_result_cpu.setText(getString(R.string.mbc_normaltempdevices));
                this.tv_data.setVisibility(View.INVISIBLE);
                this.iv_cirle_layout.setVisibility(View.GONE);
                this.toplayoutCooler.setVisibility(View.VISIBLE);
                this.tView.setVisibility(View.GONE);
                if (getIntent().getStringExtra("DATA").equalsIgnoreCase(getResources().getString(R.string.mbc_normaltempdevices))) {
                    String string = this.sharedPrefUtil.getString(SharedPrefUtil.AFTERCOOLTEMP);
                    Log.d("positiontemp", string + "");
                    try {
                        if (string.split(String.valueOf((char) Typography.degree))[0].equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                            this.tv_temp.setText("40");
                        } else {
                            this.tv_temp.setText(string.split(String.valueOf((char) Typography.degree))[0]);
                        }
                    } catch (Exception e) {
                        try {
                            this.tv_temp.setText(GlobalData.temperaryTemp.split(" ")[0]);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                } else {
                    TextView textView5 = this.tv_temp;
                    textView5.setText("" + getIntent().getStringExtra("DATA"));
                }
                Timer timer = new Timer();
                this.timer = timer;
                timer.schedule(new TimerTask() { // from class: com.mobiclean.phoneclean.CommonResultActivity.4
                    @Override // java.util.TimerTask, java.lang.Runnable
                    public void run() {
                        CommonResultActivity.this.readUsage();
                        CommonResultActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String replace = new DecimalFormat(".0#").format(CommonResultActivity.this.mmmmmm).replace(",", "").replace(".", "");
                                    if (replace.startsWith("-")) {
                                        CommonResultActivity.this.t_cpuuses.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                                    } else {
                                        TextView textView6 = CommonResultActivity.this.t_cpuuses;
                                        textView6.setText("" + replace);
                                    }
                                } catch (Exception e3) {
                                    e3.printStackTrace();
                                }
                            }
                        });
                    }
                }, 3000L, WorkRequest.MIN_BACKOFF_MILLIS);
                GlobalData.cpu_temperature = this.tv_temp.getText().toString() + "\u2103";
                break;
        }
        String[] split = stringExtra.split(" ");
        if (!stringExtra.equalsIgnoreCase(getResources().getString(R.string.mbc_phone_boosted)) && stringExtra.equalsIgnoreCase("COOLER")) {
            TextView textView6 = this.tv_data;
            textView6.setText(Html.fromHtml(split[0] + "<sup><small><small>" + split[1] + "</small></small></sup>"));
        } else {
            TextView textView7 = this.tv_data;
            textView7.setText("" + stringExtra);
        }
        if (stringExtra2.equalsIgnoreCase("JUNK") && GlobalData.cacheCheckedAboveMarshmallow) {
            this.tv_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonResultActivity.this.startActivity(new Intent(CommonResultActivity.this.context, DeleteCacheActivity.class));
                }
            });
            int i = Build.VERSION.SDK_INT;
            if (i >= 23) {
                if (MobiClean.getInstance().junkData != null && !MobiClean.getInstance().junkData.sys_cache_deleted && i <= 19) {
                    this.tv_message.setText(Html.fromHtml(getResources().getString(R.string.mbc_unable_toclean)));
                }
            } else {
                this.tv_message.setText("");
            }
        }
        if (getIntent().getStringExtra("DATA").equals(getResources().getString(R.string.mbc_cleaning_aborted))) {
            if (filterSmall(GlobalData.cacheContainingApps).size() > 0) {
                this.tv_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonResultActivity.this.startActivity(new Intent(CommonResultActivity.this.context, DeleteCacheActivity.class));
                    }
                });
                int i2 = Build.VERSION.SDK_INT;
                if (i2 >= 23) {
                    if (MobiClean.getInstance().junkData == null || MobiClean.getInstance().junkData.sys_cache_deleted) {
                        this.tv_message.setText("");
                    } else if (i2 <= 19) {
                        this.tv_message.setText(Html.fromHtml(getResources().getString(R.string.mbc_unable_toclean)));
                    }
                } else {
                    this.tv_message.setText("");
                }
            } else if (Build.VERSION.SDK_INT >= 23) {
                this.tv_message.setVisibility(View.GONE);
            } else {
                this.tv_message.setText("");
            }
        } else if (getIntent().getStringExtra("DATA").equals(getResources().getString(R.string.mbc_normaltempdevices))) {
            this.tv_message.setText("");
            tv_result_cpu.setVisibility(View.VISIBLE);
            this.tv_result_cpu.setText(R.string.mbc_normaltempdevices);
            this.tv_message.setVisibility(View.VISIBLE);
        }
        if (stringExtra2.equalsIgnoreCase("COOLER")) {
//            this.imgBin.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_anim_cooler));
        } else if (stringExtra2.equalsIgnoreCase("Boost")) {
            this.imgBin.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_anim_boost));
        } else if (stringExtra2.equalsIgnoreCase("SOCIAL")) {
            this.imgBin.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_anim_turbo_cleaner));
        } else if (stringExtra2.equalsIgnoreCase("AV")) {
            this.imgBin.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shield_white));
        }
        Util.appendLogmobiclean(TAG, "method:setTopLayoutAsPerType ends " + stringExtra2, "");
    }

    @Override
    public void facebookProfileVisit() {
        super.facebookProfileVisit();
    }

    @Override
    public int getFacePosition() {
        return super.getFacePosition();
    }

    @Override
    public String getFacebookPageURL() {
        return super.getFacebookPageURL();
    }

    @Override
    public void instaShare() {
        super.instaShare();
    }

    @Override
    public void onBackPressed() {
        try {
            MobiClean.getInstance().junkData = null;
            Intent i = new Intent(CommonResultActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
            System.runFinalization();
            Runtime.getRuntime().gc();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (MobiClean.getInstance().spaceManagerModule != null) {
            GlobalData.returnedAfterDeletion = true;
        }
        try {
            Timer timer = this.timer;
            if (timer != null) {
                timer.cancel();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        onBackPressed();

    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_cpu_cooler_result);
        Util.saveScreen(getClass().getName(), this);

        sendAnalytics("SCREEN_VISIT_" + getClass().getSimpleName());
        Util.appendLogmobiclean(TAG, "onCreate()", "");
        this.notDeleted = getIntent().getIntExtra("not_deleted", 0);
        this.context = this;
        this.sharedPrefUtil = new SharedPrefUtil(this.context);
        if (displaymetrics == null) {
            displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        }
        findids();

        redirectToNoti();
        setTopLayoutAsPerType();
        ImageView imageView = (ImageView) findViewById(R.id.option_click);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonResultActivity.this.multipleClicked()) {
                    return;
                }
                CommonResultActivity.this.startActivity(new Intent(CommonResultActivity.this, AVIgnorelist.class));
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(this);
        String string = this.sharedPrefUtil.getString(SharedPrefUtil.RATED_AT);
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(string);
        sb.append("  ");
        sb.append(Util.checkTimeDifference(System.currentTimeMillis() + "", string));
        setAnimation();
        boolean z = GlobalData.loadAds;
        if (!Util.isAdsFree(this.context) && !Util.isConnectingToInternet(this)) {
            try {
//                this.adLayout.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Util.appendLogmobiclean(TAG, "oncreate ends ", "");
        GlobalData.imageviewed = false;
        if (MobiClean.getInstance().junkData != null) {
            MobiClean.getInstance().junkData.sys_cache_deleted = false;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.whitelist_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onPause() {
        super.onPause();
        GlobalData.loadAds = true;
        GlobalData.backPressedResult = true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w(TAG, "OnResume Called");
        GlobalData.proceededToAd = false;
    }

    @Override
    public void send_facebook() {
        super.send_facebook();
    }

    @Override
    public void send_twitter() {
        super.send_twitter();
    }

    @Override
    public void send_whatspp() {
        super.send_whatspp();
    }

    @Override
    public void setFacePosition(int i) {
        super.setFacePosition(i);
    }

    @Override
    public void shareApp() {
        super.shareApp();
    }

}