package com.cleanPhone.mobileCleaner;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.cleanPhone.mobileCleaner.utility.DetermineTextSize;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.ProcessWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RamBoostAppsScreen extends ParentActivity implements View.OnClickListener {
    private static final String TAG = "RamBoostAppsScreen";
    private TextView ads_message;
    private TextView ads_title;
    private Button boostBtn;
    private ArrayList<ProcessWrapper> boostDataList;
    private Context context;
    private int deviceHeight;
    private int deviceWidth;
    public TextView i;
    public ProgressDialog j;
    public long l;
    private LinearLayout layoutsmallupper;
    public LinearLayout m;
    public RelativeLayout n;
    private boolean noti_result_back;
    public FrameLayout o;
    public TextView p;
    private ListView processListview;
    public TextView q;
    private boolean redirectToNoti;
    private SharedPrefUtil sharedPrefUtil;
    private LinearLayout topLayout;
    private TextView tvStatus;
    private TextView tvsize_unit;
    private TextView tvsize_unit_mb;
    private long totBoostSize = 0;
    private long sizechecked = 0;
    public long k = 0;
    private long totalram = 0;
    private String log = "";
    private boolean fromNotification = false;

    public class ProcessAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ProcessAdapter(Context context) {
            this.mInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return RamBoostAppsScreen.this.boostDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = this.mInflater.inflate(R.layout.process_listitem, viewGroup, false);
            }
            ImageView imageView = (ImageView) ViewHolder.get(view, R.id.boostitemicon);
            final CheckBox checkBox = (CheckBox) ViewHolder.get(view, R.id.boostitemcheck);
            checkBox.setFocusable(false);
            checkBox.setClickable(false);
            ProcessWrapper processWrapper = (ProcessWrapper) RamBoostAppsScreen.this.boostDataList.get(i);
            ((TextView) ViewHolder.get(view, R.id.boostitemname)).setText("" + processWrapper.appname);
            ((TextView) ViewHolder.get(view, R.id.boostitemsize)).setText("" + Util.convertBytes(processWrapper.size));
            try {
                imageView.setImageDrawable(RamBoostAppsScreen.this.context.getPackageManager().getApplicationIcon(processWrapper.name));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if (RamBoostAppsScreen.this.multipleClicked()) {
                        return;
                    }
                    if (((ProcessWrapper) RamBoostAppsScreen.this.boostDataList.get(i)).ischecked) {
                        RamBoostAppsScreen.this.totBoostSize -= ((ProcessWrapper) RamBoostAppsScreen.this.boostDataList.get(i)).size;
                        RamBoostAppsScreen.this.boostBtn.setText(String.format(RamBoostAppsScreen.this.getResources().getString(R.string.mbc_boost).replace("DO_NOT_TRANSLATE", "%s"), Util.convertBytes(RamBoostAppsScreen.this.totBoostSize)));
                        ((ProcessWrapper) RamBoostAppsScreen.this.boostDataList.get(i)).ischecked = false;
                        checkBox.setChecked(false);
                    } else {
                        RamBoostAppsScreen.this.totBoostSize += ((ProcessWrapper) RamBoostAppsScreen.this.boostDataList.get(i)).size;
                        RamBoostAppsScreen.this.boostBtn.setText(String.format(RamBoostAppsScreen.this.getResources().getString(R.string.mbc_boost).replace("DO_NOT_TRANSLATE", "%s"), Util.convertBytes(RamBoostAppsScreen.this.totBoostSize)));
                        ((ProcessWrapper) RamBoostAppsScreen.this.boostDataList.get(i)).ischecked = true;
                        checkBox.setChecked(true);
                    }
                    if (RamBoostAppsScreen.this.totBoostSize == 0) {
                        RamBoostAppsScreen.this.boostBtn.setText(String.format(RamBoostAppsScreen.this.getResources().getString(R.string.mbc_boost).replace("DO_NOT_TRANSLATE", "%s"), ""));
                    }
                }
            });
            if (((ProcessWrapper) RamBoostAppsScreen.this.boostDataList.get(i)).ischecked) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            return view;
        }
    }


    public static class ViewHolder {
        public static <T extends View> T get(View view, int i) {
            SparseArray sparseArray = (SparseArray) view.getTag();
            if (sparseArray == null) {
                sparseArray = new SparseArray();
                view.setTag(sparseArray);
            }
            T t = (T) sparseArray.get(i);
            if (t == null) {
                T t2 = (T) view.findViewById(i);
                sparseArray.put(i, t2);
                return t2;
            }
            return t;
        }
    }

    private void boostRam() {
    }


    @TargetApi(16)
    public void getRamSize() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        if (activityManager != null) {
            activityManager.getMemoryInfo(memoryInfo);
        }
        long j = Build.VERSION.SDK_INT >= 16 ? memoryInfo.totalMem : 0L;
        long j2 = memoryInfo.availMem;
        long j3 = j2 / 1048576;
        long j4 = memoryInfo.totalMem;
        long j5 = ((j4 - j2) * 100) / j;
        this.totalram = j4;
        this.k = j4 - j2;
        this.log += "Total ram size = " + Util.convertBytes(this.totalram) + "  Ram before boost = " + Util.convertBytes(this.k) + IOUtils.LINE_SEPARATOR_UNIX;
        this.sharedPrefUtil.saveString(SharedPrefUtil.RAMATPAUSE, "" + Math.round((float) 0));
    }

    private void init() {
        this.context = this;
        this.m = (LinearLayout) findViewById(R.id.layout_one);
        this.n = (RelativeLayout) findViewById(R.id.layout_two);
        this.o = (FrameLayout) findViewById(R.id.frame_mid_laysss);
        this.p = (TextView) findViewById(R.id.ads_btn_countinue);
        this.q = (TextView) findViewById(R.id.ads_btn_cancel);
        this.ads_title = (TextView) findViewById(R.id.dialog_title);
        this.ads_message = (TextView) findViewById(R.id.dialog_msg);
        this.q.setOnClickListener(this);
        this.p.setOnClickListener(this);

        this.sharedPrefUtil = new SharedPrefUtil(this.context);
        this.i = (TextView) findViewById(R.id.textview_processservies);
        this.layoutsmallupper = (LinearLayout) findViewById(R.id.la);
        this.topLayout = (LinearLayout) findViewById(R.id.boostscreen_toplayout);
        this.boostBtn = (Button) findViewById(R.id.boost_btn);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.parent);
        this.tvsize_unit = (TextView) findViewById(R.id.boost_sizetv_unit);
        this.tvsize_unit_mb = (TextView) findViewById(R.id.boost_unit);
        this.tvStatus = (TextView) findViewById(R.id.boost_status);
        this.processListview = (ListView) findViewById(R.id.boost_processlistview);
    }

    private boolean notExists(String str) {
        for (int i = 0; i < this.boostDataList.size(); i++) {
            try {
                if (str.equalsIgnoreCase("" + this.boostDataList.get(i).appname)) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }
        return true;
    }

    private void redirectToNoti() {
        this.redirectToNoti = getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false);
        this.noti_result_back = getIntent().getBooleanExtra(GlobalData.NOTI_RESULT_BACK, false);
    }

    private void setDeviceDimensions() {
        Util.appendLogmobiclean(TAG, "method:setDeviceDimensions", "");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
    }

    private void setDimensions() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.topLayout.getLayoutParams();
        layoutParams.height = (this.deviceHeight * 16) / 100;
        this.topLayout.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.processListview.getLayoutParams();
        int i = this.deviceHeight;
        layoutParams2.height = (i * 52) / 100;
        layoutParams2.setMargins(0, 0, 0, (i * 3) / 100);
        this.processListview.setLayoutParams(layoutParams2);
        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) this.layoutsmallupper.getLayoutParams();
        layoutParams3.height = (this.deviceHeight * 7) / 100;
        this.layoutsmallupper.setLayoutParams(layoutParams3);
    }

    private void setFonts() {
        TextView textView = this.tvStatus;
        textView.setTextSize(0, DetermineTextSize.determineTextSize(textView.getTypeface(), (this.deviceHeight * 4) / 100));
        TextView textView2 = this.tvsize_unit;
        textView2.setTextSize(0, DetermineTextSize.determineTextSize(textView2.getTypeface(), (this.deviceHeight * 11) / 100));
        TextView textView3 = this.tvsize_unit_mb;
        textView3.setTextSize(0, DetermineTextSize.determineTextSize(textView3.getTypeface(), (this.deviceHeight * 5) / 100));
        this.i.setTextSize(0, DetermineTextSize.determineTextSize(this.tvStatus.getTypeface(), (this.deviceHeight * 4) / 100));
        this.boostBtn.setTextSize(0, DetermineTextSize.determineTextSize(this.tvStatus.getTypeface(), (this.deviceHeight * 4) / 100));
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
        ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_phone_boost));
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_phone_boost_small));
        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(String.format(getResources().getString(R.string.mbc_simple_back_press), getResources().getString(R.string.mbc_after_boost_scan_txt)));
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RamBoostAppsScreen.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (RamBoostAppsScreen.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
                if (RamBoostAppsScreen.this.redirectToNoti || RamBoostAppsScreen.this.noti_result_back) {
                    RamBoostAppsScreen.this.startActivity(new Intent(RamBoostAppsScreen.this.context, HomeActivity.class));
                } else {
                    RamBoostAppsScreen.this.finish();
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
            try {
                RelativeLayout relativeLayout = this.n;
                if (relativeLayout != null && relativeLayout.getVisibility() == View.VISIBLE) {
                    this.m.setVisibility(View.VISIBLE);
                    this.n.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (view.getId() == R.id.ads_btn_countinue) {
            this.n.setVisibility(View.GONE);
            this.m.setVisibility(View.VISIBLE);
            if (!this.redirectToNoti && !this.noti_result_back) {
                finish();
            } else {
                startActivity(new Intent(this.context, HomeActivity.class));
            }
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_boost_list);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Util.appendLogmobiclean(TAG, "onCreate", "");
        this.context = this;
        this.boostDataList = new ArrayList<>();
        Log.d("BCHECK", GlobalData.processDataList.size() + "");
        for (int i = 0; i < GlobalData.processDataList.size(); i++) {
            if (notExists(GlobalData.processDataList.get(i).appname)) {
                this.boostDataList.add(GlobalData.processDataList.get(i));
            }
        }
        Log.d("BCHECK22", this.boostDataList.size() + "  " + GlobalData.processDataList.size());
        setDeviceDimensions();
        try {
            Collections.sort(this.boostDataList, new Comparator<ProcessWrapper>() {
                @Override
                public int compare(ProcessWrapper processWrapper, ProcessWrapper processWrapper2) {
                    return Long.compare(processWrapper2.size, processWrapper.size);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Util.appendLogmobiclean(TAG, "Sorted list", "");
        init();
        setFonts();
        setDimensions();
        redirectToNoti();
        if (Build.VERSION.SDK_INT <= 23) {
            long j = 0;
            for (int i2 = 0; i2 < this.boostDataList.size(); i2++) {
                j += this.boostDataList.get(i2).size;
                this.totBoostSize += this.boostDataList.get(i2).size;
            }
            this.i.setText("" + this.boostDataList.size());
            Util.appendLogmobiclean(TAG, " before ProcessAdapter", "");
            this.processListview.setAdapter((ListAdapter) new ProcessAdapter(this.context));
            Util.appendLogmobiclean(TAG, " after ProcessAdapter", "");
            this.boostBtn.setEnabled(true);
            this.tvStatus.setText(R.string.mbc_ram_to_boost);
            try {
                Util.appendLogmobiclean(TAG, "TOTAL SIZE OF PROCESS>>>>>" + Util.convertBytes_only(j), "");
                this.tvsize_unit.setText("" + Util.convertBytes_only(j));
                this.tvsize_unit_mb.setText("" + Util.convertBytes_unit(j));
                this.boostBtn.setText(String.format(getResources().getString(R.string.mbc_boost).replace("" + getString(R.string.error_occured), "%s"), Util.convertBytes(j)));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        this.boostBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                if (RamBoostAppsScreen.this.multipleClicked()) {
                    return;
                }
                Util.appendLogmobiclean(RamBoostAppsScreen.TAG, "preexecute clicked", "");
                if (RamBoostAppsScreen.this.totBoostSize == 0) {
                    Util.appendLogmobiclean(RamBoostAppsScreen.TAG, " total size Select at least one process to boost.", "");
                    Context context = RamBoostAppsScreen.this.context;
                    Toast.makeText(context, "" + RamBoostAppsScreen.this.getResources().getString(R.string.mbc_atleastone_boost), Toast.LENGTH_SHORT).show();
                    return;
                }
                new AsyncTask<String, String, String>() {
                    @Override
                    public void onPreExecute() {
                        Util.appendLogmobiclean(RamBoostAppsScreen.TAG, "preexecute boostbtn", "");
                        RamBoostAppsScreen.this.sizechecked = 0L;
                        RamBoostAppsScreen ramBoostAppsScreen = RamBoostAppsScreen.this;
                        ramBoostAppsScreen.k = 0L;
                        ramBoostAppsScreen.l = 0L;
                        ramBoostAppsScreen.j = new ProgressDialog(RamBoostAppsScreen.this.context);
                        ProgressDialog progressDialog = RamBoostAppsScreen.this.j;
                        progressDialog.setMessage("" + RamBoostAppsScreen.this.getString(R.string.mbc_boosting));
                        RamBoostAppsScreen.this.j.show();
                        RamBoostAppsScreen.this.log = "";
                        RamBoostAppsScreen.this.boostBtn.setEnabled(false);
                        super.onPreExecute();
                    }

                    @Override
                    public String doInBackground(String... strArr) {
                        Util.appendLogmobiclean(RamBoostAppsScreen.TAG, "doInBackground boostbtn", "");
                        RamBoostAppsScreen.this.getRamSize();
                        Util.appendLog("", "boostcheck.txt");
                        int i3 = 0;
                        for (int i4 = 0; i4 < RamBoostAppsScreen.this.boostDataList.size(); i4++) {
                            if (((ProcessWrapper) RamBoostAppsScreen.this.boostDataList.get(i4)).ischecked) {
                                i3++;
                                RamBoostAppsScreen.this.sizechecked += ((ProcessWrapper) RamBoostAppsScreen.this.boostDataList.get(i4)).size;
                                ((ActivityManager) RamBoostAppsScreen.this.context.getSystemService(ACTIVITY_SERVICE)).killBackgroundProcesses(((ProcessWrapper) RamBoostAppsScreen.this.boostDataList.get(i4)).name);
                            }
                        }
                        RamBoostAppsScreen ramBoostAppsScreen = RamBoostAppsScreen.this;
                        ramBoostAppsScreen.k -= ramBoostAppsScreen.sizechecked;
                        RamBoostAppsScreen.this.log = RamBoostAppsScreen.this.log + "Total boosted = " + Util.convertBytes(RamBoostAppsScreen.this.sizechecked) + "  Ram after boost = " + Util.convertBytes(RamBoostAppsScreen.this.k) + IOUtils.LINE_SEPARATOR_UNIX;
                        SharedPrefUtil sharedPrefUtil = RamBoostAppsScreen.this.sharedPrefUtil;
                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(System.currentTimeMillis());
                        sharedPrefUtil.saveString(SharedPrefUtil.RAMPAUSE, sb.toString());
                        if (RamBoostAppsScreen.this.boostDataList.size() == i3) {
                            RamBoostAppsScreen.this.sharedPrefUtil.saveString(SharedPrefUtil.LASTBOOSTTIME, "" + System.currentTimeMillis());
                        }
                        Util.appendLogmobiclean(RamBoostAppsScreen.TAG, "doInBackground ends", "");
                        return null;
                    }

                    @Override
                    public void onPostExecute(String str) {
                        RamBoostAppsScreen.this.j.dismiss();
                        Util.appendLogmobiclean(RamBoostAppsScreen.TAG, "TOTAL SIZE OF BOOST PROCESS>>>>> " + Util.convertBytes(RamBoostAppsScreen.this.totBoostSize), "");
                        Intent putExtra = new Intent(RamBoostAppsScreen.this.context, JunkDeleteAnimationScreen.class).putExtra("DATA", Util.convertBytes(RamBoostAppsScreen.this.totBoostSize)).putExtra("TYPE", "BOOST").putExtra(GlobalData.REDIRECTNOTI, true);
                        RamBoostAppsScreen ramBoostAppsScreen = RamBoostAppsScreen.this;
                        long j2 = (ramBoostAppsScreen.k * 100) / ramBoostAppsScreen.totalram;
                        SharedPrefUtil sharedPrefUtil = RamBoostAppsScreen.this.sharedPrefUtil;
                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        float f = (float) j2;
                        sb.append(Math.round(f));
                        sharedPrefUtil.saveString(SharedPrefUtil.RAMATPAUSE, sb.toString());
                        Log.d("SAVED ramsaveSize2", "" + Math.round(f));
                        RamBoostAppsScreen.this.finish();
                        RamBoostAppsScreen.this.startActivity(putExtra);
                        Util.appendLog("" + RamBoostAppsScreen.this.log, "boostcheck.txt");
                        super.onPostExecute( str);
                    }
                }.execute(new String[0]);
            }
        });
        Util.appendLogmobiclean(TAG, "onCreate ends", "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onResume() {
        Util.appendLogmobiclean(TAG, "onResume", "");
        if (this.boostDataList == null) {
            this.boostDataList = new ArrayList<>();
        }
        setDeviceDimensions();
        super.onResume();
    }
}
