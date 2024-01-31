package com.cleanPhone.mobileCleaner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cleanPhone.mobileCleaner.filestorage.ListItem;

import java.util.ArrayList;


public class FileListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ListItem> listItems;

    public FileListAdapter(ArrayList<ListItem> arrayList, Context context) {
        this.listItems = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.listItems.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.file_list_item, viewGroup, false);
        }
        ((TextView) view.findViewById(R.id.name)).setText(this.listItems.get(i).getName());
        ((TextView) view.findViewById(R.id.path)).setText(this.listItems.get(i).getPath());
        return view;
    }

    @Override
    public ListItem getItem(int i) {
        return this.listItems.get(i);
    }
}
