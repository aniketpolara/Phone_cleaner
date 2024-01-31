package com.cleanPhone.mobileCleaner.fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.cleanPhone.mobileCleaner.DownloadsActivity;
import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.SocialAnimationActivity;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.ads.DH_sm_TemplateView;
import com.cleanPhone.mobileCleaner.app.ApplicationManagerActivity;
import com.cleanPhone.mobileCleaner.similerphotos.DuplicacyMidSettings;
import com.cleanPhone.mobileCleaner.tools.SpaceManagerActivity;
import com.cleanPhone.mobileCleaner.utility.GlobalData;


public class FreeupSpaceFragmentParent extends Fragment implements View.OnClickListener {
    public Context V;
    private boolean isInBackground = false;
    private View view;
    RelativeLayout rl_native;
    View space;
    int admob=2;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;


    private void setDeviceDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (getActivity() != null) {
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardview_appmanager:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }


                    startActivity(new Intent(getActivity(), ApplicationManagerActivity.class));





                return;
            case R.id.cardview_downloads:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }
                    startActivity(new Intent(getActivity(), DownloadsActivity.class));



                return;
            case R.id.cardview_duplicates:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }
                    GlobalData.fromSpacemanager = false;
                    startActivity(new Intent(getActivity(), DuplicacyMidSettings.class));



                return;
            case R.id.cardview_largefile:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }
                    GlobalData.fromSocialCleaning = false;
                    MobiClean.getInstance().duplicatesData = null;
                    startActivity(new Intent(getActivity(), SpaceManagerActivity.class).putExtra("FROM", "TB"));



                return;
            case R.id.cardview_social:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }
                    Intent intent = new Intent(getActivity(), SocialAnimationActivity.class);
                    intent.putExtra(ShareConstants.TITLE, true);
                    intent.putExtra("FROM", "JUNK_MEDIA_CLEANER");
                    startActivity(intent);


                return;
            case R.id.cardview_spacemanager:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }


                    GlobalData.fromSocialCleaning = false;
                    MobiClean.getInstance().duplicatesData = null;
                    GlobalData.returnedAfterDeletion = false;
                    startActivity(new Intent(getActivity(), SpaceManagerActivity.class));


                return;
            default:
                return;
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(getActivity());
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_freeupspasce, viewGroup, false);
        this.V = getActivity();
        setDeviceDimensions();
        ((LinearLayout) this.view.findViewById(R.id.cardview_appmanager)).setOnClickListener(this);
        ((LinearLayout) this.view.findViewById(R.id.cardview_spacemanager)).setOnClickListener(this);
        ((LinearLayout) this.view.findViewById(R.id.cardview_duplicates)).setOnClickListener(this);
        ((LinearLayout) this.view.findViewById(R.id.cardview_largefile)).setOnClickListener(this);
        ((LinearLayout) this.view.findViewById(R.id.cardview_social)).setOnClickListener(this);
        ((LinearLayout) this.view.findViewById(R.id.cardview_downloads)).setOnClickListener(this);


        showInterstialAds(getActivity());
        DH_sm_TemplateView templateView=view.findViewById(R.id.templetview);
        LinearLayout llBanner = view.findViewById(R.id.llBanner);
        DH_GllobalItem.showFBnative(getActivity(),llBanner,templateView);

        return this.view;
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
}
