package com.cleanPhone.mobileCleaner.tools;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;


import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.JunkDeleteAnimationScreen;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.SocialAnimationActivity;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.similerphotos.AsyncTask;
import com.cleanPhone.mobileCleaner.socialmedia.MediaList;
import com.cleanPhone.mobileCleaner.socialmedia.SocialMedia;
import com.cleanPhone.mobileCleaner.utility.DetermineTextSize;
import com.cleanPhone.mobileCleaner.utility.FileUtil;
import com.cleanPhone.mobileCleaner.utility.FilesListActivity;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.MediaStoreUtil;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.BigSizeFilesWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SocialCleanerListActivity extends ParentActivity implements View.OnClickListener {
    public static int fillCount;
    public static long totalCleanSpace;
    private String TAG = "SocialCleanerListActivity";
    private ExpandableListAdapter adapter;
    private int deviceHeight;
    private int deviceWidth;
    private DisplayMetrics displaymetrics;
    public Context i;
    private boolean isComingFromDelete;
    private boolean noti_result_back;
    private boolean redirectToNoti;
    private TextView unitTv;
    public static ArrayList<BigSizeFilesWrapper> whimagelist = new ArrayList<>();
    public static ArrayList<BigSizeFilesWrapper> whvideolist = new ArrayList<>();
    public static ArrayList<BigSizeFilesWrapper> whaudioslist = new ArrayList<>();
    public static ArrayList<BigSizeFilesWrapper> whdoclist = new ArrayList<>();
    public static ArrayList<BigSizeFilesWrapper> fbimagelist = new ArrayList<>();
    public static ArrayList<BigSizeFilesWrapper> twimagelist = new ArrayList<>();

    public class CleanSocialData extends AsyncTask<String, Integer, String> {
        public ProgressDialog f5230a;

        public CleanSocialData() {
        }

        private boolean isDeleted(BigSizeFilesWrapper bigSizeFilesWrapper) {
            if (bigSizeFilesWrapper.file.exists()) {
                bigSizeFilesWrapper.file.delete();
                return !bigSizeFilesWrapper.file.exists();
            }
            return true;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            Util.appendLogmobiclean(SocialCleanerListActivity.this.TAG, "CleanSocialData-----onPreExecute", GlobalData.FILE_NAME);
            Iterator<SocialMedia> it = MobiClean.getInstance().socialModule.arrContents.iterator();
            int i = 0;
            while (it.hasNext()) {
                for (MediaList mediaList : it.next().arrContents) {
                    i += mediaList.selectedCount;
                }
            }
            String str = SocialCleanerListActivity.this.TAG;
            Util.appendLogmobiclean(str, "CleanSocialData-----onPreExecute--------total_selected_count" + i, GlobalData.FILE_NAME);
            this.f5230a = new ProgressDialog(SocialCleanerListActivity.this);
            SocialCleanerListActivity.this.getWindow().addFlags(2097280);
            ProgressDialog progressDialog = this.f5230a;
            progressDialog.setTitle("" + SocialCleanerListActivity.this.getResources().getString(R.string.mbc_cleaning));
            this.f5230a.setCanceledOnTouchOutside(false);
            this.f5230a.setProgressStyle(1);
            this.f5230a.setCancelable(false);
            this.f5230a.setMax(i);
            this.f5230a.show();
        }

        @Override
        public String doInBackground(String... strArr) {
            Util.appendLogmobiclean(SocialCleanerListActivity.this.TAG, "CleanSocialData-----doInBackground", GlobalData.FILE_NAME);
            SocialCleanerListActivity.this.isComingFromDelete = true;
            MobiClean.getInstance().socialModule.recoveredSize = 0L;
            Iterator<SocialMedia> it = MobiClean.getInstance().socialModule.arrContents.iterator();
            char c2 = 0;
            int i = 0;
            while (it.hasNext()) {
                for (MediaList mediaList : it.next().arrContents) {
                    int size = mediaList.arrContents.size();
                    int i2 = 0;
                    while (i2 < size) {
                        BigSizeFilesWrapper bigSizeFilesWrapper = mediaList.arrContents.get(i2);
                        if (bigSizeFilesWrapper.ischecked) {
                            if (isDeleted(bigSizeFilesWrapper)) {
                                mediaList.selectedCount--;
                                long j = mediaList.selectedSize;
                                long j2 = bigSizeFilesWrapper.size;
                                mediaList.selectedSize = j - j2;
                                mediaList.totalCount--;
                                mediaList.totalSize -= j2;
                                mediaList.arrContents.remove(i2);
                                size--;
                                i++;
                                Integer[] numArr = new Integer[1];
                                numArr[c2] = Integer.valueOf(i);
                                publishProgress(numArr);
                                SocialCleanerListActivity.this.updateMediaScannerPath(bigSizeFilesWrapper.file);
                            } else {
                                i2++;
                            }
                            MobiClean.getInstance().socialModule.recoveredSize += bigSizeFilesWrapper.size;
                            i = i;
                        } else {
                            i2++;
                        }
                        c2 = 0;
                    }
                }
            }
            return MobiClean.getInstance().socialModule.recoveredSize > 0 ? Util.convertBytes(MobiClean.getInstance().socialModule.recoveredSize) : "";
        }

        @Override
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            Util.appendLogmobiclean(SocialCleanerListActivity.this.TAG, "CleanSocialData-----onPostExecute", GlobalData.FILE_NAME);
            if (this.f5230a.isShowing()) {
                this.f5230a.dismiss();
            }
            try {
                SocialCleanerListActivity.this.getWindow().clearFlags(2097280);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(str)) {
                return;
            }
            LargeFile.totalSocialSize = 0L;
            SocialCleanerListActivity.this.startActivity(new Intent(SocialCleanerListActivity.this, JunkDeleteAnimationScreen.class).putExtra("TYPE", "Social").putExtra("DATA", str));
        }

        @Override
        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate(numArr);
            this.f5230a.setProgress(numArr[0].intValue());
        }
    }

    public class Multi2 extends Thread {
        @Override
        public void run() {
            System.out.println("thread is running...");
        }
    }

    public class Multi4 extends Thread {
        @Override
        public void run() {
            System.out.println("thread is running...");
        }
    }


    public Drawable getADrawable(int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return getResources().getDrawable(i, getTheme());
        }
        return getResources().getDrawable(i);
    }

    private String getKeyword(int i) {
        return "whatsapp";
    }

    private void redirectToNoti() {
        this.noti_result_back = getIntent().getBooleanExtra(GlobalData.NOTI_RESULT_BACK, false);
        this.redirectToNoti = getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false);
    }

    private void setDeviceDimensions() {
        if (this.displaymetrics == null) {
            this.displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(this.displaymetrics);
        }
    }

    private void showConfirmDialog() {
        Util.appendLogmobiclean(this.TAG, "showConfirmDialog--- clean item ", GlobalData.FILE_NAME);
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
         ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_social_cleaner));
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_turbo_cleaner));
        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(getResources().getString(R.string.mbc_delete_large_file));
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SocialCleanerListActivity.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SocialCleanerListActivity.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
                new CleanSocialData().execute(new String[0]);
            }
        });
        dialog.show();
    }


    public void updateMediaScannerPath(File file) {
        if (FileUtil.isKitKat()) {
            Uri uriFromFile = MediaStoreUtil.getUriFromFile(this, file.getAbsolutePath());
            if (uriFromFile != null) {
                getContentResolver().delete(uriFromFile, null, null);
            }
        } else if (!FileUtil.isSystemAndroid5()) {
            MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, null);
        } else {
            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent.setData(Uri.fromFile(file));
            sendBroadcast(intent);
        }
    }


    @Override
    public void onBackPressed() {
        Util.appendLogmobiclean(this.TAG, "CalculateToalSizeTask---AsyncTask---onPreExecute", GlobalData.FILE_NAME);
        GlobalData.backPressedResult = true;
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


         ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_anim_turbo_cleaner));
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_turbo_cleaner));
        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(String.format(getResources().getString(R.string.mbc_simple_back_press), getResources().getString(R.string.mbc_junk_result_txt)));
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SocialCleanerListActivity.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SocialCleanerListActivity.this.multipleClicked()) {
                    return;
                }
                MobiClean.getInstance().socialModule = null;
                System.runFinalization();
                Runtime.getRuntime().runFinalization();
                System.gc();
                dialog.dismiss();
                if (!SocialCleanerListActivity.this.noti_result_back && !SocialCleanerListActivity.this.redirectToNoti) {
                    SocialCleanerListActivity.this.finish();
                    return;
                }
                SocialCleanerListActivity.this.finish();
                SocialCleanerListActivity.this.startActivity(new Intent(SocialCleanerListActivity.this.i, HomeActivity.class));
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (!multipleClicked() && view.getId() == R.id.btncleannow) {
            if (MobiClean.getInstance().socialModule.selectedSize == 0) {
                Toast.makeText(this, "" + getResources().getString(R.string.mbc_at_leastone), Toast.LENGTH_LONG).show();
                return;
            }
            showConfirmDialog();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        this.isComingFromDelete = false;
        setContentView(R.layout.activity_social_cleaner_list);
        Util.saveScreen(getClass().getName(), this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
        Util.appendLogmobiclean(this.TAG, "oncrete call", GlobalData.FILE_NAME);
        this.displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(this.displaymetrics);
        }
        DisplayMetrics displayMetrics = this.displaymetrics;
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
        this.i = this;
        redirectToNoti();
        findViewById(R.id.btncleannow).setOnClickListener(this);

        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(this);
        this.adapter = expandableListAdapter;
        ((ExpandableListView) findViewById(R.id.social_exp_lv)).setAdapter(expandableListAdapter);
        ((TextView) findViewById(R.id.junkdisplay_sizetv)).setText(Util.convertBytes_only(MobiClean.getInstance().socialModule.totalSize));
        ((TextView) findViewById(R.id.junkdisplay_sizetv)).setTextSize(0, DetermineTextSize.determineTextSize(((TextView) findViewById(R.id.junkdisplay_sizetv)).getTypeface(), (this.displaymetrics.heightPixels * 12) / 100));
        this.unitTv = (TextView) findViewById(R.id.junkdisplay_sizetv_unit);
        String convertBytes_unit = Util.convertBytes_unit(MobiClean.getInstance().socialModule.totalSize);
        TextView textView = this.unitTv;
        textView.setText("" + convertBytes_unit);
        this.unitTv.setTextSize(0, (float) DetermineTextSize.determineTextSize(((TextView) findViewById(R.id.junkdisplay_sizetv)).getTypeface(), (float) ((this.displaymetrics.heightPixels * 6) / 100)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Util.appendLogmobiclean(this.TAG, "method onDestroy", GlobalData.FILE_NAME);
        totalCleanSpace = 0L;
        fillCount = 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            GlobalData.backPressedResult = true;
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MobiClean.getInstance().socialModule == null) {
            finish();
        }
        MobiClean.getInstance().socialModule.updateSelf();
        if (this.isComingFromDelete && MobiClean.getInstance().socialModule.totalSize == 0) {
            finish();
            return;
        }
        setDeviceDimensions();
        ExpandableListAdapter expandableListAdapter = this.adapter;
        if (expandableListAdapter != null) {
            expandableListAdapter.notifyDataSetChanged();
        }
        long j = MobiClean.getInstance().socialModule.totalSize;
        if (MobiClean.getInstance().socialModule.selectedSize == 0) {
            ((Button) findViewById(R.id.btncleannow)).setText(getResources().getString(R.string.mbc_clean));
        } else {
            ((Button) findViewById(R.id.btncleannow)).setText(getResources().getString(R.string.mbc_clean) + " " + Util.convertBytes(MobiClean.getInstance().socialModule.selectedSize));
        }
        ((TextView) findViewById(R.id.junkdisplay_sizetv)).setText(Util.convertBytes_only(MobiClean.getInstance().socialModule.totalSize));
        ((TextView) findViewById(R.id.junkdisplay_sizetv)).setTextSize(0, DetermineTextSize.determineTextSize(((TextView) findViewById(R.id.junkdisplay_sizetv)).getTypeface(), (this.displaymetrics.heightPixels * 12) / 100));
        String convertBytes_unit = Util.convertBytes_unit(MobiClean.getInstance().socialModule.totalSize);
        TextView textView = this.unitTv;
        textView.setText("" + convertBytes_unit);
        this.unitTv.setTextSize(0, (float) DetermineTextSize.determineTextSize(((TextView) findViewById(R.id.junkdisplay_sizetv)).getTypeface(), (float) ((this.displaymetrics.heightPixels * 6) / 100)));
    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private HashMap<String, ArrayList<String>> _listDataChild;
        private List<String> _listDataHeader;
        public boolean f5231a;
        public LayoutInflater b;

        public class Multi extends Thread {

            public ExpandableListAdapter f5239a;

            @Override
            public void run() {
                SocialCleanerListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Multi.this.f5239a.notifyDataSetChanged();
                    }
                });
            }
        }

        public class Multi1 extends Thread {
            public ExpandableListAdapter f5241a;

            @Override
            public void run() {
                System.out.println("thread is running2...");
                SocialCleanerListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Multi1.this.f5241a.notifyDataSetChanged();
                    }
                });
            }
        }

        public ExpandableListAdapter(Context context, List<String> list, HashMap<String, ArrayList<String>> hashMap) {
            this.f5231a = false;
            this.b = null;
            this._context = context;
            this._listDataHeader = list;
            this._listDataChild = hashMap;
        }

        @Override
        public Object getChild(int i, int i2) {
            Util.appendLogmobiclean(SocialCleanerListActivity.this.TAG, "ExpandableListAdapter getChild", GlobalData.FILE_NAME);
            if (i2 != 0) {
                if (i2 != 1) {
                    if (i2 != 2) {
                        if (i2 != 3) {
                            return i2 != 4 ? "" : SocialAnimationActivity.FileTypes.GIF.name();
                        }
                        return SocialCleanerListActivity.this.getResources().getString(R.string.mbc_viewmore_document);
                    }
                    return SocialCleanerListActivity.this.getResources().getString(R.string.mbc_viewmore_video);
                }
                return SocialCleanerListActivity.this.getResources().getString(R.string.mbc_myaudios);
            }
            return SocialCleanerListActivity.this.getResources().getString(R.string.mbc_viewmore_image);
        }

        @Override
        public long getChildId(int i, int i2) {
            Util.appendLogmobiclean(SocialCleanerListActivity.this.TAG, "ExpandableListAdapter getChildId", GlobalData.FILE_NAME);
            return i2;
        }

        @Override
        public View getChildView(final int i, final int i2, boolean z, View view, ViewGroup viewGroup) {
            Util.appendLogmobiclean(SocialCleanerListActivity.this.TAG, "ExpandableListAdapter getChildView", GlobalData.FILE_NAME);
            final SocialMedia socialMedia = MobiClean.getInstance().socialModule.arrContents.get(i);
            MediaList mediaList = socialMedia.arrContents.get(i2);
            ArrayList<BigSizeFilesWrapper> arrayList = mediaList.arrContents;
            if (view == null) {
                view = this.b.inflate(R.layout.sociallistitemlayout, (ViewGroup) null);
            }
            if (arrayList.size() == 0) {
                view.setLayoutParams(new AbsListView.LayoutParams(1, 1));
                view.setVisibility(View.GONE);
                view.setBackground(null);
                view.invalidate();
            } else {
                view.setVisibility(View.VISIBLE);
                view.setLayoutParams(new AbsListView.LayoutParams(-1, 0));
                view.invalidate();
            }
            TextView textView = (TextView) view.findViewById(R.id.sociallistitemapp);
            ImageView imageView = (ImageView) view.findViewById(R.id.sociallistitemimage);
            textView.setText((String) getChild(i, mediaList.mediaType.ordinal()));
            ((TextView) view.findViewById(R.id.sociallistitemsize)).setText(mediaList.selectedCount + DialogConfigs.DIRECTORY_SEPERATOR + mediaList.totalCount);
            int ordinal = mediaList.mediaType.ordinal();
            if (ordinal == 0) {
                imageView.setImageDrawable(SocialCleanerListActivity.this.getADrawable(R.drawable.toolbox_images_icon));
            } else if (ordinal == 1) {
                imageView.setImageDrawable(SocialCleanerListActivity.this.getADrawable(R.drawable.toolbox_audio_icon));
            } else if (ordinal == 2) {
                imageView.setImageDrawable(SocialCleanerListActivity.this.getADrawable(R.drawable.toolbox_video_icon));
            } else if (ordinal == 3) {
                imageView.setImageDrawable(SocialCleanerListActivity.this.getADrawable(R.drawable.toolbox_documents_icon));
            } else if (ordinal == 4) {
                imageView.setImageDrawable(SocialCleanerListActivity.this.getADrawable(R.drawable.toolbox_documents_icon));
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    try {
                        MobiClean.getInstance().socialModule.currentList = socialMedia.arrContents.get(i2);
                        Intent intent = new Intent(SocialCleanerListActivity.this, FilesListActivity.class);
                        intent.putExtra("child_position", i2);
                        intent.putExtra("grp_position", i);
                        SocialCleanerListActivity.this.startActivity(intent);
                    } catch (Exception e) {

                    }
                }
            });
            return view;
        }

        @Override
        public int getChildrenCount(int i) {
            Util.appendLogmobiclean(SocialCleanerListActivity.this.TAG, "ExpandableListAdapter getChildrenCount", GlobalData.FILE_NAME);
            return MobiClean.getInstance().socialModule.arrContents.get(i).arrContents.size();
        }

        @Override
        public Object getGroup(int i) {
            Util.appendLogmobiclean(SocialCleanerListActivity.this.TAG, "ExpandableListAdapter getGroup", GlobalData.FILE_NAME);
            if (i != 0) {
                if (i != 1) {
                    if (i != 2) {
                        if (i != 3) {
                            if (i != 4) {
                                return i != 5 ? "" : SocialAnimationActivity.SocialType.Skype.name();
                            }
                            return SocialAnimationActivity.SocialType.Messenger.name();
                        }
                        return SocialAnimationActivity.SocialType.Instagram.name();
                    }
                    return SocialAnimationActivity.SocialType.Twitter.name();
                }
                return SocialAnimationActivity.SocialType.Facebook.name();
            }
            return SocialAnimationActivity.SocialType.WhatsApp.name();
        }

        @Override
        public int getGroupCount() {
            Util.appendLogmobiclean(SocialCleanerListActivity.this.TAG, "ExpandableListAdapter getGroupCount", GlobalData.FILE_NAME);
            return MobiClean.getInstance().socialModule.arrContents.size();
        }

        @Override
        public long getGroupId(int i) {
            Util.appendLogmobiclean(SocialCleanerListActivity.this.TAG, "ExpandableListAdapter getGroupId", GlobalData.FILE_NAME);
            return i;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
            Util.appendLogmobiclean(SocialCleanerListActivity.this.TAG, "ExpandableListAdapter getGroupView", GlobalData.FILE_NAME);
            final SocialMedia socialMedia = MobiClean.getInstance().socialModule.arrContents.get(i);
            String str = socialMedia.name;
            int i2 = 0;
            int i3 = 0;
            for (MediaList mediaList : socialMedia.arrContents) {
                i2 += mediaList.totalCount;
                i3 += mediaList.selectedCount;
            }
            if (view == null) {
                view = this.b.inflate(R.layout.social_list_header, (ViewGroup) null);
            }
            if (i2 == 0) {
                view.setLayoutParams(new AbsListView.LayoutParams(1, 1));
                view.setBackground(null);
                view.setVisibility(View.GONE);
                view.invalidate();
            } else {
                view.setVisibility(View.VISIBLE);
                view.setLayoutParams(new AbsListView.LayoutParams(-1, 0));
                view.invalidate();
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.rightarrow_junk);
            TextView textView = (TextView) view.findViewById(R.id.tvjunkname);
            ((LinearLayout) view.findViewById(R.id.grp_checkcontainer)).setVisibility(View.GONE);
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.check_exp);
            checkBox.setChecked(i3 == i2);
            textView.setText(str);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    Util.appendLogmobiclean(SocialCleanerListActivity.this.TAG, "check setOnClickListener", GlobalData.FILE_NAME);
                    if (ExpandableListAdapter.this.f5231a) {
                        if (checkBox.isChecked()) {
                            socialMedia.selectAll();
                        } else {
                            socialMedia.unSelectAll();
                        }
                        MobiClean.getInstance().socialModule.updateSelf();
                        if (MobiClean.getInstance().socialModule.selectedCount == 0) {
                            ((Button) SocialCleanerListActivity.this.findViewById(R.id.btncleannow)).setText(SocialCleanerListActivity.this.getResources().getString(R.string.mbc_clean));
                        } else {
                            ((Button) SocialCleanerListActivity.this.findViewById(R.id.btncleannow)).setText(SocialCleanerListActivity.this.getResources().getString(R.string.mbc_clean) + " " + Util.convertBytes(MobiClean.getInstance().socialModule.selectedSize) + "");
                        }
                        ExpandableListAdapter.this.notifyDataSetChanged();
                    }
                }
            });
            checkBox.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view2, MotionEvent motionEvent) {
                    ExpandableListAdapter.this.f5231a = true;
                    return false;
                }
            });
            if (z) {
                imageView.animate().rotation(90.0f).start();
            } else {
                imageView.animate().rotation(0.0f).start();
            }
            return view;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int i, int i2) {
            return true;
        }

        public ExpandableListAdapter(Context context) {
            this.f5231a = false;
            this.b = null;
            Util.appendLogmobiclean(SocialCleanerListActivity.this.TAG, "ExpandableListAdapter call", GlobalData.FILE_NAME);
            this._context = context;
            this.b = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    }
}
