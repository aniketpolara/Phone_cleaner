package com.cleanPhone.mobileCleaner.tools;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.work.Data;

import com.bumptech.glide.Glide;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.google.firebase.messaging.ServiceStarter;
import com.cleanPhone.mobileCleaner.CommonResultActivity;
import com.cleanPhone.mobileCleaner.DialogListners;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.SocialAnimationActivity;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.similerphotos.DuplicacyRefreshAsyncTask;
import com.cleanPhone.mobileCleaner.socialmedia.MediaList;
import com.cleanPhone.mobileCleaner.utility.CacheActivity;
import com.cleanPhone.mobileCleaner.utility.FileUtil;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.MountPoints;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.SimpleSectionedListAdapter;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.BigSizeFilesWrapper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class FilesGridActivity extends CacheActivity implements View.OnClickListener, DialogListners {
    public static final int REQUEST_CODE_STORAGE_ACCESS_INPUT = 2415;
    private static String TAG = "FilesGridVertiseScreen";
    private TextView ads_message;
    private TextView ads_title;
    TextView tv_file_delete_text;
    private int defaultProgress;
    private CheckBox default_chkBox;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    int admob = 3;

    private RelativeLayout deleteBtn;
    private AsyncTask<String, Integer, DELETION> deleteTask;
    private int deviceHeight;
    private int deviceWidth;
    private ProgressDialog displayProgress;
    private boolean fromToolBox;
    public RelativeLayout j;
    public RelativeLayout k;
    public FrameLayout l;
    private ListView listview;
    private ListView listviewImages;
    public TextView m;
    private MediaList mediaList;
    public TextView n;
    public ImageAdapter o;
    public FilesAdapter p;
    private boolean redirectToNoti;
    private boolean resumedFromClick;
    private SeekBar seekbar;
    private SharedPrefUtil sharedPrefUtil;
    private TextView tv_size;
    private TextView tvview;
    private ArrayList<SimpleSectionedListAdapter.Section> sections = new ArrayList<>();
    private long totalSelectedSize = 0;
    private long totaldeletedsize = 0;
    private String[] imageArray = {"0 MB", "1 MB", "2 MB", "3 MB", "4 MB", "5 MB", "6 MB", "7 MB", "8 MB", "9 MB", "10 MB", "15 MB", "20 MB", "25 MB"};
    private String[] videoArray = {"0 MB", "5 MB", "10 MB", "15 MB", "25 MB", "30 MB", "50 MB", "75 MB", "100 MB", "200 MB", "350 MB", "500 MB", "750 MB", "1 GB"};
    private String[] audioArray = {"0 MB", "1 MB", "2 MB", "3 MB", "4 MB", "5 MB", "7 MB", "10 MB", "12 MB", "15 MB", "20 MB", "25 MB", "50 MB", "100 MB"};
    private String[] filesArray = {"0 KB", "50 KB", "100 KB", "500 KB", "1 MB", "2 MB", "3 MB", "4 MB", "5 MB", "10 MB", "15 MB", "20 MB", "25 MB", "50 MB"};
    private String[] othersArray = {"0 MB", "1 MB", "2 MB", "5 MB", "10 MB", "20 MB", "30 MB", "50 MB", "75 MB", "100 MB", "200 MB", "500 MB", "750 MB", "1 GB"};
    private String[] apkArray = {"0 MB", "1 MB", "2 MB", "3 MB", "4 MB", "5 MB", "7 MB", "10 MB", "12 MB", "15 MB", "20 MB", "25 MB", "50 MB", "100 MB"};
    private String[] allArray = {"0 MB", "1 MB", "2 MB", "3 MB", "4 MB", "5 MB", "6 MB", "7 MB", "8 MB", "9 MB", "10 MB", "15 MB", "20 MB", "25 MB"};
    private int[] imageArraySize = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25};
    private int[] videoArraySize = {0, 5, 10, 15, 25, 30, 50, 75, 100, 200, 350, ServiceStarter.ERROR_UNKNOWN, 750, 1024};
    private int[] audioArraysize = {0, 1, 2, 3, 4, 5, 7, 10, 12, 15, 20, 25, 50, 100};
    private int[] apkArraysize = {0, 1, 2, 3, 4, 5, 7, 10, 12, 15, 20, 25, 50, 100};
    private int[] filesArraySize = {0, 50, 100, ServiceStarter.ERROR_UNKNOWN, 1024, 2048, 3072, 4096, 5120, Data.MAX_DATA_BYTES, 15360, 20480, 25600, 51200};
    private int[] othersArraysize = {0, 1, 2, 5, 10, 20, 30, 50, 75, 100, 200, ServiceStarter.ERROR_UNKNOWN, 750, 1024};
    private int[] allArraySize = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25};
    private int notDeleted = 0;
    private String[] mHeaderNames = {""};
    private final int VIEW_TYPE_EXAMPLE = 0;
    private final int VIEW_TYPE_EXAMPLE_TWO = 1;
    private Integer[] mHeaderPositions = {0};

    public static class AnonymousClass22 {
        public static final int[] f5164a;
        public static final int[] b;

        static {
            int[] iArr = new int[DELETION.values().length];
            b = iArr;
            try {
                iArr[DELETION.ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                b[DELETION.PERMISSION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                b[DELETION.SUCCESS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[SocialAnimationActivity.FileTypes.values().length];
            f5164a = iArr2;
            try {
                iArr2[SocialAnimationActivity.FileTypes.Image.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f5164a[SocialAnimationActivity.FileTypes.Video.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f5164a[SocialAnimationActivity.FileTypes.Audio.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f5164a[SocialAnimationActivity.FileTypes.Document.ordinal()] = 4;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f5164a[SocialAnimationActivity.FileTypes.Others.ordinal()] = 5;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f5164a[SocialAnimationActivity.FileTypes.APK.ordinal()] = 6;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f5164a[SocialAnimationActivity.FileTypes.ALL.ordinal()] = 7;
            } catch (NoSuchFieldError unused10) {
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

    public class FilesAdapter extends BaseAdapter {
        public int f5172a;
        public int b;
        private LayoutInflater mInflater;

        public FilesAdapter(Context context) {
            this.f5172a = FilesGridActivity.this.dip2px(83.33f);
            this.b = FilesGridActivity.this.dip2px(76.33f);
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        private Bitmap getBitmapFromExt(String str) {
            if (str == null) {
                return BitmapFactory.decodeResource(FilesGridActivity.this.getResources(), R.drawable.ic_unknown);
            }
            if (Arrays.asList("pdf", "doc", "docx", "xls", "ppt", "odt", "rtf", "txt", "pptx", "htm", "html", "log", "csv", "dot", "dotx", "docm", "dotm", "xml", "mht", "dic", "xlsx", "msg", "mhtml", "pps", "xltx", "xlt", "xlsm", "xltm", "ppsx", "pptm", "ppsm").contains(str)) {
                return BitmapFactory.decodeResource(FilesGridActivity.this.getResources(), R.drawable.toolbox_documents_icon);
            }
            if (Arrays.asList("mp3", "aac", "m4a", "wav").contains(str)) {
                return BitmapFactory.decodeResource(FilesGridActivity.this.getResources(), R.drawable.toolbox_audio_icon);
            }
            if (str.equalsIgnoreCase("apk")) {
                return BitmapFactory.decodeResource(FilesGridActivity.this.getResources(), R.drawable.apk_files);
            }
            if (Arrays.asList("jpg", "png").contains(str)) {
                return BitmapFactory.decodeResource(FilesGridActivity.this.getResources(), R.drawable.similar_photo);
            }
            return null;
        }

        @Override
        public int getCount() {
            return FilesGridActivity.this.mediaList.filesList.size();
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
            int viewType = getItemViewType(i);
            ViewHolder2 viewHolder2 = null;
            switch (viewType) {
                case VIEW_TYPE_EXAMPLE_TWO: {
                    if (view == null) {
                        view = this.mInflater.inflate(R.layout.fileslistitem, viewGroup, false);
                    }
                    ImageView imageView = (ImageView) ViewHolder.get(view, R.id.junklistitemimage);
                    TextView textView = (TextView) ViewHolder.get(view, R.id.junklistitemapp);
                    TextView textView2 = (TextView) ViewHolder.get(view, R.id.junklistitemsize);
                    final CheckBox checkBox = (CheckBox) ViewHolder.get(view, R.id.junklistitemcheck);
                    ImageView imageView2 = (ImageView) ViewHolder.get(view, R.id.listiteminfo);
                    LinearLayout linearLayout = (LinearLayout) ViewHolder.get(view, R.id.checkcontainer);
                    checkBox.setFocusable(false);
                    checkBox.setClickable(false);
                    final BigSizeFilesWrapper bigSizeFilesWrapper = FilesGridActivity.this.mediaList.filesList.get(i);
                    try {
                        if (FilesGridActivity.this.mediaList.mediaType != SocialAnimationActivity.FileTypes.Audio) {
                            if (FilesGridActivity.this.mediaList.mediaType != SocialAnimationActivity.FileTypes.Others) {
                                if (FilesGridActivity.this.mediaList.mediaType != SocialAnimationActivity.FileTypes.Document) {
                                    if (FilesGridActivity.this.mediaList.mediaType == SocialAnimationActivity.FileTypes.APK) {
                                        imageView.setImageBitmap(BitmapFactory.decodeResource(FilesGridActivity.this.getResources(), R.drawable.apk_files));
                                    } else {
                                        Bitmap bitmapFromExt = getBitmapFromExt(FilenameUtils.getExtension(bigSizeFilesWrapper.path));
                                        if (bitmapFromExt == null) {
                                            Glide.with((FragmentActivity) FilesGridActivity.this).load(new File(bigSizeFilesWrapper.path)).placeholder((int) R.drawable.toolbox_documents_icon).error(R.drawable.toolbox_documents_icon).override(this.f5172a, this.b).centerCrop().into(imageView);
                                        } else {
                                            imageView.setImageBitmap(bitmapFromExt);
                                        }
                                    }
                                } else {
                                    imageView.setImageBitmap(BitmapFactory.decodeResource(FilesGridActivity.this.getResources(), R.drawable.toolbox_documents_icon));
                                }
                            } else {
                                imageView.setImageBitmap(BitmapFactory.decodeResource(FilesGridActivity.this.getResources(), R.drawable.toolbox_others_icon));
                                if (bigSizeFilesWrapper.ext.equalsIgnoreCase("apk")) {
                                    imageView.setImageBitmap(BitmapFactory.decodeResource(FilesGridActivity.this.getResources(), R.drawable.ic_android));
                                } else {
                                    imageView.setImageBitmap(BitmapFactory.decodeResource(FilesGridActivity.this.getResources(), R.drawable.toolbox_others_icon));
                                }
                            }
                        } else {
                            imageView.setImageBitmap(BitmapFactory.decodeResource(FilesGridActivity.this.getResources(), R.drawable.toolbox_audio_icon));
                        }
                        textView.setText("" + bigSizeFilesWrapper.name);
                        textView2.setText("" + Util.convertBytes(bigSizeFilesWrapper.size));
                        textView.setSelected(true);
                        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        imageView2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view2) {
                                if (FilesGridActivity.this.multipleClicked()) {
                                    return;
                                }
                                Util.appendLogmobiclean(FilesGridActivity.TAG, "imageInfo View.OnClickListener", GlobalData.FILE_NAME);
                                final Dialog dialog = new Dialog(FilesGridActivity.this);
                                if (dialog.getWindow() != null) {
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                    dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
                                }
                                dialog.setContentView(R.layout.new_dialog_junk_cancel);
                                dialog.setCancelable(true);
                                dialog.setCanceledOnTouchOutside(true);
                                dialog.getWindow().setLayout(-1, -1);
                                dialog.getWindow().setGravity(17);
                                View space;


                                dialog.findViewById(R.id.dialog_img).setVisibility(View.GONE);
                                dialog.findViewById(R.id.dialog_title).setVisibility(View.GONE);
                                String str = FilesGridActivity.this.getmDate(bigSizeFilesWrapper.dateTaken);
                                ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(FilesGridActivity.this.getResources().getString(R.string.mbc_name) + " " + bigSizeFilesWrapper.name + "\n\n" + FilesGridActivity.this.getResources().getString(R.string.mbc_path) + " " + bigSizeFilesWrapper.path + "\n\n" + FilesGridActivity.this.getResources().getString(R.string.mbc_size) + " " + Util.convertBytes(bigSizeFilesWrapper.size) + "\n\n" + FilesGridActivity.this.getResources().getString(R.string.mbc_time) + " " + str);
                                dialog.findViewById(R.id.ll_yes_no).setVisibility(View.GONE);
                                dialog.findViewById(R.id.ll_ok).setVisibility(View.VISIBLE);
                                dialog.findViewById(R.id.ll_ok).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view3) {
                                        if (FilesGridActivity.this.multipleClicked()) {
                                            return;
                                        }
                                        dialog.dismiss();
                                    }
                                });


                                dialog.show();


                            }
                        });
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view2) {
                                if (FilesGridActivity.this.multipleClicked()) {
                                    return;
                                }
                                Util.appendLogmobiclean(FilesGridActivity.TAG, "convertView .OnClickListener", GlobalData.FILE_NAME);
                                if (FilesGridActivity.this.mediaList.mediaType == SocialAnimationActivity.FileTypes.Audio) {
                                    Util.appendLogmobiclean(FilesGridActivity.TAG, "convertView.setOnClickListener type.equalsIgnoreCase(audios)", GlobalData.FILE_NAME);
                                    try {
                                        FilesGridActivity filesGridActivity = FilesGridActivity.this;
                                        filesGridActivity.openFile(filesGridActivity, new File(bigSizeFilesWrapper.path));
                                        FilesGridActivity filesGridActivity2 = FilesGridActivity.this;
                                        Toast.makeText(filesGridActivity2, filesGridActivity2.getString(R.string.unableToOpenFile), Toast.LENGTH_SHORT).show();
                                        return;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return;
                                    }
                                }
                                Util.appendLogmobiclean(FilesGridActivity.TAG, "convertView.setOnClickListener type.equalsIgnoreCase(files)", GlobalData.FILE_NAME);
                                try {
                                    FilesGridActivity filesGridActivity3 = FilesGridActivity.this;
                                    filesGridActivity3.openFile(filesGridActivity3, new File(bigSizeFilesWrapper.path));
                                } catch (Exception e2) {
                                    FilesGridActivity filesGridActivity4 = FilesGridActivity.this;
                                    Toast.makeText(filesGridActivity4, filesGridActivity4.getString(R.string.unableToOpenFile), Toast.LENGTH_SHORT).show();
                                    e2.printStackTrace();
                                }
                            }
                        });
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view2) {
                                if (FilesGridActivity.this.multipleClicked()) {
                                    return;
                                }
                                Util.appendLogmobiclean(FilesGridActivity.TAG, "checkcontainer LinearLayout click Listener", GlobalData.FILE_NAME);
                                if (bigSizeFilesWrapper.ischecked) {
                                    FilesGridActivity.this.mediaList.unSelectNodeAtIndex(i);
                                    checkBox.setChecked(false);
                                    for (int i2 = 0; i2 < FilesGridActivity.this.mediaList.arrContents.size(); i2++) {
                                        if (FilesGridActivity.this.mediaList.arrContents.get(i2).id == bigSizeFilesWrapper.id) {
                                            FilesGridActivity.this.mediaList.arrContents.get(i2).ischecked = false;
                                        }
                                    }
                                } else {
                                    for (int i3 = 0; i3 < FilesGridActivity.this.mediaList.arrContents.size(); i3++) {
                                        if (FilesGridActivity.this.mediaList.arrContents.get(i3).id == bigSizeFilesWrapper.id) {
                                            FilesGridActivity.this.mediaList.arrContents.get(i3).ischecked = true;
                                        }
                                    }
                                    FilesGridActivity.this.mediaList.selectNodeAtIndex(i);
                                    checkBox.setChecked(true);
                                }
                                FilesGridActivity.this.updateDeleteButtonTitle();
                                FilesGridActivity.this.updateSelectedCount();
                            }
                        });
                        if (bigSizeFilesWrapper.ischecked) {
                            checkBox.setChecked(true);
                        } else {
                            checkBox.setChecked(false);
                        }
                    } catch (Exception e) {
                        String str = FilesGridActivity.TAG;
                        Util.appendLogmobiclean(str, "FilesAdapter exception !!!!!!!!!!" + e.getMessage(), GlobalData.FILE_NAME);
                        e.printStackTrace();
                    }
                    break;
                }
                case VIEW_TYPE_EXAMPLE: {
                    if (view == null) {
                        viewHolder2 = new ViewHolder2(view);
                        view = this.mInflater.inflate(R.layout.ad_view, (ViewGroup) null);
//                        viewHolder2.rl_native = (TemplateView) view.findViewById(R.id.native_view);

                        ViewHolder2 finalViewHolder = viewHolder2;
                          break;
                    }
                }

            }

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

    public class ImageAdapter extends BaseAdapter {
        public int f5180a;
        public int b;
        public LayoutInflater mInflater;
        Context context;

        public ImageAdapter(Context context) {
            this.f5180a = FilesGridActivity.this.dip2px(83.33f);
            this.b = FilesGridActivity.this.dip2px(76.33f);
            this.context = context;
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Util.appendLogmobiclean(FilesGridActivity.TAG, "ImagesViewAdapter Constractor ", GlobalData.FILE_NAME);
            new IconHolder(context, true, true);
        }

        @Override
        public int getCount() {
            return FilesGridActivity.this.mediaList.filesList.size();
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
                view = this.mInflater.inflate(R.layout.fileslistitem, viewGroup, false);
            }
            ImageView imageView = (ImageView) ViewHolder.get(view, R.id.junklistitemimage);
            TextView textView = (TextView) ViewHolder.get(view, R.id.junklistitemapp);
            TextView textView2 = (TextView) ViewHolder.get(view, R.id.junklistitemsize);
            final CheckBox checkBox = (CheckBox) ViewHolder.get(view, R.id.junklistitemcheck);
            ImageView imageView2 = (ImageView) ViewHolder.get(view, R.id.listiteminfo);
            LinearLayout linearLayout = (LinearLayout) ViewHolder.get(view, R.id.checkcontainer);
            checkBox.setFocusable(false);
            checkBox.setClickable(false);
            final BigSizeFilesWrapper bigSizeFilesWrapper = FilesGridActivity.this.mediaList.filesList.get(i);
            Log.e(TAG, "getView: " + mediaList.filesList.get(i).path);
            try {
                Glide.with(context).load(new File(bigSizeFilesWrapper.path)).placeholder((int) R.drawable.toolbox_documents_icon).error(R.drawable.toolbox_documents_icon).override(this.f5180a, this.b).centerCrop().into(imageView);
                textView.setText("" + bigSizeFilesWrapper.name);
                textView2.setText("" + Util.convertBytes(bigSizeFilesWrapper.size));
                textView.setSelected(true);
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                imageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        if (FilesGridActivity.this.multipleClicked()) {
                            return;
                        }
                        Util.appendLogmobiclean(FilesGridActivity.TAG, "imageinfo click Listener", GlobalData.FILE_NAME);
                        final Dialog dialog = new Dialog(FilesGridActivity.this);
                        if (dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
                        }
                        dialog.setContentView(R.layout.new_dialog_junk_cancel);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.getWindow().setLayout(-1, -1);
                        dialog.getWindow().setGravity(17);
                        View space;

                        dialog.findViewById(R.id.dialog_img).setVisibility(View.GONE);
                        dialog.findViewById(R.id.dialog_title).setVisibility(View.GONE);
                        String format = new SimpleDateFormat("MMM dd,yyyy, hh:mm aaa").format(new Date(bigSizeFilesWrapper.dateTaken));
                        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(FilesGridActivity.this.getResources().getString(R.string.mbc_name) + " " + bigSizeFilesWrapper.name + "\n\n" + FilesGridActivity.this.getResources().getString(R.string.mbc_path) + " " + bigSizeFilesWrapper.path + "\n\n" + FilesGridActivity.this.getResources().getString(R.string.mbc_size) + " " + Util.convertBytes(bigSizeFilesWrapper.size) + "\n\n" + FilesGridActivity.this.getResources().getString(R.string.mbc_time) + " " + format);
                        dialog.findViewById(R.id.ll_yes_no).setVisibility(View.GONE);
                        dialog.findViewById(R.id.ll_ok).setVisibility(View.VISIBLE);
                        dialog.findViewById(R.id.ll_ok).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view3) {
                                if (FilesGridActivity.this.multipleClicked()) {
                                    return;
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        if (bigSizeFilesWrapper.ischecked) {
                            FilesGridActivity.this.mediaList.unSelectNodeAtIndex(i);
                            checkBox.setChecked(false);
                        } else {
                            FilesGridActivity.this.mediaList.selectNodeAtIndex(i);
                            checkBox.setChecked(true);
                        }
                        FilesGridActivity.this.updateDeleteButtonTitle();
                        FilesGridActivity.this.updateSelectedCount();
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        if (FilesGridActivity.this.multipleClicked()) {
                            return;
                        }
                        Util.appendLogmobiclean(FilesGridActivity.TAG, "convertView VIEW click Listener", GlobalData.FILE_NAME);
                        if (FilesGridActivity.this.mediaList.mediaType == SocialAnimationActivity.FileTypes.Image) {
                            Util.appendLogmobiclean(FilesGridActivity.TAG, "type.equalsIgnoreCase images", GlobalData.FILE_NAME);
                            try {
                                FilesGridActivity filesGridActivity = FilesGridActivity.this;
                                filesGridActivity.openFile(filesGridActivity, new File(bigSizeFilesWrapper.path));
                            } catch (Exception e) {
                                FilesGridActivity filesGridActivity2 = FilesGridActivity.this;
                                Toast.makeText(filesGridActivity2, filesGridActivity2.getString(R.string.unableToOpenFile), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                            FilesGridActivity.this.resumedFromClick = true;
                            return;
                        }
                        Util.appendLogmobiclean(FilesGridActivity.TAG, "type.equalsIgnoreCase Video", GlobalData.FILE_NAME);
                        try {
                            FilesGridActivity filesGridActivity3 = FilesGridActivity.this;
                            filesGridActivity3.openFile(filesGridActivity3, new File(bigSizeFilesWrapper.path));
                        } catch (Exception e2) {
                            FilesGridActivity filesGridActivity4 = FilesGridActivity.this;
                            Toast.makeText(filesGridActivity4, filesGridActivity4.getString(R.string.unableToOpenFile), Toast.LENGTH_SHORT).show();
                            e2.printStackTrace();
                        }
                        FilesGridActivity.this.resumedFromClick = true;
                    }
                });
                if (bigSizeFilesWrapper.ischecked) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
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

    public class ViewHolder2 {



        public ViewHolder2(View ignoreListAdapter) {
        }
    }

    private void GetDeviceDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
    }

    private boolean deleteImageFile(BigSizeFilesWrapper bigSizeFilesWrapper) {
        boolean z;
        File file = new File(bigSizeFilesWrapper.path);
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
                    FilesGridActivity.this.mediaList.initRecoveredBeforeDelete();
                    FilesGridActivity.this.displayProgress = new ProgressDialog(FilesGridActivity.this);
                    FilesGridActivity.this.getWindow().addFlags(2097280);
                    FilesGridActivity.this.displayProgress.setCanceledOnTouchOutside(false);
                    FilesGridActivity.this.displayProgress.setProgressStyle(1);
                    FilesGridActivity.this.displayProgress.setCancelable(false);
                    FilesGridActivity.this.displayProgress.setMax(FilesGridActivity.this.mediaList.selectedCount);
                    FilesGridActivity.this.displayProgress.show();
                }

                @Override
                @RequiresApi(api = 21)
                public DELETION doInBackground(Void... voidArr) {
                    if (FileUtil.isSystemAndroid5()) {
                        FilesGridActivity.onActivityResultLollipop(FilesGridActivity.this, i, i2, intent);
                    }
                    return FilesGridActivity.this.permissionBasedDeletion();
                }

                @Override
                public void onPostExecute(DELETION deletion) {
                    if (FilesGridActivity.this.displayProgress != null) {
                        FilesGridActivity.this.displayProgress.dismiss();
                    }
                    try {
                        FilesGridActivity.this.getWindow().clearFlags(2097280);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int i3 = AnonymousClass22.b[deletion.ordinal()];
                    if (i3 == 1) {
                        Toast.makeText(FilesGridActivity.this, "", Toast.LENGTH_LONG).show();
                    } else if (i3 == 2) {
                        FilesGridActivity.this.permissionAlert();
                    } else if (i3 != 3) {
                    } else {
                        FilesGridActivity.this.successDeletion();
                    }
                }

                @Override
                public void onProgressUpdate(Integer... numArr) {
                    super.onProgressUpdate(numArr);
                    FilesGridActivity.this.displayProgress.setProgress(numArr[0].intValue());
                }
            }.execute(new Void[0]);
        } catch (Exception e) {
            String str = TAG;
            Util.appendLogmobiclean(str, "Permission deletion exception====" + e.getMessage(), GlobalData.FILE_NAME);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void deletionTask() {
        Util.appendLogmobiclean(TAG, "method deletionTask calling", GlobalData.FILE_NAME);
        this.deleteTask = new AsyncTask<String, Integer, DELETION>() {
            @RequiresApi(api = 21)
            private boolean isBothStorageCanDelete(ArrayList<String> arrayList) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (!FileUtil.isWritableNormalOrSaf(FilesGridActivity.this, new File(arrayList.get(i)))) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public void onPreExecute() {
                Util.appendLogmobiclean(FilesGridActivity.TAG, "method deletionTask pre execute calling", GlobalData.FILE_NAME);
                FilesGridActivity.this.mediaList.initRecoveredBeforeDelete();
                FilesGridActivity.this.displayProgress = new ProgressDialog(FilesGridActivity.this);
                FilesGridActivity.this.getWindow().addFlags(2097280);
                FilesGridActivity.this.displayProgress.setCanceledOnTouchOutside(false);
                FilesGridActivity.this.displayProgress.setCancelable(false);
                FilesGridActivity.this.displayProgress.setMax(FilesGridActivity.this.mediaList.selectedCount);
                super.onPreExecute();
            }

            @Override
            public DELETION doInBackground(String... strArr) {
                if (strArr != null) {
                    return FilesGridActivity.this.permissionBasedDeletion();
                }
                Util.appendLogmobiclean(FilesGridActivity.TAG, "method deletionTask doinbackground calling", GlobalData.FILE_NAME);
                if (FileUtil.IsDeletionBelow6()) {
                    Util.appendLogmobiclean(FilesGridActivity.TAG, "method deletionTask os below 5.0", GlobalData.FILE_NAME);
                    return FilesGridActivity.this.normalDeletion();
                }
                ArrayList<String> returnMountPOints = MountPoints.returnMountPOints(FilesGridActivity.this);
                if (returnMountPOints == null) {
                    Util.appendLogmobiclean(FilesGridActivity.TAG, "mount points arr is null", GlobalData.FILE_NAME);
                    return FilesGridActivity.this.normalDeletion();
                } else if (returnMountPOints.size() == 1) {
                    Util.appendLogmobiclean(FilesGridActivity.TAG, "mount points arr size is 1", GlobalData.FILE_NAME);
                    return FilesGridActivity.this.normalDeletion();
                } else {
                    String str = FilesGridActivity.TAG;
                    Util.appendLogmobiclean(str, "mount points arr size is " + returnMountPOints.size(), GlobalData.FILE_NAME);
                    File file = new File(returnMountPOints.get(1));
                    if (file.listFiles() == null || file.listFiles().length == 0) {
                        return FilesGridActivity.this.normalDeletion();
                    }
                    if (Build.VERSION.SDK_INT > 21) {
                        if (!isBothStorageCanDelete(returnMountPOints)) {
                            Util.appendLogmobiclean(FilesGridActivity.TAG, "file is not able to write on external.Taking Permission", GlobalData.FILE_NAME);
                            return DELETION.PERMISSION;
                        }
                        Util.appendLogmobiclean(FilesGridActivity.TAG, "file is able to write on external", GlobalData.FILE_NAME);
                        return FilesGridActivity.this.permissionBasedDeletion();
                    }
                    return DELETION.SUCCESS;
                }
            }

            @Override
            public void onPostExecute(DELETION deletion) {
                String str = FilesGridActivity.TAG;
                Util.appendLogmobiclean(str, "method deletionTask onPostExecute() calling with status====" + deletion.name(), GlobalData.FILE_NAME);
                FilesGridActivity.this.deleteBtn.setEnabled(true);
                try {
                    FilesGridActivity.this.getWindow().clearFlags(2097280);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int i = AnonymousClass22.b[deletion.ordinal()];
                if (i == 1) {
                    FilesGridActivity filesGridActivity = FilesGridActivity.this;
                    Toast.makeText(filesGridActivity, "" + FilesGridActivity.this.getString(R.string.error), Toast.LENGTH_LONG).show();
                } else if (i == 2) {
                    FilesGridActivity.this.permissionAlert();
                } else if (i == 3) {
                    FilesGridActivity.this.successDeletion();
                }
                super.onPostExecute(deletion);
            }

            @Override
            public void onProgressUpdate(Integer... numArr) {
                super.onProgressUpdate(numArr);
                FilesGridActivity.this.displayProgress.setProgress(numArr[0].intValue());
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

    public int dip2px(float f) {
        return (int) ((f * getResources().getDisplayMetrics().density) + 0.5f);
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

    public String getmDate(long j) {
        String charSequence = DateFormat.format("dd-MMM-yyyy' at 'HH:mm a", new Date(j * 1000)).toString();
        return charSequence + "";
    }

    public void handleAllSize(int i) {
        char c2 = '\f';
        if (i < 8) {
            this.tv_size.setText(this.allArray[0]);
            c2 = 0;
        } else if (i < 16) {
            this.tv_size.setText(this.allArray[1]);
            c2 = 1;
        } else if (i < 24) {
            this.tv_size.setText(this.allArray[2]);
            c2 = 2;
        } else if (i < 32) {
            this.tv_size.setText(this.allArray[3]);
            c2 = 3;
        } else if (i < 40) {
            this.tv_size.setText(this.allArray[4]);
            c2 = 4;
        } else if (i < 48) {
            this.tv_size.setText(this.allArray[5]);
            c2 = 5;
        } else if (i < 56) {
            this.tv_size.setText(this.allArray[6]);
            c2 = 6;
        } else if (i < 64) {
            this.tv_size.setText(this.allArray[7]);
            c2 = 7;
        } else if (i < 72) {
            this.tv_size.setText(this.allArray[8]);
            c2 = '\b';
        } else if (i < 80) {
            this.tv_size.setText(this.allArray[9]);
            c2 = '\t';
        } else if (i < 88) {
            this.tv_size.setText(this.allArray[10]);
            c2 = '\n';
        } else if (i < 96) {
            this.tv_size.setText(this.allArray[11]);
            c2 = 11;
        } else if (i < 99) {
            this.tv_size.setText(this.allArray[12]);
        } else {
            this.tv_size.setText(this.allArray[13]);
            c2 = '\r';
        }
        GlobalData.allCriteria = this.allArraySize[c2];
        Log.e(TAG, "handleAllSize refresh called");
        refresh(GlobalData.allCriteria * 1048576, getString(R.string.files));
    }

    public void handleApkSize(int i) {
        char c2 = '\f';
        if (i < 8) {
            this.tv_size.setText(this.apkArray[0]);
            c2 = 0;
        } else if (i < 16) {
            this.tv_size.setText(this.apkArray[1]);
            c2 = 1;
        } else if (i < 24) {
            this.tv_size.setText(this.apkArray[2]);
            c2 = 2;
        } else if (i < 32) {
            this.tv_size.setText(this.apkArray[3]);
            c2 = 3;
        } else if (i < 40) {
            this.tv_size.setText(this.apkArray[4]);
            c2 = 4;
        } else if (i < 48) {
            this.tv_size.setText(this.apkArray[5]);
            c2 = 5;
        } else if (i < 56) {
            this.tv_size.setText(this.apkArray[6]);
            c2 = 6;
        } else if (i < 64) {
            this.tv_size.setText(this.apkArray[7]);
            c2 = 7;
        } else if (i < 72) {
            this.tv_size.setText(this.apkArray[8]);
            c2 = '\b';
        } else if (i < 80) {
            this.tv_size.setText(this.apkArray[9]);
            c2 = '\t';
        } else if (i < 88) {
            this.tv_size.setText(this.apkArray[10]);
            c2 = '\n';
        } else if (i < 96) {
            this.tv_size.setText(this.apkArray[11]);
            c2 = 11;
        } else if (i < 99) {
            this.tv_size.setText(this.apkArray[12]);
        } else {
            this.tv_size.setText(this.apkArray[13]);
            c2 = '\r';
        }
        GlobalData.apkCriteria = this.apkArraysize[c2];
        Log.e(TAG, "handleApkSize refresh called");
        refresh(GlobalData.apkCriteria * 1048576, getString(R.string.apks));
    }

    public void handleAudiosSize(int i) {
        char c2 = '\f';
        if (i < 8) {
            this.tv_size.setText(this.audioArray[0]);
            c2 = 0;
        } else if (i < 16) {
            this.tv_size.setText(this.audioArray[1]);
            c2 = 1;
        } else if (i < 24) {
            this.tv_size.setText(this.audioArray[2]);
            c2 = 2;
        } else if (i < 32) {
            this.tv_size.setText(this.audioArray[3]);
            c2 = 3;
        } else if (i < 40) {
            this.tv_size.setText(this.audioArray[4]);
            c2 = 4;
        } else if (i < 48) {
            this.tv_size.setText(this.audioArray[5]);
            c2 = 5;
        } else if (i < 56) {
            this.tv_size.setText(this.audioArray[6]);
            c2 = 6;
        } else if (i < 64) {
            this.tv_size.setText(this.audioArray[7]);
            c2 = 7;
        } else if (i < 72) {
            this.tv_size.setText(this.audioArray[8]);
            c2 = '\b';
        } else if (i < 80) {
            this.tv_size.setText(this.audioArray[9]);
            c2 = '\t';
        } else if (i < 88) {
            this.tv_size.setText(this.audioArray[10]);
            c2 = '\n';
        } else if (i < 96) {
            this.tv_size.setText(this.audioArray[11]);
            c2 = 11;
        } else if (i < 99) {
            this.tv_size.setText(this.audioArray[12]);
        } else {
            this.tv_size.setText(this.audioArray[13]);
            c2 = '\r';
        }
        GlobalData.audioCriteia = this.audioArraysize[c2];
        Log.e(TAG, "handleAudiosSize refresh called");
        refresh(GlobalData.audioCriteia * 1048576, getString(R.string.audios));
    }

    public void handleDocumentSize(int i) {
        char c2 = '\f';
        if (i < 8) {
            this.tv_size.setText(this.filesArray[0]);
            c2 = 0;
        } else if (i < 16) {
            this.tv_size.setText(this.filesArray[1]);
            c2 = 1;
        } else if (i < 24) {
            this.tv_size.setText(this.filesArray[2]);
            c2 = 2;
        } else if (i < 32) {
            this.tv_size.setText(this.filesArray[3]);
            c2 = 3;
        } else if (i < 40) {
            this.tv_size.setText(this.filesArray[4]);
            c2 = 4;
        } else if (i < 48) {
            this.tv_size.setText(this.filesArray[5]);
            c2 = 5;
        } else if (i < 56) {
            this.tv_size.setText(this.filesArray[6]);
            c2 = 6;
        } else if (i < 64) {
            this.tv_size.setText(this.filesArray[7]);
            c2 = 7;
        } else if (i < 72) {
            this.tv_size.setText(this.filesArray[8]);
            c2 = '\b';
        } else if (i < 80) {
            this.tv_size.setText(this.filesArray[9]);
            c2 = '\t';
        } else if (i < 88) {
            this.tv_size.setText(this.filesArray[10]);
            c2 = '\n';
        } else if (i < 96) {
            this.tv_size.setText(this.filesArray[11]);
            c2 = 11;
        } else if (i < 99) {
            this.tv_size.setText(this.filesArray[12]);
        } else {
            this.tv_size.setText(this.filesArray[13]);
            c2 = '\r';
        }
        GlobalData.fileCriteia = this.filesArraySize[c2];
        Log.e(TAG, "handleDocumentSize refresh called");
        refresh(GlobalData.fileCriteia * 1024, getString(R.string.documents));
    }

    public void handleImagesSize(int i) {
        char c2 = '\f';
        if (i < 8) {
            this.tv_size.setText(this.imageArray[0]);
            c2 = 0;
        } else if (i < 16) {
            this.tv_size.setText(this.imageArray[1]);
            c2 = 1;
        } else if (i < 24) {
            this.tv_size.setText(this.imageArray[2]);
            c2 = 2;
        } else if (i < 32) {
            this.tv_size.setText(this.imageArray[3]);
            c2 = 3;
        } else if (i < 40) {
            this.tv_size.setText(this.imageArray[4]);
            c2 = 4;
        } else if (i < 48) {
            this.tv_size.setText(this.imageArray[5]);
            c2 = 5;
        } else if (i < 56) {
            this.tv_size.setText(this.imageArray[6]);
            c2 = 6;
        } else if (i < 64) {
            this.tv_size.setText(this.imageArray[7]);
            c2 = 7;
        } else if (i < 72) {
            this.tv_size.setText(this.imageArray[8]);
            c2 = '\b';
        } else if (i < 80) {
            this.tv_size.setText(this.imageArray[9]);
            c2 = '\t';
        } else if (i < 88) {
            this.tv_size.setText(this.imageArray[10]);
            c2 = '\n';
        } else if (i < 96) {
            this.tv_size.setText(this.imageArray[11]);
            c2 = 11;
        } else if (i < 99) {
            this.tv_size.setText(this.imageArray[12]);
        } else {
            this.tv_size.setText(this.imageArray[13]);
            c2 = '\r';
        }
        int i2 = this.imageArraySize[c2];
        GlobalData.imageCriteia = i2;
        refresh(i2 * 1048576, getString(R.string.images_found));
    }

    public void handleOthersSize(int i) {
        char c2 = '\f';
        if (i < 8) {
            this.tv_size.setText(this.othersArray[0]);
            c2 = 0;
        } else if (i < 16) {
            this.tv_size.setText(this.othersArray[1]);
            c2 = 1;
        } else if (i < 24) {
            this.tv_size.setText(this.othersArray[2]);
            c2 = 2;
        } else if (i < 32) {
            this.tv_size.setText(this.othersArray[3]);
            c2 = 3;
        } else if (i < 40) {
            this.tv_size.setText(this.othersArray[4]);
            c2 = 4;
        } else if (i < 48) {
            this.tv_size.setText(this.othersArray[5]);
            c2 = 5;
        } else if (i < 56) {
            this.tv_size.setText(this.othersArray[6]);
            c2 = 6;
        } else if (i < 64) {
            this.tv_size.setText(this.othersArray[7]);
            c2 = 7;
        } else if (i < 72) {
            this.tv_size.setText(this.othersArray[8]);
            c2 = '\b';
        } else if (i < 80) {
            this.tv_size.setText(this.othersArray[9]);
            c2 = '\t';
        } else if (i < 88) {
            this.tv_size.setText(this.othersArray[10]);
            c2 = '\n';
        } else if (i < 96) {
            this.tv_size.setText(this.othersArray[11]);
            c2 = 11;
        } else if (i < 99) {
            this.tv_size.setText(this.othersArray[12]);
        } else {
            this.tv_size.setText(this.othersArray[13]);
            c2 = '\r';
        }
        GlobalData.otherCriteia = this.othersArraysize[c2];
        Log.e(TAG, "handleOthersSize refresh called");
        refresh(GlobalData.otherCriteia * 1048576, getString(R.string.mbc_myothers));
    }

    public void handleVideosSize(int i) {
        char c2 = '\f';
        if (i < 8) {
            this.tv_size.setText(this.videoArray[0]);
            c2 = 0;
        } else if (i < 16) {
            this.tv_size.setText(this.videoArray[1]);
            c2 = 1;
        } else if (i < 24) {
            this.tv_size.setText(this.videoArray[2]);
            c2 = 2;
        } else if (i < 32) {
            this.tv_size.setText(this.videoArray[3]);
            c2 = 3;
        } else if (i < 40) {
            this.tv_size.setText(this.videoArray[4]);
            c2 = 4;
        } else if (i < 48) {
            this.tv_size.setText(this.videoArray[5]);
            c2 = 5;
        } else if (i < 56) {
            this.tv_size.setText(this.videoArray[6]);
            c2 = 6;
        } else if (i < 64) {
            this.tv_size.setText(this.videoArray[7]);
            c2 = 7;
        } else if (i < 72) {
            this.tv_size.setText(this.videoArray[8]);
            c2 = '\b';
        } else if (i < 80) {
            this.tv_size.setText(this.videoArray[9]);
            c2 = '\t';
        } else if (i < 88) {
            this.tv_size.setText(this.videoArray[10]);
            c2 = '\n';
        } else if (i < 96) {
            this.tv_size.setText(this.videoArray[11]);
            c2 = 11;
        } else if (i < 99) {
            this.tv_size.setText(this.videoArray[12]);
        } else {
            this.tv_size.setText(this.videoArray[13]);
            c2 = '\r';
        }
        GlobalData.videoCriteia = this.videoArraySize[c2];
        Log.e(TAG, "handleVideosSize refresh called");
        refresh(GlobalData.videoCriteia * 1048576, getString(R.string.videos));
    }

    private void initControls() {
        this.j = (RelativeLayout) findViewById(R.id.layout_one);
        this.k = (RelativeLayout) findViewById(R.id.layout_two);
        this.l = (FrameLayout) findViewById(R.id.frame_mid_laysss);
        this.m = (TextView) findViewById(R.id.ads_btn_countinue);
        this.tv_file_delete_text = (TextView) findViewById(R.id.tv_file_delete_text);
        this.n = (TextView) findViewById(R.id.ads_btn_cancel);
        this.ads_title = (TextView) findViewById(R.id.dialog_title);
        this.ads_message = (TextView) findViewById(R.id.dialog_msg);
        this.default_chkBox = (CheckBox) findViewById(R.id.default_chkBox);
        this.n.setOnClickListener(this);
        this.m.setOnClickListener(this);
        this.listviewImages = (ListView) findViewById(R.id.grid);
        this.deleteBtn = (RelativeLayout) findViewById(R.id.btnfiledelete);
        this.listview = (ListView) findViewById(R.id.listview);
        this.tvview = (TextView) findViewById(R.id.tv_gird);
        DuplicacyRefreshAsyncTask.stopExecuting = true;

        this.sharedPrefUtil = new SharedPrefUtil(this);
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

    public DELETION normalDeletion() {
        int i = 0;
        this.notDeleted = 0;
        Util.appendLogmobiclean(TAG, "method normaldeletion calling ", GlobalData.FILE_NAME);
        try {
            try {
                int size = this.mediaList.filesList.size();
                int i2 = 0;
                while (i < size) {
                    BigSizeFilesWrapper bigSizeFilesWrapper = this.mediaList.filesList.get(i);
                    if (bigSizeFilesWrapper.ischecked) {
                        if (deleteImageFile(bigSizeFilesWrapper)) {
                            Util.appendLogmobiclean(TAG, "normal delete success with path===" + bigSizeFilesWrapper.path, GlobalData.FILE_NAME);
                            this.mediaList.deleteNode(bigSizeFilesWrapper);
                            this.mediaList.filesList.remove(i);
                            i2++;
                            this.displayProgress.setProgress(i2);
                            size--;
                        } else {
                            this.notDeleted++;
                            Util.appendLogmobiclean(TAG, "normal delete failed with path===" + bigSizeFilesWrapper.path, GlobalData.FILE_NAME);
                            i++;
                        }
                        MobiClean.getInstance().spaceManagerModule.recoveredSize += bigSizeFilesWrapper.size;
                    } else {
                        i++;
                    }
                }
                return DELETION.SUCCESS;
            } catch (Exception e) {
                Util.appendLogmobiclean(TAG, "normal delete exception====" + e.getMessage(), GlobalData.FILE_NAME);
                DELETION deletion = DELETION.ERROR;
                return DELETION.SUCCESS;
            }
        } catch (Throwable unused) {
            return DELETION.SUCCESS;
        }
    }

    @SuppressLint("WrongConstant")
    @TargetApi(21)
    public static void onActivityResultLollipop(Context context, int i, int i2, @NonNull Intent intent) {
        if (i == 2415) {
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

    @SuppressLint("WrongConstant")
    public void openFile(Context context, File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(3);
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
                        } else if (!file.toString().contains(".wav") && !file.toString().contains(".mp3") && !file.toString().contains(".wma") && !file.toString().contains(".ogg") && !file.toString().contains(".flac") && !file.toString().contains(".aac")) {
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
        context.startActivity(intent);
    }

    public void permissionAlert() {
        final Dialog dialog = new Dialog(this);
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


        dialog.findViewById(R.id.dialog_img).setVisibility(View.GONE);
        dialog.findViewById(R.id.dialog_title).setVisibility(View.GONE);
        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(getString(R.string.mbc_sdcard_permission_delete));
        ((TextView) dialog.findViewById(R.id.ll_no_txt)).setText(getString(R.string.mbc_deny));
        ((TextView) dialog.findViewById(R.id.ll_yes_txt)).setText(getString(R.string.mbc_grant));
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FilesGridActivity.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
                FilesGridActivity.this.deleteTask.execute(new String[0]);
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FilesGridActivity.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
                FilesGridActivity filesGridActivity = FilesGridActivity.this;
                filesGridActivity.showdialog_sdcard(filesGridActivity, filesGridActivity);
            }
        });
        dialog.show();
    }

    @RequiresApi(api = 21)
    public DELETION permissionBasedDeletion() {
        boolean deleteFile;
        int i = 0;
        this.notDeleted = 0;
        Util.appendLogmobiclean(TAG, "method permissionBasedDeletion()", GlobalData.FILE_NAME);
        try {
            try {
                int size = this.mediaList.filesList.size();
                int i2 = 0;
                while (i < size) {
                    BigSizeFilesWrapper bigSizeFilesWrapper = this.mediaList.filesList.get(i);
                    if (bigSizeFilesWrapper.ischecked) {
                        File file = new File(bigSizeFilesWrapper.path);
                        if (file.delete()) {
                            deleteFile = !file.exists();
                        } else {
                            deleteFile = FileUtil.deleteFile(this, file);
                        }
                        if (!deleteFile) {
                            deleteFile = FileUtil.deleteFile(this, file);
                        }
                        if (deleteFile) {
                            i2++;
                            this.displayProgress.setProgress(i2);
                            Util.appendLogmobiclean(TAG, "permission deletion success with path===" + file.getAbsolutePath(), GlobalData.FILE_NAME);
                            updateMediaScannerPath(file);
                            this.mediaList.deleteNode(bigSizeFilesWrapper);
                            this.mediaList.filesList.remove(i);
                            size--;
                        } else {
                            Util.appendLogmobiclean(TAG, "permission deletion failed with path===" + file.getAbsolutePath(), GlobalData.FILE_NAME);
                            this.notDeleted = this.notDeleted + 1;
                            i++;
                        }
                        MobiClean.getInstance().spaceManagerModule.recoveredSize += bigSizeFilesWrapper.size;
                    } else {
                        i++;
                    }
                }
                return DELETION.SUCCESS;
            } catch (Exception e) {
                Util.appendLogmobiclean(TAG, "Permission deletion exception====" + e.getMessage(), GlobalData.FILE_NAME);
                DELETION deletion = DELETION.ERROR;
                return DELETION.SUCCESS;
            }
        } catch (Throwable unused) {
            return DELETION.SUCCESS;
        }
    }

    private void redirectToNoti() {
        this.redirectToNoti = getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false);
    }

    private void refresh(long j, String str) {
        this.mediaList.filesList.clear();
        this.mediaList.unSelectAll();
        updateDeleteButtonTitle();
        updateSelectedCount();
        for (int i = 0; i < this.mediaList.arrContents.size(); i++) {
            if (this.mediaList.arrContents.get(i).size >= j) {
                MediaList mediaList = this.mediaList;
                mediaList.filesList.add(mediaList.arrContents.get(i));
            }
        }
        setAdapters();
        ((TextView) findViewById(R.id.toolbar_title)).setText(this.mediaList.selectedCount + DialogConfigs.DIRECTORY_SEPERATOR + this.mediaList.filesList.size() + " " + str);
        if (this.mediaList.filesList.size() == 0) {
            this.deleteBtn.setEnabled(false);

            this.deleteBtn.setAlpha(0.5f);
            return;
        }
        this.deleteBtn.setEnabled(true);
        this.deleteBtn.setAlpha(1.0f);
    }

    private void setAdapters() {
        Util.appendLogmobiclean(TAG, " method setAdapters Calling ", GlobalData.FILE_NAME);
        switch (AnonymousClass22.f5164a[this.mediaList.mediaType.ordinal()]) {
            case 1:
            case 2:
                this.o = new ImageAdapter(this);
                int i = 0;
                while (true) {
                    Integer[] numArr = this.mHeaderPositions;
                    if (i < numArr.length) {
                        this.sections.add(new SimpleSectionedListAdapter.Section(numArr[i].intValue(), this.mHeaderNames[i]));
                        i++;
                    } else {
                        this.listviewImages.setAdapter((ListAdapter) new SimpleSectionedListAdapter(this, this.o, R.layout.list_item_header, R.id.header));
                        break;
                    }
                }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                this.listviewImages.setVisibility(View.GONE);
                FilesAdapter filesAdapter = new FilesAdapter(this);
                this.p = filesAdapter;
                this.listview.setAdapter((ListAdapter) filesAdapter);
                break;
        }
        updateSelectedCount();
    }

    private void setArrays() {
        int[] iArr = new int[this.mediaList.filesList.size()];
        for (int i = 0; i < this.mediaList.filesList.size(); i++) {
            iArr[i] = this.mediaList.filesList.get(i).id;
        }
    }

    private void setListners() {
        Util.appendLogmobiclean(TAG, " method setListners Calling ", GlobalData.FILE_NAME);
        Log.e(TAG, "setListners() called");
        this.deleteBtn.setOnClickListener(this);
        this.seekbar = (SeekBar) findViewById(R.id.seekBar);
        this.tv_size = (TextView) findViewById(R.id.tv_size);
        this.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.mobiclean.phoneclean.tools.FilesGridActivity.1
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Log.e(FilesGridActivity.TAG, "onProgressChanged called");
                switch (AnonymousClass22.f5164a[FilesGridActivity.this.mediaList.mediaType.ordinal()]) {
                    case 1:
                        Log.e(FilesGridActivity.TAG, "setListeners called : handleImagesSize" + i);
                        FilesGridActivity.this.handleImagesSize(i);
                        return;
                    case 2:
                        FilesGridActivity.this.handleVideosSize(i);
                        return;
                    case 3:
                        FilesGridActivity.this.handleAudiosSize(i);
                        return;
                    case 4:
                        FilesGridActivity.this.handleDocumentSize(i);
                        return;
                    case 5:
                        FilesGridActivity.this.handleOthersSize(i);
                        return;
                    case 6:
                        FilesGridActivity.this.handleApkSize(i);
                        return;
                    case 7:
                        FilesGridActivity.this.handleAllSize(i);
                        return;
                    default:
                        return;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int i = AnonymousClass22.f5164a[FilesGridActivity.this.mediaList.mediaType.ordinal()];
                if (i == 1) {
                    FilesGridActivity.this.sharedPrefUtil.saveCheckBoxState("IMAGE_CHECKBOX", FilesGridActivity.this.default_chkBox.isChecked());
                    SharedPrefUtil sharedPrefUtil = FilesGridActivity.this.sharedPrefUtil;
                    sharedPrefUtil.saveString("IMAGE_SETTING", "" + FilesGridActivity.this.seekbar.getProgress());
                } else if (i == 2) {
                    FilesGridActivity.this.sharedPrefUtil.saveCheckBoxState("VIDEO_CHECKBOX", FilesGridActivity.this.default_chkBox.isChecked());
                    SharedPrefUtil sharedPrefUtil2 = FilesGridActivity.this.sharedPrefUtil;
                    sharedPrefUtil2.saveString("VIDEO_SETTING", "" + FilesGridActivity.this.seekbar.getProgress());
                } else if (i == 3) {
                    FilesGridActivity.this.sharedPrefUtil.saveCheckBoxState("AUDIO_CHECKBOX", FilesGridActivity.this.default_chkBox.isChecked());
                    SharedPrefUtil sharedPrefUtil3 = FilesGridActivity.this.sharedPrefUtil;
                    sharedPrefUtil3.saveString("AUDIO_SETTING", "" + FilesGridActivity.this.seekbar.getProgress());
                } else if (i == 4) {
                    FilesGridActivity.this.sharedPrefUtil.saveCheckBoxState("DOCUMENT_CHECKBOX", FilesGridActivity.this.default_chkBox.isChecked());
                    SharedPrefUtil sharedPrefUtil4 = FilesGridActivity.this.sharedPrefUtil;
                    sharedPrefUtil4.saveString("DOCUMENT_SETTING", "" + FilesGridActivity.this.seekbar.getProgress());
                } else if (i == 5) {
                    FilesGridActivity.this.sharedPrefUtil.saveCheckBoxState("OTHER_CHECKBOX", FilesGridActivity.this.default_chkBox.isChecked());
                    SharedPrefUtil sharedPrefUtil5 = FilesGridActivity.this.sharedPrefUtil;
                    sharedPrefUtil5.saveString("OTHER_SETTING", "" + FilesGridActivity.this.seekbar.getProgress());
                } else if (i != 7) {
                } else {
                    FilesGridActivity.this.sharedPrefUtil.saveCheckBoxState("ALL_CHECKBOX", FilesGridActivity.this.default_chkBox.isChecked());
                    SharedPrefUtil sharedPrefUtil6 = FilesGridActivity.this.sharedPrefUtil;
                    sharedPrefUtil6.saveString("ALL_CHECKBOX", "" + FilesGridActivity.this.seekbar.getProgress());
                }
            }
        });
        this.default_chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    FilesGridActivity.this.seekbar.setProgress(FilesGridActivity.this.getDefaultProgress());
                }
            }
        });
        switch (AnonymousClass22.f5164a[this.mediaList.mediaType.ordinal()]) {
            case 1:
                if (this.sharedPrefUtil.getString("IMAGE_SETTING") != null) {
                    this.default_chkBox.setChecked(this.sharedPrefUtil.getCheckBoxState("IMAGE_CHECKBOX"));
                    this.seekbar.setProgress(0);
                    return;
                }
                this.seekbar.setProgress(this.defaultProgress);
                return;
            case 2:
                if (this.sharedPrefUtil.getString("VIDEO_SETTING") != null) {
                    this.default_chkBox.setChecked(this.sharedPrefUtil.getCheckBoxState("VIDEO_CHECKBOX"));
                    this.seekbar.setProgress(0);
                    return;
                }
                this.seekbar.setProgress(this.defaultProgress);
                return;
            case 3:
                if (this.sharedPrefUtil.getString("AUDIO_SETTING") != null) {
                    this.default_chkBox.setChecked(this.sharedPrefUtil.getCheckBoxState("AUDIO_CHECKBOX"));
                    this.seekbar.setProgress(0);
                    return;
                }
                this.seekbar.setProgress(this.defaultProgress);
                return;
            case 4:
                if (this.sharedPrefUtil.getString("DOCUMENT_SETTING") != null) {
                    this.default_chkBox.setChecked(this.sharedPrefUtil.getCheckBoxState("DOCUMENT_CHECKBOX"));
                    this.seekbar.setProgress(0);
                    return;
                }
                this.seekbar.setProgress(this.defaultProgress);
                return;
            case 5:
                if (this.sharedPrefUtil.getString("OTHER_SETTING") != null) {
                    this.default_chkBox.setChecked(this.sharedPrefUtil.getCheckBoxState("OTHER_CHECKBOX"));
                    this.seekbar.setProgress(0);
                    return;
                }
                this.seekbar.setProgress(this.defaultProgress);
                return;
            case 6:
                if (this.sharedPrefUtil.getString("APK_SETTING") != null) {
                    this.default_chkBox.setChecked(this.sharedPrefUtil.getCheckBoxState("APK_SETTING"));
                    this.seekbar.setProgress(0);
                    return;
                }
                this.seekbar.setProgress(this.defaultProgress);
                return;
            case 7:
                if (this.sharedPrefUtil.getString("ALL_SETTING") != null) {
                    this.default_chkBox.setChecked(this.sharedPrefUtil.getCheckBoxState("ALL_CHECKBOX"));
                    this.seekbar.setProgress(0);
                    return;
                }
                this.seekbar.setProgress(this.defaultProgress);
                return;
            default:
                return;
        }
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

    private void setTitle() {
        Util.appendLogmobiclean(TAG, " method setTitle Calling ", GlobalData.FILE_NAME);
        switch (AnonymousClass22.f5164a[this.mediaList.mediaType.ordinal()]) {
            case 1:
                this.tvview.setText(R.string.mbc_notfound_photo);
                return;
            case 2:
                this.tvview.setText(R.string.mbc_notfound_vidio);
                return;
            case 3:
                this.tvview.setText(R.string.mbc_notfound_audio);
                return;
            case 4:
                this.tvview.setText(R.string.mbc_notfound_document);
                return;
            case 5:
                this.tvview.setText(R.string.mbc_notfound_data);
                return;
            case 6:
                this.tvview.setText(R.string.mbc_notfound_apk);
                return;
            case 7:
                this.tvview.setText(R.string.mbc_notfound_apk);
                return;
            default:
                return;
        }
    }

    private void showCustomDialog() {
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
        ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_junk_cleaner));
        View space;

        ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_junk_cleaner));
        if (!this.fromToolBox) {
            ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_large_files));
        } else {
            ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_large_files));
        }
        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(getResources().getString(R.string.mbc_delete_large_file));
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (FilesGridActivity.this.multipleClicked()) {
                    return;
                }
                Util.appendLogmobiclean(FilesGridActivity.TAG, "method showCustomDialog calling cancel press", GlobalData.FILE_NAME);
                dialog.dismiss();
                FilesGridActivity.this.deleteBtn.setEnabled(true);
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FilesGridActivity.this.multipleClicked()) {
                    return;
                }
                FilesGridActivity.this.deleteBtn.setEnabled(false);
                Util.appendLogmobiclean(FilesGridActivity.TAG, "method showCustomDialog calling continue press", GlobalData.FILE_NAME);
                FilesGridActivity.this.deletionTask();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showFilterDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setContentView(R.layout.dialog_size_filter);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(-1, -1);
        dialog.getWindow().setGravity(17);
        dialog.show();
        dialog.findViewById(R.id.tabLayout).setVisibility(View.GONE);
        View space;

        final SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.seekBar);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.default_chkBox);
        this.tv_size = (TextView) dialog.findViewById(R.id.tv_size);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
                int i2 = AnonymousClass22.f5164a[FilesGridActivity.this.mediaList.mediaType.ordinal()];
                if (i2 == 1) {
                    Log.e(FilesGridActivity.TAG, " seekBar called : handleImagesSize");
                    FilesGridActivity.this.handleImagesSize(i);
                } else if (i2 == 2) {
                    FilesGridActivity.this.handleVideosSize(i);
                } else if (i2 == 3) {
                    FilesGridActivity.this.handleAudiosSize(i);
                } else if (i2 == 4) {
                    FilesGridActivity.this.handleDocumentSize(i);
                } else if (i2 == 5) {
                    FilesGridActivity.this.handleOthersSize(i);
                } else if (i2 != 7) {
                } else {
                    FilesGridActivity.this.handleAllSize(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar2) {
            }
        });
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return checkBox.isChecked();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    seekBar.setProgress(FilesGridActivity.this.getDefaultProgress());
                }
            }
        });
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FilesGridActivity.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FilesGridActivity.this.multipleClicked()) {
                    return;
                }
                int i = AnonymousClass22.f5164a[FilesGridActivity.this.mediaList.mediaType.ordinal()];
                if (i == 1) {
                    FilesGridActivity.this.sharedPrefUtil.saveCheckBoxState("IMAGE_CHECKBOX", checkBox.isChecked());
                    SharedPrefUtil sharedPrefUtil = FilesGridActivity.this.sharedPrefUtil;
                    sharedPrefUtil.saveString("IMAGE_SETTING", "" + seekBar.getProgress());
                } else if (i == 2) {
                    FilesGridActivity.this.sharedPrefUtil.saveCheckBoxState("VIDEO_CHECKBOX", checkBox.isChecked());
                    SharedPrefUtil sharedPrefUtil2 = FilesGridActivity.this.sharedPrefUtil;
                    sharedPrefUtil2.saveString("VIDEO_SETTING", "" + seekBar.getProgress());
                } else if (i == 3) {
                    FilesGridActivity.this.sharedPrefUtil.saveCheckBoxState("AUDIO_CHECKBOX", checkBox.isChecked());
                    SharedPrefUtil sharedPrefUtil3 = FilesGridActivity.this.sharedPrefUtil;
                    sharedPrefUtil3.saveString("AUDIO_SETTING", "" + seekBar.getProgress());
                } else if (i == 4) {
                    FilesGridActivity.this.sharedPrefUtil.saveCheckBoxState("DOCUMENT_CHECKBOX", checkBox.isChecked());
                    SharedPrefUtil sharedPrefUtil4 = FilesGridActivity.this.sharedPrefUtil;
                    sharedPrefUtil4.saveString("DOCUMENT_SETTING", "" + seekBar.getProgress());
                } else if (i == 5) {
                    FilesGridActivity.this.sharedPrefUtil.saveCheckBoxState("OTHER_CHECKBOX", checkBox.isChecked());
                    SharedPrefUtil sharedPrefUtil5 = FilesGridActivity.this.sharedPrefUtil;
                    sharedPrefUtil5.saveString("OTHER_SETTING", "" + seekBar.getProgress());
                } else if (i == 7) {
                    FilesGridActivity.this.sharedPrefUtil.saveCheckBoxState("ALL_CHECKBOX", checkBox.isChecked());
                    SharedPrefUtil sharedPrefUtil6 = FilesGridActivity.this.sharedPrefUtil;
                    sharedPrefUtil6.saveString("ALL_SETTING", "" + seekBar.getProgress());
                }
                dialog.dismiss();
            }
        });
        switch (AnonymousClass22.f5164a[this.mediaList.mediaType.ordinal()]) {
            case 1:
                if (this.sharedPrefUtil.getString("IMAGE_SETTING") != null) {
                    checkBox.setChecked(this.sharedPrefUtil.getCheckBoxState("IMAGE_CHECKBOX"));
                    seekBar.setProgress(0);
                    return;
                }
                seekBar.setProgress(this.defaultProgress);
                return;
            case 2:
                if (this.sharedPrefUtil.getString("VIDEO_SETTING") != null) {
                    checkBox.setChecked(this.sharedPrefUtil.getCheckBoxState("VIDEO_CHECKBOX"));
                    seekBar.setProgress(0);
                    return;
                }
                seekBar.setProgress(this.defaultProgress);
                return;
            case 3:
                if (this.sharedPrefUtil.getString("AUDIO_SETTING") != null) {
                    checkBox.setChecked(this.sharedPrefUtil.getCheckBoxState("AUDIO_CHECKBOX"));
                    seekBar.setProgress(0);
                    return;
                }
                seekBar.setProgress(this.defaultProgress);
                return;
            case 4:
                if (this.sharedPrefUtil.getString("DOCUMENT_SETTING") != null) {
                    checkBox.setChecked(this.sharedPrefUtil.getCheckBoxState("DOCUMENT_CHECKBOX"));
                    seekBar.setProgress(0);
                    return;
                }
                seekBar.setProgress(this.defaultProgress);
                return;
            case 5:
                if (this.sharedPrefUtil.getString("OTHER_SETTING") != null) {
                    checkBox.setChecked(this.sharedPrefUtil.getCheckBoxState("OTHER_CHECKBOX"));
                    seekBar.setProgress(0);
                    return;
                }
                seekBar.setProgress(this.defaultProgress);
                return;
            case 6:
                if (this.sharedPrefUtil.getString("APK_SETTING") != null) {
                    checkBox.setChecked(this.sharedPrefUtil.getCheckBoxState("APK_SETTING"));
                    this.seekbar.setProgress(0);
                    return;
                }
                this.seekbar.setProgress(this.defaultProgress);
                return;
            case 7:
                if (this.sharedPrefUtil.getString("ALL_SETTING") != null) {
                    checkBox.setChecked(this.sharedPrefUtil.getCheckBoxState("ALL_SETTING"));
                    seekBar.setProgress(0);
                    return;
                }
                seekBar.setProgress(this.defaultProgress);
                return;
            default:
                return;
        }
    }

    private void sortListByDate() {
        try {
            Collections.sort(this.mediaList.filesList, new Comparator<BigSizeFilesWrapper>() {
                @Override
                public int compare(BigSizeFilesWrapper bigSizeFilesWrapper, BigSizeFilesWrapper bigSizeFilesWrapper2) {
                    return Long.compare(bigSizeFilesWrapper2.dateTaken, bigSizeFilesWrapper.dateTaken);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageAdapter imageAdapter = this.o;
        if (imageAdapter != null) {
            imageAdapter.notifyDataSetChanged();
        }
        FilesAdapter filesAdapter = this.p;
        if (filesAdapter != null) {
            filesAdapter.notifyDataSetChanged();
        }
    }

    private void sortListByName() {
        try {
            Collections.sort(this.mediaList.filesList, new Comparator<BigSizeFilesWrapper>() {
                @Override
                public int compare(BigSizeFilesWrapper bigSizeFilesWrapper, BigSizeFilesWrapper bigSizeFilesWrapper2) {
                    return bigSizeFilesWrapper.name.trim().compareToIgnoreCase(bigSizeFilesWrapper2.name.trim());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageAdapter imageAdapter = this.o;
        if (imageAdapter != null) {
            imageAdapter.notifyDataSetChanged();
        }
        FilesAdapter filesAdapter = this.p;
        if (filesAdapter != null) {
            filesAdapter.notifyDataSetChanged();
        }
    }

    private void sortListBySize() {
        try {
            Collections.sort(this.mediaList.filesList, new Comparator<BigSizeFilesWrapper>() {
                @Override
                public int compare(BigSizeFilesWrapper bigSizeFilesWrapper, BigSizeFilesWrapper bigSizeFilesWrapper2) {
                    return Long.compare(bigSizeFilesWrapper2.size, bigSizeFilesWrapper.size);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageAdapter imageAdapter = this.o;
        if (imageAdapter != null) {
            imageAdapter.notifyDataSetChanged();
        }
        FilesAdapter filesAdapter = this.p;
        if (filesAdapter != null) {
            filesAdapter.notifyDataSetChanged();
        }
    }

    public void successDeletion() {
        Intent intent = new Intent(this, CommonResultActivity.class);
        intent.putExtra("DATA", "" + Util.convertBytes(this.mediaList.recoveredSize));
        intent.putExtra("TYPE", "Recovered");
        intent.putExtra("FROMLARGE", true);
        intent.putExtra("not_deleted", this.notDeleted);
        if (this.redirectToNoti) {
            intent.putExtra(GlobalData.REDIRECTNOTI, false);
        }
        this.resumedFromClick = true;
        GlobalData.afterDelete = true;

            startActivity(intent);


    }

    public void updateDeleteButtonTitle() {
        MediaList mediaList = this.mediaList;
        if (mediaList != null) {
            if (mediaList.selectedSize == 0) {
                this.tv_file_delete_text.setText("Delete");
            } else {
                this.tv_file_delete_text.setText(String.format(getResources().getString(R.string.mbc_deletee).replace("DO_NOT_TRANSLATE", "%s"), Util.convertBytes(this.mediaList.selectedSize)));
            }
        }
    }

    private void updateMediaScannerPath(File file) {
        MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String str, Uri uri) {
            }
        });
    }


    public void updateSelectedCount() {
        String str;
        switch (AnonymousClass22.f5164a[this.mediaList.mediaType.ordinal()]) {
            case 1:
                str = this.mediaList.selectedCount + DialogConfigs.DIRECTORY_SEPERATOR + this.mediaList.filesList.size() + " " + getResources().getString(R.string.mbc_viewmore_image);
                break;
            case 2:
                str = this.mediaList.selectedCount + DialogConfigs.DIRECTORY_SEPERATOR + this.mediaList.filesList.size() + " " + getResources().getString(R.string.mbc_viewmore_video);
                break;
            case 3:
                str = this.mediaList.selectedCount + DialogConfigs.DIRECTORY_SEPERATOR + this.mediaList.filesList.size() + " " + getResources().getString(R.string.mbc_viewmore_audio);
                break;
            case 4:
                str = this.mediaList.selectedCount + DialogConfigs.DIRECTORY_SEPERATOR + this.mediaList.filesList.size() + " " + getResources().getString(R.string.mbc_viewmore_document);
                break;
            case 5:
                str = this.mediaList.selectedCount + DialogConfigs.DIRECTORY_SEPERATOR + this.mediaList.filesList.size() + " " + getResources().getString(R.string.mbc_myothers);
                break;
            case 6:
                str = this.mediaList.selectedCount + DialogConfigs.DIRECTORY_SEPERATOR + this.mediaList.filesList.size() + " " + getResources().getString(R.string.mbc_apk_files);
                break;
            case 7:
                str = this.mediaList.selectedCount + DialogConfigs.DIRECTORY_SEPERATOR + this.mediaList.filesList.size() + " " + getResources().getString(R.string.files);
                break;
            default:
                str = "";
                break;
        }
        ((TextView) findViewById(R.id.toolbar_title)).setText(str);
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

    public int getDefaultProgress() {
        return 0;
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2415 && i2 == -1) {
            deletion(i, i2, intent);
        }
    }

    @Override
    public void onBackPressed() {
        Util.appendLogmobiclean(TAG, "ON BACK PRESS", GlobalData.FILE_NAME);
        if (this.k.getVisibility() == View.VISIBLE) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (multipleClicked()) {
            return;
        }
        int id = view.getId();
        if (id == R.id.ads_btn_cancel) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
            this.k.setVisibility(View.GONE);
            this.j.setVisibility(View.VISIBLE);
            this.deleteBtn.setEnabled(true);
        } else if (id == R.id.ads_btn_countinue) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
            this.k.setVisibility(View.GONE);
            this.j.setVisibility(View.VISIBLE);
            this.deleteBtn.setEnabled(false);
            Util.appendLogmobiclean(TAG, "method showCustomDialog calling continue press", GlobalData.FILE_NAME);
            deletionTask();
        } else if (id != R.id.btnfiledelete) {
        } else {
            Util.appendLogmobiclean(TAG, " btnfiledelete Button Calling Before for Loop", GlobalData.FILE_NAME);
            if (this.mediaList.selectedCount == 0) {
                Util.appendLogmobiclean(TAG, "Please select at least one item to delete.", GlobalData.FILE_NAME);
                Toast.makeText(this, getResources().getString(R.string.mbc_at_leastone), Toast.LENGTH_SHORT).show();
                return;
            }
            showCustomDialog();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_grid);
 setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        initControls();
        try {
            redirectToNoti();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.resumedFromClick = false;
        try {
            MediaList mediaList = MobiClean.getInstance().spaceManagerModule.currentList;
            this.mediaList = mediaList;
            mediaList.filesList = new ArrayList<>();
            this.mediaList.refresh();
            MediaList mediaList2 = this.mediaList;
            mediaList2.filesList.addAll(mediaList2.arrContents);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.fromToolBox = getIntent().getBooleanExtra("fromToolBox", false);
        Util.appendLogmobiclean(TAG, " onCreate() Calling ", GlobalData.FILE_NAME);
        GlobalData.afterDelete = false;
        try {
            setArrays();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        GetDeviceDimensions();
        try {
            updateDeleteButtonTitle();
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        switch (AnonymousClass22.f5164a[this.mediaList.mediaType.ordinal()]) {
            case 1:
            case 2:
                this.listview.setVisibility(View.GONE);
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                this.listview.setVisibility(View.VISIBLE);
                break;
        }
        if (this.mediaList.filesList.size() == 0) {
            this.tvview.setVisibility(View.VISIBLE);
            this.listview.setVisibility(View.GONE);
            this.listviewImages.setVisibility(View.GONE);
            this.deleteBtn.setVisibility(View.GONE);
        }
        setTitle();
        setListners();
        setAdapters();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.large_files_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_markall:
                MediaList mediaList = this.mediaList;
                mediaList.selectAll(mediaList.filesList);
                updateDeleteButtonTitle();
                updateSelectedCount();
                setAdapters();
                return true;
            case R.id.action_settings:
                showFilterDialog();
                return true;
            case R.id.action_sortByDate:
                sortListByDate();
                return true;
            case R.id.action_sortByName:
                sortListByName();
                return true;
            case R.id.action_sortBySize:
                sortListBySize();
                return true;
            case R.id.action_unmarkall:
                this.mediaList.unSelectAll();
                updateDeleteButtonTitle();
                updateSelectedCount();
                setAdapters();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onResume() {
        ImageAdapter imageAdapter = this.o;
        if (imageAdapter != null) {
            imageAdapter.notifyDataSetChanged();
            updateDeleteButtonTitle();
            updateSelectedCount();
        }
        FilesAdapter filesAdapter = this.p;
        if (filesAdapter != null) {
            filesAdapter.notifyDataSetChanged();
            updateDeleteButtonTitle();
            updateSelectedCount();
        }
        RelativeLayout button = this.deleteBtn;
        if (button != null) {
            button.setEnabled(true);
        }
        super.onResume();
    }

    public void showdialog_sdcard(Context context, final DialogListners dialogListners) {
        Util.appendLogmobicleanbug(TAG, "in sd card dialog method");
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
        }
        dialog.setContentView(R.layout.dialog_sdcard);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(-1, -1);
        dialog.getWindow().setGravity(17);
        ((TextView) dialog.findViewById(R.id.ll_no_txt)).setText(context.getString(R.string.mbc_notnow));
        ((TextView) dialog.findViewById(R.id.ll_yes_txt)).setText(context.getString(R.string.mbc_continues));
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.appendLogmobicleanbug(FilesGridActivity.TAG, "sd card dialog dismiss");
                FilesGridActivity.this.deleteBtn.setEnabled(true);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Util.appendLogmobicleanbug(FilesGridActivity.TAG, "sd card ok click");
                dialogListners.clickOK();
            }
        });
        dialog.show();
    }
}
