package com.cleanPhone.mobileCleaner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.os.SystemClock;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.utility.DetermineTextSize;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.PermitActivity;
import com.cleanPhone.mobileCleaner.utility.PopUpScreen;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppDetails;
import com.cleanPhone.mobileCleaner.wrappers.PackageInfoStruct;
import com.cleanPhone.mobileCleaner.wrappers.RestoreWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class ApplicationManageActivity extends PermitActivity implements View.OnClickListener {
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    private static final String APP_PKG_NAME_22 = "pkg";
    private static final int MY_PERMISSIONS_REQUEST_CODE2 = 765;
    private static final int REQUEST_PERMISSIONS = 404;
    private static final String SCHEME = "package";
    private boolean appOnsdcard;
    private TextView appsize;
    private TextView appsize_unit;
    private TextView apptexttv;
    private Button backupTab;
    private Button btn1;
    private Button btn2;
    private LinearLayout btnLayout;
    private Context context;
    private int deviceHeight;
    private Dialog dialog;
    private int finalI;
    private RelativeLayout hiddenPermissionLayout;
    private ArrayList<PackageInfoStruct> installedApps;
    public TextView j;
    public ProgressDialog l;
    private String lastpname;
    private ListView listView;
    private long mLastClickTime;
    private boolean noti_result_back;
    private RelativeLayout parent;
    private ProgressDialog progressDialog;
    private Button restoreTab;
    private LinearLayout topLayout;
    private volatile boolean lastPackagefound = false;
    private ArrayList<RestoreWrapper> restoreData = new ArrayList<>();
    private long totalSizeSDCARD = 0;
    private long availableSizeSDCARD = 0;
    private boolean showMoveSDcard = false;
    private boolean isExecuting = false;
    private String TAG = "ApplicationManageActivity";
    private int totbackup = 0;
    public long k = 0;
    public int m = 0;
    public int n = 0;
    public long o = 0;
    private final int VIEW_TYPE_EXAMPLE = 0;
    private final int VIEW_TYPE_EXAMPLE_TWO = 1;

    public class AppManagerAdapter extends ArrayAdapter<PackageInfoStruct> {
        private final Context context;
        private View convertView;
        private final ArrayList<PackageInfoStruct> data;
        private LayoutInflater inflater;

        public class ViewHolder {
            public ImageView f4568a;
            public TextView b;
            public TextView f4569c;
            public CheckBox checkbox;

            public ViewHolder(AppManagerAdapter appManagerAdapter) {
            }
        }

        public class ViewHolder2 {

            public ViewHolder2(View ignoreListAdapter) {
            }
        }

        public AppManagerAdapter(Context context, int i, ArrayList<PackageInfoStruct> arrayList) {
            super(context, i, arrayList);
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data = arrayList;
        }

        @Override
        @NonNull
        public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
            int viewType = getItemViewType(i);
            ViewHolder viewHolder = null;
            ViewHolder2 viewHolder2 = null;
            switch (viewType) {
                case VIEW_TYPE_EXAMPLE_TWO: {
                    this.convertView = view;
                    if (view == null) {
                        viewHolder = new ViewHolder(this);
                        view = this.inflater.inflate(R.layout.applistitemview, (ViewGroup) null);
                        viewHolder.f4568a = (ImageView) view.findViewById(R.id.appitem_image);
                        viewHolder.f4569c = (TextView) view.findViewById(R.id.appitem_name);
                        viewHolder.b = (TextView) view.findViewById(R.id.appitem_size);
                        CheckBox checkBox = (CheckBox) view.findViewById(R.id.appitem_check);
                        viewHolder.checkbox = checkBox;
                        checkBox.setFocusable(false);
                        viewHolder.checkbox.setClickable(false);
                        view.setTag(viewHolder);
//                    } else {
//                        view2 = view;
//                        viewHolder = (ViewHolder) view.getTag();
//                    }
                        final PackageInfoStruct packageInfoStruct = this.data.get(i);
                        if (packageInfoStruct.pname != null) {
                            try {
                                viewHolder.f4568a.setImageDrawable(this.context.getPackageManager().getApplicationIcon(packageInfoStruct.pname));
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        String str = packageInfoStruct.appname;
                        if (str != null && str.contains(".")) {
                            try {
                                packageInfoStruct.appname = "" + ((Object) ApplicationManageActivity.this.getPackageManager().getApplicationLabel(ApplicationManageActivity.this.getPackageManager().getApplicationInfo(packageInfoStruct.pname, 0)));
                            } catch (PackageManager.NameNotFoundException e2) {
                                e2.printStackTrace();
                            }
                        }
                        TextView textView = viewHolder.f4569c;
                        textView.setText("" + packageInfoStruct.appname);
                        TextView textView2 = viewHolder.b;
                        textView2.setText("" + Util.convertBytes(packageInfoStruct.appsize));
                        ViewHolder finalViewHolder = viewHolder;
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view3) {
                                if (ApplicationManageActivity.this.multipleClicked()) {
                                    return;
                                }
                                if (packageInfoStruct.ischecked) {
                                    finalViewHolder.checkbox.setChecked(false);
                                    packageInfoStruct.ischecked = false;
                                    return;
                                }
                                finalViewHolder.checkbox.setChecked(true);
                                packageInfoStruct.ischecked = true;
                            }
                        });
                        if (packageInfoStruct.ischecked) {
                            viewHolder.checkbox.setChecked(true);
                        } else {
                            viewHolder.checkbox.setChecked(false);
                        }
                        break;
                    }
                }
                case VIEW_TYPE_EXAMPLE: {
                    if (view == null) {
                        viewHolder2 = new ViewHolder2(view);
                        view = this.inflater.inflate(R.layout.ad_view, (ViewGroup) null);
//                        viewHolder2.rl_native = (TemplateView) view.findViewById(R.id.native_view);

                        ViewHolder2 finalViewHolder1 = viewHolder2;
                        break;
                    }
                }

            }


//            View view2;
//            final ViewHolder viewHolder;
//
//            return view2;
            return view;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return (position % 9 == 8) ? VIEW_TYPE_EXAMPLE : VIEW_TYPE_EXAMPLE_TWO;
        }
    }

    public class RestoreAdapter extends ArrayAdapter<RestoreWrapper> {
        private final Context context;
        private final ArrayList<RestoreWrapper> data;
        private LayoutInflater inflater;

        public class ViewHolder {
            public ImageView f4573a;
            public TextView b;
            public TextView f4574c;
            public CheckBox checkbox;

            public ViewHolder(RestoreAdapter restoreAdapter) {
            }
        }

        public RestoreAdapter(Context context, int i, ArrayList<RestoreWrapper> arrayList) {
            super(context, i, arrayList);
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data = arrayList;
        }

        @Override
        @NonNull
        public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
            View view2;
            final ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder(this);
                view2 = this.inflater.inflate(R.layout.applistitemview, (ViewGroup) null);
                viewHolder.f4573a = (ImageView) view2.findViewById(R.id.appitem_image);
                viewHolder.f4574c = (TextView) view2.findViewById(R.id.appitem_name);
                viewHolder.b = (TextView) view2.findViewById(R.id.appitem_size);
                CheckBox checkBox = (CheckBox) view2.findViewById(R.id.appitem_check);
                viewHolder.checkbox = checkBox;
                checkBox.setFocusable(false);
                viewHolder.checkbox.setClickable(false);
                view2.setTag(viewHolder);
            } else {
                view2 = view;
                viewHolder = (ViewHolder) view.getTag();
            }
            final RestoreWrapper restoreWrapper = this.data.get(i);
            if (restoreWrapper.packagename != null) {
                try {
                    viewHolder.f4573a.setImageDrawable(this.context.getPackageManager().getApplicationIcon(restoreWrapper.packagename));
                } catch (PackageManager.NameNotFoundException e) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap decodeFile = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + GlobalData.backuppath + "icons/" + restoreWrapper.packagename + ".png", options);
                    try {
                        if (decodeFile == null) {
                            viewHolder.f4573a.setImageBitmap(BitmapFactory.decodeResource(ApplicationManageActivity.this.getResources(), R.drawable.ic_android));
                        } else {
                            viewHolder.f4573a.setImageBitmap(decodeFile);
                        }
                    } catch (Exception e2) {
                        try {
                            viewHolder.f4573a.setImageBitmap(BitmapFactory.decodeResource(ApplicationManageActivity.this.getResources(), R.drawable.ic_android));
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                        e2.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
            TextView textView = viewHolder.f4574c;
            textView.setText("" + restoreWrapper.appname);
            TextView textView2 = viewHolder.b;
            textView2.setText("" + Util.convertBytes(restoreWrapper.size));
            view2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view3) {
                    if (ApplicationManageActivity.this.multipleClicked()) {
                        return;
                    }
                    RestoreWrapper restoreWrapper2 = restoreWrapper;
                    if (restoreWrapper2.ischecked) {
                        restoreWrapper2.ischecked = false;
                        viewHolder.checkbox.setChecked(false);
                        return;
                    }
                    restoreWrapper2.ischecked = true;
                    viewHolder.checkbox.setChecked(true);
                }
            });
            if (restoreWrapper.ischecked) {
                viewHolder.checkbox.setChecked(true);
            } else {
                viewHolder.checkbox.setChecked(false);
            }
            return view2;
        }
    }

    public class cachePackState extends IPackageStatsObserver.Stub {
        private cachePackState() {
        }

        private void fillCacheSizes(PackageStats packageStats) {
            int i = 0;
            while (true) {
                if (i >= ApplicationManageActivity.this.installedApps.size()) {
                    break;
                }
                if (((PackageInfoStruct) ApplicationManageActivity.this.installedApps.get(i)).pname.equalsIgnoreCase("" + packageStats.packageName)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        long j = packageStats.externalCacheSize;
                        ((PackageInfoStruct) ApplicationManageActivity.this.installedApps.get(i)).movesize = j + j;
                        ((PackageInfoStruct) ApplicationManageActivity.this.installedApps.get(i)).cachesize = packageStats.cacheSize + packageStats.externalCacheSize;
                    } else {
                        ((PackageInfoStruct) ApplicationManageActivity.this.installedApps.get(i)).movesize = packageStats.codeSize;
                        ((PackageInfoStruct) ApplicationManageActivity.this.installedApps.get(i)).appsize = packageStats.cacheSize + packageStats.externalCacheSize;
                    }
                    ((PackageInfoStruct) ApplicationManageActivity.this.installedApps.get(i)).dataSize = packageStats.dataSize;
                    ((PackageInfoStruct) ApplicationManageActivity.this.installedApps.get(i)).appsize = packageStats.cacheSize + packageStats.externalCacheSize + packageStats.dataSize + packageStats.externalDataSize + packageStats.codeSize;
                    ApplicationManageActivity applicationManageActivity = ApplicationManageActivity.this;
                    applicationManageActivity.o += ((PackageInfoStruct) applicationManageActivity.installedApps.get(i)).appsize;
                    ((PackageInfoStruct) ApplicationManageActivity.this.installedApps.get(i)).apksize = packageStats.codeSize;
                } else {
                    i++;
                }
            }
            if (packageStats.packageName.equalsIgnoreCase("" + ApplicationManageActivity.this.lastpname)) {
                ApplicationManageActivity.this.lastPackagefound = true;
            }
        }

        @Override
        public void onGetStatsCompleted(PackageStats packageStats, boolean z) {
            fillCacheSizes(packageStats);
        }
    }

    private void GetAllAppsDetail() {
        if (this.isExecuting) {
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(this.context);
        this.l = progressDialog;
        progressDialog.setCancelable(true);
        this.l.setCanceledOnTouchOutside(true);
        new AsyncTask<String, String, String>() {
            @Override
            public void onPreExecute() {
                Util.appendLogmobiclean(ApplicationManageActivity.this.TAG, "GetAllAppsDetail()>>>>>>>calling onPreExecute()", GlobalData.FILE_NAME);
                ApplicationManageActivity.this.isExecuting = true;
                Log.e(ApplicationManageActivity.this.TAG, "onPreExecute");
                ProgressDialog progressDialog2 = ApplicationManageActivity.this.l;
                progressDialog2.setMessage("" + ApplicationManageActivity.this.getString(R.string.mbc_loading));
                ApplicationManageActivity.this.l.show();
                ApplicationManageActivity applicationManageActivity = ApplicationManageActivity.this;
                applicationManageActivity.o = 0L;
                applicationManageActivity.lastPackagefound = false;
                super.onPreExecute();
            }

            @Override
            public String doInBackground(String... strArr) {
                Util.appendLogmobiclean(ApplicationManageActivity.this.TAG, "GetAllAppsDetail()>>>>>>>calling doInBackground()", GlobalData.FILE_NAME);
                ApplicationManageActivity.this.getpackageSize();
                ApplicationManageActivity applicationManageActivity = ApplicationManageActivity.this;
                applicationManageActivity.getRestoreData("" + Environment.getExternalStorageDirectory() + GlobalData.backuppath);
                return null;
            }

            @Override
            public void onPostExecute(String str) {
                if (ApplicationManageActivity.this.installedApps.size() > 0) {
                    ApplicationManageActivity.this.appsize.setText(Util.convertBytes_only(ApplicationManageActivity.this.o));
                    ApplicationManageActivity.this.appsize_unit.setText(Util.convertBytes_unit(ApplicationManageActivity.this.o));
                    Util.appendLogmobiclean(ApplicationManageActivity.this.TAG, " GetAllAppsDetail()>>>>>>>onPostExecute()", GlobalData.FILE_NAME);
                } else {
                    ApplicationManageActivity.this.appsize.setText(R.string.mbc_noappfound);
                    ApplicationManageActivity.this.appsize_unit.setText(" ");
                }
                ApplicationManageActivity applicationManageActivity = ApplicationManageActivity.this;
                AppManagerAdapter appManagerAdapter = new AppManagerAdapter(applicationManageActivity.context, R.layout.junklistitemlayout, ApplicationManageActivity.this.installedApps);
                ApplicationManageActivity.this.backupTab.setText(String.format(ApplicationManageActivity.this.getResources().getString(R.string.mbc_install).replace("DO_NOT_TRANSLATE", "%d"), Integer.valueOf(ApplicationManageActivity.this.installedApps.size())));
                ApplicationManageActivity.this.restoreTab.setText(String.format(ApplicationManageActivity.this.getResources().getString(R.string.mbc_archived).replace("DO_NOT_TRANSLATE", "%d"), Integer.valueOf(ApplicationManageActivity.this.restoreData.size())));
                ApplicationManageActivity.this.listView.setAdapter((ListAdapter) appManagerAdapter);
                appManagerAdapter.setNotifyOnChange(true);
                ApplicationManageActivity.this.backupView();
                ApplicationManageActivity.this.isExecuting = false;
                ProgressDialog progressDialog2 = ApplicationManageActivity.this.l;
                if (progressDialog2 == null || !progressDialog2.isShowing()) {
                    return;
                }
                try {
                    ApplicationManageActivity.this.l.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[0]);
    }

    @SuppressLint({"WrongConstant", "UseCompatLoadingForDrawables", "ResourceType"})
    private void actionBtn1() {
        this.n = 0;
        if (this.btn1.getText().toString().equalsIgnoreCase(getResources().getString(R.string.mbc_restore))) {
            for (int i = 0; i < this.restoreData.size(); i++) {
                if (this.restoreData.get(i).ischecked) {
                    this.n++;
                    if (!isInstalled(this.restoreData.get(i).packagename)) {
                        this.n++;
                        try {
                            File file = new File(this.restoreData.get(i).path);
                            if (Build.VERSION.SDK_INT > 23) {
                                startActivity(new Intent("android.intent.action.VIEW").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(3).setDataAndType(FileProvider.getUriForFile(this.context, "com.mobiclean.phoneclean.fileprovider", file), "application/vnd.android.package-archive"));
                            } else {
                                Intent intent = new Intent("android.intent.action.VIEW");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                                startActivity(intent);
                            }
                        } catch (Exception unused) {
                            File file2 = new File(this.restoreData.get(i).path);
                            if (Build.VERSION.SDK_INT > 23) {
                                startActivity(new Intent("android.intent.action.VIEW").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(3).setDataAndType(FileProvider.getUriForFile(this.context, "com.mobiclean.phoneclean.fileprovider", file2), "application/vnd.android.package-archive"));
                            } else {
                                Intent intent2 = new Intent("android.intent.action.VIEW");
                                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent2.setDataAndType(Uri.fromFile(file2), "application/vnd.android.package-archive");
                                startActivity(intent2);
                            }
                        }
                    } else {
                        Log.e(this.TAG, "App is installed");
                        showAdsFullDialog(getResources().getString(R.string.mbc_restore), getResources().getString(R.string.mbc_reinsatall_app).replace("App", this.restoreData.get(i).appname), "RR", this.context.getResources().getDrawable(17301543), i);
                    }
                }
            }
            if (this.n == 0) {
                Snackbar make = Snackbar.make(this.parent, getResources().getString(R.string.mbc_select_app_restore), -1);
                ((TextView) make.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
                make.show();
            }
        } else if (!super.checkStoragePermissions()) {
            super.requestAppPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, R.string.mbc_runtime_permissions_txt, REQUEST_PERMISSIONS);
        } else {
            backup();
        }
    }

    @SuppressLint("ResourceType")
    private void actionBtn2() {
        this.k = 0L;
        if (this.btn2.getText().toString().equalsIgnoreCase(getResources().getString(R.string.mbc_delete))) {
            Log.e("APP_MANAGER", "" + this.btn2.getText().toString());
            boolean z = true;
            for (int i = 0; i < this.restoreData.size(); i++) {
                if (this.restoreData.get(i).ischecked) {
                    Log.e("---------SIZE", "" + this.restoreData.get(i).size);
                    this.k = this.k + this.restoreData.get(i).size;
                    z = false;
                }
            }
            Log.e("---------SIZE_TOT", "" + this.k);
            if (z) {
                Snackbar make = Snackbar.make(this.parent, getResources().getString(R.string.mbc_select_app_delete), -1);
                ((TextView) make.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
                make.show();
                return;
            }
            showAdsFullDialog(getResources().getString(R.string.mbc_delete), getResources().getString(R.string.mbc_delete_app_file), "DP", this.context.getResources().getDrawable(17301564), 0);
            return;
        }
        Log.e("APP_MANAGER", "" + this.btn2.getText().toString());
        int i2 = 0;
        for (int i3 = 0; i3 < this.installedApps.size(); i3++) {
            if (this.installedApps.get(i3).ischecked) {
                i2++;
                Log.e("package name---------", "" + this.installedApps.get(i3));
                Uri parse = Uri.parse("package:" + this.installedApps.get(i3).pname);
                GlobalData.uninstalledAppSize = this.installedApps.get(i3).appsize;
                this.context.startActivity(new Intent("android.intent.action.DELETE", parse));
            }
        }
        if (i2 == 0) {
            Snackbar make2 = Snackbar.make(this.parent, getResources().getString(R.string.mbc_select_app_uninsall), -1);
            ((TextView) make2.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
            make2.show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void backup() {
        long j = 0;
        try {
            this.availableSizeSDCARD = 0L;
            this.totalSizeSDCARD = 0L;
            getSDCARDSize();
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            if (Build.VERSION.SDK_INT >= 18) {
                j = (statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong()) + this.availableSizeSDCARD;
            } else {
                j = Environment.getExternalStorageDirectory().getFreeSpace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (j > 104857600) {
            new AsyncTask<String, String, String>() {
                @Override
                public void onPreExecute() {
                    super.onPreExecute();
                    ApplicationManageActivity.this.l = new ProgressDialog(ApplicationManageActivity.this);
                    ApplicationManageActivity.this.getWindow().addFlags(2097280);
                    ApplicationManageActivity.this.l.setMessage("" + ApplicationManageActivity.this.getString(R.string.mbc_processing));
                    ApplicationManageActivity.this.l.setCanceledOnTouchOutside(false);
                    ApplicationManageActivity.this.l.setProgressStyle(1);
                    ApplicationManageActivity.this.l.setCancelable(false);
                    for (int i = 0; i < ApplicationManageActivity.this.installedApps.size(); i++) {
                        if (((PackageInfoStruct) ApplicationManageActivity.this.installedApps.get(i)).ischecked) {
                            ApplicationManageActivity.this.totbackup++;
                        }
                    }
                    ApplicationManageActivity applicationManageActivity = ApplicationManageActivity.this;
                    applicationManageActivity.l.setMax(applicationManageActivity.totbackup);
                    ApplicationManageActivity.this.l.show();
                }

                @Override
                public String doInBackground(String... strArr) {
                    for (int i = 0; i < ApplicationManageActivity.this.installedApps.size(); i++) {
                        if (((PackageInfoStruct) ApplicationManageActivity.this.installedApps.get(i)).ischecked) {
                            ApplicationManageActivity applicationManageActivity = ApplicationManageActivity.this;
                            applicationManageActivity.m++;
                            StringBuilder sb = new StringBuilder();
                            sb.append("");
                            ApplicationManageActivity applicationManageActivity2 = ApplicationManageActivity.this;
                            sb.append(applicationManageActivity2.checkName(((PackageInfoStruct) applicationManageActivity2.installedApps.get(i)).appname, ((PackageInfoStruct) ApplicationManageActivity.this.installedApps.get(i)).pname));
                            applicationManageActivity.copyFile(((PackageInfoStruct) applicationManageActivity.installedApps.get(i)).apkPath, "" + Environment.getExternalStorageDirectory() + GlobalData.backuppath, sb.toString());
                            try {
                                ApplicationManageActivity applicationManageActivity3 = ApplicationManageActivity.this;
                                applicationManageActivity3.writeBitmap(applicationManageActivity3.context.getPackageManager().getApplicationIcon(((PackageInfoStruct) ApplicationManageActivity.this.installedApps.get(i)).pname), "" + ((PackageInfoStruct) ApplicationManageActivity.this.installedApps.get(i)).pname);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            publishProgress("" + ApplicationManageActivity.this.m);
                        }
                    }
                    return null;
                }

                @Override
                public void onPostExecute(String str) {
                    ApplicationManageActivity.this.totbackup = 0;
                    ApplicationManageActivity applicationManageActivity = ApplicationManageActivity.this;
                    if (applicationManageActivity.m == 0) {
                        Snackbar make = Snackbar.make(applicationManageActivity.parent, ApplicationManageActivity.this.getResources().getString(R.string.mbc_selectapp), -1);
                        ((TextView) make.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
                        make.show();
                    } else {
                        applicationManageActivity.getRestoreData("" + Environment.getExternalStorageDirectory() + GlobalData.backuppath);
                        ApplicationManageActivity.this.restoreTab.setText(String.format(ApplicationManageActivity.this.getResources().getString(R.string.mbc_archived).replace("DO_NOT_TRANSLATE", "%d"), Integer.valueOf(ApplicationManageActivity.this.restoreData.size())));
                        RelativeLayout relativeLayout = ApplicationManageActivity.this.parent;
                        String replace = ApplicationManageActivity.this.getResources().getString(R.string.mbc_application_file).replace("DO_NOT_TRANSLATE", "%s");
                        Snackbar make2 = Snackbar.make(relativeLayout, String.format(replace, Environment.getExternalStorageDirectory() + GlobalData.backuppath), 0);
                        ((TextView) make2.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
                        make2.show();
                    }
                    ApplicationManageActivity applicationManageActivity2 = ApplicationManageActivity.this;
                    applicationManageActivity2.m = 0;
                    applicationManageActivity2.backupView();
                    ApplicationManageActivity.this.l.dismiss();
                }

                @Override
                public void onProgressUpdate(String... strArr) {
                    super.onProgressUpdate(strArr);
                    ApplicationManageActivity applicationManageActivity = ApplicationManageActivity.this;
                    applicationManageActivity.l.setProgress(applicationManageActivity.m);
                }
            }.execute(new String[0]);
            return;
        }
        Snackbar make = Snackbar.make(this.parent, String.format(getResources().getString(R.string.mbc_minimum_space), "100MB"), 0);
        ((TextView) make.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
        make.show();
    }


    public void backupView() {
        this.j.setVisibility(View.INVISIBLE);
        this.listView.setVisibility(View.VISIBLE);
        for (int i = 0; i < this.installedApps.size(); i++) {
            this.installedApps.get(i).ischecked = false;
        }
        this.backupTab.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.scroll);
        this.restoreTab.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        AppManagerAdapter appManagerAdapter = new AppManagerAdapter(this.context, R.layout.junklistitemlayout, this.installedApps);
        this.backupTab.setText(String.format(getResources().getString(R.string.mbc_install).replace("DO_NOT_TRANSLATE", "%d"), Integer.valueOf(this.installedApps.size())));
        this.listView.setAdapter((ListAdapter) appManagerAdapter);
        appManagerAdapter.setNotifyOnChange(true);
        Button button = this.btn1;
        button.setText("" + getResources().getString(R.string.mbc_backup));
        Button button2 = this.btn2;
        button2.setText("" + getResources().getString(R.string.mbc_unistall));
        this.btn1.setVisibility(View.VISIBLE);
        this.btn2.setVisibility(View.VISIBLE);
        this.btnLayout.setVisibility(View.VISIBLE);
        if (this.installedApps.size() > 0) {
            return;
        }
        this.listView.setVisibility(View.GONE);
        this.j.setVisibility(View.VISIBLE);
        this.j.setText(R.string.mbc_noappsinsall);
    }


    public String checkName(String str, String str2) {
        File file = new File(Environment.getExternalStorageDirectory() + GlobalData.backuppath);
        if (file.exists() && file.isDirectory() && file.list() != null && file.listFiles().length > 0) {
            File[] listFiles = file.listFiles();
            for (int i = 0; i < file.listFiles().length; i++) {
                if ((str + ".apk").equalsIgnoreCase("" + file.list()[i])) {
                    try {
                        PackageInfo packageArchiveInfo = getPackageManager().getPackageArchiveInfo("" + listFiles[i].getPath(), 0);
                        if (!str2.equalsIgnoreCase("" + packageArchiveInfo.packageName)) {
                            return str + str2;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return str;
    }


    public void deleteFolder(String str) {
        try {
            File file = new File(str);
            String[] list = file.list();
            if (file.exists() && list != null) {
                for (String str2 : list) {
                    File file2 = new File(file, str2);
                    if (file2.isDirectory()) {
                        deleteFolder(file2.getPath());
                    } else {
                        file2.delete();
                    }
                }
            }
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getRestoreData(String str) {
        File[] listFiles;
        Util.appendLogmobiclean(this.TAG, "getRestoreData()>>>>>>>", GlobalData.FILE_NAME);
        this.restoreData.clear();
        PackageManager packageManager = getPackageManager();
        File file = new File(str);
        if (file.exists() && (listFiles = file.listFiles()) != null && listFiles.length != 0) {
            for (File file2 : listFiles) {
                String extension = FilenameUtils.getExtension(file2.getPath());
                if (file2.isFile() && extension.equalsIgnoreCase("apk")) {
                    RestoreWrapper restoreWrapper = new RestoreWrapper();
                    try {
                        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo("" + file2.getPath(), 0);
                        restoreWrapper.packagename = packageArchiveInfo.packageName;
                        restoreWrapper.size = file2.length();
                        try {
                            restoreWrapper.appname = packageArchiveInfo.applicationInfo.loadLabel(packageManager).toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        restoreWrapper.path = file2.getPath();
                        this.restoreData.add(restoreWrapper);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        try {
            Collections.sort(this.restoreData, new Comparator<RestoreWrapper>() {
                @Override
                public int compare(RestoreWrapper restoreWrapper2, RestoreWrapper restoreWrapper3) {
                    return restoreWrapper2.appname.compareToIgnoreCase(restoreWrapper3.appname);
                }
            });
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    private void getSDCARDSize() {
        String str;
        Iterator it = Arrays.asList("MicroSD", "external_SD", "sdcard1", "ext_card", "external_sd", "ext_sd", "external", "extSdCard", "externalSdCard").iterator();
        while (true) {
            if (!it.hasNext()) {
                str = null;
                break;
            }
            String str2 = (String) it.next();
            File file = new File("/mnt/", str2);
            if (file.isDirectory() && file.canWrite()) {
                str = file.getAbsolutePath();
                break;
            }
            File file2 = new File("/storage/", str2);
            if (file2.isDirectory() && file2.canWrite()) {
                str = file2.getAbsolutePath();
                break;
            }
            File file3 = new File("/storage/emulated", str2);
            if (file3.isDirectory() && file3.canWrite()) {
                str = file3.getAbsolutePath();
                break;
            }
        }
        if (str != null) {
            StatFs statFs = new StatFs(str);
            if (Build.VERSION.SDK_INT >= 18) {
                long blockSizeLong = statFs.getBlockSizeLong();
                this.totalSizeSDCARD = statFs.getBlockCountLong() * blockSizeLong;
                this.availableSizeSDCARD = statFs.getAvailableBlocksLong() * blockSizeLong;
            }
        }
    }


    public void getpackageSize() {
        this.installedApps = new AppDetails(this.context).getInstalledUserApps();
        PackageManager packageManager = getPackageManager();
        if (Build.VERSION.SDK_INT <= 25) {
            for (int i = 0; i < this.installedApps.size(); i++) {
                try {
                    ArrayList<PackageInfoStruct> arrayList = this.installedApps;
                    this.lastpname = arrayList.get(arrayList.size() - 1).pname;
                    packageManager.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class).invoke(packageManager, this.installedApps.get(i).pname, new cachePackState());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Util.appendLogmobiclean(this.TAG, "Illegal Access Exception", "");
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    Util.appendLogmobiclean(this.TAG, "Illegal Argument Exception", "");
                } catch (NoSuchMethodException e3) {
                    e3.printStackTrace();
                    Util.appendLogmobiclean(this.TAG, "No Such Method Exception", "");
                } catch (SecurityException e4) {
                    e4.printStackTrace();
                    Util.appendLogmobiclean(this.TAG, "Security Exception", "");
                } catch (InvocationTargetException e5) {
                    e5.printStackTrace();
                    Util.appendLogmobiclean(this.TAG, "Invocation Target Exception", "");
                }
            }
            if (this.installedApps.size() == 0) {
                this.lastPackagefound = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ApplicationManageActivity.this.appsize.setText(R.string.mbc_noappfound);
                            ApplicationManageActivity.this.apptexttv.setText(R.string.mbc_onthisdevies);
                        } catch (Exception e6) {
                            e6.printStackTrace();
                        }
                    }
                });
            }
            while (!this.lastPackagefound) {
                Util.appendLogmobiclean(this.TAG, "getpackageSize()>>>>>>>" + this.installedApps.size(), GlobalData.FILE_NAME);
            }
            return;
        }
        for (int i2 = 0; i2 < this.installedApps.size(); i2++) {
            try {
                File file = new File(this.installedApps.get(i2).apkPath);
                this.installedApps.get(i2).appsize = file.length();
                this.installedApps.get(i2).apksize = file.length();
                this.o += this.installedApps.get(i2).appsize;
            } catch (Exception e6) {
                e6.printStackTrace();
                return;
            }
        }
    }

    private void init() {

        this.hiddenPermissionLayout = (RelativeLayout) findViewById(R.id.hiddenpermissionlayout);
        this.j = (TextView) findViewById(R.id.textview_NOAPPS);
        this.listView = (ListView) findViewById(R.id.listviewapps);
        this.backupTab = (Button) findViewById(R.id.bachuptab);
        this.restoreTab = (Button) findViewById(R.id.restoretab);
        this.btn1 = (Button) findViewById(R.id.app_btn1);
        this.btn2 = (Button) findViewById(R.id.app_btn2);
        this.topLayout = (LinearLayout) findViewById(R.id.appmanager_toplayout);
        this.appsize = (TextView) findViewById(R.id.app_tvsize);
        this.appsize_unit = (TextView) findViewById(R.id.app_tvsize_unit);
        this.apptexttv = (TextView) findViewById(R.id.app_manager_tvtext);
        this.parent = (RelativeLayout) findViewById(R.id.appmanager_parent);
        this.btnLayout = (LinearLayout) findViewById(R.id.btnlayout);
        ((RelativeLayout) findViewById(R.id.rl_permission_close_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationManageActivity.this.hiddenPermissionLayout.setVisibility(View.GONE);
            }
        });
    }

    private boolean isInstalled(String str) {
        for (int i = 0; i < this.installedApps.size(); i++) {
            String str2 = this.installedApps.get(i).pname;
            if (str2.equalsIgnoreCase("" + str)) {
                return true;
            }
        }
        return false;
    }

    private void openFolder() {
        Intent intent = new Intent("android.intent.action.VIEW");
        Uri uriForFile = FileProvider.getUriForFile(this.context, "com.mobiclean.phoneclean.fileprovider", new File("" + Environment.getExternalStorageDirectory() + GlobalData.backuppath));
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uriForFile, "vnd.android.document/directory");
        if (intent.resolveActivityInfo(this.context.getPackageManager(), 0) != null) {
            this.context.startActivity(intent);
        } else {
            Toast.makeText(this.context, "No file explorer found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void redirectToNoti() {
        this.noti_result_back = getIntent().getBooleanExtra(GlobalData.NOTI_RESULT_BACK, false);
    }

    private void redirectToSettings() {
        Intent intent = new Intent();
        int i = Build.VERSION.SDK_INT;
        if (i >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts(SCHEME, "com.mobiclean.phoneclean", null));
        } else {
            String str = i == 8 ? APP_PKG_NAME_22 : APP_PKG_NAME_21;
            intent.setAction("android.intent.action.VIEW");
            intent.setClassName(APP_DETAILS_PACKAGE_NAME, APP_DETAILS_CLASS_NAME);
            intent.putExtra(str, "com.mobiclean.phoneclean");
        }
        try {
            this.context.startActivity(intent);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ApplicationManageActivity.this.showTutorialScreen();
                }
            }, 500L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshTask() {
        RelativeLayout relativeLayout;
        setDeviceDimensions();
        GetAllAppsDetail();
        if (!super.checkStoragePermissions() || (relativeLayout = this.hiddenPermissionLayout) == null) {
            return;
        }
        relativeLayout.setVisibility(View.GONE);
    }


    public void restoreView() {
        this.j.setVisibility(View.INVISIBLE);
        this.listView.setVisibility(View.VISIBLE);
        for (int i = 0; i < this.restoreData.size(); i++) {
            this.restoreData.get(i).ischecked = false;
        }
        this.restoreTab.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.scroll);
        this.backupTab.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        Button button = this.btn1;
        button.setText("" + getResources().getString(R.string.mbc_restore));
        Button button2 = this.btn2;
        button2.setText("" + getResources().getString(R.string.mbc_delete));
        getRestoreData("" + Environment.getExternalStorageDirectory() + GlobalData.backuppath);
        RestoreAdapter restoreAdapter = new RestoreAdapter(this.context, R.layout.junklistitemlayout, this.restoreData);
        this.restoreTab.setText(String.format(getResources().getString(R.string.mbc_archived).replace("DO_NOT_TRANSLATE", "%d"), Integer.valueOf(this.restoreData.size())));
        this.listView.setAdapter((ListAdapter) restoreAdapter);
        restoreAdapter.setNotifyOnChange(true);
        this.btn1.setVisibility(View.VISIBLE);
        this.btn2.setVisibility(View.VISIBLE);
        this.btnLayout.setVisibility(View.VISIBLE);
        if (this.restoreData.size() > 0) {
            return;
        }
        this.listView.setVisibility(View.GONE);
        this.j.setVisibility(View.VISIBLE);
        this.j.setText(R.string.mbc_noappstores);
    }

    private void setDeviceDimensions() {
        if (ParentActivity.displaymetrics == null) {
            ParentActivity.displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(ParentActivity.displaymetrics);
        }
        this.deviceHeight = ParentActivity.displaymetrics.heightPixels;
    }

    private void setDimensions() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.topLayout.getLayoutParams();
        layoutParams.height = (this.deviceHeight * 14) / 100;
        this.topLayout.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.listView.getLayoutParams();
        layoutParams2.height = ((this.deviceHeight * 90) / 100) - (Util.actionbarsize(this.context) * 4);
        this.listView.setLayoutParams(layoutParams2);
    }

    private void setListners() {
        this.backupTab.setOnClickListener(this);
        this.restoreTab.setOnClickListener(this);
        this.btn1.setOnClickListener(this);
        this.btn2.setOnClickListener(this);
    }

    private void showAdsFullDialog(String str, String str2, final String str3, Drawable drawable, final int i) {
        try {
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
            dialog.findViewById(R.id.dialog_img).setVisibility(View.GONE);
           ((TextView) dialog.findViewById(R.id.dialog_title)).setText(str);
            ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(str2);
            if (str3.equalsIgnoreCase("DP")) {
                ((TextView) dialog.findViewById(R.id.ll_yes_txt)).setText(R.string.mbc_yes);
                ((TextView) dialog.findViewById(R.id.ll_no_txt)).setText(R.string.mbc_no);
            } else {
                ((TextView) dialog.findViewById(R.id.ll_yes_txt)).setText(R.string.mbc_ok);
                ((TextView) dialog.findViewById(R.id.ll_no_txt)).setText(R.string.mbc_cancel_button_label);
            }
            dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ApplicationManageActivity.this.multipleClicked() && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(View view) {
                    if (ApplicationManageActivity.this.multipleClicked()) {
                        return;
                    }
                    if (str3.equalsIgnoreCase("DP")) {
                        for (int i2 = 0; i2 < ApplicationManageActivity.this.restoreData.size(); i2++) {
                            if (((RestoreWrapper) ApplicationManageActivity.this.restoreData.get(i2)).ischecked) {
                                final File file = new File("" + ((RestoreWrapper) ApplicationManageActivity.this.restoreData.get(i2)).path);
                                if (file.exists()) {
                                    file.delete();
                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                                            intent.setData(Uri.fromFile(file));
                                            ApplicationManageActivity.this.context.sendBroadcast(intent);
                                        }
                                    });
                                }
                                final File file2 = new File("" + Environment.getExternalStorageDirectory() + GlobalData.backuppath + "icons/" + ((RestoreWrapper) ApplicationManageActivity.this.restoreData.get(i2)).packagename + ".png");
                                if (file2.exists()) {
                                    file2.delete();
                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                                            intent.setData(Uri.fromFile(file2));
                                            ApplicationManageActivity.this.context.sendBroadcast(intent);
                                        }
                                    });
                                }
                            }
                        }
                        final File file3 = new File("" + Environment.getExternalStorageDirectory() + GlobalData.backuppath + "icons/");
                        if (file3.exists() && file3.isDirectory() && file3.list() != null && file3.list().length == 0) {
                            file3.delete();
                            final File file4 = new File("" + Environment.getExternalStorageDirectory() + "/MobiClean");
                            if (file4.exists()) {
                                ApplicationManageActivity.this.deleteFolder("" + Environment.getExternalStorageDirectory() + "/MobiClean");
                            }
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                                    intent.setData(Uri.fromFile(file3));
                                    ApplicationManageActivity.this.context.sendBroadcast(intent);
                                }
                            });
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                                    intent.setData(Uri.fromFile(file4));
                                    ApplicationManageActivity.this.context.sendBroadcast(intent);
                                }
                            });
                        }
                        @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) Snackbar make = Snackbar.make(ApplicationManageActivity.this.parent, Html.fromHtml(String.format(ApplicationManageActivity.this.getResources().getString(R.string.mbc_app_backup_deleted), Util.convertBytes(ApplicationManageActivity.this.k))), -1);
                        ((TextView) make.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
                        make.show();
                        ApplicationManageActivity.this.restoreView();
                    } else if (!str3.equalsIgnoreCase("rr")) {
                        ApplicationManageActivity.super.requestAppPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, R.string.mbc_runtime_permissions_txt, ApplicationManageActivity.REQUEST_PERMISSIONS);
                    } else {
                        ApplicationManageActivity.this.n++;
                        try {
                            File file5 = new File(((RestoreWrapper) ApplicationManageActivity.this.restoreData.get(i)).path);
                            if (Build.VERSION.SDK_INT > 23) {
                                ApplicationManageActivity.this.startActivity(new Intent("android.intent.action.VIEW").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(3).setDataAndType(FileProvider.getUriForFile(ApplicationManageActivity.this.context, "com.mobiclean.phoneclean.fileprovider", file5), "application/vnd.android.package-archive"));
                            } else {
                                Intent intent = new Intent("android.intent.action.VIEW");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setDataAndType(Uri.fromFile(file5), "application/vnd.android.package-archive");
                                ApplicationManageActivity.this.startActivity(intent);
                            }
                        } catch (Exception unused) {
                            File file6 = new File(((RestoreWrapper) ApplicationManageActivity.this.restoreData.get(i)).path);
                            if (Build.VERSION.SDK_INT > 23) {
                                ApplicationManageActivity.this.startActivity(new Intent("android.intent.action.VIEW").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(3).setDataAndType(FileProvider.getUriForFile(ApplicationManageActivity.this.context, "com.mobiclean.phoneclean.fileprovider", file6), "application/vnd.android.package-archive"));
                            } else {
                                Intent intent2 = new Intent("android.intent.action.VIEW");
                                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent2.setDataAndType(Uri.fromFile(file6), "application/vnd.android.package-archive");
                                ApplicationManageActivity.this.startActivity(intent2);
                            }
                        }
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showTutorialScreen() {
        Intent intent = new Intent(this.context, PopUpScreen.class);
        intent.putExtra("MSG", "Tap on permissions > toggle on storage");
        startActivity(intent);
    }

    private void showdialog_app_manager(String str, String str2, String str3) {
        try {
            final Dialog dialog = new Dialog(this, R.style.AppTheme);
            dialog.requestWindowFeature(1);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
            }
            dialog.setContentView(R.layout.backpress_ads_screen);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            }
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = displayMetrics.widthPixels;
            layoutParams.height = displayMetrics.heightPixels;
            dialog.getWindow().setAttributes(layoutParams);
            FrameLayout frameLayout = (FrameLayout) dialog.findViewById(R.id.frame_mid_laysss);
            TextView button = (TextView) dialog.findViewById(R.id.ads_btn_countinue);
            TextView button2 = (TextView) dialog.findViewById(R.id.ads_btn_cancel);
            button2.setTextColor(Color.parseColor("#2d54c6"));
            button.setTextColor(Color.parseColor("#7590e0"));
            button.setText(getString(R.string.mbc_ok));
            ((TextView) dialog.findViewById(R.id.dialog_title)).setText("" + str2);
            ((TextView) dialog.findViewById(R.id.dialog_msg)).setText("" + str3);
            button2.setVisibility(View.GONE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ApplicationManageActivity.this.multipleClicked()) {
                        return;
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void writeBitmap(Drawable drawable, String str) throws IOException {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        File file = new File(Environment.getExternalStorageDirectory() + GlobalData.backuppath + "icons");
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file + DialogConfigs.DIRECTORY_SEPERATOR + str + ".png");
        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void copyFile(String str, String str2, String str3) {
        try {
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileInputStream fileInputStream = new FileInputStream(str);
            FileOutputStream fileOutputStream = new FileOutputStream(str2 + DialogConfigs.DIRECTORY_SEPERATOR + str3 + ".apk");
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    return;
                }
            }
        } catch (Exception unused) {
        }
    }


    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1) {
            if (i2 == 0) {
                Log.e("------", "User pressed 'Done' button");
            } else if (i2 == -1) {
                Log.e("------", "User pressed 'Open' button");
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override
    public void onBackPressed() {
        if (this.noti_result_back) {
            startActivity(new Intent(this.context, HomeActivity.class));
            return;
        }
        Log.e("GAME", "SIMPLE TEST");
        finish();
        Dialog dialog = this.dialog;
        if (dialog != null) {
            dialog.dismiss();
            this.dialog.hide();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (multipleClicked()) {
            return;
        }
        switch (view.getId()) {
            case R.id.app_btn1:
                Util.appendLogmobiclean(this.TAG, "BackUP Button OR REstore Button Click", GlobalData.FILE_NAME);
                if (!super.checkStoragePermissions()) {
                    super.requestAppPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, R.string.mbc_runtime_permissions_txt, REQUEST_PERMISSIONS);
                    return;
                } else if (SystemClock.elapsedRealtime() - this.mLastClickTime < 1000) {
                    return;
                } else {
                    actionBtn1();
                    this.mLastClickTime = SystemClock.elapsedRealtime();
                    return;
                }
            case R.id.app_btn2:
                Util.appendLogmobiclean(this.TAG, "Unistall Button OR DELETE Button Click", GlobalData.FILE_NAME);
                if (SystemClock.elapsedRealtime() - this.mLastClickTime < 1000) {
                    return;
                }
                actionBtn2();
                this.mLastClickTime = SystemClock.elapsedRealtime();
                return;
            case R.id.bachuptab:
                Util.appendLogmobiclean(this.TAG, "bachuptab Button Click", GlobalData.FILE_NAME);
                backupView();
                return;
            case R.id.restoretab:
                Util.appendLogmobiclean(this.TAG, "restoretab Button Click", GlobalData.FILE_NAME);
                if (!super.checkStoragePermissions()) {
                    super.requestAppPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, R.string.mbc_runtime_permissions_txt, REQUEST_PERMISSIONS);
                    return;
                } else {
                    restoreView();
                    return;
                }
            default:
                return;
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_app_manager_screen);
        this.context = this;

        setDeviceDimensions();
        getWindow().addFlags(128);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        init();
        redirectToNoti();
        setListners();
        setDimensions();
        TextView textView = this.appsize;
        textView.setTextSize(0, DetermineTextSize.determineTextSize(textView.getTypeface(), (this.deviceHeight * 10) / 100));
        TextView textView2 = this.appsize_unit;
        textView2.setTextSize(0, DetermineTextSize.determineTextSize(textView2.getTypeface(), (this.deviceHeight * 5) / 100));
        TextView textView3 = this.apptexttv;
        textView3.setTextSize(0, DetermineTextSize.determineTextSize(textView3.getTypeface(), (this.deviceHeight * 4) / 100));
        this.hiddenPermissionLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        GlobalData.onAppmanager = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalData.onAppmanager = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        RelativeLayout relativeLayout;
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        } else {
            setDeviceDimensions();
            GetAllAppsDetail();
            if (super.checkStoragePermissions() && (relativeLayout = this.hiddenPermissionLayout) != null) {
                relativeLayout.setVisibility(View.GONE);
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onPermissionsGranted(int i) {
        RelativeLayout relativeLayout = this.hiddenPermissionLayout;
        if (relativeLayout != null) {
            relativeLayout.setVisibility(View.GONE);
            this.context = this;
            setDeviceDimensions();
            GetAllAppsDetail();
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i == MY_PERMISSIONS_REQUEST_CODE2 && iArr.length > 0 && iArr[0] == 0) {
            backup();
        }
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    @Override
    public void onResume() {
        this.context = this;
        refreshTask();
        super.onResume();
    }
}
