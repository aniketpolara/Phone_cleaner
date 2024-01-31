package com.cleanPhone.mobileCleaner.ads;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.FacebookSdk;
import com.facebook.ads.Ad;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.cleanPhone.mobileCleaner.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class DH_GllobalItem {
    //    public static InterstitialAd dFbInterstitialAd;
//    public static com.google.android.gms.ads.InterstitialAd dAdnobInterstitialAd;
    public static FirebaseRemoteConfig rconfin_FirebaseRemoteConfig;
    public static String string_TAG = "FirebaseRemoteConfigLog";
    public static String str_Admob_openAds_id;
    public static String str_Admob_native_advance;

    public static String str_Admob_interstial_id;
    public static String str_Admob_interstial_id2;
    public static String str_Admob_interstial_id3;

    public static long str_Admob_interstitial_loadtime_1;
    public static long str_Admob_interstitial_loadtime_2;
    public static long str_Admob_interstitial_loadtime_3;


    public static String str_Admob_adaptive_banner;//
    public static String str_Admob_adaptive_banner2;
    public static String str_Admob_adaptive_banner3;


    public static String str_Admob_native_id;
    public static String str_Admob_native_id1;
    public static String str_Admob_native_id2;

    public static InterstitialAd intad_AAdnobInterstitialAd;
    public static InterstitialAd intad_AdnobInterstitialAd2;
    public static InterstitialAd intad_AdnobInterstitialAd3;


    public static String fb_Adaptivebanner;
    public static String fb_interstial;
    public static String fb_native_ad;
//ama check kar id ave chene pachh han jov
    public static long fbinter_showtime;
    public static long fbinter_loadtime;

    public static boolean isLoadAdmobStart = false;

    public static boolean isRequestAdmob = false;
    public static boolean isRequestAdmob2 = false;
    public static boolean isRequestAdmob3 = false;

    public static boolean showInterstistial = true;


    public static boolean showfbinter = true;
    public static boolean showinter1 = true;
    public static boolean showinter2 = true;
    public static boolean showinter3 = true;


    public static long admob_Interstial_showtime;

    public static int int_NativeCount = 0;

    public static ArrayList<NativeAd> nad_LoadedNativeAds = new ArrayList<>();

    public static PH_NativeModel nemodl_NativeModel = new PH_NativeModel();
    private static int int_NativeAdCount = 3;

    public DH_GllobalItem() {
        nemodl_NativeModel = new PH_NativeModel();
    }

    public static void firebaseInit() {
        rconfin_FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        rconfin_FirebaseRemoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().setFetchTimeoutInSeconds(0).build());
        rconfin_FirebaseRemoteConfig.fetch(0);
        rconfin_FirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            initadskey();

//
//                            admob_openAds_id = "ca-app-pub-3940256099942544/3419835294";
//                            admob_interstial_id = "ca-app-pub-3940256099942544/1033173712";
//                            admob_interstial_id2 = "ca-app-pub-3940256099942544/1033173712";
//                            admob_interstial_id3 = "ca-app-pub-3940256099942544/1033173712";
//                            admob_interstitial_loadtime_1 = 3000;
//                            admob_interstitial_loadtime_2 = 3000;
//                            admob_interstitial_loadtime_3 = 3000;
//                            admob_adaptive_banner = "ca-app-pub-3940256099942544/6300978111";
//                            admob_adaptive_banner2 = "ca-app-pub-3940256099942544/6300978111";
//                            admob_adaptive_banner3 = "ca-app-pub-3940256099942544/6300978111";
//                            admob_rewarded_id = "ca-app-pub-3940256099942544/5224354917";
//                            admob_rewarded_id2 = "ca-app-pub-3940256099942544/5224354917";
//                            admob_rewarded_id3 = "ca-app-pub-3940256099942544/5224354917";
//                            admob_reward_loadtime_1 = 10000;
//                            admob_reward_loadtime_2 = 10000;
//                            admob_reward_loadtime_3 = 10000;
//                            admob_interstial_showtime = 3000;
//                            admob_native_id = "ca-app-pub-3940256099942544/2247696110";


                            Log.d("checkfetchdate", "openad" + rconfin_FirebaseRemoteConfig.getString("admob_app_open"));
                            Log.d("checkfetchdate", "openad" + rconfin_FirebaseRemoteConfig.getLong("admob_interstial_showtime"));
                        } else {
                            Log.d(string_TAG, "Fetch Failed");
                        }
                    }
                });
    }

    public static void initadskey() {
        str_Admob_openAds_id = DH_GllobalItem.rconfin_FirebaseRemoteConfig.getString("admob_app_open");
        str_Admob_interstial_id = DH_GllobalItem.rconfin_FirebaseRemoteConfig.getString("admob_interstial_id_1");
        str_Admob_interstial_id2 = DH_GllobalItem.rconfin_FirebaseRemoteConfig.getString("admob_interstial_id_2");
        str_Admob_interstial_id3 = DH_GllobalItem.rconfin_FirebaseRemoteConfig.getString("admob_interstial_id_3");
        str_Admob_interstitial_loadtime_1 = DH_GllobalItem.rconfin_FirebaseRemoteConfig.getLong("admob_interstial_loadtime_1");
        str_Admob_interstitial_loadtime_2 = DH_GllobalItem.rconfin_FirebaseRemoteConfig.getLong("admob_interstial_loadtime_2");
        str_Admob_interstitial_loadtime_3 = DH_GllobalItem.rconfin_FirebaseRemoteConfig.getLong("admob_interstial_loadtime_3");
        str_Admob_adaptive_banner = DH_GllobalItem.rconfin_FirebaseRemoteConfig.getString("admob_banner_1");
        str_Admob_adaptive_banner2 = DH_GllobalItem.rconfin_FirebaseRemoteConfig.getString("admob_banner_2");
        str_Admob_adaptive_banner3 = DH_GllobalItem.rconfin_FirebaseRemoteConfig.getString("admob_banner_3");
        admob_Interstial_showtime = rconfin_FirebaseRemoteConfig.getLong("admob_interstial_showtime");
        str_Admob_native_id = rconfin_FirebaseRemoteConfig.getString("admob_native_id");
        str_Admob_native_id1 = rconfin_FirebaseRemoteConfig.getString("admob_native_id1");
        str_Admob_native_id2 = rconfin_FirebaseRemoteConfig.getString("admob_native_id2");

        fbinter_showtime = rconfin_FirebaseRemoteConfig.getLong("fbinter_showtime");
        fbinter_loadtime = rconfin_FirebaseRemoteConfig.getLong("fbinter_loadtime");

        fb_Adaptivebanner = rconfin_FirebaseRemoteConfig.getString("fb_Adaptivebanner");
        fb_interstial = rconfin_FirebaseRemoteConfig.getString("fb_interstial");
        fb_native_ad = rconfin_FirebaseRemoteConfig.getString("fb_native_ad");

//        superAdsManagerGlry.fb_native_ad = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
//        superAdsManagerGlry.fb_Adaptivebanner="IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
//        superAdsManagerGlry.fb_interstial="IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";


        Log.d("checkfetchdate", "openad" + fbinter_showtime);
        Log.d("checkfetchdate", "openad" + fbinter_loadtime);
        Log.d("checkfetchdate", "fb_Adaptivebanner" + fb_Adaptivebanner);
        Log.d("checkfetchdate", "fb_interstial" + fb_interstial);
        Log.d("checkfetchdate", "fb_native_ad" + fb_native_ad);

    }

    public static boolean isNetworkAvailable(Context activity) {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    //fb Banner Ads
    public static NativeBannerAd nativeBannerAd;
    public static LinearLayout adView;


    public static void AdmobAdaptiveBanner(final Activity context, final ViewGroup linearLayout, BannerCallback bannerCallback) {
        if (fb_Adaptivebanner == null || fb_Adaptivebanner.equals("")) {
            firebaseInit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AdmobAdaptiveBanner(context, linearLayout, bannerCallback);
                }
            }, 2000);
        } else {
            if ((isNetworkAvailable(context))) {

                Log.d("BannerAds", "fb_Adaptivebanner===" + fb_Adaptivebanner);
                nativeBannerAd = new NativeBannerAd(context, fb_Adaptivebanner);

                NativeAdListener nativeAdListener = new NativeAdListener() {

                    @Override
                    public void onMediaDownloaded(Ad ad) {
                        // Native ad finished downloading all assets
                        Log.d("BannerAds", "Native ad finished downloading all assets.");
                    }


                    @Override
                    public void onError(Ad ad, com.facebook.ads.AdError adError) {
                        Log.d("BannerAds", "fbnative-onError---" + adError.getErrorMessage());
                        fbAdaptiveBanner(context, linearLayout, bannerCallback);

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {

                        Log.d("BannerAds", "fbnative-onAdLoaded");
                        // Native ad is loaded and ready to be displayed
                        if (nativeBannerAd == null || nativeBannerAd != ad) {
                            Log.d("BannerAds", " nativeBannerAd nul;l");
                            return;
                        }
                        // Inflate Native Banner Ad into Container
                        Log.d("BannerAds", " inflateAd first");

                        inflateAd(nativeBannerAd, context, linearLayout);
                        bannerCallback.onAdLoad();


                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                        // Native ad clicked
                        Log.d("BannerAds", "Native ad clicked!");
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                        // Native ad impression
                        Log.d("BannerAds", "Native ad impression logged!");
                    }
                };
                nativeBannerAd.loadAd(
                        nativeBannerAd.buildLoadAdConfig()
                                .withAdListener(nativeAdListener)
                                .build());

            } else {
                Log.d("BannerAds", "setVisibility gone!");
                linearLayout.setVisibility(View.GONE);
            }
        }
    }

    //view for native ads
    public static void inflateAd(NativeBannerAd nativeBannerAd, Context context, ViewGroup linearlayout) {
        nativeBannerAd.unregisterView();
        NativeAdLayout nativeAdLayout = new NativeAdLayout(context);


        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.ph_mbitty_native_banner_ad_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeBannerAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);
        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        MediaView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());
        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
        linearlayout.removeAllViews();
        linearlayout.addView(nativeAdLayout);

    }

// bNner
    public static void fbAdaptiveBanner(final Activity context, final ViewGroup linearLayout, BannerCallback bannerCallback) {
//        if (!clGameSaver.getUpgrade(context)) {
        if (str_Admob_adaptive_banner == null || str_Admob_adaptive_banner.equals("")) {
            firebaseInit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fbAdaptiveBanner(context, linearLayout, bannerCallback);
                }
            }, 2000);
        } else {

            //ads request
            RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_MA)
                    .build();
            MobileAds.setRequestConfiguration(requestConfiguration);


            AdView adView = new AdView(context);
            adView.setAdUnitId(str_Admob_adaptive_banner);
            linearLayout.removeAllViews();
            AdSize adSize = getAdSize(context, linearLayout);
//              adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdSize(adSize);

//            try {
////                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, adSize.getHeightInPixels(context)));
//            } catch (ClassCastException e) {
//                try {
//                    linearLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, adSize.getHeightInPixels(context)));
//                } catch (ClassCastException ex) {
//                    linearLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, adSize.getHeightInPixels(context)));
//                }
//            }

            linearLayout.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    AdmobAdaptiveBanner2(context, linearLayout, bannerCallback);
                    Log.d("BannerAds", "AdmobBanner Error1 - " + loadAdError.getMessage());
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdLoaded() {
                    bannerCallback.onAdLoad();
                    super.onAdLoaded();
                    Log.d("BannerAds", "AdmobAdaptiveBanner load1 - ");
                }
            });

            adView.loadAd(adRequest);
        }
//        }
    }

    public static void AdmobAdaptiveBanner2(final Activity context, final ViewGroup linearLayout, BannerCallback bannerCallback) {
        if (str_Admob_adaptive_banner2 == null || str_Admob_adaptive_banner2.equals("")) {
            firebaseInit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AdmobAdaptiveBanner2(context, linearLayout, bannerCallback);
                }
            }, 2000);
        } else {

            RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_MA)
                    .build();
            MobileAds.setRequestConfiguration(requestConfiguration);


            AdView adView = new AdView(context);
            adView.setAdUnitId(str_Admob_adaptive_banner2);
            linearLayout.removeAllViews();
            AdSize adSize = getAdSize(context, linearLayout);
//              adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdSize(adSize);
//            try {
//                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, adSize.getHeightInPixels(context)));
//            } catch (ClassCastException e) {
//                try {
//                    linearLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, adSize.getHeightInPixels(context)));
//                } catch (ClassCastException ex) {
//                    linearLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, adSize.getHeightInPixels(context)));
//                }
//            }
            linearLayout.addView(adView);
            AdRequest adRequest =
                    new AdRequest.Builder().build();
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }


                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
//                        callback.onBannerFailed();
                    AdmobAdaptiveBanner3(context, linearLayout, bannerCallback);
                    Log.d("BannerAds", "AdmobAdaptiveBanner2 fail - " + loadAdError.getMessage());
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdLoaded() {
                    bannerCallback.onAdLoad();
                    super.onAdLoaded();
                    Log.d("BannerAds", "AdmobAdaptiveBanner2 load - ");
                }
            });
            adView.loadAd(adRequest);
        }
    }

    public static void AdmobAdaptiveBanner3(final Activity context, final ViewGroup linearLayout, BannerCallback bannerCallback) {
        if (str_Admob_adaptive_banner3 == null || str_Admob_adaptive_banner3.equals("")) {
            firebaseInit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AdmobAdaptiveBanner3(context, linearLayout, bannerCallback);
                }
            }, 2000);
        } else {

            RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_MA)
                    .build();
            MobileAds.setRequestConfiguration(requestConfiguration);

            AdView adView = new AdView(context);
            adView.setAdUnitId(str_Admob_adaptive_banner3);
            linearLayout.removeAllViews();
            AdSize adSize = getAdSize(context, linearLayout);
//              adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdSize(adSize);
//            try {
//                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, adSize.getHeightInPixels(context)));
//            } catch (ClassCastException e) {
//                try {
//                    linearLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, adSize.getHeightInPixels(context)));
//                } catch (ClassCastException ex) {
//                    linearLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, adSize.getHeightInPixels(context)));
//                }
//            }
            linearLayout.addView(adView);
            AdRequest adRequest =
                    new AdRequest.Builder().build();
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }


                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    bannerCallback.onAdFail();
                    Log.d("BannerAds", "AdmobAdaptiveBanner3 fail - " + loadAdError.getMessage());
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdLoaded() {
                    bannerCallback.onAdLoad();
                    super.onAdLoaded();
                    Log.d("BannerAds", "AdmobAdaptiveBanner3 load - ");
                }
            });
            adView.loadAd(adRequest);
        }
    }

    public static AdSize getAdSize(Activity activity, ViewGroup linearLayout) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = linearLayout.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public static boolean isRequestfacebook = false;
    public static com.facebook.ads.InterstitialAd dFbInterstitialAd;
    public static boolean isLoadAdsfb = true;


    public static void fbInterstitialAds(final Activity context) {

        if (fb_interstial == null || fb_interstial.equals("")) {
            firebaseInit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fbInterstitialAds(context);
                }
            }, 2000);

        } else {


            Log.d("1InterstitialAds", "FB fbInterstitialAds");
            dFbInterstitialAd = new com.facebook.ads.InterstitialAd(context, fb_interstial);
            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {

                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    Log.d("InterstitialAds", "FB onInterstitialDismissed");
                    isRequestfacebook = false;
                    showInterstistial = false;
                    showfbinter = false;
                    dFbInterstitialAd = null;
//                    showFbaftertime = false;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("InterstitialAds", "FBonAdLoaded admob_interstial_showtime complete");
                            showInterstistial = true;
                            errorAds(context);

                        }
                    }, 500);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("InterstitialAds", "FBonAdLoaded admob_facebook_loadtime complete");
                            showfbinter = true;
                            errorAds(context);

                        }
                    }, 500);
                }

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    isRequestfacebook = false;
                    Log.d("InterstitialAds", "fb-onError" + adError.getErrorMessage());

                    dFbInterstitialAd = null;
                    if (intad_AAdnobInterstitialAd == null) {
                        if (!isRequestAdmob) {
                            admobInterstitialAds(context);
                        }
                    }

                }

                @Override
                public void onAdLoaded(Ad ad) {
                    isRequestfacebook = true;
                    Log.d("InterstitialAds", "FB onAdLoaded");
                    isLoadAdsfb = true;
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            };
            dFbInterstitialAd.loadAd(dFbInterstitialAd.buildLoadAdConfig()
                    .withAdListener(interstitialAdListener)
                    .build());

        }

    }


    public static void admobInterstitialAds(Activity context/*, onAdWorkLoadedListener onAdWorkCompletedListener1*/) {

        if (str_Admob_interstial_id == null || str_Admob_interstial_id.equals("")) {
            firebaseInit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    admobInterstitialAds(context);
                }
            }, 2000);
        } else {

            RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_MA)
                    .build();
            MobileAds.setRequestConfiguration(requestConfiguration);

            isLoadAdmobStart = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(context, str_Admob_interstial_id, adRequest, new InterstitialAdLoadCallback() {

                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    Log.d("InterstitialAds", "Admob load 1");
                    intad_AAdnobInterstitialAd = interstitialAd;
                    isRequestAdmob = true;
                    isLoadAdmobStart = true;

                    intad_AAdnobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            Log.d("InterstitialAds", "onAdFailed show1---" + adError.getMessage());
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            intad_AAdnobInterstitialAd = null;
                            Log.d("InterstitialAds", "Interstitial1-dismiss");
                            showInterstistial = false;
                            isRequestAdmob = false;
                            showinter1 = false;

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("InterstitialAds", "Interstitial-show time Complete");

                                    showInterstistial = true;
                                }
                            }, admob_Interstial_showtime);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showinter1 = true;
                                    Log.d("InterstitialAds", "admob_interstial_id 11, ----load-------------- time complete");

                                }
                            }, (int) str_Admob_interstitial_loadtime_1);


                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent();
                            intad_AAdnobInterstitialAd = null;
                        }
                    });


                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    isLoadAdmobStart = false;
                    intad_AAdnobInterstitialAd = null;
                    isRequestAdmob = false;

                    Log.d("InterstitialAds", "Interstitial1-Error" + loadAdError.getMessage());

                    if (intad_AdnobInterstitialAd2 == null) {
                        if (!isRequestAdmob2) {
                            admobInterstitialAds2(context);
                        }
                    }
                }

            });
        }

    }

    public static void admobInterstitialAds2(final Context context) {

        if (str_Admob_interstial_id2 == null || str_Admob_interstial_id2.equals("")) {
            firebaseInit();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    admobInterstitialAds2(context);
                }
            }, 2000);
        } else {
            isLoadAdmobStart = true;
            RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_MA)
                    .build();
            MobileAds.setRequestConfiguration(requestConfiguration);
            try {
                AdRequest adRequest = new AdRequest.Builder().build();
                Log.d("InterstitialAds", "Admob adRequest2");
                InterstitialAd.load(context, str_Admob_interstial_id2, adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        Log.d("InterstitialAds", "Admob load 2");
                        intad_AdnobInterstitialAd2 = interstitialAd;
                        isLoadAdmobStart = true;
                        isRequestAdmob2 = true;

                        intad_AdnobInterstitialAd2.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                intad_AdnobInterstitialAd2 = null;
                                showInterstistial = false;
                                isRequestAdmob2 = false;
                                showinter2 = false;

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("InterstitialAds", "Interstitial2-show time Complete");
                                        showInterstistial = true;
                                    }
                                }, admob_Interstial_showtime);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("InterstitialAds", "admob_interstial_id 22, ----load-------------- time complete");
                                        showinter2 = true;
                                    }
                                }, (int) str_Admob_interstitial_loadtime_2);

                                Log.d("InterstitialAds", "Interstitial2-dismiss");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.d("InterstitialAds", "Interstitial2-fail to show---" + adError.toString());
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                intad_AdnobInterstitialAd2 = null;
                                Log.d("TAG", "The ad was shown.");
                            }

                        });


                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.i(string_TAG, loadAdError.getMessage());
                        intad_AdnobInterstitialAd2 = null;
                        isRequestAdmob2 = false;
                        isLoadAdmobStart = false;
                        Log.d("InterstitialAds", "Interstitial2-Error" + loadAdError.getMessage());

                        if (intad_AdnobInterstitialAd3 == null) {
                            if (!isRequestAdmob3) {
                                admobInterstitialAds3(context);
                            }
                        }
                    }

                });

            } catch (Exception e) {
                e.getMessage();
            }
        }

    }


    //Interstitial.................

    public static void admobInterstitialAds3(final Context context) {

        if (str_Admob_interstial_id3 == null || str_Admob_interstial_id3.equals("")) {
            firebaseInit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    admobInterstitialAds3(context);
                }
            }, 2000);
        } else {
            isLoadAdmobStart = true;

            RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_MA)
                    .build();
            MobileAds.setRequestConfiguration(requestConfiguration);

//                -------------------
            try {
                AdRequest adRequest = new AdRequest.Builder().build();
                Log.d("InterstitialAds", "Admob adRequest3");
                InterstitialAd.load(context, str_Admob_interstial_id3, adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        intad_AdnobInterstitialAd3 = interstitialAd;
                        isLoadAdmobStart = true;
                        Log.d("InterstitialAds", "Admob load 3");
                        isRequestAdmob3 = true;

                        intad_AdnobInterstitialAd3.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                intad_AdnobInterstitialAd3 = null;
                                showInterstistial = false;
                                isRequestAdmob3 = false;

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("InterstitialAds", "Interstitial3-show time Complete");
                                        showInterstistial = true;
                                    }
                                }, admob_Interstial_showtime);

                                Log.d("InterstitialAds", "Interstitial3-dismiss");
                                showinter3 = false;


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("InterstitialAds", "admob_interstial_id 33, ----load-------------- time complete");
                                        showinter3 = true;
                                    }
                                }, (int) str_Admob_interstitial_loadtime_3);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.d("InterstitialAds", "Interstitial3-fail to show---" + adError.toString());
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                intad_AdnobInterstitialAd3 = null;

                                Log.d("TAG", "The ad was shown.");
                            }
                        });


                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.i(string_TAG, loadAdError.getMessage());
                        intad_AdnobInterstitialAd3 = null;
                        isRequestAdmob3 = false;
                        isLoadAdmobStart = false;
                        Log.d("InterstitialAds", "Interstitial3-Error" + loadAdError.getMessage());

                    }
                });

            } catch (Exception e) {
                Log.d("InterstitialAds", "app Exception" + e.getMessage());
                e.getMessage();
            }

        }

    }


    public static void showInterstialAds(Activity activity) {
        if (showInterstistial) {

            if (dFbInterstitialAd != null && dFbInterstitialAd.isAdLoaded()) {
                Log.d("InterstitialAds", "Facebook-Show");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dFbInterstitialAd.show();
                    }
                }, 100);

//                showFbaftertime = true;
                Log.d("InterstitialAds", "Facebook-Show");
            } else if (intad_AAdnobInterstitialAd != null) {
//                showFbaftertime = false;
                Log.d("InterstitialAds", "Admob show1");
                intad_AAdnobInterstitialAd.show(activity);

            } else if (intad_AdnobInterstitialAd2 != null) {
//                showFbaftertime = false;
                Log.d("InterstitialAds", "Admob show2");
                intad_AdnobInterstitialAd2.show(activity);

            } else if (intad_AdnobInterstitialAd3 != null) {
//                showFbaftertime = false;
                Log.d("InterstitialAds", "Admob show3");
                intad_AdnobInterstitialAd3.show(activity);

            } else {
                Log.d("InterstitialAds", "any Interstitial not loaded");
            }
        }


    }

//    public static void showOnlyAds(Activity activity) {
//
//
//        if (showInterstistial) {
//            if (mAdnobInterstitialAd != null) {
//                Log.d("InterstitialAds", "Admob show1");
//                mAdnobInterstitialAd.show(activity);
//
//            } else if (dAdnobInterstitialAd2 != null) {
//                Log.d("InterstitialAds", "Admob show2");
//                dAdnobInterstitialAd2.show(activity);
//
//            } else if (dAdnobInterstitialAd3 != null) {
//                Log.d("InterstitialAds", "Admob show3");
//                dAdnobInterstitialAd3.show(activity);
//
//            } else {
//                Log.d("InterstitialAds", "any Interstitial not loaded");
//            }
//        }
//
//
//    }


    public static void errorAds(Activity context) {
        if (showfbinter) {
            if (!isRequestfacebook) {
                if (dFbInterstitialAd == null) {
                    Log.d("1InterstitialAds", "dFbInterstitialAd null");
                    fbInterstitialAds(context);
                } else
                    Log.d("1InterstitialAds", "dFbInterstitialAd not null");
            } else {
                Log.d("1InterstitialAds", "isRequestfacebook true");
            }
        } else {
            Log.d("1InterstitialAds", "showfbinter false");
        }

        try {
            if (showinter1) {
                if (!isRequestAdmob) {
                    if (intad_AAdnobInterstitialAd == null) {
                        if (!isRequestAdmob) {
                            admobInterstitialAds(context);
                        } else {
                            Log.d("InterstitialAds", "errorAdmobload() inter 1-already request");
                        }
                    }
                } else {
                    Log.d("InterstitialAds", "errorAdmobload() inter 1-already request");
                }
            }
            if (showinter2) {
                if (!isRequestAdmob2) {
                    if (intad_AdnobInterstitialAd2 == null) {
                        if (!isRequestAdmob2) {
                            admobInterstitialAds2(context);
                        } else {
                            Log.d("InterstitialAds", "errorAdmobload() inter 2-already request");
                        }
                    }
                } else {
                    Log.d("InterstitialAds", "errorAdmobload() inter 2-already request");
                }
            }
            if (showinter3) {
                if (!isRequestAdmob3) {
                    if (intad_AdnobInterstitialAd3 == null) {
                        if (!isRequestAdmob3) {
                            admobInterstitialAds3(context);
                        } else {
                            Log.d("InterstitialAds", "errorAdmobload() inter 3-already request");
                        }
                    }
                } else {
                    Log.d("InterstitialAds", "errorAdmobload() inter 3-already request");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void destroyAdaptive(LinearLayout linearLayout) {
        if (linearLayout != null)
            linearLayout.removeAllViews();
    }

    public static com.facebook.ads.NativeAd nativeAd;
    public static LinearLayout adView2;
    public static NativeAdLayout fbNative = null;
    public static NativeAd admobNative = null;


    public static void loadNativeAd(final Activity context) {
        Log.d("NativeAd", "reest native: " + fb_native_ad);
        FacebookSdk.setIsDebugEnabled(true);
        if (fb_native_ad == null || fb_native_ad.equals("")) {
            firebaseInit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadNativeAd(context);
                }
            }, 2000);
        } else {
            if (isNetworkAvailable(context)) {
                nativeAd = new com.facebook.ads.NativeAd(context, fb_native_ad);
                NativeAdListener nativeAdListener = new NativeAdListener() {

                    @Override
                    public void onMediaDownloaded(Ad ad) {
                        // Native ad finished downloading all assets
                    }


                    @Override
                    public void onError(Ad ad, com.facebook.ads.AdError adError) {
                        NativeAds(context);
                        Log.d("NativeAd", "1111fbnative-onError---" + adError.getErrorMessage());

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        // Native ad is loaded and ready to be displayed
                        Log.d("NativeAd", "1111fbnative-onAdLoaded");

//                        NativeAds(context, admobview);

                        // Native ad is loaded and ready to be displayed
                        if (nativeAd == null || nativeAd != ad) {
                            Log.d("NativeAd", "retttt-onAdLoaded");
                            return;
                        }
                        // Inflate Native Banner Ad into Container
                        inflateAdnative(nativeAd, context);

                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                        // Native ad clicked
                        Log.d("NativeAd", "Native ad clicked!");
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                        // Native ad impression
                        Log.d("NativeAd", "Native ad impression logged!");
                    }
                };
                nativeAd.loadAd(
                        nativeAd.buildLoadAdConfig()
                                .withAdListener(nativeAdListener)
                                .build());

            } else {
//                linearLayout.setVisibility(View.v);
            }
        }
    }

    public static void loadNativeAd1(final Activity context, ViewGroup fbNativeLayout) {
        Log.d("NativeAd", "reest native: " + fb_native_ad);
        FacebookSdk.setIsDebugEnabled(true);
        if (fb_native_ad == null || fb_native_ad.equals("")) {
            firebaseInit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadNativeAd(context);
                }
            }, 2000);
        } else {
            if (isNetworkAvailable(context)) {
                nativeAd = new com.facebook.ads.NativeAd(context, fb_native_ad);
                NativeAdListener nativeAdListener = new NativeAdListener() {

                    @Override
                    public void onMediaDownloaded(Ad ad) {
                        // Native ad finished downloading all assets
                    }


                    @Override
                    public void onError(Ad ad, com.facebook.ads.AdError adError) {
                        NativeAds(context);
                        Log.d("NativeAd", "1111fbnative-onError---" + adError.getErrorMessage());

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        // Native ad is loaded and ready to be displayed
                        Log.d("NativeAd", "1111fbnative-onAdLoaded");

//                        NativeAds(context, admobview);

                        // Native ad is loaded and ready to be displayed
                        if (nativeAd == null || nativeAd != ad) {
                            Log.d("NativeAd", "retttt-onAdLoaded");
                            return;
                        }
                        // Inflate Native Banner Ad into Container
                        inflateAdnative1(nativeAd, context, fbNativeLayout);

                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                        // Native ad clicked
                        Log.d("NativeAd", "Native ad clicked!");
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                        // Native ad impression
                        Log.d("NativeAd", "Native ad impression logged!");
                    }
                };
                nativeAd.loadAd(
                        nativeAd.buildLoadAdConfig()
                                .withAdListener(nativeAdListener)
                                .build());

            } else {
//                linearLayout.setVisibility(View.v);
            }
        }
    }

    public static void inflateAdnative(com.facebook.ads.NativeAd nativeAd, Context context) {
        Log.d("Adshow", "1111fbnative-inflateAdnative");
        NativeAdLayout nativeAdLayout = new NativeAdLayout(context);
        nativeAd.unregisterView();

        // Add the Ad view into the ad container.

        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        adView2 = (LinearLayout) inflater.inflate(R.layout.ph_fb_native, nativeAdLayout, false);
        nativeAdLayout.addView(adView2);


        // Add the AdOptionsView
        LinearLayout adChoicesContainer = adView2.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView2.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView2.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView2.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView2.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView2.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView2.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView2.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView2, nativeAdMedia, nativeAdIcon, clickableViews);
        fbNative = nativeAdLayout;
//        linearLayout.removeAllViews();
//        linearLayout.addView(nativeAdLayout);


    }

    public static void inflateAdnative1(com.facebook.ads.NativeAd nativeAd, Context context, ViewGroup fbNativeLayout) {
        Log.d("Adshow", "1111fbnative-inflateAdnative");
        NativeAdLayout nativeAdLayout = new NativeAdLayout(context);
        nativeAd.unregisterView();

        // Add the Ad view into the ad container.

        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        adView2 = (LinearLayout) inflater.inflate(R.layout.ph_fb_native, nativeAdLayout, false);
        nativeAdLayout.addView(adView2);


        // Add the AdOptionsView
        LinearLayout adChoicesContainer = adView2.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView2.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView2.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView2.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView2.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView2.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView2.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView2.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView2, nativeAdMedia, nativeAdIcon, clickableViews);
        fbNative = nativeAdLayout;
        fbNativeLayout.removeAllViews();
        fbNativeLayout.addView(nativeAdLayout);


    }

    public static void showfbnativ(Activity activity, ViewGroup fbNativeLayout) {
//        if (fbNative != null) {
//            fbNativeLayout.setVisibility(View.VISIBLE);
//            fbNativeLayout.removeView(fbNative);
//            fbNativeLayout.removeAllViews();
//            fbNativeLayout.addView(fbNative);
//            fbNative = null;
//        } else {
        DH_GllobalItem.loadNativeAd1(activity, fbNativeLayout);
//            fbNativeLayout.setVisibility(View.GONE);
//            showfbnativ(activity, fbNativeLayout);
//        }
    }

    public static void showfbnativ_show(Activity activity, ViewGroup fbNativeLayout) {
        if (fbNative != null) {
            fbNativeLayout.setVisibility(View.VISIBLE);
            fbNativeLayout.removeView(fbNative);
            fbNativeLayout.removeAllViews();
            fbNativeLayout.addView(fbNative);
            fbNative = null;
        } else {
            DH_GllobalItem.loadNativeAd(activity);
            fbNativeLayout.setVisibility(View.GONE);
            showfbnativ(activity, fbNativeLayout);
        }
    }

    public static void showFBnative(Activity activity, LinearLayout fbNativeLayout, DH_sm_TemplateView admobNativeLayout) {
        if (fbNative != null) {
            fbNativeLayout.setVisibility(View.VISIBLE);
            admobNativeLayout.setVisibility(View.GONE);
            Log.d("NativeAd", "showfbbbb");
            fbNativeLayout.removeView(fbNative);
            fbNativeLayout.removeAllViews();
            fbNativeLayout.addView(fbNative);
            fbNative = null;
        } else if (admobNative != null) {
            fbNativeLayout.setVisibility(View.GONE);
            admobNativeLayout.setVisibility(View.VISIBLE);
            DH_NativeTemplateStyle styles = new
                    DH_NativeTemplateStyle.Builder().withMainBackgroundColor(null).build();
            admobNativeLayout.setDh_ntem_Styles(styles);
            admobNativeLayout.setDh_nad_NativeAd(admobNative);
            Log.d("NativeAd", "showelse");
        }


    }

    public static void showAdmobnative(DH_sm_TemplateView admobview) {

        if (admobNative != null) {
            DH_NativeTemplateStyle styles = new
                    DH_NativeTemplateStyle.Builder().withMainBackgroundColor(null).build();
            admobview.setDh_ntem_Styles(styles);
            admobview.setDh_nad_NativeAd(admobNative);
            Log.d("NativeAd", "admob add: ");

        } else {
            Log.d("NativeAd", "admob add null: ");
        }
    }


    public static void NativeAds(Activity activity) {

        if (str_Admob_native_id == null || str_Admob_native_id.equals("")) {
            firebaseInit();
            Log.d("InterAds", "Admob inter-id null");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    NativeAds(activity);
                }
            }, 2000);

        } else {

            RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_MA)
                    .build();
            MobileAds.setRequestConfiguration(requestConfiguration);
            AdLoader adLoader = new AdLoader.Builder(activity, str_Admob_native_id)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            if (admobNative != null) {
                                admobNative = null;
                            }
                            admobNative = nativeAd;
                            Log.d("NativeAd", "AdmobNativeAdLoaded1: ");
                        }
                    })

                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            NativeAds2(activity);
                            Log.d("NativeAd", "AdmobNativefail1: " + adError);

                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());

        }
    }

    public static void NativeAds2(Activity activity) {

        if (str_Admob_native_id1 == null || str_Admob_native_id1.equals("")) {
            firebaseInit();
            Log.d("InterAds", "Admob inter-id null");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    NativeAds2(activity);
                }
            }, 2000);

        } else {
            RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_MA)
                    .build();
            MobileAds.setRequestConfiguration(requestConfiguration);
            AdLoader adLoader = new AdLoader.Builder(activity, str_Admob_native_id1)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            if (admobNative != null) {
                                admobNative = null;
                            }
                            admobNative = nativeAd;
                            Log.d("NativeAd", "AdmobNativeload2: ");

                        }
                    })

                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            NativeAds3(activity);
                            Log.d("NativeAd", "AdmobNativefail2: " + adError.getMessage());

                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());

        }
    }

    public static void NativeAds3(Activity activity) {

        if (str_Admob_native_id2 == null || str_Admob_native_id2.equals("")) {
            firebaseInit();
            Log.d("InterAds", "Admob inter-id null");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    NativeAds3(activity);
                }
            }, 2000);

        } else {


            RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_MA)
                    .build();
            MobileAds.setRequestConfiguration(requestConfiguration);
            AdLoader adLoader = new AdLoader.Builder(activity, str_Admob_native_id2)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            if (admobNative != null) {
                                admobNative = null;
                            }
                            admobNative = nativeAd;
                        }
                    })

                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {

                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());

        }
    }

//
//    public static void addNativeAd(Activity activity, sm_TemplateView native_ad) {
//        NativeAd nativeAd = null;
//        if (int_NativeCount == 0) {
//            Log.d("AddNative", "AddNative nativeCount 0");
//            if (nemodl_NativeModel.getNative1() != null) {
//                nativeAd = nemodl_NativeModel.getNative1();
//                Log.d("AddNative", "add view first-1");
//                nemodl_NativeModel.setNative1(null);
//            } else if (nemodl_NativeModel.getNative2() != null) {
//                nativeAd = nemodl_NativeModel.getNative2();
//                Log.d("AddNative", "add view first - 2");
//                nemodl_NativeModel.setNative2(null);
//            } else if (nemodl_NativeModel.getNative3() != null) {
//                nativeAd = nemodl_NativeModel.getNative3();
//                Log.d("AddNative", "add view first -3");
//                nemodl_NativeModel.setNative3(null);
//            }
//            int_NativeCount++;
//            if (nativeAd != null) {
//                Log.d("AddNative", "first nativeAd not null");
//                sm_NativeTemplateStyle styles = new sm_NativeTemplateStyle.Builder().withMainBackgroundColor(null).build();
//                native_ad.setNtem_Styles(styles);
//                native_ad.setNad_NativeAd(nativeAd);
//            } else {
//                Log.d("AddNative", "nativeAd first GONE");
//                native_ad.setVisibility(View.GONE);
//            }
//
//        } else if (int_NativeCount == 1) {
//            Log.d("AddNative", "AddNative nativeCount 1");
//            if (nemodl_NativeModel.getNative2() != null) {
//                nativeAd = nemodl_NativeModel.getNative2();
//                Log.d("AddNative", "add view sec -2");
//                nemodl_NativeModel.setNative2(null);
//            } else if (nemodl_NativeModel.getNative3() != null) {
//                nativeAd = nemodl_NativeModel.getNative3();
//                Log.d("AddNative", "add view sec -3");
//                nemodl_NativeModel.setNative3(null);
//            } else if (nemodl_NativeModel.getNative1() != null) {
//                nativeAd = nemodl_NativeModel.getNative1();
//                nemodl_NativeModel.setNative1(null);
//                Log.d("AddNative", "add view sec -1");
//            }
//            int_NativeCount++;
//            if (nativeAd != null) {
//                sm_NativeTemplateStyle styles = new sm_NativeTemplateStyle.Builder().withMainBackgroundColor(null).build();
//                native_ad.setNtem_Styles(styles);
//                native_ad.setNad_NativeAd(nativeAd);
//            } else {
//                native_ad.setVisibility(View.GONE);
//            }
//        } else if (int_NativeCount == 2) {
//            Log.d("AddNative", "AddNative nativeCount 2");
//            if (nemodl_NativeModel.getNative3() != null) {
//                nativeAd = nemodl_NativeModel.getNative3();
//                Log.d("AddNative", "add view third -3");
//                nemodl_NativeModel.setNative3(null);
//            } else if (nemodl_NativeModel.getNative1() != null) {
//                nativeAd = nemodl_NativeModel.getNative1();
//                nemodl_NativeModel.setNative1(null);
//                Log.d("AddNative", "add view third -1");
//            } else if (nemodl_NativeModel.getNative2() != null) {
//                nativeAd = nemodl_NativeModel.getNative2();
//                Log.d("AddNative", "add view third -2");
//                nemodl_NativeModel.setNative2(null);
//            }
//
//            int_NativeCount = 0;
//            if (nativeAd != null) {
//                Log.d("AddNative", "AddNative 3 not null");
//                sm_NativeTemplateStyle styles = new sm_NativeTemplateStyle.Builder().withMainBackgroundColor(null).build();
//                native_ad.setNtem_Styles(styles);
//                native_ad.setNad_NativeAd(nativeAd);
//            } else {
//                Log.d("AddNative", "AddNative 3  Gone");
//                native_ad.setVisibility(View.GONE);
//            }
//        } else {
//            Log.d("AddNative", "AddNative Gone");
//            native_ad.setVisibility(View.GONE);
//        }
//
//        if (nemodl_NativeModel.getNative1() == null) {
//            loadFirstNativeAd(activity);
//            Log.d("AddNative", "LOAD1");
//        }
//        if (nemodl_NativeModel.getNative2() == null) {
//            loadSecondNativeAd(activity);
//            Log.d("AddNative", "LOAD2");
//        }
//        if (nemodl_NativeModel.getNative3() == null) {
//            loadThirdNativeAd(activity);
//            Log.d("AddNative", "LOAD3");
//        }
////        }
//
//    }
//
//
//    public static void loadFirstNativeAd(Activity activity) {
//        if (str_Admob_native_id == null || str_Admob_native_id.equals("")) {
//            firebaseInit();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    loadFirstNativeAd(activity);
//                }
//            }, 2000);
//        } else {
//            MobileAds.initialize(activity);
//            AdLoader adLoader = new AdLoader.Builder(activity, str_Admob_native_id)
//                    .forNativeAd(nativeAd -> {
////                        if (loadedNativeAds.size() < nativeAdCount) {
//                        nad_LoadedNativeAds.add(nativeAd);
//
//
//                        nemodl_NativeModel.setNative1(nativeAd);
////                            loadFirstNativeAd(activity);
////                        }
//                        Log.d("AddNative", "first load success");
//
//                    })
//
//                    .withAdListener(new AdListener() {
//                        @Override
//                        public void onAdFailedToLoad(LoadAdError adError) {
////                            loadSecondNativeAd(activity);
//                            Log.d("AddNative", "FIRST LOAD FAIL");
//
//                        }
//                    })
//                    .withNativeAdOptions(new NativeAdOptions.Builder()
//                            .build())
//                    .build();
//            adLoader.loadAd(new AdRequest.Builder().build());
//        }
//    }
//
//    public static void loadSecondNativeAd(Activity activity) {
//        if (str_Admob_native_id1 == null || str_Admob_native_id1.equals("")) {
//            firebaseInit();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    loadSecondNativeAd(activity);
//                }
//            }, 2000);
//        } else {
//            MobileAds.initialize(activity);
//            AdLoader adLoader = new AdLoader.Builder(activity, str_Admob_native_id1)
//                    .forNativeAd(nativeAd -> {
//                        nad_LoadedNativeAds.add(nativeAd);
//
//                        nemodl_NativeModel.setNative2(nativeAd);
//                        Log.d("AddNative", "second load success");
//
//                    })
//                    .withAdListener(new AdListener() {
//                        @Override
//                        public void onAdFailedToLoad(LoadAdError adError) {
////                            loadThirdNativeAd(activity);
//                            Log.d("AddNative", "SECOND LOAD FAIL");
//
//                        }
//                    })
//                    .withNativeAdOptions(new NativeAdOptions.Builder()
//                            .build())
//                    .build();
//
//            adLoader.loadAd(new AdRequest.Builder().build());
//        }
//    }
//
//    public static void loadThirdNativeAd(Activity activity) {
//        if (str_Admob_native_id2 == null || str_Admob_native_id2.equals("")) {
//            firebaseInit();
//            new Handler().postDelayed(() -> loadThirdNativeAd(activity), 2000);
//        } else {
//            MobileAds.initialize(activity);
//            AdLoader adLoader = new AdLoader.Builder(activity, str_Admob_native_id2)
//                    .forNativeAd(nativeAd -> {
//                        nad_LoadedNativeAds.add(nativeAd);
//
//                        nemodl_NativeModel.setNative3(nativeAd);
//                        Log.d("AddNative", "third load success");
//                    })
//
//                    .withAdListener(new AdListener() {
//                        @Override
//                        public void onAdFailedToLoad(LoadAdError adError) {
//                            Log.d("AddNative", "third LOAD FAIL");
//                        }
//                    })
//                    .withNativeAdOptions(new NativeAdOptions.Builder()
//                            .build())
//                    .build();
//            adLoader.loadAd(new AdRequest.Builder().build());
//        }
//    }
//
//    public static void reloadNative(Activity activity) {
////        if (nativeModel.getNative1() == null) {
////            loadFirstNativeAd(activity);
////            Log.d("AddNative", "load first");
////        } else if (nativeModel.getNative2() == null) {
////            loadSecondNativeAd(activity);
////            Log.d("AddNative", "load second");
////        } else if (nativeModel.getNative3() == null) {
////            loadThirdNativeAd(activity);
////            Log.d("AddNative", "load third");
////        }
//    }


    public interface BannerCallback {
        void onAdLoad();

        void onAdFail();
    }


}

