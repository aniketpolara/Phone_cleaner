// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.cleanPhone.mobileCleaner.ads;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.cleanPhone.mobileCleaner.R;


public class DH_sm_TemplateView extends FrameLayout {
    private static final String str_MEDIUM_TEMPLATE = "medium_template";
    private static final String str_SMALL_TEMPLATE = "small_template";
    private int dh_int_TemplateType;
    private DH_NativeTemplateStyle dh_ntem_Styles;
    private NativeAd dh_nad_NativeAd;
    private NativeAdView dh_nad_NativeAdView;
    private TextView dh_tv_PrimaryView;
    private TextView dh_tv_SecondaryView;
    private RatingBar dh_rb_RatingBar;
    private TextView dh_tv_TertiaryView;
    private ImageView dh_iv_IconView;
    private MediaView dh_mv_MediaView;
    private Button dh_btn_CallToActionView;
    private ConstraintLayout dh_cl_Background;

    public DH_sm_TemplateView(Context dh_context) {
        super(dh_context);
    }

    public DH_sm_TemplateView(Context dh_context, @Nullable AttributeSet dh_attrs) {
        super(dh_context, dh_attrs);
        initView(dh_context, dh_attrs);
    }

    public DH_sm_TemplateView(Context dh_context, @Nullable AttributeSet dh_attrs, int dh_defStyleAttr) {
        super(dh_context, dh_attrs, dh_defStyleAttr);
        initView(dh_context, dh_attrs);
    }

    public DH_sm_TemplateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    public void setDh_ntem_Styles(DH_NativeTemplateStyle dh_ntem_Styles) {
        this.dh_ntem_Styles = dh_ntem_Styles;
        this.applyStyles();
    }

    public NativeAdView getDh_nad_NativeAdView() {
        return dh_nad_NativeAdView;
    }

    private void applyStyles() {

        Drawable mainBackground = dh_ntem_Styles.getMainBackgroundColor();
        if (mainBackground != null) {
            dh_cl_Background.setBackground(mainBackground);
            if (dh_tv_PrimaryView != null) {
                dh_tv_PrimaryView.setBackground(mainBackground);
            }
            if (dh_tv_SecondaryView != null) {
                dh_tv_SecondaryView.setBackground(mainBackground);
            }
            if (dh_tv_TertiaryView != null) {
                dh_tv_TertiaryView.setBackground(mainBackground);
            }
        }

        Typeface dh_primary = dh_ntem_Styles.getPrimaryTextTypeface();
        if (dh_primary != null && dh_tv_PrimaryView != null) {
            dh_tv_PrimaryView.setTypeface(dh_primary);
        }

        Typeface dh_secondary = dh_ntem_Styles.getSecondaryTextTypeface();
        if (dh_secondary != null && dh_tv_SecondaryView != null) {
            dh_tv_SecondaryView.setTypeface(dh_secondary);
        }

        Typeface dh_tertiary = dh_ntem_Styles.getTertiaryTextTypeface();
        if (dh_tertiary != null && dh_tv_TertiaryView != null) {
            dh_tv_TertiaryView.setTypeface(dh_tertiary);
        }

        Typeface dh_ctaTypeface = dh_ntem_Styles.getCallToActionTextTypeface();
        if (dh_ctaTypeface != null && dh_btn_CallToActionView != null) {
            dh_btn_CallToActionView.setTypeface(dh_ctaTypeface);
        }

        int dh_primaryTypefaceColor = dh_ntem_Styles.getPrimaryTextTypefaceColor();
        if (dh_primaryTypefaceColor > 0 && dh_tv_PrimaryView != null) {
            dh_tv_PrimaryView.setTextColor(dh_primaryTypefaceColor);
        }

        int dh_secondaryTypefaceColor = dh_ntem_Styles.getSecondaryTextTypefaceColor();
        if (dh_secondaryTypefaceColor > 0 && dh_tv_SecondaryView != null) {
//            secondaryView.setTextColor(secondaryTypefaceColor);
        }

        int dh_tertiaryTypefaceColor = dh_ntem_Styles.getTertiaryTextTypefaceColor();
        if (dh_tertiaryTypefaceColor > 0 && dh_tv_TertiaryView != null) {
            dh_tv_TertiaryView.setTextColor(dh_tertiaryTypefaceColor);
        }

        int dh_ctaTypefaceColor = dh_ntem_Styles.getCallToActionTypefaceColor();
        if (dh_ctaTypefaceColor > 0 && dh_btn_CallToActionView != null) {
            dh_btn_CallToActionView.setTextColor(dh_ctaTypefaceColor);
        }

        float dh_ctaTextSize = dh_ntem_Styles.getCallToActionTextSize();
        if (dh_ctaTextSize > 0 && dh_btn_CallToActionView != null) {
            dh_btn_CallToActionView.setTextSize(dh_ctaTextSize);
        }

        float dh_primaryTextSize = dh_ntem_Styles.getPrimaryTextSize();
        if (dh_primaryTextSize > 0 && dh_tv_PrimaryView != null) {
            dh_tv_PrimaryView.setTextSize(dh_primaryTextSize);
        }

        float dh_secondaryTextSize = dh_ntem_Styles.getSecondaryTextSize();
        if (dh_secondaryTextSize > 0 && dh_tv_SecondaryView != null) {
            Log.d("checktext", "textset");
            dh_tv_SecondaryView.setTextSize(dh_secondaryTextSize);
            Log.d("checktext", "textset" + dh_tv_SecondaryView.getText().toString());
        }

        float dh_tertiaryTextSize = dh_ntem_Styles.getTertiaryTextSize();
        if (dh_tertiaryTextSize > 0 && dh_tv_TertiaryView != null) {
            dh_tv_TertiaryView.setTextSize(dh_tertiaryTextSize);
        }

        Drawable dh_ctaBackground = dh_ntem_Styles.getCallToActionBackgroundColor();
        if (dh_ctaBackground != null && dh_btn_CallToActionView != null) {
            dh_btn_CallToActionView.setBackground(dh_ctaBackground);
        }

        Drawable dh_primaryBackground = dh_ntem_Styles.getPrimaryTextBackgroundColor();
        if (dh_primaryBackground != null && dh_tv_PrimaryView != null) {
            dh_tv_PrimaryView.setBackground(dh_primaryBackground);
        }

        Drawable dh_secondaryBackground = dh_ntem_Styles.getSecondaryTextBackgroundColor();
        if (dh_secondaryBackground != null && dh_tv_SecondaryView != null) {
            dh_tv_SecondaryView.setBackground(dh_secondaryBackground);
        }

        Drawable dh_tertiaryBackground = dh_ntem_Styles.getTertiaryTextBackgroundColor();
        if (dh_tertiaryBackground != null && dh_tv_TertiaryView != null) {
            dh_tv_TertiaryView.setBackground(dh_tertiaryBackground);
        }

        invalidate();
        requestLayout();
    }

    private boolean adHasOnlyStore(NativeAd dh_nativeAd) {
        String dh_store = dh_nativeAd.getStore();
        String dh_advertiser = dh_nativeAd.getAdvertiser();
        return !TextUtils.isEmpty(dh_store) && TextUtils.isEmpty(dh_advertiser);
    }

    public void setDh_nad_NativeAd(NativeAd dh_nad_NativeAd) {
        this.dh_nad_NativeAd = dh_nad_NativeAd;

        String dh_store = dh_nad_NativeAd.getStore();
        String dh_advertiser = dh_nad_NativeAd.getAdvertiser();
        String dh_headline = dh_nad_NativeAd.getHeadline();
        String dh_body = dh_nad_NativeAd.getBody();
        String dh_cta = dh_nad_NativeAd.getCallToAction();
        Double dh_starRating = dh_nad_NativeAd.getStarRating();
        NativeAd.Image dh_icon = dh_nad_NativeAd.getIcon();

        String dh_secondaryText;

        dh_nad_NativeAdView.setCallToActionView(dh_btn_CallToActionView);
        dh_nad_NativeAdView.setHeadlineView(dh_tv_PrimaryView);
        dh_nad_NativeAdView.setMediaView(dh_mv_MediaView);
        dh_tv_SecondaryView.setVisibility(VISIBLE);
        if (adHasOnlyStore(dh_nad_NativeAd)) {
            dh_nad_NativeAdView.setStoreView(dh_tv_SecondaryView);
            dh_secondaryText = dh_store;
        } else if (!TextUtils.isEmpty(dh_advertiser)) {
            dh_nad_NativeAdView.setAdvertiserView(dh_tv_SecondaryView);
            dh_secondaryText = dh_advertiser;
        } else {
            dh_secondaryText = "";
        }

        dh_tv_PrimaryView.setText(dh_headline);
        dh_btn_CallToActionView.setText(dh_cta);

        //  Set the secondary view to be the star rating if available.
        if (dh_starRating != null && dh_starRating > 0) {
            dh_tv_SecondaryView.setVisibility(GONE);
//      ratingBar.setVisibility(VISIBLE);
//            dh_rb_RatingBar.setRating(dh_starRating.floatValue());

//            dh_nad_NativeAdView.setStarRatingView(dh_rb_RatingBar);
        } else {
            dh_tv_SecondaryView.setText(dh_secondaryText);
            dh_tv_SecondaryView.setVisibility(VISIBLE);
            dh_rb_RatingBar.setVisibility(GONE);
        }

        if (dh_icon != null) {
            dh_iv_IconView.setVisibility(VISIBLE);
            dh_iv_IconView.setImageDrawable(dh_icon.getDrawable());
        } else {
            dh_iv_IconView.setVisibility(GONE);
        }

        if (dh_tv_TertiaryView != null) {
            dh_tv_TertiaryView.setText(dh_body);
            dh_nad_NativeAdView.setBodyView(dh_tv_TertiaryView);
        }

        dh_nad_NativeAdView.setNativeAd(dh_nad_NativeAd);
    }

    public void destroyNativeAd() {
        dh_nad_NativeAd.destroy();
    }

    public String getTemplateTypeName() {
        if (dh_int_TemplateType == R.layout.ph_gnt_medium_template_view) {
            return str_MEDIUM_TEMPLATE;
        } else if (dh_int_TemplateType == R.layout.ph_gnt_small_template_view) {
            return str_SMALL_TEMPLATE;
        }
        return "";
    }

    private void initView(Context dh_context, AttributeSet dh_attributeSet) {

        TypedArray dh_attributes =
                dh_context.getTheme().obtainStyledAttributes(dh_attributeSet, R.styleable.TemplateView, 0, 0);

        try {
            dh_int_TemplateType =
                    dh_attributes.getResourceId(
                            R.styleable.TemplateView_gnt_template_type, R.layout.ph_gnt_medium_template_view);
        } finally {
            dh_attributes.recycle();
        }
        LayoutInflater dh_inflater =
                (LayoutInflater) dh_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dh_inflater.inflate(dh_int_TemplateType, this);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        dh_nad_NativeAdView = (NativeAdView) findViewById(R.id.dh_nad_NativeAdView);
        dh_tv_PrimaryView = (TextView) findViewById(R.id.dh_tv_PrimaryView);
        dh_tv_SecondaryView = (TextView) findViewById(R.id.dh_tv_SecondaryView);
        dh_tv_TertiaryView = (TextView) findViewById(R.id.dh_tv_TertiaryView);

//        dh_rb_RatingBar = (RatingBar) findViewById(R.id.dh_rb_RatingBar);
//        dh_rb_RatingBar.setEnabled(false);

        dh_btn_CallToActionView = (Button) findViewById(R.id.dh_btn_CallToActionView);
        dh_iv_IconView = (ImageView) findViewById(R.id.dh_iv_IconView);
        dh_mv_MediaView = (MediaView) findViewById(R.id.dh_mv_MediaView);
        dh_cl_Background = (ConstraintLayout) findViewById(R.id.dh_cl_Background);
    }
}
