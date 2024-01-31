package com.cleanPhone.mobileCleaner.socialmedia;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.cleanPhone.mobileCleaner.R;

import java.util.ArrayList;

public class GridActivity extends CacheGridActivity {
    private GridView grid;
    private ImageAdapter mAdapter;
    private String[] mHeaderNames;
    private Integer[] mHeaderPositions;
    private Integer[] mImageIds;
    private ArrayList<SimpleSectionedGridAdapter.Section> sections = new ArrayList<>();


    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter(Context context) {
            this.mInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return GridActivity.this.mImageIds.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = this.mInflater.inflate(R.layout.grid_item_grid, viewGroup, false);
            }
            GridActivity gridActivity = GridActivity.this;
            gridActivity.loadBitmap(gridActivity.mImageIds[i].intValue(), (ImageView) ViewHolder.get(view, R.id.image_grid));
            return view;
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

    public GridActivity() {
        Integer valueOf = Integer.valueOf((int) R.drawable.ic_android);
        this.mImageIds = new Integer[]{valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf, valueOf};
        this.mHeaderNames = new String[]{"Cute Cats", "Few Cats", "Some Cats", "Some More Cats", "Some More More Cats", "Many Cats", "Many Many Cats", "So Many Cats"};
        this.mHeaderPositions = new Integer[]{0, 6, 11, 37, 38, 60, 77, 89};
    }

    private void initControls() {
        this.grid = (GridView) findViewById(R.id.grid);
        this.mAdapter = new ImageAdapter(this);
        int i = 0;
        while (true) {
            Integer[] numArr = this.mHeaderPositions;
            if (i < numArr.length) {
                this.sections.add(new SimpleSectionedGridAdapter.Section(numArr[i].intValue(), this.mHeaderNames[i]));
                i++;
            } else {
                SimpleSectionedGridAdapter simpleSectionedGridAdapter = new SimpleSectionedGridAdapter(this, this.mAdapter, R.layout.grid_item_header, R.id.header_layout_grid, R.id.header_grid);
                simpleSectionedGridAdapter.setGridView(this.grid);
                simpleSectionedGridAdapter.setSections((SimpleSectionedGridAdapter.Section[]) this.sections.toArray(new SimpleSectionedGridAdapter.Section[0]));
                this.grid.setAdapter((ListAdapter) simpleSectionedGridAdapter);
                return;
            }
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_gird_gridview);
        initControls();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.grid, menu);
        return true;
    }
}
