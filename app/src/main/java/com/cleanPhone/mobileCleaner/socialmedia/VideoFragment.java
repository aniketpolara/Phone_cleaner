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

public class VideoFragment extends Fragment {
    public View V;
    private ImagesViewAdapter adapter;
    private ArrayList<BigSizeFilesWrapper> dataList;
    private int mediaApp;
    private TextView tvCount;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.V = layoutInflater.inflate(R.layout.fragment_two, viewGroup, false);
        if (getActivity() != null) {
            this.mediaApp = getActivity().getIntent().getIntExtra(FirebaseAnalytics.Param.INDEX, 0);
        }
        this.tvCount = (TextView) this.V.findViewById(R.id.tv_social_count);
        ((TextView) this.V.findViewById(R.id.tv_social_type)).setText("" + getActivity().getResources().getString(R.string.mbc_viewmore_video));
        final ArrayList<AppJunk> arrayList = MobiClean.getInstance().mediaAppModule.socialApp;
        this.dataList = MobiClean.getInstance().mediaAppModule.socialApp.get(this.mediaApp).mediaJunkData.get(1).dataList;
        TextView textView = this.tvCount;
        textView.setText(MobiClean.getInstance().mediaAppModule.socialApp.get(this.mediaApp).mediaJunkData.get(1).totSelectedCount + " / " + this.dataList.size());
        if (this.dataList.size() == 0) {
            this.V.findViewById(R.id.hidden_view).setVisibility(View.VISIBLE);
            this.V.findViewById(R.id.parent_fragment).setVisibility(View.GONE);
        } else {
            this.adapter = new ImagesViewAdapter(getActivity(), this.dataList, SocialAnimationActivity.FileTypes.Video.ordinal(), (CheckBox) this.V.findViewById(R.id.checkAll_vid), this.tvCount);
            ((SocialGridView) this.V.findViewById(R.id.rv_vidadapter)).setAdapter((ListAdapter) new SimpleSectionedListAdapter(getActivity(), this.adapter, R.layout.list_item_header, R.id.header));
            final CheckBox checkBox = (CheckBox) this.V.findViewById(R.id.checkAll_vid);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppJunk appJunk = MobiClean.getInstance().mediaAppModule.socialApp.get(VideoFragment.this.mediaApp);
                    if (checkBox.isChecked()) {
                        appJunk.mediaJunkData.get(1).selectAll();
                        appJunk.selectAll();
                        checkBox.setChecked(true);
                    } else {
                        appJunk.mediaJunkData.get(1).unselectAll();
                        appJunk.unselectAll();
                        checkBox.setChecked(false);
                    }
                    VideoFragment.this.adapter.notifyDataSetChanged();
                    TextView textView2 = VideoFragment.this.tvCount;
                    textView2.setText(((AppJunk) arrayList.get(VideoFragment.this.mediaApp)).mediaJunkData.get(1).totSelectedCount + " / " + VideoFragment.this.dataList.size());
                    ((TextView) VideoFragment.this.getActivity().findViewById(R.id.clean_now_text)).setText("" + VideoFragment.this.getActivity().getString(R.string.mbc_clean) + " " + Util.convertBytes(((AppJunk) arrayList.get(VideoFragment.this.mediaApp)).selectedSize));
                }
            });
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
            this.tvCount.setText(MobiClean.getInstance().mediaAppModule.socialApp.get(this.mediaApp).mediaJunkData.get(1).totSelectedCount + " / " + this.dataList.size());
            String convertBytes = Util.convertBytes(MobiClean.getInstance().mediaAppModule.socialApp.get(this.mediaApp).selectedSize);
            if (MobiClean.getInstance().mediaAppModule.socialApp.get(this.mediaApp).selectedSize > 0) {
                string = getActivity().getString(R.string.mbc_clean) + " " + convertBytes;
            } else {
                string = getActivity().getString(R.string.mbc_clean);
            }
            ((TextView) getActivity().findViewById(R.id.clean_now_text)).setText(string);
            MobiClean.getInstance().mediaAppModule.socialApp.get(GlobalData.APP_INDEX).refresh();
            long j = MobiClean.getInstance().mediaAppModule.socialApp.get(GlobalData.APP_INDEX).appJunkSize;
            ((TextView) getActivity().findViewById(R.id.junkdisplay_sizetv)).setText(Util.convertBytes(j));
            GlobalData.returnedAfterSocialDeletion = false;
            if (j == 0) {
                getActivity().finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
