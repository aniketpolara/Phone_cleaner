package com.cleanPhone.mobileCleaner.tracking;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CountryCode extends IntentService {
    private String country;
    private SharedPrefUtil sharedPrefUtil;
    private HttpURLConnection urlConnection;

    public CountryCode() {
        super("CountryCode");
    }

    private String downloadUrl(String str) throws IOException {
        String str2;
        InputStream inputStream = null;
        this.urlConnection = null;
        try {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                this.urlConnection = httpURLConnection;
                httpURLConnection.connect();
                inputStream = this.urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    stringBuffer.append(readLine);
                }
                str2 = stringBuffer.toString();
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e = e;
                    Log.e("", e.toString());
                    return str2;
                }
            } finally {
                inputStream.close();
                this.urlConnection.disconnect();
            }
        } catch (Exception e2) {
            str2 = "";
        }
        return str2;
    }

    @Override
    public void onHandleIntent(Intent intent) {
        SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(getBaseContext());
        this.sharedPrefUtil = sharedPrefUtil;
        String str = "US";
        Util.appendLogmobicleanTest("Before start service Country Code", TextUtils.isEmpty(sharedPrefUtil.getString(SharedPrefUtil.COUNTRYNAME)) ? "US" : this.sharedPrefUtil.getString(SharedPrefUtil.COUNTRYNAME), "");
        Util.appendLogmobicleanTest(CountryCode.class.getName(), "Service started====", "");
        if (this.sharedPrefUtil.getString(SharedPrefUtil.COUNTRYNAME) == null) {
            try {
                try {
                    JSONObject jSONObject = new JSONObject(downloadUrl(GlobalData.COUNTRY_URL + this.sharedPrefUtil.getString(SharedPrefUtil.IP) + "/46"));
                    String string = jSONObject.getString(FirebaseAnalytics.Param.CURRENCY);
                    String string2 = jSONObject.getString("country");
                    String string3 = jSONObject.getString("ccode");
                    this.sharedPrefUtil.saveString(SharedPrefUtil.CURRENCY, string);
                    this.sharedPrefUtil.saveString(SharedPrefUtil.COUNTRYNAME, string2);
                    this.sharedPrefUtil.saveString("COUNTRY_CODE", string3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Util.appendLogmobiclean(CountryCode.class.getName(), "Service finish====", "");
                if (!TextUtils.isEmpty(this.country)) {
                    str = this.country;
                }
                Util.appendLogmobiclean("After service finished Country Code", str, "");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
