package com.cleanPhone.mobileCleaner.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.PackageInfoStruct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ApplicationFragmentView extends Fragment implements View.OnClickListener {
    private static final int REQUEST_PERMISSIONS = 811;
    private static Context cont;
    private static ApplicationAdapter mAdapter;
    public View V;
    private long availableSizeSDCARD;
    private ArrayList<PackageInfoStruct> installedApps;
    private boolean isBackup = false;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mediaNameRecyclerView;
    private MobiClean mobiclean;
    private long totalSizeSDCARD;
    public LinearLayout pbarlayout;
    public int admob = 2;


    public static class AppFragmentUninstallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                try {
                    MobiClean.getInstance().appManagerData.removeFromInstalled(intent.getStringExtra("APP"));
                    ApplicationFragmentView.mAdapter.notifyDataSetChanged();
                    ((TextView) ((AppCompatActivity) ApplicationFragmentView.cont).findViewById(R.id.junkdisplay_sizetv)).setText("" + Util.convertBytes_only(MobiClean.getInstance().appManagerData.totsize));
                    ((TextView) ((AppCompatActivity) ApplicationFragmentView.cont).findViewById(R.id.junkdisplay_sizetv_unit)).setText("" + Util.convertBytes_unit(MobiClean.getInstance().appManagerData.totsize));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void backUpApp() {
        if (permissionForStorageGiven()) {
            int i = 0;
            while (true) {
                if (i >= this.installedApps.size()) {
                    break;
                } else if (this.installedApps.get(i).ischecked) {
                    this.isBackup = true;
                    break;
                } else {
                    i++;
                }
            }
            if (this.isBackup) {
                backup();
                this.isBackup = false;
                return;
            }
            Toast.makeText(getActivity(), getResources().getString(R.string.mbc_selectapp), Toast.LENGTH_SHORT).show();
            return;
        }
        ((ApplicationManagerActivity) getActivity()).requestAppPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, R.string.mbc_runtime_permissions_txt, REQUEST_PERMISSIONS);
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
                public ProgressDialog pdialog;
                public int tot;

                @Override
                public void onPreExecute() {
                    super.onPreExecute();
                    this.pdialog = new ProgressDialog(ApplicationFragmentView.this.getActivity());
                    ApplicationFragmentView.this.getActivity().getWindow().addFlags(2097280);
                    this.pdialog.setMax(ApplicationFragmentView.this.mobiclean.appManagerData.totSelectedForBackup);
                    pbarlayout.setVisibility(View.VISIBLE);
                }

                @Override
                public String doInBackground(String... strArr) {
                    for (int i = 0; i < ApplicationFragmentView.this.installedApps.size(); i++) {
                        if (((PackageInfoStruct) ApplicationFragmentView.this.installedApps.get(i)).ischecked) {
                            this.tot++;
                            ApplicationFragmentView applicationFragmentView = ApplicationFragmentView.this;
                            StringBuilder sb = new StringBuilder();
                            sb.append("");
                            ApplicationFragmentView applicationFragmentView2 = ApplicationFragmentView.this;
                            sb.append(applicationFragmentView2.checkName(((PackageInfoStruct) applicationFragmentView2.installedApps.get(i)).appname, ((PackageInfoStruct) ApplicationFragmentView.this.installedApps.get(i)).pname));
                            applicationFragmentView.copyFile(((PackageInfoStruct) applicationFragmentView.installedApps.get(i)).apkPath, "" + Environment.getExternalStorageDirectory() + GlobalData.backuppath, sb.toString());
                            try {
                                ApplicationFragmentView applicationFragmentView3 = ApplicationFragmentView.this;
                                applicationFragmentView3.writeBitmap(applicationFragmentView3.getActivity().getPackageManager().getApplicationIcon(((PackageInfoStruct) ApplicationFragmentView.this.installedApps.get(i)).pname), "" + ((PackageInfoStruct) ApplicationFragmentView.this.installedApps.get(i)).pname);
                                ApplicationFragmentView.this.mobiclean.appManagerData.addToBackedUp((PackageInfoStruct) ApplicationFragmentView.this.installedApps.get(i));
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            publishProgress("" + this.tot);
                        }
                    }
                    return null;
                }

                @Override
                public void onPostExecute(String str) {
                    ApplicationFragmentView.this.mobiclean.appManagerData.totSelectedForBackup = 0;
                    if (this.tot == 0) {
                        Toast.makeText(ApplicationFragmentView.this.getActivity(), ApplicationFragmentView.this.getResources().getString(R.string.mbc_selectapp), Toast.LENGTH_SHORT).show();
                    } else if (Util.isConnectingToInternet(ApplicationFragmentView.this.getActivity())) {
                        ApplicationFragmentView applicationFragmentView = ApplicationFragmentView.this;
                        FragmentActivity activity = applicationFragmentView.getActivity();
                        String string = ApplicationFragmentView.this.getResources().getString(R.string.mbc_app_manager);
                        String string2 = ApplicationFragmentView.this.getResources().getString(R.string.mbc_backup_done);
                        String replace = ApplicationFragmentView.this.getResources().getString(R.string.mbc_application_file).replace("DO_NOT_TRANSLATE", "%s");

                        applicationFragmentView.showdialog_app_manager(activity, string, string2, String.format(replace, Environment.getExternalStorageDirectory() + GlobalData.backuppath));
                    } else {
                        FragmentActivity activity2 = ApplicationFragmentView.this.getActivity();
                        String replace2 = ApplicationFragmentView.this.getResources().getString(R.string.mbc_application_file).replace("DO_NOT_TRANSLATE", "%s");
                        Toast.makeText(activity2, String.format(replace2, Environment.getExternalStorageDirectory() + GlobalData.backuppath), Toast.LENGTH_LONG).show();
                    }
                    this.tot = 0;
                    pbarlayout.setVisibility(View.GONE);
                    ApplicationFragmentView.this.mobiclean.appManagerData.uncheckAllInstalled();
                    ApplicationFragmentView.this.mobiclean.appManagerData.uncheckAllBackedUp();
                    ApplicationFragmentView.mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onProgressUpdate(String... strArr) {
                    super.onProgressUpdate(strArr);
                    this.pdialog.setProgress(this.tot);
                }
            }.execute(new String[0]);
        } else {
            Toast.makeText(getActivity(), String.format(getResources().getString(R.string.mbc_minimum_space), "100MB"), Toast.LENGTH_LONG).show();
        }
    }

    public String checkName(String str, String str2) {
        File file = new File(Environment.getExternalStorageDirectory() + GlobalData.backuppath);
        if (file.exists() && file.isDirectory() && file.list() != null && file.listFiles().length > 0) {
            File[] listFiles = file.listFiles();
            for (int i = 0; i < file.listFiles().length; i++) {
                if ((str + ".apk").equalsIgnoreCase("" + file.list()[i])) {
                    try {
                        PackageInfo packageArchiveInfo = getActivity().getPackageManager().getPackageArchiveInfo("" + listFiles[i].getPath(), 0);
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

    private void initialize() {
        this.V.findViewById(R.id.app_backup).setOnClickListener(this);
        this.V.findViewById(R.id.app_uninstall).setOnClickListener(this);
        pbarlayout = this.V.findViewById(R.id.pbarlayout);

        this.mobiclean = MobiClean.getInstance();
    }

    private boolean permissionForStorageGiven() {
        int i = Build.VERSION.SDK_INT;
        if (i >= 30) {
            return Environment.isExternalStorageManager();
        }
        return i < 23 || ContextCompat.checkSelfPermission(getActivity(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public void showdialog_app_manager(Context context, String str, String str2, String str3) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
        dialog.setContentView(R.layout.app_manager_ad_lay);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        ((TextView) dialog.findViewById(R.id.tv_mid)).setText(Html.fromHtml("" + str2));
        ((TextView) dialog.findViewById(R.id.tv_btm)).setText("" + str3);
        dialog.show();
    }

    private void uninstall() {
        int i = 0;
        for (int i2 = 0; i2 < this.installedApps.size(); i2++) {
            if (this.installedApps.get(i2).ischecked) {
                i++;
                Log.e("package name---------", "" + this.installedApps.get(i2));
                getActivity().startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:" + this.installedApps.get(i2).pname)));
            }
        }
        if (i == 0) {
            Toast.makeText(getActivity(), getResources().getString(R.string.mbc_select_app_uninsall), Toast.LENGTH_SHORT).show();
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
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.app_backup) {


            backUpApp();


        } else if (id != R.id.app_uninstall) {
        } else {


            uninstall();


        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.V = layoutInflater.inflate(R.layout.fragment_apps, viewGroup, false);
        cont = getActivity();
        initialize();
        pbarlayout = this.V.findViewById(R.id.pbarlayout);
        this.installedApps = this.mobiclean.appManagerData.installedApps;
        if (MobiClean.getInstance().appManagerData.installedApps.size() == 0) {
            this.V.findViewById(R.id.tv_emptybackup).setVisibility(View.VISIBLE);
            this.V.findViewById(R.id.rviewapps).setVisibility(View.GONE);
            return this.V;
        }
        mAdapter = new ApplicationAdapter(getActivity());
        this.mLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = (RecyclerView) this.V.findViewById(R.id.rviewapps);
        this.mediaNameRecyclerView = recyclerView;
        recyclerView.setLayoutManager(this.mLayoutManager);
        this.mediaNameRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mediaNameRecyclerView.setAdapter(mAdapter);
        return this.V;
    }

    @Override
    public void onResume() {
        super.onResume();
        ApplicationAdapter applicationAdapter = mAdapter;
        if (applicationAdapter != null) {
            applicationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
    }
}
