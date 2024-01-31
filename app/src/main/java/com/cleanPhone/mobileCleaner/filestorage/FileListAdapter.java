package com.cleanPhone.mobileCleaner.filestorage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.cleanPhone.mobileCleaner.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class FileListAdapter extends BaseAdapter {
    private String TAG = FileListAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<FileListItem> listItem;
    private NotifyItemChecked notifyItemChecked;
    private DialogProperties properties;

    public class ViewHolder {
        public ImageView f4896a;
        public CheckBox b;
        public TextView name;
        public TextView type;

        public ViewHolder(FileListAdapter fileListAdapter, View view) {
            this.name = (TextView) view.findViewById(R.id.fname);
            this.type = (TextView) view.findViewById(R.id.ftype);
            this.f4896a = (ImageView) view.findViewById(R.id.image_type);
            this.b = (CheckBox) view.findViewById(R.id.file_mark);
        }
    }

    public FileListAdapter(ArrayList<FileListItem> arrayList, Context context, DialogProperties dialogProperties) {
        this.listItem = arrayList;
        this.context = context;
        this.properties = dialogProperties;
    }

    public void c(NotifyItemChecked notifyItemChecked) {
        this.notifyItemChecked = notifyItemChecked;
    }

    @Override
    public int getCount() {
        return this.listItem.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.dialog_file_list_item, viewGroup, false);
            viewHolder = new ViewHolder(this, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final FileListItem fileListItem = this.listItem.get(i);
        if (MarkedItemList.f(fileListItem.getLocation())) {
            view.setAnimation(AnimationUtils.loadAnimation(this.context, R.anim.marked_item_animation));
        } else {
            view.setAnimation(AnimationUtils.loadAnimation(this.context, R.anim.unmarked_item_animation));
        }
        if (fileListItem.isDirectory()) {
            viewHolder.f4896a.setColorFilter(ContextCompat.getColor(this.context, R.color.colorPrimaryDark));
            if (this.properties.selection_type == 0) {
                viewHolder.b.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.b.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.f4896a.setImageResource(R.drawable.ic_type_file);
            viewHolder.f4896a.setColorFilter(ContextCompat.getColor(this.context, R.color.colorPrimaryDark));
            if (this.properties.selection_type == 1) {
                viewHolder.b.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.b.setVisibility(View.VISIBLE);
            }
        }
        viewHolder.f4896a.setContentDescription(fileListItem.getFilename());
        viewHolder.name.setText(fileListItem.getFilename());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        Date date = new Date(fileListItem.getTime());
        if (i == 0 && fileListItem.getFilename().startsWith("...")) {
            viewHolder.type.setText(R.string.mbc_label_parent_directory);
        } else {
            TextView textView = viewHolder.type;
            textView.setText(this.context.getResources().getString(R.string.mbc_last_edited) + simpleDateFormat.format(date) + ", " + simpleDateFormat2.format(date));
        }
        if (viewHolder.b.getVisibility() == View.VISIBLE) {
            if (i == 0 && fileListItem.getFilename().startsWith("...")) {
                viewHolder.b.setVisibility(View.INVISIBLE);
            }
            if (MarkedItemList.f(fileListItem.getLocation())) {
                viewHolder.b.setChecked(true);
            } else {
                viewHolder.b.setChecked(false);
            }
        }
        viewHolder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                FileListItem fileListItem2 = fileListItem;
                fileListItem2.setMarked(!fileListItem2.isMarked());
                if (fileListItem.isMarked()) {
                    if (FileListAdapter.this.properties.selection_mode == 1) {
                        MarkedItemList.a(fileListItem);
                    } else {
                        MarkedItemList.b(fileListItem);
                    }
                } else {
                    MarkedItemList.g(fileListItem.getLocation());
                }
                FileListAdapter.this.notifyItemChecked.notifyCheckBoxIsClicked();
            }
        });
        return view;
    }

    @Override
    public FileListItem getItem(int i) {
        return this.listItem.get(i);
    }
}
