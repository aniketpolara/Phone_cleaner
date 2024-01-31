package com.cleanPhone.mobileCleaner;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


public class ImgScreen extends AppCompatActivity {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_img_screen);
        final SpinImgView spinImgView = (SpinImgView) findViewById(R.id.spin);
        spinImgView.setOnRotationListener(new SpinImgView.OnRotationListener<String>() {
            @Override
            public void onRotation() {
                Log.d("XXXX", "On Rotation");
            }

            @Override
            public void onStopRotation(String str) {
                Log.d("XXXX", "On Rotation");
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spinImgView.rotate(50.0f, 3000L, 50L);
            }
        }, 1000L);
    }
}
