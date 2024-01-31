package com.cleanPhone.mobileCleaner.alramsservices;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.facebook.share.internal.ShareConstants;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.cleanPhone.mobileCleaner.JunkScanActivity;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.SocialAnimationActivity;
import com.cleanPhone.mobileCleaner.notifimanager.NotificationCleanerActivity;
import com.cleanPhone.mobileCleaner.similerphotos.DuplicacyMidSettings;
import com.cleanPhone.mobileCleaner.similerphotos.DuplicatesActivity;
import com.cleanPhone.mobileCleaner.similerphotos.ImageDetail;
import com.cleanPhone.mobileCleaner.similerphotos.MySharedPreference;
import com.cleanPhone.mobileCleaner.socialmedia.SocialMediaApp;
import com.cleanPhone.mobileCleaner.socialmedia.SocialMediaFilesTabScreen;
import com.cleanPhone.mobileCleaner.socialmedia.SocialMediaNew;
import com.cleanPhone.mobileCleaner.socialmedia.SocialmediaModule;
import com.cleanPhone.mobileCleaner.tools.LargeFile;
import com.cleanPhone.mobileCleaner.tools.SocialCleanerListActivity;
import com.cleanPhone.mobileCleaner.utility.ForegroundCheck;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.ShowNotification;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppJunk;
import com.cleanPhone.mobileCleaner.wrappers.MediaAppModule;
import com.cleanPhone.mobileCleaner.wrappers.MediaJunkData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;


public class NotificationService extends IntentService {
    private static final String APP_FOLDER = "DCIM/FreeUpSpace/compress/compressed images";
    private static final String APP_FOLDER2 = "DCIM/FreeUpSpace/resize/resized-images";
    private static final String APP_FOLDER_VIDEO = "DCIM/FreeUpSpace/compress/compressed videos";
    private static final int DUP_REQCODE = 300;
    private static final long NOTI_DIFFER = 15000;
    private static final int SOCIAL_CODE = 700;
    private static final String TAG = "NotificationIntentServi";
    private static final int WHATS_CODE = 832;
    private long allCacheSize;
    private long availableSizeSDCARD;
    private volatile int batterylevel;
    public transient Context context;
    private int fillCount;
    private boolean lastPackagefound;
    private int limit;
    private BroadcastReceiver mBatInfoReceiver;
    private String noti_type;
    private int priority_count;
    private long ramFillSpace;
    private long ramsaveSize;
    private SharedPrefUtil sharedPref;
    private long sizebfore;
    private int totalProgressCount;
    private long totalSizeSDCARD;


    public class GetLargeFilesData2 extends AsyncTask<String, Integer, String> implements MediaJunkData.updateProgress {
        public GetLargeFilesData2() {
        }

        public void getWhatsappMedia(GetLargeFilesData2 getLargeFilesData2) {
            AppJunk appJunk = new AppJunk("WhatsApp");
            MediaJunkData mediaJunkData = new MediaJunkData(0, NotificationService.this.getResources().getString(R.string.mbc_photos));
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images");
            arrayList.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images/Sent");
            mediaJunkData.getFiles(arrayList, getLargeFilesData2, "WhatsApp");
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData.totSize;
            appJunk.mediaJunkData.add(mediaJunkData);
            MediaJunkData mediaJunkData2 = new MediaJunkData(1, NotificationService.this.getResources().getString(R.string.mbc_viewmore_video));
            ArrayList<String> arrayList2 = new ArrayList<>();
            arrayList2.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Video");
            arrayList2.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Video/Sent");
            mediaJunkData2.getFiles(arrayList2, getLargeFilesData2, "WhatsApp");
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData2.totSize;
            appJunk.mediaJunkData.add(mediaJunkData2);
            MediaJunkData mediaJunkData3 = new MediaJunkData(2, NotificationService.this.getResources().getString(R.string.mbc_myaudios));
            ArrayList<String> arrayList3 = new ArrayList<>();
            arrayList3.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Audio");
            arrayList3.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Audio/Sent");
            mediaJunkData3.getFiles(arrayList3, getLargeFilesData2, "WhatsApp");
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData3.totSize;
            appJunk.mediaJunkData.add(mediaJunkData3);
            MediaJunkData mediaJunkData4 = new MediaJunkData(3, NotificationService.this.getResources().getString(R.string.mbc_viewmore_document));
            ArrayList<String> arrayList4 = new ArrayList<>();
            arrayList4.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Documents");
            arrayList4.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Documents/Sent");
            mediaJunkData4.getFiles(arrayList4, getLargeFilesData2, "WhatsApp");
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData4.totSize;
            appJunk.mediaJunkData.add(mediaJunkData4);
            MediaJunkData mediaJunkData5 = new MediaJunkData(4, "GIF");
            ArrayList<String> arrayList5 = new ArrayList<>();
            arrayList5.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Animated Gifs");
            arrayList5.add(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Animated Gifs/Sent");
            mediaJunkData5.getFiles(arrayList5, getLargeFilesData2, "WhatsApp");
            appJunk.appJunkSize = appJunk.appJunkSize + mediaJunkData5.totSize;
            appJunk.mediaJunkData.add(mediaJunkData5);
            MediaAppModule mediaAppModule = MobiClean.getInstance().mediaAppModule;
            mediaAppModule.socialApp.add(appJunk);
            mediaAppModule.totalSize += appJunk.appJunkSize;
            new SocialMediaApp().name = "Whatsapp";
            Util.appendLogmobiclean(NotificationService.TAG, "method : GetLargeFilesData---AsyncTask---doInBackground---getWhatsappMedia", GlobalData.FILE_NAME);
        }

        @Override
        public void onCancelled() {
            super.onCancelled();
            Util.appendLogmobiclean(NotificationService.TAG, "GetLargeFilesData---AsyncTask---onCancelled", GlobalData.FILE_NAME);
        }

        @Override
        public void onPreExecute() {
            LargeFile.totalSocialSize = 0L;
            super.onPreExecute();
            Util.appendLogmobiclean(NotificationService.TAG, "GetLargeFilesData---AsyncTask---onPreExecute", GlobalData.FILE_NAME);
        }

        @Override
        public void update(String str) {
            SocialCleanerListActivity.fillCount++;
            if (SocialAnimationActivity.totalProgressCount != 0) {
                publishProgress(Integer.valueOf((SocialCleanerListActivity.fillCount * 100) / SocialAnimationActivity.totalProgressCount));
            }
        }

        @Override
        public String doInBackground(String... strArr) {
            Util.appendLogmobiclean(NotificationService.TAG, "GetLargeFilesData---AsyncTask---doInBackground Scaning Start", GlobalData.FILE_NAME);
            MobiClean.getInstance().socialModule = null;
            MobiClean.getInstance().socialModule = new SocialmediaModule();
            MobiClean.getInstance().socialModuleNew = null;
            MobiClean.getInstance().socialModuleNew = new SocialMediaNew();
            MobiClean.getInstance().mediaAppModule = new MediaAppModule();
            getWhatsappMedia(this);
            MobiClean.getInstance().socialModule.updateSelf();
            MobiClean.getInstance().mediaAppModule.refresh();
            Util.appendLogmobiclean(NotificationService.TAG, "GetLargeFilesData---AsyncTask---doInBackground Scaning finish", GlobalData.FILE_NAME);
            return null;
        }

        @Override
        public void onPostExecute(String str) {
            super.onPostExecute( str);
            MobiClean.getInstance().mediaAppModule.refresh();
            Log.d("WHHHHHHH", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + MobiClean.getInstance().mediaAppModule.totalSize);
            if (MobiClean.getInstance().mediaAppModule.totalSize > 1048576) {
                NotificationService notificationService = NotificationService.this;
                notificationService.whatsapp_notificaiton(notificationService.context.getString(R.string.mbc_whtsapp_noti), NotificationService.this.context.getString(R.string.mbc_whtsapp_noti_text), NotificationService.this.context.getString(R.string.mbc_review));
                return;
            }
            NotificationService.c(NotificationService.this);
            NotificationService notificationService2 = NotificationService.this;
            notificationService2.performScan(notificationService2.priority_count);
        }
    }

    public NotificationService() {
        super("NotificationService");
        this.limit = 1;
        this.lastPackagefound = false;
        this.allCacheSize = 0L;
        this.batterylevel = 0;
        this.ramsaveSize = 0L;
        this.ramFillSpace = 0L;
        this.sizebfore = 0L;
        this.totalSizeSDCARD = 0L;
        this.availableSizeSDCARD = 0L;
        this.totalProgressCount = 0;
        this.fillCount = 0;
        this.mBatInfoReceiver = new BroadcastReceiver() { // from class: com.mobiclean.phoneclean.alramsservices.NotificationService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                NotificationService.this.batterylevel = intent.getIntExtra(FirebaseAnalytics.Param.LEVEL, -1);
            }
        };
    }

    public static int c(NotificationService notificationService) {
        int i = notificationService.priority_count + 1;
        notificationService.priority_count = i;
        return i;
    }

    private void checkDup() {
        if (new SharedPrefUtil(getBaseContext()).getBooleanToggle(SharedPrefUtil.NOTIDUP) && ForegroundCheck.get().isBackground() && this.batterylevel > this.limit) {
            CustomNotification_duplicate(getResources().getString(R.string.mbc_dup_new_noti), getResources().getString(R.string.mbc_viewandclean), getResources().getString(R.string.mbc_review), 0);
            return;
        }
        int i = this.priority_count + 1;
        this.priority_count = i;
        performScan(i);
    }


    private void checkJunk() {
        Log.e(TAG, "CheckSocialData");
        if (new SharedPrefUtil(getBaseContext()).getBooleanToggle(SharedPrefUtil.NOTIJUNK) && ForegroundCheck.get().isBackground() && this.batterylevel > this.limit) {
            CustomNotification_junk("" + getResources().getString(R.string.mbc_junk_new_noti), getResources().getString(R.string.mbc_tap_tocleanjunk), getResources().getString(R.string.mbc_clean));
            return;
        }
        int i = this.priority_count + 1;
        this.priority_count = i;
        performScan(i);
    }

    private void checkNotiCleaner() {
        boolean z = false;
        for (String str : NotificationManagerCompat.getEnabledListenerPackages(this)) {
            if (str.equals(getPackageName())) {
                z = true;
            }
        }
        if (z) {
            int i = this.priority_count + 1;
            this.priority_count = i;
            performScan(i);
        } else if (!new SharedPrefUtil(getBaseContext()).getBooleanToggle("notinc")) {
            int i2 = this.priority_count + 1;
            this.priority_count = i2;
            performScan(i2);
        } else {
            String string = getResources().getString(R.string.mbc_notification_head);
            String string2 = getResources().getString(R.string.mbc_notification_head_text);
            noticleaner_notificaiton(string, string2, "" + getResources().getString(R.string.mbc_notification_head_text));
        }
    }

    private void checkSocial() {
        Log.e(TAG, "CheckSocialData");
        if (new SharedPrefUtil(getBaseContext()).getBooleanToggle(SharedPrefUtil.NOTISOCIALCLEANING) && ForegroundCheck.get().isBackground() && this.batterylevel > this.limit) {
            String string = getResources().getString(R.string.mbc_social_new_noti);
            String string2 = getResources().getString(R.string.mbc_viewandclean);
            socialcleaner_notificaiton(string, string2, "" + getResources().getString(R.string.mbc_review));
            return;
        }
        int i = this.priority_count + 1;
        this.priority_count = i;
        performScan(i);
    }

    private void checkSpace() {
        Util.appendLogmobiclean("NotificationService", "space service run at :", "");
        Util.appendLog("Disk check service run at :" + new SimpleDateFormat("hh:mm:ss").format(new Date(System.currentTimeMillis())), "servicelogs.txt");
        if (this.batterylevel > this.limit && !ForegroundCheck.get().isForeground()) {
            if (new SharedPrefUtil(getBaseContext()).getBooleanToggle(SharedPrefUtil.NOTISPACE) && ForegroundCheck.get().isBackground() && this.batterylevel > this.limit) {
                long totalSpace = Environment.getExternalStorageDirectory().getTotalSpace();
                long totalSpace2 = Environment.getExternalStorageDirectory().getTotalSpace() - Environment.getExternalStorageDirectory().getFreeSpace();
                getSDCARDSize();
                long j = this.totalSizeSDCARD;
                long j2 = totalSpace + j;
                long j3 = totalSpace2 + (j - this.availableSizeSDCARD);
                if (j2 == 0) {
                    j2 = Environment.getDataDirectory().getTotalSpace();
                    j3 = Environment.getDataDirectory().getTotalSpace() - Environment.getDataDirectory().getFreeSpace();
                }
                long j4 = (j3 * 100) / j2;
                return;
            }
            int i = this.priority_count + 1;
            this.priority_count = i;
            performScan(i);
        }
    }

    private void getSDCARDSize() {
        String str;
        Iterator it = Arrays.asList("4703-12FD", "MicroSD", "external_SD", "sdcard1", "ext_card", "external_sd", "ext_sd", "external", "extSdCard", "externalSdCard", "sdext", "sdext1", "sdext2", "sdext3", "sdext4").iterator();
        while (true) {
            if (!it.hasNext()) {
                str = null;
                break;
            }
            String str2 = (String) it.next();
            File file = new File("/mnt/", str2);
            if (file.isDirectory() && file.canWrite()) {
                str = file.getAbsolutePath();
                break;
            }
            File file2 = new File("/storage/", str2);
            if (file2.isDirectory() && file2.canWrite()) {
                str = file2.getAbsolutePath();
                break;
            }
            File file3 = new File("/storage/emulated", str2);
            if (file3.isDirectory() && file3.canWrite()) {
                str = file3.getAbsolutePath();
                break;
            }
        }
        if (str != null) {
            StatFs statFs = new StatFs(str);
            long blockSizeLong = statFs.getBlockSizeLong();
            this.totalSizeSDCARD = statFs.getBlockCountLong() * blockSizeLong;
            this.availableSizeSDCARD = statFs.getAvailableBlocksLong() * blockSizeLong;
        }
    }

    private void initialize() {
        this.sharedPref = new SharedPrefUtil(getBaseContext());
        this.context = getBaseContext();
        Intent registerReceiver = getBaseContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver != null) {
            this.batterylevel = registerReceiver.getIntExtra(FirebaseAnalytics.Param.LEVEL, -1);
        }
        Log.i("CHECKB", "" + this.batterylevel);
        this.priority_count = this.sharedPref.getInt(SharedPrefUtil.LAST_PRIORITY_COUNT) + 1;
    }

    private boolean notiAlreadyGiven() {
        String string = this.sharedPref.getString(SharedPrefUtil.NOTI_TIM_DIF);
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(Util.checkTimeDifference("" + System.currentTimeMillis(), string) / 1000);
        Log.i("TYPPPPPP", sb.toString());
        if (string != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(System.currentTimeMillis());
            return Util.checkTimeDifference(sb2.toString(), string) < NOTI_DIFFER;
        }
        return false;
    }


    public void performScan(int i) {
        Log.i("NISS", "" + i);
        if (i == 1) {
            Log.i("NISS", "JUNK " + i);
            checkJunk();
        } else if (i == 2) {
            Log.i("NISS", "DUP " + i);
            checkDup();
        } else if (i == 3) {
            Log.i("NISS", "SOCIAL" + i);
            checkSocial();
        } else if (i == 4) {
            Log.i("NISS", "SPACE " + i);
            checkNotiCleaner();
        } else if (i == 5) {
            Log.i("NISS", "SPACE " + i);
            if (permissionForStorageGiven()) {
                new GetLargeFilesData2().execute(new String[0]);
            } else {
                performScan(i + 1);
            }
        } else if (i == 99) {
            Log.i("NISS", "SPACE " + i);
            checkSpace();
        } else if (i == 100) {
            Log.i("NISS", "RAM " + i);
        }
        if (this.batterylevel < 50) {
            new AlarmNotiService().a(getBaseContext());
        }
    }

    private boolean permissionForStorageGiven() {
        int i = Build.VERSION.SDK_INT;
        if (i >= 30) {
            return Environment.isExternalStorageManager();
        }
        return i < 23 || ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public static void write(Context context, ArrayList<ImageDetail> arrayList) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getFilesDir().getAbsolutePath());
        String str = File.separator;
        sb.append(str);
        sb.append("serlization");
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(context.getFilesDir().getAbsolutePath() + str + "serlization" + str + "imglist.srl");
        if (file2.exists()) {
            file2.delete();
        }
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file + str + "imglist.srl"));
            objectOutputStream.writeObject(arrayList);
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void CustomNotification_duplicate(String str, String str2, String str3, int i) {
        Intent intent;
        PendingIntent activity;
        if (MySharedPreference.getLngIndex(getBaseContext()) == 0) {
            intent = new Intent(getBaseContext(), DuplicacyMidSettings.class);
        } else {
            intent = new Intent(getBaseContext(), DuplicatesActivity.class);
        }
        intent.addFlags(268468224);
        intent.putExtra("FROMNOTIFICATION", true);
        intent.putExtra("FROMCAMERA", new SharedPrefUtil(this.context).getBoolean("FCAMERA"));
        intent.putExtra("FROMNOTI", true);
        intent.putExtra(GlobalData.REDIRECTNOTI, true);
        if (Build.VERSION.SDK_INT >= 23) {
            activity = PendingIntent.getActivity(this, DUP_REQCODE, intent, 67108864);
        } else {
            activity = PendingIntent.getActivity(this, DUP_REQCODE, intent, 0);
        }
        PendingIntent pendingIntent = activity;
        SharedPrefUtil sharedPrefUtil = this.sharedPref;
        sharedPrefUtil.saveString(SharedPrefUtil.NOTI_TIM_DIF, "" + System.currentTimeMillis());
        SharedPrefUtil sharedPrefUtil2 = this.sharedPref;
        sharedPrefUtil2.saveString(SharedPrefUtil.GET_DUP_LASTNOTITIME, System.currentTimeMillis() + "");
        this.priority_count = 2;
        this.sharedPref.saveInt(SharedPrefUtil.LAST_PRIORITY_COUNT, 2);
        new ShowNotification().showNotificationWithButton(getBaseContext(), pendingIntent, str, str2, getString(R.string.mbc_clean), DUP_REQCODE);
        stopSelf();
    }

    @SuppressLint("WrongConstant")
    public void CustomNotification_junk(String str, String str2, String str3) {
        PendingIntent activity;
        Intent intent = new Intent(this.context, JunkScanActivity.class);
        intent.putExtra("FROMNOTI", true);
        intent.putExtra(GlobalData.REDIRECTNOTI, true);
        intent.addFlags(268468224);
        if (Build.VERSION.SDK_INT >= 23) {
            activity = PendingIntent.getActivity(this, 0, intent, 201326592);
        } else {
            activity = PendingIntent.getActivity(this, 0, intent, 134217728);
        }
        new ShowNotification().showNotificationWithButton(getBaseContext(), activity, str, str2, getString(R.string.mbc_clean), 500);
        SharedPrefUtil sharedPrefUtil = this.sharedPref;
        sharedPrefUtil.saveString(SharedPrefUtil.NOTI_TIM_DIF, "" + System.currentTimeMillis());
        this.priority_count = 1;
        this.sharedPref.saveInt(SharedPrefUtil.LAST_PRIORITY_COUNT, 1);
    }

    @SuppressLint("WrongConstant")
    public void noticleaner_notificaiton(String str, String str2, String str3) {
        PendingIntent activity;
        try {
            Intent intent = new Intent(getBaseContext(), NotificationCleanerActivity.class);
            intent.putExtra(GlobalData.REDIRECTNOTI, true);
            intent.putExtra("FROMNOTI", true);
            intent.addFlags(268468224);
            if (Build.VERSION.SDK_INT >= 23) {
                activity = PendingIntent.getActivity(this, 0, intent, 201326592);
            } else {
                activity = PendingIntent.getActivity(this, 0, intent, 134217728);
            }
            PendingIntent pendingIntent = activity;
            if (notiAlreadyGiven()) {
                return;
            }
            SharedPrefUtil sharedPrefUtil = this.sharedPref;
            sharedPrefUtil.saveString(SharedPrefUtil.NOTI_TIM_DIF, "" + System.currentTimeMillis());
            this.priority_count = 4;
            this.sharedPref.saveInt(SharedPrefUtil.LAST_PRIORITY_COUNT, 4);
            new ShowNotification().showNotificationWithButton(getBaseContext(), pendingIntent, str, str2, getString(R.string.mbc_manage), 137);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(this.mBatInfoReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHandleIntent(Intent intent) {
        if (intent != null) {
            this.noti_type = intent.getStringExtra("NOTITYPE");
            Log.d("TYPPPPPP", "" + this.noti_type);
        }
        this.sharedPref = new SharedPrefUtil(getBaseContext());
        if (notiAlreadyGiven()) {
            return;
        }
        initialize();
        performScan(this.priority_count);
        if (this.priority_count >= 5) {
            this.sharedPref.saveInt(SharedPrefUtil.LAST_PRIORITY_COUNT, 0);
        }
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        Util.appendLog("RESTARTED JUNK:" + new SimpleDateFormat("hh:mm:ss").format(new Date(System.currentTimeMillis())), "servicelogs.txt");
        Util.appendLog("RESTARTED JUNK" + new SimpleDateFormat("hh:mm:ss").format(Long.valueOf(System.currentTimeMillis())), "task.txt");
        super.onTaskRemoved(intent);
    }

    @SuppressLint("WrongConstant")
    public void socialcleaner_notificaiton(String str, String str2, String str3) {
        PendingIntent activity;
        try {
            Intent intent = new Intent(this.context, SocialAnimationActivity.class);
            intent.putExtra(GlobalData.REDIRECTNOTI, true);
            intent.putExtra("FROMNOTI", true);
            intent.putExtra("FROMHOME", true);
            intent.addFlags(268468224);
            intent.putExtra(ShareConstants.TITLE, "232323");
            if (Build.VERSION.SDK_INT >= 23) {
                activity = PendingIntent.getActivity(this, 0, intent, 201326592);
            } else {
                activity = PendingIntent.getActivity(this, 0, intent, 134217728);
            }
            new ShowNotification().showNotificationWithButton(getBaseContext(), activity, str, str2, getString(R.string.mbc_clean), SOCIAL_CODE);
            SharedPrefUtil sharedPrefUtil = this.sharedPref;
            sharedPrefUtil.saveString(SharedPrefUtil.NOTI_TIM_DIF, "" + System.currentTimeMillis());
            this.priority_count = 3;
            this.sharedPref.saveInt(SharedPrefUtil.LAST_PRIORITY_COUNT, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void whatsapp_notificaiton(String str, String str2, String str3) {
        PendingIntent activity;
        try {
            Intent intent = new Intent(this.context, SocialMediaFilesTabScreen.class);
            intent.putExtra(FirebaseAnalytics.Param.INDEX, GlobalData.APP_INDEX);
            intent.putExtra("NAME", "WhatsApp");
            intent.putExtra("FROMHOME", false);
            intent.putExtra("SIZE", MobiClean.getInstance().mediaAppModule.socialApp.get(0).appJunkSize);
            intent.addFlags(268468224);
            intent.putExtra(GlobalData.REDIRECTNOTI, true);
            intent.putExtra("FROMNOTI", true);
            intent.putExtra("WHATS", true);
            if (Build.VERSION.SDK_INT >= 23) {
                activity = PendingIntent.getActivity(this, 0, intent, 201326592);
            } else {
                activity = PendingIntent.getActivity(this, 0, intent, 134217728);
            }
            PendingIntent pendingIntent = activity;
            if (!new SharedPrefUtil(getBaseContext()).getBooleanToggle("notiwh")) {
                int i = this.priority_count + 1;
                this.priority_count = i;
                performScan(i);
                return;
            }
            new ShowNotification().showNotificationWithButton(getBaseContext(), pendingIntent, str, str2, getString(R.string.mbc_clean), WHATS_CODE);
            SharedPrefUtil sharedPrefUtil = this.sharedPref;
            sharedPrefUtil.saveString(SharedPrefUtil.NOTI_TIM_DIF, "" + System.currentTimeMillis());
            this.priority_count = 5;
            this.sharedPref.saveInt(SharedPrefUtil.LAST_PRIORITY_COUNT, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
