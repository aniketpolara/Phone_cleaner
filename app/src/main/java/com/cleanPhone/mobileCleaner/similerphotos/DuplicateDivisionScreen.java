package com.cleanPhone.mobileCleaner.similerphotos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.GlobalData;

import java.io.File;
import java.util.ArrayList;

public class DuplicateDivisionScreen extends AppCompatActivity {
    private static final String TAG = "DuplctPart";
    private int deviceHeight;
    public ArrayList<ArrayList<ImageDetail>> h = new ArrayList<>();
    private int hieght;
    public Context i;
    public ListView j;
    public ImageView k;
    public LinearLayout l;
    public LinearLayout m;
    private boolean redirectToNoti;
    private int width;

    public class ListAdapter extends BaseAdapter {
        public ImageView f5046a;
        public ImageView b;
        public ImageView f5047c;
        public ImageView f5048d;
        public TextView e;
        public Context f;
        public ArrayList<ArrayList<ImageDetail>> g;
        private LayoutInflater mInflater;

        public ListAdapter(Context context, ArrayList<ArrayList<ImageDetail>> arrayList) {
            this.f = context;
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.g = arrayList;
        }

        @Override
        public int getCount() {
            return this.g.size();
        }

        @Override
        public Object getItem(int i) {
            return Integer.valueOf(this.g.size());
        }

        @Override
        public long getItemId(int i) {
            return this.g.size();
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View inflate = this.mInflater.inflate(R.layout.group_item, (ViewGroup) null);
            this.f5046a = (ImageView) inflate.findViewById(R.id.imgg1);
            this.b = (ImageView) inflate.findViewById(R.id.imgg2);
            this.f5047c = (ImageView) inflate.findViewById(R.id.imgg3);
            this.f5048d = (ImageView) inflate.findViewById(R.id.imgg4);
            this.e = (TextView) inflate.findViewById(R.id.textview);
            LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.group_layout);
            TextView textView = this.e;
            textView.setText(DuplicateDivisionScreen.this.getResources().getString(R.string.mbc_group) + " (" + (i + 1) + ")");
            StringBuilder sb = new StringBuilder();
            sb.append(this.g.get(i).toString());
            sb.append(IOUtils.LINE_SEPARATOR_UNIX);
            sb.append(this.g.get(i).size());
            Log.e("DuplctPart???", sb.toString());
            if (this.g.get(i).size() > 0) {
                Glide.with(this.f).load(new File(this.g.get(i).get(0).path)).into(this.f5046a);
            } else {
                Glide.with(this.f).load(new File("")).into(this.f5046a);
            }
            if (this.g.get(i).size() > 1) {
                Glide.with(this.f).load(new File(this.g.get(i).get(1).path)).into(this.b);
            } else {
                Glide.with(this.f).load(new File("")).into(this.b);
            }
            if (this.g.get(i).size() > 2) {
                Glide.with(this.f).load(new File(this.g.get(i).get(2).path)).into(this.f5047c);
            } else {
                Glide.with(this.f).load(new File("")).into(this.f5047c);
            }
            if (this.g.get(i).size() > 3) {
                Glide.with(this.f).load(new File(this.g.get(i).get(3).path)).into(this.f5048d);
            } else {
                Glide.with(this.f).load(new File("")).into(this.f5048d);
            }
            inflate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    MobiClean.getInstance().duplicatesData.imageList.clear();
                    MobiClean.getInstance().duplicatesData.imageList.addAll(ListAdapter.this.g.get(i));
                    DuplicateDivisionScreen.this.getWindow().clearFlags(128);
                    Intent intent = new Intent(DuplicateDivisionScreen.this, DuplicatesActivity.class);
                    intent.putExtra("FROMNOTI", DuplicateDivisionScreen.this.getIntent().getBooleanExtra("FROMNOTI", false));
                    if (GlobalData.fromSpacemanager) {
                        DuplicateDivisionScreen.this.finish();
                    }
                    DuplicateDivisionScreen.this.startActivity(intent);
                    DuplicateDivisionScreen.this.finish();
                }
            });
            return inflate;
        }
    }

    private void GetDimention() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.i.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        int i = displayMetrics.heightPixels;
        this.deviceHeight = i;
        this.width = (displayMetrics.widthPixels * 15) / 100;
        this.hieght = (i * 10) / 100;
        setdimention();
    }

    private void GetIds() {
        this.i = this;
        this.l = (LinearLayout) findViewById(R.id.top_layout);
        this.m = (LinearLayout) findViewById(R.id.mid_layout);
        this.j = (ListView) findViewById(R.id.listview_sublist);
        this.k = (ImageView) findViewById(R.id.image_info);
    }

    private void redirectToNoti() {
        this.redirectToNoti = getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false);
    }

    private void setdimention() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.l.getLayoutParams();
        layoutParams.height = (this.deviceHeight * 18) / 100;
        this.l.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.m.getLayoutParams();
        layoutParams2.height = (this.deviceHeight * 7) / 100;
        this.m.setLayoutParams(layoutParams2);
        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) this.j.getLayoutParams();
        layoutParams3.height = (this.deviceHeight * 75) / 100;
        this.j.setLayoutParams(layoutParams3);
    }

    @Override
    public void onBackPressed() {
        if (this.redirectToNoti) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        } else {
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_duplicate_partition);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        GetIds();
        GetDimention();
        redirectToNoti();
        ArrayList<ImageDetail> arrayList = MobiClean.getInstance().duplicatesData.imageList;
        this.h.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            if (this.h.size() > 0) {
                ArrayList<ArrayList<ImageDetail>> arrayList2 = this.h;
                if (arrayList2.get(arrayList2.size() - 1).size() < 10000) {
                    ArrayList<ArrayList<ImageDetail>> arrayList3 = this.h;
                    arrayList3.get(arrayList3.size() - 1).add(arrayList.get(i));
                    Log.e(TAG, "onCreate:arrlist size========= "+arrayList );
                } else {
                    this.h.add(new ArrayList<>());
                }
            } else {
                this.h.add(new ArrayList<>());
            }
        }
        this.j.setAdapter((android.widget.ListAdapter) new ListAdapter(this.i, this.h));
        this.k.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DuplicateDivisionScreen.this);
                String string = DuplicateDivisionScreen.this.getResources().getString(R.string.mbc_group_detail);
                builder.setMessage(string.replace("%1$s", "" + GlobalData.photocount)).setTitle(DuplicateDivisionScreen.this.getResources().getString(R.string.mbc_group_info)).setCancelable(true);
                builder.setPositiveButton(DuplicateDivisionScreen.this.getString(R.string.mbc_ok), new DialogInterface.OnClickListener() { // from class: com.mobiclean.cache.cleaner.similerphotos.DuplicateDivisionScreen.1.1
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}