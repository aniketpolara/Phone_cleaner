package com.cleanPhone.mobileCleaner.socialmedia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.SocialAnimationActivity;
import com.cleanPhone.mobileCleaner.listadapt.ImagesViewAdapter;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SimpleSectionedListAdapter;
import com.cleanPhone.mobileCleaner.utility.SocialGridView;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppJunk;
import com.cleanPhone.mobileCleaner.wrappers.BigSizeFilesWrapper;

import java.util.ArrayList;

public class GIFFragment extends Fragment {
    public View V;
    private ImagesViewAdapter adapter;
    private ArrayList<BigSizeFilesWrapper> dataList;
    private int mediaApp;
    private ArrayList<AppJunk> socialApp;
    private TextView tvCount;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.V = layoutInflater.inflate(R.layout.fragment_four, viewGroup, false);
        if (getActivity() != null) {
            this.mediaApp = getActivity().getIntent().getIntExtra(FirebaseAnalytics.Param.INDEX, 0);
        }
        this.tvCount = (TextView) this.V.findViewById(R.id.tv_social_count);
        ((TextView) this.V.findViewById(R.id.tv_social_type)).setText("" + getString(R.string.gifs));
        ArrayList<AppJunk> arrayList = MobiClean.getInstance().mediaAppModule.socialApp;
        this.socialApp = arrayList;
        ArrayList<BigSizeFilesWrapper> arrayList2 = arrayList.get(this.mediaApp).mediaJunkData.get(4).dataList;
        this.dataList = arrayList2;
        if (arrayList2.size() == 0) {
            this.V.findViewById(R.id.hidden_view).setVisibility(View.VISIBLE);
            this.V.findViewById(R.id.parent_fragment).setVisibility(View.GONE);
        } else {
            TextView textView = this.tvCount;
            textView.setText(this.socialApp.get(this.mediaApp).mediaJunkData.get(4).totSelectedCount + " / " + this.dataList.size());
            this.adapter = new ImagesViewAdapter(getActivity(), this.dataList, SocialAnimationActivity.FileTypes.GIF.ordinal(), (CheckBox) this.V.findViewById(R.id.checkAll_gif), (TextView) this.V.findViewById(R.id.tv_social_count));
            final CheckBox checkBox = (CheckBox) this.V.findViewById(R.id.checkAll_gif);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppJunk appJunk = MobiClean.getInstance().mediaAppModule.socialApp.get(GIFFragment.this.mediaApp);
                    if (checkBox.isChecked()) {
                        appJunk.mediaJunkData.get(4).selectAll();
                        appJunk.selectAll();
                        checkBox.setChecked(true);
                    } else {
                        appJunk.mediaJunkData.get(4).unselectAll();
                        appJunk.unselectAll();
                        checkBox.setChecked(false);
                    }
                    GIFFragment.this.adapter.notifyDataSetChanged();
                    TextView textView2 = GIFFragment.this.tvCount;
                    textView2.setText(((AppJunk) GIFFragment.this.socialApp.get(GIFFragment.this.mediaApp)).mediaJunkData.get(4).totSelectedCount + " / " + GIFFragment.this.dataList.size());
                    ((TextView) GIFFragment.this.getActivity().findViewById(R.id.clean_now_text)).setText("" + GIFFragment.this.getActivity().getString(R.string.mbc_clean) + " " + Util.convertBytes(((AppJunk) GIFFragment.this.socialApp.get(GIFFragment.this.mediaApp)).selectedSize));
                }
            });
            ((SocialGridView) this.V.findViewById(R.id.rv_gifadapter)).setAdapter((ListAdapter) new SimpleSectionedListAdapter(getActivity(), this.adapter, R.layout.list_item_header, R.id.header));
        }
        return this.V;
    }

    @Override
    public void onResume() {
        String string;
        super.onResume();
        try {
            ImagesViewAdapter imagesViewAdapter = this.adapter;
            if (imagesViewAdapter == null || !GlobalData.returnedAfterSocialDeletion) {
                return;
            }
            imagesViewAdapter.notifyDataSetChanged();
            this.tvCount.setText(this.socialApp.get(this.mediaApp).mediaJunkData.get(4).totSelectedCount + " / " + this.dataList.size());
            String convertBytes = Util.convertBytes(this.socialApp.get(this.mediaApp).selectedSize);
            if (this.socialApp.get(this.mediaApp).selectedSize > 0) {
                string = getActivity().getString(R.string.mbc_clean) + " " + convertBytes;
            } else {
                string = getActivity().getString(R.string.mbc_clean);
            }
            ((TextView) getActivity().findViewById(R.id.clean_now_text)).setText(string);
            this.socialApp.get(GlobalData.APP_INDEX).refresh();
            ((TextView) getActivity().findViewById(R.id.junkdisplay_sizetv)).setText(Util.convertBytes(this.socialApp.get(GlobalData.APP_INDEX).appJunkSize));
            GlobalData.returnedAfterSocialDeletion = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
