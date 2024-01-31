package com.cleanPhone.mobileCleaner.utility;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;

public class SimpleSectionedListAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private ListAdapter mBaseAdapter;
    private int mHeaderTextViewResId;
    private LayoutInflater mLayoutInflater;
    private int mSectionResourceId;
    private boolean mValid = true;
    private SparseArray<Section> mSections = new SparseArray<>();

    public static class Section {
        public int f5333a;
        public int b;
        public CharSequence f5334c;

        public Section(int i, CharSequence charSequence) {
            this.f5333a = i;
            this.f5334c = charSequence;
        }

        public CharSequence getTitle() {
            return this.f5334c;
        }
    }

    public SimpleSectionedListAdapter(Context context, BaseAdapter baseAdapter, int i, int i2) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mSectionResourceId = i;
        this.mHeaderTextViewResId = i2;
        this.mBaseAdapter = baseAdapter;
        baseAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                SimpleSectionedListAdapter simpleSectionedListAdapter = SimpleSectionedListAdapter.this;
                simpleSectionedListAdapter.mValid = !simpleSectionedListAdapter.mBaseAdapter.isEmpty();
                SimpleSectionedListAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                SimpleSectionedListAdapter.this.mValid = false;
                SimpleSectionedListAdapter.this.notifyDataSetInvalidated();
            }
        });
    }

    @Override
    public boolean areAllItemsEnabled() {
        return this.mBaseAdapter.areAllItemsEnabled();
    }

    @Override
    public int getCount() {
        if (this.mValid) {
            return this.mBaseAdapter.getCount() + this.mSections.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (isSectionHeaderPosition(i)) {
            return this.mSections.get(i);
        }
        return this.mBaseAdapter.getItem(sectionedPositionToPosition(i));
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
            return getViewTypeCount() - 1;
        }
        return this.mBaseAdapter.getItemViewType(sectionedPositionToPosition(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (isSectionHeaderPosition(i)) {
            if (view == null) {
                view = this.mLayoutInflater.inflate(this.mSectionResourceId, viewGroup, false);
            } else if (view.findViewById(this.mHeaderTextViewResId) == null) {
                view = this.mLayoutInflater.inflate(this.mSectionResourceId, viewGroup, false);
            }
            ((TextView) view.findViewById(this.mHeaderTextViewResId)).setText(this.mSections.get(i).f5334c);
            return view;
        }
        return this.mBaseAdapter.getView(sectionedPositionToPosition(i), view, viewGroup);
    }

    @Override
    public int getViewTypeCount() {
        return this.mBaseAdapter.getViewTypeCount() + 1;
    }

    @Override
    public boolean hasStableIds() {
        return this.mBaseAdapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return this.mBaseAdapter.isEmpty();
    }

    @Override
    public boolean isEnabled(int i) {
        if (isSectionHeaderPosition(i)) {
            return false;
        }
        return this.mBaseAdapter.isEnabled(sectionedPositionToPosition(i));
    }

    @Override
    public boolean isItemViewTypePinned(int i) {
        return isSectionHeaderPosition(i);
    }

    public boolean isSectionHeaderPosition(int i) {
        return this.mSections.get(i) != null;
    }

    public int positionToSectionedPosition(int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < this.mSections.size() && this.mSections.valueAt(i3).f5333a <= i; i3++) {
            i2++;
        }
        return i + i2;
    }

    public int sectionedPositionToPosition(int i) {
        if (isSectionHeaderPosition(i)) {
            return -1;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.mSections.size() && this.mSections.valueAt(i3).b <= i; i3++) {
            i2--;
        }
        return i + i2;
    }

    public void setSections(Section... sectionArr) {
        this.mSections.clear();
        notifyDataSetChanged();
        Arrays.sort(sectionArr, new Comparator<Section>() {
            @Override
            public int compare(Section section, Section section2) {
                int i = section.f5333a;
                int i2 = section2.f5333a;
                if (i == i2) {
                    return 0;
                }
                return i < i2 ? -1 : 1;
            }
        });
        int i = 0;
        for (Section section : sectionArr) {
            int i2 = section.f5333a + i;
            section.b = i2;
            this.mSections.append(i2, section);
            i++;
        }
        notifyDataSetChanged();
    }
}
