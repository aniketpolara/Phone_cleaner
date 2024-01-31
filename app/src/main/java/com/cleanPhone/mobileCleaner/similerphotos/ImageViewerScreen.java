package com.cleanPhone.mobileCleaner.similerphotos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.cleanPhone.mobileCleaner.DialogListners;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.Splash;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.tools.FilesGridActivity;
import com.cleanPhone.mobileCleaner.utility.FileUtil;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.MountPoints;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;


public class ImageViewerScreen extends ParentActivity implements DialogListners, View.OnClickListener {
    private static final String TAG = ImageViewerScreen.class.getSimpleName();
    private TextView ads_message;
    private TextView ads_title;
    private Context context;
    private android.os.AsyncTask<String, Integer, FilesGridActivity.DELETION> deleteTask;
    private int deviceHeight;
    private int deviceWidth;
    private ProgressDialog displayProgress;
    public TextView i;
    private ImageView image;
    private TextView image_check;
    private String img_details;
    public DisplayMetrics j;
    public ImageAdapter k;
    public RelativeLayout m;
    public RelativeLayout n;
    private int notdeleted;
    public FrameLayout o;
    public TextView p;
    private CheckBox parent_check;
    private Gallery preview_gallery;
    public TextView q;
    private int totalSelectedForDelete;
    public int l = 0;
    public String dateFormat = "dd-MM-yyyy hh:mm:ss";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.dateFormat);
    private long ignoreSize = 0;
    private long updatedSize = 0;
    private int isBothChk = 0;
    public boolean s = false;

    public static class AnonymousClass14 {

        public static final int[] f5080a;

        static {
            int[] iArr = new int[FilesGridActivity.DELETION.values().length];
            f5080a = iArr;
            try {
                iArr[FilesGridActivity.DELETION.ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5080a[FilesGridActivity.DELETION.PERMISSION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f5080a[FilesGridActivity.DELETION.SUCCESS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f5080a[FilesGridActivity.DELETION.SELECTION.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f5080a[FilesGridActivity.DELETION.NOTDELETION.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f5080a[FilesGridActivity.DELETION.FINISH.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    public class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context context) {
            TypedArray obtainStyledAttributes = ImageViewerScreen.this.obtainStyledAttributes(new int[]{R.style.Gallery1});
            obtainStyledAttributes.getResourceId(0, 0);
            obtainStyledAttributes.recycle();
        }

        @Override
        public int getCount() {
            return MobiClean.getInstance().duplicatesData.currentList.size();
        }

        @Override
        public Object getItem(int i) {
            return Integer.valueOf(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView imageView = new ImageView(ImageViewerScreen.this.context);
            try {
                Glide.with((FragmentActivity) ImageViewerScreen.this).load(new File(MobiClean.getInstance().duplicatesData.currentList.get(i).path)).into(imageView);
                if (MobiClean.getInstance().duplicatesData.currentList.get(i).ischecked) {
                    imageView.setAlpha(150);
                } else {
                    imageView.setAlpha(255);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new Gallery.LayoutParams((ImageViewerScreen.this.deviceWidth * 32) / 100, (ImageViewerScreen.this.deviceHeight * 32) / 100));
            return imageView;
        }
    }

    public static long A(ImageViewerScreen imageViewerScreen, long j) {
        long j2 = imageViewerScreen.ignoreSize + j;
        imageViewerScreen.ignoreSize = j2;
        return j2;
    }

    @SuppressLint("StaticFieldLeak")
    private void deletion(final int i, final int i2, final Intent intent) {
        try {
            new android.os.AsyncTask<Void, Void, FilesGridActivity.DELETION>() {
                @Override
                public void onPreExecute() {
                    ImageViewerScreen.this.displayProgress = new ProgressDialog(ImageViewerScreen.this);
                    ImageViewerScreen.this.getWindow().addFlags(2097280);
                    ImageViewerScreen.this.displayProgress.setTitle(ImageViewerScreen.this.getString(R.string.mbc_cleaning));
                    ImageViewerScreen.this.displayProgress.setCanceledOnTouchOutside(false);
                    ImageViewerScreen.this.displayProgress.setProgressStyle(1);
                    ImageViewerScreen.this.displayProgress.setCancelable(false);
                    ImageViewerScreen.this.displayProgress.setMax(MobiClean.getInstance().duplicatesData.selectedForDelete().size());
                    ImageViewerScreen.this.displayProgress.show();
                }

                @Override
                @RequiresApi(api = 21)
                public FilesGridActivity.DELETION doInBackground(Void... voidArr) {
                    if (FileUtil.isSystemAndroid5()) {
                        FilesGridActivity.onActivityResultLollipop(ImageViewerScreen.this, i, i2, intent);
                    }
                    return ImageViewerScreen.this.permissionBasedDeletion();
                }

                @Override
                public void onPostExecute(FilesGridActivity.DELETION deletion) {
                    if (ImageViewerScreen.this.displayProgress != null) {
                        ImageViewerScreen.this.displayProgress.dismiss();
                    }
                    ImageViewerScreen.this.getWindow().clearFlags(128);
                    int i3 = AnonymousClass14.f5080a[deletion.ordinal()];
                    if (i3 == 1) {
                        Toast.makeText(ImageViewerScreen.this, "", Toast.LENGTH_LONG).show();
                    } else if (i3 == 2) {
                        ImageViewerScreen.this.permissionAlert();
                    } else if (i3 == 3) {
                        ImageViewerScreen.this.successDeletion();
                    } else if (i3 == 5) {
                        ImageViewerScreen.this.successDeletion();
                    } else if (i3 != 6) {
                    } else {
                        ImageViewerScreen.this.finish();
                    }
                }
            }.execute(new Void[0]);
        } catch (Exception e) {
            String str = TAG;
            Util.appendLogmobiclean(str, "Normal deletion exception====" + e.getMessage(), GlobalData.FILE_NAME);
        }
    }


    @SuppressLint("StaticFieldLeak")
    public void deletionTask() {
        Util.appendLogmobiclean(TAG, "method deletionTask calling", GlobalData.FILE_NAME);
        this.deleteTask = new android.os.AsyncTask<String, Integer, FilesGridActivity.DELETION>() {
            private boolean deleteImageFile(File file) {
                boolean z;
                if (!file.exists()) {
                    ImageViewerScreen.this.updateMediaScannerPath(file);
                    z = true;
                } else {
                    delete(file);
                    z = !file.exists();
                    if (z) {
                        ImageViewerScreen.this.updateMediaScannerPath(file);
                    } else {
                        FileUtil.isKitKat();
                    }
                }
                if (Build.VERSION.SDK_INT == 21) {
                    return true;
                }
                return z;
            }

            @RequiresApi(api = 21)
            private boolean isBothStorageCanDelete(ArrayList<String> arrayList) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (!FileUtil.isWritableNormalOrSaf(ImageViewerScreen.this, new File(arrayList.get(i)))) {
                        return false;
                    }
                }
                return true;
            }

            private FilesGridActivity.DELETION normalDeletion() {
                try {
                    try {
                        ImageDetail imageDetail = MobiClean.getInstance().duplicatesData.currentList.get(ImageViewerScreen.this.preview_gallery.getSelectedItemPosition());
                        if (deleteImageFile(new File(imageDetail.path))) {
                            Util.appendLogmobiclean(ImageViewerScreen.TAG, "normal delete success with path===" + imageDetail.path, "");
                            ImageViewerScreen.this.displayProgress.setProgress(1);
                            ImageViewerScreen.this.isBothChk = 0;
                            ImageViewerScreen.this.ignoreSize = 0L;
                            if (MobiClean.getInstance().duplicatesData.currentList.size() == 2) {
                                Iterator<ImageDetail> it = MobiClean.getInstance().duplicatesData.currentList.iterator();
                                while (it.hasNext()) {
                                    ImageDetail next = it.next();
                                    ImageViewerScreen.A(ImageViewerScreen.this, next.size);
                                    if (next.ischecked) {
                                        ImageViewerScreen imageViewerScreen = ImageViewerScreen.this;
                                        imageViewerScreen.s = true;
                                        ImageViewerScreen.y(imageViewerScreen);
                                    }
                                }
                            } else if (MobiClean.getInstance().duplicatesData.currentList.size() >= 2) {
                                ImageViewerScreen.this.ignoreSize = imageDetail.size;
                                if (imageDetail.ischecked) {
                                    ImageViewerScreen.this.s = true;
                                }
                            }
                            if (imageDetail.ischecked) {
                                ImageViewerScreen.this.updatedSize = imageDetail.size;
                            }
                            MobiClean.getInstance().duplicatesData.removeNode(imageDetail);
                            MobiClean.getInstance().duplicatesData.currentList.remove(ImageViewerScreen.this.preview_gallery.getSelectedItemPosition());
                            MobiClean.getInstance().duplicatesData.grouplist.get(MobiClean.getInstance().duplicatesData.currentGroupIndex - 1).children.remove(ImageViewerScreen.this.preview_gallery.getSelectedItemPosition());
                        } else {
                            ImageViewerScreen.this.notdeleted++;
                            Util.appendLogmobiclean(ImageViewerScreen.TAG, "normal delete failed with path===" + imageDetail.path, "");
                        }
                        MobiClean.getInstance().duplicatesData.isBackAfterPreviewDelete = true;
                        if (ImageViewerScreen.this.notdeleted > 0) {
                            return FilesGridActivity.DELETION.NOTDELETION;
                        }
                        if (MobiClean.getInstance().duplicatesData.currentList.size() == 1) {
                            return FilesGridActivity.DELETION.FINISH;
                        }
                        return FilesGridActivity.DELETION.SUCCESS;
                    } catch (Exception e) {
                        Util.appendLogmobiclean(ImageViewerScreen.TAG, "Normal deletion exception====" + e.getMessage(), GlobalData.FILE_NAME);
                        FilesGridActivity.DELETION deletion = FilesGridActivity.DELETION.ERROR;
                        MobiClean.getInstance().duplicatesData.isBackAfterPreviewDelete = true;
                        if (ImageViewerScreen.this.notdeleted > 0) {
                            return FilesGridActivity.DELETION.NOTDELETION;
                        }
                        if (MobiClean.getInstance().duplicatesData.currentList.size() == 1) {
                            return FilesGridActivity.DELETION.FINISH;
                        }
                        return FilesGridActivity.DELETION.SUCCESS;
                    }
                } catch (Throwable unused) {
                    MobiClean.getInstance().duplicatesData.isBackAfterPreviewDelete = true;
                    if (ImageViewerScreen.this.notdeleted > 0) {
                        return FilesGridActivity.DELETION.NOTDELETION;
                    }
                    if (MobiClean.getInstance().duplicatesData.currentList.size() == 1) {
                        return FilesGridActivity.DELETION.FINISH;
                    }
                    return FilesGridActivity.DELETION.SUCCESS;
                }
            }

            public boolean delete(File file) {
                file.delete();
                if (file.exists()) {
                    String[] strArr = {file.getAbsolutePath()};
                    ContentResolver contentResolver = ImageViewerScreen.this.getContentResolver();
                    Uri contentUri = MediaStore.Files.getContentUri("external");
                    contentResolver.delete(contentUri, "_data=?", strArr);
                    if (file.exists()) {
                        contentResolver.delete(contentUri, "_data=?", strArr);
                    }
                }
                return true;
            }

            @Override
            public void onPreExecute() {
                ImageViewerScreen.this.notdeleted = 0;
                super.onPreExecute();
                Util.appendLogmobiclean(ImageViewerScreen.TAG, "method deletionTask pre execute calling", GlobalData.FILE_NAME);
                ImageViewerScreen.this.displayProgress = new ProgressDialog(ImageViewerScreen.this);
                ImageViewerScreen.this.getWindow().addFlags(2097280);
                ImageViewerScreen.this.displayProgress.setTitle(ImageViewerScreen.this.getString(R.string.mbc_cleaning));
                ImageViewerScreen.this.displayProgress.setCanceledOnTouchOutside(false);
                ImageViewerScreen.this.displayProgress.setProgressStyle(1);
                ImageViewerScreen.this.displayProgress.setCancelable(false);
                ImageViewerScreen.this.displayProgress.setMax(1);
                ImageViewerScreen.this.displayProgress.show();
            }

            @Override
            public FilesGridActivity.DELETION doInBackground(String... strArr) {
                Util.appendLogmobiclean(ImageViewerScreen.TAG, "method deletionTask doinbackground calling", GlobalData.FILE_NAME);
                if (strArr != null) {
                    return ImageViewerScreen.this.permissionBasedDeletion();
                }
                if (FileUtil.IsDeletionBelow6()) {
                    Util.appendLogmobiclean(ImageViewerScreen.TAG, "method deletionTask os below 5.0", GlobalData.FILE_NAME);
                    return normalDeletion();
                }
                ArrayList<String> returnMountPOints = MountPoints.returnMountPOints(ImageViewerScreen.this);
                if (returnMountPOints == null) {
                    Util.appendLogmobiclean(ImageViewerScreen.TAG, "mount points arr is null", GlobalData.FILE_NAME);
                    return normalDeletion();
                } else if (returnMountPOints.size() == 1) {
                    Util.appendLogmobiclean(ImageViewerScreen.TAG, "mount points arr size is 1", GlobalData.FILE_NAME);
                    return normalDeletion();
                } else {
                    String str = ImageViewerScreen.TAG;
                    Util.appendLogmobiclean(str, "mount points arr size is " + returnMountPOints.size(), GlobalData.FILE_NAME);
                    File file = new File(returnMountPOints.get(1));
                    if (file.listFiles() != null && file.listFiles().length != 0) {
                        if (Build.VERSION.SDK_INT > 21) {
                            if (!isBothStorageCanDelete(returnMountPOints)) {
                                Util.appendLogmobiclean(ImageViewerScreen.TAG, "file is not able to write on external.Taking Permission", GlobalData.FILE_NAME);
                                return FilesGridActivity.DELETION.PERMISSION;
                            }
                            Util.appendLogmobiclean(ImageViewerScreen.TAG, "file is able to write on external", GlobalData.FILE_NAME);
                            return ImageViewerScreen.this.permissionBasedDeletion();
                        }
                        return FilesGridActivity.DELETION.SUCCESS;
                    }
                    return normalDeletion();
                }
            }

            @Override
            public void onPostExecute(FilesGridActivity.DELETION deletion) {
                super.onPostExecute(deletion);
                String str = ImageViewerScreen.TAG;
                Util.appendLogmobiclean(str, "method deletionTask onPostExecute() calling with status====" + deletion.name(), GlobalData.FILE_NAME);
                if (ImageViewerScreen.this.displayProgress != null) {
                    ImageViewerScreen.this.displayProgress.dismiss();
                }
                ImageViewerScreen.this.getWindow().clearFlags(128);
                switch (AnonymousClass14.f5080a[deletion.ordinal()]) {
                    case 1:
                        ImageViewerScreen imageViewerScreen = ImageViewerScreen.this;
                        Toast.makeText(imageViewerScreen, imageViewerScreen.getString(R.string.mbc_deletion_err), Toast.LENGTH_LONG).show();
                        return;
                    case 2:
                        ImageViewerScreen.this.permissionAlert();
                        return;
                    case 3:
                        ImageViewerScreen.this.successDeletion();
                        return;
                    case 4:
                        ImageViewerScreen imageViewerScreen2 = ImageViewerScreen.this;
                        Toast.makeText(imageViewerScreen2, imageViewerScreen2.getString(R.string.mbc_pleaseselect), Toast.LENGTH_SHORT).show();
                        return;
                    case 5:
                        ImageViewerScreen.this.successDeletion();
                        return;
                    case 6:
                        ImageViewerScreen.this.H();
                        if (MobiClean.getInstance().duplicatesData.currentList.size() == 1) {
                            MobiClean.getInstance().duplicatesData.grouplist.remove(MobiClean.getInstance().duplicatesData.currentGroupIndex - 1);
                            ImageViewerScreen.this.setResult(-1);
                            ImageViewerScreen.this.finish();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }

            @Override
            public void onProgressUpdate(Integer... numArr) {
                super.onProgressUpdate(numArr);
                ImageViewerScreen.this.displayProgress.setProgress(numArr[0].intValue());
            }
        };
        ArrayList<String> returnMountPOints = MountPoints.returnMountPOints(this);
        boolean z = true;
        if (returnMountPOints != null && returnMountPOints.size() > 1) {
            File file = new File(returnMountPOints.get(1));
            if (file.listFiles() != null) {
            }
        }
        z = false;
        if (z) {
            if (!isBothStorageCanDelete(returnMountPOints)) {
                permissionAlert();
                return;
            } else {
                this.deleteTask.execute("permissiondeletion");
                return;
            }
        }
        this.deleteTask.execute(new String[0]);
    }

    public int getTotalSelected() {
        int i = 0;
        for (int i2 = 0; i2 < MobiClean.getInstance().duplicatesData.currentList.size(); i2++) {
            if (MobiClean.getInstance().duplicatesData.currentList.get(i2).ischecked) {
                i++;
            }
        }
        return i;
    }

    private int getTotalselected() {
        ArrayList<ImageDetail> arrayList = MobiClean.getInstance().duplicatesData.currentList;
        int i = 0;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if (arrayList.get(i2).ischecked) {
                i++;
            }
        }
        return i;
    }

    private void getids() {
        this.m = (RelativeLayout) findViewById(R.id.layout_one);
        this.n = (RelativeLayout) findViewById(R.id.layout_two);
        this.o = (FrameLayout) findViewById(R.id.frame_mid_laysss);
        this.p = (TextView) findViewById(R.id.ads_btn_countinue);
        this.q = (TextView) findViewById(R.id.ads_btn_cancel);
        this.ads_title = (TextView) findViewById(R.id.dialog_title);
        this.ads_message = (TextView) findViewById(R.id.dialog_msg);
        this.q.setOnClickListener(this);
        this.p.setOnClickListener(this);
    }

    @RequiresApi(api = 21)
    private boolean isBothStorageCanDelete(ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (!FileUtil.isWritableNormalOrSaf(this, new File(arrayList.get(i)))) {
                return false;
            }
        }
        return true;
    }

    public boolean notAllChecked() {
        ArrayList<ImageDetail> arrayList = MobiClean.getInstance().duplicatesData.currentList;
        int i = 0;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if (!arrayList.get(i2).ischecked) {
                i++;
            }
        }
        return i > 1;
    }

    public void permissionAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("" + this.context.getResources().getString(R.string.please_grant_permission));
        builder.setPositiveButton("" + this.context.getResources().getString(R.string.grant), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ImageViewerScreen imageViewerScreen = ImageViewerScreen.this;
                Splash.showdialog_sdcard(imageViewerScreen, imageViewerScreen);
            }
        });
        builder.setNegativeButton("" + this.context.getResources().getString(R.string.deny), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ImageViewerScreen.this.deleteTask.execute(new String[0]);
            }
        });
        builder.show();
    }

    @RequiresApi(api = 21)
    public FilesGridActivity.DELETION permissionBasedDeletion() {
        boolean deleteFile;
        try {
            try {
                ImageDetail imageDetail = MobiClean.getInstance().duplicatesData.currentList.get(this.preview_gallery.getSelectedItemPosition());
                File file = new File(imageDetail.path);
                if (file.delete()) {
                    deleteFile = !file.exists();
                } else {
                    deleteFile = FileUtil.deleteFile(this, file);
                }
                if (!deleteFile) {
                    deleteFile = FileUtil.deleteFile(this, file);
                }
                if (deleteFile) {
                    Util.appendLogmobiclean(TAG, "normal delete success with path===" + imageDetail.path, "");
                    this.isBothChk = 0;
                    this.ignoreSize = 0L;
                    if (MobiClean.getInstance().duplicatesData.currentList.size() == 2) {
                        Iterator<ImageDetail> it = MobiClean.getInstance().duplicatesData.currentList.iterator();
                        while (it.hasNext()) {
                            ImageDetail next = it.next();
                            this.ignoreSize += next.size;
                            if (next.ischecked) {
                                this.s = true;
                                this.isBothChk++;
                            }
                        }
                    } else if (MobiClean.getInstance().duplicatesData.currentList.size() >= 2) {
                        this.ignoreSize = imageDetail.size;
                        if (imageDetail.ischecked) {
                            this.s = true;
                        }
                    }
                    if (imageDetail.ischecked) {
                        this.updatedSize = imageDetail.size;
                    }
                    this.displayProgress.setProgress(1);
                    MobiClean.getInstance().duplicatesData.removeNode(imageDetail);
                    MobiClean.getInstance().duplicatesData.currentList.remove(this.preview_gallery.getSelectedItemPosition());
                    MobiClean.getInstance().duplicatesData.grouplist.get(MobiClean.getInstance().duplicatesData.currentGroupIndex - 1).children.remove(this.preview_gallery.getSelectedItemPosition());
                } else {
                    this.notdeleted++;
                    Log.w("------------", "on dialog show deletionTask else if else 111111111: ");
                    Util.appendLogmobiclean(TAG, "normal delete failed with path===" + imageDetail.path, "");
                }
                MobiClean.getInstance().duplicatesData.isBackAfterPreviewDelete = true;
                if (this.notdeleted > 0) {
                    return FilesGridActivity.DELETION.NOTDELETION;
                }
                if (MobiClean.getInstance().duplicatesData.currentList.size() == 1) {
                    return FilesGridActivity.DELETION.FINISH;
                }
                return FilesGridActivity.DELETION.SUCCESS;
            } catch (Exception e) {
                Util.appendLogmobiclean(TAG, "Permission deletion exception====" + e.getMessage(), GlobalData.FILE_NAME);
                FilesGridActivity.DELETION deletion = FilesGridActivity.DELETION.ERROR;
                MobiClean.getInstance().duplicatesData.isBackAfterPreviewDelete = true;
                if (this.notdeleted > 0) {
                    return FilesGridActivity.DELETION.NOTDELETION;
                }
                if (MobiClean.getInstance().duplicatesData.currentList.size() == 1) {
                    return FilesGridActivity.DELETION.FINISH;
                }
                return FilesGridActivity.DELETION.SUCCESS;
            }
        } catch (Throwable unused) {
            MobiClean.getInstance().duplicatesData.isBackAfterPreviewDelete = true;
            if (this.notdeleted > 0) {
                return FilesGridActivity.DELETION.NOTDELETION;
            }
            if (MobiClean.getInstance().duplicatesData.currentList.size() == 1) {
                return FilesGridActivity.DELETION.FINISH;
            }
            return FilesGridActivity.DELETION.SUCCESS;
        }
    }

    private void setUpToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    private void showCustomDialog() {
        Log.w("------------", "on dialog show : ");
        Util.appendLogmobiclean(TAG, "method showCustomDialog calling ", GlobalData.FILE_NAME);
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
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_duplicate));
        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(getResources().getString(R.string.mbc_delete_large_file));
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageViewerScreen.this.multipleClicked()) {
                    return;
                }
                Util.appendLogmobiclean(ImageViewerScreen.TAG, "method showCustomDialog calling cancel press", GlobalData.FILE_NAME);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageViewerScreen.this.multipleClicked()) {
                    return;
                }
                Util.appendLogmobiclean(ImageViewerScreen.TAG, "method showCustomDialog calling continue press", GlobalData.FILE_NAME);
                ImageViewerScreen.this.deletionTask();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void successDeletion() {
        this.k.notifyDataSetChanged();
        H();
        if (MobiClean.getInstance().duplicatesData.currentList.size() == 1) {
            MobiClean.getInstance().duplicatesData.grouplist.remove(MobiClean.getInstance().duplicatesData.currentGroupIndex - 1);
            setResult(-1);
            finish();
        }
        TextView textView = this.image_check;
        textView.setText(this.totalSelectedForDelete + DialogConfigs.DIRECTORY_SEPERATOR + MobiClean.getInstance().duplicatesData.currentList.size() + " " + String.format(getResources().getString(R.string.mbc_mark).replace("DO_NOT_TRANSLATE", "%s"), ""));
    }


    public void updateMediaScannerPath(File file) {
        MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String str, Uri uri) {
            }
        });
    }

    public static int y(ImageViewerScreen imageViewerScreen) {
        int i = imageViewerScreen.isBothChk;
        imageViewerScreen.isBothChk = i + 1;
        return i;
    }

    public String ConvertMilliSecondsToFormattedDate(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        return this.simpleDateFormat.format(calendar.getTime());
    }

    public boolean G() {
        int size = MobiClean.getInstance().duplicatesData.currentList.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            if (MobiClean.getInstance().duplicatesData.currentList.get(i2).ischecked) {
                i++;
            }
        }
        return i == size - 1;
    }

    public void H() {
        if (this.s) {
            if (this.isBothChk == 2) {
                MobiClean.getInstance().duplicatesData.totalselectedSize -= this.ignoreSize;
            } else {
                MobiClean.getInstance().duplicatesData.totalselectedSize -= this.updatedSize;
            }
            MobiClean.getInstance().duplicatesData.totalselected--;
            this.s = false;
        }
        MobiClean.getInstance().duplicatesData.totalDuplicatesSize -= this.ignoreSize;
    }


    @Override
    public void clickOK() {
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.putExtra("android.intent.extra.LOCAL_ONLY", true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, FilesGridActivity.REQUEST_CODE_STORAGE_ACCESS_INPUT);
    }

    public void getScreenSize() {
        WindowManager windowManager = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(this.j);
        }
        DisplayMetrics displayMetrics = this.j;
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2415 && i2 == -1) {
            deletion(i, i2, intent);
        }
    }

    @Override
    public void onClick(View view) {
        if (multipleClicked()) {
            return;
        }
        if (view.getId() == R.id.ads_btn_cancel) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
            this.n.setVisibility(View.GONE);
            this.m.setVisibility(View.VISIBLE);
        }
        if (view.getId() == R.id.ads_btn_countinue) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
            this.n.setVisibility(View.GONE);
            this.m.setVisibility(View.VISIBLE);
            Util.appendLogmobiclean(TAG, "method showCustomDialog calling continue press", GlobalData.FILE_NAME);
            deletionTask();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_image_viewer_screen);
        setUpToolbar();
        this.context = this;
        this.j = new DisplayMetrics();
        getScreenSize();
        this.totalSelectedForDelete = getTotalselected();
        getids();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.parent_gallery);
        this.preview_gallery = (Gallery) findViewById(R.id.preview_group_img);
        this.image_check = (TextView) findViewById(R.id.image_check);
        this.parent_check = (CheckBox) findViewById(R.id.parent_check);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.check_layout);
        this.i = (TextView) findViewById(R.id.tv_group_heading_name);
        this.image = (ImageView) findViewById(R.id.preview_img);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((this.deviceWidth * 35) / 100, (this.deviceHeight * 35) / 100);
        layoutParams.addRule(13);
        ((ImageView) findViewById(R.id.selected_gallery)).setLayoutParams(layoutParams);
        try {
            TextView textView = this.i;
            textView.setText(getResources().getString(R.string.mbc_group) + " " + MobiClean.getInstance().duplicatesData.currentGroupIndex);
            TextView textView2 = this.image_check;
            textView2.setText(this.totalSelectedForDelete + DialogConfigs.DIRECTORY_SEPERATOR + MobiClean.getInstance().duplicatesData.currentList.size() + " " + String.format(getResources().getString(R.string.mbc_mark).replace("DO_NOT_TRANSLATE", "%s"), ""));
            ImageAdapter imageAdapter = new ImageAdapter(this.context);
            this.k = imageAdapter;
            this.preview_gallery.setAdapter((SpinnerAdapter) imageAdapter);
            this.preview_gallery.setSelection(MobiClean.getInstance().duplicatesData.currentGroupChildIndex);
            this.preview_gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                    try {
                        ImageViewerScreen.this.l = i;
                        ImageDetail imageDetail = MobiClean.getInstance().duplicatesData.currentList.get(i);
                        ImageViewerScreen imageViewerScreen = ImageViewerScreen.this;
                        imageViewerScreen.img_details = ImageViewerScreen.this.getResources().getString(R.string.mbc_name) + imageDetail.name + "\n\n" + ImageViewerScreen.this.getResources().getString(R.string.mbc_path) + imageDetail.path + "\n\n" + ImageViewerScreen.this.getResources().getString(R.string.mbc_width) + imageDetail.width + "\n\n" + ImageViewerScreen.this.getResources().getString(R.string.mbc_height) + imageDetail.height + "\n\n" + ImageViewerScreen.this.getResources().getString(R.string.mbc_size) + Util.convertBytes(imageDetail.size) + "\n\n" + ImageViewerScreen.this.ConvertMilliSecondsToFormattedDate(imageDetail.addDateInMSec.longValue());
                        Glide.with((FragmentActivity) ImageViewerScreen.this).load(new File(imageDetail.path)).thumbnail(0.5f).into(ImageViewerScreen.this.image);
                        ImageViewerScreen.this.parent_check.setChecked(imageDetail.ischecked);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            this.image_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ImageViewerScreen.this.multipleClicked()) {
                        return;
                    }
                    if (MobiClean.getInstance().duplicatesData.currentList.get(ImageViewerScreen.this.l).ischecked) {
                        ImageViewerScreen.this.k.notifyDataSetChanged();
                    } else if (ImageViewerScreen.this.getTotalSelected() < MobiClean.getInstance().duplicatesData.currentList.size() - 1) {
                        ImageViewerScreen.this.k.notifyDataSetChanged();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.parent_check.setClickable(false);
        this.parent_check.setFocusable(false);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ImageViewerScreen.this.parent_check.isChecked()) {
                    if (ImageViewerScreen.this.notAllChecked()) {
                        ImageViewerScreen.this.parent_check.setChecked(true);
                        ImageViewerScreen.this.totalSelectedForDelete++;
                        MobiClean.getInstance().duplicatesData.currentList.get(ImageViewerScreen.this.l).ischecked = true;
                        MobiClean.getInstance().duplicatesData.selectNode(MobiClean.getInstance().duplicatesData.currentList.get(ImageViewerScreen.this.l));
                    } else {
                        Toast.makeText(ImageViewerScreen.this, "" + ImageViewerScreen.this.getResources().getString(R.string.mbc_cant_delete), Toast.LENGTH_LONG).show();
                    }
                } else {
                    ImageViewerScreen.this.parent_check.setChecked(false);
                    ImageViewerScreen.this.totalSelectedForDelete--;
                    MobiClean.getInstance().duplicatesData.currentList.get(ImageViewerScreen.this.l).ischecked = false;
                    MobiClean.getInstance().duplicatesData.unselectNode(MobiClean.getInstance().duplicatesData.currentList.get(ImageViewerScreen.this.l));
                }
                ImageViewerScreen.this.image_check.setText(ImageViewerScreen.this.totalSelectedForDelete + DialogConfigs.DIRECTORY_SEPERATOR + MobiClean.getInstance().duplicatesData.currentList.size() + " " + String.format(ImageViewerScreen.this.getResources().getString(R.string.mbc_mark).replace("DO_NOT_TRANSLATE", "%s"), ""));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_imageviewr, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
            return true;
        } else if (itemId == R.id.delete) {
            ImageDetail imageDetail = MobiClean.getInstance().duplicatesData.currentList.get(this.preview_gallery.getSelectedItemPosition());
            if (imageDetail.lockImg) {
                Toast.makeText(this, getString(R.string.mbc_unlock_first), Toast.LENGTH_LONG).show();
                return true;
            } else if (G() && !imageDetail.ischecked) {
                Toast.makeText(this, getString(R.string.mbc_cant_deleted), Toast.LENGTH_LONG).show();
                return true;
            } else {
                showCustomDialog();
                return true;
            }
        } else if (itemId == R.id.action_imagedetail) {
            ImageDetail imageDetail2 = MobiClean.getInstance().duplicatesData.currentList.get(this.preview_gallery.getSelectedItemPosition());
            this.img_details = getResources().getString(R.string.mbc_name) + " " + imageDetail2.name + "\n\n" + getResources().getString(R.string.mbc_path) + " " + imageDetail2.path + "\n\n" + getResources().getString(R.string.mbc_width) + ": " + imageDetail2.width + "\n\n" + getResources().getString(R.string.mbc_height) + ": " + imageDetail2.height + "\n\n" + getResources().getString(R.string.mbc_size) + " " + Util.convertBytes(imageDetail2.size) + "\n\nDate and Time: " + ConvertMilliSecondsToFormattedDate(imageDetail2.addDateInMSec.longValue());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(this.img_details);
            builder.setMessage(sb.toString());
            builder.setPositiveButton(getString(17039370), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
            return true;
        } else if (itemId != R.id.ignore) {
            if (itemId != R.id.lock) {
                if (itemId == R.id.un_lock) {
                    MobiClean.getInstance().duplicatesData.currentList.get(this.preview_gallery.getSelectedItemPosition()).lockImg = false;
                    this.k.notifyDataSetChanged();
                    invalidateOptionsMenu();
                    return true;
                }
                return super.onOptionsItemSelected(menuItem);
            } else if (MobiClean.getInstance().duplicatesData.currentList.get(this.preview_gallery.getSelectedItemPosition()).ischecked) {
                Toast.makeText(this, getString(R.string.mbc_lock_ignore), 1).show();
                return true;
            } else {
                MobiClean.getInstance().duplicatesData.currentList.get(this.preview_gallery.getSelectedItemPosition()).lockImg = true;
                this.k.notifyDataSetChanged();
                invalidateOptionsMenu();
                return true;
            }
        } else {
            ImageDetail imageDetail3 = MobiClean.getInstance().duplicatesData.currentList.get(this.preview_gallery.getSelectedItemPosition());
            if (imageDetail3.lockImg) {
                Toast.makeText(this, getString(R.string.mbc_ignore_unlock), 1).show();
                return true;
            } else if (G() && !imageDetail3.ischecked) {
                Toast.makeText(this, getString(R.string.mbc_cant_ignored), 1).show();
                return true;
            } else {
                this.isBothChk = 0;
                this.ignoreSize = 0L;
                if (MobiClean.getInstance().duplicatesData.currentList.size() == 2) {
                    Iterator<ImageDetail> it = MobiClean.getInstance().duplicatesData.currentList.iterator();
                    while (it.hasNext()) {
                        ImageDetail next = it.next();
                        this.ignoreSize += next.size;
                        if (next.ischecked) {
                            this.s = true;
                            this.isBothChk++;
                        }
                    }
                } else if (MobiClean.getInstance().duplicatesData.currentList.size() >= 2) {
                    this.ignoreSize = imageDetail3.size;
                    if (imageDetail3.ischecked) {
                        this.s = true;
                    }
                }
                if (imageDetail3.ischecked) {
                    this.updatedSize = imageDetail3.size;
                }
                MobiClean.getInstance().duplicatesData.removeNode(imageDetail3);
                MobiClean.getInstance().duplicatesData.currentList.remove(this.preview_gallery.getSelectedItemPosition());
                MobiClean.getInstance().duplicatesData.grouplist.get(MobiClean.getInstance().duplicatesData.currentGroupIndex - 1).children.remove(this.preview_gallery.getSelectedItemPosition());
                MobiClean.getInstance().duplicatesData.isBackAfterPreviewDelete = true;
                H();
                this.k.notifyDataSetChanged();
                if (MobiClean.getInstance().duplicatesData.currentList.size() == 1) {
                    MobiClean.getInstance().duplicatesData.grouplist.remove(MobiClean.getInstance().duplicatesData.currentGroupIndex - 1);
                    setResult(-1);
                    finish();
                }
                return true;
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem findItem = menu.findItem(R.id.lock);
        MenuItem findItem2 = menu.findItem(R.id.un_lock);
        if (MobiClean.getInstance().duplicatesData.currentList.get(this.preview_gallery.getSelectedItemPosition()).lockImg) {
            findItem2.setVisible(true);
            findItem.setVisible(false);
        } else {
            findItem2.setVisible(false);
            findItem.setVisible(true);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
