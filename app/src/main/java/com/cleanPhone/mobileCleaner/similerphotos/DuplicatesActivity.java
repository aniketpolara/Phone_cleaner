package com.cleanPhone.mobileCleaner.similerphotos;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.cleanPhone.mobileCleaner.CommonResultActivity;
import com.cleanPhone.mobileCleaner.DialogListners;
import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.Splash;
import com.cleanPhone.mobileCleaner.customview.SectionedGridRecyclerViewAdapter;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.tools.FilesGridActivity;
import com.cleanPhone.mobileCleaner.utility.FileUtil;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.MountPoints;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.DuplicatesData;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class DuplicatesActivity extends ParentActivity implements DialogListners, View.OnClickListener {
    private static final String IMAGE_CACHE_DIR = "thumbs";
    public static final int NO_DUPS_CODE = 1268;
    public static final int VIEW_CODE = 34;
    public static final int automarkcode = 1047;
    public static boolean isLVLChange = false;
    public static boolean isModeChange = false;
    public static boolean pressCancel = false;
    private TextView ads_message;
    private TextView ads_message_inapp;
    private TextView ads_title;
    private TextView ads_title_inapp;
    private BatteryChangeReceiver breceiver;
    private LinearLayout btnlayout;
    private Context context;
    private AsyncTask<String, String, String> createGroupsTask;
    private AsyncTask<String, Integer, FilesGridActivity.DELETION> deleteTask;
    private int deviceHeight;
    private int deviceWidth;
    private ProgressDialog dialogStopWait;
    private Dialog dialogg;
    private ProgressDialog displayProgress;
    private int distance;
    private SectionedGridRecyclerViewAdapter.Section[] dummy;
    public Map<Integer, ArrayList<ImageDetail>> duplicateGridImages;
    private boolean fromnotiFication;
    public LinearLayout k;
    public TextView l;
    private ListView listView;
    public TextView m;
    private RecyclerView mRecyclerView;
    private SectionedGridRecyclerViewAdapter mSectionedAdapter;
    public RelativeLayout n;
    private boolean noti_result_back;
    public RelativeLayout o;
    public RelativeLayout p;
    private float pass_score;
    public FrameLayout q;
    private boolean redirectToNoti;
    public FrameLayout s;
    int admob = 3;
   public FrameLayout t;
    private int time;
    private TextView tv1btn;
    private TextView tvcount;
    private TextView tvdupcount;
    private TextView tvduplicates;
    private TextView tvsymbol;
    private TextView tvunit;
    public TextView u;
    public TextView v;
    public TextView w;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    public TextView x;
    public int j = 0;
    private int batterylevel = -1;
    private boolean fromNotification = false;
    private String TAG = "DuplicatesActivity";
    private boolean isWaitScreenShown = false;
    private ArrayList<String> options = new ArrayList<>();
    public int y = 0;

    public static class AnonymousClass24 {

        public static final int[] f5067a;

        static {
            int[] iArr = new int[FilesGridActivity.DELETION.values().length];
            f5067a = iArr;
            try {
                iArr[FilesGridActivity.DELETION.ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5067a[FilesGridActivity.DELETION.PERMISSION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f5067a[FilesGridActivity.DELETION.SUCCESS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f5067a[FilesGridActivity.DELETION.SELECTION.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f5067a[FilesGridActivity.DELETION.NOTDELETION.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public class BatteryChangeReceiver extends BroadcastReceiver {
        public BatteryChangeReceiver(DuplicatesActivity duplicatesActivity) {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }

    public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {
        private final Context mContext;
        private final ArrayList<ImageDetail> mItems;
        public int f5070a = 0;

        public class SimpleViewHolder extends RecyclerView.ViewHolder {
            public final ImageView img;
            public final CheckBox p;
            public TextView q;

            public SimpleViewHolder(SimpleAdapter simpleAdapter, View view) {
                super(view);
                ImageView imageView = (ImageView) view.findViewById(R.id.img);
                this.img = imageView;
                this.p = (CheckBox) view.findViewById(R.id.img_chk);
                this.q = (TextView) view.findViewById(R.id.junkListItemSize);
            }
        }

        public SimpleAdapter(Context context, Map<Integer, ArrayList<ImageDetail>> map) {
            this.mContext = context;
            ArrayList arrayList = new ArrayList();
            for (Map.Entry<Integer, ArrayList<ImageDetail>> entry : map.entrySet()) {
                ArrayList<ImageDetail> value = entry.getValue();
                this.f5070a += value.size();
                arrayList.addAll(value);
            }
            this.mItems = new ArrayList<>(this.f5070a);
            for (int i = 0; i < this.f5070a; i++) {
                a(i, (ImageDetail) arrayList.get(i));
            }
        }

        private int dip2px(float f) {
            return (int) ((f * this.mContext.getResources().getDisplayMetrics().density) + 0.5f);
        }

        public void a(int i, ImageDetail imageDetail) {
            this.mItems.add(i, imageDetail);
            notifyItemInserted(i);
        }

        @Override
        public int getItemCount() {
            return this.mItems.size();
        }

        @Override
        public void onBindViewHolder(@NonNull final SimpleViewHolder simpleViewHolder, int i) {
            try {
                final ImageDetail imageDetail = this.mItems.get(i);

                if (imageDetail.ischecked) {
                    simpleViewHolder.p.setChecked(true);
                } else {
                    simpleViewHolder.p.setChecked(false);
                }
                Glide.with(this.mContext).load(new File(imageDetail.path)).into(simpleViewHolder.img);
                TextView textView = simpleViewHolder.q;
                textView.setText("" + Util.convertBytes(imageDetail.size));
                simpleViewHolder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.appendLogmobiclean(DuplicatesActivity.this.TAG, "type.equalsIgnoreCase images", GlobalData.FILE_NAME);
                        MobiClean.getInstance().duplicatesData.currentList = null;
                        DuplicatesData duplicatesData = MobiClean.getInstance().duplicatesData;
                        DuplicatesActivity duplicatesActivity = DuplicatesActivity.this;
                        duplicatesData.currentList = duplicatesActivity.getCurrentlySelectedGroup(imageDetail.path + "");
                        MobiClean.getInstance().duplicatesData.isBackAfterDelete = true;
                        GlobalData.imageviewed = true;
                        Intent intent = new Intent(DuplicatesActivity.this.context, ImageViewerScreen.class);
                        intent.putExtra("INFO", "NAME: " + imageDetail.name + "\n\nPATH: " + imageDetail.path + "\n\nWIDTH: " + imageDetail.width + "\n\nHEIGHT: " + imageDetail.height + "\n\nSIZE: " + Util.convertBytes(imageDetail.size));
                        StringBuilder sb = new StringBuilder();
                        sb.append(imageDetail.path);
                        sb.append("");
                        intent.putExtra("PATH", sb.toString());
                        DuplicatesActivity.this.startActivityForResult(intent, 34);
                    }
                });


                simpleViewHolder.p.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageDetail imageDetail2 = imageDetail;
                        if (imageDetail2.lockImg) {
                            simpleViewHolder.p.setChecked(false);
                            DuplicatesActivity duplicatesActivity = DuplicatesActivity.this;
                            Toast.makeText(duplicatesActivity, duplicatesActivity.getString(R.string.mbc_unlock_select), Toast.LENGTH_LONG).show();
                            return;
                        }
                        imageDetail2.ischecked = simpleViewHolder.p.isChecked();
                        if (simpleViewHolder.p.isChecked()) {
                            MobiClean.getInstance().duplicatesData.currentList = null;
                            DuplicatesData duplicatesData = MobiClean.getInstance().duplicatesData;
                            DuplicatesActivity duplicatesActivity2 = DuplicatesActivity.this;
                            duplicatesData.currentList = duplicatesActivity2.getCurrentlySelectedGroup(imageDetail.path + "");
                            int size = MobiClean.getInstance().duplicatesData.currentList.size();
                            int i2 = 0;
                            for (int i3 = 0; i3 < size; i3++) {
                                if (MobiClean.getInstance().duplicatesData.currentList.get(i3).ischecked) {
                                    i2++;
                                }
                            }
                            simpleViewHolder.p.setChecked(true);
                            MobiClean.getInstance().duplicatesData.selectNode(imageDetail);
                            if (size == i2) {
                                imageDetail.ischecked = false;
                                simpleViewHolder.p.setChecked(false);
                                DuplicatesActivity duplicatesActivity3 = DuplicatesActivity.this;
                                Toast.makeText(duplicatesActivity3, duplicatesActivity3.getString(R.string.mbc_cant_delete), Toast.LENGTH_LONG).show();
                                MobiClean.getInstance().duplicatesData.unselectNode(imageDetail);
                            }
                        } else {
                            simpleViewHolder.p.setChecked(false);
                            MobiClean.getInstance().duplicatesData.unselectNode(imageDetail);
                        }
                        DuplicatesActivity.this.setDeleteBtnText();
                        DuplicatesActivity.this.refreshRecyclerView();
                        SimpleAdapter.this.notifyDataSetChanged();
                        DuplicatesActivity.this.invalidateOptionsMenu();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        @NonNull
        public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new SimpleViewHolder(this, LayoutInflater.from(this.mContext).inflate(R.layout.item, viewGroup, false));
        }
    }

    private void btnClickListner() {
        this.w.setOnClickListener(this);
        this.x.setOnClickListener(this);
        this.btnlayout.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (DuplicatesActivity.this.multipleClicked()) {
                    return;
                }

                    if (MobiClean.getInstance().duplicatesData.totalselected != 0) {
                        DuplicatesActivity.this.showRealTimeDialog();
                        return;
                    }
                    DuplicatesActivity duplicatesActivity = DuplicatesActivity.this;
                    Toast.makeText(duplicatesActivity, duplicatesActivity.getResources().getString(R.string.mbc_at_leastone), Toast.LENGTH_SHORT).show();


            }
        });
    }


    public boolean checkInGroups(ImageDetail imageDetail) {
        Util.appendLogmobiclean(this.TAG, "before adding group list size===" + MobiClean.getInstance().duplicatesData.grouplist.size(), "");
        Util.appendLogmobiclean(this.TAG, "Checking image===" + imageDetail.name, "");
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= MobiClean.getInstance().duplicatesData.grouplist.size()) {
                break;
            }
            double calculateHamingScore = DuplicacyUtil.calculateHamingScore(MobiClean.getInstance().duplicatesData.grouplist.get(i).children.get(0), imageDetail, this.distance, this.time);
            if (this.pass_score == 1.0f) {
                this.pass_score = 0.997f;
            }
            if (calculateHamingScore >= this.pass_score) {
                MobiClean.getInstance().duplicatesData.grouplist.get(i).children.add(imageDetail);
                z = true;
                break;
            }
            i++;
        }
        if (!z) {
            DuplicateGroup duplicateGroup = new DuplicateGroup();
            ArrayList<ImageDetail> arrayList = new ArrayList<>();
            duplicateGroup.children = arrayList;
            arrayList.add(imageDetail);
            MobiClean.getInstance().duplicatesData.grouplist.add(duplicateGroup);
        }
        return z;
    }

    private void clearNotification() {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.cancel(300);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadinit() {
        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(DuplicatesActivity.this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        return;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

    private void createGroups() {
        pressCancel = false;
        Log.d("SIZEEEEEE", "createGroups = 11");
        AsyncTask<String, String, String> asyncTask = new AsyncTask<String, String, String>() {
            @Override
            public void onCancelled() {
                Log.d("ONCANCEL", "111111");
                if (!GlobalData.shouldContinue) {
                    Log.d("ONCANCEL", "22222");
                    DuplicatesActivity.this.dialogStopWait.dismiss();
                    if (!DuplicatesActivity.this.redirectToNoti && !DuplicatesActivity.this.noti_result_back && !DuplicatesActivity.this.fromnotiFication) {
                        Log.d("ONCANCEL", "33333");
                        MobiClean.getInstance().duplicatesData.clearAll();
                        DuplicatesActivity.this.context = null;
                        DuplicatesActivity.this.finish();
                    } else {
                        DuplicatesActivity.this.finish();
                    }
                }
                super.onCancelled();
            }

            @Override
            public void onPreExecute() {
                DuplicatesActivity.this.j = 0;
                Log.d("SIZEEEEEE", "preex = 11");
                GlobalData.isExecuting = true;
                Util.appendLogmobiclean(DuplicatesActivity.this.TAG, " createGroupsTask onpre", "");
                DuplicatesActivity.this.dialogg = new Dialog(DuplicatesActivity.this.context, R.style.AppTheme);
                DuplicatesActivity.this.dialogg.requestWindowFeature(1);
                DuplicatesActivity.this.dialogg.getWindow().setBackgroundDrawable(new ColorDrawable(1426063360));
                DuplicatesActivity.this.dialogg.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                DuplicatesActivity.this.dialogg.setContentView(R.layout.custam_progress_duplicate);
                DuplicatesActivity.this.dialogg.setCancelable(true);
                DuplicatesActivity.this.dialogg.setCanceledOnTouchOutside(false);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(DuplicatesActivity.this.dialogg.getWindow().getAttributes());
                DuplicatesActivity.this.dialogg.getWindow().setAttributes(layoutParams);
                DuplicatesActivity duplicatesActivity = DuplicatesActivity.this;
                duplicatesActivity.m = (TextView) duplicatesActivity.dialogg.findViewById(R.id.update_text);
                DuplicatesActivity.this.dialogg.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() { // from class: com.mobiclean.phoneclean.similerphotos.DuplicatesActivity.21.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        if (DuplicatesActivity.this.multipleClicked()) {
                            return;
                        }
                        DuplicatesActivity.this.scanInterruptedDialog();
                    }
                });
                DuplicatesActivity duplicatesActivity2 = DuplicatesActivity.this;
                duplicatesActivity2.q = (FrameLayout) duplicatesActivity2.dialogg.findViewById(R.id.frame_mid_laysss);
                DuplicatesActivity.this.dialogg.show();
                DuplicatesActivity.this.dialogg.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        DuplicatesActivity.this.scanInterrupted();
                    }
                });
                super.onPreExecute();
            }

            @Override // android.os.AsyncTask
            public String doInBackground(String... strArr) {
                Log.d("SIZEEEEEE", "doInBackground = 11");
                Util.appendLogmobiclean(DuplicatesActivity.this.TAG, " createGroupsTask doInBackground ", "");
                MobiClean.getInstance().duplicatesData.grouplist.clear();
                publishProgress(DuplicatesActivity.this.getString(R.string.mbc_searchiing_duplicate));
                int i = 0;
                for (int i2 = 0; i2 < MobiClean.getInstance().duplicatesData.imageList.size(); i2++) {
                    try {
                        DuplicatesActivity.this.j = i2;
                        ImageDetail imageDetail = MobiClean.getInstance().duplicatesData.imageList.get(i2);
                        String str = DuplicatesActivity.this.TAG;
                        Util.appendLogmobiclean(str, "File name for processing====" + imageDetail.name, "");
                        boolean checkInGroups = DuplicatesActivity.this.checkInGroups(imageDetail);
                        Log.d("VALLLLS", DuplicatesActivity.this.distance + "  " + DuplicatesActivity.this.time);
                        if (checkInGroups) {
                            i++;
                        }
                        publishProgress("" + ((i2 * 100) / MobiClean.getInstance().duplicatesData.imageList.size()) + "% " + DuplicatesActivity.this.getResources().getString(R.string.mbc_completed) + IOUtils.LINE_SEPARATOR_UNIX + i + " " + DuplicatesActivity.this.getResources().getString(R.string.mbc_duplicatesfound));
                        String str2 = DuplicatesActivity.this.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("after adding group list size===");
                        sb.append(MobiClean.getInstance().duplicatesData.grouplist.size());
                        Util.appendLogmobiclean(str2, sb.toString(), "");
                        for (int i3 = 0; i3 < MobiClean.getInstance().duplicatesData.grouplist.size(); i3++) {
                            if (!GlobalData.shouldContinue) {
                                return null;
                            }
                            if (DuplicatesActivity.this.options.size() > 0) {
                                if (((String) DuplicatesActivity.this.options.get(0)).equals(DuplicatesActivity.this.getString(R.string.mbc_low_resolution))) {
                                    Collections.sort(MobiClean.getInstance().duplicatesData.grouplist.get(i3).children, new Comparator<ImageDetail>() {
                                        @Override
                                        public int compare(ImageDetail imageDetail2, ImageDetail imageDetail3) {
                                            return imageDetail3.height - imageDetail2.height;
                                        }
                                    });
                                }
                                if (((String) DuplicatesActivity.this.options.get(0)).equals(DuplicatesActivity.this.getString(R.string.mbc_high_resolution))) {
                                    try {
                                        Collections.sort(MobiClean.getInstance().duplicatesData.grouplist.get(i3).children, new Comparator<ImageDetail>() {
                                            public int compare(ImageDetail imageDetail2, ImageDetail imageDetail3) {
                                                return imageDetail2.height - imageDetail3.height;
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (((String) DuplicatesActivity.this.options.get(0)).equals(DuplicatesActivity.this.getString(R.string.mbc_image_size_smaller))) {
                                    try {
                                        Collections.sort(MobiClean.getInstance().duplicatesData.grouplist.get(i3).children, new Comparator<ImageDetail>() {
                                            @Override
                                            public int compare(ImageDetail imageDetail2, ImageDetail imageDetail3) {
                                                return (int) (imageDetail3.size - imageDetail2.size);
                                            }
                                        });
                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                }
                                if (((String) DuplicatesActivity.this.options.get(0)).equals(DuplicatesActivity.this.getString(R.string.mbc_image_size_larger))) {
                                    try {
                                        Collections.sort(MobiClean.getInstance().duplicatesData.grouplist.get(i3).children, new Comparator<ImageDetail>() {
                                            @Override
                                            public int compare(ImageDetail imageDetail2, ImageDetail imageDetail3) {
                                                return (int) (imageDetail2.size - imageDetail3.size);
                                            }
                                        });
                                    } catch (Exception e3) {
                                        e3.printStackTrace();
                                    }
                                }
                                if (((String) DuplicatesActivity.this.options.get(0)).equals(DuplicatesActivity.this.getString(R.string.mbc_earlier_date))) {
                                    try {
                                        Collections.sort(MobiClean.getInstance().duplicatesData.grouplist.get(i3).children, new Comparator<ImageDetail>() {
                                            @Override
                                            public int compare(ImageDetail imageDetail2, ImageDetail imageDetail3) {
                                                return (int) (imageDetail3.addDateInMSec.longValue() - imageDetail2.addDateInMSec.longValue());
                                            }
                                        });
                                    } catch (Exception e4) {
                                        e4.printStackTrace();
                                    }
                                }
                                if (((String) DuplicatesActivity.this.options.get(0)).equals(DuplicatesActivity.this.getString(R.string.mbc_latest_date))) {
                                    try {
                                        Collections.sort(MobiClean.getInstance().duplicatesData.grouplist.get(i3).children, new Comparator<ImageDetail>() {
                                            @Override
                                            public int compare(ImageDetail imageDetail2, ImageDetail imageDetail3) {
                                                return (int) (imageDetail2.addDateInMSec.longValue() - imageDetail3.addDateInMSec.longValue());
                                            }
                                        });
                                    } catch (Exception e5) {
                                        e5.printStackTrace();
                                    }
                                }
                            }
                        }
                        if (!GlobalData.shouldContinue) {
                            return null;
                        }
                        if (MobiClean.getInstance().duplicatesData != null) {
                            MobiClean.getInstance().duplicatesData.fillDisplayedImageList();
                        }
                    } catch (Exception e6) {
                        e6.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            public void onPostExecute(String str) {
                Log.d("SIZEEEEEE", "onPostExecute = 33");
                DuplicatesActivity.isLVLChange = false;
                DuplicatesActivity.isModeChange = false;
                if (GlobalData.shouldContinue) {
                    System.runFinalization();
                    Runtime.getRuntime().gc();
                    System.gc();
                    Util.appendLogmobiclean(DuplicatesActivity.this.TAG, " createGroupsTask onPostExecute ", "");
                    try {
                        if (DuplicatesActivity.this.dialogg != null && DuplicatesActivity.this.dialogg.isShowing()) {
                            DuplicatesActivity.this.dialogg.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DuplicatesActivity.this.showGroups();
                    GlobalData.isExecuting = false;
                    super.onPostExecute(str);
                }
            }

            @Override
            public void onProgressUpdate(String... strArr) {
                try {
                    TextView textView = DuplicatesActivity.this.m;
                    textView.setText("" + strArr[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.onProgressUpdate(strArr);
            }
        };
        this.createGroupsTask = asyncTask;
        asyncTask.execute(new String[0]);
    }

    private void deletion(final int i, final int i2, final Intent intent) {
        try {
            new AsyncTask<Void, Void, FilesGridActivity.DELETION>() {
                @Override
                public void onPreExecute() {
                    DuplicatesActivity.this.displayProgress = new ProgressDialog(DuplicatesActivity.this);
                    DuplicatesActivity.this.getWindow().addFlags(2097280);
                    ProgressDialog progressDialog = DuplicatesActivity.this.displayProgress;
                    progressDialog.setTitle("" + DuplicatesActivity.this.getResources().getString(R.string.mbc_cleaning));
                    DuplicatesActivity.this.displayProgress.setCanceledOnTouchOutside(false);
                    DuplicatesActivity.this.displayProgress.setProgressStyle(1);
                    DuplicatesActivity.this.displayProgress.setCancelable(false);
                    DuplicatesActivity.this.displayProgress.setMax(MobiClean.getInstance().duplicatesData.selectedForDelete().size());
                    DuplicatesActivity.this.displayProgress.show();
                }

                @Override
                @RequiresApi(api = 21)
                public FilesGridActivity.DELETION doInBackground(Void... voidArr) {
                    if (FileUtil.isSystemAndroid5()) {
                        FilesGridActivity.onActivityResultLollipop(DuplicatesActivity.this, i, i2, intent);
                    }
                    return DuplicatesActivity.this.permissionBasedDeletion();
                }

                @Override
                public void onPostExecute(FilesGridActivity.DELETION deletion) {
                    if (DuplicatesActivity.this.displayProgress != null) {
                        DuplicatesActivity.this.displayProgress.dismiss();
                    }
                    DuplicatesActivity.this.getWindow().clearFlags(128);
                    int i3 = AnonymousClass24.f5067a[deletion.ordinal()];
                    if (i3 == 1) {
                        Toast.makeText(DuplicatesActivity.this, "", Toast.LENGTH_LONG).show();
                    } else if (i3 == 2) {
                        DuplicatesActivity.this.permissionAlert();
                    } else if (i3 == 3) {
                        DuplicatesActivity.this.successDeletion();
                    } else if (i3 != 5) {
                    } else {
                        DuplicatesActivity.this.successDeletion();
                    }
                }
            }.execute(new Void[0]);
        } catch (Exception e) {
            String str = this.TAG;
            Util.appendLogmobiclean(str, "Normal deletion exception====" + e.getMessage(), GlobalData.FILE_NAME);
        }
    }

    public void deletionTask() {
        Util.appendLogmobiclean(this.TAG, "method deletionTask calling", GlobalData.FILE_NAME);
        this.deleteTask = new AsyncTask<String, Integer, FilesGridActivity.DELETION>() { // from class: com.mobiclean.phoneclean.similerphotos.DuplicatesActivity.4
            private boolean deleteImageFile(File file) {
                boolean z;
                if (!file.exists()) {
                    DuplicatesActivity.this.updateMediaScannerPath(file);
                    z = true;
                } else {
                    delete(file);
                    z = !file.exists();
                    if (z) {
                        DuplicatesActivity.this.updateMediaScannerPath(file);
                    } else {
                        FileUtil.isKitKat();
                    }
                }
                if (Build.VERSION.SDK_INT == 21) {
                    return true;
                }
                return z;
            }

            private FilesGridActivity.DELETION normalDeletion() {
                try {
                    try {
                        Util.appendLogmobiclean(DuplicatesActivity.this.TAG, "in normal deletion", "");
                        Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, " normal deletion befire loop");
                        int i = 0;
                        for (int i2 = 0; i2 < MobiClean.getInstance().duplicatesData.grouplist.size(); i2++) {
                            int size = MobiClean.getInstance().duplicatesData.grouplist.get(i2).children.size();
                            int i3 = 0;
                            while (i3 < size) {
                                ImageDetail imageDetail = MobiClean.getInstance().duplicatesData.grouplist.get(i2).children.get(i3);
                                if (imageDetail.ischecked) {
                                    Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, " normal deletion befire deleteImageFile");
                                    if (deleteImageFile(new File(imageDetail.path))) {
                                        Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, " normal deletion befire deleteImageFile");
                                        Util.appendLogmobiclean(DuplicatesActivity.this.TAG, "normal delete success with path===" + imageDetail.path, "");
                                        i++;
                                        DuplicatesActivity.this.displayProgress.setProgress(i);
                                        MobiClean.getInstance().duplicatesData.removeNode(imageDetail);
                                        MobiClean.getInstance().duplicatesData.grouplist.get(i2).children.remove(i3);
                                        size--;
                                    } else {
                                        DuplicatesActivity duplicatesActivity = DuplicatesActivity.this;
                                        duplicatesActivity.y++;
                                        Util.appendLogmobiclean(duplicatesActivity.TAG, "normal delete failed with path===" + imageDetail.path, "");
                                    }
                                }
                                i3++;
                            }
                        }
                        if (DuplicatesActivity.this.y > 0) {
                            return FilesGridActivity.DELETION.NOTDELETION;
                        }
                        return FilesGridActivity.DELETION.SUCCESS;
                    } catch (Exception e) {
                        Util.appendLogmobiclean(DuplicatesActivity.this.TAG, "Normal deletion exception====" + e.getMessage(), GlobalData.FILE_NAME);
                        Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, "22 Normal deletion exception====" + e.getMessage());
                        FilesGridActivity.DELETION deletion = FilesGridActivity.DELETION.ERROR;
                        if (DuplicatesActivity.this.y > 0) {
                            return FilesGridActivity.DELETION.NOTDELETION;
                        }
                        return FilesGridActivity.DELETION.SUCCESS;
                    }
                } catch (Throwable unused) {
                    if (DuplicatesActivity.this.y > 0) {
                        return FilesGridActivity.DELETION.NOTDELETION;
                    }
                    return FilesGridActivity.DELETION.SUCCESS;
                }
            }

            public boolean delete(File file) {
                file.delete();
                if (file.exists()) {
                    String[] strArr = {file.getAbsolutePath()};
                    ContentResolver contentResolver = DuplicatesActivity.this.getContentResolver();
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
                DuplicatesActivity.this.y = 0;
                super.onPreExecute();
                Util.appendLogmobiclean(DuplicatesActivity.this.TAG, "method deletionTask pre execute calling", GlobalData.FILE_NAME);
                Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, "method deletionTask pre execute calling");
                DuplicatesActivity.this.displayProgress = new ProgressDialog(DuplicatesActivity.this);
                DuplicatesActivity.this.getWindow().addFlags(2097280);
                ProgressDialog progressDialog = DuplicatesActivity.this.displayProgress;
                progressDialog.setTitle("" + DuplicatesActivity.this.getResources().getString(R.string.mbc_cleaning));
                DuplicatesActivity.this.displayProgress.setCanceledOnTouchOutside(false);
                DuplicatesActivity.this.displayProgress.setProgressStyle(1);
                DuplicatesActivity.this.displayProgress.setCancelable(false);
                DuplicatesActivity.this.displayProgress.setMax(MobiClean.getInstance().duplicatesData.selectedForDelete().size());
                DuplicatesActivity.this.displayProgress.show();
            }

            @Override
            public FilesGridActivity.DELETION doInBackground(String... strArr) {
                Util.appendLogmobiclean(DuplicatesActivity.this.TAG, "method deletionTask doinbackground calling", GlobalData.FILE_NAME);
                Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, "in doin background");
                if (strArr != null) {
                    Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, "string != null");
                    return DuplicatesActivity.this.permissionBasedDeletion();
                }
                Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, "in doinbg bfore isdeletionbelowsix ");
                if (FileUtil.IsDeletionBelow6()) {
                    Util.appendLogmobiclean(DuplicatesActivity.this.TAG, "method deletionTask os below 5.0", GlobalData.FILE_NAME);
                    return normalDeletion();
                }
                Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, "in doinbg after isdeletionbelowsix ");
                ArrayList<String> returnMountPOints = MountPoints.returnMountPOints(DuplicatesActivity.this);
                String str = DuplicatesActivity.this.TAG;
                Util.appendLogmobicleanbug(str, "in doinbg mount points " + returnMountPOints);
                if (returnMountPOints == null) {
                    String str2 = DuplicatesActivity.this.TAG;
                    Util.appendLogmobicleanbug(str2, "in doinbg mount points == null " + returnMountPOints);
                    Util.appendLogmobiclean(DuplicatesActivity.this.TAG, "mount points arr is null", GlobalData.FILE_NAME);
                    return normalDeletion();
                } else if (returnMountPOints.size() == 1) {
                    String str3 = DuplicatesActivity.this.TAG;
                    Util.appendLogmobicleanbug(str3, "in doinbg mount points == 1 " + returnMountPOints.size());
                    Util.appendLogmobiclean(DuplicatesActivity.this.TAG, "mount points arr size is 1", GlobalData.FILE_NAME);
                    return normalDeletion();
                } else {
                    String str4 = DuplicatesActivity.this.TAG;
                    Util.appendLogmobiclean(str4, "mount points arr size is " + returnMountPOints.size(), GlobalData.FILE_NAME);
                    String str5 = DuplicatesActivity.this.TAG;
                    Util.appendLogmobicleanbug(str5, "in doinbg mount points size ==  " + returnMountPOints.size());
                    File file = new File(returnMountPOints.get(1));
                    if (file.listFiles() == null || file.listFiles().length == 0) {
                        Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, "in doinbg temp.listFiles() == null || temp.listFiles().length == 0 ");
                        return normalDeletion();
                    } else if (Build.VERSION.SDK_INT > 21) {
                        if (!DuplicatesActivity.this.isBothStorageCanDelete(returnMountPOints)) {
                            String str6 = DuplicatesActivity.this.TAG;
                            Util.appendLogmobicleanbug(str6, "in doinbg Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP both can't delete " + returnMountPOints.size());
                            Util.appendLogmobiclean(DuplicatesActivity.this.TAG, "file is not able to write on external.Taking Permission", GlobalData.FILE_NAME);
                            return FilesGridActivity.DELETION.PERMISSION;
                        }
                        String str7 = DuplicatesActivity.this.TAG;
                        Util.appendLogmobicleanbug(str7, "in doinbg Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP both can delete " + returnMountPOints.size());
                        Util.appendLogmobiclean(DuplicatesActivity.this.TAG, "file is able to write on external", GlobalData.FILE_NAME);
                        return DuplicatesActivity.this.permissionBasedDeletion();
                    } else {
                        return FilesGridActivity.DELETION.SUCCESS;
                    }
                }
            }

            @Override
            public void onPostExecute(FilesGridActivity.DELETION deletion) {
                super.onPostExecute(deletion);
                String str = DuplicatesActivity.this.TAG;
                Util.appendLogmobiclean(str, "method deletionTask onPostExecute() calling with status====" + deletion.name(), GlobalData.FILE_NAME);
                Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, "on post execute  ");
                if (DuplicatesActivity.this.displayProgress != null && DuplicatesActivity.this.displayProgress.isShowing()) {
                    DuplicatesActivity.this.displayProgress.dismiss();
                }
                DuplicatesActivity.this.getWindow().clearFlags(128);
                String str2 = DuplicatesActivity.this.TAG;
                Util.appendLogmobicleanbug(str2, "onpostexecute b4 switch case " + deletion.name());
                int i = AnonymousClass24.f5067a[deletion.ordinal()];
                if (i == 1) {
                    Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, DuplicatesActivity.this.getString(R.string.mbc_deletion_err));
                    DuplicatesActivity duplicatesActivity = DuplicatesActivity.this;
                    Toast.makeText(duplicatesActivity, duplicatesActivity.getString(R.string.mbc_deletion_err), Toast.LENGTH_LONG).show();
                } else if (i == 2) {
                    Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, "bfore permissionAlert");
                    DuplicatesActivity.this.permissionAlert();
                } else if (i == 3) {
                    Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, "onpostexecute success");
                    DuplicatesActivity.this.successDeletion();
                } else if (i != 4) {
                    if (i != 5) {
                        return;
                    }
                    Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, "onpostexecute successdeletion");
                    DuplicatesActivity.this.successDeletion();
                } else {
                    Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, "onpostexecute selection");
                    DuplicatesActivity duplicatesActivity2 = DuplicatesActivity.this;
                    Toast.makeText(duplicatesActivity2, "" + DuplicatesActivity.this.getResources().getString(R.string.mbc_at_leastone), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgressUpdate(Integer... numArr) {
                super.onProgressUpdate(numArr);
                DuplicatesActivity.this.displayProgress.setProgress(numArr[0].intValue());
            }
        };
        ArrayList<String> returnMountPOints = MountPoints.returnMountPOints(this);
        String str = this.TAG;
        Util.appendLogmobicleanbug(str, "Mount Points " + returnMountPOints);
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

    private void fillOptions() {
        if (GlobalData.isObjExist(this, GlobalData.SmartFile)) {
            this.options = new ArrayList<>();
        }
    }


    public ArrayList<ImageDetail> getCurrentlySelectedGroup(String str) {
        int i = 0;
        for (Integer num : this.duplicateGridImages.keySet()) {
            i++;
            ArrayList<ImageDetail> arrayList = new ArrayList<>(this.duplicateGridImages.get(Integer.valueOf(num.intValue())));
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                if (arrayList.get(i2).path.equals(str)) {
                    MobiClean.getInstance().duplicatesData.currentGroupIndex = i;
                    MobiClean.getInstance().duplicatesData.currentGroupChildIndex = i2;
                    return arrayList;
                }
            }
        }
        return null;
    }

    public static Drawable getDrawable(Context context, int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return ContextCompat.getDrawable(context, i);
        }
        return context.getResources().getDrawable(i);
    }

    private void handleAutoMarkReq() {
        fillOptions();
        for (int i = 0; i < MobiClean.getInstance().duplicatesData.grouplist.size(); i++) {
            if (this.options.size() > 0) {
                if (this.options.get(0).equals(getString(R.string.mbc_low_resolution))) {
                    try {
                        Collections.sort(MobiClean.getInstance().duplicatesData.grouplist.get(i).children, new Comparator<ImageDetail>() { // from class: com.mobiclean.phoneclean.similerphotos.DuplicatesActivity.5
                            @Override // java.util.Comparator
                            public int compare(ImageDetail imageDetail, ImageDetail imageDetail2) {
                                return imageDetail2.height - imageDetail.height;
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (this.options.get(0).equals(getString(R.string.mbc_high_resolution))) {
                    try {
                        Collections.sort(MobiClean.getInstance().duplicatesData.grouplist.get(i).children, new Comparator<ImageDetail>() { // from class: com.mobiclean.phoneclean.similerphotos.DuplicatesActivity.6
                            @Override // java.util.Comparator
                            public int compare(ImageDetail imageDetail, ImageDetail imageDetail2) {
                                return imageDetail.height - imageDetail2.height;
                            }
                        });
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (this.options.get(0).equals(getString(R.string.mbc_image_size_smaller))) {
                    try {
                        Collections.sort(MobiClean.getInstance().duplicatesData.grouplist.get(i).children, new Comparator<ImageDetail>() { // from class: com.mobiclean.phoneclean.similerphotos.DuplicatesActivity.7
                            @Override // java.util.Comparator
                            public int compare(ImageDetail imageDetail, ImageDetail imageDetail2) {
                                return (int) (imageDetail2.size - imageDetail.size);
                            }
                        });
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
                if (this.options.get(0).equals(getString(R.string.mbc_image_size_larger))) {
                    try {
                        Collections.sort(MobiClean.getInstance().duplicatesData.grouplist.get(i).children, new Comparator<ImageDetail>() { // from class: com.mobiclean.phoneclean.similerphotos.DuplicatesActivity.8
                            @Override // java.util.Comparator
                            public int compare(ImageDetail imageDetail, ImageDetail imageDetail2) {
                                return (int) (imageDetail.size - imageDetail2.size);
                            }
                        });
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                }
                if (this.options.get(0).equals(getString(R.string.mbc_earlier_date))) {
                    Collections.sort(MobiClean.getInstance().duplicatesData.grouplist.get(i).children, new Comparator<ImageDetail>() { // from class: com.mobiclean.phoneclean.similerphotos.DuplicatesActivity.9
                        @Override // java.util.Comparator
                        public int compare(ImageDetail imageDetail, ImageDetail imageDetail2) {
                            return (int) (imageDetail2.addDateInMSec.longValue() - imageDetail.addDateInMSec.longValue());
                        }
                    });
                }
                if (this.options.get(0).equals(getString(R.string.mbc_latest_date))) {
                    Collections.sort(MobiClean.getInstance().duplicatesData.grouplist.get(i).children, new Comparator<ImageDetail>() { // from class: com.mobiclean.phoneclean.similerphotos.DuplicatesActivity.10
                        @Override // java.util.Comparator
                        public int compare(ImageDetail imageDetail, ImageDetail imageDetail2) {
                            return (int) (imageDetail.addDateInMSec.longValue() - imageDetail2.addDateInMSec.longValue());
                        }
                    });
                }
            }
        }
        if (this.options.size() > 0) {
            if (MobiClean.getInstance().duplicatesData != null) {
                MobiClean.getInstance().duplicatesData.fillDisplayedImageList();
            }
            markAll();
        }
    }

    private void init() {
        this.n = (RelativeLayout) findViewById(R.id.layout_one);
        this.o = (RelativeLayout) findViewById(R.id.layout_two);
        this.p = (RelativeLayout) findViewById(R.id.layout_three);
        this.q = (FrameLayout) findViewById(R.id.frame_mid_laysss);
        this.s = (FrameLayout) findViewById(R.id.frame_mid_laysss);
        this.t = (FrameLayout) findViewById(R.id.frame_mid_laysss_deletion);
        this.u = (TextView) findViewById(R.id.ads_btn_countinue);
        this.v = (TextView) findViewById(R.id.ads_btn_cancel);
        this.ads_title = (TextView) findViewById(R.id.dialog_title);
        this.ads_message = (TextView) findViewById(R.id.dialog_msg);
        this.v.setOnClickListener(this);
        this.u.setOnClickListener(this);
        this.w = (TextView) findViewById(R.id.ads_btn_countinue_deletion);
        this.x = (TextView) findViewById(R.id.ads_btn_cancel_deletion);
        this.ads_title_inapp = (TextView) findViewById(R.id.dialog_title_deletion);
        this.ads_message_inapp = (TextView) findViewById(R.id.dialog_msg_deletion);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        this.mRecyclerView = recyclerView;
        recyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        GlobalData.fromdup = true;
        this.tvduplicates = (TextView) findViewById(R.id.tvnoduplicates);
        this.tv1btn = (TextView) findViewById(R.id.dup_text);
        this.btnlayout = (LinearLayout) findViewById(R.id.dup_btn_layout);
        this.tvcount = (TextView) findViewById(R.id.imagecount);
        this.tvdupcount = (TextView) findViewById(R.id.dupdisplay_sizetv);
        this.tvunit = (TextView) findViewById(R.id.junkdisplay_sizetv_unit);
        this.tvsymbol = (TextView) findViewById(R.id.symbol);
        this.tvcount.setVisibility(View.GONE);
        this.pass_score = (float) (((GlobalData.duplicacyLevel * 10.0f) / 10000.0f) + 0.9d);
        this.distance = GlobalData.duplicacyDist;
        this.time = GlobalData.duplicacyTime;
        this.listView = (ListView) findViewById(R.id.listview);
        this.duplicateGridImages = new TreeMap();
        registerReceiver(this.breceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
    }

    @RequiresApi(api = 21)
    public boolean isBothStorageCanDelete(ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (!FileUtil.isWritableNormalOrSaf(this, new File(arrayList.get(i)))) {
                return false;
            }
        }
        return true;
    }

    private void markAll() {
        MobiClean.getInstance().duplicatesData.markAll();
        setAdapter();
        updateBtnTxt();
        invalidateOptionsMenu();
    }

    public void permissionAlert() {
        Util.appendLogmobicleanbug(this.TAG, "in permission alert");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("" + getResources().getString(R.string.please_grant_permission));
        builder.setPositiveButton("" + getResources().getString(R.string.grant), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Util.appendLogmobicleanbug(DuplicatesActivity.this.TAG, "sd card dialog show");
                DuplicatesActivity duplicatesActivity = DuplicatesActivity.this;
                Splash.showdialog_sdcard(duplicatesActivity, duplicatesActivity);
            }
        });
        builder.setNegativeButton("" + getResources().getString(R.string.deny), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DuplicatesActivity.this.deleteTask.execute(new String[0]);
            }
        });
        builder.show();
    }


    @RequiresApi(api = 21)
    public FilesGridActivity.DELETION permissionBasedDeletion() {
        Util.appendLogmobicleanbug(this.TAG, "in permissionbased deletion");
        try {
            try {
                Util.appendLogmobicleanbug(this.TAG, "in permissionbased deletion before loop");
                int i = 0;
                for (int i2 = 0; i2 < MobiClean.getInstance().duplicatesData.grouplist.size(); i2++) {
                    int size = MobiClean.getInstance().duplicatesData.grouplist.get(i2).children.size();
                    int i3 = 0;
                    while (i3 < size) {
                        ImageDetail imageDetail = MobiClean.getInstance().duplicatesData.grouplist.get(i2).children.get(i3);
                        if (imageDetail.ischecked) {
                            File file = new File(imageDetail.path);
                            Util.appendLogmobicleanbug(this.TAG, "in permissionbased deletion in loop : 1. false");
                            boolean deleteFile = file.delete() ? !file.exists() : FileUtil.deleteFile(this, file);
                            Util.appendLogmobicleanbug(this.TAG, "in permissionbased deletion in loop : 2. " + deleteFile);
                            if (!deleteFile) {
                                deleteFile = FileUtil.deleteFile(this, file);
                            }
                            Util.appendLogmobicleanbug(this.TAG, "in permissionbased deletion in loop : 3. " + deleteFile);
                            if (deleteFile) {
                                Util.appendLogmobicleanbug(this.TAG, "in permissionbased deletion in loop : deleted = true " + deleteFile);
                                Util.appendLogmobiclean(this.TAG, "normal delete success with path===" + imageDetail.path, "");
                                MobiClean.getInstance().duplicatesData.removeNode(imageDetail);
                                MobiClean.getInstance().duplicatesData.grouplist.get(i2).children.remove(i3);
                                i++;
                                this.displayProgress.setProgress(i);
                                updateMediaScannerPath(file);
                                size--;
                            } else {
                                this.y++;
                                Util.appendLogmobicleanbug(this.TAG, "in permissionbased deletion in loop : deleted = false " + deleteFile);
                                Util.appendLogmobiclean(this.TAG, "normal delete failed with path===" + imageDetail.path, "");
                            }
                        }
                        i3++;
                    }
                }
                Util.appendLogmobicleanbug(this.TAG, "in permissionbased deletion finally  notdeleted =  " + this.y);
                if (this.y > 0) {
                    return FilesGridActivity.DELETION.NOTDELETION;
                }
                return FilesGridActivity.DELETION.SUCCESS;
            } catch (Exception e) {
                Util.appendLogmobicleanbug(this.TAG, "in permissionbased deletion exception 1.  " + e.getMessage());
                Util.appendLogmobiclean(this.TAG, "Permission deletion exception====" + e.getMessage(), GlobalData.FILE_NAME);
                FilesGridActivity.DELETION deletion = FilesGridActivity.DELETION.ERROR;
                Util.appendLogmobicleanbug(this.TAG, "in permissionbased deletion finally  notdeleted =  " + this.y);
                if (this.y > 0) {
                    return FilesGridActivity.DELETION.NOTDELETION;
                }
                return FilesGridActivity.DELETION.SUCCESS;
            }
        } catch (Throwable unused) {
            Util.appendLogmobicleanbug(this.TAG, "in permissionbased deletion finally  notdeleted =  " + this.y);
            if (this.y > 0) {
                return FilesGridActivity.DELETION.NOTDELETION;
            }
            return FilesGridActivity.DELETION.SUCCESS;
        }
    }

    private void redirectToNoti() {
        this.redirectToNoti = getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false);
        this.noti_result_back = getIntent().getBooleanExtra(GlobalData.NOTI_RESULT_BACK, false);
        this.fromnotiFication = getIntent().getBooleanExtra("FROMNOTI", false);
    }

    private ArrayList<ImageDetail> removeDeletedItems(ArrayList<ImageDetail> arrayList) {
        ArrayList<ImageDetail> arrayList2 = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if (new File(arrayList.get(i).path).exists()) {
                arrayList2.add(arrayList.get(i));
            }
        }
        return arrayList2;
    }

    private void removeSelections() {
        MobiClean.getInstance().duplicatesData.unmarkAll();
        setAdapter();
        this.tv1btn.setText(R.string.mbc_mbc_empty_compress);
        invalidateOptionsMenu();
    }


    public void scanInterrupted() {
        GlobalData.shouldContinue = false;
        pressCancel = true;
        showGroups();
        GlobalData.isExecuting = false;
        this.dialogg.dismiss();
    }


    public void scanInterruptedDialog() {
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
        ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_warning));
        ((ImageView) dialog.findViewById(R.id.dialog_img)).setLayoutParams(new LinearLayout.LayoutParams(150, 150));
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_scanning_interrupted));
        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(getResources().getString(R.string.mbc_scan_interupt_msg));
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DuplicatesActivity.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DuplicatesActivity.this.multipleClicked()) {
                    return;
                }
                DuplicatesActivity.this.scanInterrupted();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setAdapter() {
        Util.appendLogmobiclean(this.TAG, " markAll ", "");
        int i = 0;
        for (int i2 = 0; i2 < MobiClean.getInstance().duplicatesData.grouplist.size(); i2++) {
            if (MobiClean.getInstance().duplicatesData.grouplist.get(i2).children.size() != 1) {
                i++;
                ArrayList<ImageDetail> arrayList = new ArrayList<>();
                for (int i3 = 0; i3 < MobiClean.getInstance().duplicatesData.grouplist.get(i2).children.size(); i3++) {
                    arrayList.add(MobiClean.getInstance().duplicatesData.grouplist.get(i2).children.get(i3));
                }
                this.duplicateGridImages.put(Integer.valueOf(i), arrayList);
            }
        }
        MobiClean.getInstance().duplicatesData.fillheaderList(this);
        T();
    }

    private void setDeviceDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        int i = displayMetrics.heightPixels;
        this.deviceHeight = i;
        int i2 = displayMetrics.widthPixels;
        this.deviceWidth = i2;
        GlobalData.deviceHeight = i;
        GlobalData.deviceWidth = i2;
    }


    private void settitleBarText() {
        TextView textView = this.tvdupcount;
        textView.setText("" + Util.convertBytes_only(MobiClean.getInstance().duplicatesData.totalDuplicatesSize));
        TextView textView2 = this.tvunit;
        textView2.setText("" + Util.convertBytes_unit(MobiClean.getInstance().duplicatesData.totalDuplicatesSize));
    }


    public void showGroups() {
        try {
            getWindow().clearFlags(128);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MobiClean.getInstance().duplicatesData.filterGroupList();
        String str = this.TAG;
        Util.appendLogmobiclean(str, "createGroups " + MobiClean.getInstance().duplicatesData.grouplist.size(), "");
        int i = 0;
        for (int i2 = 0; i2 < MobiClean.getInstance().duplicatesData.grouplist.size(); i2++) {
            i++;
            ArrayList<ImageDetail> arrayList = new ArrayList<>();
            if (MobiClean.getInstance().duplicatesData.grouplist == null || MobiClean.getInstance().duplicatesData.grouplist.get(i2).children == null) {
                return;
            }
            for (int i3 = 0; i3 < MobiClean.getInstance().duplicatesData.grouplist.get(i2).children.size(); i3++) {
                arrayList.add(MobiClean.getInstance().duplicatesData.grouplist.get(i2).children.get(i3));
            }
            String str2 = this.TAG;
            Util.appendLogmobiclean(str2, "list size====" + arrayList.size(), "");
            if (arrayList.size() > 1) {
                this.duplicateGridImages.put(Integer.valueOf(i), arrayList);
            }
        }
        String str3 = this.TAG;
        Log.d(str3, "createGroups 2 = " + MobiClean.getInstance().duplicatesData.grouplist.size());
        String str4 = this.TAG;
        Log.d(str4, "createGroups headerList " + MobiClean.getInstance().duplicatesData.grouplist.size());
        MobiClean.getInstance().duplicatesData.fillheaderList(this);
        T();
        if (MobiClean.getInstance().duplicatesData.totalselected == 0) {
            this.tv1btn.setText(getString(R.string.mbc_mbc_empty_compress));
        } else if (MobiClean.getInstance().duplicatesData.totalselected == 1) {
            TextView textView = this.tv1btn;
            textView.setText("" + getString(R.string.delete) + " " + MobiClean.getInstance().duplicatesData.totalselected + " " + getString(R.string.photo) + " (" + Util.convertBytes(MobiClean.getInstance().duplicatesData.totalselectedSize) + ")");
        } else {
            TextView textView2 = this.tv1btn;
            textView2.setText("" + getString(R.string.delete) + " " + MobiClean.getInstance().duplicatesData.totalselected + " " + getString(R.string.photo) + "s (" + Util.convertBytes(MobiClean.getInstance().duplicatesData.totalselectedSize) + ")");
        }
        if (MobiClean.getInstance().duplicatesData.headerlist.size() == 0 && !MobiClean.getInstance().duplicatesData.isBackAfterDelete) {
            Intent intent = new Intent(this, NoDuplicates.class);
            int i4 = this.j + 1;
            this.j = i4;
            intent.putExtra("process_imgs", i4);
            intent.putExtra("cancel_press", pressCancel);
            MobiClean.getInstance().duplicatesData.isBackAfterDelete = true;
            startActivityForResult(intent, NO_DUPS_CODE);
            return;
        }
        if (MobiClean.getInstance().duplicatesData.headerlist.size() == 0) {
            this.listView.setVisibility(View.GONE);
            this.tvcount.setVisibility(View.GONE);
            this.tvduplicates.setVisibility(View.VISIBLE);
        } else {
            this.listView.setVisibility(View.VISIBLE);
            this.tvduplicates.setVisibility(View.GONE);
            this.tvcount.setVisibility(View.VISIBLE);
        }
        this.tvcount.setVisibility(View.GONE);
        String str5 = this.TAG;
        Util.appendLogmobiclean(str5, "" + MobiClean.getInstance().duplicatesData.totalselected + " Duplicates Found", "");
        MobiClean.getInstance().duplicatesData.refresh();
        settitleBarText();
        btnClickListner();
        invalidateOptionsMenu();
    }

    public void showRealTimeDialog() {
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
        int admob = 2;

        View space;


        ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_duplicate_photos));
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_duplicate));
        ((TextView) dialog.findViewById(R.id.ll_no)).setText(getResources().getString(R.string.mbc_cancel));
        ((TextView) dialog.findViewById(R.id.ll_yes_txt)).setText(getResources().getString(R.string.mbc_ok));
        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(getResources().getString(R.string.mbc_delete_large_file));
        dialog.findViewById(R.id.ll_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DuplicatesActivity.this.multipleClicked() && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DuplicatesActivity.this.multipleClicked()) {
                    return;
                }
                DuplicatesActivity.this.deletionTask();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }


    private void showStopWaitdialog() {
        this.isWaitScreenShown = true;
        this.dialogStopWait.setCancelable(false);
        this.dialogStopWait.setCanceledOnTouchOutside(false);
        ProgressDialog progressDialog = this.dialogStopWait;
        progressDialog.setMessage("" + getResources().getString(R.string.mbc_stopping_scan) + ", " + getResources().getString(R.string.mbc_please_wait));
        this.dialogStopWait.show();
        this.dialogStopWait.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
    }

    public void stopScanningAndBack() {
        try {
            AsyncTask<String, String, String> asyncTask = this.createGroupsTask;
            if (asyncTask != null) {
                if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                    GlobalData.shouldContinue = false;
                    this.createGroupsTask.cancel(true);
                }
            } else {
                if (!this.redirectToNoti && !this.noti_result_back && !this.fromnotiFication) {
                    finish();
                }
                finish();
                startActivity(new Intent(this.context, HomeActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AsyncTask<String, String, String> asyncTask2 = this.createGroupsTask;
        if (asyncTask2 != null) {
            if (asyncTask2.getStatus() == AsyncTask.Status.RUNNING) {
                showStopWaitdialog();
                return;
            } else if (!this.redirectToNoti && !this.noti_result_back && !this.fromnotiFication) {
                finish();
                return;
            } else {
                finish();
                startActivity(new Intent(this.context, HomeActivity.class));
                return;
            }
        }
        finish();
    }

    public void successDeletion() {
        long j = MobiClean.getInstance().duplicatesData.totalselectedSize;
        MobiClean.getInstance().duplicatesData.reset();
        MobiClean.getInstance().duplicatesData.isBackAfterDelete = true;
        MobiClean.getInstance().duplicatesData.filterGroupList();
        MobiClean.getInstance().duplicatesData.refresh();
        Intent intent = new Intent(this, CommonResultActivity.class);
        intent.putExtra("DATA", "" + Util.convertBytes(j));
        intent.putExtra("not_deleted", this.y);
        intent.putExtra("TYPE", "DUPLICATE");
        GlobalData.afterDelete = true;

            startActivity(intent);


    }

    private void trackIfFromNotification() {
        getIntent().getBooleanExtra("FROMNOTI", false);
    }

    private void updateBtnTxt() {
        if (MobiClean.getInstance().duplicatesData.totalselected == 0) {
            this.tv1btn.setText(getString(R.string.mbc_mbc_empty_compress));
        } else if (MobiClean.getInstance().duplicatesData.totalselected == 1) {
            TextView textView = this.tv1btn;
            textView.setText(getString(R.string.mbc_delete) + " " + MobiClean.getInstance().duplicatesData.totalselected + " " + getString(R.string.photo) + " (" + Util.convertBytes(MobiClean.getInstance().duplicatesData.totalselectedSize) + ")");
        } else {
            TextView textView2 = this.tv1btn;
            textView2.setText(getString(R.string.mbc_delete) + " " + MobiClean.getInstance().duplicatesData.totalselected + " " + getString(R.string.photo) + "s (" + Util.convertBytes(MobiClean.getInstance().duplicatesData.totalselectedSize) + ")");
        }
    }

    public void updateMediaScannerPath(File file) {
        MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String str, Uri uri) {
            }
        });
    }

    private void visitFromNotification() {
        Util.appendLogmobiclean(this.TAG, "not FOM NOTI", "");
        MobiClean.getInstance().duplicatesData.unmarkAll();
        createGroups();
    }

    public void T() {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        for (Map.Entry<Integer, ArrayList<ImageDetail>> entry : this.duplicateGridImages.entrySet()) {
            ArrayList<ImageDetail> value = entry.getValue();
            int i2 = 0;
            for (int i3 = 0; i3 < value.size(); i3++) {
                if (value.get(i3).ischecked) {
                    i2++;
                }
            }
            arrayList.add(new SectionedGridRecyclerViewAdapter.Section(i, getResources().getString(R.string.mbc_group) + entry.getKey(), "" + value.size(), i2 + DialogConfigs.DIRECTORY_SEPERATOR + value.size(), i2 == value.size() - 1));
            i += value.size();
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, this.duplicateGridImages);
        this.dummy = new SectionedGridRecyclerViewAdapter.Section[arrayList.size()];
        SectionedGridRecyclerViewAdapter sectionedGridRecyclerViewAdapter = new SectionedGridRecyclerViewAdapter(this, R.layout.section, this.mRecyclerView, simpleAdapter);
        this.mSectionedAdapter = sectionedGridRecyclerViewAdapter;
        sectionedGridRecyclerViewAdapter.setSections((SectionedGridRecyclerViewAdapter.Section[]) arrayList.toArray(this.dummy));
        this.mRecyclerView.setAdapter(this.mSectionedAdapter);
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

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2415 && i2 == -1) {
            deletion(i, i2, intent);
        } else if (i == 1047 && i2 == -1) {
            handleAutoMarkReq();
        } else if (i != 1268 || i2 != -1) {
            if (i == 34 && -1 == i2) {
                if (MobiClean.getInstance().duplicatesData.grouplist.size() == 1 && MobiClean.getInstance().duplicatesData.grouplist.get(0).children.size() == 1) {
                    finish();
                }
                showGroups();
            }
        } else if (intent == null) {
            finish();
        } else {
            pressCancel = intent.getBooleanExtra("cancel_press", false);
            isModeChange = intent.getBooleanExtra("is_mode_change", false);
            isLVLChange = intent.getBooleanExtra("is_lvl_change", false);
            int c2 = MySharedPreference.c(this);
            GlobalData.duplicacyLevel = c2;
            this.pass_score = (float) (((c2 * 10.0f) / 10000.0f) + 0.9d);
            GlobalData.shouldContinue = true;
            GlobalData.proceededToAd = false;
            if (isLVLChange && !isModeChange) {
                showGroups();
            } else if (pressCancel || isModeChange) {
                createGroups();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!GlobalData.shouldContinue && this.isWaitScreenShown) {
            Util.appendLogmobiclean(this.TAG, "junk returning as onBackPressed wait stop already open", GlobalData.FILE_NAME);
        } else if (GlobalData.fromSpacemanager) {
            promptBack();
        } else if (MobiClean.getInstance().duplicatesData.totalDuplicates > 0) {
            promptBackDialog();
        } else {
            promptBack();
        }
    }

    @Override
    public void onClick(View view) {
        RelativeLayout relativeLayout;
        if (multipleClicked()) {
            return;
        }
        if (view.getId() == R.id.ads_btn_cancel && (relativeLayout = this.o) != null && relativeLayout.getVisibility() == View.VISIBLE) {
            this.o.setVisibility(View.GONE);
            this.n.setVisibility(View.VISIBLE);
        }
        if (view.getId() == R.id.ads_btn_countinue) {
            this.o.setVisibility(View.VISIBLE);
            this.n.setVisibility(View.GONE);
            stopScanningAndBack();
        }
        if (view.getId() == R.id.ads_btn_countinue_deletion) {
            this.p.setVisibility(View.GONE);
            this.n.setVisibility(View.VISIBLE);
            deletionTask();
        }
        if (view.getId() == R.id.ads_btn_cancel_deletion) {
            this.p.setVisibility(View.GONE);
            this.n.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_duplicates_screen);

        Util.saveScreen(getClass().getName(), this);
        if (bundle != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                if (GlobalData.isObjExist(this, "img_list")) {
                    try {
                        MobiClean.getInstance().duplicatesData.imageList = (ArrayList) GlobalData.getObj(this, "img_list");
                        for (int i = 0; i < MobiClean.getInstance().duplicatesData.imageList.size(); i++) {
                            MobiClean.getInstance().duplicatesData.imageList.get(i).exif = new ExifInterface(MobiClean.getInstance().duplicatesData.imageList.get(i).path);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e2) {
                        e2.printStackTrace();
                    }
                }
            } else {
                MobiClean.getInstance().duplicatesData.imageList = (ArrayList) bundle.getSerializable("img_list");
                for (int i2 = 0; i2 < MobiClean.getInstance().duplicatesData.imageList.size(); i2++) {
                    try {
                        MobiClean.getInstance().duplicatesData.imageList.get(i2).exif = new ExifInterface(MobiClean.getInstance().duplicatesData.imageList.get(i2).path);
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
            GlobalData.duplicacyDist = bundle.getInt("dupli_dist");
            GlobalData.duplicacyTime = bundle.getInt("dupli_time");
            GlobalData.duplicacyLevel = bundle.getInt("dupli_level");
        }
        GlobalData.imageviewed = false;
        getWindow().addFlags(128);
//        setSupportActionBar((Toolbar) findViewById(R.id.DUPtoolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
        fillOptions();
        GlobalData.shouldContinue = true;
        GlobalData.proceededToAd = false;
        Log.d("CRITERIA1", GlobalData.duplicacyDist + " " + GlobalData.duplicacyLevel + "  " + GlobalData.duplicacyTime);
        Util.appendLogmobiclean(this.TAG, "oncreate ", "");
        String str = this.TAG;
        Util.appendLogmobiclean(str, "criteria ", GlobalData.duplicacyDist + " " + GlobalData.duplicacyLevel + "  " + GlobalData.duplicacyTime);
        setDeviceDimensions();
        StringBuilder sb = new StringBuilder();
        sb.append("1111");
        sb.append(GlobalData.shouldContinue);
        Log.d("CALLLLLED", sb.toString());
        this.context = this;
        this.dialogStopWait = new ProgressDialog(this.context);
        init();
        redirectToNoti();
        this.fromnotiFication = getIntent().getBooleanExtra("FROMNOTI", false);
        if (MobiClean.getInstance().duplicatesData == null) {
            MobiClean.getInstance().duplicatesData = new DuplicatesData();
        }
        boolean z = MobiClean.getInstance().duplicatesData.alreadyScanned;
        if (this.fromnotiFication) {
            visitFromNotification();
        } else {
            Util.appendLogmobiclean(this.TAG, "NOT FROM NOTI", "");
            if (z) {
                Log.d(this.TAG, "alreadyScanned");
                showGroups();
            } else {
                createGroups();
            }
        }
        clearNotification();
        trackIfFromNotification();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dup_screen, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        try {
            Dialog dialog = this.dialogg;
            if (dialog != null && dialog.isShowing()) {
                this.dialogg.dismiss();
            }
            GlobalData.shouldContinue = false;
            BatteryChangeReceiver batteryChangeReceiver = this.breceiver;
            if (batteryChangeReceiver != null) {
                unregisterReceiver(batteryChangeReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_automark) {
            if (!MySharedPreference.i(this)) {

                    Intent intent = new Intent(DuplicatesActivity.this, SmartSelection.class);
                    intent.putExtra("auto_mark", true);
                    startActivityForResult(intent, automarkcode);


            } else {
                handleAutoMarkReq();
            }
            return true;
        } else if (itemId == R.id.assistant) {


                startActivityForResult(new Intent(DuplicatesActivity.this, SmartSelection.class), automarkcode);


            return true;
        } else if (itemId == R.id.action_mark) {
            removeSelections();
            return true;
        } else if (itemId == R.id.action_markall) {
            markAll();
            return true;
        } else if (itemId == R.id.action_settingscan) {
                finish();
                startActivity(new Intent(DuplicatesActivity.this, DuplicacyMidSettings.class));
                context = null;

            return true;
        } else if (itemId == R.id.action_unmarkall) {
            removeSelections();
            return true;
        } else if (itemId == 16908332) {
            if (GlobalData.fromSpacemanager) {
                promptBack();
            } else if (MobiClean.getInstance().duplicatesData.totalDuplicates > 0) {
                promptBackDialog();
            } else {
                promptBack();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onResume() {
        LinearLayout linearLayout;
        super.onResume();
        if (GlobalData.proceededToAd) {
            finish();
        }
        if (MobiClean.getInstance().duplicatesData != null && MobiClean.getInstance().duplicatesData.isBackAfterDelete) {
            MobiClean.getInstance().duplicatesData.isBackAfterDelete = false;
            if (GlobalData.imageviewed && !MobiClean.getInstance().duplicatesData.isBackAfterPreviewDelete) {
                refreshRecyclerView();
            } else {
                this.duplicateGridImages.clear();
                setAdapter();
            }
            MobiClean.getInstance().duplicatesData.isBackAfterPreviewDelete = false;
            PrintStream printStream = System.out;
            printStream.println("Header size=======" + MobiClean.getInstance().duplicatesData.headerlist.size());
            if (MobiClean.getInstance().duplicatesData.headerlist.size() == 0) {
                AsyncTask<String, String, String> asyncTask = this.createGroupsTask;
                if (asyncTask != null && asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                    return;
                }
                finish();
            } else {
                this.listView.setVisibility(View.VISIBLE);
                this.tvduplicates.setVisibility(View.GONE);
                setDeleteBtnText();
                settitleBarText();
            }
        }
        if (!Util.isAdsFree(this) || (linearLayout = this.k) == null) {
            return;
        }
        linearLayout.setVisibility(View.GONE);
        this.l.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                GlobalData.saveObj(this, "img_list", MobiClean.getInstance().duplicatesData.imageList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            bundle.putSerializable("img_list", MobiClean.getInstance().duplicatesData.imageList);
        }
        bundle.putInt("dupli_level", GlobalData.duplicacyLevel);
        bundle.putInt("dupli_dist", GlobalData.duplicacyDist);
        bundle.putInt("dupli_time", GlobalData.duplicacyTime);
    }

    public void promptBack() {
        if (GlobalData.fromSpacemanager) {
            AsyncTask<String, String, String> asyncTask = this.createGroupsTask;
            if (asyncTask != null) {
                if (asyncTask.getStatus() != AsyncTask.Status.RUNNING) {
                    stopScanningAndBack();
                    return;
                }
                return;
            }
            finish();
            return;
        }
        scanInterrupted();
    }

    public void promptBackDialog() {
        if (MobiClean.getInstance().duplicatesData.totalDuplicates > 0) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(1);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
            }
            dialog.setContentView(R.layout.new_dialog_junk_cancel);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setGravity(17);
            View space;



            ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_duplicate_photos));
            ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_duplicate));
            ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(getResources().getString(R.string.mbc_clearresult));
            dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (DuplicatesActivity.this.multipleClicked()) {
                        return;
                    }
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (DuplicatesActivity.this.multipleClicked()) {
                        return;
                    }
                    dialog.dismiss();
                    DuplicatesActivity.this.stopScanningAndBack();
                }
            });
            return;
        }
        this.context = null;
        finish();
    }

    public void refreshRecyclerView() {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        for (Map.Entry<Integer, ArrayList<ImageDetail>> entry : this.duplicateGridImages.entrySet()) {
            ArrayList<ImageDetail> value = entry.getValue();
            int i2 = 0;
            int i3 = 0;
            for (int i4 = 0; i4 < value.size(); i4++) {
                if (value.get(i4).lockImg) {
                    i2++;
                }
                if (value.get(i4).ischecked) {
                    i3++;
                }
            }
            boolean z = true;
            if (i2 == 0) {
                if (i3 == value.size() - 1) {
                    String str = getResources().getString(R.string.mbc_group) + entry.getKey();
                    String str2 = "" + value.size();
                    arrayList.add(new SectionedGridRecyclerViewAdapter.Section(i, str, str2, i3 + DialogConfigs.DIRECTORY_SEPERATOR + value.size(), z));
                    i += value.size();
                }
                z = false;
                String str3 = getResources().getString(R.string.mbc_group) + entry.getKey();
                String str22 = "" + value.size();
                arrayList.add(new SectionedGridRecyclerViewAdapter.Section(i, str3, str22, i3 + DialogConfigs.DIRECTORY_SEPERATOR + value.size(), z));
                i += value.size();
            } else {
                if (i2 + i3 == value.size()) {
                    String str32 = getResources().getString(R.string.mbc_group) + entry.getKey();
                    String str222 = "" + value.size();
                    arrayList.add(new SectionedGridRecyclerViewAdapter.Section(i, str32, str222, i3 + DialogConfigs.DIRECTORY_SEPERATOR + value.size(), z));
                    i += value.size();
                }
                z = false;
                String str322 = getResources().getString(R.string.mbc_group) + entry.getKey();
                String str2222 = "" + value.size();
                arrayList.add(new SectionedGridRecyclerViewAdapter.Section(i, str322, str2222, i3 + DialogConfigs.DIRECTORY_SEPERATOR + value.size(), z));
                i += value.size();
            }
        }
        this.mSectionedAdapter.setSections((SectionedGridRecyclerViewAdapter.Section[]) arrayList.toArray(this.dummy));
    }

    public void setDeleteBtnText() {
        if (MobiClean.getInstance().duplicatesData.totalselected == 0) {
            this.tv1btn.setText(R.string.mbc_mbc_empty_compress);
        } else if (MobiClean.getInstance().duplicatesData.totalselected == 1) {
            TextView textView = this.tv1btn;
            textView.setText("" + getString(R.string.delete) + " " + MobiClean.getInstance().duplicatesData.totalselected + " " + getString(R.string.photo) + " (" + Util.convertBytes(MobiClean.getInstance().duplicatesData.totalselectedSize) + ")");
        } else {
            TextView textView2 = this.tv1btn;
            textView2.setText("" + getString(R.string.delete) + " " + MobiClean.getInstance().duplicatesData.totalselected + " " + getString(R.string.photo) + "s (" + Util.convertBytes(MobiClean.getInstance().duplicatesData.totalselectedSize) + ")");
        }
    }
}