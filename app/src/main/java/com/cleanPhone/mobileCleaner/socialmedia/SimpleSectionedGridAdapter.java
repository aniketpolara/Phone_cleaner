package com.cleanPhone.mobileCleaner.socialmedia;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;


public class SimpleSectionedGridAdapter extends BaseAdapter implements PinnedSectionGridView.PinnedSectionGridAdapter {
    private ListAdapter mBaseAdapter;
    private int mColumnWidth;
    private Context mContext;
    private GridView mGridView;
    private int mHeaderLayoutResId;
    private int mHeaderTextViewResId;
    private int mHeaderWidth;
    private int mHorizontalSpacing;
    private View mLastViewSeen;
    private LayoutInflater mLayoutInflater;
    private int mNumColumns;
    private int mSectionResourceId;
    private int mStrechMode;
    private int mWidth;
    private int requestedColumnWidth;
    private int requestedHorizontalSpacing;
    private boolean mValid = true;
    private SparseArray<Section> mSections = new SparseArray<>();
    private Section[] mInitialSections = new Section[0];

    public static class Section {
        public int f5130a;
        public int b;
        public CharSequence f5131c;
        public int f5132d = 0;

        public Section(int i, CharSequence charSequence) {
            this.f5130a = i;
            this.f5131c = charSequence;
        }

        public CharSequence getTitle() {
            return this.f5131c;
        }
    }

    public static class ViewHolder {
        public static <T extends View> T get(View view, int i) {
            SparseArray sparseArray = (SparseArray) view.getTag();
            if (sparseArray == null) {
                sparseArray = new SparseArray();
                view.setTag(sparseArray);
            }
            T t = (T) sparseArray.get(i);
            if (t == null) {
                T t2 = (T) view.findViewById(i);
                sparseArray.put(i, t2);
                return t2;
            }
            return t;
        }
    }

    public SimpleSectionedGridAdapter(Context context, BaseAdapter baseAdapter, int i, int i2, int i3) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mSectionResourceId = i;
        this.mHeaderLayoutResId = i2;
        this.mHeaderTextViewResId = i3;
        this.mBaseAdapter = baseAdapter;
        this.mContext = context;
        baseAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                SimpleSectionedGridAdapter simpleSectionedGridAdapter = SimpleSectionedGridAdapter.this;
                simpleSectionedGridAdapter.mValid = !simpleSectionedGridAdapter.mBaseAdapter.isEmpty();
                SimpleSectionedGridAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                SimpleSectionedGridAdapter.this.mValid = false;
                SimpleSectionedGridAdapter.this.notifyDataSetInvalidated();
            }
        });
    }

    private FillerView getFillerView(View view) {
        FillerView fillerView = new FillerView(this.mContext);
        fillerView.setMeasureTarget(view);
        return fillerView;
    }

    private int getHeaderSize() {
        int i = this.mHeaderWidth;
        if (i > 0) {
            return i;
        }
        if (this.mWidth != this.mGridView.getWidth()) {
            this.mStrechMode = this.mGridView.getStretchMode();
            this.mWidth = ((PinnedSectionGridView) this.mGridView).getAvailableWidth() - (this.mGridView.getPaddingLeft() + this.mGridView.getPaddingRight());
            this.mNumColumns = ((PinnedSectionGridView) this.mGridView).getNumColumns();
            this.requestedColumnWidth = ((PinnedSectionGridView) this.mGridView).getColumnWidth();
            this.requestedHorizontalSpacing = ((PinnedSectionGridView) this.mGridView).getHorizontalSpacing();
        }
        int i2 = this.mWidth;
        int i3 = this.mNumColumns;
        int i4 = this.requestedColumnWidth;
        int i5 = this.requestedHorizontalSpacing;
        int i6 = (i2 - (i3 * i4)) - ((i3 - 1) * i5);
        int i7 = this.mStrechMode;
        if (i7 == 0) {
            this.mWidth = i2 - i6;
            this.mColumnWidth = i4;
            this.mHorizontalSpacing = i5;
        } else if (i7 == 1) {
            this.mColumnWidth = i4;
            if (i3 > 1) {
                this.mHorizontalSpacing = i5 + (i6 / (i3 - 1));
            } else {
                this.mHorizontalSpacing = i5 + i6;
            }
        } else if (i7 == 2) {
            this.mColumnWidth = i4 + (i6 / i3);
            this.mHorizontalSpacing = i5;
        } else if (i7 == 3) {
            this.mColumnWidth = i4;
            this.mHorizontalSpacing = i5;
            this.mWidth = (i2 - i6) + (i5 * 2);
        }
        int i8 = this.mWidth + ((i3 - 1) * (this.mColumnWidth + this.mHorizontalSpacing));
        this.mHeaderWidth = i8;
        return i8;
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
    public int getHeaderLayoutResId() {
        return this.mHeaderLayoutResId;
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
            } else if (view.findViewById(this.mHeaderLayoutResId) == null) {
                view = this.mLayoutInflater.inflate(this.mSectionResourceId, viewGroup, false);
            }
            int i2 = this.mSections.get(i).f5132d;
            if (i2 == 1) {
                ((TextView) view.findViewById(this.mHeaderTextViewResId)).setText(this.mSections.get(i).f5131c);
                ((HeaderLayout) view.findViewById(this.mHeaderLayoutResId)).setHeaderWidth(getHeaderSize());
                return view;
            } else if (i2 != 2) {
                return getFillerView(this.mLastViewSeen);
            } else {
                ((TextView) view.findViewById(this.mHeaderTextViewResId)).setText(this.mSections.get(i).f5131c);
                ((HeaderLayout) view.findViewById(this.mHeaderLayoutResId)).setHeaderWidth(0);
                return view;
            }
        }
        View view2 = this.mBaseAdapter.getView(sectionedPositionToPosition(i), view, viewGroup);
        this.mLastViewSeen = view2;
        return view2;
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
        return isSectionHeaderPosition(i) && this.mSections.get(i).f5132d != 0;
    }

    public boolean isSectionHeaderPosition(int i) {
        return this.mSections.get(i) != null;
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

    public void setGridView(GridView gridView) {
        if (gridView instanceof PinnedSectionGridView) {
            this.mGridView = gridView;
            this.mStrechMode = gridView.getStretchMode();
            this.mWidth = gridView.getWidth() - (this.mGridView.getPaddingLeft() + this.mGridView.getPaddingRight());
            PinnedSectionGridView pinnedSectionGridView = (PinnedSectionGridView) gridView;
            this.mNumColumns = pinnedSectionGridView.getNumColumns();
            this.requestedColumnWidth = pinnedSectionGridView.getColumnWidth();
            this.requestedHorizontalSpacing = pinnedSectionGridView.getHorizontalSpacing();
            return;
        }
        throw new IllegalArgumentException("Does your grid view extends PinnedSectionGridView?");
    }

    public void setSections(Section... sectionArr) {
        this.mInitialSections = sectionArr;
        setSections();
    }

    public void setSections() {
        this.mSections.clear();
        getHeaderSize();
        Arrays.sort(this.mInitialSections, new Comparator<Section>() {
            @Override
            public int compare(Section section, Section section2) {
                int i = section.f5130a;
                int i2 = section2.f5130a;
                if (i == i2) {
                    return 0;
                }
                return i < i2 ? -1 : 1;
            }
        });
        int i = 0;
        int i2 = 0;
        while (true) {
            Section[] sectionArr = this.mInitialSections;
            if (i < sectionArr.length) {
                Section section = sectionArr[i];
                for (int i3 = 0; i3 < this.mNumColumns - 1; i3++) {
                    Section section2 = new Section(section.f5130a, section.f5131c);
                    section2.f5132d = 2;
                    int i4 = section2.f5130a + i2;
                    section2.b = i4;
                    this.mSections.append(i4, section2);
                    i2++;
                }
                Section section3 = new Section(section.f5130a, section.f5131c);
                section3.f5132d = 1;
                int i5 = section3.f5130a + i2;
                section3.b = i5;
                this.mSections.append(i5, section3);
                i2++;
                Section[] sectionArr2 = this.mInitialSections;
                if (i < sectionArr2.length - 1) {
                    int i6 = sectionArr2[i + 1].f5130a;
                    int i7 = this.mNumColumns;
                    int i8 = i7 - ((i6 - section.f5130a) % i7);
                    if (i7 != i8) {
                        for (int i9 = 0; i9 < i8; i9++) {
                            Section section4 = new Section(section.f5130a, section.f5131c);
                            section4.f5132d = 0;
                            int i10 = i6 + i2;
                            section4.b = i10;
                            this.mSections.append(i10, section4);
                            i2++;
                        }
                    }
                }
                i++;
            } else {
                notifyDataSetChanged();
                return;
            }
        }
    }
}
