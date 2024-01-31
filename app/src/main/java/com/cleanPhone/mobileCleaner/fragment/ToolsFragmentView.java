package com.cleanPhone.mobileCleaner.fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.ads.DH_sm_TemplateView;
import com.cleanPhone.mobileCleaner.coolers.CoolerCPUAnimationActivity;
import com.cleanPhone.mobileCleaner.gmebooster.GameBoostActivity;
import com.cleanPhone.mobileCleaner.tools.DeviceDetailActivity;
import com.cleanPhone.mobileCleaner.utility.GlobalData;

public class ToolsFragmentView extends Fragment implements View.OnClickListener {
    public Context V;
    private boolean isInBackground = false;
    private View view;
    View space;
    int admob=2;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardview_appaddiction:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                }
                return;
            case R.id.cardview_appmanager:
            case R.id.cardview_downloads:
            case R.id.cardview_duplicates:
            default:
                return;
            case R.id.cardview_cpucooler:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }

                    startActivity(new Intent(getActivity(), CoolerCPUAnimationActivity.class));



            case R.id.cardview_datausage:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }
                GlobalData.fromDatausageSetting = false;
                return;
            case R.id.cardview_devicedetail:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }

                    startActivity(new Intent(getActivity(), DeviceDetailActivity.class));


                return;
            case R.id.cardview_gamebosster:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }

      startActivity(new Intent(getActivity(), GameBoostActivity.class));


                return;
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_toolbox, viewGroup, false);
        this.V = getActivity();
        LinearLayout linearLayout2 = (LinearLayout) this.view.findViewById(R.id.cardview_appaddiction);
        ((LinearLayout) this.view.findViewById(R.id.cardview_devicedetail)).setOnClickListener(this);
        ((LinearLayout) this.view.findViewById(R.id.cardview_gamebosster)).setOnClickListener(this);
        ((LinearLayout) this.view.findViewById(R.id.cardview_datausage)).setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        ((LinearLayout) this.view.findViewById(R.id.cardview_cpucooler)).setOnClickListener(this);

        int i = Build.VERSION.SDK_INT;
        if (i < 21) {
            linearLayout2.setVisibility(View.GONE);
        }


        showInterstialAds(getActivity());
        DH_sm_TemplateView templateView=view.findViewById(R.id.templetview);
        LinearLayout llBanner = view.findViewById(R.id.llBanner);
        DH_GllobalItem.showFBnative(getActivity(),llBanner,templateView);




        return this.view;
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
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.isInBackground = false;
        DH_GllobalItem.errorAds(getActivity());
    }
    public void loadinit() {
        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getContext(), "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        return;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (DH_GllobalItem.fbNative == null) {
            DH_GllobalItem.loadNativeAd(getActivity());
        }
    }
}