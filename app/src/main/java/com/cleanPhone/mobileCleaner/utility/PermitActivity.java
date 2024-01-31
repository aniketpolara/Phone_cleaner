package com.cleanPhone.mobileCleaner.utility;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;


public abstract class PermitActivity extends ParentActivity {
    public boolean fromPermissionSettings = false;
    private SparseIntArray mErrorString;

    public boolean checkStoragePermissions() {
        int i = Build.VERSION.SDK_INT;
        if (i >= 30) {
            return Environment.isExternalStorageManager();
        }
        return i < 23 || ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0;
    }

    public void closebtnClick() {
        findViewById(R.id.iv_permission_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermitActivity.this.multipleClicked()) {
                    return;
                }
                PermitActivity.this.finish();
            }
        });
    }

    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (Build.VERSION.SDK_INT >= 30) {
            if (Environment.isExternalStorageManager()) {
                Util.f5356a = true;
                onPermissionsGranted(i);
                return;
            }
            Util.f5356a = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PermitActivity.this.findViewById(R.id.hiddenpermissionlayout).setVisibility(View.VISIBLE);
                    PermitActivity.this.findViewById(R.id.hiddenpermissionlayout).bringToFront();
                    PermitActivity.this.findViewById(R.id.hiddenpermissionlayout).setTranslationZ(10.0f);
                    if (PermitActivity.this.findViewById(R.id.ad_view_banner_container) != null) {
                        PermitActivity.this.findViewById(R.id.ad_view_banner_container).setVisibility(View.GONE);
                    }
                }
            });
            Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.mErrorString = new SparseIntArray();
    }

    public abstract void onPermissionsGranted(int i);

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        int i2 = 0;
        for (int i3 : iArr) {
            i2 += i3;
        }
        if (iArr.length > 0 && i2 == 0) {
            Util.f5356a = true;
            onPermissionsGranted(i);
            return;
        }
        Util.f5356a = false;
        findViewById(R.id.hiddenpermissionlayout).setVisibility(View.VISIBLE);
        findViewById(R.id.hiddenpermissionlayout).bringToFront();
        findViewById(R.id.hiddenpermissionlayout).setTranslationZ(10.0f);
        if (findViewById(R.id.ad_view_banner_container) != null) {
            findViewById(R.id.ad_view_banner_container).setVisibility(View.GONE);
        }
    }

    public void requestAppPermissions(final String[] strArr, int i, final int i2) {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Util.f5356a = false;
                try {
                    Intent intent = new Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                    startActivityForResult(intent, i2);
                } catch (Exception unused) {
                    Intent intent2 = new Intent();
                    intent2.setAction("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION");
                    startActivityForResult(intent2, i2);
                }
            } else {
                Util.f5356a = true;
                onPermissionsGranted(i2);
            }
        } else {
            this.mErrorString.put(i2, i);
            int i3 = 0;
            boolean z = false;
            for (String str : strArr) {
                i3 += ContextCompat.checkSelfPermission(this, str);
                z = z || ActivityCompat.shouldShowRequestPermissionRationale(this, str);
            }
            if (i3 != 0) {
                Util.f5356a = false;
                if (z) {
                    findViewById(R.id.hiddenpermissionlayout).setVisibility(View.VISIBLE);
                    findViewById(R.id.hiddenpermissionlayout).bringToFront();
                    findViewById(R.id.hiddenpermissionlayout).setTranslationZ(10.0f);
                    if (findViewById(R.id.ad_view_banner_container) != null) {
                        findViewById(R.id.ad_view_banner_container).setVisibility(View.GONE);
                    }
                } else {
                    ActivityCompat.requestPermissions(this, strArr, i2);
                }
            } else {
                Util.f5356a = true;
                onPermissionsGranted(i2);
            }
        }

        findViewById(R.id.iv_permission_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermitActivity.this.multipleClicked()) {
                    return;
                }
                PermitActivity.this.finish();
            }
        });
        findViewById(R.id.btngrantpermission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 30) {
                    if (!Environment.isExternalStorageManager()) {
                        Util.f5356a = false;
                        try {
                            Intent intent3 = new Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION");
                            intent3.addCategory("android.intent.category.DEFAULT");
                            intent3.setData(Uri.parse(String.format("package:%s", PermitActivity.this.getApplicationContext().getPackageName())));
                            PermitActivity.this.startActivityForResult(intent3, i2);
                            return;
                        } catch (Exception unused2) {
                            Intent intent4 = new Intent();
                            intent4.setAction("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION");
                            PermitActivity.this.startActivityForResult(intent4, i2);
                            return;
                        }
                    }
                    Util.f5356a = true;
                    PermitActivity.this.onPermissionsGranted(i2);
                    return;
                }
                ContextCompat.checkSelfPermission(PermitActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE");
                if (!GlobalData.phone_state_Permission) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(PermitActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                        ActivityCompat.requestPermissions(PermitActivity.this, strArr, i2);
                        return;
                    }
                    Intent intent5 = new Intent();
                    intent5.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent5.addCategory("android.intent.category.DEFAULT");
                    intent5.setData(Uri.parse("package:" + PermitActivity.this.getPackageName()));
                    intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent5.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent5.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    PermitActivity.this.startActivity(intent5);
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(PermitActivity.this, "android.permission.READ_PHONE_STATE")) {
                    ActivityCompat.requestPermissions(PermitActivity.this, strArr, i2);
                } else {
                    PermitActivity.this.fromPermissionSettings = true;
                    Intent intent6 = new Intent();
                    intent6.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent6.addCategory("android.intent.category.DEFAULT");
                    intent6.setData(Uri.parse("package:" + PermitActivity.this.getPackageName()));
                    intent6.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent6.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent6.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    PermitActivity.this.startActivity(intent6);
                }
            }
        });
    }
}
