package com.cleanPhone.mobileCleaner;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.cleanPhone.mobileCleaner.filestorage.Utility;
import com.cleanPhone.mobileCleaner.listadapt.FilesDownloadAdapter;
import com.cleanPhone.mobileCleaner.similerphotos.MySharedPreference;
import com.cleanPhone.mobileCleaner.utility.FileUtil;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.MountPoints;
import com.cleanPhone.mobileCleaner.utility.PermitActivity;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.DownloadWrapper;

import java.io.File;
import java.util.ArrayList;

public class DownloadsActivity extends PermitActivity implements DialogListners {
    public static final int REQUEST_CODE_STORAGE_ACCESS_INPUT = 4422;
    private static final int REQUEST_PERMISSIONS = 660;
    private MobiClean appInstance;
    private TextView btnClean;
    private ProgressDialog displayProgress;
    private ArrayList<DownloadWrapper> downloadDeletionList;
    private RelativeLayout hiddenPermissionLayout;
    private int notDeleted = 0;
    private RecyclerView recyclerView;
    private ImageView back;
    int admob = 2;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;


    public static class AnonymousClass7 {
        public static final int[] f4597a;

        static {
            int[] iArr = new int[DELETION.values().length];
            f4597a = iArr;
            try {
                iArr[DELETION.ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f4597a[DELETION.PERMISSION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f4597a[DELETION.SUCCESS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public enum DELETION {
        SUCCESS,
        ERROR,
        PERMISSION,
        SELECTION,
        FINISH,
        NOTDELETION
    }

    private boolean deleteImageFile(DownloadWrapper downloadWrapper) {
        boolean z;
        File file = new File(downloadWrapper.path);
        if (file.exists()) {
            delete(file);
            z = !file.exists();
            if (z) {
                updateMediaScannerPath(file);
            } else {
                FileUtil.isKitKat();
            }
        } else {
            updateMediaScannerPath(file);
            z = true;
        }
        if (Build.VERSION.SDK_INT == 21) {
            return true;
        }
        return z;
    }

    @SuppressLint("StaticFieldLeak")
    private void deletion(final int i, final int i2, final Intent intent) {
        try {
            new AsyncTask<Void, Integer, DELETION>() {
                @Override
                public void onPreExecute() {
                    DownloadsActivity.this.displayProgress = new ProgressDialog(DownloadsActivity.this);
                    DownloadsActivity.this.getWindow().addFlags(2097280);
                    ProgressDialog progressDialog = DownloadsActivity.this.displayProgress;
                    progressDialog.setTitle("" + DownloadsActivity.this.getResources().getString(R.string.mbc_cleaning));
                    DownloadsActivity.this.displayProgress.setCanceledOnTouchOutside(false);
                    DownloadsActivity.this.displayProgress.setProgressStyle(1);
                    DownloadsActivity.this.displayProgress.setCancelable(false);
                    DownloadsActivity.this.displayProgress.setMax(DownloadsActivity.this.downloadDeletionList.size());
                    DownloadsActivity.this.displayProgress.show();
                }

                @Override
                @RequiresApi(api = 21)
                public DELETION doInBackground(Void... voidArr) {
                    if (FileUtil.isSystemAndroid5()) {
                        DownloadsActivity.onActivityResultLollipop(DownloadsActivity.this, i, i2, intent);
                    }
                    return DownloadsActivity.this.permissionBasedDeletion();
                }

                @Override
                public void onPostExecute(DELETION deletion) {
                    if (DownloadsActivity.this.displayProgress != null) {
                        DownloadsActivity.this.displayProgress.dismiss();
                    }
                    try {
                        DownloadsActivity.this.getWindow().clearFlags(2097280);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int i3 = AnonymousClass7.f4597a[deletion.ordinal()];
                    if (i3 == 1) {
                        Toast.makeText(DownloadsActivity.this, "", Toast.LENGTH_LONG).show();
                    } else if (i3 == 2) {
                        DownloadsActivity.this.permissionAlert();
                    } else if (i3 != 3) {
                    } else {
                        DownloadsActivity.this.successDeletion();
                    }
                }

                @Override
                public void onProgressUpdate(Integer... numArr) {
                    super.onProgressUpdate(numArr);
                    DownloadsActivity.this.displayProgress.setProgress(numArr[0].intValue());
                }
            }.execute(new Void[0]);
        } catch (Exception unused) {
        }
    }

    public static Uri getSharedPreferenceUri(int i, Context context) {
        String string = getSharedPreferences(context).getString(context.getString(i), null);
        if (string == null) {
            return null;
        }
        return Uri.parse(string);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void initIds() {
        this.hiddenPermissionLayout = (RelativeLayout) findViewById(R.id.hiddenpermissionlayout);

        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view_download);
        this.btnClean = (TextView) findViewById(R.id.downloadclean_btn);
        this.back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.rl_permission_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DownloadsActivity.this.multipleClicked()) {
                    return;
                }
                DownloadsActivity.this.finish();
            }
        });
    }

    public DELETION normalDeletion() {
        for (int i = 0; i < this.downloadDeletionList.size(); i++) {
            try {
                try {
                    if (deleteImageFile(this.downloadDeletionList.get(i))) {
                        this.appInstance.downloadsData.f4598a += this.downloadDeletionList.get(i).size;
                    }
                } catch (Exception unused) {
                    DELETION deletion = DELETION.ERROR;
                    return DELETION.SUCCESS;
                }
            } catch (Throwable unused2) {
                return DELETION.SUCCESS;
            }
        }
        return DELETION.SUCCESS;
    }

    @SuppressLint("WrongConstant")
    @TargetApi(21)
    public static void onActivityResultLollipop(Context context, int i, int i2, @NonNull Intent intent) {
        if (i == 4422) {
            if (getSharedPreferenceUri(R.string.mbc_key_internal_uri_extsdcard_input, context) == null) {
                getSharedPreferenceUri(R.string.mbc_key_internal_uri_extsdcard_photos, context);
            }
            Uri uri = null;
            if (i2 == -1) {
                uri = intent.getData();
                setSharedPreferenceUri(R.string.mbc_key_internal_uri_extsdcard_input, uri, context);
            }
            if (i2 != -1) {
                System.out.println("File is not writable");
                return;
            }
            int flags = intent.getFlags() & 3;
            if (uri != null) {
                context.getContentResolver().takePersistableUriPermission(uri, flags);
            }
        }
    }


    public void permissionAlert() {
        Splash.showdialog_sdcard(this, this);
    }


    @RequiresApi(api = 21)
    public DELETION permissionBasedDeletion() {
        for (int i = 0; i < this.downloadDeletionList.size(); i++) {
            try {
                try {
                    if (deleteImageFile(this.downloadDeletionList.get(i))) {
                        this.appInstance.downloadsData.f4598a += this.downloadDeletionList.get(i).size;
                    }
                } catch (Exception unused) {
                    DELETION deletion = DELETION.ERROR;
                    return DELETION.SUCCESS;
                }
            } catch (Throwable unused2) {
                return DELETION.SUCCESS;
            }
        }
        return DELETION.SUCCESS;
    }

    private void setAdapter() {
        String str = null;
        try {
            str = "" + getString(R.string.mbc_no) + " " + getString(R.string.mbc_download);
            if (MySharedPreference.getLngIndex(this) == 0) {
                str = "No Downloads Found";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (MobiClean.getInstance().downloadsData.downloadsList != null && MobiClean.getInstance().downloadsData.downloadsList.size() != 0) {
            FilesDownloadAdapter filesDownloadAdapter = new FilesDownloadAdapter(this, this.btnClean);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_download);
            this.recyclerView = recyclerView;
            recyclerView.setLayoutManager(linearLayoutManager);
            this.recyclerView.setItemAnimator(new DefaultItemAnimator());
            this.recyclerView.setAdapter(filesDownloadAdapter);
            try {
                this.recyclerView.addItemDecoration(new DividerItemDecoration(this.recyclerView.getContext(), 1));
                return;
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        }
        Intent intent = new Intent(this, CommonResultActivity.class);
        intent.putExtra("DATA", str);
        intent.putExtra("TYPE", "NODOWNLOAD");
        intent.putExtra("FROMLARGE", true);
        intent.putExtra("not_deleted", this.notDeleted);
            startActivity(intent);
            finish();



    }

    public static void setSharedPreferenceUri(int i, @Nullable Uri uri, Context context) {
        SharedPreferences.Editor edit = getSharedPreferences(context).edit();
        if (uri == null) {
            edit.putString(context.getString(i), null);
        } else {
            edit.putString(context.getString(i), uri.toString());
        }
        edit.apply();
    }


    public void showAdsFullDialog() {
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


            ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_junk_cleaner));
            ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_large_files));
            ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(getResources().getString(R.string.mbc_delete_large_file));
            dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (DownloadsActivity.this.multipleClicked()) {
                        return;
                    }
                    Util.appendLogmobiclean("DownloadsVertiseScreen", "method showAdsFullDialog calling cancel press", GlobalData.FILE_NAME);
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onClick(View view) {
                    if (DownloadsActivity.this.multipleClicked()) {
                        return;
                    }
                    Util.appendLogmobiclean("DownloadsVertiseScreen", "method showAdsFullDialog calling continue press", GlobalData.FILE_NAME);
                    DownloadsActivity downloadsActivity = DownloadsActivity.this;
                    downloadsActivity.downloadDeletionList = downloadsActivity.appInstance.downloadsData.a();
                    new AsyncTask<String, Integer, DELETION>() {
                        @RequiresApi(api = 21)
                        private boolean isBothStorageCanDelete(ArrayList<String> arrayList) {
                            for (int i = 0; i < arrayList.size(); i++) {
                                if (!FileUtil.isWritableNormalOrSaf(DownloadsActivity.this, new File(arrayList.get(i)))) {
                                    return false;
                                }
                            }
                            return true;
                        }

                        @Override
                        public void onPreExecute() {
                            DownloadsActivity.this.displayProgress = new ProgressDialog(DownloadsActivity.this);
                            DownloadsActivity.this.getWindow().addFlags(2097280);
                            DownloadsActivity.this.displayProgress.setTitle(R.string.mbc_cleaning);
                            DownloadsActivity.this.displayProgress.setCanceledOnTouchOutside(false);
                            DownloadsActivity.this.displayProgress.setProgressStyle(1);
                            DownloadsActivity.this.displayProgress.setCancelable(false);
                            DownloadsActivity.this.displayProgress.setMax(DownloadsActivity.this.downloadDeletionList.size());
                            super.onPreExecute();
                        }

                        @Override
                        public DELETION doInBackground(String... strArr) {
                            ArrayList<String> returnMountPOints;
                            try {
                                if (!FileUtil.IsDeletionBelow6() && (returnMountPOints = MountPoints.returnMountPOints(DownloadsActivity.this)) != null && returnMountPOints.size() != 1) {
                                    File file = new File(returnMountPOints.get(1));
                                    if (file.listFiles() == null || file.listFiles().length == 0) {
                                        return DownloadsActivity.this.normalDeletion();
                                    }
                                    if (Build.VERSION.SDK_INT > 21) {
                                        if (isBothStorageCanDelete(returnMountPOints)) {
                                            return DownloadsActivity.this.permissionBasedDeletion();
                                        }
                                        return DELETION.PERMISSION;
                                    }
                                    return DELETION.SUCCESS;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return DownloadsActivity.this.normalDeletion();
                        }

                        @Override
                        public void onPostExecute(DELETION deletion) {
                            if (DownloadsActivity.this.displayProgress != null && DownloadsActivity.this.displayProgress.isShowing()) {
                                DownloadsActivity.this.displayProgress.dismiss();
                            }
                            try {
                                DownloadsActivity.this.getWindow().clearFlags(2097280);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            int i = AnonymousClass7.f4597a[deletion.ordinal()];
                            if (i == 1) {
                                DownloadsActivity downloadsActivity2 = DownloadsActivity.this;
                                Toast.makeText(downloadsActivity2, "" + DownloadsActivity.this.getString(R.string.error), Toast.LENGTH_LONG).show();
                            } else if (i == 2) {
                                DownloadsActivity.this.permissionAlert();
                            } else if (i == 3) {
                                DownloadsActivity.this.successDeletion();
                            }
                            super.onPostExecute(deletion);
                        }

                        @Override
                        public void onProgressUpdate(Integer... numArr) {
                            super.onProgressUpdate(numArr);
                            DownloadsActivity.this.displayProgress.setProgress(numArr[0].intValue());
                        }
                    }.execute(new String[0]);
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void successDeletion() {
        Intent intent = new Intent(this, CommonResultActivity.class);
        intent.putExtra("DATA", "" + Util.convertBytes(this.appInstance.downloadsData.f4598a));
        intent.putExtra("TYPE", "Recovered");
        intent.putExtra("FROMLARGE", true);
        intent.putExtra("not_deleted", this.notDeleted);

            startActivity(intent);
            finish();



    }

    private void updateMediaScannerPath(File file) {
        if (Build.VERSION.SDK_INT >= 19) {
            MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String str, Uri uri) {
                }
            });
        } else {
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        }
    }

    @Override
    public void clickOK() {
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.putExtra("android.intent.extra.LOCAL_ONLY", true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_STORAGE_ACCESS_INPUT);
    }

    public boolean delete(File file) {
        file.delete();
        if (file.exists()) {
            String[] strArr = {file.getAbsolutePath()};
            ContentResolver contentResolver = getContentResolver();
            Uri contentUri = MediaStore.Files.getContentUri("external");
            contentResolver.delete(contentUri, "_data=?", strArr);
            if (file.exists()) {
                contentResolver.delete(contentUri, "_data=?", strArr);
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 4422 && i2 == -1) {
            deletion(i, i2, intent);
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_downloads_screen);
        this.appInstance = MobiClean.getInstance();
        AdRequest adRequest = new AdRequest.Builder().build();
        super.requestAppPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, R.string.mbc_runtime_permissions_txt, REQUEST_PERMISSIONS);
        initIds();
        if (Utility.checkStorageAccessPermissions(this)) {
            setAdapter();
        }
        this.btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DownloadsActivity.this.appInstance.downloadsData.totalSelected != 0) {
                    DownloadsActivity.this.showAdsFullDialog();
                    return;
                }
                DownloadsActivity downloadsActivity = DownloadsActivity.this;
                Toast.makeText(downloadsActivity, "" + DownloadsActivity.this.getString(R.string.mbc_pleaseselect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        finish();
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onPermissionsGranted(int i) {
        RelativeLayout relativeLayout = this.hiddenPermissionLayout;
        if (relativeLayout != null) {
            relativeLayout.setVisibility(View.GONE);
            setAdapter();
        }
    }
}
