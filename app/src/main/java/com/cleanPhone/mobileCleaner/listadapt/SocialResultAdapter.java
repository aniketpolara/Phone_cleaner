package com.cleanPhone.mobileCleaner.listadapt;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.socialmedia.SocialMediaFilesTabScreen;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppJunk;


import java.util.ArrayList;


public class SocialResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private final Context context;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;

    private ArrayList<AppJunk> dataList;
    private static final int LAYOUT_ONE = 0;
    private static final int LAYOUT_TWO = 1;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View iv_divider_shadow;
        private LinearLayout ll_social;
        private TextView size;
        private TextView title;

        public MyViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.tvsocial_app_name);
            this.size = (TextView) view.findViewById(R.id.tvsocial_app_size);
            this.ll_social = (LinearLayout) view.findViewById(R.id.ll_social);
        }
    }

    public SocialResultAdapter(Context context, ArrayList arrayList) {
        Util.appendLogmobiclean("SocialResultAdapter", "construtor", GlobalData.FILE_NAME);
        this.dataList = arrayList;
        this.context = context;
    }


    public int getPosition(String str) {
        Util.appendLogmobiclean("SocialResultAdapter", "getPosition()", GlobalData.FILE_NAME);
        String lowerCase = str.toLowerCase();
        lowerCase.hashCode();
        char c2 = 65535;
        switch (lowerCase.hashCode()) {
            case -1436108013:
                if (lowerCase.equals("messenger")) {
                    c2 = 0;
                    break;
                }
                break;
            case -916346253:
                if (lowerCase.equals("twitter")) {
                    c2 = 1;
                    break;
                }
                break;
            case 28903346:
                if (lowerCase.equals("instagram")) {
                    c2 = 2;
                    break;
                }
                break;
            case 109512406:
                if (lowerCase.equals("skype")) {
                    c2 = 3;
                    break;
                }
                break;
            case 497130182:
                if (lowerCase.equals("DEFAULT_GRAPH_DOMAIN")) {
                    c2 = 4;
                    break;
                }
                break;
            case 1934780818:
                if (lowerCase.equals("whatsapp")) {
                    c2 = 5;
                    break;
                }
                break;
        }
        switch (c2) {
            case 0:
                return 5;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 1;
            case 5:
            default:
                return 0;
        }
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            final AppJunk appJunk = this.dataList.get(i);
            TextView textView = myViewHolder.title;
            textView.setText("" + appJunk.appName);
            TextView textView2 = myViewHolder.size;
            textView2.setText("" + Util.convertBytes(appJunk.appJunkSize));

            myViewHolder.ll_social.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.appendLogmobiclean("SocialResultAdapter", "ll_social click", GlobalData.FILE_NAME);
                    GlobalData.APP_INDEX = SocialResultAdapter.this.getPosition(appJunk.appName);
                    Intent intent = new Intent(SocialResultAdapter.this.context, SocialMediaFilesTabScreen.class);
                    intent.putExtra(FirebaseAnalytics.Param.INDEX, GlobalData.APP_INDEX);
                    intent.putExtra("NAME", "" + appJunk.appName);
                    intent.putExtra("SIZE", appJunk.appJunkSize);
                    intent.putExtra(GlobalData.REDIRECTNOTI, ((AppCompatActivity) SocialResultAdapter.this.context).getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false));
                    intent.putExtra("FROMHOME", ((AppCompatActivity) SocialResultAdapter.this.context).getIntent().getBooleanExtra("FROMHOME", false));

                        SocialResultAdapter.this.context.startActivity(intent);

                }
            });
            if (i == getItemCount() - 1) {
                myViewHolder.ll_social.setBackground(null);
            }


    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;


        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.social_app_adapter, parent, false);
        viewHolder = new MyViewHolder(view);


        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(position%4==4)
            return LAYOUT_ONE;
        else
            return LAYOUT_TWO;
    }

}
