package com.cleanPhone.mobileCleaner.filestorage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.GlobalData;

import java.io.File;
import java.util.ArrayList;

public class FilePickerDialog extends Dialog implements AdapterView.OnItemClickListener {
    private DialogSelectionListener callbacks;
    private Context context;
    private TextView dir_path;
    private TextView dname;
    private ExtensionFilter filter;
    private ArrayList<FileListItem> internalList;
    private ListView listView;
    private FileListAdapter mFileListAdapter;
    private DialogProperties properties;
    private RelativeLayout select;
    private TextView title;
    private String titleStr;

    public FilePickerDialog(Context context) {
        super(context);
        this.titleStr = null;
        this.context = context;
        DialogProperties dialogProperties = new DialogProperties();
        this.properties = dialogProperties;
        this.filter = new ExtensionFilter(dialogProperties);
        this.internalList = new ArrayList<>();
    }

    private void setTitle() {
        TextView textView = this.title;
        if (textView == null || this.dname == null) {
            return;
        }
        if (this.titleStr != null) {
            if (textView.getVisibility() == View.INVISIBLE) {
                this.title.setVisibility(View.VISIBLE);
            }
            this.title.setText(this.titleStr);
            if (this.dname.getVisibility() == View.VISIBLE) {
                this.dname.setVisibility(View.INVISIBLE);
                return;
            }
            return;
        }
        if (textView.getVisibility() == View.VISIBLE) {
            this.title.setVisibility(View.INVISIBLE);
        }
        if (this.dname.getVisibility() == View.INVISIBLE) {
            this.dname.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void dismiss() {
        MarkedItemList.c();
        this.internalList.clear();
        super.dismiss();
    }

    public DialogProperties getProperties() {
        return this.properties;
    }

    @Override
    public void onBackPressed() {
        String charSequence = this.dname.getText().toString();
        if (this.internalList.size() > 0) {
            File file = new File(this.internalList.get(0).getLocation());
            if (charSequence.equals(this.properties.root.getName())) {
                super.onBackPressed();
            } else {
                this.dname.setText(file.getName());
                this.dir_path.setText(file.getAbsolutePath());
                this.internalList.clear();
                if (!file.getName().equals(this.properties.root.getName())) {
                    FileListItem fileListItem = new FileListItem();
                    fileListItem.setFilename("...");
                    fileListItem.setDirectory(true);
                    fileListItem.setLocation(file.getParentFile().getAbsolutePath());
                    fileListItem.setTime(file.lastModified());
                    this.internalList.add(fileListItem);
                }
                this.internalList = Utility.prepareFileListEntries(this.internalList, file, this.filter);
                this.mFileListAdapter.notifyDataSetChanged();
            }
            setTitle();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this.context);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_main);
        this.listView = (ListView) findViewById(R.id.fileList);
        this.select = (RelativeLayout) findViewById(R.id.select);
        this.dname = (TextView) findViewById(R.id.dname);
        this.title = (TextView) findViewById(R.id.title);
        this.dir_path = (TextView) findViewById(R.id.dir_path);
        this.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] e = MarkedItemList.e();
                if (FilePickerDialog.this.callbacks != null) {
                    FilePickerDialog.this.callbacks.onSelectedFilePaths(e);
                }
                FilePickerDialog.this.dismiss();
            }
        });
        ((RelativeLayout) findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerDialog.this.dismiss();
                if (FilePickerDialog.this.callbacks != null) {
                    FilePickerDialog.this.callbacks.onSelectedFilePaths(null);
                }
            }
        });
        FileListAdapter fileListAdapter = new FileListAdapter(this.internalList, this.context, this.properties);
        this.mFileListAdapter = fileListAdapter;
        fileListAdapter.c(new NotifyItemChecked() {
            @Override
            public void notifyCheckBoxIsClicked() {
                int d2 = MarkedItemList.d();
                if (d2 == 0) {
                    FilePickerDialog.this.select.setEnabled(false);
                } else {
                    FilePickerDialog.this.select.setEnabled(true);
                }
                if (FilePickerDialog.this.properties.selection_mode == 0) {
                    FilePickerDialog.this.mFileListAdapter.notifyDataSetChanged();
                }
            }
        });
        this.listView.setAdapter((ListAdapter) this.mFileListAdapter);
        setTitle();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (this.internalList.size() > i) {
            FileListItem fileListItem = this.internalList.get(i);
            if (fileListItem.isDirectory()) {
                if (new File(fileListItem.getLocation()).canRead()) {
                    File file = new File(fileListItem.getLocation());
                    this.dname.setText(file.getName());
                    setTitle();
                    this.dir_path.setText(file.getAbsolutePath());
                    this.internalList.clear();
                    if (!file.getName().equals(this.properties.root.getName())) {
                        FileListItem fileListItem2 = new FileListItem();
                        fileListItem2.setFilename("...");
                        fileListItem2.setDirectory(true);
                        fileListItem2.setLocation(file.getParentFile().getAbsolutePath());
                        fileListItem2.setTime(file.lastModified());
                        this.internalList.add(fileListItem2);
                    }
                    this.internalList = Utility.prepareFileListEntries(this.internalList, file, this.filter);
                    this.mFileListAdapter.notifyDataSetChanged();
                    return;
                }
                Context context = this.context;
                Toast.makeText(context, "" + this.context.getResources().getString(R.string.directory_cannot_accessed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        File file;
        super.onStart();
        if (Utility.checkStorageAccessPermissions(this.context)) {
            if (this.properties.root.exists() && this.properties.root.isDirectory()) {
                file = new File(this.properties.root.getAbsolutePath());
            } else {
                file = new File(this.properties.f4894a.getAbsolutePath());
            }
            this.dname.setText(file.getName());
            this.dir_path.setText(file.getAbsolutePath());
            setTitle();
            this.internalList.clear();
            this.internalList = Utility.prepareFileListEntries(this.internalList, file, this.filter);
            this.mFileListAdapter.notifyDataSetChanged();
            this.listView.setOnItemClickListener(this);
        }
    }

    public void setDialogSelectionListener(DialogSelectionListener dialogSelectionListener) {
        this.callbacks = dialogSelectionListener;
    }

    public void setProperties(DialogProperties dialogProperties) {
        this.properties = dialogProperties;
        this.filter = new ExtensionFilter(dialogProperties);
    }

    @Override
    public void show() {
        if (!Utility.checkStorageAccessPermissions(this.context)) {
            if (Build.VERSION.SDK_INT >= 23) {
                Context context = this.context;
                Toast.makeText(context, "" + this.context.getResources().getString(R.string.application_needs_permission), Toast.LENGTH_LONG).show();
                ((Activity) this.context).requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 112);
                ((Activity) this.context).requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 113);
                return;
            }
            return;
        }
        super.show();
    }

    public FilePickerDialog(Context context, DialogProperties dialogProperties) {
        super(context);
        this.titleStr = null;
        this.context = context;
        this.properties = dialogProperties;
        this.filter = new ExtensionFilter(dialogProperties);
        this.internalList = new ArrayList<>();
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        if (charSequence != null) {
            this.titleStr = charSequence.toString();
        } else {
            this.titleStr = null;
        }
        setTitle();
    }
}
