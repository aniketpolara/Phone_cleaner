package com.cleanPhone.mobileCleaner.gmebooster;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.customview.GridExpandableView;
import com.cleanPhone.mobileCleaner.utility.DetermineTextSize;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppDetails;
import com.cleanPhone.mobileCleaner.wrappers.PackageInfoStruct;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameBoostActivity extends ParentActivity {
    private static final String TAG = "GameBoostVertiseScreen";
    private Context context;
    private int deviceHeight;
    private int deviceWidth;
    private ImageView fab, back;
    private ImageView fabfilledList;
    private GridExpandableView gameGrid;
    private LinearLayout gamePartionLayout;
    private boolean gameboost_shortcut;
    private ArrayList<String> gamesDataList;
    private Button gamescanbtn;
    private ImageView imgRocket;
    private ImageView imgSmoke;
    private ArrayList<PackageInfoStruct> installedUserApps;
    private LinearLayout layout_status;
    private LinearLayout layout_upper;
    private boolean noti_result_back;
    private PackageManager pm;
    private ProgressDialog progressDialog;
    private long ramsaveSize;

    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;

    int admob = 3;
    private RelativeLayout rlGames;
    private List<ActivityManager.RunningAppProcessInfo> runningProcesses;
    private TextView symbol;
    private TextView tv_bottomTv;
    private TextView tv_gamecount;
    private TextView tv_ramreslese;
    private TextView tv_ramreslesunit;
    private TextView tv_scan_games_count;
    private TextView tv_scan_games_percent;
    private TextView tvtext;
    public String[] j = {"Action", "Adventure", "Arcade", "Board", "Casual", "Educational", "GAME_MUSIC", "Music", "Puzzle", "Racing", "Role Playing", "Simulation", "Sports", "GAME_PUZZLE", "GAME_WORD", "GAME_CASUAL", "STRATEGY", "card"};
    private int ramsize = 0;
    private int totalPss = 0;
    private volatile boolean stopFiltering = false;
    private boolean adLoaded = false;
    private ArrayList<String> selectedAppsList = new ArrayList<>();
    private ArrayList<String> gamesData = new ArrayList<>();

    public class GameListAdepter extends BaseAdapter {
        private Context context1;
        private boolean isAllAps;
        private LayoutInflater layoutInflater;
        private PackageManager pm;

        public GameListAdepter(Context context, ArrayList<String> arrayList, boolean z) {
            this.isAllAps = z;
            this.pm = context.getPackageManager();
            GameBoostActivity.this.gamesDataList = arrayList;
            this.context1 = context;
            GameBoostActivity.this.selectedAppsList.clear();
        }

        @Override
        public int getCount() {
            return GameBoostActivity.this.gamesDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return Integer.valueOf(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layoutInflater = layoutInflater;
            if (layoutInflater != null) {
                view = layoutInflater.inflate(R.layout.grid_list_game, viewGroup, false);
            }
            final ImageView imageView = (ImageView) ViewHolder.get(view, R.id.image);
            final LinearLayout linearLayout = ViewHolder.get(view, R.id.lin_layout);
            TextView textView = (TextView) ViewHolder.get(view, R.id.gameboostitemname);
            try {
                PackageManager packageManager = this.pm;
                imageView.setImageDrawable(packageManager.getApplicationIcon("" + ((String) GameBoostActivity.this.gamesDataList.get(i)).split("@")[1]));
                StringBuilder sb = new StringBuilder();
                sb.append("");
                PackageManager packageManager2 = this.pm;
                sb.append((Object) packageManager2.getApplicationInfo("" + ((String) GameBoostActivity.this.gamesDataList.get(i)).split("@")[1], 0).loadLabel(this.pm));
                textView.setText(sb.toString());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if (GameBoostActivity.this.multipleClicked()) {
                        return;
                    }

                        GameBoostActivity gameBoostActivity = GameBoostActivity.this;
                        gameBoostActivity.boostGame(((String) gameBoostActivity.gamesDataList.get(i)).split("@")[1]);


                }
            });
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view2) {
                    View inflate = GameListAdepter.this.layoutInflater.inflate(R.layout.popup, (ViewGroup) null);
                    final PopupWindow popupWindow = new PopupWindow(inflate, -2, -2);
                    TextView textView2 = (TextView) inflate.findViewById(R.id.popupremove);
                    TextView textView3 = (TextView) inflate.findViewById(R.id.popupunistall);
                    try {
                        String string = GameBoostActivity.this.getResources().getString(R.string.mbc_unistall);
                        textView3.setText(string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase());
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    textView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view3) {
                            if (GameBoostActivity.this.multipleClicked()) {
                                return;
                            }
                                GameBoostActivity gameBoostActivity = GameBoostActivity.this;
                                gameBoostActivity.removeGameFromList("" + ((String) GameBoostActivity.this.gamesDataList.get(i)).split("@")[1]);
                                popupWindow.dismiss();
                                ArrayList gamesSavedList = GameBoostActivity.this.getGamesSavedList();
                                if (gamesSavedList.size() <= 0) {
                                    GameBoostActivity.this.showView();
                                    return;
                                }
                                GameBoostActivity gameBoostActivity2 = GameBoostActivity.this;
                                GameBoostActivity.this.gameGrid.setAdapter((ListAdapter) new GameListAdepter(gameBoostActivity2.context, gamesSavedList, false));
                                GameBoostActivity.this.gameGrid.setNumColumns(3);
                                GameBoostActivity.this.hideView();



                        }
                    });
                    textView3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view3) {
                            if (GameBoostActivity.this.multipleClicked()) {
                                return;
                            }

                                GameBoostActivity.this.context.startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:" + ((String) GameBoostActivity.this.gamesDataList.get(i)).split("@")[1])));
                                popupWindow.dismiss();


                        }
                    });
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setFocusable(true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(0));
                    popupWindow.showAsDropDown(imageView);
                    popupWindow.showAtLocation(imageView, 17, 0, 0);
                    return true;
                }
            });
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

    @SuppressLint("StaticFieldLeak")
    public void appsService() {
        new AsyncTask<String, String, String>() {
            @Override
            public String doInBackground(String... strArr) {
                return null;
            }

            @Override
            public void onPreExecute() {
                Log.e(TAG, "onPreExecute: " + "Gaame boosting ");
                ProgressDialog progressDialog = GameBoostActivity.this.progressDialog;
                progressDialog.setMessage("" + GameBoostActivity.this.getString(R.string.mbc_loading));
                GameBoostActivity.this.progressDialog.show();
                GameBoostActivity.this.getAllAppsInstalled();
                super.onPreExecute();
            }

            @Override
            public void onPostExecute(String str) {
                GameBoostActivity.this.progressDialog.dismiss();
                super.onPostExecute(str);
            }
        }.execute(new String[0]);
    }


    public void boost() {
        String str = new String();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager packageManager = getPackageManager();
        try {
            List<ActivityManager.RunningAppProcessInfo> list = this.runningProcesses;
            if (list != null) {
                list.clear();
            }
            if (activityManager != null) {
                this.runningProcesses = activityManager.getRunningAppProcesses();
            }
            List<ActivityManager.RunningAppProcessInfo> list2 = this.runningProcesses;
            if (list2 != null) {
                if (list2.size() < 2) {
                    kill_servieses();
                    return;
                }
                ArrayList ignoredData = getIgnoredData();
                for (int i = 0; i < this.runningProcesses.size(); i++) {
                    try {
                        str = "" + ((Object) packageManager.getApplicationLabel(packageManager.getApplicationInfo(this.runningProcesses.get(i).processName, PackageManager.GET_META_DATA)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!ignoredData.contains(this.runningProcesses.get(i).processName)) {
                        if (!str.equalsIgnoreCase("Google play services")) {
                            if (!str.equalsIgnoreCase("Google play store")) {
                                if (str.equalsIgnoreCase("APC Rebrand")) {
                                }
                                int[] iArr = {this.runningProcesses.get(i).pid};
                                Debug.MemoryInfo[] memoryInfoArr = new Debug.MemoryInfo[0];
                                if (activityManager != null) {
                                    memoryInfoArr = activityManager.getProcessMemoryInfo(iArr);
                                }
                                for (Debug.MemoryInfo memoryInfo : memoryInfoArr) {
                                    if (this.runningProcesses.get(i).importance == 400 || this.runningProcesses.get(i).importance == 200 || this.runningProcesses.get(i).importance == 300) {
                                        this.totalPss += memoryInfo.getTotalPss() * 1024;
                                    }
                                }
                                for (Debug.MemoryInfo memoryInfo2 : memoryInfoArr) {
                                    Util.appendLog("killing " + this.runningProcesses.get(i).processName + IOUtils.LINE_SEPARATOR_UNIX, "gameboostprocesses.txt");
                                    activityManager.killBackgroundProcesses(this.runningProcesses.get(i).processName);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void boostGame(final String str) {
        new AsyncTask<String, String, String>() {
            @Override
            public void onPreExecute() {
                GameBoostActivity.this.getRamSize();
                Util.appendLog(">>>>> before " + GameBoostActivity.this.ramsize, "gamebooster.txt");
                super.onPreExecute();
            }

            @Override
            public String doInBackground(String... strArr) {
                GameBoostActivity.this.boost();
                return null;
            }

            @Override
            public void onPostExecute(String str2) {
                GameBoostActivity.this.getRamSize();
                Util.appendLog(">>>>> after       " + GameBoostActivity.this.ramsize, "gamebooster.txt");
                    Intent intent = new Intent(GameBoostActivity.this.context, GameMessageBoost.class);
                    intent.putExtra("PKG", str);
                    GameBoostActivity.this.startActivity(intent);

                super.onPostExecute(str);
            }
        }.execute(new String[0]);
    }


    private boolean checkInstalled(ArrayList<String> arrayList, String str) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (str.startsWith(arrayList.get(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIsaGame(String str) {
        if (str.toLowerCase().contains("game")) {
            return true;
        }
        int i = 0;
        while (true) {
            String[] strArr = this.j;
            if (i >= strArr.length) {
                return false;
            }
            if (str.equalsIgnoreCase(strArr[i])) {
                return true;
            }
            i++;
        }
    }


    public void getAllAppsInstalled() {
        GlobalData.nongameApps = getAllAppsList();
            startActivity(new Intent(GameBoostActivity.this, GameAppListActivity.class));


        invalidateOptionsMenu();
    }

    private ArrayList<String> getAllAppsList() {
        String str;
        ArrayList gamesSavedList = getGamesSavedList();
        ArrayList<String> arrayList = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        for (int i = 0; i < this.installedUserApps.size(); i++) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append((Object) packageManager.getApplicationInfo("" + this.installedUserApps.get(i).pname, 0).loadLabel(packageManager));
                sb.append("@");
                sb.append(this.installedUserApps.get(i).pname);
                str = sb.toString();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                str = "";
            }
            if (!gamesSavedList.contains("" + str)) {
                arrayList.add(this.installedUserApps.get(i).pname);
            }
        }
        return arrayList;
    }


    public ArrayList getGamesSavedList() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < this.installedUserApps.size(); i++) {
            arrayList.add(this.installedUserApps.get(i).pname);
        }
        ArrayList arrayList2 = new ArrayList();
        try {
            FileInputStream openFileInput = openFileInput("games.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(openFileInput);
            arrayList2.addAll((ArrayList) objectInputStream.readObject());
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                if (!checkInstalled(arrayList, (String) arrayList2.get(i2))) {
                    arrayList2.remove(i2);
                }
            }
            objectInputStream.close();
            openFileInput.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ArrayList arrayList3 = new ArrayList();
                FileOutputStream openFileOutput = openFileOutput("games.txt", 0);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(openFileOutput);
                objectOutputStream.writeObject(arrayList3);
                objectOutputStream.close();
                openFileOutput.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        ArrayList arrayList4 = new ArrayList();
        for (int i3 = 0; i3 < arrayList2.size(); i3++) {
            if (checkIsaGame(((String) arrayList2.get(i3)).split("@")[1])) {
                arrayList4.add(((String) arrayList2.get(i3)).split("@")[0]);
            }
        }
        try {
            int count = this.gameGrid.getAdapter().getCount();
            if (count > 1) {
                TextView textView = this.tvtext;
                textView.setText("" + getString(R.string.mbc_nogamesfounds));
            } else {
                TextView textView2 = this.tvtext;
                textView2.setText("" + getString(R.string.mbc_nogamesfound));
            }
            TextView textView3 = this.tv_gamecount;
            textView3.setText("" + count);
        } catch (Exception e3) {
            TextView textView4 = this.tv_gamecount;
            textView4.setText("" + arrayList4.size());
            e3.printStackTrace();
        }
        ArrayList arrayList5 = new ArrayList();
        for (int i4 = 0; i4 < arrayList4.size(); i4++) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("");
                PackageManager packageManager = this.pm;
                sb.append((Object) packageManager.getApplicationInfo("" + ((String) arrayList4.get(i4)), 0).loadLabel(this.pm));
                sb.append("@");
                sb.append((String) arrayList4.get(i4));
                arrayList5.add(sb.toString());
            } catch (PackageManager.NameNotFoundException e4) {
                e4.printStackTrace();
            }
        }
        try {
            Collections.sort(arrayList5, new Comparator<String>() {
                @Override
                public int compare(String str, String str2) {
                    return str.compareToIgnoreCase(str2);
                }
            });
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        for (int i5 = 0; i5 < arrayList5.size(); i5++) {
        }
        return arrayList5;
    }


    public void getRamSize() {
        try {
            this.ramsize = 0;
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            if (activityManager != null) {
                activityManager.getMemoryInfo(memoryInfo);
            }
            long j = memoryInfo.availMem;
            long j2 = memoryInfo.totalMem;
            this.ramsaveSize = ((j2 - j) * 100) / j2;
            this.ramsize = (int) (j / 1048576);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getid() {
        this.progressDialog = new ProgressDialog(this);
        this.fab = (ImageView) findViewById(R.id.fab);
        this.back = (ImageView) findViewById(R.id.iv_back);
        this.fabfilledList = (ImageView) findViewById(R.id.fab_filledList);
        this.gamescanbtn = (Button) findViewById(R.id.gamescan_btn);
        this.layout_status = (LinearLayout) findViewById(R.id.laup);
        this.layout_upper = (LinearLayout) findViewById(R.id.layouts);
        this.gameGrid = (GridExpandableView) findViewById(R.id.gamebooster_list);
        this.tv_gamecount = (TextView) findViewById(R.id.textview_gamecount);
        this.symbol = (TextView) findViewById(R.id.symbol_game);
        this.tv_ramreslese = (TextView) findViewById(R.id.gameboost_ramrelese);
        this.tv_ramreslesunit = (TextView) findViewById(R.id.gameboost_ramrelese_unit);
        this.tv_bottomTv = (TextView) findViewById(R.id.tvbottomtext);
        this.tvtext = (TextView) findViewById(R.id.textview_processservies2);
        this.imgRocket = (ImageView) findViewById(R.id.gamerocket);
        this.imgSmoke = (ImageView) findViewById(R.id.game_smoke);
        this.gamePartionLayout = (LinearLayout) findViewById(R.id.game_partitionlayout);
        this.tv_scan_games_count = (TextView) findViewById(R.id.tv_scan_games_count);
        this.tv_scan_games_percent = (TextView) findViewById(R.id.tv_scan_games_percent);
    }


    public void hideView() {
        this.fabfilledList.setVisibility(View.VISIBLE);
        this.layout_status.setVisibility(View.VISIBLE);
        this.gameGrid.setVisibility(View.VISIBLE);
        this.gamePartionLayout.setVisibility(View.INVISIBLE);
        TextView textView = this.tv_gamecount;
        textView.setText("" + this.gameGrid.getAdapter().getCount());
        this.fab.setVisibility(View.GONE);
    }

    public static boolean isConnectingToInternet(Context context) {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null && (allNetworkInfo = connectivityManager.getAllNetworkInfo()) != null) {
            for (NetworkInfo networkInfo : allNetworkInfo) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    private void kill_servieses() {
        List<ApplicationInfo> installedApplications = getPackageManager().getInstalledApplications(0);
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList ignoredData = getIgnoredData();
        for (ApplicationInfo applicationInfo : installedApplications) {
            if ((applicationInfo.flags & 1) != 1 && !applicationInfo.packageName.equals("com.mobiclean.phoneclean")) {
                if (!ignoredData.contains("" + applicationInfo.packageName) && activityManager != null) {
                    activityManager.killBackgroundProcesses(applicationInfo.packageName);
                }
            }
        }
    }

    public void loadinit() {
        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getApplicationContext(), "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        return;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

    private void redirectToNoti() {
        this.noti_result_back = getIntent().getBooleanExtra(GlobalData.NOTI_RESULT_BACK, false);
        this.gameboost_shortcut = getIntent().getBooleanExtra(GlobalData.GAME_BOOST_SHORTCUT, false);
        getIntent().getBooleanExtra(GlobalData.HEADER_NOTI_TRACK, false);
    }


    public void removeGameFromList(String str) {
        try {
            ArrayList arrayList = new ArrayList();
            FileInputStream openFileInput = openFileInput("games.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(openFileInput);
            arrayList.addAll((ArrayList) objectInputStream.readObject());
            for (int i = 0; i < arrayList.size(); i++) {
                if (((String) arrayList.get(i)).startsWith("" + str)) {
                    arrayList.remove(i);
                }
            }
            arrayList.remove(str);
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

    private void setDeviceDimensions() {
        DisplayMetrics displayMetrics = ParentActivity.displaymetrics;
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
    }

    private void setDimensions() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.gameGrid.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, (this.deviceHeight * 15) / 100);
        this.gameGrid.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.imgRocket.getLayoutParams();
        int i = this.deviceHeight;
        layoutParams2.height = (i * 38) / 100;
        layoutParams2.width = (this.deviceWidth * 35) / 100;
        layoutParams2.setMargins(0, 0, 0, (i * 18) / 100);
        this.imgRocket.setLayoutParams(layoutParams2);
        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) this.imgSmoke.getLayoutParams();
        layoutParams3.height = (this.deviceHeight * 9) / 100;
        layoutParams3.width = (this.deviceWidth * 98) / 100;
        this.imgSmoke.setLayoutParams(layoutParams3);
        RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) this.layout_upper.getLayoutParams();
        layoutParams4.height = (this.deviceHeight * 16) / 100;
        this.layout_upper.setLayoutParams(layoutParams4);
    }

    private void setFonts() {
        TextView textView = this.tv_ramreslese;
        textView.setTextSize(0, DetermineTextSize.determineTextSize(textView.getTypeface(), (this.deviceHeight * 7) / 100));
        TextView textView2 = this.tv_ramreslesunit;
        textView2.setTextSize(0, DetermineTextSize.determineTextSize(textView2.getTypeface(), (this.deviceHeight * 5) / 100));
    }


    public void showView() {
        this.fabfilledList.setVisibility(View.GONE);
        this.layout_status.setVisibility(View.GONE);
        this.gamePartionLayout.setVisibility(View.VISIBLE);
        this.gameGrid.setVisibility(View.GONE);
        this.fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (this.noti_result_back) {
            startActivity(new Intent(this.context, HomeActivity.class));
            finish();
        } else if (this.gameboost_shortcut) {
            finish();
        } else {
            Log.e("GAME", "SIMPLE TEST");
            finish();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_gamebosster);
        this.context = this;
        this.pm = getPackageManager();
        if (this.installedUserApps == null) {
            this.installedUserApps = new AppDetails(this.context).getInstalledUserApps();
        }
        getid();
        redirectToNoti();
        setDeviceDimensions();
        getRamSize();
        TextView textView = this.tv_ramreslese;
        textView.setText("" + Math.round((float) this.ramsaveSize) + "%");
        setDimensions();
        setFonts();
 this.gameGrid.setNumColumns(3);
        this.gameGrid.setExpanded(true);
        this.gameGrid.setAdapter((ListAdapter) new GameListAdepter(this.context, getGamesSavedList(), false));
        this.gamescanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GameBoostActivity.this.multipleClicked()) {
                }
            }
        });
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                GameBoostActivity.this.appsService();


            }
        });
        this.fabfilledList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: " + "fab list click");
                GameBoostActivity.this.appsService();


            }
        });
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        showInterstialAds(GameBoostActivity.this);



        new SharedPrefUtil(this).saveLastTimeUsed(SharedPrefUtil.LUSED_GAMEBOOST, System.currentTimeMillis());
        sendAnalytics("SCREEN_VISIT_" + getClass().getSimpleName());
    }


    @Override
    public void onResume() {
        super.onResume();
        GlobalData.SETAPPLAnguage(this);
        setDeviceDimensions();

        if (this.installedUserApps == null) {
            this.installedUserApps = new AppDetails(this.context).getInstalledUserApps();
        }
        do {
        } while (this.installedUserApps == null);
        if (this.gameGrid != null) {
            ArrayList gamesSavedList = getGamesSavedList();
            if (gamesSavedList.size() > 0) {
                this.gameGrid.setAdapter((ListAdapter) new GameListAdepter(this.context, gamesSavedList, false));
                this.gameGrid.setNumColumns(3);
                hideView();
            } else {
                showView();
            }
            try {
                getRamSize();
                TextView textView = this.tv_ramreslese;
                textView.setText("" + this.ramsaveSize);
                if (this.gameGrid.getAdapter().getCount() > 1) {
                    TextView textView2 = this.tvtext;
                    textView2.setText("" + getString(R.string.mbc_nogamesfounds));
                } else {
                    TextView textView3 = this.tvtext;
                    textView3.setText("" + getString(R.string.mbc_nogamesfound));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
