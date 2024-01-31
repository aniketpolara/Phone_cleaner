package com.cleanPhone.mobileCleaner.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefUtil {
    public static final String ADD_REFFR = "ADDORREFER";
    public static final String ADFREE_TIME = "ADFREE_TIME";
    public static final String ADMEDIA = "ADMEDIA";
    public static final String ADMEDIA_SECOND = "ADMEDIA_SECOND";
    public static final String ADMEDIA_THIRD = "ADMEDIA_THIRD";
    public static final String ADMOB_ADX_NETWORK = "ADMOB_ADX_NETWORK";
    public static final String ADMOB_APP_OPEN_AD_ID = "ADMOB_APP_OPEN_AD_ID";
    public static final String ADMOB_BANNER_AD_ID = "ADMOB_BANNER_AD_ID";
    public static final String ADMOB_FULL_AD_EXIT_ID = "ADMOB_FULL_AD_EXIT_ID";
    public static final String ADMOB_INTERSTITIAL_AD_ID = "ADMOB_INTERSTITIAL_AD_ID";
    public static final String ADMOB_MEDIUM_RECT_AD_ID = "ADMOB_MEDIUM_RECT_AD_ID";
    public static final String ADS_BATCH = "ADS_BATCH";
    public static final String ADS_DATA = "ADDATA";
    public static final String ADS_FREE = "ADS_FREE";
    public static final String ADS_FREE_KEY = "ADS_FREE_KEY";
    public static final String ADUNIT_DATA = "ADUNIT_DATA";
    public static final String ADX_APP_OPEN_AD_ID = "ADX_APP_OPEN_AD_ID";
    public static final String ADX_BANNER_AD_ID = "ADX_BANNER_AD_ID";
    public static final String ADX_INTERSTITIAL_AD_ID = "ADX_INTERSTITIAL_AD_ID";
    public static final String ADX_MEDIUM_RECT_AD_ID = "ADX_MEDIUM_RECT_AD_ID";
    public static final String AD_REGION = "AD_REGION";
    public static final String AFTERCOOLTEMP = "AFTERCOOLTEMP";
    public static final String AFTER_INS_SENT = "AF_IN_SENT";
    public static final String ALLREADY_SHARED = "ALLREADY_SHARED";
    public static final String APPFIRSTTIME = "APPFIRSTTIME";
    public static final String APP_NEXT_AD_SHOW = "APP_NEXT_AD_SHOW";
    public static final String AV_FREE_KEY = "AV_FREE_KEY";
    public static final String BATTERYFIRSTTIME = "BATTERYFIRSTTIME";
    public static final String CAMP = "CAMP";
    public static final String CAPTURE_COUNT = "CAPTURE_COUNT";
    public static final String COUNTER = "COUNTER";
    public static final String COUNTRYNAME = "COUNTRYNAME";
    public static final String COUNTRY_CODE = "COUNTRY_CODE";
    public static final String CURRENCY = "CURRENCY";
    public static final String CURRENTDB = "CURRENTDB";
    public static final String DETAIL_NOTSENT = "DETAIL_NOTSENT";
    public static final String DONT_SHOW_ADDIALOG = "DONT_SHOW_ADDIALOG";
    public static final String DON_SHO_DISC = "DON_SHO_DISC";
    public static final String DUP_LEVEL_FTIME = "DUP_LEVELFTIME";
    private static final String FIRST_LAUNCH = "first_launch";
    public static final String GAME_TYPE = "GAME_TYPE";
    public static final String GET_CALLDATA = "GETCALLDATA";
    public static final String GET_DUP_LASTNOTITIME = "DUP_LASTNOTITIME";
    public static final String HIDE_AUTOSTART = "HIDE_AUTOSTART";
    public static final String IGNORE_LISTSYS = "IGNORE_LISTSYS";
    public static final String INSTALL_TIME = "INSTALL_TIME";
    public static final String INST_DET_SENT = "INST_DET_SENT";
    public static final String IP = "IP";
    public static final String ISADFREE = "ISADFREE";
    public static final String IS_OFFER = "IS_OFFER";
    public static final String JUNCCLEANTIME = "junccleantime";
    public static final String KEY_PURCHASE_DAY = "KEY_PURCHASE_DAY";
    public static String LASTBOOSTTIME = "lastboosttime";
    public static String LASTBOOSTTIME_HEADER = "LASTBOOSTTIME_HEADER";
    public static final String LASTCOOLTIME = "LASTCOOLTIME";
    public static final String LASTTIMEJUNKCLEANED = "LASTTIMEJUNKCLEANED";
    public static final String LAST_KEYCHECKED = "LAST_KEYCHECKED";
    public static final String LAST_OPT_TIME = "LAST_OPT_TIME";
    public static final String LAST_PRIORITY_COUNT = "PRIORITY";
    public static final String LAST_SCREEN = "LAST_SCREEN";
    public static final String LAST_TIME_VISIT = "LAST_TIME_VISIT";
    private static String LOGIN_STATUS = "login_status";
    public static final String LUSED_ANTIVIRUS = "LUSED_ANTIVIRUS";
    public static final String LUSED_APP = "LUSED_APP";
    public static final String LUSED_APP_ADDICTION = "LUSED_APP_ADDICTION";
    public static final String LUSED_APP_MANAGER = "LUSED_APP_MANAGER";
    public static final String LUSED_BATTERY = "LUSED_BATTERY";
    public static final String LUSED_BOOST = "LUSED_BOOST";
    public static final String LUSED_COOLER = "LUSED_COOLER";
    public static final String LUSED_DATA_WATCH = "LUSED_DATA_WATCH";
    public static final String LUSED_GAMEBOOST = "LUSED_GAMEBOOST";
    public static final String LUSED_JUNK = "LUSED_JUNK";
    public static final String LUSED_NOTI_CLEANER = "LUSED_NOTI_CLEANER";
    public static String LUSED_ONE_TAP_BOOST = "LUSED_ONE_TAP_BOOST";
    public static final String LUSED_PRIVATE_BROWSER = "LUSED_PRIVATE_BROWSER";
    public static final String LUSED_SIMILAR = "LUSED_SIMILAR";
    public static final String LUSED_SOCIAL = "LUSED_SOCIAL";
    public static final String LUSED_SPACE_MANAGER = "LUSED_SPACE_MANAGER";
    public static final String LUSED_SUGGESTED_APPS = "LUSED_SUGGESTED_APPS";
    public static final String MACFEE_NOTI = "MACFEE_NOTI";
    public static final String NOTIBOOST = "NOTI_BOOST";
    public static final String NOTICHARGING = "NOTICHARGING";
    public static final String NOTICLEANER_ON = "NOTICLEANER_ON";
    public static final String NOTICOOLER = "NOTICOOLER";
    public static final String NOTIDUP = "NOTI_DUP";
    public static final String NOTIJUNK = "NOTI_JUNK";
    public static final String NOTISOCIALCLEANING = "NOTISOCIALCLEANING";
    public static final String NOTISPACE = "NOTI_SPACE";
    public static final String NOTI_HEADER = "NOTI_HEADER";
    public static final String NOTI_TIM_DIF = "time_diff";
    public static final String NOTI_TOGGLE_ON = "NOTI_TOGGLE_ON";
    public static final String NOT_OPENED = "NOT_OPENED";
    public static final String NO_AD_TIME = "NO_AD_TIME";
    public static final String OFFER_CART = "OFFER_CART";
    public static final String OFFER_URL = "OFFER_URL";
    public static final String OUR_AD = "OUR_AD";
    public static final String OWN_AD = "OWN_AD";
    public static final String PIX = "PIX";
    public static final String PLAN_ID = "PLAN_ID";
    public static final String PPV = "PPV";
    public static final String PUB = "PUB";
    public static final String PUSHTOKEN = "PUSHTOKEN";
    public static final String RAMATPAUSE = "RAMATPAUSE";
    public static final String RAMPAUSE = "RAMPAUSE";
    public static final String RAM_SHOWN = "SHUFFLE";
    public static final String RATED = "RATED2";
    public static final String RATED_AT = "RATED_AT";
    private static final String REALTIME_PROTECTION_STATE = "real_time_protection_state";
    public static final String REFER_URL = "referurl";
    private static final String REFFER_CODE = "reffer";
    public static final String REGION = "REGION";
    public static final String RETEN_TRACKED = "RETEN_TRACKED";
    public static final String RTP_ON = "rtp_on";
    public static final String RTP_QUERRY = "RTP_QUERRY";
    public static final String SAVED_APPS = "SAVED_APPS";
    public static final String SAVED_GAME = "SAVED_GAME";
    public static final String SAVED_GAME_LIST = "SAVED_GAME_LIST";
    public static final String SAVED_HIBERNATE_LIST = "SAVED_HIBERNATE_LIST";
    public static final String SHOW_CHARGEING = "SHOW_CHARGEING";
    public static final String SHOW_NUM = "SHOW_NUM";
    public static final String SHOW_NUM_SR = "SHOW_NUM_SR";
    public static final String SHOW_PSY_SHOW = "SHOW_PSY_SHOW";
    public static final String SOCIAL_NOTI = "SOCIAL_NOTI";
    public static final String SOURCE = "SOURCE";
    public static final String SPLASH_AD_SHOW = "SPLASH_AD_SHOW";
    public static final String SPLASH_FIRST = "SPLASH_FIRST";
    public static final String SUGGEST_POPUP = "SUGGEST_POPUP";
    public static final String SUGGEST_POPUP_CPU_COOL = "SUGGEST_POPUP_CPU_COOL";
    public static final String SUGGEST_POPUP_CPU_HEADER = "SUGGEST_POPUP_CPU_HEADER";
    public static final String TIME_SAVED = "TIME_SAVED";
    public static final String TRK_DONE = "TRK_DONE";
    public static final String UNIQUE_PCODE = "UNIQUE_PCODE";
    public static final String UPDATE_AT = "UPDATE_AT";
    public static final String UPDATE_TYPE = "UPDATE_TYPE";
    private static final String USER_SP = "def_spr";
    public static final String UTM_CONTENT = "UTM_CONTENT";
    public static final String UTM_TERM = "utm_term";
    public static final String VISIT_COUNT = "VISIT_COUNT";
    public static final String V_COUNT = "V_COUNT";
    private Context mContext;
    private SharedPreferences sharedPreferences;

    public SharedPrefUtil(Context context) {
        this.mContext = context;
        try {
            this.sharedPreferences = context.getSharedPreferences(USER_SP, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearPreferences(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(USER_SP, 0).edit();
        edit.clear();
        edit.apply();
    }

    public static boolean isFirstLaunch(Context context) {
        return context.getSharedPreferences(USER_SP, 0).getBoolean(FIRST_LAUNCH, false);
    }

    public static boolean isRefererCalled(Context context) {
        return context.getSharedPreferences(USER_SP, 0).getBoolean(REFFER_CODE, false);
    }

    public static void logout(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(USER_SP, 0).edit();
        edit.putBoolean(LOGIN_STATUS, false);
        edit.apply();
    }

    public static boolean profileOFF(Context context) {
        return context.getSharedPreferences(USER_SP, 0).getBoolean(LOGIN_STATUS, false);
    }

    public static void ptofileON(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(USER_SP, 0).edit();
        edit.putBoolean(LOGIN_STATUS, true);
        edit.apply();
    }

    public static void setFirstLaunch(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(USER_SP, 0).edit();
        edit.putBoolean(FIRST_LAUNCH, true);
        edit.apply();
    }

    public static void setRefereCalled(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(USER_SP, 0).edit();
        edit.putBoolean(REFFER_CODE, true);
        edit.apply();
    }

    public boolean getBoolean(String str) {
        Context context = this.mContext;
        SharedPreferences sharedPreferences = context != null ? context.getSharedPreferences(USER_SP, 0) : null;
        if (sharedPreferences != null) {
            Log.e(str, "" + sharedPreferences.getBoolean(str, false));
            return sharedPreferences.getBoolean(str, false);
        }
        return false;
    }

    public boolean getBooleanToggle(String str) {
        return this.mContext.getSharedPreferences(USER_SP, 0).getBoolean(str, true);
    }

    public boolean getBoolean_lock(String str) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(USER_SP, 0);
        Log.e(str, "" + sharedPreferences.getBoolean(str, true));
        return sharedPreferences.getBoolean(str, false);
    }

    public boolean getCheckBoxState(String str) {
        return this.sharedPreferences.getBoolean(str, false);
    }

    public int getInt(String str) {
        return this.mContext.getSharedPreferences(USER_SP, 0).getInt(str, 0);
    }

    public long getLastTimeUsed(String str) {
        return this.mContext.getSharedPreferences(USER_SP, 0).getLong(str, 0L);
    }

    public long getLong(String str) {
        return this.mContext.getSharedPreferences(USER_SP, 0).getLong(str, 0L);
    }

    public String getString(String str) {
        return this.mContext.getSharedPreferences(USER_SP, 0).getString(str, null);
    }

    public String getStringDefault(String str) {
        return this.mContext.getSharedPreferences(USER_SP, 0).getString(str, GlobalData.getDefaultBatterySetting(this.mContext));
    }

    public String getStringSleep(String str) {
        return this.mContext.getSharedPreferences(USER_SP, 0).getString(str, GlobalData.getSleepBatterySetting(this.mContext));
    }

    public boolean isRealTimeProtectionEnabled() {
        return this.sharedPreferences.getBoolean(REALTIME_PROTECTION_STATE, true);
    }

    public void saveBoolean(String str, boolean z) {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(USER_SP, 0).edit();
        edit.putBoolean(str, z);
        edit.apply();
        Log.e(str, "" + z);
    }

    public void saveBooleanToggle(String str, boolean z) {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(USER_SP, 0).edit();
        edit.putBoolean(str, z);
        edit.apply();
        Log.e(str, "" + z);
    }

    public void saveCheckBoxState(String str, boolean z) {
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.putBoolean(str, z);
        edit.apply();
    }

    public void saveInt(String str, int i) {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(USER_SP, 0).edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public void saveLastTimeUsed(String str, long j) {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(USER_SP, 0).edit();
        edit.putLong(str, j);
        edit.apply();
    }

    public void saveLong(String str, long j) {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(USER_SP, 0).edit();
        edit.putLong(str, j);
        edit.apply();
    }

    public void saveString(String str, String str2) {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(USER_SP, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public void saveStringDefault(String str, String str2) {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(USER_SP, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public void saveStringSleep(String str, String str2) {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(USER_SP, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public void setRealTimeProtectionState(boolean z) {
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.putBoolean(REALTIME_PROTECTION_STATE, z);
        edit.apply();
    }
}
