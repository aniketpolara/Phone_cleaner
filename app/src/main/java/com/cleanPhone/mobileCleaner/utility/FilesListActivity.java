package com.cleanPhone.mobileCleaner.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.cleanPhone.mobileCleaner.CommonResultActivity;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.socialmedia.MediaList;
import com.cleanPhone.mobileCleaner.tools.IconHolder;
import com.cleanPhone.mobileCleaner.wrappers.BigSizeFilesWrapper;

import java.io.File;
import java.util.ArrayList;

public class FilesListActivity extends CacheActivity implements View.OnClickListener {
    public static String exten_type = "";
    private ImgAdapter adapter;
    private CheckBox checkAll;
    private int childPosition;
    private Button deleteBtn;
    private DisplayMetrics displaymetrics;
    private FileAdapter fileAdapter;
    private FilesAdapter filesAdapter;
    private String filter;
    private int grpPosition;
    public TextView j;
    private ListView listview;
    private GridView listviewImages;
    private ImageAdapter mAdapter;
    private int[] mImageIds;
    private boolean resumedFromClick;
    private TextView tvview;
    private String type;
    private TextView unitTv;
    private ArrayList<SimpleSectionedListAdapter.Section> sections = new ArrayList<>();
    private long totalSelectedSize = 0;
    private long totaldeletedsize = 0;
    private ArrayList<BigSizeFilesWrapper> filesData = new ArrayList<>();
    private boolean isTouched = false;
    private ArrayList<BigSizeFilesWrapper> records = new ArrayList<>();
    private String[] mHeaderNames = {""};
    private Integer[] mHeaderPositions = {0};

    public class FileAdapter extends ArrayAdapter<BigSizeFilesWrapper> {
        public IconHolder f5281a;
        private LayoutInflater mInflater;

        public FileAdapter(Context context) {
            super(context, 0, FilesListActivity.this.records);
            FilesListActivity.this.dip2px(83.33f);
            FilesListActivity.this.dip2px(76.33f);
            IconHolder iconHolder = new IconHolder(context, true, true);
            this.f5281a = iconHolder;
            iconHolder.cleanup();
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            BigSizeFilesWrapper item = getItem(i);
            if (view == null) {
                view = this.mInflater.inflate(R.layout.item_list_social_files, (ViewGroup) null);
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.junklistitemimage);
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.junklistitemcheck);
            ((TextView) view.findViewById(R.id.junklistitemapp)).setText(item.name);
            ((TextView) view.findViewById(R.id.junklistitemsize)).setText(Util.convertBytes(item.size));
            if (item.ischecked) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            final MediaList mediaList = MobiClean.getInstance().socialModule.currentList;
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if (checkBox.isChecked()) {
                        MobiClean.getInstance().socialModule.currentList.selectNodeAtIndex(i);
                    } else {
                        MobiClean.getInstance().socialModule.currentList.unSelectNodeAtIndex(i);
                        FilesListActivity.this.checkAll.setChecked(false);
                    }
                    CheckBox checkBox2 = FilesListActivity.this.checkAll;
                    MediaList mediaList2 = mediaList;
                    checkBox2.setChecked(mediaList2.selectedCount == mediaList2.totalCount);
                    FilesListActivity.this.j.setText(Util.convertBytes_only(mediaList.selectedSize));
                    ((TextView) FilesListActivity.this.findViewById(R.id.tv_social_count)).setText(mediaList.getSelectedTotalString());
                    FilesListActivity.this.totalSelectedSize = mediaList.selectedSize;
                    FilesListActivity.this.unitTv.setText(Util.convertBytes_unit(FilesListActivity.this.totalSelectedSize));
                    Float.parseFloat(String.valueOf(Util.convertBytes_only(FilesListActivity.this.totalSelectedSize)).replace(",", ""));
                }
            });
            if (mediaList.mediaType.ordinal() == 3) {
                imageView.setImageResource(R.drawable.placeholder_doc);
            } else if (mediaList.mediaType.ordinal() == 2) {
                Glide.with((FragmentActivity) FilesListActivity.this).load(item.file).into(imageView);
            } else if (mediaList.mediaType.ordinal() == 4) {
                Glide.with((FragmentActivity) FilesListActivity.this).load(item.file).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.placeholder_audio);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    Intent intent = new Intent();
                    int ordinal = mediaList.mediaType.ordinal();
                    if (ordinal == 1) {
                        intent.setAction("android.intent.action.VIEW");
                        intent.setDataAndType(Uri.parse("file://" + ((BigSizeFilesWrapper) FilesListActivity.this.records.get(i)).path), "audio/*");
                        try {
                            FilesListActivity.this.startActivity(intent);
                        } catch (Exception e) {
                            try {
                                try {
                                    FilesListActivity filesListActivity = FilesListActivity.this;
                                    Uri uriForFile = FileProvider.getUriForFile(filesListActivity, "com.mobiclean.phoneclean.provider", ((BigSizeFilesWrapper) filesListActivity.records.get(i)).file);
                                    Log.d("TTTT", uriForFile.toString() + "");
                                    intent.setDataAndType(uriForFile, "audio/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    FilesListActivity.this.startActivity(intent);
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                                e.printStackTrace();
                            } catch (Exception unused) {
                                FilesListActivity filesListActivity2 = FilesListActivity.this;
                                Toast.makeText(filesListActivity2, "" + FilesListActivity.this.getString(R.string.sorry_no_activity), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (ordinal == 2) {
                        intent.setAction("android.intent.action.VIEW");
                        try {
                            intent.setDataAndType(Uri.parse("file://" + ((BigSizeFilesWrapper) FilesListActivity.this.records.get(i)).path), "video/mp4");
                            FilesListActivity.this.startActivity(intent);
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            FilesListActivity filesListActivity3 = FilesListActivity.this;
                            intent.setDataAndType(FileProvider.getUriForFile(filesListActivity3, "com.mobiclean.phoneclean.provider", ((BigSizeFilesWrapper) filesListActivity3.records.get(i)).file), "video/mp4").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            FilesListActivity.this.startActivity(intent);
                        }
                        FilesListActivity.this.resumedFromClick = true;
                    } else if (ordinal == 3) {
                        try {
                            FilesListActivity filesListActivity4 = FilesListActivity.this;
                            filesListActivity4.openFile(filesListActivity4, ((BigSizeFilesWrapper) filesListActivity4.records.get(i)).file);
                        } catch (Exception e4) {
                            FilesListActivity filesListActivity5 = FilesListActivity.this;
                            Toast.makeText(filesListActivity5, "" + FilesListActivity.this.getString(R.string.sorry_no_activity), Toast.LENGTH_SHORT).show();
                            e4.printStackTrace();
                        }
                    } else if (ordinal != 4) {
                    } else {
                        intent.setAction("android.intent.action.VIEW");
                        try {
                            intent.setDataAndType(Uri.parse("file://" + ((BigSizeFilesWrapper) FilesListActivity.this.records.get(i)).path), "video/mp4");
                            FilesListActivity.this.startActivity(intent);
                        } catch (Exception e5) {
                            e5.printStackTrace();
                            FilesListActivity filesListActivity6 = FilesListActivity.this;
                            intent.setDataAndType(FileProvider.getUriForFile(filesListActivity6, "com.mobiclean.phoneclean.provider", ((BigSizeFilesWrapper) filesListActivity6.records.get(i)).file), "video/mp4").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            FilesListActivity.this.startActivity(intent);
                        }
                        FilesListActivity.this.resumedFromClick = true;
                    }
                }
            });
            return view;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public BigSizeFilesWrapper getItem(int i) {
            return (BigSizeFilesWrapper) FilesListActivity.this.records.get(i);
        }
    }


    public class FilesAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public FilesAdapter(Context context) {
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return FilesListActivity.this.filesData.size();
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
                view = this.mInflater.inflate(R.layout.item_list_social_files, viewGroup, false);
            }
            final ImageView imageView = (ImageView) ViewHolder.get(view, R.id.junklistitemimage);
            TextView textView = (TextView) ViewHolder.get(view, R.id.junklistitemapp);
            TextView textView2 = (TextView) ViewHolder.get(view, R.id.junklistitemsize);
            final CheckBox checkBox = (CheckBox) ViewHolder.get(view, R.id.junklistitemcheck);
            ImageView imageView2 = (ImageView) ViewHolder.get(view, R.id.listiteminfo);
            LinearLayout linearLayout = (LinearLayout) ViewHolder.get(view, R.id.checkcontainer);
            checkBox.setFocusable(false);
            checkBox.setClickable(false);
            try {
                if (!FilesListActivity.this.type.equalsIgnoreCase("videos")) {
                    if (!FilesListActivity.this.type.equalsIgnoreCase("audio")) {
                        if (FilesListActivity.this.type.equalsIgnoreCase("others")) {
                            imageView.setImageBitmap(BitmapFactory.decodeResource(FilesListActivity.this.getResources(), R.drawable.placeholder_audio));
                        } else {
                            imageView.setImageBitmap(BitmapFactory.decodeResource(FilesListActivity.this.getResources(), R.drawable.placeholder_doc));
                        }
                    } else {
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        try {
                            mediaMetadataRetriever.setDataSource(((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).path);
                        } catch (IllegalArgumentException unused) {
                            textView.setText("" + ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).name);
                            imageView.setImageBitmap(BitmapFactory.decodeResource(FilesListActivity.this.getResources(), R.drawable.placeholder_audio));
                        }
                        byte[] embeddedPicture = mediaMetadataRetriever.getEmbeddedPicture();
                        if (embeddedPicture != null) {
                            imageView.setImageBitmap(BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.length));
                        } else {
                            imageView.setImageBitmap(BitmapFactory.decodeResource(FilesListActivity.this.getResources(), R.drawable.placeholder_audio));
                        }
                    }
                } else {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(ThumbnailUtils.createVideoThumbnail(((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).path, 3));
                        }
                    });
                }
                textView.setText("" + ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).name);
                textView2.setText("" + Util.convertBytes(((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).size));
                textView.setSelected(true);
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                imageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        if (FilesListActivity.this.multipleClicked()) {
                            return;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(FilesListActivity.this);
                        builder.setCancelable(false);
                        builder.setMessage("" + FilesListActivity.this.getResources().getString(R.string.mbc_name) + ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).name + "\n\n" + FilesListActivity.this.getResources().getString(R.string.mbc_path) + ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).path + "\n\n" + FilesListActivity.this.getResources().getString(R.string.mbc_size) + Util.convertBytes(((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).size));
                        builder.setPositiveButton(R.string.mbc_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i2) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        if (FilesListActivity.this.multipleClicked()) {
                            return;
                        }
                        Intent intent = new Intent();
                        if (!FilesListActivity.this.type.equalsIgnoreCase("videos")) {
                            if (FilesListActivity.this.type.equalsIgnoreCase("audio")) {
                                intent.setAction("android.intent.action.VIEW");
                                intent.setDataAndType(Uri.parse("file://" + ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).path), "audio/*");
                                try {
                                    FilesListActivity.this.resumedFromClick = true;
                                    try {
                                        FilesListActivity.this.startActivity(intent);
                                        return;
                                    } catch (Exception e) {
                                        try {
                                            Uri uriForFile = FileProvider.getUriForFile(FilesListActivity.this, "com.mobiclean.phoneclean.provider", new File(((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).path));
                                            Log.d("TTTT", uriForFile.toString() + "");
                                            intent.setDataAndType(uriForFile, "audio/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            FilesListActivity.this.startActivity(intent);
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                        e.printStackTrace();
                                        return;
                                    }
                                } catch (Exception unused2) {
                                    FilesListActivity filesListActivity = FilesListActivity.this;
                                    Toast.makeText(filesListActivity, "" + FilesListActivity.this.getString(R.string.sorry_no_activity), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            File file = new File(((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).path);
                            try {
                                FilesListActivity.this.resumedFromClick = true;
                                FilesListActivity filesListActivity2 = FilesListActivity.this;
                                filesListActivity2.openFile(filesListActivity2, file);
                                return;
                            } catch (Exception e3) {
                                FilesListActivity filesListActivity3 = FilesListActivity.this;
                                Toast.makeText(filesListActivity3, "" + FilesListActivity.this.getString(R.string.sorry_no_activity), Toast.LENGTH_SHORT).show();
                                e3.printStackTrace();
                                return;
                            }
                        }
                        intent.setAction("android.intent.action.VIEW");
                        intent.setDataAndType(Uri.parse("file://" + ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).path), "video/mp4");
                        try {
                            FilesListActivity.this.resumedFromClick = true;
                            try {
                                FilesListActivity.this.startActivity(intent);
                            } catch (Exception e4) {
                                try {
                                    Uri uriForFile2 = FileProvider.getUriForFile(FilesListActivity.this, "com.mobiclean.phoneclean.provider", new File(((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).path));
                                    Log.d("TTTT", uriForFile2.toString() + "");
                                    intent.setDataAndType(uriForFile2, "video/mp4").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    FilesListActivity.this.startActivity(intent);
                                    e4.printStackTrace();
                                } catch (Exception e5) {
                                    e5.printStackTrace();
                                }
                            }
                        } catch (Exception unused3) {
                            FilesListActivity filesListActivity4 = FilesListActivity.this;
                            Toast.makeText(filesListActivity4, "" + FilesListActivity.this.getString(R.string.sorry_no_activity), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                final MediaList mediaList = MobiClean.getInstance().socialModule.currentList;
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        if (FilesListActivity.this.multipleClicked()) {
                            return;
                        }
                        FilesListActivity.this.isTouched = false;
                        if (((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).ischecked) {
                            mediaList.selectedSize -= ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).size;
                            FilesListActivity.this.totalSelectedSize = mediaList.selectedSize;
                            if (FilesListActivity.this.totalSelectedSize > 0) {
                                Float.parseFloat("" + Util.convertBytes_only(FilesListActivity.this.totalSelectedSize));
                                FilesListActivity.this.unitTv.setText(Util.convertBytes_unit(FilesListActivity.this.totalSelectedSize));
                            } else {
                                Float.parseFloat("" + Util.convertBytes_only(FilesListActivity.this.totalSelectedSize));
                                FilesListActivity.this.unitTv.setText("B");
                            }
                            mediaList.selectedCount--;
                            ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).ischecked = false;
                            checkBox.setChecked(false);
                        } else {
                            mediaList.selectedSize += ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).size;
                            FilesListActivity.this.totalSelectedSize = mediaList.selectedSize;
                            if (FilesListActivity.this.totalSelectedSize <= 0) {
                                Float.parseFloat("" + Util.convertBytes_only(FilesListActivity.this.totalSelectedSize));
                                FilesListActivity.this.unitTv.setText("B");
                            } else {
                                Float.parseFloat("" + Util.convertBytes_only(FilesListActivity.this.totalSelectedSize));
                                FilesListActivity.this.unitTv.setText(Util.convertBytes_unit(FilesListActivity.this.totalSelectedSize));
                            }
                            mediaList.selectedCount++;
                            ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).ischecked = true;
                            checkBox.setChecked(true);
                        }
                        if (FilesListActivity.this.isallChecked()) {
                            ((CheckBox) FilesListActivity.this.findViewById(R.id.checkAll)).setChecked(true);
                        } else {
                            ((CheckBox) FilesListActivity.this.findViewById(R.id.checkAll)).setChecked(false);
                        }
                    }
                });
                if (((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).ischecked) {
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

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter(Context context) {
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return FilesListActivity.this.filesData.size();
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
                view = this.mInflater.inflate(R.layout.item_grid_social_img, viewGroup, false);
            }
            ImageView imageView = (ImageView) ViewHolder.get(view, R.id.junklistitemimage);
            TextView textView = (TextView) ViewHolder.get(view, R.id.junklistitemapp);
            TextView textView2 = (TextView) ViewHolder.get(view, R.id.junklistitemsize);
            final CheckBox checkBox = (CheckBox) ViewHolder.get(view, R.id.junklistitemcheck);
            ImageView imageView2 = (ImageView) ViewHolder.get(view, R.id.listiteminfo);
            final FrameLayout frameLayout = (FrameLayout) ViewHolder.get(view, R.id.frame_gradient);
            LinearLayout linearLayout = (LinearLayout) ViewHolder.get(view, R.id.checkcontainer);
            checkBox.setFocusable(false);
            checkBox.setClickable(false);
            try {
                if (FilesListActivity.this.type.equalsIgnoreCase("images")) {
                    if (FilesListActivity.this.mImageIds[i] == 0) {
                        FilesListActivity filesListActivity = FilesListActivity.this;
                        Bitmap bitmapFromMemCache = filesListActivity.getBitmapFromMemCache("" + i);
                        if (bitmapFromMemCache == null) {
                            Bitmap decodeFile = BitmapFactory.decodeFile(((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).path);
                            imageView.setImageBitmap(decodeFile);
                            FilesListActivity filesListActivity2 = FilesListActivity.this;
                            filesListActivity2.addBitmapToMemoryCache("" + i, decodeFile);
                        } else {
                            imageView.setImageBitmap(bitmapFromMemCache);
                        }
                    } else {
                        FilesListActivity filesListActivity3 = FilesListActivity.this;
                        filesListActivity3.loadBitmap(filesListActivity3.mImageIds[i], imageView, FilesListActivity.this.type);
                    }
                } else if (FilesListActivity.this.type.equalsIgnoreCase("videos")) {
                    if (FilesListActivity.this.mImageIds[i] == 0) {
                        FilesListActivity filesListActivity4 = FilesListActivity.this;
                        Bitmap bitmapFromMemCache2 = filesListActivity4.getBitmapFromMemCache("" + i);
                        if (bitmapFromMemCache2 == null) {
                            Bitmap createVideoThumbnail = ThumbnailUtils.createVideoThumbnail(((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).path, 3);
                            imageView.setImageBitmap(createVideoThumbnail);
                            FilesListActivity filesListActivity5 = FilesListActivity.this;
                            filesListActivity5.addBitmapToMemoryCache("" + i, createVideoThumbnail);
                        } else {
                            imageView.setImageBitmap(bitmapFromMemCache2);
                        }
                    } else {
                        FilesListActivity filesListActivity6 = FilesListActivity.this;
                        filesListActivity6.loadBitmap(filesListActivity6.mImageIds[i], imageView, FilesListActivity.this.type);
                    }
                } else {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(FilesListActivity.this.getResources(), R.drawable.my_files));
                }
                textView2.setText("" + Util.convertBytes(((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).size));
                final MediaList mediaList = MobiClean.getInstance().socialModule.currentList;
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        if (FilesListActivity.this.multipleClicked()) {
                            return;
                        }
                        FilesListActivity.this.isTouched = false;
                        if (((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).ischecked) {
                            mediaList.selectedSize -= ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).size;
                            FilesListActivity.this.totalSelectedSize = mediaList.selectedSize;
                            if (FilesListActivity.this.totalSelectedSize > 0) {
                                Float.parseFloat(String.valueOf(Util.convertBytes_only(FilesListActivity.this.totalSelectedSize)).replace(",", ""));
                                FilesListActivity.this.unitTv.setText(Util.convertBytes_unit(FilesListActivity.this.totalSelectedSize));
                            } else {
                                Float.parseFloat(String.valueOf(Util.convertBytes_only(FilesListActivity.this.totalSelectedSize)).replace(",", ""));
                                FilesListActivity.this.unitTv.setText("B");
                            }
                            mediaList.selectedCount--;
                            ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).ischecked = false;
                            checkBox.setChecked(false);
                            frameLayout.setVisibility(View.GONE);
                        } else {
                            mediaList.selectedSize += ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).size;
                            FilesListActivity.this.totalSelectedSize = mediaList.selectedSize;
                            if (FilesListActivity.this.totalSelectedSize <= 0) {
                                Float.parseFloat(String.valueOf(Util.convertBytes_only(FilesListActivity.this.totalSelectedSize)).replace(",", ""));
                                FilesListActivity.this.unitTv.setText("B");
                            } else {
                                Float.parseFloat(String.valueOf(Util.convertBytes_only(FilesListActivity.this.totalSelectedSize)).replace(",", ""));
                                FilesListActivity.this.unitTv.setText(Util.convertBytes_unit(FilesListActivity.this.totalSelectedSize));
                            }
                            ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).ischecked = true;
                            mediaList.selectedCount++;
                            checkBox.setChecked(true);
                            frameLayout.setVisibility(View.VISIBLE);
                        }
                        if (FilesListActivity.this.isallChecked()) {
                            ((CheckBox) FilesListActivity.this.findViewById(R.id.checkAll)).setChecked(true);
                        } else {
                            ((CheckBox) FilesListActivity.this.findViewById(R.id.checkAll)).setChecked(false);
                        }
                        if (FilesListActivity.this.totalSelectedSize == 0) {
                            FilesListActivity.this.deleteBtn.setText("" + FilesListActivity.this.getResources().getString(R.string.mbc_delete));
                        }
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        if (FilesListActivity.this.multipleClicked()) {
                            return;
                        }
                        FilesListActivity.this.type.equalsIgnoreCase("images");
                    }
                });
                if (((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i)).ischecked) {
                    checkBox.setChecked(true);
                    frameLayout.setVisibility(View.VISIBLE);
                } else {
                    checkBox.setChecked(false);
                    frameLayout.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }
    }

    public class ImgAdapter extends ArrayAdapter<BigSizeFilesWrapper> {
        private LayoutInflater mInflater;

        public ImgAdapter(Context context) {
            super(context, 0, FilesListActivity.this.records);
            FilesListActivity.this.dip2px(83.33f);
            FilesListActivity.this.dip2px(76.33f);
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final BigSizeFilesWrapper item = getItem(i);
            if (view == null) {
                view = this.mInflater.inflate(R.layout.item_grid_social_img, (ViewGroup) null);
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.junklistitemimage);
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.junklistitemcheck);
            if (item.ischecked) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            final MediaList mediaList = MobiClean.getInstance().socialModule.currentList;
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if (checkBox.isChecked()) {
                        MobiClean.getInstance().socialModule.addDataForDeletion(item);
                        item.ischecked = true;
                        return;
                    }
                    MobiClean.getInstance().socialModule.removeDataFromDeletionList(item);
                    item.ischecked = false;
                    FilesListActivity.this.checkAll.setChecked(false);
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    Intent intent = new Intent();
                    if (mediaList.mediaType.ordinal() != 0) {
                        return;
                    }
                    try {
                        intent.setAction("android.intent.action.VIEW");
                        intent.setDataAndType(Uri.parse("file://" + ((BigSizeFilesWrapper) FilesListActivity.this.records.get(i)).path), "image/*");
                        FilesListActivity.this.startActivity(intent);
                    } catch (Exception unused) {
                        intent.setAction("android.intent.action.VIEW");
                        FilesListActivity filesListActivity = FilesListActivity.this;
                        intent.setDataAndType(FileProvider.getUriForFile(filesListActivity, "com.mobiclean.phoneclean.provider", ((BigSizeFilesWrapper) filesListActivity.records.get(i)).file), "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        try {
                            FilesListActivity.this.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            FilesListActivity filesListActivity2 = FilesListActivity.this;
                            Toast.makeText(filesListActivity2, "" + FilesListActivity.this.getString(R.string.sorry_no_activity), Toast.LENGTH_SHORT).show();
                        }
                    }
                    FilesListActivity.this.resumedFromClick = true;
                }
            });
            if (mediaList.mediaType.ordinal() != 2) {
                Glide.with((FragmentActivity) FilesListActivity.this).load(item.file).into(imageView);
            }
            return view;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public BigSizeFilesWrapper getItem(int i) {
            return (BigSizeFilesWrapper) FilesListActivity.this.records.get(i);
        }
    }

    public abstract class OnScrollObserver implements AbsListView.OnScrollListener {
        public int f5305a = 0;
        public boolean b = true;

        public OnScrollObserver(FilesListActivity filesListActivity) {
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            int i4 = this.f5305a;
            if (i < i4 && !this.b) {
                onScrollUp();
                this.b = true;
            } else if (i > i4 && this.b) {
                onScrollDown();
                this.b = false;
            }
            this.f5305a = i;
        }

        public abstract void onScrollDown();

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
        }

        public abstract void onScrollUp();
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


    public int dip2px(float f) {
        return (int) ((f * getResources().getDisplayMetrics().density) + 0.5f);
    }

    private void initControls() {
        this.unitTv = (TextView) findViewById(R.id.mediadisplay_sizetv_unit);
        this.listviewImages = (GridView) findViewById(R.id.grid);
        this.deleteBtn = (Button) findViewById(R.id.btnfiledelete);
        this.listview = (ListView) findViewById(R.id.listview);
        this.tvview = (TextView) findViewById(R.id.tv_gird);
        this.checkAll = (CheckBox) findViewById(R.id.checkAll);
        this.j = (TextView) findViewById(R.id.mediadisplay_sizetv);
        findViewById(R.id.iv_back).setOnClickListener(this);
        TextView textView = this.unitTv;
        textView.setTextSize(0, DetermineTextSize.determineTextSize(textView.getTypeface(), (this.displaymetrics.heightPixels * 6) / 100));
        this.j.setTextSize(0, DetermineTextSize.determineTextSize(((TextView) findViewById(R.id.mediadisplay_sizetv)).getTypeface(), (this.displaymetrics.heightPixels * 9) / 100));
    }


    public boolean isallChecked() {
        for (int i = 0; i < this.filesData.size(); i++) {
            if (!this.filesData.get(i).ischecked) {
                return false;
            }
        }
        return true;
    }

    public void loadBitmap(int i, ImageView imageView, String str) {
        Bitmap bitmapFromMemCache = getBitmapFromMemCache(String.valueOf(i));
        if (bitmapFromMemCache != null) {
            imageView.setImageBitmap(bitmapFromMemCache);
        } else if (CacheActivity.cancelPotentialWork(i, imageView)) {
            BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(imageView, str);
            imageView.setImageDrawable(new AsyncDrawable(getResources(), null, bitmapWorkerTask));
            bitmapWorkerTask.execute(i + "", " ");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View view) {
        boolean z;
        if (multipleClicked()) {
            return;
        }
        int id = view.getId();
        if (id != R.id.btnfiledelete) {
            if (id != R.id.iv_back) {
                return;
            }
            finish();
            return;
        }
        int i = 0;
        while (true) {
            if (i >= this.filesData.size()) {
                z = true;
                break;
            } else if (this.filesData.get(i).ischecked) {
                z = false;
                break;
            } else {
                i++;
            }
        }
        if (z) {
            Toast.makeText(this, getResources().getString(R.string.mbc_at_leastone), Toast.LENGTH_SHORT).show();
        } else {
            new AsyncTask<String, String, String>() {
                @Override
                public void onPreExecute() {
                    FilesListActivity.this.totaldeletedsize = 0L;
                    super.onPreExecute();
                }

                @Override
                public String doInBackground(String... strArr) {
                    String str;
                    String str2 = FilesListActivity.this.type;
                    switch (str2.hashCode()) {
                        case -1406804131:
                            str = "audios";
                            break;
                        case -1185250696:
                            str = "images";
                            break;
                        case -1006804125:
                            str = "others";
                            break;
                        case -816678056:
                            str = "videos";
                            break;
                        case 97434231:
                            str = "files";
                            break;
                        default:
                            return null;
                    }
                    str2.equals(str);
                    return null;
                }

                @Override
                public void onPostExecute(String str) {
                    ArrayList arrayList = new ArrayList();
                    for (int i2 = 0; i2 < FilesListActivity.this.filesData.size(); i2++) {
                        if (((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i2)).ischecked && new File(((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i2)).path).exists()) {
                            arrayList.add(((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i2)).path);
                            FilesListActivity.this.totaldeletedsize -= ((BigSizeFilesWrapper) FilesListActivity.this.filesData.get(i2)).size;
                        }
                    }
                    if (arrayList.size() <= 0) {
                        Intent intent = new Intent(FilesListActivity.this, CommonResultActivity.class);
                        GlobalData.showIronsrc = true;
                        intent.putExtra("DATA", "" + Util.convertBytes(FilesListActivity.this.totaldeletedsize));
                        intent.putExtra("TYPE", "Recovered");
                        FilesListActivity.this.finish();
                        FilesListActivity.this.resumedFromClick = true;
                        GlobalData.afterDelete = true;
                        FilesListActivity.this.startActivity(intent);
                    }
                    super.onPostExecute( str);
                }
            }.execute(new String[0]);
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.activity_social_grid);
        this.displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(this.displaymetrics);
        initControls();
        final MediaList mediaList = MobiClean.getInstance().socialModule.currentList;
        this.records = mediaList.arrContents;
        if (mediaList.mediaType.ordinal() != 0) {
            this.fileAdapter = new FileAdapter(this);
            this.listview.setAdapter((ListAdapter) new SimpleSectionedListAdapter(this, this.fileAdapter, R.layout.list_item_header, R.id.header));
            this.listviewImages.setVisibility(View.GONE);
            this.listview.setVisibility(View.VISIBLE);
        } else {
            this.adapter = new ImgAdapter(this);
            this.listviewImages.setAdapter((ListAdapter) new SimpleSectionedListAdapter(this, this.adapter, R.layout.list_item_header, R.id.header));
            this.listviewImages.setVisibility(View.VISIBLE);
            this.listview.setVisibility(View.GONE);
        }
        this.checkAll.setChecked(mediaList.selectedCount == mediaList.totalCount);
        this.j.setText(Util.convertBytes_only(mediaList.selectedSize));
        ((TextView) findViewById(R.id.tv_social_type)).setText(mediaList.title);
        ((TextView) findViewById(R.id.tv_social_count)).setText(mediaList.getSelectedTotalString());
        this.unitTv.setText(Util.convertBytes_unit(MobiClean.getInstance().socialModule.currentList.selectedSize));
        this.checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FilesListActivity.this.checkAll.isChecked()) {
                    mediaList.selectAll();
                } else {
                    mediaList.unSelectAll();
                }
                FilesListActivity.this.j.setText(Util.convertBytes_only(mediaList.selectedSize));
                ((TextView) FilesListActivity.this.findViewById(R.id.tv_social_count)).setText(mediaList.getSelectedTotalString());
                FilesListActivity.this.totalSelectedSize = mediaList.selectedSize;
                FilesListActivity.this.unitTv.setText(Util.convertBytes_unit(MobiClean.getInstance().socialModule.currentList.selectedSize));
                Float.parseFloat(String.valueOf(Util.convertBytes_only(FilesListActivity.this.totalSelectedSize)).replace(",", ""));
                if (FilesListActivity.this.adapter != null) {
                    FilesListActivity.this.adapter.notifyDataSetChanged();
                }
                if (FilesListActivity.this.fileAdapter != null) {
                    FilesListActivity.this.fileAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        finish();
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
    }

    public void openFile(Context context, File file) {
        Uri fromFile = Uri.fromFile(file);
        Intent intent = new Intent("android.intent.action.VIEW");
        if (!file.toString().contains(".doc") && !file.toString().contains(".docx")) {
            if (file.toString().contains(".pdf")) {
                intent.setDataAndType(fromFile, "application/pdf");
            } else if (!file.toString().contains(".ppt") && !file.toString().contains(".pptx")) {
                if (!file.toString().contains(".xls") && !file.toString().contains(".xlsx")) {
                    if (!file.toString().contains(".zip") && !file.toString().contains(".rar")) {
                        if (file.toString().contains(".rtf")) {
                            intent.setDataAndType(fromFile, "application/rtf");
                        } else if (!file.toString().contains(".wav") && !file.toString().contains(".mp3")) {
                            if (file.toString().contains(".gif")) {
                                intent.setDataAndType(fromFile, "image/gif");
                            } else if (!file.toString().contains(".jpg") && !file.toString().contains(".jpeg") && !file.toString().contains(".png")) {
                                if (file.toString().contains(".txt")) {
                                    intent.setDataAndType(fromFile, "text/plain");
                                } else if (!file.toString().contains(".3gp") && !file.toString().contains(".mpg") && !file.toString().contains(".mpeg") && !file.toString().contains(".mpe") && !file.toString().contains(".mp4") && !file.toString().contains(".avi")) {
                                    intent.setDataAndType(fromFile, "*/*");
                                } else {
                                    intent.setDataAndType(fromFile, "video/*");
                                }
                            } else {
                                intent.setDataAndType(fromFile, "image/jpeg");
                            }
                        } else {
                            intent.setDataAndType(fromFile, "audio/x-wav");
                        }
                    } else {
                        intent.setDataAndType(fromFile, "application/x-wav");
                    }
                } else {
                    intent.setDataAndType(fromFile, "application/vnd.ms-excel");
                }
            } else {
                intent.setDataAndType(fromFile, "application/vnd.ms-powerpoint");
            }
        } else {
            intent.setDataAndType(fromFile, "application/msword");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                intent.setDataAndType(FileProvider.getUriForFile(context, "com.mobiclean.phoneclean.provider", file), "*/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            } catch (Exception unused) {
            }
            e.printStackTrace();
        }
    }
}
