package com.cleanPhone.mobileCleaner.notifimanager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.codersroute.flexiblewidgets.FlexibleSwitch;
import com.github.ybq.android.spinkit.SpinKitView;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppDetails;
import com.cleanPhone.mobileCleaner.wrappers.PackageInfoStruct;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NotificationIgnoreList extends ParentActivity {
    private EventBus bus = EventBus.getDefault();
    private boolean isTouched = false;
    public ArrayList<PackageInfoStruct> j = new ArrayList<>();
    private ListView listView;
    private SwitchCompat onOffView;
    private PackageManager packageManager;
    private SharedPrefUtil sharedPrefUtil;
    public SpinKitView pbar;
    int admob = 1;
    private final int VIEW_TYPE_EXAMPLE = 0;
    private final int VIEW_TYPE_EXAMPLE_TWO = 1;

    public class IgnoreListAdapter extends ArrayAdapter<PackageInfoStruct> {
        private final Context context;
        private LayoutInflater inflater;
        private ArrayList<PackageInfoStruct> list;

        public class ViewHolder {
            public ImageView f4976a;
            public TextView b;
            public FlexibleSwitch f4977c;

            public ViewHolder(View ignoreListAdapter) {
            }
        }



        public IgnoreListAdapter(Context context, int i, ArrayList<PackageInfoStruct> arrayList) {
            super(context, i, arrayList);
            this.context = context;
            this.list = arrayList;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        @NonNull
        public View getView(final int i, View view, @NonNull ViewGroup viewGroup) {

            ViewHolder viewHolder=null;
            if (view == null) {
                viewHolder = new ViewHolder(view);
                view = LayoutInflater.from(getContext()).inflate(R.layout.ignore_item, viewGroup, false);
                viewHolder.f4976a = (ImageView) view.findViewById(R.id.ignorelistitemimage);
                viewHolder.b = (TextView) view.findViewById(R.id.ignorelistitemapp);
                viewHolder.f4977c = (FlexibleSwitch) view.findViewById(R.id.ignorelistitemcheck);
                viewHolder.f4977c.setFocusable(false);
                viewHolder.f4977c.setClickable(false);
//                        view.setTag(viewHolder);

//                    else {
//                        view = view;
//                        viewHolder = (ViewHolder) view.getTag();
//                    }
                PackageInfoStruct packageInfoStruct = this.list.get(i);
                if (packageInfoStruct.pname != null) {
                    try {
                        viewHolder.f4976a.setImageDrawable(this.context.getPackageManager().getApplicationIcon(packageInfoStruct.pname));
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                TextView textView = viewHolder.b;
                textView.setText("" + packageInfoStruct.appname);
                ViewHolder finalViewHolder = viewHolder;
                viewHolder.f4977c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view3) {
                        NotificationIgnoreList.this.isTouched = false;
                        PackageInfoStruct packageInfoStruct2 = NotificationIgnoreList.this.j.get(i);
                        packageInfoStruct2.ischecked = finalViewHolder.f4977c.isChecked();
                        if (NotificationIgnoreList.this.allChecked()) {
                            NotificationIgnoreList.this.sharedPrefUtil.saveBooleanToggle(SharedPrefUtil.NOTI_TOGGLE_ON, true);
                            NotificationIgnoreList.this.onOffView.setChecked(true);
                            NotificationIgnoreList.this.onOffView.setText(NotificationIgnoreList.this.getString(R.string.mbc_noti_automatic));
                        }
                        if (!finalViewHolder.f4977c.isChecked()) {
                            NotificationIgnoreList.this.sharedPrefUtil.saveBooleanToggle(SharedPrefUtil.NOTI_TOGGLE_ON, false);
                            NotificationIgnoreList.this.onOffView.setText(NotificationIgnoreList.this.getString(R.string.mbc_noti_manual));
                            NotificationIgnoreList.this.onOffView.setChecked(false);
                            finalViewHolder.f4977c.setChecked(false);
                            if (NotificationIgnoreList.this.allUnChecked()) {
                                NotificationIgnoreList.this.sharedPrefUtil.saveBooleanToggle(SharedPrefUtil.NOTICLEANER_ON, false);
                            }
                            NotificationIgnoreList notificationIgnoreList = NotificationIgnoreList.this;
                            notificationIgnoreList.removeFromNotificationList("" + packageInfoStruct2.appname);
                            Context context = IgnoreListAdapter.this.context;
                            Toast.makeText(context, NotificationIgnoreList.this.getResources().getString(R.string.mbc_noti_no_longer) + " " + packageInfoStruct2.appname, Toast.LENGTH_SHORT).show();
                        } else {
                            finalViewHolder.f4977c.setChecked(true);
                            Context context2 = IgnoreListAdapter.this.context;
                            Toast.makeText(context2, "" + packageInfoStruct2.appname + " " + NotificationIgnoreList.this.getResources().getString(R.string.mbc_noti_enable), Toast.LENGTH_SHORT).show();
                        }
                        try {
                            GlobalData.saveObj(IgnoreListAdapter.this.context, GlobalData.IgnoreList, NotificationIgnoreList.this.j);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        NotificationIgnoreList.this.updateNotofication();
                    }
                });
                if (packageInfoStruct.ischecked) {
                    viewHolder.f4977c.setChecked(true);
                } else {
                    viewHolder.f4977c.setChecked(false);
//                        }
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

    public boolean allChecked() {
        for (int i = 0; i < this.j.size(); i++) {
            if (!this.j.get(i).ischecked) {
                return false;
            }
        }
        return true;
    }

    public boolean allUnChecked() {
        for (int i = 0; i < this.j.size(); i++) {
            if (this.j.get(i).ischecked) {
                return false;
            }
        }
        return true;
    }

    public void checkUncheckAll(boolean z) {
        this.sharedPrefUtil.saveBooleanToggle(SharedPrefUtil.NOTICLEANER_ON, z);
        getIgnoreList();
        for (int i = 0; i < this.j.size(); i++) {
            this.j.get(i).ischecked = z;
        }
        try {
            GlobalData.saveObj(this, GlobalData.IgnoreList, this.j);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setAdapter();
    }

    private Drawable getADrawable(int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return getResources().getDrawable(i, getTheme());
        }
        return getResources().getDrawable(i);
    }

    private Bitmap getBitmap(String str) {
        try {
            Drawable applicationIcon = getPackageManager().getApplicationIcon(str);
            Bitmap createBitmap = Bitmap.createBitmap(applicationIcon.getIntrinsicWidth(), applicationIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            applicationIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            applicationIcon.draw(canvas);
            return createBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return ((BitmapDrawable) getADrawable(R.drawable.ic_android)).getBitmap();
        }
    }


    public ArrayList<PackageInfoStruct> getIgnoreList() {
        this.j = new AppDetails(this).getApplicationsInfo(this);
        ArrayList arrayList = new ArrayList();
        if (GlobalData.isObjExist(this, GlobalData.IgnoreList)) {
            try {
                arrayList = (ArrayList) GlobalData.getObj(this, GlobalData.IgnoreList);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (arrayList.size() > 0) {
            for (int i = 0; i < this.j.size(); i++) {
                int i2 = 0;
                while (true) {
                    if (i2 >= arrayList.size()) {
                        break;
                    } else if (this.j.get(i).appname.equals(((PackageInfoStruct) arrayList.get(i2)).appname)) {
                        this.j.get(i).ischecked = ((PackageInfoStruct) arrayList.get(i2)).ischecked;
                        break;
                    } else {
                        i2++;
                    }
                }
            }
        }
        try {
            Collections.sort(this.j, new Comparator<PackageInfoStruct>() {
                @Override
                public int compare(PackageInfoStruct packageInfoStruct, PackageInfoStruct packageInfoStruct2) {
                    return Boolean.compare(packageInfoStruct2.ischecked, packageInfoStruct.ischecked);
                }
            });
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            GlobalData.saveObj(this, GlobalData.IgnoreList, this.j);
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        return this.j;
    }

    private ArrayList<PackageInfoStruct> getList() {
        if (this.sharedPrefUtil.getBooleanToggle(SharedPrefUtil.NOTI_TOGGLE_ON)) {
            ArrayList<PackageInfoStruct> arrayList = new ArrayList<>();
            for (int i = 0; i < this.j.size(); i++) {
                arrayList.add(this.j.get(i));
                arrayList.get(i).ischecked = true;
            }
            return arrayList;
        }
        return this.j;
    }

    private boolean ignored(String str) {
        if (GlobalData.isObjExist(this, GlobalData.IgnoreList)) {
            ArrayList arrayList = null;
            try {
                arrayList = (ArrayList) GlobalData.getObj(this, GlobalData.IgnoreList);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (arrayList == null) {
                return false;
            }
            for (int i = 0; i < arrayList.size(); i++) {
                if (((PackageInfoStruct) arrayList.get(i)).appname.equalsIgnoreCase(str) && !((PackageInfoStruct) arrayList.get(i)).ischecked) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }


    public void removeAllFromPreference() {
        if (GlobalData.isObjExist(this, GlobalData.NotificationList)) {
            GlobalData.deleteObj(this, GlobalData.NotificationList);
        }
    }


    public void removeFromNotificationList(String str) {
        if (GlobalData.isObjExist(this, GlobalData.NotificationList)) {
            try {
                ArrayList arrayList = (ArrayList) GlobalData.getObj(this, GlobalData.NotificationList);
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    if (((NotifcationWrapper) arrayList.get(i)).appname.equalsIgnoreCase(str)) {
                        arrayList.remove(i);
                        size--;
                    } else {
                        i++;
                    }
                }
                if (arrayList.size() == 0) {
                    GlobalData.deleteObj(this, GlobalData.NotificationList);
                } else {
                    GlobalData.saveObj(this, GlobalData.NotificationList, arrayList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setAdapter() {
        this.listView.setAdapter((ListAdapter) new IgnoreListAdapter(this, R.layout.ignore_item, getList()));
    }

    @SuppressLint("WrongConstant")
    public void updateNotofication() {
        PendingIntent activity;
        RemoteViews remoteViews;
        Notification build;
        ArrayList arrayList;
        int i;
        int i2;
        ArrayList arrayList2 = new ArrayList();
        if (GlobalData.isObjExist(this, GlobalData.NotificationList)) {
            try {
                arrayList2 = (ArrayList) GlobalData.getObj(this, GlobalData.NotificationList);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            GlobalData.fromNotificationsetting = false;
            Intent intent = new Intent(getBaseContext(), NotificationCleanerActivity.class);
            if (Build.VERSION.SDK_INT >= 23) {
                activity = PendingIntent.getActivity(this, 555, intent, 201326592);
            } else {
                activity = PendingIntent.getActivity(this, 555, intent, 134217728);
            }
            remoteViews = new RemoteViews(getPackageName(), (int) R.layout.custom_notification);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "707");
            builder.setSmallIcon(R.drawable.splesh_logo);
            builder.setContent(remoteViews);
            build = builder.build();
            arrayList = new ArrayList();
            i = 0;
            i2 = 0;
        } catch (Exception e3) {
            e3.printStackTrace();
            return;
        }
        while (true) {
            String str = "";
            if (i >= arrayList2.size()) {
                break;
            }
            if (this.packageManager == null) {
                this.packageManager = getPackageManager();
            }
            try {
                str = ((NotifcationWrapper) arrayList2.get(i)).appname;
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            if (!ignored(str)) {
                i2++;
                if (!arrayList.contains(((NotifcationWrapper) arrayList2.get(i)).pkg)) {
                    arrayList.add(((NotifcationWrapper) arrayList2.get(i)).pkg);
                }
            }
            i++;

            return;
        }
        if (i2 == 0) {
            NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
            if (notificationManager != null) {
                notificationManager.cancel(808);
            }
            GlobalData.deleteObj(this, GlobalData.NotificationList);
            return;
        }
        try {
            remoteViews.removeAllViews(R.id.layout);
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            RemoteViews remoteViews2 = new RemoteViews(getPackageName(), (int) R.layout.img_notification);
            try {
                remoteViews2.setImageViewBitmap(R.id.img, getBitmap((String) arrayList.get(i3)));
            } catch (Exception e6) {
                e6.printStackTrace();
            }
            remoteViews.addView(R.id.layout, remoteViews2);
        }
        if (i2 == 1) {
            remoteViews.setTextViewText(R.id.tvtitle, "" + i2 + " " + getString(R.string.mbc_junknotification));
        } else {
            remoteViews.setTextViewText(R.id.tvtitle, "" + i2 + " " + getString(R.string.mbc_junknotification));
        }
        remoteViews.setTextViewText(R.id.btn_noti, "" + getString(R.string.mbc_clean));
        try {
            remoteViews.setOnClickPendingIntent(R.id.btn_noti, activity);
        } catch (Exception e7) {
            e7.printStackTrace();
        }
        NotificationManager notificationManager2 = (NotificationManager) getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("707", "Notifications", 3);
            if (notificationManager2 != null) {
                notificationManager2.createNotificationChannel(notificationChannel);
            }
        }
        if (notificationManager2 != null) {
            notificationManager2.notify(808, build);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void B() {
        new AsyncTask<String, String, String>() {

            @Override
            public void onPreExecute() {
                super.onPreExecute();
                findViewById(R.id.pbarlayout).setVisibility(View.VISIBLE);
            }

            @Override
            public String doInBackground(String... strArr) {
                NotificationIgnoreList.this.getIgnoreList();
                return null;
            }

            @Override
            public void onPostExecute(String str) {
                NotificationIgnoreList.this.setAdapter();
                findViewById(R.id.pbarlayout).setVisibility(View.GONE);
            }
        }.execute(new String[0]);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        try {
            GlobalData.saveObj(this, GlobalData.IgnoreList, this.j);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);

        GlobalData.notificationBack = true;
        setContentView(R.layout.activity_notification_ignore_list);
        Util.saveScreen(getClass().getName(), this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.listView = (ListView) findViewById(R.id.listview_ignore);
        this.onOffView = (SwitchCompat) findViewById(R.id.on_off_notification);
        pbar = findViewById(R.id.pbar);
       this.sharedPrefUtil = new SharedPrefUtil(this);
        this.onOffView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                NotificationIgnoreList notificationIgnoreList;
                int i;
                if (NotificationIgnoreList.this.isTouched) {
                    NotificationIgnoreList.this.sharedPrefUtil.saveBooleanToggle(SharedPrefUtil.NOTI_TOGGLE_ON, z);
                    if (!z) {
                        NotificationIgnoreList.this.removeAllFromPreference();
                        NotificationIgnoreList.this.checkUncheckAll(false);
                        NotificationManager notificationManager = (NotificationManager) NotificationIgnoreList.this.getSystemService(Context.NOTIFICATION_SERVICE);
                        if (notificationManager != null) {
                            notificationManager.cancel(808);
                        }
                        NotificationIgnoreList.this.bus.post(Integer.valueOf((int) GlobalData.EB_CODE_NOTIFICATION_REMOVE));
                    } else {
                        NotificationIgnoreList.this.checkUncheckAll(true);
                    }
                    NotificationIgnoreList.this.B();
                    SwitchCompat switchCompat = NotificationIgnoreList.this.onOffView;
                    if (z) {
                        notificationIgnoreList = NotificationIgnoreList.this;
                        i = R.string.mbc_noti_automatic;
                    } else {
                        notificationIgnoreList = NotificationIgnoreList.this;
                        i = R.string.mbc_noti_manual;
                    }
                    switchCompat.setText(notificationIgnoreList.getString(i));
                }
            }
        });
        this.onOffView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                NotificationIgnoreList.this.isTouched = true;
                return false;
            }
        });
        this.onOffView.setChecked(this.sharedPrefUtil.getBooleanToggle(SharedPrefUtil.NOTI_TOGGLE_ON));
        SwitchCompat switchCompat = this.onOffView;
        switchCompat.setText(getString(switchCompat.isChecked() ? R.string.mbc_noti_automatic : R.string.mbc_noti_manual));
        B();
        setAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ignore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.already_purchased) {
            final Dialog dialog = new Dialog(this);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
            }
            dialog.setContentView(R.layout.new_dialog_junk_cancel);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setGravity(17);
            dialog.findViewById(R.id.dialog_img).setVisibility(View.GONE);
            dialog.findViewById(R.id.dialog_title).setVisibility(View.GONE);
            ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(getString(R.string.noti_ignore_info));
            dialog.findViewById(R.id.ll_yes_no).setVisibility(View.GONE);
            dialog.findViewById(R.id.ll_ok).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.ll_ok).setOnClickListener(new View.OnClickListener() {
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (NotificationIgnoreList.this.multipleClicked()) {
                        return;
                    }
                    dialog.dismiss();
                }
            });

                    dialog.show();



        } else {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }


}
