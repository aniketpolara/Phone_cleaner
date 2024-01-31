package com.cleanPhone.mobileCleaner.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.similerphotos.DuplicatesActivity;

import java.util.Arrays;
import java.util.Comparator;

public class SectionedGridRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerView.Adapter mBaseAdapter;
    private final Context mContext;
    private int mSectionResourceId;
    private boolean mValid = true;
    private SparseArray<Section> mSections = new SparseArray<>();

    public static class Section {
        public String f4888a;
        public int b;
        public int f4889c;
        public CharSequence f4890d;
        public String e;
        public boolean f;

        public Section(int i, CharSequence charSequence, String str, String str2, boolean z) {
            this.b = i;
            this.f4890d = charSequence;
            this.e = str;
            this.f4888a = str2;
            this.f = z;
        }

        public String getCount() {
            return this.e;
        }

        public CharSequence getTitle() {
            return this.f4890d;
        }
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        public TextView p;
        public TextView q;
        public TextView r;
        public CheckBox s;

        public SectionViewHolder(View view) {
            super(view);
            this.q = (TextView) view.findViewById(R.id.section_text);
            this.p = (TextView) view.findViewById(R.id.section_text2);
            this.r = (TextView) view.findViewById(R.id.tv_group_childcount);
            this.s = (CheckBox) view.findViewById(R.id.grp_chk);
        }
    }

    public SectionedGridRecyclerViewAdapter(Context context, int i, RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        this.mSectionResourceId = i;
        this.mBaseAdapter = adapter;
        this.mContext = context;
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                SectionedGridRecyclerViewAdapter sectionedGridRecyclerViewAdapter = SectionedGridRecyclerViewAdapter.this;
                sectionedGridRecyclerViewAdapter.mValid = sectionedGridRecyclerViewAdapter.mBaseAdapter.getItemCount() > 0;
                SectionedGridRecyclerViewAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int i2, int i3) {
                SectionedGridRecyclerViewAdapter sectionedGridRecyclerViewAdapter = SectionedGridRecyclerViewAdapter.this;
                sectionedGridRecyclerViewAdapter.mValid = sectionedGridRecyclerViewAdapter.mBaseAdapter.getItemCount() > 0;
                SectionedGridRecyclerViewAdapter.this.notifyItemRangeChanged(i2, i3);
            }

            @Override
            public void onItemRangeInserted(int i2, int i3) {
                SectionedGridRecyclerViewAdapter sectionedGridRecyclerViewAdapter = SectionedGridRecyclerViewAdapter.this;
                sectionedGridRecyclerViewAdapter.mValid = sectionedGridRecyclerViewAdapter.mBaseAdapter.getItemCount() > 0;
                SectionedGridRecyclerViewAdapter.this.notifyItemRangeInserted(i2, i3);
            }

            @Override
            public void onItemRangeRemoved(int i2, int i3) {
                SectionedGridRecyclerViewAdapter sectionedGridRecyclerViewAdapter = SectionedGridRecyclerViewAdapter.this;
                sectionedGridRecyclerViewAdapter.mValid = sectionedGridRecyclerViewAdapter.mBaseAdapter.getItemCount() > 0;
                SectionedGridRecyclerViewAdapter.this.notifyItemRangeRemoved(i2, i3);
            }
        });
        final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        if (gridLayoutManager != null) {
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.mobiclean.phoneclean.customview.SectionedGridRecyclerViewAdapter.2
                @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                public int getSpanSize(int i2) {
                    if (SectionedGridRecyclerViewAdapter.this.isSectionHeaderPosition(i2)) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (this.mValid) {
            return this.mBaseAdapter.getItemCount() + this.mSections.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int i) {
        if (isSectionHeaderPosition(i)) {
            return Integer.MAX_VALUE - this.mSections.indexOfKey(i);
        }
        return this.mBaseAdapter.getItemId(sectionedPositionToPosition(i));
    }

    @Override
    public int getItemViewType(int i) {
        if (isSectionHeaderPosition(i)) {
            return 0;
        }
        return this.mBaseAdapter.getItemViewType(sectionedPositionToPosition(i)) + 1;
    }

    public boolean isSectionHeaderPosition(int i) {
        return this.mSections.get(i) != null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        if (isSectionHeaderPosition(i)) {
            SectionViewHolder sectionViewHolder = (SectionViewHolder) viewHolder;
            sectionViewHolder.q.setText(this.mSections.get(i).f4890d);
            TextView textView = sectionViewHolder.p;
            textView.setText("(" + this.mSections.get(i).e + " " + this.mContext.getResources().getString(R.string.images_found) + ")");
            sectionViewHolder.r.setText(this.mSections.get(i).f4888a);
            if (this.mSections.get(i).f) {
                sectionViewHolder.s.setChecked(true);
            } else {
                sectionViewHolder.s.setChecked(false);
            }
            sectionViewHolder.s.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int intValue = Integer.valueOf(((Section) SectionedGridRecyclerViewAdapter.this.mSections.get(i)).f4890d.toString().split(SectionedGridRecyclerViewAdapter.this.mContext.getString(R.string.mbc_group))[1]).intValue();
                    DuplicatesActivity duplicatesActivity = (DuplicatesActivity) SectionedGridRecyclerViewAdapter.this.mContext;
                    if (((SectionViewHolder) viewHolder).s.isChecked()) {
                        ((SectionViewHolder) viewHolder).s.setChecked(true);
                        ((Section) SectionedGridRecyclerViewAdapter.this.mSections.get(i)).f = true;
                        for (int i2 = 1; i2 < duplicatesActivity.duplicateGridImages.get(Integer.valueOf(intValue)).size(); i2++) {
                            if (!duplicatesActivity.duplicateGridImages.get(Integer.valueOf(intValue)).get(i2).ischecked && !duplicatesActivity.duplicateGridImages.get(Integer.valueOf(intValue)).get(i2).lockImg) {
                                duplicatesActivity.duplicateGridImages.get(Integer.valueOf(intValue)).get(i2).ischecked = true;
                                MobiClean.getInstance().duplicatesData.selectNode(duplicatesActivity.duplicateGridImages.get(Integer.valueOf(intValue)).get(i2));
                            }
                        }
                        if (MobiClean.getInstance().duplicatesData.isLockAnyImage(duplicatesActivity.duplicateGridImages.get(Integer.valueOf(intValue))) && !duplicatesActivity.duplicateGridImages.get(Integer.valueOf(intValue)).get(0).lockImg) {
                            duplicatesActivity.duplicateGridImages.get(Integer.valueOf(intValue)).get(0).ischecked = true;
                            MobiClean.getInstance().duplicatesData.selectNode(duplicatesActivity.duplicateGridImages.get(Integer.valueOf(intValue)).get(0));
                        }
                    } else {
                        ((SectionViewHolder) viewHolder).s.setChecked(false);
                        ((Section) SectionedGridRecyclerViewAdapter.this.mSections.get(i)).f = false;
                        for (int i3 = 0; i3 < duplicatesActivity.duplicateGridImages.get(Integer.valueOf(intValue)).size(); i3++) {
                            if (duplicatesActivity.duplicateGridImages.get(Integer.valueOf(intValue)).get(i3).ischecked) {
                                duplicatesActivity.duplicateGridImages.get(Integer.valueOf(intValue)).get(i3).ischecked = false;
                                MobiClean.getInstance().duplicatesData.unselectNode(duplicatesActivity.duplicateGridImages.get(Integer.valueOf(intValue)).get(i3));
                            }
                        }
                    }
                    duplicatesActivity.setDeleteBtnText();
                    duplicatesActivity.refreshRecyclerView();
                }
            });
            return;
        }
        this.mBaseAdapter.onBindViewHolder(viewHolder, sectionedPositionToPosition(i));
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new SectionViewHolder(LayoutInflater.from(this.mContext).inflate(this.mSectionResourceId, viewGroup, false));
        }
        return this.mBaseAdapter.onCreateViewHolder(viewGroup, i - 1);
    }

    public int sectionedPositionToPosition(int i) {
        if (isSectionHeaderPosition(i)) {
            return -1;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.mSections.size() && this.mSections.valueAt(i3).f4889c <= i; i3++) {
            i2--;
        }
        return i + i2;
    }

    public void setSections(Section[] sectionArr) {
        this.mSections.clear();
        Arrays.sort(sectionArr, new Comparator<Section>() {
            @Override
            public int compare(Section section, Section section2) {
                int i = section.b;
                int i2 = section2.b;
                if (i == i2) {
                    return 0;
                }
                return i < i2 ? -1 : 1;
            }
        });
        int i = 0;
        for (Section section : sectionArr) {
            int i2 = section.b + i;
            section.f4889c = i2;
            this.mSections.append(i2, section);
            i++;
        }
        notifyDataSetChanged();
    }
}
