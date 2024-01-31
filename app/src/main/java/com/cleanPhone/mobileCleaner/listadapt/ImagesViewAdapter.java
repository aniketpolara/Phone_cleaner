package com.cleanPhone.mobileCleaner.listadapt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppJunk;
import com.cleanPhone.mobileCleaner.wrappers.BigSizeFilesWrapper;
import com.cleanPhone.mobileCleaner.wrappers.MediaJunkData;

import java.io.File;
import java.util.ArrayList;

public class ImagesViewAdapter extends ArrayAdapter<BigSizeFilesWrapper> {
    private CheckBox cboxAll;
    private Context context;
    private int height;
    private LayoutInflater mInflater;
    private int mediaType;
    private ArrayList<BigSizeFilesWrapper> records;
    private final float scale;
    private TextView tv;
    private int width;

    public ImagesViewAdapter(Context context, ArrayList<BigSizeFilesWrapper> arrayList, int i, CheckBox checkBox, TextView textView) {
        super(context, 0, arrayList);
        this.scale = context.getResources().getDisplayMetrics().density;
        this.context = context;
        this.tv = textView;
        this.records = arrayList;
        this.mediaType = i;
        this.cboxAll = checkBox;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        this.width = displayMetrics.widthPixels;
        this.height = displayMetrics.heightPixels;
    }


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
                                intent.setDataAndType(uriForFile, "image/jpeg");
                            }
                        } else {
                            intent.setDataAndType(uriForFile, "audio/x-wav");
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
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @NonNull
    public View getView(final int i, View view, @NonNull ViewGroup viewGroup) {
        final BigSizeFilesWrapper item = getItem(i);
        @SuppressLint("ResourceType") final View findViewById = ((AppCompatActivity) this.context).getWindow().getDecorView().findViewById(16908290);
        if (view == null) {
            view = this.mInflater.inflate(R.layout.item_grid_social_img, (ViewGroup) null);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.junklistitemimage);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.checkcontainer);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.junklistitemcheck);
        if (item != null) {
            ((TextView) view.findViewById(R.id.junkListItemSize)).setText("" + Util.convertBytes(item.size));
        }
        if (item != null) {
            if (item.ischecked) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                AppJunk appJunk = MobiClean.getInstance().mediaAppModule.socialApp.get(GlobalData.APP_INDEX);
                MediaJunkData mediaJunkData = appJunk.mediaJunkData.get(ImagesViewAdapter.this.mediaType);
                if (checkBox.isChecked()) {
                    appJunk.unselect(item);
                    checkBox.setChecked(false);
                    BigSizeFilesWrapper bigSizeFilesWrapper = item;
                    if (bigSizeFilesWrapper != null) {
                        bigSizeFilesWrapper.ischecked = false;
                    }
                    mediaJunkData.unselect(bigSizeFilesWrapper);
                } else {
                    appJunk.select(item);
                    checkBox.setChecked(true);
                    BigSizeFilesWrapper bigSizeFilesWrapper2 = item;
                    if (bigSizeFilesWrapper2 != null) {
                        bigSizeFilesWrapper2.ischecked = true;
                    }
                    mediaJunkData.select(bigSizeFilesWrapper2);
                }
                ImagesViewAdapter.this.cboxAll.setChecked(mediaJunkData.totSelectedCount == ((long) ImagesViewAdapter.this.records.size()));
                TextView textView = ImagesViewAdapter.this.tv;
                textView.setText("" + mediaJunkData.totSelectedCount + DialogConfigs.DIRECTORY_SEPERATOR + ImagesViewAdapter.this.records.size());
                ((TextView) findViewById.findViewById(R.id.clean_now_text)).setText("" + ImagesViewAdapter.this.context.getString(R.string.mbc_clean) + " " + Util.convertBytes(appJunk.selectedSize) + "");
                ImagesViewAdapter.this.notifyDataSetChanged();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                new Intent();
                int i2 = ImagesViewAdapter.this.mediaType;
                if (i2 == 0) {
                    try {
                        ImagesViewAdapter imagesViewAdapter = ImagesViewAdapter.this;
                        imagesViewAdapter.openFile(imagesViewAdapter.context, ((BigSizeFilesWrapper) ImagesViewAdapter.this.records.get(i)).file);
                    } catch (Exception e) {
                        Context context = ImagesViewAdapter.this.context;
                        Toast.makeText(context, "" + ImagesViewAdapter.this.context.getResources().getString(R.string.sorry_no_activity), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else if (i2 == 1) {
                    try {
                        ImagesViewAdapter imagesViewAdapter2 = ImagesViewAdapter.this;
                        imagesViewAdapter2.openFile(imagesViewAdapter2.context, ((BigSizeFilesWrapper) ImagesViewAdapter.this.records.get(i)).file);
                    } catch (Exception e2) {
                        Context context2 = ImagesViewAdapter.this.context;
                        Toast.makeText(context2, "" + ImagesViewAdapter.this.context.getResources().getString(R.string.sorry_no_activity), Toast.LENGTH_SHORT).show();
                        e2.printStackTrace();
                    }
                } else if (i2 != 2) {
                    try {
                        ImagesViewAdapter imagesViewAdapter3 = ImagesViewAdapter.this;
                        imagesViewAdapter3.openFile(imagesViewAdapter3.context, ((BigSizeFilesWrapper) ImagesViewAdapter.this.records.get(i)).file);
                    } catch (Exception e3) {
                        Context context3 = ImagesViewAdapter.this.context;
                        Toast.makeText(context3, "" + ImagesViewAdapter.this.context.getResources().getString(R.string.sorry_no_activity), Toast.LENGTH_SHORT).show();
                        e3.printStackTrace();
                    }
                } else {
                    try {
                        ImagesViewAdapter imagesViewAdapter4 = ImagesViewAdapter.this;
                        imagesViewAdapter4.openFile(imagesViewAdapter4.context, ((BigSizeFilesWrapper) ImagesViewAdapter.this.records.get(i)).file);
                    } catch (Exception e4) {
                        Context context4 = ImagesViewAdapter.this.context;
                        Toast.makeText(context4, "" + ImagesViewAdapter.this.context.getResources().getString(R.string.sorry_no_activity), Toast.LENGTH_SHORT).show();
                        e4.printStackTrace();
                    }
                }
            }
        });
        int i2 = this.mediaType;
        if (i2 == 2) {
            imageView.setImageResource(R.drawable.placeholder_audio);
        } else if (i2 == 3) {
            imageView.setImageResource(R.drawable.placeholder_doc);
        } else if (item != null) {
            Glide.with(this.context).load(item.file).into(imageView);
        }
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public BigSizeFilesWrapper getItem(int i) {
        return this.records.get(i);
    }
}
