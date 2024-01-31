package com.cleanPhone.mobileCleaner.alramsservices;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.cleanPhone.mobileCleaner.bservices.CheckStatusOfServices;
import com.cleanPhone.mobileCleaner.bservices.CoolAndBoostService;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmNotiService {
    private static final int ALARM_BOOST_CODE = 33321;
    private static final int ALARM_CODE = 74856;
    private static final int ALARM_CODE_BATT = 9065;
    private static final int ALARM_CODE_RETEN = 9419;
    private static final int ALARM_SECOND_CODE = 44092;
    private final String[] PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    private String getTimeInMillis(String str, String str2) {
        long j=0;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat();
        String format=new String();
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat();
        long j2 = 0;
        try {
            simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            format = simpleDateFormat.format(Calendar.getInstance().getTime());
            simpleDateFormat2 = new SimpleDateFormat("dd-MM-yyyy hh:mm");
            j = simpleDateFormat2.parse(format + " " + str).getTime();
        } catch (Exception e) {
            e = e;
        }
        try {
            if (Util.checkTimeDifference("" + j, "" + System.currentTimeMillis()) <= 0) {
                Date parse = simpleDateFormat2.parse(format + " " + str2);
                parse.getTime();
                Calendar calendar = Calendar.getInstance();
                calendar.set(5, parse.getDate() + 1);
                j = simpleDateFormat2.parse(simpleDateFormat.format(calendar.getTime()) + " " + str).getTime();
            }
        } catch (Exception e2) {
            e2 = e2;
            j2 = j;
            e2.printStackTrace();
            j = j2;
            return "" + j;
        }
        return "" + j;
    }

    private void setAlarm(Context context) {
        PendingIntent service;
        if (Util.hasPermission(context, this.PERMISSIONS)) {
            Util.appendLogmobiclean("AlarmNotiService", "Alarm started ", "");
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        String timeInMillis = getTimeInMillis("22:00", "22:00");
        long checkTimeDifference = Util.checkTimeDifference(timeInMillis, "" + System.currentTimeMillis());
        Log.i("CALLED timeleft", checkTimeDifference + " ");
        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra("NOTITYPE", "noti4");
        if (Build.VERSION.SDK_INT >= 23) {
            service = PendingIntent.getService(context, ALARM_CODE, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            service = PendingIntent.getService(context, ALARM_CODE, intent, 0);
        }
        PendingIntent pendingIntent = service;
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + checkTimeDifference, 43200000L, pendingIntent);
        }
    }

    private void setAlarmForBoost(Context context) {
        PendingIntent service;
        if (Util.hasPermission(context, this.PERMISSIONS)) {
            Util.appendLogmobiclean("AlarmForBoostNotification", "Alarm started ", "");
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        String timeInMillis = getTimeInMillis(GlobalData.FIRST_BOOSTNOTI_TIME, GlobalData.SECOND_BOOSTNOTI_TIME);
        long checkTimeDifference = Util.checkTimeDifference(timeInMillis, "" + System.currentTimeMillis());
        Log.i("CALLED timeleft", checkTimeDifference + " ");
        Intent intent = new Intent(context, CoolAndBoostService.class);
        if (Build.VERSION.SDK_INT >= 23) {
            service = PendingIntent.getService(context, ALARM_BOOST_CODE, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            service = PendingIntent.getService(context, ALARM_BOOST_CODE, intent, 0);
        }
        PendingIntent pendingIntent = service;
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + checkTimeDifference, 43200000L, pendingIntent);
        }
    }

    private void setSecondAlarm(Context context) {
        PendingIntent service;
        Util.appendLogmobiclean("AlarmNotiService", "Alarm started ", "");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        String timeInMillis = getTimeInMillis(GlobalData.FIRST_NOTI_TIME1, GlobalData.FIRST_NOTI_TIME1);
        long checkTimeDifference = Util.checkTimeDifference(timeInMillis, "" + System.currentTimeMillis());
        Log.i("CALLED timeleft", checkTimeDifference + " ");
        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra("NOTITYPE", "noti2");
        if (Build.VERSION.SDK_INT >= 23) {
            service = PendingIntent.getService(context, ALARM_SECOND_CODE, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            service = PendingIntent.getService(context, ALARM_SECOND_CODE, intent, 0);
        }
        PendingIntent pendingIntent = service;
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + checkTimeDifference, 86400000L, pendingIntent);
        }
    }

    public void a(Context context) {
        PendingIntent service;
        try {
            Util.appendLogmobiclean("AlarmNotiService", "Alarm started ", "");
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, BatteryNotificationService.class);
            int i = Build.VERSION.SDK_INT;
            if (i >= 23) {
                service = PendingIntent.getService(context, ALARM_CODE_BATT, intent, PendingIntent.FLAG_IMMUTABLE);
            } else {
                service = PendingIntent.getService(context, ALARM_CODE_BATT, intent, 0);
            }
            Calendar calendar = Calendar.getInstance();
            Log.d("TIMMMM", "before " + calendar.getTime().toString());
            calendar.set(10, calendar.get(10) + 1);
            Log.d("TIMMMM", "after " + calendar.getTime().toString());
            if (i >= 23) {
                if (alarmManager != null) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), service);
                }
            } else if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkAlarms(Context context) {
        boolean z = false;
        boolean z2=false;
        boolean z3=false;
        try {
            Intent intent = new Intent(context, NotificationService.class);
            intent.putExtra("NOTITYPE", "noti4");
            int i = Build.VERSION.SDK_INT;
            boolean z4 = true;
            if (i < 23) {
                z = PendingIntent.getService(context, ALARM_CODE, intent, PendingIntent.FLAG_NO_CREATE) != null;
            }
            if (Util.hasPermission(context, this.PERMISSIONS)) {
                Util.appendLogmobiclean("", "Alarm working " + z, "");
            }
            if (!z) {
                setAlarm(context);
            }
            Intent intent2 = new Intent(context, CoolAndBoostService.class);
            if (i < 23) {
                z2 = PendingIntent.getService(context, ALARM_BOOST_CODE, intent2, PendingIntent.FLAG_NO_CREATE) != null;
            }
            Util.appendLogmobiclean("", "Alarm working " + z2, "");
            if (!z2) {
                setAlarmForBoost(context);
            }
            Intent intent3 = new Intent(context, NotificationService.class);
            intent3.putExtra("NOTITYPE", "noti2");
            if (i < 23) {
                z3 = PendingIntent.getService(context, ALARM_SECOND_CODE, intent3, PendingIntent.FLAG_NO_CREATE) != null;
            }
            if (Util.hasPermission(context, this.PERMISSIONS)) {
                Util.appendLogmobiclean("", "Alarm working " + z3, "");
            }
            if (!z3) {
                setSecondAlarm(context);
            }
            new SharedPrefUtil(context).getBoolean("BATTERYSAVER_ON");
            if (new SharedPrefUtil(context).getBoolean(SharedPrefUtil.RETEN_TRACKED)) {
                return;
            }
            Intent intent4 = new Intent(context, CheckStatusOfServices.class);
            if (i >= 23) {
                if (PendingIntent.getService(context, ALARM_CODE_RETEN, intent4, PendingIntent.FLAG_IMMUTABLE) != null) {
                    if (z4) {
                        setAlarmForRetention(context);
                        return;
                    }
                    return;
                }
                z4 = false;
                if (z4) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAlarmForRetention(Context context) {
        PendingIntent service;
        try {
            if (new SharedPrefUtil(context).getBoolean(SharedPrefUtil.RETEN_TRACKED)) {
                return;
            }
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, CheckStatusOfServices.class);
            int i = Build.VERSION.SDK_INT;
            if (i >= 23) {
                service = PendingIntent.getService(context, ALARM_CODE_RETEN, intent, PendingIntent.FLAG_IMMUTABLE);
            } else {
                service = PendingIntent.getService(context, ALARM_CODE_RETEN, intent, 0);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(new SharedPrefUtil(context).getLong(SharedPrefUtil.INSTALL_TIME));
            Log.d("TIMMMM", "before " + calendar.getTime().toString());
            calendar.set(12, calendar.get(12) + 30);
            Log.d("TIMMMM", "after " + calendar.getTime().toString());
            if (i >= 23) {
                if (alarmManager != null) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), service);
                }
            } else if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void startServices(Context context) {
        setAlarm(context);
        setSecondAlarm(context);
        setAlarmForBoost(context);
    }
}
