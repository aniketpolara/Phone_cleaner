package com.cleanPhone.mobileCleaner.notifimanager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.JunkDeleteAnimationScreen;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.SwipeDismissListViewTouchListener;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppDetails;
import com.cleanPhone.mobileCleaner.wrappers.PackageInfoStruct;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class NotificationCleanerActivity extends ParentActivity {
    private NotificationAdapter adapter;
    private Context context;
    private SimpleDateFormat dformat;
    private boolean isOn;
    private ListView listViewNoti;
    private FrameLayout mAdView3_rect;
    private NotificationReceiver nReceiver;
    public ArrayList<NotifcationWrapper> notificationDataList;
    private boolean redirectToNoti;
    private SharedPrefUtil sharedPrefUtil;
    private RelativeLayout tvClear;
    private TextView tvNotiCount;
    private TextView tvNotisstatus;
    public ImageView back, setting;
    private int count = 0;
    int admob = 2;

    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    public EventBus j = EventBus.getDefault();
    private String TAG = "NotificationCleanerActivity";


    public class NotificationAdapter extends ArrayAdapter<NotifcationWrapper> {
        public NotificationAdapter(Context context, int i) {
            super(context, i);
        }

        @Override
        public int getCount() {
            return NotificationCleanerActivity.this.notificationDataList.size();
        }

        @Override
        public NotifcationWrapper getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0L;
        }

        @Override
        @NonNull
        public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
            LayoutInflater layoutInflater = (LayoutInflater) NotificationCleanerActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                view = layoutInflater.inflate(R.layout.notificationdata_layout, (ViewGroup) null, true);
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.app_icon);
            NotifcationWrapper notifcationWrapper = NotificationCleanerActivity.this.notificationDataList.get(i);
            ((TextView) view.findViewById(R.id.tvapp)).setText("" + notifcationWrapper.appname);
            ((TextView) view.findViewById(R.id.tvappmsg)).setText(notifcationWrapper.title + " " + notifcationWrapper.text);
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(NotificationCleanerActivity.this.dformat.format(new Date(notifcationWrapper.postTime)));
            ((TextView) view.findViewById(R.id.tvtime)).setText(sb.toString());
            return view;
        }
    }


    public class NotificationReceiver extends BroadcastReceiver {
        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("notification_event");
            if (stringExtra == null || !stringExtra.equalsIgnoreCase("received")) {
                return;
            }
            NotificationCleanerActivity.this.addToList();
            NotificationCleanerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    NotificationCleanerActivity notificationCleanerActivity = NotificationCleanerActivity.this;
                    if (notificationCleanerActivity.notificationDataList == null || notificationCleanerActivity.listViewNoti == null) {
                        return;
                    }
                    NotificationCleanerActivity.this.setAdapter(true);
                }
            });
        }
    }


    public void addToList() {
        this.notificationDataList = new ArrayList<>();
        if (this.isOn) {
            try {
                if (this.context != null) {
                    if (GlobalData.isObjExist(this, GlobalData.NotificationList)) {
                        try {
                            try {
                                this.notificationDataList = (ArrayList) GlobalData.getObj(this, GlobalData.NotificationList);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (this.notificationDataList.size() > 0) {
                        new ArrayList();
                        ArrayList arrayList = (ArrayList) GlobalData.getObj(this, GlobalData.IgnoreList);
                        int size = this.notificationDataList.size();
                        if (arrayList.size() > 0) {
                            int i = 0;
                            while (i < size) {
                                boolean z = false;
                                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                                    if (this.notificationDataList.get(i).appname.equalsIgnoreCase(((PackageInfoStruct) arrayList.get(i2)).appname) && !((PackageInfoStruct) arrayList.get(i2)).ischecked) {
                                        this.notificationDataList.remove(i);
                                        z = true;
                                    }
                                }
                                if (z) {
                                    size--;
                                } else {
                                    i++;
                                }
                            }
                        }
                        if (this.notificationDataList.size() > 0) {
                            GlobalData.saveObj(this, GlobalData.NotificationList, this.notificationDataList);
                        } else {
                            GlobalData.deleteObj(this, GlobalData.NotificationList);
                        }
                    }
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }


    public void checkPermissionAvailable() {
        boolean z = false;
        for (String str : NotificationManagerCompat.getEnabledListenerPackages(this)) {
            if (str.equals(getPackageName())) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    GlobalData.fromNotificationsetting = true;
                    Intent intent2 = new Intent(NotificationCleanerActivity.this, SettingOverlayScreen.class);
                    intent2.putExtra("PERM_TEXT", NotificationCleanerActivity.this.getResources().getString(R.string.mbc_permisson_notification).replace("APP_NAME", NotificationCleanerActivity.this.getResources().getString(R.string.mbc_app_name)));
                    NotificationCleanerActivity.this.startActivity(intent2);



            }
        }, 1000L);
    }


    public void cleanNotification(int i, boolean z) {
        try {
            if (GlobalData.isObjExist(this, GlobalData.NotificationList)) {
                try {
                    try {
                        this.notificationDataList = (ArrayList) GlobalData.getObj(this, GlobalData.NotificationList);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            if (z) {
                startActivity(getPackageManager().getLaunchIntentForPackage(this.notificationDataList.get(i).pkg));
            }
            this.notificationDataList.remove(i);
            NotificationAdapter notificationAdapter = this.adapter;
            notificationAdapter.remove(notificationAdapter.getItem(i));
            setAdapter(z);
            if (this.notificationDataList.size() == 0) {
                findViewById(R.id.tvnoti_empty).setVisibility(View.VISIBLE);
                this.tvNotiCount.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                GlobalData.deleteObj(this, GlobalData.NotificationList);
            } else {
                findViewById(R.id.tvnoti_empty).setVisibility(View.GONE);
                try {
                    GlobalData.saveObj(this, GlobalData.NotificationList, this.notificationDataList);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
            updateNotofication(this.notificationDataList.size());
        } catch (Exception e4) {
            e4.printStackTrace();
        }
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
        AppDetails appDetails = new AppDetails(this);
        ArrayList<PackageInfoStruct> arrayList = new ArrayList<>();
        if (GlobalData.isObjExist(this, GlobalData.IgnoreList)) {
            try {
                return (ArrayList) GlobalData.getObj(this, GlobalData.IgnoreList);
            } catch (IOException e) {
                e.printStackTrace();
                return arrayList;
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
                return arrayList;
            }
        }
        ArrayList<PackageInfoStruct> applicationsInfo = appDetails.getApplicationsInfo(this);
        if (arrayList.size() > 0) {
            for (int i = 0; i < applicationsInfo.size(); i++) {
                int i2 = 0;
                while (true) {
                    if (i2 >= arrayList.size()) {
                        break;
                    } else if (applicationsInfo.get(i).appname.equals(arrayList.get(i2).appname)) {
                        applicationsInfo.get(i).ischecked = arrayList.get(i2).ischecked;
                        break;
                    } else {
                        i2++;
                    }
                }
            }
        }
        try {
            Collections.sort(applicationsInfo, new Comparator<PackageInfoStruct>() {
                @Override
                public int compare(PackageInfoStruct packageInfoStruct, PackageInfoStruct packageInfoStruct2) {
                    return Boolean.compare(packageInfoStruct2.ischecked, packageInfoStruct.ischecked);
                }
            });
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            GlobalData.saveObj(this, GlobalData.IgnoreList, applicationsInfo);
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        return applicationsInfo;
    }


    private Intent getIntentDeep(PendingIntent pendingIntent) throws IllegalStateException {
        try {
            char c2 = 0;
            Object invoke = Class.forName("android.app.ActivityManagerNative").getDeclaredMethod("getDefault", new Class[0]).invoke(null, new Object[0]);
            if (invoke != null) {
                @SuppressLint("SoonBlockedPrivateApi") Field declaredField = PendingIntent.class.getDeclaredField("mTarget");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(pendingIntent);
                if (obj != null) {
                    String name = invoke.getClass().getName();
                    int hashCode = name.hashCode();
                    if (hashCode != 1800859098) {
                        if (hashCode == 1906460636 && name.equals("android.app.ActivityManagerProxy")) {
                            if (c2 == 0) {
                                if (c2 == 1) {
                                    return getIntentFromService(obj);
                                }
                                throw new IllegalStateException("Unsupported IActivityManager inheritor: " + name);
                            }
                            return getIntentFromProxy(invoke, obj);
                        }
                        c2 = '\uffff';
                        if (c2 == 0) {
                        }
                    } else {
                        if (name.equals("com.android.server.am.ActivityManagerService")) {
                            c2 = 1;
                            if (c2 == 0) {
                            }
                        }
                        c2 = '\uffff';
                        if (c2 == 0) {
                        }
                    }
                } else {
                    throw new IllegalStateException("PendingIntent.mTarget field is null");
                }
            } else {
                throw new IllegalStateException("ActivityManagerNative.getDefault() returned null");
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return null;
    }

    @RequiresApi(api = 19)
    private Intent getIntentFromProxy(Object obj, Object obj2) {
        try {
            Field declaredField = Class.forName("android.app.ActivityManagerProxy").getDeclaredField("mRemote");
            declaredField.setAccessible(true);
            IBinder iBinder = (IBinder) declaredField.get(obj);
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            obtain.writeInterfaceToken("android.app.IActivityManager");
            obtain.writeStrongBinder(((IInterface) obj2).asBinder());
            transact(iBinder, obtain, obtain2, 0);
            obtain2.readException();
            Intent intent = obtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(obtain2) : null;
            obtain.recycle();
            obtain2.recycle();
            return intent;
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
    }

    @RequiresApi(api = 19)
    private Intent getIntentFromService(Object obj) {
        if (obj.getClass().getName().equals("com.android.server.am.PendingIntentRecord")) {
            try {
                Object obj2 = Class.forName("com.android.server.am.PendingIntentRecord").getDeclaredField("key").get(obj);
                Field declaredField = Class.forName("com.android.server.am.PendingIntentRecord$Key").getDeclaredField("requestIntent");
                declaredField.setAccessible(true);
                Intent intent = (Intent) declaredField.get(obj2);
                if (intent != null) {
                    return new Intent(intent);
                }
                return null;
            } catch (ClassCastException unused) {
                return null;
            } catch (ClassNotFoundException e) {
                e = e;
                throw new IllegalStateException(e);
            } catch (IllegalAccessException e2) {
                throw new IllegalStateException(e2);
            } catch (NoSuchFieldException e3) {
                throw new IllegalStateException(e3);
            }
        }
        return null;
    }

    private void redirectToNoti() {
        this.redirectToNoti = getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false);
    }

    public void removeAllFromPreference() {
    }

    public void setAdapter(boolean z) {
        if (z && this.isOn) {
            NotificationAdapter notificationAdapter = new NotificationAdapter(this, R.layout.notificationdata_layout);
            this.adapter = notificationAdapter;
            this.listViewNoti.setAdapter((ListAdapter) notificationAdapter);
        }
        if (this.notificationDataList.size() == 0) {
            Util.isAdsFree(this);
            this.listViewNoti.setVisibility(View.GONE);
            if (!Util.isConnectingToInternet(this.context) || Util.isAdsFree(this.context)) {
                findViewById(R.id.tvnoti_empty).setVisibility(View.VISIBLE);
            }
            this.tvNotisstatus.setText(getString(R.string.mbc_no_notification));
            this.tvNotiCount.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
            this.tvClear.setVisibility(View.GONE);
        } else {
            this.listViewNoti.setVisibility(View.VISIBLE);
            findViewById(R.id.tvnoti_empty).setVisibility(View.GONE);
            Util.isConnectingToInternet(this.context);
            this.tvClear.setVisibility(View.VISIBLE);
            this.tvNotiCount.setVisibility(View.VISIBLE);
        }
        NotificationAdapter notificationAdapter2 = this.adapter;
        if (notificationAdapter2 == null) {
            return;
        }
        notificationAdapter2.notifyDataSetChanged();
        this.tvNotisstatus.setText(getString(R.string.mbc_notifications));
        if (this.adapter.getCount() > 0) {

            this.listViewNoti.setVisibility(View.VISIBLE);
            this.tvClear.setVisibility(View.VISIBLE);
        } else {

            this.listViewNoti.setVisibility(View.GONE);
            this.tvClear.setVisibility(View.GONE);
        }
        TextView textView = this.tvNotiCount;
        textView.setText("" + this.adapter.getCount());
    }

    private void setListners() {
    }

    private void set_adsdimention() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
    }

    private boolean transact(IBinder iBinder, Parcel parcel, Parcel parcel2, int i) {
        return true;
    }

    @SuppressLint("WrongConstant")
    private void updateNotofication(int i) {
        PendingIntent activity;
        if (i == 0) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.cancel(808);
                return;
            }
            return;
        }
        GlobalData.fromNotificationsetting = false;
        Intent intent = new Intent(getBaseContext(), NotificationCleanerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 23) {
            activity = PendingIntent.getActivity(this, 555, intent, 201326592);
        } else {
            activity = PendingIntent.getActivity(this, 555, intent, 134217728);
        }
        RemoteViews remoteViews = new RemoteViews(getPackageName(), (int) R.layout.custom_notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "707");
        builder.setSmallIcon(R.drawable.splesh_logo);
        builder.setContent(remoteViews);
        Notification build = builder.build();
        int i2 = build.defaults | 2;
        build.defaults = i2;
        int i3 = i2 | 1;
        build.defaults = i3;
        build.defaults = i3 | 4;
        ArrayList arrayList = new ArrayList();
        for (int i4 = 0; i4 < this.notificationDataList.size(); i4++) {
            if (!arrayList.contains(this.notificationDataList.get(i4).pkg)) {
                arrayList.add(this.notificationDataList.get(i4).pkg + "");
            }
        }
        try {
            remoteViews.removeAllViews(R.id.layout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i5 = 0; i5 < arrayList.size(); i5++) {
            RemoteViews remoteViews2 = new RemoteViews(getPackageName(), (int) R.layout.img_notification);
            try {
                remoteViews2.setImageViewBitmap(R.id.img, getBitmap((String) arrayList.get(i5)));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            remoteViews.addView(R.id.layout, remoteViews2);
        }
        String str = "<b><font color='#202b65'>" + this.notificationDataList.size() + "</font></b><font color='#202b65'> " + getString(R.string.mbc_junknotification) + "</font>";
        if (this.notificationDataList.size() == 1) {
            remoteViews.setTextViewText(R.id.tvtitle, Html.fromHtml(str));
        } else {
            remoteViews.setTextViewText(R.id.tvtitle, Html.fromHtml(str));
        }
        remoteViews.setTextViewText(R.id.btn_noti, "" + getString(R.string.mbc_clean));
        remoteViews.setOnClickPendingIntent(R.id.btn_noti, activity);
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

    @RequiresApi(api = 19)
    public Intent getIntent(PendingIntent pendingIntent) throws IllegalStateException {
        try {
            return (Intent) PendingIntent.class.getDeclaredMethod("getIntent", new Class[0]).invoke(pendingIntent, new Object[0]);
        } catch (IllegalAccessException e) {
            e = e;
            e.getCause();
            throw new IllegalStateException(e);
        } catch (NoSuchMethodException unused) {
            return getIntentDeep(pendingIntent);
        } catch (InvocationTargetException e2) {
            throw new IllegalStateException(e2);
        } catch (Exception unused2) {
            return getIntentDeep(pendingIntent);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (this.redirectToNoti) {
            Intent intent = new Intent(this.context, HomeActivity.class);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(337641472);
            startActivity(intent);
            return;
        }
        finish();
    }

    @Subscribe
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_notification);
        GlobalData.notificationBack = false;
        back = findViewById(R.id.iv_back);
        setting = findViewById(R.id.iv_setting);
                back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(NotificationCleanerActivity.this, NotificationIgnoreList.class));



            }
        });
        this.j.register(this);

        this.context = this;
        set_adsdimention();
        try {
            redirectToNoti();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.sharedPrefUtil = new SharedPrefUtil(this);
        y();
        this.dformat = new SimpleDateFormat("HH:mm");
        this.listViewNoti = (ListView) findViewById(R.id.listnotification);
        this.tvNotiCount = (TextView) findViewById(R.id.tv_noticount);
        this.tvNotisstatus = (TextView) findViewById(R.id.tvnoti_status);
        this.tvClear = (RelativeLayout) findViewById(R.id.btnClearNotify);
        if (ParentActivity.displaymetrics == null) {
            ParentActivity.displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(ParentActivity.displaymetrics);
        }
        Util.isConnectingToInternet(this);
        if (this.notificationDataList == null) {
            this.notificationDataList = new ArrayList<>();
        }
        this.nReceiver = new NotificationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.mobiclean.phoneclean.NOTIFICATION_LISTENER");
        registerReceiver(this.nReceiver, intentFilter);
        SwipeDismissListViewTouchListener swipeDismissListViewTouchListener = new SwipeDismissListViewTouchListener(this.listViewNoti, new SwipeDismissListViewTouchListener.DismissCallbacks() { // from class: com.mobiclean.phoneclean.notifimanager.NotificationCleanerActivity.3
            @Override
            public boolean canDismiss(int i) {
                return true;
            }

            @Override
            public void onDismiss(ListView listView, int[] iArr) {
                for (int i : iArr) {
                    NotificationCleanerActivity.this.cleanNotification(i, false);
                }
            }

            @Override
            public boolean onclick(int i) {
                return false;
            }
        });
        this.listViewNoti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                NotificationCleanerActivity.this.cleanNotification(i, true);
            }
        });
        this.listViewNoti.setOnTouchListener(swipeDismissListViewTouchListener);
        this.tvClear.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                NotificationManager notificationManager = (NotificationManager) NotificationCleanerActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.cancel(898);
                }
                int size = NotificationCleanerActivity.this.notificationDataList.size();
                NotificationCleanerActivity.this.removeAllFromPreference();
                if (size <= 0) {
                    Toast.makeText(NotificationCleanerActivity.this.context, "Nothing to clear", Toast.LENGTH_SHORT).show();
                    return;
                }
                NotificationCleanerActivity.this.j.post(Integer.valueOf((int) GlobalData.EB_CODE_NOTIFICATION_REMOVE));
                NotificationCleanerActivity notificationCleanerActivity = NotificationCleanerActivity.this;
                if (notificationCleanerActivity.notificationDataList != null && notificationCleanerActivity.listViewNoti != null) {
                    NotificationCleanerActivity.this.notificationDataList.clear();
                }
                if (NotificationCleanerActivity.this.notificationDataList.size() == 0) {
                    NotificationCleanerActivity.this.findViewById(R.id.tvnoti_empty).setVisibility(View.VISIBLE);
                    NotificationCleanerActivity.this.tvNotiCount.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                    GlobalData.deleteObj(NotificationCleanerActivity.this, GlobalData.NotificationList);
                } else {
                    NotificationCleanerActivity.this.findViewById(R.id.tvnoti_empty).setVisibility(View.GONE);
                    try {
                        NotificationCleanerActivity notificationCleanerActivity2 = NotificationCleanerActivity.this;
                        GlobalData.saveObj(notificationCleanerActivity2, GlobalData.NotificationList, notificationCleanerActivity2.notificationDataList);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }

                    Intent intent = new Intent(NotificationCleanerActivity.this, JunkDeleteAnimationScreen.class);
                    intent.putExtra("DATA", "" + size);
                    intent.putExtra("TYPE", "NOTIFICATION");
                    NotificationCleanerActivity.this.finish();
                    NotificationCleanerActivity.this.startActivity(intent);



            }
        });
        if (getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false)) {
            setListners();
        }
        new SharedPrefUtil(this).saveLastTimeUsed(SharedPrefUtil.LUSED_NOTI_CLEANER, System.currentTimeMillis());
        clearNotification(new int[]{137});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settingnoti, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            this.j.unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            unregisterReceiver(this.nReceiver);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        if (GlobalData.fromNotificationsetting) {
            boolean z = false;
            for (String str : NotificationManagerCompat.getEnabledListenerPackages(this)) {
                if (str.equals(getPackageName())) {
                    z = true;
                }
            }
            if (!z) {
                try {
                    if (getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false)) {
                        finish();
                        startActivity(new Intent(this, HomeActivity.class));
                    } else {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    finish();
                }
            }
        }
        boolean booleanToggle = this.sharedPrefUtil.getBooleanToggle(SharedPrefUtil.NOTICLEANER_ON);
        this.isOn = booleanToggle;
        ArrayList<NotifcationWrapper> arrayList = this.notificationDataList;
        if (arrayList != null && this.listViewNoti != null) {
            if (!booleanToggle) {
                arrayList.clear();
            }
            this.j.post(Integer.valueOf((int) GlobalData.EB_CODE_NOTIFICATION));
            addToList();
            setAdapter(true);
            if (this.notificationDataList.size() == 0) {
                this.listViewNoti.setVisibility(View.GONE);
                if (!Util.isConnectingToInternet(this.context) || Util.isAdsFree(this.context)) {
                    findViewById(R.id.tvnoti_empty).setVisibility(View.VISIBLE);
                }
                this.tvNotiCount.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                this.tvClear.setVisibility(View.GONE);
            } else {
                this.listViewNoti.setVisibility(View.VISIBLE);
                findViewById(R.id.tvnoti_empty).setVisibility(View.GONE);
                Util.isConnectingToInternet(this.context);
                this.tvClear.setVisibility(View.VISIBLE);
            }
        }
        super.onResume();
    }

    @SuppressLint("StaticFieldLeak")
    public void y() {
        new AsyncTask<String, String, String>() {

            @Override
            public void onPreExecute() {
                super.onPreExecute();
                findViewById(R.id.pbarlayout).setVisibility(View.VISIBLE);

            }

            @Override
            public String doInBackground(String... strArr) {
                NotificationCleanerActivity.this.getIgnoreList();
                return null;
            }

            @Override
            public void onPostExecute(String str) {
                findViewById(R.id.pbarlayout).setVisibility(View.GONE);
                NotificationCleanerActivity.this.checkPermissionAvailable();
            }
        }.execute(new String[0]);
    }

    public void loadinit() {
        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(NotificationCleanerActivity.this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        return;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }
}