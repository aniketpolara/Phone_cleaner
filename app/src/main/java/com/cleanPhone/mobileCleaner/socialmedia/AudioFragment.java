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
import com.cleanPhone.mobileCleaner.listadapt.FileViewAdapter;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SimpleSectionedListAdapter;
import com.cleanPhone.mobileCleaner.utility.SocialGridView;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppJunk;
import com.cleanPhone.mobileCleaner.wrappers.BigSizeFilesWrapper;

import java.util.ArrayList;

public class AudioFragment extends Fragment {
    public View V;
    private FileViewAdapter adapter;
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
        View inflate = layoutInflater.inflate(R.layout.fragment_three, viewGroup, false);
        this.V = inflate;
        final CheckBox checkBox = (CheckBox) inflate.findViewById(R.id.checkAll_aud);
        if (getActivity() != null) {
            this.mediaApp = getActivity().getIntent().getIntExtra(FirebaseAnalytics.Param.INDEX, 0);
        }
        this.tvCount = (TextView) this.V.findViewById(R.id.tv_social_count);
        ((TextView) this.V.findViewById(R.id.tv_social_type)).setText("" + getActivity().getResources().getString(R.string.mbc_viewmore_audio));
        ArrayList<AppJunk> arrayList = MobiClean.getInstance().mediaAppModule.socialApp;
        this.socialApp = arrayList;
        ArrayList<BigSizeFilesWrapper> arrayList2 = arrayList.get(this.mediaApp).mediaJunkData.get(2).dataList;
        this.dataList = arrayList2;
        if (arrayList2.size() == 0) {
            this.V.findViewById(R.id.hidden_view).setVisibility(View.VISIBLE);
            this.V.findViewById(R.id.parent_fragment).setVisibility(View.GONE);
        } else {
            TextView textView = this.tvCount;
            textView.setText(this.socialApp.get(this.mediaApp).mediaJunkData.get(2).totSelectedCount + " / " + this.dataList.size());
            this.adapter = new FileViewAdapter(getActivity(), this.dataList, SocialAnimationActivity.FileTypes.Audio.ordinal(), checkBox, (TextView) this.V.findViewById(R.id.tv_social_count));
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppJunk appJunk = MobiClean.getInstance().mediaAppModule.socialApp.get(AudioFragment.this.mediaApp);
                    if (checkBox.isChecked()) {
                        appJunk.mediaJunkData.get(2).selectAll();
                        appJunk.selectAll();
                        checkBox.setChecked(true);
                    } else {
                        appJunk.mediaJunkData.get(2).unselectAll();
                        appJunk.unselectAll();
                        checkBox.setChecked(false);
                    }
                    AudioFragment.this.adapter.notifyDataSetChanged();
                    TextView textView2 = AudioFragment.this.tvCount;
                    textView2.setText(((AppJunk) AudioFragment.this.socialApp.get(AudioFragment.this.mediaApp)).mediaJunkData.get(2).totSelectedCount + " / " + AudioFragment.this.dataList.size());
                    ((TextView) AudioFragment.this.getActivity().findViewById(R.id.clean_now_text)).setText("" + AudioFragment.this.getActivity().getString(R.string.mbc_clean) + " " + Util.convertBytes(((AppJunk) AudioFragment.this.socialApp.get(AudioFragment.this.mediaApp)).selectedSize));
                }
            });
            ((SocialGridView) this.V.findViewById(R.id.rv_audadapter)).setAdapter((ListAdapter) new SimpleSectionedListAdapter(getActivity(), this.adapter, R.layout.list_item_header, R.id.header));
        }
        return this.V;
    }

    @Override
    public void onResume() {
        String string;
        super.onResume();
        try {
            FileViewAdapter fileViewAdapter = this.adapter;
            if (fileViewAdapter == null || !GlobalData.returnedAfterSocialDeletion) {
                return;
            }
            fileViewAdapter.notifyDataSetChanged();
            this.tvCount.setText(this.socialApp.get(this.mediaApp).mediaJunkData.get(2).totSelectedCount + " / " + this.dataList.size());
            String convertBytes = Util.convertBytes(this.socialApp.get(this.mediaApp).selectedSize);
            if (this.socialApp.get(this.mediaApp).selectedSize > 0) {
                string = getActivity().getString(R.string.mbc_clean) + " " + convertBytes;
            } else {
                string = getActivity().getString(R.string.mbc_clean);
            }
            ((TextView) getActivity().findViewById(R.id.clean_now_text)).setText(string);
            this.socialApp.get(GlobalData.APP_INDEX).refresh();
            long j = this.socialApp.get(GlobalData.APP_INDEX).appJunkSize;
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
