package com.cleanPhone.mobileCleaner;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

public class ShareActivity extends ParentActivity {
    private static final String SHARE_LINK_FB = "utm_source=twitter&utm_campaign=sharing&pxl=";
    private static final String SHARE_LINK_TW = "utm_source=twitter&utm_campaign=sharing&pxl=";
    private static final String SHARE_LINK_WH = "utm_source=whatsapp&utm_campaign=sharing&pxl=";
    private int facePosition = 0;


    public Drawable getADrawable(int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return getResources().getDrawable(i, getTheme());
        }
        return getResources().getDrawable(i);
    }


    private boolean isIntentAvailable(Context context, Intent intent) {
        return context.getPackageManager().queryIntentActivities(intent, 65536).size() > 0;
    }



    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + str);
        }
    }

    public void facebookProfileVisit() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getFacebookPageURL())));
        } catch (Exception unused) {
        }
    }

    public int getFacePosition() {
        return this.facePosition;
    }

    public String getFacebookPageURL() {
        PackageManager packageManager = getPackageManager();
        try {
            return packageManager.getApplicationInfo("com.facebook.katana", 0).enabled ? packageManager.getPackageInfo("com.facebook.katana", 0).versionCode >= 3002850 ? "fb://facewebmodal/f?href=https://www.facebook.com/MobiClean" : "fb://page/https://www.facebook.com/MobiClean" : "https://www.facebook.com/MobiClean";
        } catch (PackageManager.NameNotFoundException unused) {
            return "https://www.facebook.com/MobiClean";
        }
    }

    public void instaShare() {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(""));
        intent.setPackage("com.instagram.android");
        if (isIntentAvailable(this, intent)) {
            startActivity(intent);
        } else {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("")));
        }
    }

    public void send_facebook() {
        String str = getResources().getString(R.string.mbc_link_wh) + "utm_source=twitter&utm_campaign=sharing&pxl=" + new SharedPrefUtil(this).getString(SharedPrefUtil.PIX);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", str);
        intent.putExtra("android.intent.extra.TITLE", "" + getString(R.string.mbc_app_name));
        boolean z = false;
        Iterator<ResolveInfo> it = getApplicationContext().getPackageManager().queryIntentActivities(intent, 0).iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ResolveInfo next = it.next();
            if (next.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(next.activityInfo.packageName);
                z = true;
                break;
            }
        }
        if (!z) {
            intent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" + str));
        }
        startActivity(intent);
    }

    public void send_twitter() {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(String.format("https://twitter.com/intent/tweet?text=%s&url=%s", urlEncode(getResources().getString(R.string.mbc_whtsapp_line)), urlEncode(getResources().getString(R.string.mbc_link_wh) + "utm_source=twitter&utm_campaign=sharing&pxl=" + new SharedPrefUtil(this).getString(SharedPrefUtil.PIX)))));
        for (ResolveInfo resolveInfo : getPackageManager().queryIntentActivities(intent, 0)) {
            if (resolveInfo.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(resolveInfo.activityInfo.packageName);
            }
        }
        startActivity(intent);
    }

    public void send_whatspp() {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            String format = String.format(getResources().getString(R.string.mbc_whtsapp_line) + getResources().getString(R.string.mbc_link_wh) + SHARE_LINK_WH + new SharedPrefUtil(this).getString(SharedPrefUtil.PIX), new Object[0]);
            intent.setPackage("com.whatsapp");
            intent.putExtra("android.intent.extra.TEXT", format);
            startActivity(Intent.createChooser(intent, "" + getString(R.string.share_with)));
        } catch (Exception unused) {
            Toast.makeText(this, "" + getString(R.string.whatsApp) + " " + getResources().getString(R.string.mbc_not_installed), Toast.LENGTH_SHORT).show();
        }
    }

    public void setFacePosition(int i) {
        this.facePosition = i;
    }

    public void shareApp() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", getString(R.string.mbc_whtsapp_line) + " ");
        intent.putExtra("android.intent.extra.SUBJECT", getString(R.string.appname));
        startActivity(Intent.createChooser(intent, "" + getString(R.string.share)));
    }


}
