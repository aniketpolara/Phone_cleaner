package com.cleanPhone.mobileCleaner;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.JunkListWrapper;

import java.util.ArrayList;

public class DeleteCacheActivity extends AppCompatActivity {
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    private static final String APP_PKG_NAME_22 = "pkg";
    private static final String SCHEME = "package";

    public class CacheListAdapter extends ArrayAdapter<JunkListWrapper> {
        private final Context context;
        private View convertView;
        private final ArrayList<JunkListWrapper> data;
        private LayoutInflater inflater;

        public class ViewHolder {
            public ImageView f4587a;
            public TextView b;
            public TextView f4588c;
            public CheckBox checkbox;

            public ViewHolder(CacheListAdapter cacheListAdapter) {
            }
        }

        public CacheListAdapter(Context context, int i, ArrayList<JunkListWrapper> arrayList) {
            super(context, i, arrayList);
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data = arrayList;
        }

        @Override
        @NonNull
        public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
            View view2;
            ViewHolder viewHolder;
            this.convertView = view;
            if (view == null) {
                viewHolder = new ViewHolder(this);
                view2 = this.inflater.inflate(R.layout.junklistitemlayout, (ViewGroup) null);
                viewHolder.f4587a = (ImageView) view2.findViewById(R.id.junklistitemimage);
                viewHolder.f4588c = (TextView) view2.findViewById(R.id.junklistitemapp);
                viewHolder.b = (TextView) view2.findViewById(R.id.junklistitemsize);
                CheckBox checkBox = (CheckBox) view2.findViewById(R.id.junklistitemcheck);
                viewHolder.checkbox = checkBox;
                checkBox.setFocusable(false);
                viewHolder.checkbox.setClickable(false);
                view2.setTag(viewHolder);
            } else {
                view2 = view;
                viewHolder = (ViewHolder) view.getTag();
            }
            final JunkListWrapper junkListWrapper = this.data.get(i);
            if (junkListWrapper.pkg != null) {
                try {
                    viewHolder.f4587a.setImageDrawable(this.context.getPackageManager().getApplicationIcon(junkListWrapper.pkg));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (junkListWrapper.type.equalsIgnoreCase(JunkScanActivity.SYSTEMCACHE)) {
                viewHolder.checkbox.setVisibility(View.GONE);
            }
            TextView textView = viewHolder.f4588c;
            textView.setText("" + junkListWrapper.name);
            TextView textView2 = viewHolder.b;
            textView2.setText("" + Util.convertBytes(junkListWrapper.size));
            viewHolder.b.setVisibility(View.GONE);
            view2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view3) {
                    Intent intent = new Intent();
                    int i2 = Build.VERSION.SDK_INT;
                    if (i2 >= 9) {
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts(DeleteCacheActivity.SCHEME, junkListWrapper.pkg, null));
                    } else {
                        String str = i2 == 8 ? DeleteCacheActivity.APP_PKG_NAME_22 : DeleteCacheActivity.APP_PKG_NAME_21;
                        intent.setAction("android.intent.action.VIEW");
                        intent.setClassName(DeleteCacheActivity.APP_DETAILS_PACKAGE_NAME, DeleteCacheActivity.APP_DETAILS_CLASS_NAME);
                        intent.putExtra(str, junkListWrapper.pkg);
                    }
                    try {
                        CacheListAdapter.this.context.startActivity(intent);
                        DeleteCacheActivity deleteCacheActivity = DeleteCacheActivity.this;
                        Toast.makeText(deleteCacheActivity, deleteCacheActivity.getResources().getString(R.string.mbc_tap_storage), Toast.LENGTH_SHORT).show();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            });
            if (junkListWrapper.ischecked) {
                viewHolder.checkbox.setChecked(true);
            } else {
                viewHolder.checkbox.setChecked(false);
            }
            return view2;
        }
    }

    private ArrayList<JunkListWrapper> filterSmall(ArrayList<JunkListWrapper> arrayList) {
        ArrayList<JunkListWrapper> arrayList2 = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).size > 12288) {
                arrayList2.add(arrayList.get(i));
            }
        }
        return arrayList2;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        Util.saveScreen(getClass().getName(), this);
        setContentView(R.layout.cache_delete_screen);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
        }
        ((ListView) findViewById(R.id.listcachedelte)).setAdapter((ListAdapter) new CacheListAdapter(this, R.layout.junklistitemlayout, filterSmall(GlobalData.cacheContainingApps)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
