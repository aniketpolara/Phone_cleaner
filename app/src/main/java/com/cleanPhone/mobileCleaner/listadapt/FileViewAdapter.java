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
import androidx.core.content.ContextCompat;
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

public class FileViewAdapter extends ArrayAdapter<BigSizeFilesWrapper> {
    private CheckBox cboxAll;
    private Context context;
    private int height;
    private LayoutInflater mInflater;
    private int mediaType;
    private ArrayList<BigSizeFilesWrapper> records;
    private TextView tv;
    private int width;
    private final int VIEW_TYPE_EXAMPLE = 0;
    private final int VIEW_TYPE_EXAMPLE_TWO = 1;

    public FileViewAdapter(Context context, ArrayList<BigSizeFilesWrapper> arrayList, int i, CheckBox checkBox, TextView textView) {
        super(context, 0, arrayList);
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
        int viewType = getItemViewType(i);
        ViewHolder2 viewHolder2 = null;
        switch (viewType) {
            case VIEW_TYPE_EXAMPLE_TWO: {
                final BigSizeFilesWrapper item = getItem(i);
                @SuppressLint("ResourceType") final View findViewById = ((AppCompatActivity) this.context).getWindow().getDecorView().findViewById(16908290);
                if (view == null) {
                    view = this.mInflater.inflate(R.layout.fileslistitemsocial, (ViewGroup) null);
                }
                ImageView imageView = (ImageView) view.findViewById(R.id.junklistitemimage);
                TextView textView = (TextView) view.findViewById(R.id.junklistitemapp);
                TextView textView2 = (TextView) view.findViewById(R.id.junklistitemsize);
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.checkcontainer);
                final CheckBox checkBox = (CheckBox) view.findViewById(R.id.junklistitemcheck);
                view.findViewById(R.id.listiteminfo).setVisibility(View.GONE);
                if (item != null) {
                    if (item.ischecked) {
                        checkBox.setChecked(true);
                    } else {
                        checkBox.setChecked(false);
                    }
                }
                if (item != null) {
                    textView.setText("" + item.name);
                    textView2.setText(Util.convertBytes(item.size));
                }
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        AppJunk appJunk = MobiClean.getInstance().mediaAppModule.socialApp.get(GlobalData.APP_INDEX);
                        MediaJunkData mediaJunkData = appJunk.mediaJunkData.get(FileViewAdapter.this.mediaType);
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
                        FileViewAdapter.this.cboxAll.setChecked(mediaJunkData.totSelectedCount == ((long) FileViewAdapter.this.records.size()));
                        TextView textView3 = FileViewAdapter.this.tv;
                        textView3.setText("" + mediaJunkData.totSelectedCount + DialogConfigs.DIRECTORY_SEPERATOR + FileViewAdapter.this.records.size());
                        ((TextView) findViewById.findViewById(R.id.clean_now_text)).setText("" + FileViewAdapter.this.context.getString(R.string.mbc_clean) + " " + Util.convertBytes(appJunk.selectedSize) + "");
                        FileViewAdapter.this.notifyDataSetChanged();
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        new Intent();
                        int i2 = FileViewAdapter.this.mediaType;
                        if (i2 == 0) {
                            try {
                                FileViewAdapter fileViewAdapter = FileViewAdapter.this;
                                fileViewAdapter.openFile(fileViewAdapter.context, ((BigSizeFilesWrapper) FileViewAdapter.this.records.get(i)).file);
                            } catch (Exception e) {
                                Context context = FileViewAdapter.this.context;
                                Toast.makeText(context, "" + FileViewAdapter.this.context.getResources().getString(R.string.sorry_no_activity), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        } else if (i2 == 1) {
                            try {
                                FileViewAdapter fileViewAdapter2 = FileViewAdapter.this;
                                fileViewAdapter2.openFile(fileViewAdapter2.context, ((BigSizeFilesWrapper) FileViewAdapter.this.records.get(i)).file);
                            } catch (Exception e2) {
                                Context context2 = FileViewAdapter.this.context;
                                Toast.makeText(context2, "" + FileViewAdapter.this.context.getResources().getString(R.string.sorry_no_activity), Toast.LENGTH_SHORT).show();
                                e2.printStackTrace();
                            }
                        } else if (i2 != 2) {
                            try {
                                FileViewAdapter fileViewAdapter3 = FileViewAdapter.this;
                                fileViewAdapter3.openFile(fileViewAdapter3.context, ((BigSizeFilesWrapper) FileViewAdapter.this.records.get(i)).file);
                            } catch (Exception e3) {
                                Context context3 = FileViewAdapter.this.context;
                                Toast.makeText(context3, "" + FileViewAdapter.this.context.getResources().getString(R.string.sorry_no_activity), Toast.LENGTH_SHORT).show();
                                e3.printStackTrace();
                            }
                        } else {
                            try {
                                FileViewAdapter fileViewAdapter4 = FileViewAdapter.this;
                                fileViewAdapter4.openFile(fileViewAdapter4.context, ((BigSizeFilesWrapper) FileViewAdapter.this.records.get(i)).file);
                            } catch (Exception e4) {
                                Context context4 = FileViewAdapter.this.context;
                                Toast.makeText(context4, "" + FileViewAdapter.this.context.getResources().getString(R.string.sorry_no_activity), Toast.LENGTH_SHORT).show();
                                e4.printStackTrace();
                            }
                        }
                    }
                });
                int i2 = this.mediaType;
                if (i2 == 2) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.toolbox_audio_icon));
                } else if (i2 == 3) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.toolbox_documents_icon));
                } else if (item != null) {
                    Glide.with(this.context).load(item.file).into(imageView);
                }
                break;
            }  case VIEW_TYPE_EXAMPLE: {
                if (view == null) {
                    viewHolder2 = new ViewHolder2(view);
                    view = this.mInflater.inflate(R.layout.ad_view, (ViewGroup) null);
//                    viewHolder2.rl_native = (TemplateView) view.findViewById(R.id.native_view);
                    ViewHolder2 finalViewHolder = viewHolder2;
                     break;
                }
            }

        }
        return view;
        }


    public class ViewHolder2 {
        public ViewHolder2(View ignoreListAdapter) {
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (position % 9 == 8) ? VIEW_TYPE_EXAMPLE : VIEW_TYPE_EXAMPLE_TWO;
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
