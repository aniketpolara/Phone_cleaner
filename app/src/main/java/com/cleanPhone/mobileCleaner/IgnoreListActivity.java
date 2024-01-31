package com.cleanPhone.mobileCleaner;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.listadapt.WhiteListAdapterView;
import com.cleanPhone.mobileCleaner.wrappers.AppDetails;
import com.cleanPhone.mobileCleaner.wrappers.PackageInfoStruct;

import java.util.ArrayList;


public class IgnoreListActivity extends ParentActivity {
    private WhiteListAdapterView mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mediaNameRecyclerView;
    public ImageView back;
    public SpinKitView spinKitView;
    int admob = 3;

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_ignore_list_screen);

        back = findViewById(R.id.iv_back);
        spinKitView = findViewById(R.id.pbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        showInterstialAds(IgnoreListActivity.this);

        LinearLayout llBanner = findViewById(R.id.llBanner);
        DH_GllobalItem.fbAdaptiveBanner(this, llBanner, new DH_GllobalItem.BannerCallback() {
            @Override
            public void onAdLoad() {
                llBanner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFail() {
                llBanner.setVisibility(View.GONE);
            }
        });


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        back = findViewById(R.id.iv_back);
        spinKitView = findViewById(R.id.pbar);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sendAnalytics("SCREEN_VISIT_" + getClass().getSimpleName());

        new AsyncTask<String, String, ArrayList<PackageInfoStruct>>() {
            @Override
            public void onPreExecute() {
                super.onPreExecute();
                spinKitView.setVisibility(View.VISIBLE);
            }

            @Override
            public ArrayList<PackageInfoStruct> doInBackground(String... strArr) {
                return new AppDetails(IgnoreListActivity.this).getIgnoredApps(IgnoreListActivity.this);
            }

            @Override
            public void onPostExecute(ArrayList<PackageInfoStruct> arrayList) {
                super.onPostExecute(arrayList);
                try {
                    IgnoreListActivity ignoreListActivity = IgnoreListActivity.this;
                    ignoreListActivity.mAdapter = new WhiteListAdapterView(ignoreListActivity, arrayList);
                    IgnoreListActivity ignoreListActivity2 = IgnoreListActivity.this;
                    ignoreListActivity2.mLayoutManager = new LinearLayoutManager(ignoreListActivity2.getApplicationContext());
                    IgnoreListActivity ignoreListActivity3 = IgnoreListActivity.this;
                    ignoreListActivity3.mediaNameRecyclerView = (RecyclerView) ignoreListActivity3.findViewById(R.id.recycler_view);
                    IgnoreListActivity.this.mediaNameRecyclerView.setLayoutManager(IgnoreListActivity.this.mLayoutManager);
                    IgnoreListActivity.this.mediaNameRecyclerView.setAdapter(IgnoreListActivity.this.mAdapter);
                    IgnoreListActivity.this.mediaNameRecyclerView.addItemDecoration(new DividerItemDecoration(IgnoreListActivity.this.mediaNameRecyclerView.getContext(), 1));
                    spinKitView.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[0]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ignore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.already_purchased) {

            final Dialog dialog = new Dialog(this);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
            }
            dialog.setContentView(R.layout.new_dialog_junk_cancel);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setGravity(17);
            View space;

             dialog.findViewById(R.id.dialog_img).setVisibility(View.GONE);
            dialog.findViewById(R.id.dialog_title).setVisibility(View.GONE);
            ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(getString(R.string.exclude_apps_string));
            dialog.findViewById(R.id.ll_yes_no).setVisibility(View.GONE);
            dialog.findViewById(R.id.ll_ok).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.ll_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (IgnoreListActivity.this.multipleClicked()) {
                        return;
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();

            return true;
        }
        finish();
        return super.onOptionsItemSelected(menuItem);
    }
}
