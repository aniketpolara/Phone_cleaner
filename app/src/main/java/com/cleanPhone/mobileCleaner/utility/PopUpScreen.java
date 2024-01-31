package com.cleanPhone.mobileCleaner.utility;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;

import java.util.Timer;
import java.util.TimerTask;

public class PopUpScreen extends ParentActivity {
    private int deviceHeight;
    private int deviceWidth;
    public TextView i;
    private LayoutInflater layoutInflater;

    private void setDeviceDimensions() {
        DisplayMetrics displayMetrics = ParentActivity.displaymetrics;
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_pop_up_screen);
        setDeviceDimensions();
        this.i = (TextView) findViewById(R.id.tvpopupscreen);
        TextView textView = (TextView) findViewById(R.id.tvpopupscreen2);
        try {
            String stringExtra = getIntent().getStringExtra("MSG");
            if (stringExtra.toLowerCase().contains("permissions")) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
                layoutParams.setMargins(0, (this.deviceHeight * 32) / 100, 0, 0);
                textView.setLayoutParams(layoutParams);
                this.i.setVisibility(View.GONE);
            }
            textView.setText("" + stringExtra);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PopUpScreen.this.multipleClicked()) {
                    return;
                }
                PopUpScreen.this.finish();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PopUpScreen.this.multipleClicked()) {
                    return;
                }
                PopUpScreen.this.finish();
            }
        });
        try {
            new Timer().schedule(new TimerTask() { // from class: com.mobiclean.phoneclean.utility.PopUpScreen.3
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    try {
                        PopUpScreen.this.finish();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }, 4000L);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
