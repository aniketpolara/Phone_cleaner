package com.cleanPhone.mobileCleaner.similerphotos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.filestorage.DialogProperties;
import com.cleanPhone.mobileCleaner.filestorage.DialogSelectionListener;
import com.cleanPhone.mobileCleaner.filestorage.FilePickerDialog;
import com.cleanPhone.mobileCleaner.filestorage.ListItem;
import com.cleanPhone.mobileCleaner.utility.GetMountPoints;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.io.File;
import java.util.ArrayList;

public class NoDuplicates extends ParentActivity implements View.OnClickListener {
    private static final int TRANS_COLOR = 1426063360;
    private RadioButton cameraRadio;
    private RadioButton fullRadio;
    private String[] items;
    public Dialog k;
    public Button l;
    public int m;
    private ImageButton minusbtn;
    public int n;
    public Context o;
    public TextView p;
    private ImageButton plusbtn;
    public TextView q;
    public TextView s;
    private SeekBar seekbar;
    private RadioButton selectRadio;
    public ProgressBar t;
    public FrameLayout v;
    public FrameLayout w;
    public ImageView back;
    int admob = 3;

    public TextView x;
    public TextView y;
    public boolean j = false;
    private final String TAG = NoDuplicates.class.getName();
    private int selectionMode = 0;
    public long u = 0;
    private long cursorCount_camera = 0;
    private long cursorCount_folder = 0;

    public long getCursorCount() {
        Cursor cursor = null;
        try {
            try {
                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, null, null, "datetaken DESC");
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor == null) {
                    return 0L;
                }
            }
            if (cursor != null) {
                long count = cursor.getCount();
                if (cursor != null) {
                    cursor.close();
                }
                return count;
            }
            if (cursor == null) {
                return 0L;
            }
            cursor.close();
            return 0L;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public long getCursorCountCamera() {
        Cursor cursor = null;
        try {
            try {
                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data like ? OR _data like ? OR _data like ?", new String[]{"%/dcim/camera/%", "%/dcim/100MEDIA/%", "%/dcim/100andro/%"}, "datetaken DESC");
                if (cursor != null) {
                    Log.i("CURSORCOUNT1", "" + cursor.getCount());
                    Util.appendLog("imagecount =" + cursor.getCount(), "cameracount.txt");
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor == null) {
                    return 0L;
                }
            }
            if (cursor != null) {
                long count = cursor.getCount();
                if (cursor != null) {
                    cursor.close();
                }
                return count;
            }
            if (cursor == null) {
                return 0L;
            }
            cursor.close();
            return 0L;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public long getFolderCursorCount() {
        Cursor cursor = null;
        try {
            try {
                String[] strArr = {"_id"};
                String[] strArr2 = new String[DuplicacyMidSettings.listItem.size()];
                for (int i = 0; i < DuplicacyMidSettings.listItem.size(); i++) {
                    strArr2[i] = "%" + DuplicacyMidSettings.listItem.get(i).getPath() + "%";
                }
                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, "_data like ? OR _data like ? OR _data like ?", strArr2, "datetaken DESC");
                if (cursor != null) {
                    Log.i("CURSORCOUNT1", "" + cursor.getCount());
                    Util.appendLog("imagecount =" + cursor.getCount(), "cameracount.txt");
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor == null) {
                    return 0L;
                }
            }
            if (cursor != null) {
                long count = cursor.getCount();
                if (cursor != null) {
                    cursor.close();
                }
                return count;
            }
            if (cursor == null) {
                return 0L;
            }
            cursor.close();
            return 0L;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    private void getSelectionMode() {
        if (MobiClean.getInstance().duplicatesData.fromCamera) {
            this.selectionMode = 0;
            this.cameraRadio.setChecked(true);
        } else if (MobiClean.getInstance().duplicatesData.fromselectedFolder) {
            this.selectionMode = 2;
            this.selectRadio.setChecked(true);
        } else {
            this.selectionMode = 1;
            this.fullRadio.setChecked(true);
        }
    }

    @SuppressLint("ResourceType")
    private void initFolderDialog() {
        final ArrayList<File> returnAllMountPoints = new GetMountPoints(this).returnAllMountPoints();
        if (returnAllMountPoints.size() > 1) {
            new AlertDialog.Builder(this).setSingleChoiceItems(this.items, 0, (DialogInterface.OnClickListener) null).setCancelable(false).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.mobiclean.phoneclean.similerphotos.NoDuplicates.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    DialogConfigs.DEFAULT_DIR = ((File) returnAllMountPoints.get(((AlertDialog) dialogInterface).getListView().getCheckedItemPosition())).getAbsolutePath();
                    NoDuplicates.this.A();
                }
            }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
            return;
        }
        DialogConfigs.DEFAULT_DIR = returnAllMountPoints.get(0).getAbsolutePath();
        A();
    }

    private boolean isLVLChange() {
        return this.seekbar.getProgress() != MySharedPreference.c(this);
    }

    private boolean isModeChanged() {
        int i;
        if (this.cameraRadio.isChecked()) {
            i = 0;
        } else {
            i = this.selectRadio.isChecked() ? 2 : 1;
        }
        return i == 2 || i != this.selectionMode;
    }

    public void passIntent(boolean z) {
        this.cameraRadio.isChecked();
        boolean isModeChanged = isModeChanged();
        boolean isLVLChange = isLVLChange();
        Intent intent = new Intent();
        if (z) {
            intent.putExtra("FROMCAMERA", z);
            new SharedPrefUtil(this).saveBooleanToggle("FCAMERA", z);
        }
        intent.putExtra("is_lvl_change", isLVLChange);
        intent.putExtra("is_mode_change", isModeChanged);
        intent.putExtra("cancel_press", this.j);
        setResult(-1, intent);
        MySharedPreference.l(this, this.seekbar.getProgress());
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    private void reScan() {
        if (this.cameraRadio.isChecked()) {
            new DuplicacyRefreshAsyncTask(this) {
                @Override
                public void onPreExecute() {
                    super.onPreExecute();
                    MobiClean.getInstance().duplicatesData.fromCamera = true;
                    MobiClean.getInstance().duplicatesData.fromselectedFolder = false;
                    Util.appendLogmobiclean(NoDuplicates.this.TAG, " DuplicacyRefreshAsyncTask  preexecute", "");
                    NoDuplicates.this.k = new Dialog(NoDuplicates.this.o);
                    NoDuplicates.this.k.requestWindowFeature(1);
                    NoDuplicates.this.k.getWindow().setBackgroundDrawable(new ColorDrawable(NoDuplicates.TRANS_COLOR));
                    NoDuplicates.this.k.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    NoDuplicates.this.k.setContentView(R.layout.custam_progrssbars);
                    NoDuplicates.this.k.setCancelable(false);
                    NoDuplicates.this.k.setCanceledOnTouchOutside(false);
                    NoDuplicates.this.k.getWindow().setLayout(-1, -1);
                    NoDuplicates.this.k.getWindow().setGravity(17);
                    NoDuplicates noDuplicates = NoDuplicates.this;
                    noDuplicates.q = (TextView) noDuplicates.k.findViewById(R.id.progress_title);
                    NoDuplicates noDuplicates2 = NoDuplicates.this;
                    noDuplicates2.t = (ProgressBar) noDuplicates2.k.findViewById(R.id.progressBar_cust);
                    NoDuplicates noDuplicates3 = NoDuplicates.this;
                    noDuplicates3.p = (TextView) noDuplicates3.k.findViewById(R.id.progress_percent);
                    NoDuplicates noDuplicates4 = NoDuplicates.this;
                    noDuplicates4.s = (TextView) noDuplicates4.k.findViewById(R.id.progress_max);
                    NoDuplicates noDuplicates5 = NoDuplicates.this;
                    noDuplicates5.v = (FrameLayout) noDuplicates5.k.findViewById(R.id.frame_mid_laysss);
                    NoDuplicates noDuplicates6 = NoDuplicates.this;
                    noDuplicates6.q.setText(noDuplicates6.getResources().getString(R.string.mbc_fetching_images));
                    Log.e("=======", "onPreExecute");
                    NoDuplicates noDuplicates7 = NoDuplicates.this;
                    noDuplicates7.cursorCount_camera = noDuplicates7.getCursorCountCamera();
                    if (NoDuplicates.this.cursorCount_camera > 0) {
                        TextView textView = NoDuplicates.this.s;
                        textView.setText("" + ((int) NoDuplicates.this.cursorCount_camera));
                    }
                    NoDuplicates.this.k.show();
                }

                @Override
                public void onPostExecute(String str) {
                    Util.appendLogmobiclean(NoDuplicates.this.TAG, " DuplicacyRefreshAsyncTask  onPostExecute", "");
                    try {
                        try {
                            NoDuplicates.this.getWindow().clearFlags(128);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Dialog dialog = NoDuplicates.this.k;
                        if (dialog != null && dialog.isShowing()) {
                            NoDuplicates.this.k.dismiss();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    if (NoDuplicates.this.cursorCount_camera <= GlobalData.photocount) {
                        NoDuplicates.this.passIntent(true);
                    }
                    super.onPostExecute(str);
                }

                @Override
                public void onProgressUpdate(final String... strArr) {
                    super.onProgressUpdate(strArr);
                    try {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                long j = 0;
                                int parseInt = Integer.parseInt(strArr[0]);
                                NoDuplicates noDuplicates = NoDuplicates.this;
                                noDuplicates.t.setProgress((int) ((parseInt * 100) / noDuplicates.cursorCount_camera));
                                NoDuplicates.this.p.setText("" + ((int) (j / NoDuplicates.this.cursorCount_camera)) + "%");
                                NoDuplicates.this.s.setText(parseInt + DialogConfigs.DIRECTORY_SEPERATOR + ((int) NoDuplicates.this.cursorCount_camera));
                            }
                        }, 1000L);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }.execute("", "", "");
        } else if (this.fullRadio.isChecked()) {
            new DuplicacyRefreshAsyncTask(this) {
                @Override
                public void onPreExecute() {
                    Util.appendLogmobiclean(NoDuplicates.this.TAG, "DuplicacyRefreshAsyncTask called onpre", "");
                    NoDuplicates.this.getWindow().addFlags(2097280);
                    MobiClean.getInstance().duplicatesData.fromCamera = NoDuplicates.this.cameraRadio.isChecked();
                    MobiClean.getInstance().duplicatesData.fromselectedFolder = false;
                    NoDuplicates.this.k = new Dialog(NoDuplicates.this.o);
                    NoDuplicates.this.k.requestWindowFeature(1);
                    NoDuplicates.this.k.getWindow().setBackgroundDrawable(new ColorDrawable(NoDuplicates.TRANS_COLOR));
                    NoDuplicates.this.k.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    NoDuplicates.this.k.setContentView(R.layout.custam_progrssbars);
                    NoDuplicates.this.k.setCancelable(false);
                    NoDuplicates.this.k.setCanceledOnTouchOutside(false);
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(NoDuplicates.this.k.getWindow().getAttributes());
                    NoDuplicates noDuplicates = NoDuplicates.this;
                    layoutParams.width = noDuplicates.n;
                    layoutParams.height = noDuplicates.m;
                    noDuplicates.k.getWindow().setAttributes(layoutParams);
                    NoDuplicates noDuplicates2 = NoDuplicates.this;
                    noDuplicates2.q = (TextView) noDuplicates2.k.findViewById(R.id.progress_title);
                    NoDuplicates noDuplicates3 = NoDuplicates.this;
                    noDuplicates3.t = (ProgressBar) noDuplicates3.k.findViewById(R.id.progressBar_cust);
                    NoDuplicates noDuplicates4 = NoDuplicates.this;
                    noDuplicates4.p = (TextView) noDuplicates4.k.findViewById(R.id.progress_percent);
                    NoDuplicates noDuplicates5 = NoDuplicates.this;
                    noDuplicates5.s = (TextView) noDuplicates5.k.findViewById(R.id.progress_max);
                    NoDuplicates noDuplicates6 = NoDuplicates.this;
                    noDuplicates6.v = (FrameLayout) noDuplicates6.k.findViewById(R.id.frame_mid_laysss);
                    NoDuplicates noDuplicates7 = NoDuplicates.this;
                    noDuplicates7.q.setText(noDuplicates7.getResources().getString(R.string.mbc_fetching_images));
                    Log.e("=======", "onPreExecute");
                    NoDuplicates noDuplicates8 = NoDuplicates.this;
                    noDuplicates8.u = noDuplicates8.getCursorCount();
                    NoDuplicates noDuplicates9 = NoDuplicates.this;
                    if (noDuplicates9.u > 0) {
                        TextView textView = noDuplicates9.s;
                        textView.setText("" + ((int) NoDuplicates.this.u));
                    }
                    NoDuplicates.this.k.show();
                    super.onPreExecute();
                }

                @Override
                public void onPostExecute(String str) {
                    super.onPostExecute(str);
                    Dialog dialog = NoDuplicates.this.k;
                    if (dialog != null && dialog.isShowing()) {
                        NoDuplicates.this.k.dismiss();
                    }
                    Log.d("CRITERIA0", GlobalData.duplicacyDist + " " + GlobalData.duplicacyLevel + "  " + GlobalData.duplicacyTime);
                    Util.appendLogmobiclean(NoDuplicates.this.TAG, "DuplicacyRefreshAsyncTask called onpost", "");
                    NoDuplicates.this.getWindow().clearFlags(128);
                    NoDuplicates noDuplicates = NoDuplicates.this;
                    noDuplicates.passIntent(noDuplicates.cameraRadio.isChecked());
                }

                @Override
                public void onProgressUpdate(final String... strArr) {
                    super.onProgressUpdate(strArr);
                    try {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                long j = 0;
                                int parseInt = Integer.parseInt(strArr[0]);
                                NoDuplicates noDuplicates = NoDuplicates.this;
                                noDuplicates.t.setProgress((int) ((parseInt * 100) / noDuplicates.u));
                                NoDuplicates.this.p.setText("" + ((int) (j / NoDuplicates.this.u)) + "%");
                                NoDuplicates.this.s.setText(parseInt + DialogConfigs.DIRECTORY_SEPERATOR + ((int) NoDuplicates.this.u));
                            }
                        }, 1000L);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(new String[0]);
        } else if (isLVLChange() && !isModeChanged()) {
            scanForDuplicates();
        } else if (this.j || isModeChanged()) {
            initFolderDialog();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void scanForDuplicates() {
        new DuplicacyRefreshAsyncTask(this) {
            @Override
            public void onPreExecute() {
                Util.appendLogmobiclean(NoDuplicates.this.TAG, "DuplicacyRefreshAsyncTask called onpre", "");
                NoDuplicates.this.getWindow().addFlags(2097280);
                MobiClean.getInstance().duplicatesData.fromCamera = NoDuplicates.this.cameraRadio.isChecked();
                MobiClean.getInstance().duplicatesData.fromselectedFolder = true;
                NoDuplicates.this.k = new Dialog(NoDuplicates.this.o);
                NoDuplicates.this.k.requestWindowFeature(1);
                NoDuplicates.this.k.getWindow().setBackgroundDrawable(new ColorDrawable(NoDuplicates.TRANS_COLOR));
                NoDuplicates.this.k.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                NoDuplicates.this.k.setContentView(R.layout.custam_progrssbars);
                NoDuplicates.this.k.setCancelable(false);
                NoDuplicates.this.k.setCanceledOnTouchOutside(false);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(NoDuplicates.this.k.getWindow().getAttributes());
                NoDuplicates noDuplicates = NoDuplicates.this;
                layoutParams.width = noDuplicates.n;
                layoutParams.height = noDuplicates.m;
                noDuplicates.k.getWindow().setAttributes(layoutParams);
                NoDuplicates noDuplicates2 = NoDuplicates.this;
                noDuplicates2.q = (TextView) noDuplicates2.k.findViewById(R.id.progress_title);
                NoDuplicates noDuplicates3 = NoDuplicates.this;
                noDuplicates3.t = (ProgressBar) noDuplicates3.k.findViewById(R.id.progressBar_cust);
                NoDuplicates noDuplicates4 = NoDuplicates.this;
                noDuplicates4.p = (TextView) noDuplicates4.k.findViewById(R.id.progress_percent);
                NoDuplicates noDuplicates5 = NoDuplicates.this;
                noDuplicates5.s = (TextView) noDuplicates5.k.findViewById(R.id.progress_max);
                NoDuplicates noDuplicates6 = NoDuplicates.this;
                noDuplicates6.v = (FrameLayout) noDuplicates6.k.findViewById(R.id.frame_mid_laysss);
                NoDuplicates noDuplicates7 = NoDuplicates.this;
                noDuplicates7.q.setText(noDuplicates7.getResources().getString(R.string.mbc_fetching_images));
                Log.e("=======", "onPreExecute");
                NoDuplicates noDuplicates8 = NoDuplicates.this;
                noDuplicates8.cursorCount_folder = noDuplicates8.getFolderCursorCount();
                if (NoDuplicates.this.cursorCount_folder > 0) {
                    TextView textView = NoDuplicates.this.s;
                    textView.setText("" + ((int) NoDuplicates.this.cursorCount_folder));
                }
                NoDuplicates.this.k.show();
                super.onPreExecute();
            }

            @Override
            public void onPostExecute(String str) {
                super.onPostExecute(str);
                Dialog dialog = NoDuplicates.this.k;
                if (dialog != null && dialog.isShowing()) {
                    NoDuplicates.this.k.dismiss();
                }
                Log.d("CRITERIA0", GlobalData.duplicacyDist + " " + GlobalData.duplicacyLevel + "  " + GlobalData.duplicacyTime);
                Util.appendLogmobiclean(NoDuplicates.this.TAG, "DuplicacyRefreshAsyncTask called onpost", "");
                NoDuplicates.this.getWindow().clearFlags(128);
                if (MobiClean.getInstance().duplicatesData.imageList.size() == 0) {
                    NoDuplicates.this.showNoImgDialog();
                    return;
                }
                NoDuplicates noDuplicates = NoDuplicates.this;
                noDuplicates.passIntent(noDuplicates.cameraRadio.isChecked());
            }

            @Override
            public void onProgressUpdate(final String... strArr) {
                super.onProgressUpdate(strArr);
                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            long j = 0;
                            int parseInt = Integer.parseInt(strArr[0]);
                            if (NoDuplicates.this.cursorCount_folder == 0) {
                                NoDuplicates.this.cursorCount_folder = 1L;
                            }
                            NoDuplicates noDuplicates = NoDuplicates.this;
                            noDuplicates.t.setProgress((int) ((parseInt * 100) / noDuplicates.cursorCount_folder));
                            NoDuplicates.this.p.setText("" + ((int) (j / NoDuplicates.this.cursorCount_folder)) + "%");
                            NoDuplicates.this.s.setText(parseInt + DialogConfigs.DIRECTORY_SEPERATOR + ((int) NoDuplicates.this.cursorCount_folder));
                        }
                    }, 1000L);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[0]);
    }


    private void setMatchingLevelText(int i) {
        if (i == 0 || i == 25 || i != 50) {
        }
    }

    private void setSeekBar() {
        this.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (progress <= 12) {
                    seekBar.setProgress(0);
                } else if (progress <= 37) {
                    seekBar.setProgress(25);
                } else if (progress <= 62) {
                    seekBar.setProgress(50);
                } else if (progress <= 87) {
                    seekBar.setProgress(75);
                } else {
                    seekBar.setProgress(100);
                }
            }
        });
        this.seekbar.setProgress(MySharedPreference.c(this));
        this.plusbtn.setOnClickListener(this);
        this.minusbtn.setOnClickListener(this);
    }

    private void setdeviesdimention() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        this.m = displayMetrics.heightPixels;
        this.n = displayMetrics.widthPixels;
    }

    @SuppressLint("ResourceType")
    public void showNoImgDialog() {
        if (Util.isConnectingToInternet(this.o) && !Util.isAdsFree(this.o)) {
            Dialog dialog = new Dialog(this.o, R.style.AppTheme);
            this.k = dialog;
            dialog.requestWindowFeature(1);
            if (this.k.getWindow() != null) {
                this.k.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                this.k.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            }
            this.k.setContentView(R.layout.no_image_dialog_internet);
            this.k.setCancelable(false);
            this.k.setCanceledOnTouchOutside(false);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(this.k.getWindow().getAttributes());
            layoutParams.width = this.n;
            layoutParams.height = this.m;
            this.k.getWindow().setAttributes(layoutParams);
            this.l = (Button) this.k.findViewById(R.id.btn_oks);
            this.w = (FrameLayout) this.k.findViewById(R.id.frame_mid_no_image);
            View space;
              this.l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog2;
                    if (NoDuplicates.this.multipleClicked() || (dialog2 = NoDuplicates.this.k) == null || !dialog2.isShowing()) {
                        return;
                    }
                    NoDuplicates.this.k.dismiss();
                }
            });
            this.k.show();
            return;
        }
        new AlertDialog.Builder(this).setTitle(getString(R.string.mbc_oops)).setMessage(getString(R.string.mbc_no_img)).setPositiveButton(getString(17039370), (DialogInterface.OnClickListener) null).show();
    }

    public void A() {
        DialogProperties dialogProperties = new DialogProperties();
        FilePickerDialog filePickerDialog = new FilePickerDialog(this, dialogProperties);
        filePickerDialog.setTitle(getResources().getString(R.string.mbc_select_folder));
        filePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
        dialogProperties.selection_type = 1;
        dialogProperties.selection_mode = 1;
        filePickerDialog.setCanceledOnTouchOutside(true);
        filePickerDialog.setCancelable(true);
        filePickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] strArr) {
                if (strArr == null) {
                    return;
                }
                DuplicacyMidSettings.listItem.clear();
                for (String str : strArr) {
                    File file = new File(str);
                    ListItem listItem = new ListItem();
                    listItem.setName(file.getName());
                    listItem.setPath(file.getAbsolutePath());
                    DuplicacyMidSettings.listItem.add(listItem);
                    Log.e("zzz", "601 Selected Path is = " + listItem.getPath());
                }
                if (DuplicacyMidSettings.listItem.size() > 0) {
                    NoDuplicates.this.scanForDuplicates();
                }
            }
        });
        filePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        setResult(-1);
        startActivity(new Intent(NoDuplicates.this, HomeActivity.class));
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_seekminus1:
                if (this.seekbar.getProgress() > 0) {
                    SeekBar seekBar = this.seekbar;
                    seekBar.setProgress(seekBar.getProgress() - 25);
                    setMatchingLevelText(this.seekbar.getProgress());
                    return;
                }
                return;
            case R.id.btn_seekplus1:
                if (this.seekbar.getProgress() < 100) {
                    SeekBar seekBar2 = this.seekbar;
                    seekBar2.setProgress(seekBar2.getProgress() + 25);
                    setMatchingLevelText(this.seekbar.getProgress());
                    return;
                }
                return;
            default:
                return;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.no_duplicates);
        Util.saveScreen(getClass().getName(), this);
int intExtra = getIntent().getIntExtra("process_imgs", 0);
        this.j = getIntent().getBooleanExtra("cancel_press", false);
        this.items = new String[]{getString(R.string.mbc_in_storage), getString(R.string.mbc_ex_storage)};
        if (this.j) {
            ((TextView) findViewById(R.id.toolbar_title)).setText(getString(R.string.mbc_scan_abort));
        } else {
            ((TextView) findViewById(R.id.toolbar_title)).setText(getString(R.string.mbc_no_duplicate_found));
        }
        this.o = this;
        this.back = (ImageView) findViewById(R.id.iv_back);
        this.cameraRadio = (RadioButton) findViewById(R.id.rbtn_camera);
        this.fullRadio = (RadioButton) findViewById(R.id.rbtn_all);
        this.selectRadio = (RadioButton) findViewById(R.id.rbtn_folder);
        this.x = (TextView) findViewById(R.id.img_count);
        this.y = (TextView) findViewById(R.id.junkdisplay_sizetv_unit);
        this.seekbar = (SeekBar) findViewById(R.id.settings_seekbar1);
        this.plusbtn = (ImageButton) findViewById(R.id.btn_seekplus1);
        this.minusbtn = (ImageButton) findViewById(R.id.btn_seekminus1);

        this.x.setText(String.valueOf(intExtra));
        if (intExtra == 1) {
            ((TextView) findViewById(R.id.junkdisplay_sizetv_unit)).setText("" + getString(R.string.photo));
        } else {
            ((TextView) findViewById(R.id.junkdisplay_sizetv_unit)).setText(getString(R.string.images_found));
        }
        ((TextView) findViewById(R.id.tv_images_count)).setText(Html.fromHtml(getString(R.string.mbc_scanned) + ", <b>0</b> " + getString(R.string.duplicates_found)));
        setSeekBar();
        getSelectionMode();
        setdeviesdimention();
        this.selectRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoDuplicates.this, HomeActivity.class));
                finish();
            }
        });
    }


    public void scanClick(View view) {
        boolean isModeChanged = isModeChanged();
        if (isLVLChange() && !isModeChanged) {
            MobiClean.getInstance().duplicatesData.isBackAfterDelete = false;
            passIntent(false);
        } else if (!isModeChanged && !this.j) {
            Toast.makeText(this, getResources().getString(R.string.mbc_duplicate_tost), Toast.LENGTH_LONG).show();
        } else {
            reScan();
        }
    }
}
