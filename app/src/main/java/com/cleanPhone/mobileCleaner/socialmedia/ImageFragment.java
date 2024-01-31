package com.cleanPhone.mobileCleaner.socialmedia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


public class ImageFragment extends Fragment {
    public View V;
    public ArrayList<AppJunk> W;
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
        this.V = layoutInflater.inflate(R.layout.fragment_one, viewGroup, false);
        try {
            if (getActivity() != null) {
                this.mediaApp = getActivity().getIntent().getIntExtra(FirebaseAnalytics.Param.INDEX, 0);
            }
            this.tvCount = (TextView) this.V.findViewById(R.id.tv_social_count);
            ((TextView) this.V.findViewById(R.id.tv_social_type)).setText("" + getActivity().getResources().getString(R.string.mbc_photos));
            ArrayList<AppJunk> arrayList = MobiClean.getInstance().mediaAppModule.socialApp;
            this.W = arrayList;
            this.dataList = arrayList.get(this.mediaApp).mediaJunkData.get(0).dataList;
            TextView textView = this.tvCount;
            textView.setText(this.W.get(this.mediaApp).mediaJunkData.get(0).totSelectedCount + " / " + this.dataList.size());
            if (this.dataList.size() == 0) {
                this.V.findViewById(R.id.hidden_view).setVisibility(View.VISIBLE);
                this.V.findViewById(R.id.parent_fragment).setVisibility(View.GONE);
            } else {
                this.adapter = new ImagesViewAdapter(getActivity(), this.dataList, SocialAnimationActivity.FileTypes.Image.ordinal(), (CheckBox) this.V.findViewById(R.id.checkAllimg), (TextView) this.V.findViewById(R.id.tv_social_count));
                ((SocialGridView) this.V.findViewById(R.id.rv_imgadapter)).setAdapter((ListAdapter) new SimpleSectionedListAdapter(getActivity(), this.adapter, R.layout.list_item_header, R.id.header));
                final CheckBox checkBox = (CheckBox) this.V.findViewById(R.id.checkAllimg);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppJunk appJunk = MobiClean.getInstance().mediaAppModule.socialApp.get(ImageFragment.this.mediaApp);
                        if (checkBox.isChecked()) {
                            appJunk.mediaJunkData.get(0).selectAll();
                            appJunk.selectAll();
                            checkBox.setChecked(true);
                        } else {
                            appJunk.mediaJunkData.get(0).unselectAll();
                            appJunk.unselectAll();
                            checkBox.setChecked(false);
                        }
                        ImageFragment.this.adapter.notifyDataSetChanged();
                        TextView textView2 = ImageFragment.this.tvCount;
                        StringBuilder sb = new StringBuilder();
                        ImageFragment imageFragment = ImageFragment.this;
                        sb.append(imageFragment.W.get(imageFragment.mediaApp).mediaJunkData.get(0).totSelectedCount);
                        sb.append(" / ");
                        sb.append(ImageFragment.this.dataList.size());
                        textView2.setText(sb.toString());
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("");
                        sb2.append(ImageFragment.this.getActivity().getString(R.string.mbc_clean));
                        sb2.append(" ");
                        ImageFragment imageFragment2 = ImageFragment.this;
                        sb2.append(Util.convertBytes(imageFragment2.W.get(imageFragment2.mediaApp).selectedSize));
                        ((TextView)ImageFragment.this.getActivity().findViewById(R.id.clean_now_text)).setText(sb2.toString());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            this.tvCount.setText(this.W.get(this.mediaApp).mediaJunkData.get(0).totSelectedCount + " / " + this.dataList.size());
            String convertBytes = Util.convertBytes(this.W.get(this.mediaApp).selectedSize);
            if (this.W.get(this.mediaApp).selectedSize > 0) {
                string = getActivity().getString(R.string.mbc_clean) + " " + convertBytes;
            } else {
                string = getActivity().getString(R.string.mbc_clean);
            }
            ((TextView) getActivity().findViewById(R.id.btncleannow)).setText(string);
            this.W.get(GlobalData.APP_INDEX).refresh();
            long j = this.W.get(GlobalData.APP_INDEX).appJunkSize;
            ((TextView) getActivity().findViewById(R.id.junkdisplay_sizetv)).setText(Util.convertBytes(j));
            GlobalData.returnedAfterSocialDeletion = false;
            if (j == 0) {
                getActivity().finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
    }
}
