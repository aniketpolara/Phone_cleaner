package com.cleanPhone.mobileCleaner.listadapt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cleanPhone.mobileCleaner.DownloadsData;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.DownloadWrapper;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class FilesDownloadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final TextView btn;
    private final Context context;
    private String TAG = FilesDownloadAdapter.class.getSimpleName();
    private List arrExtensionImage = Arrays.asList("jpeg", "jpg", "JPEG", "JPG", "png", "gif", "tiff", "tif", "bmp", "svg", "webp");
    private List arrExtensionVideo = Arrays.asList("mp4", "3gp", "avi", "mpeg", "webm", "flv", "wmv", "mkv", "m4a", "wav", "wma", "mmf", "mp2", "flac", "au", "ac3", "mpg", "mov", "mpv", "mpe", "ogg");
    private List arrExtensionAudio = Arrays.asList("mp3", "mpa", "aac", "oga");
    private List textFileExtensions = Arrays.asList("pdf", "doc", "docx", "xls", "ppt", "odt", "rtf", "txt", "pptx", "htm", "html", "log", "csv", "dot", "dotx", "docm", "dotm", "xml", "mht", "dic", "xlsx", "msg", "mhtml", "pps", "xltx", "xlt", "xlsm", "xltm", "ppsx", "pptm", "ppsm");
    private DownloadsData downloadsData = MobiClean.getInstance().downloadsData;
    private static final int LAYOUT_ONE = 0;
    private static final int LAYOUT_TWO = 1;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cBox;
        private LinearLayout checkBoxLout;
        private ImageView imgIcon;
        public TextView size;
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.tvdownload_app_name);
            this.size = (TextView) view.findViewById(R.id.tvdownload_app_size);
            this.imgIcon = (ImageView) view.findViewById(R.id.img_fileicon);
            this.checkBoxLout = (LinearLayout) view.findViewById(R.id.check_down_layout);
            this.cBox = (CheckBox) view.findViewById(R.id.check_download);
        }
    }

    public FilesDownloadAdapter(Context context, TextView button) {
        this.btn = button;
        this.context = context;
    }

    private Drawable getADrawable(int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.context.getResources().getDrawable(i, this.context.getTheme());
        }
        return this.context.getResources().getDrawable(i);
    }

    private void getDrawableResource(DownloadWrapper downloadWrapper, ImageView imageView) {
        String str = downloadWrapper.ext;
        if (str.equalsIgnoreCase("zip")) {
            imageView.setImageDrawable(getADrawable(R.drawable.ic_zip));
        } else if (str.equalsIgnoreCase("apk")) {
            imageView.setImageDrawable(getADrawable(R.drawable.apk_files));
        } else if (this.arrExtensionImage.contains(str)) {
            Glide.with(this.context).load(downloadWrapper.path).into(imageView);
        } else if (this.arrExtensionAudio.contains(str)) {
            imageView.setImageDrawable(getADrawable(R.drawable.toolbox_audio_icon));
        } else if (this.arrExtensionVideo.contains(str)) {
            Glide.with(this.context).load(downloadWrapper.path).into(imageView);
        } else if (this.textFileExtensions.contains(str)) {
            imageView.setImageDrawable(getADrawable(R.drawable.toolbox_documents_icon));
        } else {
            imageView.setImageDrawable(getADrawable(R.drawable.ic_unknown));
        }
    }

    @SuppressLint("WrongConstant")
    public void openDownloadFile(Context context, File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(335544320);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            String str = this.TAG;
            Util.appendLogmobiclean(str, "openFile catch block" + e.getMessage(), GlobalData.FILE_NAME);
        }
    }


    @Override
    public int getItemCount() {
        return this.downloadsData.downloadsList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            final DownloadWrapper downloadWrapper = this.downloadsData.downloadsList.get(i);
            TextView textView = myViewHolder.title;
            textView.setText("" + downloadWrapper.name);
            TextView textView2 = myViewHolder.size;
            textView2.setText("" + Util.convertBytes(downloadWrapper.size));
            getDrawableResource(downloadWrapper, myViewHolder.imgIcon);
            ((View) myViewHolder.title.getParent()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        FilesDownloadAdapter filesDownloadAdapter = FilesDownloadAdapter.this;
                        filesDownloadAdapter.openDownloadFile(filesDownloadAdapter.context, new File(downloadWrapper.path));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            myViewHolder.checkBoxLout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myViewHolder.cBox.isChecked()) {
                        myViewHolder.cBox.setChecked(false);
                        downloadWrapper.checked = false;
                        FilesDownloadAdapter.this.downloadsData.totalSelected--;
                    } else {
                        myViewHolder.cBox.setChecked(true);
                        downloadWrapper.checked = true;
                        FilesDownloadAdapter.this.downloadsData.totalSelected++;
                    }
                    if (FilesDownloadAdapter.this.downloadsData.totalSelected == 0) {
                        FilesDownloadAdapter.this.btn.setText(FilesDownloadAdapter.this.context.getString(R.string.mbc_delete));
                        return;
                    }
                    FilesDownloadAdapter.this.btn.setText("" + FilesDownloadAdapter.this.context.getResources().getString(R.string.delete) + " (" + FilesDownloadAdapter.this.downloadsData.totalSelected + ")");
                }
            });


    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.downlaod_adapter_layout, parent, false);
        viewHolder = new MyViewHolder(view);

        return viewHolder;

    }


    @Override
    public int getItemViewType(int position) {
        if (position % 9 == 8) {
            return LAYOUT_ONE;
        } else {
            return LAYOUT_TWO;
        }
    }

}
