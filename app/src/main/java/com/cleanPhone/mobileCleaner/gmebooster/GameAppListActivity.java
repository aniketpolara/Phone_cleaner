package com.cleanPhone.mobileCleaner.gmebooster;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.utility.GlobalData;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GameAppListActivity extends ParentActivity {
    public ArrayList<Boolean> j;
    public Context k;
    public TextView l;
    public ListView m;
    public ImageView back;
    int admob = 3;
    public ArrayList<String> n;
    public ArrayList<String> o = new ArrayList<>();
    private final int VIEW_TYPE_EXAMPLE = 0;
    private final int VIEW_TYPE_EXAMPLE_TWO = 1;

    public class CacheListAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> data;
        private LayoutInflater inflater;

        public class ViewHolder {
            public ImageView f4918a;
            public TextView b;
            public RelativeLayout f4919c;
            public CheckBox f4920d;

            public ViewHolder(CacheListAdapter cacheListAdapter) {
            }
        }

        public class ViewHolder2 {
            View space;

            public ViewHolder2(View ignoreListAdapter) {
            }
        }

        public CacheListAdapter(Context context, int i, ArrayList<String> arrayList) {
            super(context, i, arrayList);
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data = arrayList;
            GameAppListActivity.this.getPackageManager();
        }

        @Override
        @NonNull
        public View getView(final int i, View view, @NonNull ViewGroup viewGroup) {

            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder(this);
                view = this.inflater.inflate(R.layout.allapp_gamelist, (ViewGroup) null);
                viewHolder.f4918a = (ImageView) view.findViewById(R.id.junklistitemimage);
                viewHolder.b = (TextView) view.findViewById(R.id.junklistitemapp);
                viewHolder.f4919c = (RelativeLayout) view.findViewById(R.id.checklayout);
                viewHolder.f4920d = (CheckBox) view.findViewById(R.id.appitem_check);
//                        view2.setTag(viewHolder);
//                    } else {
//                        view2 = view;
//                        viewHolder = (ViewHolder) view.getTag();
//                    }
                viewHolder.f4920d.setClickable(false);
                viewHolder.f4920d.setFocusable(false);
                TextView textView = viewHolder.b;
                textView.setText("" + this.data.get(i).split("@")[0]);
                ViewHolder finalViewHolder = viewHolder;
                viewHolder.f4919c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view3) {
                        if (GameAppListActivity.this.multipleClicked()) {
                            return;
                        }
                        if (finalViewHolder.f4920d.isChecked()) {
                            finalViewHolder.f4920d.setChecked(false);
                            if (GameAppListActivity.this.o.size() > 0) {
                                for (int i2 = 0; i2 < GameAppListActivity.this.o.size(); i2++) {
                                    if (GameAppListActivity.this.o.get(i2).equalsIgnoreCase((String) CacheListAdapter.this.data.get(i))) {
                                        GameAppListActivity.this.o.remove(i2);
                                    }
                                }
                            }
                            GameAppListActivity.this.j.set(i, Boolean.FALSE);
                        } else {
                            GameAppListActivity.this.j.set(i, Boolean.TRUE);
                            finalViewHolder.f4920d.setChecked(true);
                            CacheListAdapter cacheListAdapter = CacheListAdapter.this;
                            GameAppListActivity.this.o.add((String) cacheListAdapter.data.get(i));
                        }
                        ((TextView) GameAppListActivity.this.findViewById(R.id.tvgamelist_title)).setText(GameAppListActivity.this.o.size() + " " + GameAppListActivity.this.getString(R.string.gamesadded));
                        Log.d("CHECKKK", GameAppListActivity.this.j.toString());
                    }
                });
                String str = this.data.get(i).split("@")[1];
                if (str != null) {
                    try {
                        viewHolder.f4918a.setImageDrawable(this.context.getPackageManager().getApplicationIcon(str));
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                viewHolder.f4920d.setChecked(GameAppListActivity.this.j.get(i).booleanValue());
            }


            return view;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return (position % 9 == 8) ? VIEW_TYPE_EXAMPLE : VIEW_TYPE_EXAMPLE_TWO;
        }
    }


    public void addGameToList(String str) {
        try {
            ArrayList arrayList = new ArrayList();
            FileInputStream openFileInput = openFileInput("games.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(openFileInput);
            arrayList.addAll((ArrayList) objectInputStream.readObject());
            arrayList.add(str);
            objectInputStream.close();
            openFileInput.close();
            FileOutputStream openFileOutput = openFileOutput("games.txt", 0);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(openFileOutput);
            objectOutputStream.writeObject(arrayList);
            objectOutputStream.close();
            openFileOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getdevicedimention() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
    }

    private void makelistOfNames() {
        this.n = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        for (int i = 0; i < GlobalData.nongameApps.size(); i++) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append((Object) packageManager.getApplicationLabel(packageManager.getApplicationInfo("" + GlobalData.nongameApps.get(i), 0)));
                String sb2 = sb.toString();
                ArrayList<String> arrayList = this.n;
                arrayList.add(sb2 + "@" + GlobalData.nongameApps.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_applistgamebooster);

        showInterstialAds(GameAppListActivity.this);
        LinearLayout llBanner = findViewById(R.id.llBanner);
        DH_GllobalItem.fbAdaptiveBanner(this, llBanner, new DH_GllobalItem.BannerCallback() {
            @Override
            public void onAdLoad() {
                llBanner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFail() {
                llBanner.setVisibility(View.GONE);
            }
        });

        this.k = this;
        getdevicedimention();
        this.m = (ListView) findViewById(R.id.listcachedelte);
        this.l = (TextView) findViewById(R.id.emptytv);
        this.back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        this.j = new ArrayList<>();
        for (int i = 0; i < GlobalData.nongameApps.size(); i++) {
            this.j.add(Boolean.FALSE);
        }
        makelistOfNames();
        try {
            Collections.sort(this.n, new Comparator<String>() {
                @Override
                public int compare(String str, String str2) {
                    return str.compareToIgnoreCase(str2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.n.size() == 0) {
            this.l.setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.iv_ok)).setVisibility(View.GONE);
        } else {
            ((ImageView) findViewById(R.id.iv_ok)).setVisibility(View.VISIBLE);
        }
        ((ImageView) findViewById(R.id.iv_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GameAppListActivity.this.multipleClicked()) {
                    return;
                }
                if (GameAppListActivity.this.o.size() > 0) {
                    for (int i2 = 0; i2 < GameAppListActivity.this.o.size(); i2++) {
                        GameAppListActivity gameAppListActivity = GameAppListActivity.this;
                        gameAppListActivity.addGameToList("" + GameAppListActivity.this.o.get(i2).split("@")[1] + "@GAME");
                    }
                    GameAppListActivity.this.finish();
                    return;
                }
                GameAppListActivity gameAppListActivity2 = GameAppListActivity.this;
                Toast.makeText(gameAppListActivity2.k, gameAppListActivity2.getResources().getString(R.string.mbc_pls_add_game_boost), Toast.LENGTH_SHORT).show();


            }
        });
        this.m.setAdapter((ListAdapter) new CacheListAdapter(this, R.layout.junklistitemlayout, this.n));
        sendAnalytics("SCREEN_VISIT_" + getClass().getSimpleName());
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
