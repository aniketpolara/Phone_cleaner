package com.cleanPhone.mobileCleaner.notifimanager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.PackageInfoStruct;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class NLService extends NotificationListenerService {
    private boolean notificationOn;
    private PackageManager packageManager;
    private SharedPrefUtil sharedPrefUtil;
    public EventBus f4960a = EventBus.getDefault();
    private String TAG = getClass().getSimpleName();
    private boolean isListenerConnected = false;

    private String fullContent(Notification notification, Context context, List<String> list, String str) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = layoutInflater != null ? (ViewGroup) layoutInflater.inflate(notification.bigContentView.getLayoutId(), (ViewGroup) null) : null;
        notification.bigContentView.reapply(context.getApplicationContext(), viewGroup);
        Iterator<View> it = getAllChildren(viewGroup).iterator();
        String str2 = "";
        while (it.hasNext()) {
            View next = it.next();
            if (next instanceof TextView) {
                Util.appendLogmobiclean(this.TAG, next.getClass().getSimpleName(), "");
                String valueOf = String.valueOf(((TextView) next).getText());
                Util.appendLogmobiclean(this.TAG, valueOf, "");
                if (!valueOf.equals(list.get(0)) && valueOf.length() > 1 && !valueOf.matches("(([0]?[1-9]|1[0-2])([:.][0-5]\\d)(\\ [AaPp][Mm]))|(([0|1]?\\d?|2[0-3])([:.][0-5]\\d))") && !next.getClass().getSimpleName().equals("Button")) {
                    if (valueOf.startsWith(list.get(0))) {
                        String substring = valueOf.substring(list.get(0).length());
                        if (substring.startsWith(":")) {
                            substring = substring.substring(1);
                        }
                        if (substring.startsWith(IOUtils.LINE_SEPARATOR_UNIX)) {
                            substring = substring.substring(1);
                        }
                        valueOf = substring;
                        if (valueOf.startsWith(IOUtils.LINE_SEPARATOR_UNIX)) {
                            valueOf = valueOf.substring(1);
                        }
                    }
                    Util.appendLogmobiclean(this.TAG, valueOf, "");
                    str2 = str2.concat(valueOf).concat(IOUtils.LINE_SEPARATOR_UNIX);
                }
            }
        }
        String trim = str2.trim();
        if (trim.length() > 1) {
            Util.appendLogmobiclean(this.TAG, trim, "");
            return trim;
        }
        return null;
    }

    private Drawable getADrawable(int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return getResources().getDrawable(i, getTheme());
        }
        return getResources().getDrawable(i);
    }

    private ArrayList<View> getAllChildren(View view) {
        if (!(view instanceof ViewGroup)) {
            ArrayList<View> arrayList = new ArrayList<>();
            arrayList.add(view);
            return arrayList;
        }
        ArrayList<View> arrayList2 = new ArrayList<>();
        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            ArrayList arrayList3 = new ArrayList();
            arrayList3.add(view);
            arrayList3.addAll(getAllChildren(childAt));
            arrayList2.addAll(arrayList3);
        }
        return arrayList2;
    }

    private String getAppname(String str) {
        ApplicationInfo applicationInfo;
        PackageManager packageManager = getApplicationContext().getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
            applicationInfo = null;
        }
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
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


        private String[] getNotificationContent(StatusBarNotification statusBarNotification) {
            String str;
            String str2;
            String trim;
            String str3 = null;
            String str4;
            String[] strArr = new String[2];
            Notification notification = statusBarNotification.getNotification();
            String str5 = null;
            String r5 = null;
            List<String> text = null;
            if (Build.VERSION.SDK_INT < 21) {
                if (notification != null) {
                    try {
                        text = getText(notification);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (text == null) {
                    return strArr;
                }
                if (text.size() > 1) {
                    Util.appendLogmobiclean(this.TAG, text.toString(), "");
                    str4 = text.get(1);
                } else {
                    str4 = "";
                }
                if (str4 == null) {
                    str4 = String.valueOf(notification.tickerText);
                }
                str2 = str4;
                if (text.size() == 0) {
                    text.add(str2);
                }
                if (str2.equals("null")) {
                    return strArr;
                }
                str = text.get(0);
                try {
                    if (notification.bigContentView != null) {
                        try {
                            try {
                                Util.appendLogmobiclean(this.TAG, "bigView", "");
                                trim = fullContent(notification, this, text, str2);
                            } catch (RuntimeException unused) {
                                Looper.prepareMainLooper();
                            }
                        } catch (RuntimeException unused2) {
                            trim = fullContent(notification, this, text, str2);
                        }
                    }
                } catch (Exception unused3) {
                }
                if (TextUtils.isEmpty(str)) {
                }
                if (TextUtils.isEmpty(str2)) {
                }
                strArr[0] = str;
                String r7 = new String();
                strArr[1] = r7;
                return strArr;
            }
            try {
                str = notification.extras.get(NotificationCompat.EXTRA_TITLE).toString();
            } catch (Exception unused4) {
                str = "";
            }
            try {
                str2 = notification.extras.get(NotificationCompat.EXTRA_TEXT).toString();
            } catch (Exception unused5) {
                str2 = "";
            }
            if (TextUtils.isEmpty(str2)) {
                try {
                    if (notification.extras.getString(NotificationCompat.EXTRA_TEMPLATE, "").equals("android.app.Notification$InboxStyle") && notification.extras.containsKey(NotificationCompat.EXTRA_TEXT_LINES)) {
                        CharSequence[] charSequenceArray = notification.extras.getCharSequenceArray(NotificationCompat.EXTRA_TEXT_LINES);
                        if (charSequenceArray != null) {
                            try {
                                str3 = "";
                                for (int length = charSequenceArray.length - 1; length >= 0; length--) {
                                    try {
                                        CharSequence charSequence = charSequenceArray[length];
                                        str3 = str3 + ((Object) charSequence) + IOUtils.LINE_SEPARATOR_UNIX;
                                    } catch (Exception unused6) {
                                        str5 = str3;
                                    }
                                }
                            } catch (Exception unused7) {
                                str5 = "";
                            }
                        } else {
                            str3 = "";
                        }
                        str5 = str3;
                    } else {
                        str5 = notification.extras.get(NotificationCompat.EXTRA_BIG_TEXT).toString();
                    }
                    if (notification.extras.containsKey(NotificationCompat.EXTRA_TITLE_BIG)) {
                        str = notification.extras.getCharSequence(NotificationCompat.EXTRA_TITLE_BIG, str).toString();
                    }
                } catch (Exception unused8) {
                }
                if (str5 != null && str5.length() > 3) {
                    trim = str5.trim();
                    str2 = trim;
                }
            }
            if (TextUtils.isEmpty(str)) {
                str = "";
            }
            String str6 = TextUtils.isEmpty(str2) ? "" : str2;
            strArr[0] = str;
            strArr[1] = str6;
            return strArr;
        }



    private static List<String> getText(Notification notification) {
        RemoteViews remoteViews = notification.contentView;
        ArrayList arrayList = new ArrayList();
        try {
            Field declaredField = remoteViews.getClass().getDeclaredField("mActions");
            declaredField.setAccessible(true);
            Iterator it = ((ArrayList) declaredField.get(remoteViews)).iterator();
            while (it.hasNext()) {
                Parcel obtain = Parcel.obtain();
                ((Parcelable) it.next()).writeToParcel(obtain, 0);
                obtain.setDataPosition(0);
                if (obtain.readInt() == 2) {
                    obtain.readInt();
                    String readString = obtain.readString();
                    if (readString != null) {
                        if (readString.equals("setText")) {
                            obtain.readInt();
                            String trim = ((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(obtain)).toString().trim();
                            if (!arrayList.contains(trim)) {
                                arrayList.add(trim);
                            }
                        }
                        obtain.recycle();
                    }
                }
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean ignored(String str) {
        if (GlobalData.isObjExist(this, GlobalData.IgnoreList)) {
            ArrayList arrayList = null;
            try {
                arrayList = (ArrayList) GlobalData.getObj(this, GlobalData.IgnoreList);
            } catch (IOException e) {
                e.printStackTrace();
                Util.appendLogmobiclean(this.TAG, e.getMessage(), "");
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
                Util.appendLogmobiclean(this.TAG, e2.getMessage(), "");
            }
            if (arrayList == null) {
                return false;
            }
            for (int i = 0; i < arrayList.size(); i++) {
                if (((PackageInfoStruct) arrayList.get(i)).pname.equalsIgnoreCase(str) && ((PackageInfoStruct) arrayList.get(i)).ischecked) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean isInList(String str) {
        ArrayList arrayList = new ArrayList();
        if (GlobalData.isObjExist(this, GlobalData.IgnoreList)) {
            try {
                arrayList = (ArrayList) GlobalData.getObj(this, GlobalData.IgnoreList);
            } catch (IOException e) {
                e.printStackTrace();
                Util.appendLogmobiclean(this.TAG, e.getMessage(), "");
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
                Util.appendLogmobiclean(this.TAG, e2.getMessage(), "");
            }
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (((PackageInfoStruct) arrayList.get(i)).pname.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }


    @SuppressLint("WrongConstant")
    private void saveNotificationInPreferences(String str, String str2, int i, String str3, String str4, String str5, String str6, long j, PendingIntent pendingIntent) {
        boolean z;
        PendingIntent activity;
        if (TextUtils.isEmpty(str5)) {
            return;
        }
        Util.appendLogmobiclean("NLService", str6, "");
        try {
            ArrayList arrayList = new ArrayList();
            if (GlobalData.isObjExist(this, GlobalData.NotificationList)) {
                try {
                    try {
                        arrayList = (ArrayList) GlobalData.getObj(this, GlobalData.NotificationList);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Util.appendLogmobiclean(this.TAG, e.getMessage(), "");
                    }
                } catch (ClassNotFoundException e2) {
                    e2.printStackTrace();
                    Util.appendLogmobiclean(this.TAG, e2.getMessage(), "");
                }
            }
            if (arrayList != null) {
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    if (((NotifcationWrapper) arrayList.get(i2)).key.equals(str3)) {
                        ((NotifcationWrapper) arrayList.get(i2)).text = str5;
                        ((NotifcationWrapper) arrayList.get(i2)).title = str4;
                        ((NotifcationWrapper) arrayList.get(i2)).postTime = j;
                        ((NotifcationWrapper) arrayList.get(i2)).sourceDir = str2;
                        z = true;
                        break;
                    }
                }
            }
            z = false;
            if (z) {
                try {
                    Collections.sort(arrayList, new Comparator<NotifcationWrapper>() {
                        @Override
                        public int compare(NotifcationWrapper notifcationWrapper, NotifcationWrapper notifcationWrapper2) {
                            return notifcationWrapper.pkg.compareTo(notifcationWrapper2.pkg);
                        }
                    });
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
                GlobalData.saveObj(this, GlobalData.NotificationList, arrayList);
                GlobalData.fromNotificationsetting = false;
                try {
                    this.f4960a.post(77);
                    return;
                } catch (Exception e4) {
                    e4.printStackTrace();
                    return;
                }
            }
            NotifcationWrapper notifcationWrapper = new NotifcationWrapper();
            notifcationWrapper.appname = str;
            notifcationWrapper.title = str4;
            notifcationWrapper.key = str3;
            notifcationWrapper.pkg = str6;
            notifcationWrapper.text = str5;
            notifcationWrapper.id = i;
            notifcationWrapper.postTime = j;
            notifcationWrapper.sourceDir = str2;
            if (arrayList != null) {
                arrayList.add(notifcationWrapper);
            }
            if (arrayList != null) {
                try {
                    Collections.sort(arrayList, new Comparator<NotifcationWrapper>() { // from class: com.mobiclean.phoneclean.notifimanager.NLService.2
                        @Override // java.util.Comparator
                        public int compare(NotifcationWrapper notifcationWrapper2, NotifcationWrapper notifcationWrapper3) {
                            return notifcationWrapper2.pkg.compareTo(notifcationWrapper3.pkg);
                        }
                    });
                } catch (Exception e5) {
                    e5.printStackTrace();
                }
            }
            GlobalData.saveObj(this, GlobalData.NotificationList, arrayList);
            GlobalData.fromNotificationsetting = false;
            try {
                this.f4960a.post(77);
            } catch (Exception e6) {
                e6.printStackTrace();
            }
            boolean booleanToggle = this.sharedPrefUtil.getBooleanToggle(SharedPrefUtil.NOTICLEANER_ON);
            this.notificationOn = booleanToggle;
            if (booleanToggle) {
                Intent intent = new Intent(getBaseContext(), NotificationCleanerActivity.class);
                intent.putExtra(GlobalData.REDIRECTNOTI, true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 23) {
                    activity = PendingIntent.getActivity(this, 555, intent, 201326592);
                } else {
                    activity = PendingIntent.getActivity(this, 555, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                }
                PendingIntent pendingIntent2 = activity;
                RemoteViews remoteViews = new RemoteViews(getPackageName(), (int) R.layout.custom_notification);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "707");
                builder.setSmallIcon(R.drawable.splesh_logo);
                builder.setContent(remoteViews);
                Notification build = builder.build();
                int i3 = build.defaults | 2;
                build.defaults = i3;
                int i4 = i3 | 1;
                build.defaults = i4;
                build.defaults = i4 | 4;
                ArrayList arrayList2 = new ArrayList();
                if (arrayList != null) {
                    for (int i5 = 0; i5 < arrayList.size(); i5++) {
                        if (!arrayList2.contains(((NotifcationWrapper) arrayList.get(i5)).pkg)) {
                            arrayList2.add(((NotifcationWrapper) arrayList.get(i5)).pkg);
                        }
                    }
                }
                try {
                    remoteViews.removeAllViews(R.id.layout);
                } catch (Exception e7) {
                    e7.printStackTrace();
                }
                for (int i6 = 0; i6 < arrayList2.size(); i6++) {
                    RemoteViews remoteViews2 = new RemoteViews(getPackageName(), (int) R.layout.img_notification);
                    try {
                        remoteViews2.setImageViewBitmap(R.id.img, getBitmap((String) arrayList2.get(i6)));
                    } catch (Exception e8) {
                        e8.printStackTrace();
                    }
                    remoteViews.addView(R.id.layout, remoteViews2);
                }
                if (arrayList != null) {
                    if (arrayList.size() == 1) {
                        remoteViews.setTextViewText(R.id.tvtitle, arrayList.size() + " " + getString(R.string.mbc_junknotification));
                    } else {
                        remoteViews.setTextViewText(R.id.tvtitle, "" + arrayList.size() + " " + getString(R.string.mbc_junknotification));
                    }
                }
                remoteViews.setTextViewText(R.id.btn_noti, "" + getString(R.string.mbc_clean));
                try {
                    remoteViews.setOnClickPendingIntent(R.id.btn_noti, pendingIntent2);
                } catch (Exception e9) {
                    e9.printStackTrace();
                }
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= 26) {
                    NotificationChannel notificationChannel = new NotificationChannel("707", "Notifications", NotificationManager.IMPORTANCE_DEFAULT);
                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                }
                if (notificationManager != null) {
                    notificationManager.notify(808, build);
                    return;
                }
                return;
            }
            return;
        } catch (Exception e10) {
            e10.printStackTrace();
            Util.appendLogmobiclean(this.TAG, e10.getMessage(), "");
        }
    }


    @Override
    @Subscribe
    public void onCreate() {
        super.onCreate();
        new IntentFilter().addAction("com.mobiclean.phoneclean.NOTIFICATION_LISTENER");
//        this.f4960a.register(this);
        this.packageManager = getBaseContext().getPackageManager();
        this.sharedPrefUtil = new SharedPrefUtil(getBaseContext());
    }


    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        this.isListenerConnected = true;
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        this.isListenerConnected = false;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        String str;
        String str2;
        if (this.isListenerConnected && statusBarNotification != null) {
            this.notificationOn = this.sharedPrefUtil.getBooleanToggle(SharedPrefUtil.NOTICLEANER_ON);
            boolean z = this.sharedPrefUtil.getBoolean(SharedPrefUtil.SHOW_CHARGEING);
            if ((this.notificationOn || z) && statusBarNotification.isClearable()) {
                try {
                    PackageManager packageManager = this.packageManager;
                    str = packageManager.getApplicationInfo("" + statusBarNotification.getPackageName(), 0).loadLabel(this.packageManager).toString();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    str = "";
                }
                try {
                    str2 = this.packageManager.getApplicationInfo(statusBarNotification.getPackageName(), 0).sourceDir;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    str2 = "";
                }
                try {
                    if (statusBarNotification.getPackageName().equalsIgnoreCase("com.mobiclean.phoneclean")) {
                        return;
                    }
                } catch (Exception unused) {
                }
                if (str.equalsIgnoreCase(statusBarNotification.getPackageName()) || !isInList(statusBarNotification.getPackageName()) || !ignored(statusBarNotification.getPackageName()) || str.equalsIgnoreCase("Google play services") || str.equalsIgnoreCase("Google play store") || str.equalsIgnoreCase("APC Rebrand") || str.equals("APC Rebrand")) {
                    return;
                }
                Notification notification = statusBarNotification.getNotification();
                String[] notificationContent = getNotificationContent(statusBarNotification);
                if (notificationContent == null || notificationContent.length == 0) {
                    return;
                }
                String str3 = notificationContent[0];
                String str4 = notificationContent[1];
                String str5 = this.TAG;
                Util.appendLogmobiclean(str5, str3 + " " + str4, "");
                int i = Build.VERSION.SDK_INT;
                String key = i >= 21 ? statusBarNotification.getKey() : "";
                String str6 = this.TAG;
                Util.appendLogmobiclean(str6, "ID :" + statusBarNotification.getTag() + "\t" + str4 + "\t" + statusBarNotification.getPackageName() + "\t" + str3, "");
                saveNotificationInPreferences(str, str2, statusBarNotification.getId(), key, str3, str4, statusBarNotification.getPackageName(), statusBarNotification.getNotification().when, notification.contentIntent);
                try {
                    if (this.isListenerConnected) {
                        if (i >= 21) {
                            cancelNotification(statusBarNotification.getKey());
                        } else {
                            cancelNotification(statusBarNotification.getPackageName(), statusBarNotification.getTag(), statusBarNotification.getId());
                        }
                    }
                } catch (SecurityException unused2) {
                }
                if (this.notificationOn) {
                    Intent intent = new Intent("com.mobiclean.phoneclean.NOTIFICATION_LISTENER");
                    intent.putExtra("notification_event", "received");
                    StringBuilder sb = new StringBuilder();
                    sb.append(statusBarNotification.getPackageName());
                    sb.append(",");
                    sb.append(statusBarNotification.getId());
                    sb.append(",");
                    sb.append(getAppname("" + statusBarNotification.getPackageName()));
                    sb.append(",");
                    sb.append(str4);
                    intent.putExtra("notification_data", sb.toString());
                    sendBroadcast(intent);
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
    }
}
