package com.cleanPhone.mobileCleaner.app;

import android.content.Context;
import android.os.AsyncTask;

import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.wrappers.AppManagerData;


public class ApplicationManagerWork extends AsyncTask<String, String, AppManagerData> {
    private Context context;

    public ApplicationManagerWork(Context context) {
        this.context = context;
    }

    @Override
    public AppManagerData doInBackground(String... strArr) {
        MobiClean.getInstance().appManagerData = new AppManagerData();
        MobiClean.getInstance().appManagerData.getInstalledAppsData(this.context);
        MobiClean.getInstance().appManagerData.getRestoreData(this.context);
        return null;
    }
}
