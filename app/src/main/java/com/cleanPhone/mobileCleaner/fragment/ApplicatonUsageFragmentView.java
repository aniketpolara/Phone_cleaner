package com.cleanPhone.mobileCleaner.fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.SocialAnimationActivity;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.ads.DH_sm_TemplateView;
import com.cleanPhone.mobileCleaner.antimalware.ScannigActivity;
import com.cleanPhone.mobileCleaner.notifimanager.NotificationCleanerActivity;
import com.cleanPhone.mobileCleaner.tools.PrivateBrowser;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;

public class ApplicatonUsageFragmentView extends Fragment implements View.OnClickListener {
    public Context V;
    public SharedPrefUtil W;
    int admob = 2;
    int click = 0;
    int numOfClick = 3;
    AdRequest adRequest;
    RelativeLayout rl_native;
    View space;
    private boolean isInBackground = false;
    private long lastClickTime;
    private ProgressDialog progressDialog;
    private View view2;
    private InterstitialAd mInterstitialAd;

    public void g0() {
        startActivity(new Intent(getActivity(), ScannigActivity.class));
    }

    public void i0() {
        startActivity(new Intent(getActivity(), SocialAnimationActivity.class));
    }

    public void stopLoader() {
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.progressDialog.dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardview_notification:
                if (!((HomeActivity) getActivity()).multipleClicked() && SystemClock.elapsedRealtime() - this.lastClickTime >= 3000) {
                    this.lastClickTime = SystemClock.elapsedRealtime();
                    GlobalData.fromNotificationsetting = false;

                    startActivity(new Intent(getActivity(), NotificationCleanerActivity.class));


                    return;
                }
                return;
            case R.id.cardview_privatebrowser:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }

                startActivity(new Intent(getActivity(), PrivateBrowser.class));


                return;
            case R.id.cardview_scanning:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }

                if (Util.isConnectingToInternet(getActivity()) && !Util.isAdsFree(this.V)) {

                    startActivity(new Intent(getActivity(), ScannigActivity.class));

                    return;
                }
                return;
            case R.id.cardview_social:
            default:
                return;
            case R.id.cardview_socialcleaner:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }

                    startActivity(new Intent(getActivity(), SocialAnimationActivity.class));



        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view2 = layoutInflater.inflate(R.layout.fragment_app_usage, viewGroup, false);
        this.V = getActivity();
        this.W = new SharedPrefUtil(this.V);


        LinearLayout linearLayout = this.view2.findViewById(R.id.cardview_scanning);
        LinearLayout linearLayout2 = this.view2.findViewById(R.id.cardview_privatebrowser);
        LinearLayout linearLayout3 = this.view2.findViewById(R.id.cardview_socialcleaner);
        LinearLayout linearLayout4 = this.view2.findViewById(R.id.cardview_notification);
        LinearLayout linearLayout5 = this.view2.findViewById(R.id.cardview_app_lock);
        if (this.W.getBoolean("SHOW_ANTIVIRUS")) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        linearLayout.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        linearLayout3.setOnClickListener(this);
        linearLayout4.setOnClickListener(this);
        linearLayout5.setOnClickListener(this);
        if (Build.VERSION.SDK_INT < 19) {
            linearLayout4.setVisibility(View.GONE);
        }
        

        showInterstialAds(getActivity());
        DH_sm_TemplateView templateView=view2.findViewById(R.id.templetview);
        LinearLayout llBanner = view2.findViewById(R.id.llBanner);
        DH_GllobalItem.showFBnative(getActivity(),llBanner,templateView);

        return this.view2;
    }

    public void loadinit() {
        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getContext(), "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.isInBackground = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.isInBackground = false;
    }

    public class AnonymousClass1 implements Runnable {
        public AnonymousClass1() {
        }

        public void b() {

            ApplicatonUsageFragmentView.this.startActivity(new Intent(ApplicatonUsageFragmentView.this.getActivity(), ScannigActivity.class));


        }

        @Override
        public void run() {
            if (ApplicatonUsageFragmentView.this.progressDialog.isShowing()) {
                ApplicatonUsageFragmentView.this.stopLoader();
                AnonymousClass1.this.b();
                Util.appendLogmobicleanTest("", "Time exceeded to load ad, scan screen called", "fulladissue.txt");

                ApplicatonUsageFragmentView.this.startActivity(new Intent(ApplicatonUsageFragmentView.this.getActivity(), ScannigActivity.class));


            }
        }
    }

}
