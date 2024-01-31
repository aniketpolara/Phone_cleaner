package com.cleanPhone.mobileCleaner.tools;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.google.firebase.messaging.ServiceStarter;
import com.cleanPhone.mobileCleaner.CommonResultActivity;
import com.cleanPhone.mobileCleaner.DownloadDataFetchedListener;
import com.cleanPhone.mobileCleaner.DownloadsActivity;
import com.cleanPhone.mobileCleaner.DownloadsData;
import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.SocialAnimationActivity;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.similerphotos.DuplicacyMidSettings;
import com.cleanPhone.mobileCleaner.similerphotos.DuplicacyRefreshAsyncTask;
import com.cleanPhone.mobileCleaner.similerphotos.DuplicacyUtil;
import com.cleanPhone.mobileCleaner.similerphotos.DuplicateGroup;
import com.cleanPhone.mobileCleaner.similerphotos.DuplicatesActivity;
import com.cleanPhone.mobileCleaner.similerphotos.ImageDetail;
import com.cleanPhone.mobileCleaner.socialmedia.MediaList;
import com.cleanPhone.mobileCleaner.socialmedia.SocialMedia;
import com.cleanPhone.mobileCleaner.socialmedia.SocialmediaModule;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.PermitActivity;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.BigSizeFilesWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpaceManagerActivity extends PermitActivity implements View.OnClickListener {
    private static final int REQUEST_PERMISSIONS = 212;
    public static boolean isApply = false;
    private LinearLayout adView;
    private ApkCalculation apkCalculation;
    private AudioCalculation audioCalculation;
    private int completedTask;
    private Context context;
    private int countGroups;
    private int defaultProgress;
    int admob = 3;

    View space;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    private ProgressDialog dialogStopWait;
    private int distance;
    private DocCalculation docCalculation;
    private DownloadsCalculation downloadsCalculation;
    private DuplicacyRefreshAsyncTask duplicacyRefreshAsyncTask;
    private ExecutorService executorService;
    private String from;
    private boolean fromToolBox;
    private RelativeLayout hiddenPermissionLayout;
    private HashMap<String, Boolean> hmExtnApk;
    private HashMap<String, Boolean> hmExtnDocs;
    private HashMap<String, Boolean> hmExtnsOtherMediaAndDocs;
    private ImageCalculation imagecalculation;
    public TextView k;
    public ArrayList l;
    private ArrayList<ImageDetail> list;
    private long mLastClickTime;
    private MediaList mediasList;
    private boolean noti_result_back;
    private OtherCalculation otherCalculation;
    private float pass_score;
    private boolean proceeded;
    private boolean redirectToNoti;
    private RecyclerView rv_suggestion;
    private SearchView searchView;
    private SharedPrefUtil sharedPrefUtil;
    private SocialMedia socialMedia;
    private SuggestionListAdapter suggestionListAdapter;
    private TextView t_cpuuses;
    private TextView t_text;
    private TextView t_unit;
    private int time;
    private int totcountDuplicates;
    private TextView tv_allSpace;
    private TextView tv_no_result_found;
    private TextView tvdownCount;
    private TextView tvdownSize;
    private TextView tvdupCount;
    private TextView tvdupSize;
    private VideoCalculation videoCalculation;
    public EventBus j = EventBus.getDefault();
    private boolean isExecuting = false;
    private long totSizeDup = 0;
    private String TAG = "SpaceManagerActivity";
    private boolean duplicatesTapped = false;
    private String[] imageArray = {"0 MB", "1 MB", "2 MB", "3 MB", "4 MB", "5 MB", "6 MB", "7 MB", "8 MB", "9 MB", "10 MB", "15 MB", "20 MB", "25 MB"};
    private String[] videoArray = {"0 MB", "5 MB", "10 MB", "15 MB", "25 MB", "30 MB", "50 MB", "75 MB", "100 MB", "200 MB", "350 MB", "500 MB", "750 MB", "1 GB"};
    private String[] audioArray = {"0 MB", "1 MB", "2 MB", "3 MB", "4 MB", "5 MB", "7 MB", "10 MB", "12 MB", "15 MB", "20 MB", "25 MB", "50 MB", "100 MB"};
    private String[] apkArray = {"0 MB", "1 MB", "2 MB", "3 MB", "4 MB", "5 MB", "7 MB", "10 MB", "12 MB", "15 MB", "20 MB", "25 MB", "50 MB", "100 MB"};
    private String[] filesArray = {"0 KB", "50 KB", "100 KB", "500 KB", "1 MB", "2 MB", "3 MB", "4 MB", "5 MB", "10 MB", "15 MB", "20 MB", "25 MB", "50 MB"};
    private String[] othersArray = {"0 MB", "1 MB", "2 MB", "5 MB", "10 MB", "20 MB", "30 MB", "50 MB", "75 MB", "100 MB", "200 MB", "500 MB", "750 MB", "1 GB"};
    private String[] allArray = {"0 MB", "1 MB", "2 MB", "3 MB", "4 MB", "5 MB", "6 MB", "7 MB", "8 MB", "9 MB", "10 MB", "15 MB", "20 MB", "25 MB"};
    private int[] imageArraySize = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25};
    private int[] videoArraySize = {0, 5, 10, 15, 25, 30, 50, 75, 100, 200, 350, ServiceStarter.ERROR_UNKNOWN, 750, 1024};
    private int[] audioArraysize = {0, 1, 2, 3, 4, 5, 7, 10, 12, 15, 20, 25, 50, 100};
    private int[] apkArraysize = {0, 1, 2, 3, 4, 5, 7, 10, 12, 15, 20, 25, 50, 100};
    private int[] filesArraySize = {0, 50, 100, ServiceStarter.ERROR_UNKNOWN, 1024, 2048, 3072, 4096, 5120, Data.MAX_DATA_BYTES, 15360, 20480, 25600, 51200};
    private int[] othersArraysize = {0, 1, 2, 5, 10, 20, 30, 50, 75, 100, 200, ServiceStarter.ERROR_UNKNOWN, 750, 1024};
    private int[] allArraySize = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25};
    private List arrExtensionImage = Arrays.asList("jpeg", "jpg", "JPEG", "JPG", "png", "gif", "tiff", "tif", "bmp", "svg", "webp");
    private List arrExtensionVideo = Arrays.asList("mp4", "3gp", "avi", "mpeg", "webm", "flv", "wmv", "mkv", "m4a", "wav", "wma", "mmf", "mp2", "flac", "au", "ac3", "mpg", "mov", "mpv", "mpe", "ogg");
    private List arrExtensionAudio = Arrays.asList("mp3", "mpa", "aac", "oga");
    private List textFileExtensions = Arrays.asList("pdf", "doc", "docx", "xls", "ppt", "odt", "rtf", "txt", "pptx", "htm", "html", "log", "csv", "dot", "dotx", "docm", "dotm", "xml", "mht", "dic", "xlsx", "msg", "mhtml", "pps", "xltx", "xlt", "xlsm", "xltm", "ppsx", "pptm", "ppsm");
    private String title = "";
    private boolean duplicatesScanningCompleted = false;
    public ArrayList<BigSizeFilesWrapper> m = new ArrayList<>();

    /* loaded from: classes2.dex */
    public class ApkCalculation extends Thread {
        public ApkCalculation() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            SpaceManagerActivity spaceManagerActivity = SpaceManagerActivity.this;
            final MediaList mediaList = new MediaList(spaceManagerActivity, SpaceManagerActivity.this.title + " " + SpaceManagerActivity.this.getString(R.string.mbc_apk_files), SocialAnimationActivity.FileTypes.APK);
            mediaList.fetchAPK(0L, SpaceManagerActivity.this.hmExtnApk);
            if (SpaceManagerActivity.this.socialMedia == null) {
                SpaceManagerActivity spaceManagerActivity2 = SpaceManagerActivity.this;
                spaceManagerActivity2.title = "" + SpaceManagerActivity.this.getString(R.string.big);
                SpaceManagerActivity spaceManagerActivity3 = SpaceManagerActivity.this;
                spaceManagerActivity3.socialMedia = new SocialMedia(spaceManagerActivity3.title);
            }
            if (MobiClean.getInstance().spaceManagerModule == null) {
                MobiClean.getInstance().spaceManagerModule = new SocialmediaModule();
            }
            SpaceManagerActivity.this.socialMedia.arrContents.add(mediaList);
            SpaceManagerActivity.this.socialMedia.updateHashMapFileTypeToMediaList();
            MobiClean.getInstance().spaceManagerModule.arrContents.clear();
            MobiClean.getInstance().spaceManagerModule.arrContents.add(SpaceManagerActivity.this.socialMedia);
            MobiClean.getInstance().spaceManagerModule.updateSelf();
            SpaceManagerActivity.this.runOnUiThread(new Runnable() { // from class: com.mobiclean.phoneclean.tools.SpaceManagerActivity.ApkCalculation.1
                @Override // java.lang.Runnable
                public void run() {
                    SpaceManagerActivity.this.findViewById(R.id.tv_apk_size).setVisibility(View.VISIBLE);
                    SpaceManagerActivity.this.findViewById(R.id.tv_apk_count).setVisibility(View.VISIBLE);
                    SpaceManagerActivity.this.findViewById(R.id.pbar_apk_count).setVisibility(View.GONE);
                    ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_apk_size)).setText("" + Util.convertBytes(mediaList.totalSize));
                    ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_apk_count)).setText("" + mediaList.arrContents.size() + " " + SpaceManagerActivity.this.getString(R.string.items));
                    SpaceManagerActivity spaceManagerActivity4 = SpaceManagerActivity.this;
                    spaceManagerActivity4.completedTask = spaceManagerActivity4.completedTask + 1;
                    SpaceManagerActivity.this.l.addAll(mediaList.arrContents);
                    if (SpaceManagerActivity.this.completedTask == 6) {
                        SpaceManagerActivity.this.findViewById(R.id.tv_all_files_size).setVisibility(View.VISIBLE);
                        SpaceManagerActivity.this.findViewById(R.id.tv_all_files_count).setVisibility(View.VISIBLE);
                        SpaceManagerActivity.this.findViewById(R.id.pbar_all_count).setVisibility(View.GONE);
                        SpaceManagerActivity.this.updateTotalRecoverable();
                        SpaceManagerActivity spaceManagerActivity5 = SpaceManagerActivity.this;
                        MediaList mediaList2 = new MediaList(spaceManagerActivity5, SpaceManagerActivity.this.title + " " + SpaceManagerActivity.this.getString(R.string.all_files), SocialAnimationActivity.FileTypes.ALL);
                        mediaList2.fetchAllFiles(SpaceManagerActivity.this.l);
                        SpaceManagerActivity.this.socialMedia.arrContents.add(mediaList2);
                        ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_all_files_size)).setText("" + Util.convertBytes(mediaList2.totalSize));
                        ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_all_files_count)).setText("" + mediaList2.arrContents.size() + " " + SpaceManagerActivity.this.getString(R.string.items));
                        SpaceManagerActivity.this.socialMedia.updateHashMapFileTypeToMediaList();
                        MobiClean.getInstance().spaceManagerModule.arrContents.clear();
                        MobiClean.getInstance().spaceManagerModule.arrContents.add(SpaceManagerActivity.this.socialMedia);
                        MobiClean.getInstance().spaceManagerModule.updateSelf();
                        SpaceManagerActivity spaceManagerActivity6 = SpaceManagerActivity.this;
                        spaceManagerActivity6.suggestionListAdapter = new SuggestionListAdapter(spaceManagerActivity6.m, mediaList2);
                        SpaceManagerActivity.this.rv_suggestion.setLayoutManager(new LinearLayoutManager(SpaceManagerActivity.this.context));
                        SpaceManagerActivity.this.rv_suggestion.setAdapter(SpaceManagerActivity.this.suggestionListAdapter);
                        SpaceManagerActivity.this.invalidateOptionsMenu();
                    }
                }
            });
        }
    }

    /* loaded from: classes2.dex */
    public class AudioCalculation extends Thread {
        public AudioCalculation() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            SpaceManagerActivity spaceManagerActivity = SpaceManagerActivity.this;
            final MediaList mediaList = new MediaList(spaceManagerActivity, SpaceManagerActivity.this.title + " " + SpaceManagerActivity.this.getString(R.string.mbc_myaudios), SocialAnimationActivity.FileTypes.Audio);
            if (SpaceManagerActivity.this.socialMedia.arrContents == null) {
                return;
            }
            try {
                mediaList.fetchAudio();
                SpaceManagerActivity.this.socialMedia.arrContents.add(mediaList);
                SpaceManagerActivity.this.socialMedia.updateHashMapFileTypeToMediaList();
                MobiClean.getInstance().spaceManagerModule.arrContents.clear();
                MobiClean.getInstance().spaceManagerModule.arrContents.add(SpaceManagerActivity.this.socialMedia);
                MobiClean.getInstance().spaceManagerModule.updateSelf();
            } catch (Exception unused) {
            }
            SpaceManagerActivity.this.runOnUiThread(new Runnable() {
                @Override // java.lang.Runnable
                public void run() {
                    SpaceManagerActivity.this.findViewById(R.id.tv_audios_size).setVisibility(View.VISIBLE);
                    SpaceManagerActivity.this.findViewById(R.id.tv_audios_count).setVisibility(View.VISIBLE);
                    SpaceManagerActivity.this.findViewById(R.id.pbar_audio_count).setVisibility(View.GONE);
                    ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_audios_size)).setText("" + Util.convertBytes(mediaList.totalSize));
                    ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_audios_count)).setText("" + mediaList.arrContents.size() + " " + SpaceManagerActivity.this.getString(R.string.items));
                    SpaceManagerActivity spaceManagerActivity2 = SpaceManagerActivity.this;
                    spaceManagerActivity2.completedTask = spaceManagerActivity2.completedTask + 1;
                    SpaceManagerActivity.this.l.addAll(mediaList.arrContents);
                    if (SpaceManagerActivity.this.completedTask == 6) {
                        SpaceManagerActivity.this.findViewById(R.id.tv_all_files_size).setVisibility(View.VISIBLE);
                        SpaceManagerActivity.this.findViewById(R.id.tv_all_files_count).setVisibility(View.VISIBLE);
                        SpaceManagerActivity.this.findViewById(R.id.pbar_all_count).setVisibility(View.GONE);
                        SpaceManagerActivity.this.updateTotalRecoverable();
                        SpaceManagerActivity spaceManagerActivity3 = SpaceManagerActivity.this;
                        MediaList mediaList2 = new MediaList(spaceManagerActivity3, SpaceManagerActivity.this.title + " " + SpaceManagerActivity.this.getString(R.string.mbc_myaudios), SocialAnimationActivity.FileTypes.ALL);
                        mediaList2.fetchAllFiles(SpaceManagerActivity.this.l);
                        SpaceManagerActivity.this.socialMedia.arrContents.add(mediaList2);
                        ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_all_files_size)).setText("" + Util.convertBytes(mediaList2.totalSize));
                        ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_all_files_count)).setText("" + mediaList2.arrContents.size() + " " + SpaceManagerActivity.this.getString(R.string.items));
                        SpaceManagerActivity.this.socialMedia.updateHashMapFileTypeToMediaList();
                        MobiClean.getInstance().spaceManagerModule.arrContents.clear();
                        MobiClean.getInstance().spaceManagerModule.arrContents.add(SpaceManagerActivity.this.socialMedia);
                        MobiClean.getInstance().spaceManagerModule.updateSelf();
                        SpaceManagerActivity spaceManagerActivity4 = SpaceManagerActivity.this;
                        spaceManagerActivity4.suggestionListAdapter = new SuggestionListAdapter(spaceManagerActivity4.l, mediaList2);
                        SpaceManagerActivity.this.rv_suggestion.setLayoutManager(new LinearLayoutManager(SpaceManagerActivity.this.context));
                        SpaceManagerActivity.this.rv_suggestion.setAdapter(SpaceManagerActivity.this.suggestionListAdapter);
                        SpaceManagerActivity.this.invalidateOptionsMenu();
                    }
                }
            });
        }
    }

    public class DocCalculation extends Thread {
        public DocCalculation() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            SpaceManagerActivity spaceManagerActivity = SpaceManagerActivity.this;
            final MediaList mediaList = new MediaList(spaceManagerActivity, SpaceManagerActivity.this.title + " " + SpaceManagerActivity.this.getString(R.string.mbc_viewmore_document), SocialAnimationActivity.FileTypes.Document);
            mediaList.fetchFilesForExtns(0L, SpaceManagerActivity.this.hmExtnDocs);
            SpaceManagerActivity.this.socialMedia.arrContents.add(mediaList);
            SpaceManagerActivity.this.socialMedia.updateHashMapFileTypeToMediaList();
            try {
                MobiClean.getInstance().spaceManagerModule.arrContents.clear();
                MobiClean.getInstance().spaceManagerModule.arrContents.add(SpaceManagerActivity.this.socialMedia);
                MobiClean.getInstance().spaceManagerModule.updateSelf();
            } catch (Exception e) {
                Log.e("CRASH_EXCEPTION", e.toString());
            }
            SpaceManagerActivity.this.runOnUiThread(new Runnable() { // from class: com.mobiclean.phoneclean.tools.SpaceManagerActivity.DocCalculation.1
                @Override // java.lang.Runnable
                public void run() {
                    SpaceManagerActivity.this.findViewById(R.id.tv_documents_size).setVisibility(View.VISIBLE);
                    SpaceManagerActivity.this.findViewById(R.id.tv_documents_count).setVisibility(View.VISIBLE);
                    SpaceManagerActivity.this.findViewById(R.id.pbar_docs_count).setVisibility(View.GONE);
                    ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_documents_size)).setText("" + Util.convertBytes(mediaList.totalSize));
                    ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_documents_count)).setText("" + mediaList.arrContents.size() + " " + SpaceManagerActivity.this.getString(R.string.items));
                    SpaceManagerActivity spaceManagerActivity2 = SpaceManagerActivity.this;
                    spaceManagerActivity2.completedTask = spaceManagerActivity2.completedTask + 1;
                    SpaceManagerActivity.this.l.addAll(mediaList.arrContents);
                    if (SpaceManagerActivity.this.completedTask == 6) {
                        SpaceManagerActivity.this.findViewById(R.id.tv_all_files_size).setVisibility(View.VISIBLE);
                        SpaceManagerActivity.this.findViewById(R.id.tv_all_files_count).setVisibility(View.VISIBLE);
                        SpaceManagerActivity.this.findViewById(R.id.pbar_all_count).setVisibility(View.GONE);
                        SpaceManagerActivity.this.updateTotalRecoverable();
                        SpaceManagerActivity spaceManagerActivity3 = SpaceManagerActivity.this;
                        MediaList mediaList2 = new MediaList(spaceManagerActivity3, SpaceManagerActivity.this.title + " " + SpaceManagerActivity.this.getString(R.string.mbc_viewmore_document), SocialAnimationActivity.FileTypes.ALL);
                        mediaList2.fetchAllFiles(SpaceManagerActivity.this.l);
                        SpaceManagerActivity.this.socialMedia.arrContents.add(mediaList2);
                        ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_all_files_size)).setText("" + Util.convertBytes(mediaList2.totalSize));
                        ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_all_files_count)).setText("" + mediaList2.arrContents.size() + " " + SpaceManagerActivity.this.getString(R.string.items));
                        SpaceManagerActivity.this.socialMedia.updateHashMapFileTypeToMediaList();
                        try {
                            MobiClean.getInstance().spaceManagerModule.arrContents.clear();
                            MobiClean.getInstance().spaceManagerModule.arrContents.add(SpaceManagerActivity.this.socialMedia);
                            MobiClean.getInstance().spaceManagerModule.updateSelf();
                        } catch (Exception e2) {
                            Log.e("CRASH_EXCEPTION", e2.toString());
                        }
                        SpaceManagerActivity spaceManagerActivity4 = SpaceManagerActivity.this;
                        spaceManagerActivity4.suggestionListAdapter = new SuggestionListAdapter(spaceManagerActivity4.l, mediaList2);
                        SpaceManagerActivity.this.rv_suggestion.setLayoutManager(new LinearLayoutManager(SpaceManagerActivity.this.context));
                        SpaceManagerActivity.this.rv_suggestion.setAdapter(SpaceManagerActivity.this.suggestionListAdapter);
                        SpaceManagerActivity.this.invalidateOptionsMenu();
                    }
                }
            });
        }
    }

    /* loaded from: classes2.dex */
    public class DownloadsCalculation extends Thread {

        /* renamed from: com.mobiclean.phoneclean.tools.SpaceManagerActivity$DownloadsCalculation$1  reason: invalid class name */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 implements DownloadDataFetchedListener {
            public AnonymousClass1() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* renamed from: a */
            public /* synthetic */ void b(int i, long j) {
                SpaceManagerActivity.this.tvdownCount.setVisibility(View.VISIBLE);
                SpaceManagerActivity.this.tvdownSize.setVisibility(View.VISIBLE);
                SpaceManagerActivity.this.findViewById(R.id.pbar_downloads_count).setVisibility(View.GONE);
                TextView textView = SpaceManagerActivity.this.tvdownCount;
                textView.setText(i + " " + SpaceManagerActivity.this.getString(R.string.items));
                SpaceManagerActivity.this.tvdownSize.setText(Util.convertBytes(j));
            }

            @Override // com.mobiclean.phoneclean.DownloadDataFetchedListener
            public void onFetched(final int i, final long j) {
                SpaceManagerActivity.this.runOnUiThread(new Runnable() { // from class: d.c.a.a.m.c
                    @Override // java.lang.Runnable
                    public final void run() {
                        AnonymousClass1.this.b(i, j);
                    }
                });
            }
        }

        public DownloadsCalculation() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            MobiClean.getInstance().downloadsData = new DownloadsData(SpaceManagerActivity.this);
            MobiClean.getInstance().downloadsData.getDownloadedFiles(SpaceManagerActivity.this, new AnonymousClass1());
        }
    }

    /* loaded from: classes2.dex */
    public class ImageCalculation extends Thread {
        public ImageCalculation() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            SpaceManagerActivity spaceManagerActivity = SpaceManagerActivity.this;
            final MediaList mediaList = new MediaList(spaceManagerActivity, SpaceManagerActivity.this.title + " " + SpaceManagerActivity.this.getString(R.string.mbc_viewmore_image), SocialAnimationActivity.FileTypes.Image);
            if (SpaceManagerActivity.this.socialMedia.arrContents == null) {
                return;
            }
            try {
                mediaList.fetchImages();
                SpaceManagerActivity.this.socialMedia.arrContents.add(mediaList);
                SpaceManagerActivity.this.socialMedia.updateHashMapFileTypeToMediaList();
                MobiClean.getInstance().spaceManagerModule.arrContents.clear();
                MobiClean.getInstance().spaceManagerModule.arrContents.add(SpaceManagerActivity.this.socialMedia);
                MobiClean.getInstance().spaceManagerModule.updateSelf();
            } catch (Exception unused) {
            }
            SpaceManagerActivity.this.runOnUiThread(new Runnable() { // from class: com.mobiclean.phoneclean.tools.SpaceManagerActivity.ImageCalculation.1
                @Override // java.lang.Runnable
                public void run() {
                    SpaceManagerActivity.this.findViewById(R.id.tv_images_size).setVisibility(View.VISIBLE);
                    SpaceManagerActivity.this.findViewById(R.id.tv_images_count).setVisibility(View.VISIBLE);
                    SpaceManagerActivity.this.findViewById(R.id.pbar_photos_count).setVisibility(View.GONE);
                    ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_images_size)).setText("" + Util.convertBytes(mediaList.totalSize));
                    ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_images_count)).setText("" + mediaList.arrContents.size() + " " + SpaceManagerActivity.this.getString(R.string.items));
                    SpaceManagerActivity spaceManagerActivity2 = SpaceManagerActivity.this;
                    spaceManagerActivity2.completedTask = spaceManagerActivity2.completedTask + 1;
                    SpaceManagerActivity.this.l.addAll(mediaList.arrContents);
                    if (SpaceManagerActivity.this.completedTask == 6) {
                        SpaceManagerActivity.this.findViewById(R.id.tv_all_files_size).setVisibility(View.VISIBLE);
                        SpaceManagerActivity.this.findViewById(R.id.tv_all_files_count).setVisibility(View.VISIBLE);
                        SpaceManagerActivity.this.findViewById(R.id.pbar_all_count).setVisibility(View.GONE);
                        SpaceManagerActivity.this.updateTotalRecoverable();
                        SpaceManagerActivity spaceManagerActivity3 = SpaceManagerActivity.this;
                        MediaList mediaList2 = new MediaList(spaceManagerActivity3, SpaceManagerActivity.this.title + " " + SpaceManagerActivity.this.getString(R.string.all_files), SocialAnimationActivity.FileTypes.ALL);
                        mediaList2.fetchAllFiles(SpaceManagerActivity.this.l);
                        SpaceManagerActivity.this.socialMedia.arrContents.add(mediaList2);
                        ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_all_files_size)).setText("" + Util.convertBytes(mediaList2.totalSize));
                        ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_all_files_count)).setText("" + mediaList2.arrContents.size() + " " + SpaceManagerActivity.this.getString(R.string.items));
                        SpaceManagerActivity.this.socialMedia.updateHashMapFileTypeToMediaList();
                        MobiClean.getInstance().spaceManagerModule.arrContents.clear();
                        MobiClean.getInstance().spaceManagerModule.arrContents.add(SpaceManagerActivity.this.socialMedia);
                        MobiClean.getInstance().spaceManagerModule.updateSelf();
                        SpaceManagerActivity spaceManagerActivity4 = SpaceManagerActivity.this;
                        spaceManagerActivity4.suggestionListAdapter = new SuggestionListAdapter(spaceManagerActivity4.l, mediaList2);
                        SpaceManagerActivity.this.rv_suggestion.setLayoutManager(new LinearLayoutManager(SpaceManagerActivity.this.context));
                        SpaceManagerActivity.this.rv_suggestion.setAdapter(SpaceManagerActivity.this.suggestionListAdapter);
                        SpaceManagerActivity.this.invalidateOptionsMenu();
                    }
                }
            });
        }
    }

    /* loaded from: classes2.dex */
    public class OtherCalculation extends Thread {
        public OtherCalculation() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            SpaceManagerActivity spaceManagerActivity = SpaceManagerActivity.this;
            final MediaList mediaList = new MediaList(spaceManagerActivity, SpaceManagerActivity.this.title + " " + SpaceManagerActivity.this.getString(R.string.mbc_myothers), SocialAnimationActivity.FileTypes.Others);
            mediaList.fetchFilesForNotInExtns(0L, SpaceManagerActivity.this.hmExtnsOtherMediaAndDocs);
            SpaceManagerActivity.this.socialMedia.arrContents.add(mediaList);
            SpaceManagerActivity.this.socialMedia.updateHashMapFileTypeToMediaList();
            MobiClean.getInstance().spaceManagerModule.arrContents.clear();
            MobiClean.getInstance().spaceManagerModule.arrContents.add(SpaceManagerActivity.this.socialMedia);
            MobiClean.getInstance().spaceManagerModule.updateSelf();
            SpaceManagerActivity.this.runOnUiThread(new Runnable() {
                @Override // java.lang.Runnable
                public void run() {
                    SpaceManagerActivity.this.findViewById(R.id.tv_others_count).setVisibility(View.VISIBLE);
                    SpaceManagerActivity.this.findViewById(R.id.tv_others_size).setVisibility(View.VISIBLE);
                    SpaceManagerActivity.this.findViewById(R.id.pbar_others_count).setVisibility(View.GONE);
                    long size = mediaList.totalSize;

                    ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_others_size)).setText("" + Util.convertBytes(mediaList.totalSize));
                    ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_others_count)).setText("" + mediaList.arrContents.size() + " " + SpaceManagerActivity.this.getString(R.string.items));
                    SpaceManagerActivity spaceManagerActivity2 = SpaceManagerActivity.this;
                    spaceManagerActivity2.completedTask = spaceManagerActivity2.completedTask + 1;
                    SpaceManagerActivity.this.l.addAll(mediaList.arrContents);
                    if (SpaceManagerActivity.this.completedTask == 6) {
                        SpaceManagerActivity.this.findViewById(R.id.tv_all_files_size).setVisibility(View.VISIBLE);
                        SpaceManagerActivity.this.findViewById(R.id.tv_all_files_count).setVisibility(View.VISIBLE);
                        SpaceManagerActivity.this.findViewById(R.id.pbar_all_count).setVisibility(View.GONE);
                        SpaceManagerActivity.this.updateTotalRecoverable();
                        SpaceManagerActivity spaceManagerActivity3 = SpaceManagerActivity.this;
                        MediaList mediaList2 = new MediaList(spaceManagerActivity3, SpaceManagerActivity.this.title + " " + SpaceManagerActivity.this.getString(R.string.all_files), SocialAnimationActivity.FileTypes.ALL);
                        mediaList2.fetchAllFiles(SpaceManagerActivity.this.l);
                        SpaceManagerActivity.this.socialMedia.arrContents.add(mediaList2);
                        ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_all_files_size)).setText("" + Util.convertBytes(mediaList2.totalSize));
                        ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_all_files_count)).setText("" + mediaList2.arrContents.size() + " " + SpaceManagerActivity.this.getString(R.string.items));
                        SpaceManagerActivity.this.socialMedia.updateHashMapFileTypeToMediaList();
                        MobiClean.getInstance().spaceManagerModule.arrContents.clear();
                        MobiClean.getInstance().spaceManagerModule.arrContents.add(SpaceManagerActivity.this.socialMedia);
                        MobiClean.getInstance().spaceManagerModule.updateSelf();
                        SpaceManagerActivity spaceManagerActivity4 = SpaceManagerActivity.this;
                        spaceManagerActivity4.suggestionListAdapter = new SuggestionListAdapter(spaceManagerActivity4.l, mediaList2);
                        SpaceManagerActivity.this.rv_suggestion.setLayoutManager(new LinearLayoutManager(SpaceManagerActivity.this.context));
                        SpaceManagerActivity.this.rv_suggestion.setAdapter(SpaceManagerActivity.this.suggestionListAdapter);
                        SpaceManagerActivity.this.invalidateOptionsMenu();
                    }
                }
            });
        }
    }

    /* loaded from: classes2.dex */
    public class SuggestionListAdapter extends RecyclerView.Adapter<SuggestionListAdapter.MyViewHolder> implements Filterable {

        /* renamed from: a  reason: collision with root package name */
        public ArrayList<BigSizeFilesWrapper> f5261a;
        public ArrayList<BigSizeFilesWrapper> b;

        /* renamed from: c  reason: collision with root package name */
        public ValueFilter f5262c;

        /* renamed from: d  reason: collision with root package name */
        public MediaList f5263d;

        /* loaded from: classes2.dex */
        public class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView iv_suggestion_list_icon;
            private TextView tv_suggestion_list_date;
            private TextView tv_suggestion_list_name;
            private TextView tv_suggestion_list_size;

            public MyViewHolder(@NonNull SuggestionListAdapter suggestionListAdapter, View view) {
                super(view);
                this.tv_suggestion_list_name = (TextView) view.findViewById(R.id.tv_suggestion_list_name);
                this.tv_suggestion_list_size = (TextView) view.findViewById(R.id.tv_suggestion_list_size);
                this.iv_suggestion_list_icon = (ImageView) view.findViewById(R.id.iv_suggestion_list_icon);
                this.tv_suggestion_list_date = (TextView) view.findViewById(R.id.tv_suggestion_list_date);
            }
        }

        /* loaded from: classes2.dex */
        public class ValueFilter extends Filter {
            private ValueFilter() {
            }

            @Override // android.widget.Filter
            public FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence != null && charSequence.length() > 0) {
                    ArrayList arrayList = new ArrayList();
                    Iterator<BigSizeFilesWrapper> it = SuggestionListAdapter.this.f5261a.iterator();
                    while (it.hasNext()) {
                        BigSizeFilesWrapper next = it.next();
                        if (next.name.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            arrayList.add(next);
                        }
                    }
                    filterResults.count = arrayList.size();
                    filterResults.values = arrayList;
                    String str = SpaceManagerActivity.this.TAG;
                    Log.e(str, "--------------filterResults.count------------" + filterResults.count);
                } else {
                    filterResults.count = SuggestionListAdapter.this.f5261a.size();
                    filterResults.values = SuggestionListAdapter.this.f5261a;
                }
                return filterResults;
            }

            @Override // android.widget.Filter
            public void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.count == 0) {
                    SpaceManagerActivity.this.tv_no_result_found.setVisibility(View.VISIBLE);
                } else {
                    SpaceManagerActivity.this.tv_no_result_found.setVisibility(View.GONE);
                }
                SuggestionListAdapter suggestionListAdapter = SuggestionListAdapter.this;
                suggestionListAdapter.b = (ArrayList) filterResults.values;
                SpaceManagerActivity.this.suggestionListAdapter.notifyDataSetChanged();
            }
        }

        public SuggestionListAdapter(ArrayList<BigSizeFilesWrapper> arrayList, MediaList mediaList) {
            this.f5263d = mediaList;
            this.f5261a = arrayList;
            this.b = arrayList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: a */
        public /* synthetic */ void b(BigSizeFilesWrapper bigSizeFilesWrapper, View view) {
            try {
                SpaceManagerActivity spaceManagerActivity = SpaceManagerActivity.this;
                spaceManagerActivity.openFile(spaceManagerActivity, new File(bigSizeFilesWrapper.path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // android.widget.Filterable
        public Filter getFilter() {
            if (this.f5262c == null) {
                this.f5262c = new ValueFilter();
            }
            return this.f5262c;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.b.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            final BigSizeFilesWrapper bigSizeFilesWrapper = this.b.get(i);
            myViewHolder.tv_suggestion_list_name.setText(bigSizeFilesWrapper.name);
            TextView textView = myViewHolder.tv_suggestion_list_size;
            textView.setText("" + Util.convertBytes(bigSizeFilesWrapper.size));
            myViewHolder.tv_suggestion_list_date.setText(new SimpleDateFormat("MMM dd,yyyy, hh:mm aaa").format(new Date(bigSizeFilesWrapper.dateTaken)));
            SocialAnimationActivity.FileTypes fileTypes = this.f5263d.mediaType;
            if (fileTypes == SocialAnimationActivity.FileTypes.Audio) {
                myViewHolder.iv_suggestion_list_icon.setImageBitmap(BitmapFactory.decodeResource(SpaceManagerActivity.this.getResources(), R.drawable.toolbox_audio_icon));
            } else if (fileTypes == SocialAnimationActivity.FileTypes.Others) {
                myViewHolder.iv_suggestion_list_icon.setImageBitmap(BitmapFactory.decodeResource(SpaceManagerActivity.this.getResources(), R.drawable.toolbox_others_icon));
            } else if (fileTypes == SocialAnimationActivity.FileTypes.Document) {
                myViewHolder.iv_suggestion_list_icon.setImageBitmap(BitmapFactory.decodeResource(SpaceManagerActivity.this.getResources(), R.drawable.toolbox_documents_icon));
            } else if (fileTypes == SocialAnimationActivity.FileTypes.APK) {
                myViewHolder.iv_suggestion_list_icon.setImageBitmap(BitmapFactory.decodeResource(SpaceManagerActivity.this.getResources(), R.drawable.apk_files));
            } else {
                Bitmap bitmapFromExt = SpaceManagerActivity.this.getBitmapFromExt(FilenameUtils.getExtension(bigSizeFilesWrapper.path));
                if (bitmapFromExt == null) {
                    Glide.with((FragmentActivity) SpaceManagerActivity.this).load(new File(bigSizeFilesWrapper.path)).into(myViewHolder.iv_suggestion_list_icon);
                } else {
                    myViewHolder.iv_suggestion_list_icon.setImageBitmap(bitmapFromExt);
                }
            }
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: d.c.a.a.m.d
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SuggestionListAdapter.this.b(bigSizeFilesWrapper, view);
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        @NonNull
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MyViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.file_suggestion_list, viewGroup, false));
        }
    }

    /* loaded from: classes2.dex */
    public class VideoCalculation extends Thread {
        public VideoCalculation() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            SpaceManagerActivity spaceManagerActivity = SpaceManagerActivity.this;
            final MediaList mediaList = new MediaList(spaceManagerActivity, SpaceManagerActivity.this.title + " " + SpaceManagerActivity.this.getString(R.string.mbc_viewmore_video), SocialAnimationActivity.FileTypes.Video);
            if (SpaceManagerActivity.this.socialMedia.arrContents == null) {
                return;
            }
            try {
                mediaList.fetchVideos();
                SpaceManagerActivity.this.socialMedia.arrContents.add(mediaList);
                SpaceManagerActivity.this.socialMedia.updateHashMapFileTypeToMediaList();
                MobiClean.getInstance().spaceManagerModule.arrContents.clear();
                MobiClean.getInstance().spaceManagerModule.arrContents.add(SpaceManagerActivity.this.socialMedia);
                MobiClean.getInstance().spaceManagerModule.updateSelf();
            } catch (Exception unused) {
            }
            SpaceManagerActivity.this.runOnUiThread(new Runnable() { // from class: com.mobiclean.phoneclean.tools.SpaceManagerActivity.VideoCalculation.1
                @Override // java.lang.Runnable
                public void run() {
                    SpaceManagerActivity.this.findViewById(R.id.tv_videos_size).setVisibility(View.VISIBLE);
                    SpaceManagerActivity.this.findViewById(R.id.tv_videos_count).setVisibility(View.VISIBLE);
                    SpaceManagerActivity.this.findViewById(R.id.pbar_videos_count).setVisibility(View.GONE);
                    ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_videos_size)).setText("" + Util.convertBytes(mediaList.totalSize));
                    ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_videos_count)).setText("" + mediaList.arrContents.size() + " " + SpaceManagerActivity.this.getString(R.string.items));
                    SpaceManagerActivity spaceManagerActivity2 = SpaceManagerActivity.this;
                    spaceManagerActivity2.completedTask = spaceManagerActivity2.completedTask + 1;
                    SpaceManagerActivity.this.l.addAll(mediaList.arrContents);
                    if (SpaceManagerActivity.this.completedTask == 6) {
                        SpaceManagerActivity.this.findViewById(R.id.tv_all_files_size).setVisibility(View.VISIBLE);
                        SpaceManagerActivity.this.findViewById(R.id.tv_all_files_count).setVisibility(View.VISIBLE);
                        SpaceManagerActivity.this.findViewById(R.id.pbar_all_count).setVisibility(View.GONE);
                        SpaceManagerActivity.this.updateTotalRecoverable();
                        SpaceManagerActivity spaceManagerActivity3 = SpaceManagerActivity.this;
                        MediaList mediaList2 = new MediaList(spaceManagerActivity3, SpaceManagerActivity.this.title + " " + SpaceManagerActivity.this.getString(R.string.all_files), SocialAnimationActivity.FileTypes.ALL);
                        mediaList2.fetchAllFiles(SpaceManagerActivity.this.l);
                        SpaceManagerActivity.this.socialMedia.arrContents.add(mediaList2);
                        ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_all_files_size)).setText("" + Util.convertBytes(mediaList2.totalSize));
                        ((TextView) SpaceManagerActivity.this.findViewById(R.id.tv_all_files_count)).setText("" + mediaList2.arrContents.size() + " " + SpaceManagerActivity.this.getString(R.string.items));
                        SpaceManagerActivity.this.socialMedia.updateHashMapFileTypeToMediaList();
                        MobiClean.getInstance().spaceManagerModule.arrContents.clear();
                        MobiClean.getInstance().spaceManagerModule.arrContents.add(SpaceManagerActivity.this.socialMedia);
                        MobiClean.getInstance().spaceManagerModule.updateSelf();
                        SpaceManagerActivity spaceManagerActivity4 = SpaceManagerActivity.this;
                        spaceManagerActivity4.suggestionListAdapter = new SuggestionListAdapter(spaceManagerActivity4.l, mediaList2);
                        SpaceManagerActivity.this.rv_suggestion.setLayoutManager(new LinearLayoutManager(SpaceManagerActivity.this.context));
                        SpaceManagerActivity.this.rv_suggestion.setAdapter(SpaceManagerActivity.this.suggestionListAdapter);
                        SpaceManagerActivity.this.invalidateOptionsMenu();
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callDuplicates() {
        ProgressDialog progressDialog = this.dialogStopWait;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.dialogStopWait.dismiss();
        }
        Log.i("CLICKED", "-----");
        this.proceeded = true;
        if (MobiClean.getInstance().duplicatesData == null) {
            if (MobiClean.getInstance().duplicatesData != null) {
                MobiClean.getInstance().duplicatesData.clearAll();
            }
            Log.i("CLICKED", "-----1111");
            Util.appendLogmobiclean(this.TAG, "Duplicate NOT avilable Going to DuplicacyMidSettings class >>>>>>>>>", GlobalData.FILE_NAME);


                startActivity(new Intent(SpaceManagerActivity.this, DuplicacyMidSettings.class));
                context = null;

        } else if (MobiClean.getInstance().duplicatesData.showImageList != null && MobiClean.getInstance().duplicatesData.showImageList.size() >= 1) {
            Util.appendLogmobiclean(this.TAG, "Duplicate avilable show duplicate on screen >>>>>>>>>", GlobalData.FILE_NAME);
            Log.i("CLICKED", "-----22222");
            GlobalData.duplicacyLevel = 25;
            GlobalData.duplicacyTime = 600;
            GlobalData.duplicacyDist = 20;
            long j = 0;
            for (int i = 0; i < MobiClean.getInstance().duplicatesData.showImageList.size(); i++) {
                j += MobiClean.getInstance().duplicatesData.showImageList.get(i).size;
            }
            findViewById(R.id.pbar_dup_count).setVisibility(View.GONE);
            TextView textView = this.tvdupSize;
            textView.setText("" + Util.convertBytes(j));
            TextView textView2 = this.tvdupCount;
            textView2.setText("" + MobiClean.getInstance().duplicatesData.showImageList.size() + " " + getString(R.string.items));
            MobiClean.getInstance().duplicatesData.alreadyScanned = true;
      startActivity(new Intent(SpaceManagerActivity.this, DuplicatesActivity.class));
                context = null;

        } else {
            if (MobiClean.getInstance().duplicatesData != null) {
                MobiClean.getInstance().duplicatesData.clearAll();
            }
            Log.i("CLICKED", "-----3333");
            Util.appendLogmobiclean(this.TAG, "Duplicate NOT avilable Going to DuplicacyMidSettings class >>>>>>>>>", GlobalData.FILE_NAME);
                startActivity(new Intent(SpaceManagerActivity.this, DuplicacyMidSettings.class));
                context = null;


        }
    }

    private void clearNotification() {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.cancel(800);
            }
        } catch (Exception e) {
            String str = this.TAG;
            Util.appendLogmobiclean(str, "method clearNotification()  !!!!!!!!!!!!" + e.getMessage(), GlobalData.FILE_NAME);
            e.printStackTrace();
        }
    }


    private void createGroups() {
        Log.d("SSSSSSSS", "called");
        if (MobiClean.getInstance().duplicatesData == null || DuplicacyRefreshAsyncTask.stopExecuting) {
            return;
        }
        new AsyncTask<String, String, String>() { // from class: com.mobiclean.phoneclean.tools.SpaceManagerActivity.7
            private boolean check(ArrayList<ImageDetail> arrayList, ImageDetail imageDetail) {
                for (int i = 0; i < arrayList.size(); i++) {
                    String str = arrayList.get(i).path;
                    if (str.equalsIgnoreCase("" + imageDetail.path)) {
                        return false;
                    }
                }
                return true;
            }

            private void checkInGroups(ImageDetail imageDetail) {
                Util.appendLogmobiclean(SpaceManagerActivity.this.TAG, "method checkInGroups()********", GlobalData.FILE_NAME);
                boolean z = false;
                int i = 0;
                while (true) {
                    if (i >= MobiClean.getInstance().duplicatesData.grouplist.size()) {
                        break;
                    } else if (SpaceManagerActivity.this.proceeded) {
                        return;
                    } else {
                        double calculateHamingScore = DuplicacyUtil.calculateHamingScore(MobiClean.getInstance().duplicatesData.grouplist.get(i).children.get(0), imageDetail, SpaceManagerActivity.this.distance, SpaceManagerActivity.this.time);
                        if (SpaceManagerActivity.this.pass_score == 1.0f) {
                            SpaceManagerActivity.this.pass_score = 0.997f;
                        }
                        if (calculateHamingScore >= SpaceManagerActivity.this.pass_score) {
                            if (check(MobiClean.getInstance().duplicatesData.grouplist.get(i).children, imageDetail)) {
                                MobiClean.getInstance().duplicatesData.grouplist.get(i).children.add(imageDetail);
                            }
                            z = true;
                        } else {
                            i++;
                        }
                    }
                }
                if (z) {
                    return;
                }
                DuplicateGroup duplicateGroup = new DuplicateGroup();
                ArrayList<ImageDetail> arrayList = new ArrayList<>();
                duplicateGroup.children = arrayList;
                arrayList.add(imageDetail);
                MobiClean.getInstance().duplicatesData.grouplist.add(duplicateGroup);
            }

            @Override // android.os.AsyncTask
            public void onPreExecute() {
                Log.d("SSSSSSSS", "group");
                SpaceManagerActivity.this.pass_score = (float) (((GlobalData.duplicacyLevel * 10.0f) / 10000.0f) + 0.9d);
                SpaceManagerActivity.this.distance = GlobalData.duplicacyDist;
                SpaceManagerActivity.this.time = GlobalData.duplicacyTime;
                super.onPreExecute();
            }

            @Override // android.os.AsyncTask
            public String doInBackground(String... strArr) {
                Log.d("sssss", "11111111111111111111 proce  " + SpaceManagerActivity.this.proceeded);
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (SpaceManagerActivity.this.proceeded) {
                    return null;
                }
                int i = 0;
                while (i < MobiClean.getInstance().duplicatesData.imageList.size()) {
                    boolean z = true;
                    if (DuplicacyRefreshAsyncTask.stopExecuting) {
                        i = MobiClean.getInstance().duplicatesData.imageList.size() - 1;
                    }
                    if (SpaceManagerActivity.this.proceeded) {
                        return null;
                    }
                    checkInGroups(MobiClean.getInstance().duplicatesData.imageList.get(i));
                    ImageDetail imageDetail = MobiClean.getInstance().duplicatesData.imageList.get(i);
                    int i2 = 0;
                    while (true) {
                        if (i2 >= MobiClean.getInstance().duplicatesData.grouplist.size()) {
                            z = false;
                            break;
                        }
                        double calculateHamingScore = DuplicacyUtil.calculateHamingScore(MobiClean.getInstance().duplicatesData.grouplist.get(i2).children.get(0), imageDetail, SpaceManagerActivity.this.distance, SpaceManagerActivity.this.time);
                        if (SpaceManagerActivity.this.proceeded) {
                            return null;
                        }
                        if (calculateHamingScore > SpaceManagerActivity.this.pass_score) {
                            MobiClean.getInstance().duplicatesData.grouplist.get(i2).children.size();
                            if (check(MobiClean.getInstance().duplicatesData.grouplist.get(i2).children, imageDetail)) {
                                MobiClean.getInstance().duplicatesData.grouplist.get(i2).children.add(imageDetail);
                            }
                        } else {
                            i2++;
                        }
                    }
                    if (!z) {
                        DuplicateGroup duplicateGroup = new DuplicateGroup();
                        ArrayList<ImageDetail> arrayList = new ArrayList<>();
                        duplicateGroup.children = arrayList;
                        arrayList.add(imageDetail);
                        MobiClean.getInstance().duplicatesData.grouplist.add(duplicateGroup);
                    }
                    i++;
                }
                if (MobiClean.getInstance().duplicatesData != null) {
                    MobiClean.getInstance().duplicatesData.fillDisplayedImageList();
                }
                return null;
            }

            @Override // android.os.AsyncTask
            public void onPostExecute(String str) {
                Util.appendLogmobiclean(SpaceManagerActivity.this.TAG, "onPostExecute()", GlobalData.FILE_NAME);
                SpaceManagerActivity.this.duplicatesScanningCompleted = true;
                if (isCancelled()) {
                    return;
                }
                try {
                    MobiClean.getInstance().duplicatesData.refresh();
                    SpaceManagerActivity.this.totcountDuplicates = MobiClean.getInstance().duplicatesData.totalDuplicates;
                    SpaceManagerActivity.this.totSizeDup = MobiClean.getInstance().duplicatesData.totalDuplicatesSize;
                    SpaceManagerActivity.this.checkDuplicatesStatus();
                    Log.d("SIZE", SpaceManagerActivity.this.totSizeDup + "   " + SpaceManagerActivity.this.totcountDuplicates);
                    MobiClean.getInstance().duplicatesData.alreadyScanned = true;
                } catch (Exception e) {
                    SpaceManagerActivity.this.tvdupSize.setText(SpaceManagerActivity.this.getString(R.string.mbc_scan));
                    SpaceManagerActivity.this.tvdupSize.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
                SpaceManagerActivity.this.updateTotalRecoverable();
                super.onPostExecute(str);
            }
        }.execute(new String[0]);
    }

    private int dip2px(float f) {
        return (int) ((f * getResources().getDisplayMetrics().density) + 0.5f);
    }

    private boolean duplicatesCached() {
        Util.appendLogmobiclean(this.TAG, "duplicatesCached()>>>>>>>>> Calling ", GlobalData.FILE_NAME);
        return MobiClean.getInstance().duplicatesData != null && MobiClean.getInstance().duplicatesData.showImageList.size() > 0;
    }

    private void executeThreads() {
        MobiClean.getInstance().spaceManagerModule = null;
        MobiClean.getInstance().spaceManagerModule = new SocialmediaModule();
        findViewById(R.id.tv_images_size).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_images_count).setVisibility(View.GONE);
        findViewById(R.id.pbar_photos_count).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_videos_size).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_videos_count).setVisibility(View.GONE);
        findViewById(R.id.pbar_videos_count).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_audios_size).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_audios_count).setVisibility(View.GONE);
        findViewById(R.id.pbar_audio_count).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_documents_count).setVisibility(View.GONE);
        findViewById(R.id.tv_documents_size).setVisibility(View.INVISIBLE);
        findViewById(R.id.pbar_docs_count).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_others_size).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_others_count).setVisibility(View.GONE);
        findViewById(R.id.pbar_others_count).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_apk_size).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_apk_count).setVisibility(View.GONE);
        findViewById(R.id.pbar_apk_count).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_downloads_size).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_downloads_count).setVisibility(View.GONE);
        findViewById(R.id.pbar_downloads_count).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_all_files_size).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_all_files_count).setVisibility(View.GONE);
        findViewById(R.id.pbar_all_count).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_duplicates_size).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_duplicates_count).setVisibility(View.GONE);
        findViewById(R.id.pbar_dup_count).setVisibility(View.VISIBLE);
        this.title = "Big ";
        this.socialMedia = new SocialMedia("Big ");
        this.l = new ArrayList();
        this.imagecalculation = new ImageCalculation();
        this.videoCalculation = new VideoCalculation();
        this.audioCalculation = new AudioCalculation();
        this.docCalculation = new DocCalculation();
        this.otherCalculation = new OtherCalculation();
        this.apkCalculation = new ApkCalculation();
        this.downloadsCalculation = new DownloadsCalculation();
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(7);
        this.executorService = newFixedThreadPool;
        newFixedThreadPool.execute(this.imagecalculation);
        this.executorService.execute(this.videoCalculation);
        this.executorService.execute(this.audioCalculation);
        this.executorService.execute(this.docCalculation);
        this.executorService.execute(this.otherCalculation);
        this.executorService.execute(this.apkCalculation);
        this.executorService.execute(this.downloadsCalculation);
        this.j.post(Integer.valueOf((int) GlobalData.SCAN_IMAGES));
    }

    private Drawable getADrawable(int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return getResources().getDrawable(i, getTheme());
        }
        return getResources().getDrawable(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap getBitmapFromExt(String str) {
        if (str == null) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.ic_unknown);
        }
        if (Arrays.asList("pdf", "doc", "docx", "xls", "ppt", "odt", "rtf", "txt", "pptx", "htm", "html", "log", "csv", "dot", "dotx", "docm", "dotm", "xml", "mht", "dic", "xlsx", "msg", "mhtml", "pps", "xltx", "xlt", "xlsm", "xltm", "ppsx", "pptm", "ppsm").contains(str)) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.toolbox_documents_icon);
        }
        if (Arrays.asList("mp3", "aac", "m4a", "wav").contains(str)) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.toolbox_audio_icon);
        }
        if (str.equalsIgnoreCase("apk")) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.apk_files);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void goback() {
        ProgressDialog progressDialog = this.dialogStopWait;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.dialogStopWait.dismiss();
        }
        MobiClean.getInstance().socialModule = null;
        MobiClean.getInstance().duplicatesData = null;
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
        this.proceeded = true;
        if (!this.redirectToNoti && !this.noti_result_back) {
            finish();
            return;
        }
        Intent intent = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intent);
    }

    private void handleAudiosSize(int i) {
        char c2 = '\r';
        if (i < 8) {
            this.k.setText(this.audioArray[0]);
        } else {
            if (i < 16) {
                this.k.setText(this.audioArray[1]);
                c2 = 1;
            } else if (i < 24) {
                this.k.setText(this.audioArray[2]);
                c2 = 2;
            } else if (i < 32) {
                this.k.setText(this.audioArray[3]);
                c2 = 3;
            } else if (i < 40) {
                this.k.setText(this.audioArray[4]);
                c2 = 4;
            } else if (i < 48) {
                this.k.setText(this.audioArray[5]);
                c2 = 5;
            } else if (i < 56) {
                this.k.setText(this.audioArray[6]);
                c2 = 6;
            } else if (i < 64) {
                this.k.setText(this.audioArray[7]);
                c2 = 7;
            } else if (i < 72) {
                this.k.setText(this.audioArray[8]);
                c2 = '\b';
            } else if (i < 80) {
                this.k.setText(this.audioArray[9]);
                c2 = '\t';
            } else if (i < 88) {
                this.k.setText(this.audioArray[10]);
                c2 = '\n';
            } else if (i < 96) {
                this.k.setText(this.audioArray[11]);
                c2 = 11;
            } else if (i < 99) {
                this.k.setText(this.audioArray[12]);
                c2 = '\f';
            } else if (i > 99) {
                this.k.setText(this.audioArray[13]);
            }
            GlobalData.audioCriteia = this.audioArraysize[c2];
        }
        c2 = 0;
        GlobalData.audioCriteia = this.audioArraysize[c2];
    }

    private void handleDocumentSize(int i) {
        char c2 = '\r';
        if (i < 8) {
            this.k.setText(this.filesArray[0]);
        } else {
            if (i < 16) {
                this.k.setText(this.filesArray[1]);
                c2 = 1;
            } else if (i < 24) {
                this.k.setText(this.filesArray[2]);
                c2 = 2;
            } else if (i < 32) {
                this.k.setText(this.filesArray[3]);
                c2 = 3;
            } else if (i < 40) {
                this.k.setText(this.filesArray[4]);
                c2 = 4;
            } else if (i < 48) {
                this.k.setText(this.filesArray[5]);
                c2 = 5;
            } else if (i < 56) {
                this.k.setText(this.filesArray[6]);
                c2 = 6;
            } else if (i < 64) {
                this.k.setText(this.filesArray[7]);
                c2 = 7;
            } else if (i < 72) {
                this.k.setText(this.filesArray[8]);
                c2 = '\b';
            } else if (i < 80) {
                this.k.setText(this.filesArray[9]);
                c2 = '\t';
            } else if (i < 88) {
                this.k.setText(this.filesArray[10]);
                c2 = '\n';
            } else if (i < 96) {
                this.k.setText(this.filesArray[11]);
                c2 = 11;
            } else if (i < 99) {
                this.k.setText(this.filesArray[12]);
                c2 = '\f';
            } else if (i > 99) {
                this.k.setText(this.filesArray[13]);
            }
            GlobalData.fileCriteia = this.filesArraySize[c2];
        }
        c2 = 0;
        GlobalData.fileCriteia = this.filesArraySize[c2];
    }

    private void handleImagesSize(int i) {
        char c2 = '\r';
        if (i < 8) {
            this.k.setText(this.imageArray[0]);
        } else {
            if (i < 16) {
                this.k.setText(this.imageArray[1]);
                c2 = 1;
            } else if (i < 24) {
                this.k.setText(this.imageArray[2]);
                c2 = 2;
            } else if (i < 32) {
                this.k.setText(this.imageArray[3]);
                c2 = 3;
            } else if (i < 40) {
                this.k.setText(this.imageArray[4]);
                c2 = 4;
            } else if (i < 48) {
                this.k.setText(this.imageArray[5]);
                c2 = 5;
            } else if (i < 56) {
                this.k.setText(this.imageArray[6]);
                c2 = 6;
            } else if (i < 64) {
                this.k.setText(this.imageArray[7]);
                c2 = 7;
            } else if (i < 72) {
                this.k.setText(this.imageArray[8]);
                c2 = '\b';
            } else if (i < 80) {
                this.k.setText(this.imageArray[9]);
                c2 = '\t';
            } else if (i < 88) {
                this.k.setText(this.imageArray[10]);
                c2 = '\n';
            } else if (i < 96) {
                this.k.setText(this.imageArray[11]);
                c2 = 11;
            } else if (i < 99) {
                this.k.setText(this.imageArray[12]);
                c2 = '\f';
            } else if (i > 99) {
                this.k.setText(this.imageArray[13]);
            }
            GlobalData.imageCriteia = this.imageArraySize[c2];
        }
        c2 = 0;
        GlobalData.imageCriteia = this.imageArraySize[c2];
    }

    private void handleOthersSize(int i) {
        if (i < 8) {
            this.k.setText(this.othersArray[0]);
        } else if (i < 16) {
            this.k.setText(this.othersArray[1]);
        } else if (i < 24) {
            this.k.setText(this.othersArray[2]);
        } else if (i < 32) {
            this.k.setText(this.othersArray[3]);
        } else if (i < 40) {
            this.k.setText(this.othersArray[4]);
        } else if (i < 48) {
            this.k.setText(this.othersArray[5]);
        } else if (i < 56) {
            this.k.setText(this.othersArray[6]);
        } else if (i < 64) {
            this.k.setText(this.othersArray[7]);
        } else if (i < 72) {
            this.k.setText(this.othersArray[8]);
        } else if (i < 80) {
            this.k.setText(this.othersArray[9]);
        } else if (i < 88) {
            this.k.setText(this.othersArray[10]);
        } else if (i < 96) {
            this.k.setText(this.othersArray[11]);
        } else if (i < 99) {
            this.k.setText(this.othersArray[12]);
        } else if (i > 99) {
            this.k.setText(this.othersArray[13]);
        }
    }

    private void handleVideosSize(int i) {
        char c2 = '\r';
        if (i < 8) {
            this.k.setText(this.videoArray[0]);
        } else {
            if (i < 16) {
                this.k.setText(this.videoArray[1]);
                c2 = 1;
            } else if (i < 24) {
                this.k.setText(this.videoArray[2]);
                c2 = 2;
            } else if (i < 32) {
                this.k.setText(this.videoArray[3]);
                c2 = 3;
            } else if (i < 40) {
                this.k.setText(this.videoArray[4]);
                c2 = 4;
            } else if (i < 48) {
                this.k.setText(this.videoArray[5]);
                c2 = 5;
            } else if (i < 56) {
                this.k.setText(this.videoArray[6]);
                c2 = 6;
            } else if (i < 64) {
                this.k.setText(this.videoArray[7]);
                c2 = 7;
            } else if (i < 72) {
                this.k.setText(this.videoArray[8]);
                c2 = '\b';
            } else if (i < 80) {
                this.k.setText(this.videoArray[9]);
                c2 = '\t';
            } else if (i < 88) {
                this.k.setText(this.videoArray[10]);
                c2 = '\n';
            } else if (i < 96) {
                this.k.setText(this.videoArray[11]);
                c2 = 11;
            } else if (i < 99) {
                this.k.setText(this.videoArray[12]);
                c2 = '\f';
            } else if (i > 99) {
                this.k.setText(this.videoArray[13]);
            }
            GlobalData.videoCriteia = this.videoArraySize[c2];
        }
        c2 = 0;
        GlobalData.videoCriteia = this.videoArraySize[c2];
    }

    private void init() {
        this.tv_allSpace = (TextView) findViewById(R.id.cpucollerfirst_temp);
        this.t_cpuuses = (TextView) findViewById(R.id.cpucoolerfirst_usage);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        this.t_unit = (TextView) findViewById(R.id.symboltext);
        this.t_text = (TextView) findViewById(R.id.symbol);
        this.rv_suggestion = (RecyclerView) findViewById(R.id.rv_suggestion);
        this.tv_no_result_found = (TextView) findViewById(R.id.tv_no_result_found);
        this.hiddenPermissionLayout = (RelativeLayout) findViewById(R.id.hiddenpermissionlayout);
        this.tvdupSize = (TextView) findViewById(R.id.tv_duplicates_size);
        this.tvdupCount = (TextView) findViewById(R.id.tv_duplicates_count);
        this.tvdownCount = (TextView) findViewById(R.id.tv_downloads_count);
        this.tvdownSize = (TextView) findViewById(R.id.tv_downloads_size);
        String str = this.from;
        boolean z = str != null && str.equalsIgnoreCase("TB");
        this.fromToolBox = z;
        if (z) {
            textView.setText(R.string.mbc_large_files);
        }
        this.sharedPrefUtil = new SharedPrefUtil(this);
        findViewById(R.id.iv_permission_close_btn).setOnClickListener(new View.OnClickListener() { // from class: com.mobiclean.phoneclean.tools.SpaceManagerActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SpaceManagerActivity.this.finish();
            }
        });
    }

    private void listeners() {
        Util.appendLogmobiclean(this.TAG, "listeners()>>>>>>>>> Calling ", GlobalData.FILE_NAME);
        findViewById(R.id.ll_audios).setOnClickListener(this);
        findViewById(R.id.ll_documents).setOnClickListener(this);
        findViewById(R.id.ll_images).setOnClickListener(this);
        findViewById(R.id.ll_apk_files).setOnClickListener(this);
        findViewById(R.id.ll_all_files).setOnClickListener(this);
        findViewById(R.id.ll_others).setOnClickListener(this);
        findViewById(R.id.ll_downloads).setOnClickListener(this);
        findViewById(R.id.ll_videos).setOnClickListener(this);
        findViewById(R.id.ll_duplicates).setOnClickListener(this);
    }

    private void makeHashMapExtnApks() {
        String[] strArr = {"apk"};
        this.hmExtnApk = new HashMap<>(1);
        for (int i = 0; i < 1; i++) {
            this.hmExtnApk.put(strArr[i].toLowerCase(), Boolean.TRUE);
        }
    }

    private void makeHashMapExtnDocs() {
        String[] strArr = {"pdf", "doc", "docx", "xls", "ppt", "odt", "rtf", "txt", "pptx", "htm", "html", "log", "csv", "dot", "dotx", "docm", "dotm", "xml", "mht", "dic", "xlsx", "msg", "mhtml", "pps", "xltx", "xlt", "xlsm", "xltm", "ppsx", "pptm", "ppsm"};
        this.hmExtnDocs = new HashMap<>(31);
        for (int i = 0; i < 31; i++) {
            this.hmExtnDocs.put(strArr[i].toLowerCase(), Boolean.TRUE);
        }
    }

    private void makeHashMapExtnsOtherMediaAndDocs() {
        String[] strArr = {"mp4", "3gp", "avi", "mpeg", "jpeg", "jpg", "png", "gif", "mp3", "tiff", "tif", "bmp", "svg", "webp", "webm", "flv", "wmv", "f4v", "swf", "asf", "ts", "mkv", "pdf", "doc", "docx", "xls", "ppt", "odt", "rtf", "txt", "pptx", "htm", "html", "log", "csv", "dot", "dotx", "docm", "aac", "dotm", "xml", "mht", "dic", "xlsx", "msg", "mhtml", "pps", "xltx", "xlt", "xlsm", "xltm", "ppsx", "pptm", "ppsm", "db", "ogg", "m4a", "wav", "wma", "mmf", "mp2", "flac", "au", "ac3", "mpg", "mov", "apk"};
        this.hmExtnsOtherMediaAndDocs = new HashMap<>();
        for (int i = 0; i < 67; i++) {
            this.hmExtnsOtherMediaAndDocs.put(strArr[i].toLowerCase(), Boolean.TRUE);
        }
    }

    private void noDataFoundScreen(String str) {
        Intent intent = new Intent(this, CommonResultActivity.class);
        intent.putExtra("DATA", str);
        intent.putExtra("TYPE", "NODOWNLOAD");
        if (this.redirectToNoti) {
            intent.putExtra(GlobalData.REDIRECTNOTI, false);
        }
            startActivity(intent);
            context = null;


    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint("WrongConstant")
    public void openFile(Context context, File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(335544320);
        intent.addFlags(1);
        Uri uriForFile = FileProvider.getUriForFile(context, "com.mobiclean.phoneclean.fileprovider", file);
        String mimeType = Util.getMimeType(file.getAbsolutePath());
        if (mimeType != null) {
            intent.setDataAndType(uriForFile, mimeType);
        } else if (!file.toString().contains(".doc") && !file.toString().contains(".docx")) {
            if (file.toString().contains(".pdf")) {
                intent.setDataAndType(uriForFile, "application/pdf");
            } else if (!file.toString().contains(".ppt") && !file.toString().contains(".pptx")) {
                if (!file.toString().contains(".xls") && !file.toString().contains(".xlsx")) {
                    if (!file.toString().contains(".zip") && !file.toString().contains(".rar")) {
                        if (file.toString().contains(".rtf")) {
                            intent.setDataAndType(uriForFile, "application/rtf");
                        } else if (!file.toString().contains(".wav") && !file.toString().contains(".mp3")) {
                            if (file.toString().contains(".gif")) {
                                intent.setDataAndType(uriForFile, "image/gif");
                            } else if (!file.toString().contains(".jpg") && !file.toString().contains(".jpeg") && !file.toString().contains(".png")) {
                                if (file.toString().contains(".txt")) {
                                    intent.setDataAndType(uriForFile, "text/plain");
                                } else if (!file.toString().contains(".3gp") && !file.toString().contains(".mpg") && !file.toString().contains(".mpeg") && !file.toString().contains(".mpe") && !file.toString().contains(".mp4") && !file.toString().contains(".avi")) {
                                    if (file.toString().contains(".apk")) {
                                        intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
                                    } else {
                                        intent.setDataAndType(uriForFile, "*/*");
                                    }
                                } else {
                                    intent.setDataAndType(uriForFile, "video/*");
                                }
                            } else {
                                intent.setDataAndType(uriForFile, "image/*");
                            }
                        } else {
                            intent.setDataAndType(uriForFile, "audio/*");
                        }
                    } else {
                        intent.setDataAndType(uriForFile, "application/x-wav");
                    }
                } else {
                    intent.setDataAndType(uriForFile, "application/vnd.ms-excel");
                }
            } else {
                intent.setDataAndType(uriForFile, "application/vnd.ms-powerpoint");
            }
        } else {
            intent.setDataAndType(uriForFile, "application/msword");
        }
        try {
            Util.appendLogmobiclean(this.TAG, "openFile try block", GlobalData.FILE_NAME);

                startActivity(intent);


        } catch (Exception e) {
            e.printStackTrace();
            String str = this.TAG;
            Util.appendLogmobiclean(str, "openFile catch block" + e.getMessage(), GlobalData.FILE_NAME);
            Toast.makeText(context, getString(R.string.unableToOpenFile), 0).show();
        }
    }

    private boolean permissionForStorageGiven() {
        Util.appendLogmobiclean(this.TAG, "permissionForStorageGiven()>>>>>>>>> Calling ", GlobalData.FILE_NAME);
        return super.checkStoragePermissions();
    }

    private void redirectToNoti() {
        this.redirectToNoti = getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false);
        this.noti_result_back = getIntent().getBooleanExtra(GlobalData.NOTI_RESULT_BACK, false);
        getIntent().getBooleanExtra(GlobalData.HEADER_NOTI_TRACK, false);
    }

    private void resetView() {
    }

    private void setFonts() {
        Util.appendLogmobiclean(this.TAG, "setFonts()>>>>>>>>> Calling ", GlobalData.FILE_NAME);
        if (ParentActivity.displaymetrics == null) {
            ParentActivity.displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(ParentActivity.displaymetrics);
        }
        int i = ParentActivity.displaymetrics.heightPixels;
    }

    private void showFileGridScreen() {
        if (this.mediasList.arrContents.size() == 0) {
            noDataFoundScreen(getString(R.string.mbc_notfound_apk));
        } else {
            Intent intent = new Intent(this, FilesGridActivity.class);
            intent.putExtra("fromToolBox", this.fromToolBox);
            if (this.redirectToNoti) {
                intent.putExtra(GlobalData.REDIRECTNOTI, false);
            }

                startActivity(intent);
                context = null;


        }
        Util.appendLogmobiclean(this.TAG, "View_Image Button click>>>>>>>>> Calling ", GlobalData.FILE_NAME);
    }

    private void showStopWaitdialog() {
        try {
            this.dialogStopWait.setCancelable(false);
            this.dialogStopWait.setCanceledOnTouchOutside(false);
            ProgressDialog progressDialog = this.dialogStopWait;
            progressDialog.setMessage("" + getResources().getString(R.string.mbc_stopping_scan) + ", " + getResources().getString(R.string.mbc_please_wait));
            this.dialogStopWait.show();
            this.dialogStopWait.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.mobiclean.phoneclean.tools.SpaceManagerActivity.8
                @Override // android.content.DialogInterface.OnCancelListener
                public void onCancel(DialogInterface dialogInterface) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopLoader() {
        this.tvdupSize.setVisibility(View.VISIBLE);
        this.tvdupCount.setVisibility(View.VISIBLE);
        findViewById(R.id.pbar_dup_count).setVisibility(View.GONE);
        TextView textView = this.tvdupSize;
        textView.setText("" + getString(R.string.mbc_scan));
    }

    private void trackIfFromNotification() {
        getIntent().getBooleanExtra("FROMNOTI", false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTotalRecoverable() {
        if (MobiClean.getInstance().spaceManagerModule != null) {
            String[] split = Util.convertBytes(MobiClean.getInstance().spaceManagerModule.totalSize).split(" ");
            TextView textView = this.tv_allSpace;
            textView.setText("" + split[0]);
            TextView textView2 = this.t_unit;
            textView2.setText("" + split[1]);
        }
    }

    public void b0() {
        if (permissionForStorageGiven()) {
            if (!this.isExecuting) {
                executeThreads();
            }
            RelativeLayout relativeLayout = this.hiddenPermissionLayout;
            if (relativeLayout != null) {
                relativeLayout.setVisibility(View.GONE);
                listeners();
            }
        }
    }

    public void checkDuplicatesStatus() {
        if (MobiClean.getInstance().duplicatesData.showImageList.size() >= 1) {
            long j = 0;
            for (int i = 0; i < MobiClean.getInstance().duplicatesData.showImageList.size(); i++) {
                j += MobiClean.getInstance().duplicatesData.showImageList.get(i).size;
            }
            TextView textView = this.tvdupSize;
            textView.setText("" + Util.convertBytes(j));
            TextView textView2 = this.tvdupCount;
            textView2.setText("" + MobiClean.getInstance().duplicatesData.showImageList.size() + " " + getString(R.string.items));
            this.tvdupSize.setVisibility(View.VISIBLE);
            this.tvdupCount.setVisibility(View.VISIBLE);
            findViewById(R.id.pbar_dup_count).setVisibility(View.GONE);
            return;
        }
        this.tvdupSize.setVisibility(View.VISIBLE);
        this.tvdupCount.setVisibility(View.INVISIBLE);
        findViewById(R.id.pbar_dup_count).setVisibility(View.GONE);
        TextView textView3 = this.tvdupSize;
        textView3.setText("" + getString(R.string.mbc_scan));
        this.tvdupCount.setText("");
    }

    public int getDefaultProgress() {
        return this.defaultProgress;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        Util.appendLogmobiclean(this.TAG, "onBackPressed() calling ", GlobalData.FILE_NAME);
        DuplicacyRefreshAsyncTask.stopExecuting = true;
        try {
            DuplicacyRefreshAsyncTask duplicacyRefreshAsyncTask = this.duplicacyRefreshAsyncTask;
            if (duplicacyRefreshAsyncTask != null) {
                duplicacyRefreshAsyncTask.cancel(true);
                if (this.duplicacyRefreshAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                    showStopWaitdialog();
                } else {
                    if (!this.redirectToNoti && !this.noti_result_back) {
                        goback();
                    }
                    Intent intent = new Intent(this, HomeActivity.class);
                    finish();
                    startActivity(intent);
                }
            } else {
                if (!this.redirectToNoti && !this.noti_result_back) {
                    goback();
                }
                finish();
                startActivity(new Intent(this, HomeActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_others) {
            if (findViewById(R.id.pbar_others_count).getVisibility() == View.VISIBLE || this.searchView.hasFocus()) {
                return;
            }
            stopLoader();
            if (multipleClicked()) {
                return;
            }
            DuplicacyRefreshAsyncTask.stopExecuting = true;
            MobiClean.getInstance().spaceManagerModule.currentList = this.socialMedia.hmFileTypeToMediaList.get(Integer.valueOf(SocialAnimationActivity.FileTypes.Others.ordinal()));
            try {
                this.mediasList = MobiClean.getInstance().spaceManagerModule.currentList;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.mediasList.arrContents.size() == 0) {
                noDataFoundScreen(getString(R.string.mbc_notfound));
            } else {
                Intent intent = new Intent(this, FilesGridActivity.class);
                intent.putExtra("fromToolBox", this.fromToolBox);
                if (this.redirectToNoti) {
                    intent.putExtra(GlobalData.REDIRECTNOTI, false);
                }
                    startActivity(intent);
                    context = null;


            }
            Util.appendLogmobiclean(this.TAG, "View_Other Button click>>>>>>>>> Calling ", GlobalData.FILE_NAME);
        } else if (id != R.id.ll_videos) {
            switch (id) {
                case R.id.ll_all_files /* 2131362463 */:
                    if (findViewById(R.id.pbar_all_count).getVisibility() == View.VISIBLE) {
                        return;
                    }
                    DuplicacyRefreshAsyncTask.stopExecuting = true;
                    if (multipleClicked()) {
                        return;
                    }
                    stopLoader();
                    MobiClean.getInstance().spaceManagerModule.currentList = this.socialMedia.hmFileTypeToMediaList.get(Integer.valueOf(SocialAnimationActivity.FileTypes.ALL.ordinal()));
                    try {
                        this.mediasList = MobiClean.getInstance().spaceManagerModule.currentList;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    if (this.mediasList.arrContents.size() == 0) {
                        noDataFoundScreen(getString(R.string.mbc_notfound));
                    } else {
                        Intent intent2 = new Intent(this, FilesGridActivity.class);
                        intent2.putExtra("fromToolBox", this.fromToolBox);
                        if (this.redirectToNoti) {
                            intent2.putExtra(GlobalData.REDIRECTNOTI, false);
                        }

                            startActivity(intent2);
                            context = null;

                    }
                    Util.appendLogmobiclean(this.TAG, "View_Image Button click>>>>>>>>> Calling ", GlobalData.FILE_NAME);
                    return;
                case R.id.ll_apk_files /* 2131362464 */:
                    if (this.searchView.hasFocus()) {
                        return;
                    }
                    stopLoader();
                    if (multipleClicked() || findViewById(R.id.pbar_apk_count).getVisibility() == View.VISIBLE) {
                        return;
                    }
                    DuplicacyRefreshAsyncTask.stopExecuting = true;
                    MobiClean.getInstance().spaceManagerModule.currentList = this.socialMedia.hmFileTypeToMediaList.get(Integer.valueOf(SocialAnimationActivity.FileTypes.APK.ordinal()));
                    try {
                        this.mediasList = MobiClean.getInstance().spaceManagerModule.currentList;
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                    showFileGridScreen();
                    return;
                case R.id.ll_audios /* 2131362465 */:
                    if (this.searchView.hasFocus()) {
                        return;
                    }
                    stopLoader();
                    if (multipleClicked() || findViewById(R.id.pbar_audio_count).getVisibility() == View.VISIBLE) {
                        return;
                    }
                    DuplicacyRefreshAsyncTask.stopExecuting = true;
                    MobiClean.getInstance().spaceManagerModule.currentList = this.socialMedia.hmFileTypeToMediaList.get(Integer.valueOf(SocialAnimationActivity.FileTypes.Audio.ordinal()));
                    try {
                        this.mediasList = MobiClean.getInstance().spaceManagerModule.currentList;
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                    if (this.mediasList.arrContents.size() == 0) {
                        noDataFoundScreen(getString(R.string.mbc_notfound_audio));
                    } else {
                        Intent intent3 = new Intent(this, FilesGridActivity.class);
                        intent3.putExtra("fromToolBox", this.fromToolBox);
                        if (this.redirectToNoti) {
                            intent3.putExtra(GlobalData.REDIRECTNOTI, false);
                        }
                            startActivity(intent3);
                            context = null;

                    }
                    Util.appendLogmobiclean(this.TAG, "View_Audio Button click>>>>>>>>> Calling ", GlobalData.FILE_NAME);
                    return;
                default:
                    switch (id) {
                        case R.id.ll_documents /* 2131362468 */:
                            if (this.searchView.hasFocus() || multipleClicked()) {
                                return;
                            }
                            stopLoader();
                            if (findViewById(R.id.pbar_docs_count).getVisibility() == View.VISIBLE) {
                                return;
                            }
                            DuplicacyRefreshAsyncTask.stopExecuting = true;
                            MobiClean.getInstance().spaceManagerModule.currentList = this.socialMedia.hmFileTypeToMediaList.get(Integer.valueOf(SocialAnimationActivity.FileTypes.Document.ordinal()));
                            try {
                                this.mediasList = MobiClean.getInstance().spaceManagerModule.currentList;
                            } catch (Exception e5) {
                                e5.printStackTrace();
                            }
                            if (this.mediasList.arrContents.size() == 0) {
                                noDataFoundScreen(getString(R.string.mbc_notfound_document));
                            } else {
                                Intent intent4 = new Intent(this, FilesGridActivity.class);
                                intent4.putExtra("fromToolBox", this.fromToolBox);
                                if (this.redirectToNoti) {
                                    intent4.putExtra(GlobalData.REDIRECTNOTI, false);
                                }


                                    startActivity(intent4);
                                    context = null;


                            }
                            Util.appendLogmobiclean(this.TAG, "View_Documents Button click>>>>>>>>> Calling ", GlobalData.FILE_NAME);
                            return;
                        case R.id.ll_downloads /* 2131362469 */:
                            if (this.searchView.hasFocus()) {
                                return;
                            }
                            stopLoader();
                            if (multipleClicked() || findViewById(R.id.pbar_downloads_count).getVisibility() == View.VISIBLE) {
                                return;
                            }
                            DuplicacyRefreshAsyncTask.stopExecuting = true;

                                startActivity(new Intent(SpaceManagerActivity.this, DownloadsActivity.class));
                            return;
                        case R.id.ll_duplicates /* 2131362470 */:
                            if (this.searchView.hasFocus() || multipleClicked()) {
                                return;
                            }
                            DuplicacyRefreshAsyncTask.stopExecuting = true;
                            this.duplicatesTapped = true;
                            stopLoader();
                            DuplicacyRefreshAsyncTask duplicacyRefreshAsyncTask = this.duplicacyRefreshAsyncTask;
                            if (duplicacyRefreshAsyncTask != null) {
                                if (duplicacyRefreshAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                                    this.duplicacyRefreshAsyncTask.cancel(true);
                                    return;
                                }
                                Log.i("CLICKED", "22222");
                                callDuplicates();
                                return;
                            }
                            Log.i("CLICKED", "33333");
                            callDuplicates();
                            return;
                        case R.id.ll_images /* 2131362471 */:
                            if (this.searchView.hasFocus()) {
                                return;
                            }
                            stopLoader();
                            if (multipleClicked() || findViewById(R.id.pbar_photos_count).getVisibility() == View.VISIBLE) {
                                return;
                            }
                            DuplicacyRefreshAsyncTask.stopExecuting = true;
                            MobiClean.getInstance().spaceManagerModule.currentList = this.socialMedia.hmFileTypeToMediaList.get(Integer.valueOf(SocialAnimationActivity.FileTypes.Image.ordinal()));
                            try {
                                this.mediasList = MobiClean.getInstance().spaceManagerModule.currentList;
                            } catch (Exception e6) {
                                e6.printStackTrace();
                            }
                            if (this.mediasList.arrContents.size() == 0) {
                                noDataFoundScreen(getString(R.string.mbc_notfound_photo));
                                return;
                            }
                            Intent intent5 = new Intent(this, FilesGridActivity.class);
                            intent5.putExtra("fromToolBox", this.fromToolBox);
                            if (this.redirectToNoti) {
                                intent5.putExtra(GlobalData.REDIRECTNOTI, false);
                            }

                                startActivity(intent5);
                                context = null;


                            Util.appendLogmobiclean(this.TAG, "View_Image Button click>>>>>>>>> Calling ", GlobalData.FILE_NAME);
                            return;
                        default:
                            return;
                    }
            }
        } else if (this.searchView.hasFocus()) {
        } else {
            stopLoader();
            if (multipleClicked() || findViewById(R.id.pbar_videos_count).getVisibility() == View.VISIBLE) {
                return;
            }
            DuplicacyRefreshAsyncTask.stopExecuting = true;
            MobiClean.getInstance().spaceManagerModule.currentList = this.socialMedia.hmFileTypeToMediaList.get(Integer.valueOf(SocialAnimationActivity.FileTypes.Video.ordinal()));
            try {
                this.mediasList = MobiClean.getInstance().spaceManagerModule.currentList;
            } catch (Exception e7) {
                e7.printStackTrace();
            }
            if (this.mediasList.arrContents.size() == 0) {
                noDataFoundScreen(getString(R.string.mbc_notfound_vidio));
            } else {
                Intent intent6 = new Intent(this, FilesGridActivity.class);
                intent6.putExtra("fromToolBox", this.fromToolBox);
                if (this.redirectToNoti) {
                    intent6.putExtra(GlobalData.REDIRECTNOTI, false);
                }


                    startActivity(intent6);
                    context = null;

                Util.appendLogmobiclean(this.TAG, "View_Image Button click>>>>>>>>> Calling ", GlobalData.FILE_NAME);
            }
            Util.appendLogmobiclean(this.TAG, "View_Videos Button click>>>>>>>>> Calling ", GlobalData.FILE_NAME);
        }
    }

    @Subscribe
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_space_manager);
        this.context = this;
        init();


        showInterstialAds(SpaceManagerActivity.this);

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


        makeHashMapExtnDocs();
        makeHashMapExtnsOtherMediaAndDocs();
        makeHashMapExtnApks();
        invalidateOptionsMenu();
        GlobalData.fromSpacemanager = true;
        Log.d("CRITERIA1", GlobalData.duplicacyDist + " " + GlobalData.duplicacyLevel + "  " + GlobalData.duplicacyTime);
        this.dialogStopWait = new ProgressDialog(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.j.register(this);
        this.proceeded = false;
        this.duplicatesTapped = false;
        try {
            this.from = getIntent().getStringExtra("FROM");
            Util.appendLogmobiclean(this.TAG, "Get Intent from Toolbox or not", GlobalData.FILE_NAME);
        } catch (Exception e) {
            String str = this.TAG;
            Util.appendLogmobiclean(str, "get intent !!!!!!!!!!!!!" + e.getMessage(), GlobalData.FILE_NAME);
            e.printStackTrace();
        }
        super.requestAppPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, R.string.mbc_runtime_permissions_txt, REQUEST_PERMISSIONS);
        redirectToNoti();
        if (permissionForStorageGiven()) {
            listeners();
        }
        setFonts();
        findViewById(R.id.rl_permission_close_btn).setOnClickListener(new View.OnClickListener() { // from class: com.mobiclean.phoneclean.tools.SpaceManagerActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!SpaceManagerActivity.this.redirectToNoti && !SpaceManagerActivity.this.noti_result_back) {
                    SpaceManagerActivity.this.finish();
                    return;
                }
                Intent intent = new Intent(SpaceManagerActivity.this, HomeActivity.class);
                SpaceManagerActivity.this.finish();
                SpaceManagerActivity.this.startActivity(intent);
            }
        });
        this.hiddenPermissionLayout.setOnTouchListener(new View.OnTouchListener() { // from class: com.mobiclean.phoneclean.tools.SpaceManagerActivity.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        clearNotification();
        trackIfFromNotification();
        b0();
        new SharedPrefUtil(this).saveLastTimeUsed(SharedPrefUtil.LUSED_SPACE_MANAGER, System.currentTimeMillis());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem findItem = menu.findItem(R.id.action_search);
        findItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() { // from class: com.mobiclean.phoneclean.tools.SpaceManagerActivity.4
            @Override // android.view.MenuItem.OnActionExpandListener
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                if (SpaceManagerActivity.this.rv_suggestion.getVisibility() == View.VISIBLE) {
                    SpaceManagerActivity.this.rv_suggestion.setVisibility(View.GONE);
                }
                if (SpaceManagerActivity.this.tv_no_result_found.getVisibility() == View.VISIBLE) {
                    SpaceManagerActivity.this.tv_no_result_found.setVisibility(View.GONE);
                    return true;
                }
                return true;
            }

            @Override // android.view.MenuItem.OnActionExpandListener
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }
        });
        SearchView searchView = (SearchView) findItem.getActionView();
        this.searchView = searchView;
        searchView.setQueryHint(getString(R.string.mbc_search));
        this.searchView.setBackgroundColor(-1);
        this.searchView.setIconifiedByDefault(true);
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) this.searchView.findViewById(R.id.search_src_text);
        if (searchAutoComplete != null) {
            try {
                searchAutoComplete.setTextColor(ContextCompat.getColor(this.context, R.color.black));
            } catch (Exception e) {
                e.printStackTrace();
            }
            searchAutoComplete.setTextSize(16.0f);
        }
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String str) {
                if (str.length() > 0) {
                    SpaceManagerActivity.this.rv_suggestion.setVisibility(View.VISIBLE);
                    SpaceManagerActivity.this.suggestionListAdapter.getFilter().filter(str);
                } else {
                    SpaceManagerActivity.this.rv_suggestion.setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String str) {
                return false;
            }
        });
        findItem.setVisible(this.completedTask == 6);
        return true;
    }

    @Override
    public void onDestroy() {
        try {
            this.j.unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public void onEvent(Integer num) {
        if (num.intValue() == 4490) {
            Log.i("onEvent", ">>>>>>>>>>>>onEvent called");
            if (this.proceeded || duplicatesCached()) {
                return;
            }
            GlobalData.duplicacyLevel = 25;
            GlobalData.duplicacyTime = 600;
            GlobalData.duplicacyDist = 20;
            DuplicacyRefreshAsyncTask duplicacyRefreshAsyncTask = new DuplicacyRefreshAsyncTask(this) {
                // from class: com.mobiclean.phoneclean.tools.SpaceManagerActivity.6
                @SuppressLint("StaticFieldLeak")
                @Override // android.os.AsyncTask
                public void onCancelled() {
                    super.onCancelled();
                    Log.d("CANCELED", ">>>>>>>>>>>>");
                    if (SpaceManagerActivity.this.duplicatesTapped) {
                        SpaceManagerActivity.this.callDuplicates();
                    } else {
                        SpaceManagerActivity.this.goback();
                    }
                }
            };
            this.duplicacyRefreshAsyncTask = duplicacyRefreshAsyncTask;
            duplicacyRefreshAsyncTask.execute("", "", "");
        } else if (num.intValue() != 8769 || this.context == null) {
        } else {
            createGroups();
        }
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
    public void onPause() {
        DuplicacyRefreshAsyncTask.stopExecuting = true;
        super.onPause();
    }

    @Override
    public void onPermissionsGranted(int i) {
        RelativeLayout relativeLayout = this.hiddenPermissionLayout;
        if (relativeLayout != null) {
            relativeLayout.setVisibility(View.GONE);
            listeners();
            if (!this.isExecuting) {
                executeThreads();
            }
            Util.appendLogmobiclean(this.TAG, "onPermissionsGranted()>>>>>>>>>GetLargeFilesData().execute() Calling ", GlobalData.FILE_NAME);
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        b0();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        LinearLayout linearLayout;
        super.onResume();
        if (GlobalData.returnedAfterDeletion) {
            this.completedTask = 0;
            executeThreads();
            GlobalData.returnedAfterDeletion = false;
        }
        if (GlobalData.proceededToAd) {
            finish();
        } else {
            if (!this.isExecuting) {
                if (MobiClean.getInstance().duplicatesData != null) {
                    MobiClean.getInstance().duplicatesData.fillDisplayedImageList();
                    if (!duplicatesCached()) {
                        resetView();
                    }
                }
                if (MobiClean.getInstance().spaceManagerModule != null) {
                    MobiClean.getInstance().spaceManagerModule.updateSelf();
                    updateTotalRecoverable();
                } else {
                    RelativeLayout relativeLayout = this.hiddenPermissionLayout;
                    if (relativeLayout != null && relativeLayout.getVisibility() == View.VISIBLE && permissionForStorageGiven()) {
                        listeners();
                        executeThreads();
                        this.hiddenPermissionLayout.setVisibility(View.GONE);
                    }
                }
            }
            GlobalData.shouldContinue = true;
            if (Util.isAdsFree(this) && (linearLayout = this.adView) != null) {
                linearLayout.setVisibility(View.GONE);
            }
        }
        try {
            long j = 0;
            for (int i = 0; i < MobiClean.getInstance().duplicatesData.showImageList.size(); i++) {
                try {
                    j += MobiClean.getInstance().duplicatesData.showImageList.get(i).size;
                } catch (Exception unused) {
                }
            }
            if (j > 0) {
                this.tvdupCount.setVisibility(View.VISIBLE);
                this.tvdupSize.setVisibility(View.VISIBLE);
                findViewById(R.id.pbar_dup_count).setVisibility(View.GONE);
                this.tvdupSize.setText("" + Util.convertBytes(j));
                this.tvdupCount.setText("" + MobiClean.getInstance().duplicatesData.showImageList.size() + " " + getString(R.string.items));
            } else {
                this.tvdupSize.setVisibility(View.VISIBLE);
                this.tvdupSize.setText("" + getString(R.string.mbc_scan));
                if (findViewById(R.id.pbar_dup_count).getVisibility() == View.VISIBLE) {
                    this.tvdupCount.setVisibility(View.GONE);
                    this.tvdupSize.setText("");
                } else {
                    this.tvdupCount.setVisibility(View.INVISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}