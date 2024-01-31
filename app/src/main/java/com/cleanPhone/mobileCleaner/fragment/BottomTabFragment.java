package com.cleanPhone.mobileCleaner.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;


public class BottomTabFragment extends ParentFragment {
    public Activity V;
    private Fragment fragment;
    private View view;
    public String tab_value = "";
    private String TAG = "BottomTabFragment";
    int admob = 3;


    public void currentLayout(String str, Activity activity) {
        this.V = activity;
        this.tab_value = str;
        int parseInt = Integer.parseInt(str);
        if (parseInt == 1) {
            Util.isHome = true;
            HomeActivity.title.setText(this.V.getResources().getString(R.string.mbc_app_name));
            ((TextView) this.view.findViewById(R.id.tv_home)).setTextColor(this.V.getResources().getColor(R.color.text_color));
            ((ImageView) this.view.findViewById(R.id.iv_home)).setVisibility(View.GONE);
            ((ImageView) this.view.findViewById(R.id.iv_home_fill)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_security)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_security_fill)).setVisibility(View.GONE);
            ((ImageView) this.view.findViewById(R.id.iv_more_space)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_more_fill)).setVisibility(View.GONE);
            ((ImageView) this.view.findViewById(R.id.iv_tools)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_tool_fill)).setVisibility(View.GONE);
            ((TextView) this.view.findViewById(R.id.tv_security)).setTextColor(this.V.getResources().getColor(R.color.grayseperator_text));
            ((TextView) this.view.findViewById(R.id.tv_freeup)).setTextColor(this.V.getResources().getColor(R.color.grayseperator_text));
            ((TextView) this.view.findViewById(R.id.tv_toolbox)).setTextColor(this.V.getResources().getColor(R.color.grayseperator_text));
            this.view.findViewById(R.id.linear_security).setOnClickListener(this);
            this.view.findViewById(R.id.linear_freeup).setOnClickListener(this);
            this.view.findViewById(R.id.linear_toolbox).setOnClickListener(this);
            this.fragment = new HomeFragment();
            FragmentTransaction beginTransaction = ((ParentActivity) this.V).getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(R.id.frame_tab, this.fragment, "HomeFragmentTag");
            beginTransaction.commit();
        } else if (parseInt == 2) {
            Util.isHome = false;
            HomeActivity.title.setText(this.V.getResources().getText(R.string.mbc_samllsecurity));
            ((ImageView) this.view.findViewById(R.id.iv_security)).setVisibility(View.GONE);
            ((ImageView) this.view.findViewById(R.id.iv_security_fill)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_home)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_home_fill)).setVisibility(View.GONE);
            ((ImageView) this.view.findViewById(R.id.iv_more_space)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_more_fill)).setVisibility(View.GONE);
            ((ImageView) this.view.findViewById(R.id.iv_tools)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_tool_fill)).setVisibility(View.GONE);
            ((TextView) this.view.findViewById(R.id.tv_home)).setTextColor(this.V.getResources().getColor(R.color.grayseperator_text));
            ((TextView) this.view.findViewById(R.id.tv_security)).setTextColor(this.V.getResources().getColor(R.color.text_color));
            ((TextView) this.view.findViewById(R.id.tv_freeup)).setTextColor(this.V.getResources().getColor(R.color.grayseperator_text));
            ((TextView) this.view.findViewById(R.id.tv_toolbox)).setTextColor(this.V.getResources().getColor(R.color.grayseperator_text));
            this.view.findViewById(R.id.linear_home).setOnClickListener(this);
            this.view.findViewById(R.id.linear_freeup).setOnClickListener(this);
            this.view.findViewById(R.id.linear_toolbox).setOnClickListener(this);
            String name = this.fragment.getClass().getName();
            this.fragment = new ApplicatonUsageFragmentView();
            FragmentTransaction beginTransaction2 = ((ParentActivity) this.V).getSupportFragmentManager().beginTransaction();
            beginTransaction2.addToBackStack(name);
            beginTransaction2.replace(R.id.frame_tab, this.fragment);
            beginTransaction2.commit();
        } else if (parseInt == 3) {
            Util.isHome = false;
            HomeActivity.title.setText(this.V.getResources().getString(R.string.mbc_morespacesmall));
            ((ImageView) this.view.findViewById(R.id.iv_more_space)).setVisibility(View.GONE);
            ((ImageView) this.view.findViewById(R.id.iv_more_fill)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_home)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_home_fill)).setVisibility(View.GONE);
            ((ImageView) this.view.findViewById(R.id.iv_security)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_security_fill)).setVisibility(View.GONE);
            ((ImageView) this.view.findViewById(R.id.iv_tools)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_tool_fill)).setVisibility(View.GONE);
            ((TextView) this.view.findViewById(R.id.tv_home)).setTextColor(this.V.getResources().getColor(R.color.grayseperator_text));
            ((TextView) this.view.findViewById(R.id.tv_security)).setTextColor(this.V.getResources().getColor(R.color.grayseperator_text));
            ((TextView) this.view.findViewById(R.id.tv_freeup)).setTextColor(this.V.getResources().getColor(R.color.text_color));
            ((TextView) this.view.findViewById(R.id.tv_toolbox)).setTextColor(this.V.getResources().getColor(R.color.grayseperator_text));
            this.view.findViewById(R.id.linear_home).setOnClickListener(this);
            this.view.findViewById(R.id.linear_security).setOnClickListener(this);
            this.view.findViewById(R.id.linear_toolbox).setOnClickListener(this);
            this.fragment = new FreeupSpaceFragmentParent();
            FragmentTransaction beginTransaction3 = ((ParentActivity) this.V).getSupportFragmentManager().beginTransaction();
            beginTransaction3.replace(R.id.frame_tab, this.fragment);
            beginTransaction3.commit();
        } else if (parseInt != 4) {
        } else {
            HomeActivity.title.setText(this.V.getResources().getString(R.string.mbc_smalltools));
            ((ImageView) this.view.findViewById(R.id.iv_tools)).setVisibility(View.GONE);
            ((ImageView) this.view.findViewById(R.id.iv_tool_fill)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_home)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_home_fill)).setVisibility(View.GONE);
            ((ImageView) this.view.findViewById(R.id.iv_security)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_security_fill)).setVisibility(View.GONE);
            ((ImageView) this.view.findViewById(R.id.iv_more_space)).setVisibility(View.VISIBLE);
            ((ImageView) this.view.findViewById(R.id.iv_more_fill)).setVisibility(View.GONE);
            ((TextView) this.view.findViewById(R.id.tv_home)).setTextColor(this.V.getResources().getColor(R.color.grayseperator_text));
            ((TextView) this.view.findViewById(R.id.tv_security)).setTextColor(this.V.getResources().getColor(R.color.grayseperator_text));
            ((TextView) this.view.findViewById(R.id.tv_freeup)).setTextColor(this.V.getResources().getColor(R.color.grayseperator_text));
            ((TextView) this.view.findViewById(R.id.tv_toolbox)).setTextColor(this.V.getResources().getColor(R.color.text_color));
            this.view.findViewById(R.id.linear_home).setOnClickListener(this);
            this.view.findViewById(R.id.linear_security).setOnClickListener(this);
            this.view.findViewById(R.id.linear_freeup).setOnClickListener(this);
            this.fragment = new ToolsFragmentView();
            FragmentTransaction beginTransaction4 = ((ParentActivity) this.V).getSupportFragmentManager().beginTransaction();
            beginTransaction4.replace(R.id.frame_tab, this.fragment);
            beginTransaction4.commit();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_freeup:
                if (this.tab_value.equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_3D)) {
                    return;
                }
                currentLayout(ExifInterface.GPS_MEASUREMENT_3D, this.V);
                return;
            case R.id.linear_home:
                if (this.tab_value.equalsIgnoreCase("1")) {
                    return;
                }
                currentLayout("1", this.V);
                return;
            case R.id.linear_mid_layer:
            case R.id.linear_tool:
            default:
                return;
            case R.id.linear_security:
                if (this.tab_value.equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
                    return;
                }
                currentLayout(ExifInterface.GPS_MEASUREMENT_2D, this.V);
                return;
            case R.id.linear_toolbox:
                if (this.tab_value.equalsIgnoreCase("4")) {
                    return;
                }
                currentLayout("4", this.V);
                return;
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_bottom_tab, viewGroup, false);
        this.V = getActivity();
        this.view.findViewById(R.id.linear_home).setOnClickListener(this);
        this.view.findViewById(R.id.linear_security).setOnClickListener(this);
        this.view.findViewById(R.id.linear_freeup).setOnClickListener(this);
        this.view.findViewById(R.id.linear_toolbox).setOnClickListener(this);
        if (getArguments() != null) {
            currentLayout("" + getArguments().getString("tab_frag"), this.V);
        }
        return this.view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
