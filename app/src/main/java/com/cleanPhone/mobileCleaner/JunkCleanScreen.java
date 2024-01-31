package com.cleanPhone.mobileCleaner;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.animate.JunkData;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.JunkListWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.internal.cache.DiskLruCache;

public class JunkCleanScreen extends ParentActivity implements View.OnClickListener {
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    private static final String APP_PKG_NAME_22 = "pkg";
    public static final String BOOSTFILES = "boost";
    public static final String CACHEUSER = "usercache";
    public static final String EMPTY = "empty";
    private static final String SCHEME = "package";
    private static final String TAG = "JunkCleanScreen";
    public static final String TEMP = "temp";
    public static boolean isSysCacheSelected = false;
    private TextView ads_message;
    private TextView ads_title;
    int admob = 2;
    ImageView back;
    private boolean alreadyBoosted;
    private MobiClean app;
    private TextView btnClear;
    private LinearLayout cBoxContainer;
    private LinearLayout cbContainer;
    private CheckBox cbJunkChild;
    private Context context;
    private int deviceHeight;
    private int deviceWidth;
    private ExpandableListView expListView;
    private boolean fromChild;
    private ImageView image_arrow;
    public RelativeLayout j;
    public RelativeLayout k;
    public FrameLayout l;
    public ArrayList<String> listDataHeader;
    public TextView m;
    public TextView n;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    private boolean redirectToNoti;
    private TextView tvJunkAll;
    private TextView tvJunkAll_unit;
    private TextView tvName;
    private TextView tvSize;
    private boolean removeBoost = false;
    private boolean removeSysCache = false;
    public ArrayList<Integer> o = new ArrayList<>();

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<String> _listDataHeader;
        private PackageManager packageManager;

        public ExpandableListAdapter(Context context, List<String> list) {
            this._context = context;
            this._listDataHeader = list;
            this.packageManager = JunkCleanScreen.this.getPackageManager();
        }


        public boolean allChecked(ArrayList<JunkListWrapper> arrayList) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (!arrayList.get(i).ischecked) {
                    return false;
                }
            }
            return true;
        }

        private boolean allUnChecked(ArrayList<JunkListWrapper> arrayList) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).ischecked) {
                    return false;
                }
            }
            return true;
        }

        private Drawable getADrawable(int i) {
            if (Build.VERSION.SDK_INT >= 21) {
                return JunkCleanScreen.this.getResources().getDrawable(i, JunkCleanScreen.this.getTheme());
            }
            return JunkCleanScreen.this.getResources().getDrawable(i);
        }

        @Override
        public Object getChild(int i, int i2) {
            return JunkCleanScreen.this.app.junkData.junkDataMap.get(this._listDataHeader.get(i)).get(i2);
        }

        @Override
        public long getChildId(int i, int i2) {
            return i2;
        }

        @SuppressLint({"WrongConstant", "ClickableViewAccessibility"})
        @Override
        public View getChildView(final int i, int i2, boolean z, View view, ViewGroup viewGroup) {
            String str;
            int i3;
            int i4;
            String str2;
            final JunkListWrapper junkListWrapper = (JunkListWrapper) getChild(i, i2);
            LayoutInflater layoutInflater = (LayoutInflater) this._context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View inflate = (layoutInflater == null || view != null) ? view : layoutInflater.inflate(R.layout.junklistitemlayout, (ViewGroup) null);
            TextView textView = (TextView) inflate.findViewById(R.id.junklistitemapp);
            TextView textView2 = (TextView) inflate.findViewById(R.id.junklistitemsize);
            ImageView imageView = (ImageView) inflate.findViewById(R.id.junklistitemimage);
            final CheckBox checkBox = (CheckBox) inflate.findViewById(R.id.junklistitemcheck);
            LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.checkcontainer);
            checkBox.setFocusable(false);
            checkBox.setClickable(false);
            if (junkListWrapper.appname.equalsIgnoreCase("")) {
                textView.setText("" + junkListWrapper.pkg);
            } else {
                textView.setText("" + junkListWrapper.name);
            }
            if (JunkCleanScreen.this.removeSysCache) {
                if (i == 0) {
                    try {
                        textView.setText("" + ((Object) this.packageManager.getApplicationLabel(JunkCleanScreen.this.getPackageManager().getApplicationInfo(junkListWrapper.pkg, 0))));
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (i == 1) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    String str3 = junkListWrapper.name;
                    sb.append(str3.substring(str3.lastIndexOf(DialogConfigs.DIRECTORY_SEPERATOR) + 1));
                    textView.setText(sb.toString());
                } else if (i == 2) {
                    try {
                        PackageManager packageManager = this.packageManager;
                        str2 = packageManager.getPackageArchiveInfo("" + junkListWrapper.path, 0).applicationInfo.loadLabel(this.packageManager).toString();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        str2 = "";
                    }
                    textView.setText("" + str2);
                }
            } else if (i == 0) {
                try {
                    textView.setText("" + ((Object) this.packageManager.getApplicationLabel(JunkCleanScreen.this.getPackageManager().getApplicationInfo(junkListWrapper.pkg, 0))));
                } catch (PackageManager.NameNotFoundException e3) {
                    e3.printStackTrace();
                }
            } else if (i == 2) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                String str4 = junkListWrapper.name;
                sb2.append(str4.substring(str4.lastIndexOf(DialogConfigs.DIRECTORY_SEPERATOR) + 1));
                textView.setText(sb2.toString());
            } else if (i == 3) {
                try {
                    PackageManager packageManager2 = this.packageManager;
                    str = packageManager2.getPackageArchiveInfo("" + junkListWrapper.path, 0).applicationInfo.loadLabel(this.packageManager).toString();
                } catch (Exception e4) {
                    e4.printStackTrace();
                    str = "";
                }
                textView.setText("" + str);
            }
            textView2.setText("" + Util.convertBytes(junkListWrapper.size));
            linearLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view2, MotionEvent motionEvent) {
                    JunkCleanScreen.this.fromChild = true;
                    return false;
                }
            });
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    JunkListWrapper junkListWrapper2 = junkListWrapper;
                    if (junkListWrapper2.ischecked) {
                        junkListWrapper2.ischecked = false;
                        checkBox.setChecked(false);
                        if (!JunkCleanScreen.this.removeSysCache) {
                            if (i == 4) {
                                JunkCleanScreen.this.app.junkData.totalfolders--;
                            }
                        } else if (i == 3) {
                            JunkCleanScreen.this.app.junkData.totalfolders--;
                        }
                        JunkCleanScreen.this.setTextOnTop(junkListWrapper.size, false);
                    } else {
                        checkBox.setChecked(true);
                        junkListWrapper.ischecked = true;
                        if (!JunkCleanScreen.this.removeSysCache) {
                            if (i == 4) {
                                JunkCleanScreen.this.app.junkData.totalfolders++;
                            }
                        } else if (i == 3) {
                            JunkCleanScreen.this.app.junkData.totalfolders++;
                        }
                        JunkCleanScreen.this.setTextOnTop(junkListWrapper.size, true);
                    }
                    if (checkBox.isChecked()) {
                        ExpandableListAdapter expandableListAdapter = ExpandableListAdapter.this;
                        if (!expandableListAdapter.allChecked(JunkCleanScreen.this.app.junkData.junkDataMap.get(JunkCleanScreen.this.listDataHeader.get(i)))) {
                            JunkCleanScreen.this.app.junkData.totalSelectedCategories--;
                            JunkCleanScreen.this.o.remove(Integer.valueOf(i));
                            Log.d("CHECKKK", JunkCleanScreen.this.o.toString());
                            if (i == 5) {
                                JunkCleanScreen.this.app.junkData.boost_checked = false;
                            }
                        } else {
                            Log.d("ALLCHECKED", "all checked " + i);
                            JunkCleanScreen.this.app.junkData.totalSelectedCategories = JunkCleanScreen.this.app.junkData.totalSelectedCategories + 1;
                            if (!JunkCleanScreen.this.o.contains(Integer.valueOf(i))) {
                                JunkCleanScreen.this.o.add(Integer.valueOf(i));
                            }
                            if (i == 5) {
                                JunkCleanScreen.this.app.junkData.boost_checked = true;
                            }
                        }
                    } else {
                        JunkCleanScreen.this.o.remove(Integer.valueOf(i));
                        Log.d("CHECKKK2", JunkCleanScreen.this.o.toString());
                    }
                    ExpandableListAdapter.this.notifyDataSetChanged();
                }
            });
            if (JunkCleanScreen.this.removeSysCache) {
                if (i == 1) {
                    imageView.setImageDrawable(getADrawable(R.drawable.temp_files));
                } else if (i == 3) {
                    imageView.setImageDrawable(getADrawable(R.drawable.emp_folder));
                } else if (i != 4) {
                    try {
                        imageView.setImageDrawable(JunkCleanScreen.this.context.getPackageManager().getApplicationIcon(junkListWrapper.pkg));
                    } catch (Exception e5) {
                        e5.printStackTrace();
                        imageView.setImageDrawable(ContextCompat.getDrawable(JunkCleanScreen.this.context, R.drawable.apk_files));
                    }
                } else {
                    imageView.setImageDrawable(getADrawable(R.drawable.memory_booster));
                }
                if (i == 4) {
                    i3 = 8;
                    checkBox.setVisibility(View.GONE);
                } else {
                    i3 = 8;
                    checkBox.setVisibility(View.VISIBLE);
                }
                if (i == 3) {
                    textView2.setVisibility(i3);
                } else {
                    textView2.setVisibility(View.VISIBLE);
                }
            } else {
                if (i == 2) {
                    imageView.setImageDrawable(getADrawable(R.drawable.temp_files));
                } else if (i == 4) {
                    imageView.setImageDrawable(getADrawable(R.drawable.emp_folder));
                } else if (i != 5) {
                    try {
                        imageView.setImageDrawable(JunkCleanScreen.this.context.getPackageManager().getApplicationIcon(junkListWrapper.pkg));
                    } catch (Exception e6) {
                        imageView.setImageDrawable(getADrawable(R.drawable.apk_files));
                        e6.printStackTrace();
                    }
                } else if (Build.VERSION.SDK_INT <= 23) {
                    try {
                        imageView.setImageDrawable(JunkCleanScreen.this.context.getPackageManager().getApplicationIcon(junkListWrapper.pkg));
                    } catch (Exception e7) {
                        imageView.setImageDrawable(getADrawable(R.drawable.ic_android));
                        e7.printStackTrace();
                    }
                } else {
                    imageView.setImageDrawable(getADrawable(R.drawable.ic_android));
                }
                if (i != 1 && i != 5) {
                    checkBox.setVisibility(0);
                    i4 = 8;
                } else {
                    i4 = 8;
                    checkBox.setVisibility(8);
                }
                if (i == 4) {
                    textView2.setVisibility(i4);
                } else {
                    textView2.setVisibility(0);
                }
            }
            inflate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    JunkCleanScreen.this.perFormClick(junkListWrapper);
                }
            });
            checkBox.setChecked(junkListWrapper.ischecked);
            return inflate;
        }

        @Override
        public int getChildrenCount(int i) {
            try {
                return JunkCleanScreen.this.app.junkData.junkDataMap.get(this._listDataHeader.get(i)).size();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }

        @Override
        public Object getGroup(int i) {
            return this._listDataHeader.get(i);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public View getGroupView(final int i, boolean z, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater;
            String str = (String) getGroup(i);
            CheckBox checkBox = null;
            if (view == null && (layoutInflater = (LayoutInflater) this._context.getSystemService(LAYOUT_INFLATER_SERVICE)) != null) {
                view = layoutInflater.inflate(R.layout.list_header, (ViewGroup) null);
            }
            if (view != null) {
                JunkCleanScreen.this.image_arrow = (ImageView) view.findViewById(R.id.rightarrow_junk);
                JunkCleanScreen.this.tvName = (TextView) view.findViewById(R.id.tvjunkname);
                JunkCleanScreen.this.tvSize = (TextView) view.findViewById(R.id.tvjunksize);
                checkBox = (CheckBox) view.findViewById(R.id.junk_check);
                JunkCleanScreen.this.cBoxContainer = (LinearLayout) view.findViewById(R.id.grp_checkcontainer);
            }
            if (checkBox != null) {
                checkBox.setClickable(false);
                checkBox.setFocusable(false);
            }
            if (z) {
                JunkCleanScreen.this.image_arrow.animate().rotation(90.0f).start();
            } else {
                JunkCleanScreen.this.image_arrow.animate().rotation(0.0f).start();
            }
            TextView textView = JunkCleanScreen.this.tvName;
            textView.setText("" + str);
            if (JunkCleanScreen.this.removeSysCache) {
                if (i == 0) {
                    JunkCleanScreen.this.tvSize.setText(Util.convertBytes(JunkCleanScreen.this.app.junkData.SYS_CACHE_SIZE));
                } else if (i == 1) {
                    JunkCleanScreen.this.tvSize.setText(Util.convertBytes(JunkCleanScreen.this.app.junkData.TEMP_CACHE_SIZE));
                } else if (i == 2) {
                    TextView textView2 = JunkCleanScreen.this.tvSize;
                    textView2.setText("" + Util.convertBytes(JunkCleanScreen.this.app.junkData.APK_CACHE_SIZE));
                } else if (i == 3) {
                    TextView textView3 = JunkCleanScreen.this.tvSize;
                    textView3.setText("" + JunkCleanScreen.this.app.junkData.junkDataMap.get(JunkCleanScreen.this.listDataHeader.get(i)).size());
                } else if (i == 4) {
                    JunkCleanScreen.this.tvSize.setText(Util.convertBytes(JunkCleanScreen.this.app.junkData.BOOST_CACHE_SIZE));
                }
            } else if (i == 0) {
                JunkCleanScreen.this.tvSize.setText(Util.convertBytes(JunkCleanScreen.this.app.junkData.SYS_CACHE_SIZE));
            } else if (i == 1) {
                JunkCleanScreen.this.tvSize.setText(Util.convertBytes(JunkCleanScreen.this.app.junkData.APP_CACHE_SIZE));
            } else if (i == 2) {
                JunkCleanScreen.this.tvSize.setText(Util.convertBytes(JunkCleanScreen.this.app.junkData.TEMP_CACHE_SIZE));
            } else if (i == 3) {
                JunkCleanScreen.this.tvSize.setText(Util.convertBytes(JunkCleanScreen.this.app.junkData.APK_CACHE_SIZE));
            } else if (i == 4) {
                TextView textView4 = JunkCleanScreen.this.tvSize;
                textView4.setText("" + JunkCleanScreen.this.app.junkData.junkDataMap.get(JunkCleanScreen.this.listDataHeader.get(i)).size());
            } else if (i == 5) {
                JunkCleanScreen.this.tvSize.setText(Util.convertBytes(JunkCleanScreen.this.app.junkData.BOOST_CACHE_SIZE));
            }
            CheckBox finalCheckBox = checkBox;
            JunkCleanScreen.this.cBoxContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    CheckBox checkBox2 = finalCheckBox;
                    if (checkBox2 != null) {
                        if (checkBox2.isChecked()) {
                            finalCheckBox.setChecked(false);
                            JunkCleanScreen.this.app.junkData.totalSelectedCategories--;
                            if (JunkCleanScreen.this.removeSysCache) {
                                int i2 = i;
                                if (i2 == 0) {
                                    JunkCleanScreen.this.app.junkData.sys_checked = false;
                                } else if (i2 == 1) {
                                    JunkCleanScreen.this.app.junkData.temp_checked = false;
                                } else if (i2 == 2) {
                                    JunkCleanScreen.this.app.junkData.apk_checked = false;
                                } else if (i2 == 3) {
                                    JunkCleanScreen.this.app.junkData.emptyfolder_checked = false;
                                } else if (i2 == 4) {
                                    JunkCleanScreen.this.app.junkData.boost_checked = false;
                                }
                            } else {
                                int i3 = i;
                                if (i3 == 0) {
                                    JunkCleanScreen.this.app.junkData.sys_checked = false;
                                } else if (i3 == 1) {
                                    JunkCleanScreen.this.app.junkData.cache_checked = false;
                                } else if (i3 == 2) {
                                    JunkCleanScreen.this.app.junkData.temp_checked = false;
                                } else if (i3 == 3) {
                                    JunkCleanScreen.this.app.junkData.apk_checked = false;
                                } else if (i3 == 4) {
                                    JunkCleanScreen.this.app.junkData.emptyfolder_checked = false;
                                } else if (i3 == 5) {
                                    JunkCleanScreen.this.app.junkData.boost_checked = false;
                                }
                            }
                            JunkCleanScreen.this.o.remove(Integer.valueOf(i));
                        } else {
                            finalCheckBox.setChecked(true);
                            JunkCleanScreen.this.app.junkData.totalSelectedCategories++;
                            if (JunkCleanScreen.this.removeSysCache) {
                                int i4 = i;
                                if (i4 == 0) {
                                    JunkCleanScreen.this.app.junkData.sys_checked = true;
                                } else if (i4 == 1) {
                                    JunkCleanScreen.this.app.junkData.temp_checked = true;
                                } else if (i4 == 2) {
                                    JunkCleanScreen.this.app.junkData.apk_checked = true;
                                } else if (i4 == 3) {
                                    JunkCleanScreen.this.app.junkData.emptyfolder_checked = true;
                                } else if (i4 == 4) {
                                    JunkCleanScreen.this.app.junkData.boost_checked = true;
                                } else if (i4 == 5) {
                                    JunkCleanScreen.this.app.junkData.boost_checked = true;
                                }
                            } else {
                                int i5 = i;
                                if (i5 == 0) {
                                    JunkCleanScreen.this.app.junkData.sys_checked = true;
                                } else if (i5 == 1) {
                                    JunkCleanScreen.this.app.junkData.cache_checked = true;
                                } else if (i5 == 2) {
                                    JunkCleanScreen.this.app.junkData.temp_checked = true;
                                } else if (i5 == 3) {
                                    JunkCleanScreen.this.app.junkData.apk_checked = true;
                                } else if (i5 == 4) {
                                    JunkCleanScreen.this.app.junkData.emptyfolder_checked = true;
                                } else if (i5 == 5) {
                                    JunkCleanScreen.this.app.junkData.boost_checked = true;
                                }
                            }
                            if (!JunkCleanScreen.this.o.contains(Integer.valueOf(i))) {
                                JunkCleanScreen.this.o.add(Integer.valueOf(i));
                            }
                        }
                    }
                    try {
                        if (!JunkCleanScreen.this.fromChild && JunkCleanScreen.this.app.junkData.junkDataMap != null) {
                            for (int i6 = 0; i6 < JunkCleanScreen.this.app.junkData.junkDataMap.get(JunkCleanScreen.this.listDataHeader.get(i)).size(); i6++) {
                                if (finalCheckBox != null && JunkCleanScreen.this.app.junkData.junkDataMap.get(JunkCleanScreen.this.listDataHeader.get(i)).get(i6).ischecked != finalCheckBox.isChecked()) {
                                    if (JunkCleanScreen.this.removeSysCache) {
                                        if (i == 3) {
                                            if (finalCheckBox.isChecked()) {
                                                JunkCleanScreen.this.app.junkData.totalfolders++;
                                            } else {
                                                JunkCleanScreen.this.app.junkData.totalfolders--;
                                            }
                                        }
                                    } else if (i == 4) {
                                        if (finalCheckBox.isChecked()) {
                                            JunkCleanScreen.this.app.junkData.totalfolders++;
                                        } else {
                                            JunkCleanScreen.this.app.junkData.totalfolders--;
                                        }
                                    }
                                    Log.d("IS2", "here");
                                    JunkCleanScreen.this.app.junkData.junkDataMap.get(JunkCleanScreen.this.listDataHeader.get(i)).get(i6).ischecked = finalCheckBox.isChecked();
                                    JunkCleanScreen junkCleanScreen = JunkCleanScreen.this;
                                    junkCleanScreen.setTextOnTop(junkCleanScreen.app.junkData.junkDataMap.get(JunkCleanScreen.this.listDataHeader.get(i)).get(i6).size, JunkCleanScreen.this.app.junkData.junkDataMap.get(JunkCleanScreen.this.listDataHeader.get(i)).get(i6).ischecked);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ExpandableListAdapter.this.notifyDataSetChanged();
                }
            });
            checkBox.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view2, MotionEvent motionEvent) {
                    JunkCleanScreen.this.fromChild = false;
                    return false;
                }
            });
            Log.d("CHECKKKG", JunkCleanScreen.this.o.toString());
            if (JunkCleanScreen.this.o.contains(Integer.valueOf(i))) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            return view;
        }

        @Override // android.widget.ExpandableListAdapter
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int i, int i2) {
            return true;
        }
    }

    private void chekSizeOfModule(ArrayList<JunkListWrapper> arrayList, int i) {
        if (Build.VERSION.SDK_INT > 19) {
            if (i == 0) {
                this.app.junkData.SYS_CACHE_SIZE = getPerticulerJunkSize(arrayList);
            } else if (i == 1) {
                this.app.junkData.TEMP_CACHE_SIZE = getPerticulerJunkSize(arrayList);
            } else if (i == 2) {
                this.app.junkData.APK_CACHE_SIZE = getPerticulerJunkSize(arrayList);
            } else if (i == 3) {
                this.app.junkData.EMPTY_CACHE_SIZE = getPerticulerJunkSize(arrayList);
            } else if (i != 4) {
            } else {
                this.app.junkData.BOOST_CACHE_SIZE = getPerticulerJunkSize(arrayList);
            }
        } else if (i == 0) {
            this.app.junkData.SYS_CACHE_SIZE = getPerticulerJunkSize(arrayList);
        } else if (i == 1) {
            this.app.junkData.APP_CACHE_SIZE = getPerticulerJunkSize(arrayList);
        } else if (i == 2) {
            this.app.junkData.TEMP_CACHE_SIZE = getPerticulerJunkSize(arrayList);
        } else if (i == 3) {
            this.app.junkData.APK_CACHE_SIZE = getPerticulerJunkSize(arrayList);
        } else if (i == 4) {
            this.app.junkData.EMPTY_CACHE_SIZE = getPerticulerJunkSize(arrayList);
        } else if (i != 5) {
        } else {
            this.app.junkData.BOOST_CACHE_SIZE = getPerticulerJunkSize(arrayList);
        }
    }


    public void deleteJunk() {
        this.app.junkData.allChecked = isAllChecked();
            startActivity(new Intent(JunkCleanScreen.this, JunkCleaningScreen.class));

    }


    private long getPerticulerJunkSize(ArrayList<JunkListWrapper> arrayList) {
        long j = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            j += arrayList.get(i).size;
        }
        return j;
    }

    private ArrayList<String> getStringFromPath(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        File file = new File(str);
        String[] list = file.list();
        if (file.isDirectory() && list != null) {
            for (String str2 : list) {
                if (str2.endsWith("log") || str2.toLowerCase().endsWith("xlog") || str2.toLowerCase().equalsIgnoreCase("log") || str2.toLowerCase().endsWith("tmp") || str2.toLowerCase().endsWith("dat") || str2.toLowerCase().endsWith(DiskLruCache.JOURNAL_FILE) || str2.toLowerCase().endsWith("cuid") || str2.toLowerCase().endsWith("bat") || str2.toLowerCase().endsWith("dk") || str2.toLowerCase().endsWith("xml") || str2.toLowerCase().endsWith("nomedia") || str2.toLowerCase().endsWith("bin") || str2.toLowerCase().endsWith("js") || str2.toLowerCase().endsWith("css") || str2.toLowerCase().endsWith("file") || str2.toLowerCase().endsWith("idx")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(str);
                    sb.append(DialogConfigs.DIRECTORY_SEPERATOR);
                    sb.append(str2);
                    sb.append("(");
                    sb.append(Util.convertBytes(new File(str + DialogConfigs.DIRECTORY_SEPERATOR + str2).length()));
                    sb.append(")");
                    arrayList.add(sb.toString());
                }
            }
        }
        return arrayList;
    }

    private void init() {
        this.context = this;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressLint("WrongConstant") WindowManager windowManager = (WindowManager) getSystemService("window");
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        back = findViewById(R.id.iv_back);
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
        this.k = (RelativeLayout) findViewById(R.id.layout_one);
        this.j = (RelativeLayout) findViewById(R.id.layout_two);
        this.l = (FrameLayout) findViewById(R.id.frame_mid_laysss);
        this.m = (TextView) findViewById(R.id.ads_btn_countinue);
        this.n = (TextView) findViewById(R.id.ads_btn_cancel);
        this.ads_title = (TextView) findViewById(R.id.dialog_title);
        this.ads_message = (TextView) findViewById(R.id.dialog_msg);
        this.n.setOnClickListener(this);
        this.m.setOnClickListener(this);
        this.btnClear = (TextView) findViewById(R.id.junkclean_btn);
        this.expListView = (ExpandableListView) findViewById(R.id.junk_exp_lv);
        this.tvJunkAll = (TextView) findViewById(R.id.junkdisplay_sizetv);
        this.tvJunkAll_unit = (TextView) findViewById(R.id.junkdisplay_sizetv_unit);
        TextView textView = (TextView) findViewById(R.id.junkdisplay_status);
        this.o.add(0);
        this.o.add(1);
        this.o.add(3);
        this.o.add(4);
    }

    private boolean isAllChecked() {
        JunkData junkData = this.app.junkData;
        return junkData.emptyfolder_checked && junkData.sys_checked && junkData.cache_checked && junkData.temp_checked && junkData.boost_checked;
    }

    @SuppressLint("ResourceType")
    public void perFormClick(JunkListWrapper junkListWrapper) {
        if (junkListWrapper.type.equalsIgnoreCase(JunkScanActivity.SYSTEMCACHE)) {
            Intent intent = new Intent();
            int i = Build.VERSION.SDK_INT;
            if (i >= 9) {
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts(SCHEME, junkListWrapper.pkg, null));
            } else {
                String str = i == 8 ? APP_PKG_NAME_22 : APP_PKG_NAME_21;
                intent.setAction("android.intent.action.VIEW");
                intent.setClassName(APP_DETAILS_PACKAGE_NAME, APP_DETAILS_CLASS_NAME);
                intent.putExtra(str, junkListWrapper.pkg);
            }
            try {
                this.context.startActivity(intent);
                if (i >= 23) {
                    Toast.makeText(this.context, getResources().getString(R.string.mbc_tap_storage), 0).show();
                } else {
                    Toast.makeText(this.context, getResources().getString(R.string.mbc_tap_storage), 0).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (junkListWrapper.type.equalsIgnoreCase(CACHEUSER)) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(1);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            dialog.setContentView(R.layout.dialog_junk_list);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            RelativeLayout relativeLayout = (RelativeLayout) dialog.findViewById(R.id.main_head);
            ListView listView = (ListView) dialog.findViewById(R.id.listjunkfiles);
            try {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(relativeLayout.getLayoutParams());
                int i2 = this.deviceHeight;
                layoutParams.setMargins(0, (i2 * 20) / 100, 0, (i2 * 20) / 100);
                relativeLayout.setLayoutParams(layoutParams);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            dialog.findViewById(R.id.okbtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            listView.setAdapter((ListAdapter) new ArrayAdapter(this, (int) R.layout.junk_dialog_path, junkListWrapper.fileslist));
            dialog.show();
        } else if (junkListWrapper.type.equalsIgnoreCase(TEMP)) {
            ArrayList<String> stringFromPath = getStringFromPath(junkListWrapper.name + "");
            final Dialog dialog2 = new Dialog(this);
            dialog2.requestWindowFeature(1);
            if (dialog2.getWindow() != null) {
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            dialog2.setContentView(R.layout.dialog_junk_list);
            dialog2.setCancelable(true);
            dialog2.setCanceledOnTouchOutside(true);
            RelativeLayout relativeLayout2 = (RelativeLayout) dialog2.findViewById(R.id.main_head);
            ListView listView2 = (ListView) dialog2.findViewById(R.id.listjunkfiles);
            try {
                FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(relativeLayout2.getLayoutParams());
                int i3 = this.deviceHeight;
                layoutParams2.setMargins(0, (i3 * 20) / 100, 0, (i3 * 20) / 100);
                relativeLayout2.setLayoutParams(layoutParams2);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            ((LinearLayout) dialog2.findViewById(R.id.okbtn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog2.dismiss();
                }
            });
            listView2.setAdapter((ListAdapter) new ArrayAdapter(this, 17367043, stringFromPath));
            dialog2.show();
        } else {
            final Dialog dialog3 = new Dialog(this);
            if (dialog3.getWindow() != null) {
                dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog3.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
            }
            dialog3.setContentView(R.layout.new_dialog_junk_cancel);
            dialog3.setCancelable(true);
            dialog3.setCanceledOnTouchOutside(true);
            dialog3.getWindow().setLayout(-1, -1);
            dialog3.getWindow().setGravity(17);


            dialog3.findViewById(R.id.dialog_img).setVisibility(8);
            dialog3.findViewById(R.id.dialog_title).setVisibility(8);
            if (junkListWrapper.path != null) {
                ((TextView) dialog3.findViewById(R.id.dialog_msg)).setText("" + getResources().getString(R.string.mbc_name) + " " + junkListWrapper.name + "\n\n" + getResources().getString(R.string.mbc_path) + " " + junkListWrapper.path + "\n\n" + getResources().getString(R.string.mbc_size) + " " + Util.convertBytes(junkListWrapper.size));
            } else {
                ((TextView) dialog3.findViewById(R.id.dialog_msg)).setText("" + getResources().getString(R.string.mbc_name) + " " + junkListWrapper.name + "\n\n" + getResources().getString(R.string.mbc_size) + " " + Util.convertBytes(junkListWrapper.size));
            }
            dialog3.findViewById(R.id.ll_yes_no).setVisibility(8);
            dialog3.findViewById(R.id.ll_ok).setVisibility(0);
            dialog3.findViewById(R.id.ll_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (JunkCleanScreen.this.multipleClicked()) {
                        return;
                    }
                    dialog3.dismiss();
                }
            });
            dialog3.show();
        }
    }

    private void redirectToNoti() {
        this.redirectToNoti = getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false);
    }

    private void setKeysList() {
        String[] stringArray;
        if (Build.VERSION.SDK_INT > 19) {
            stringArray = getResources().getStringArray(R.array.junkmodulesaboven);
            if (this.alreadyBoosted) {
                stringArray = getResources().getStringArray(R.array.junkmodulesabovenlessboost);
            }
        } else {
            stringArray = getResources().getStringArray(R.array.junkmodules);
            if (this.alreadyBoosted) {
                stringArray = getResources().getStringArray(R.array.junkmodulesaboveseven);
            }
        }
        ArrayList<String> arrayList = new ArrayList<>();
        this.listDataHeader = arrayList;
        arrayList.clear();
        Collections.addAll(this.listDataHeader, stringArray);
    }


    public void setTextOnTop(long j, boolean z) {
        if (z) {
            JunkData junkData = this.app.junkData;
            junkData.totalsize += j;
            junkData.totalcount++;
        } else {
            JunkData junkData2 = this.app.junkData;
            junkData2.totalsize -= j;
            junkData2.totalcount--;
        }
        JunkData junkData3 = this.app.junkData;
        if (junkData3.totalsize < 0) {
            junkData3.totalsize = 0L;
        }
        if (junkData3.totalcount < 0) {
            junkData3.totalcount = 0;
        }
        Log.d("SIZEEEE", "" + j + "    " + this.app.junkData.totalsize);
        JunkData junkData4 = this.app.junkData;
        if (junkData4.totalsize > 0) {
            Log.e(TAG, "setTextOnTop: " + "First SetText");
            this.btnClear.setText(String.format(getResources().getString(R.string.mbc_after_upgrade_msg2).replace("DO_NOT_TRANSLATE", "%s"), Util.convertBytes(this.app.junkData.totalsize)));
            return;
        }
        int i = junkData4.totalfolders;
        if (i <= 0) {
            Log.e(TAG, "setTextOnTop: " + "Second SetText");
            this.btnClear.setText(getString(R.string.mbc_select_item));
        } else if (i == 1) {
            Log.e(TAG, "setTextOnTop: " + "Third SetText");
            String string = getResources().getString(R.string.mbc_cleanonefolder);
            this.btnClear.setText("" + string);
        } else {
            Log.e(TAG, "setTextOnTop: " + "last Set Text");
            this.btnClear.setText(String.format(getResources().getString(R.string.mbc_after_upgrade_msg).replace("DO_NOT_TRANSLATE", "%d"), Integer.valueOf(this.app.junkData.totalfolders)));
        }
    }


    @Override
    public void onBackPressed() {
        Util.appendLogmobiclean(TAG, " backpressed ", "");
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
        }
        dialog.setContentView(R.layout.new_dialog_junk_cancel);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(-1, -1);
        dialog.getWindow().setGravity(17);

        ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_junk_cleaner));
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_junk_title));
        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(String.format(getResources().getString(R.string.mbc_simple_back_press), getResources().getString(R.string.mbc_junk_result_txt)));
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (JunkCleanScreen.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (JunkCleanScreen.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
                Util.appendLogmobiclean(JunkCleanScreen.TAG, " backpressed pDialog finish", "");
                if (JunkCleanScreen.this.redirectToNoti) {
                    Intent intent = new Intent(JunkCleanScreen.this.context, HomeActivity.class);
                    JunkCleanScreen.this.finish();
                    JunkCleanScreen.this.startActivity(intent);
                    return;
                }
                JunkCleanScreen.this.finish();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ads_btn_cancel) {
            try {
                RelativeLayout relativeLayout = this.j;
                if (relativeLayout != null && relativeLayout.getVisibility() == View.VISIBLE) {
                    this.k.setVisibility(View.VISIBLE);
                    this.j.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (view.getId() == R.id.ads_btn_countinue) {
            this.j.setVisibility(View.GONE);
            this.k.setVisibility(View.VISIBLE);
            if (this.redirectToNoti) {
                startActivity(new Intent(this.context, HomeActivity.class));
            }
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        Util.saveScreen(getClass().getName(), this);
        getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
        setContentView(R.layout.activity_junk_clean_screen);
        init();

        showInterstialAds(JunkCleanScreen.this);

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

        Util.appendLogmobiclean(TAG, " onCreate ", "");
        MobiClean mobiClean = MobiClean.getInstance();
        this.app = mobiClean;
        if (mobiClean.junkData == null) {
            return;
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        boolean booleanExtra = getIntent().getBooleanExtra("BOOSTED", true);
        this.alreadyBoosted = booleanExtra;
        if (booleanExtra) {
            this.removeBoost = true;
        }
        this.removeSysCache = true;
        redirectToNoti();
        setKeysList();
        JunkData junkData = this.app.junkData;
        junkData.totalcount = 0;
        long j = 0;
        junkData.totalsize = 0L;
        junkData.totalSelectedCategories = 5;
        if (this.removeBoost) {
            junkData.totalSelectedCategories = 4;
        }
        int i = 3;
        if (this.removeSysCache) {
            junkData.totalSelectedCategories = 3;
        }
        for (int i2 = 0; i2 < this.app.junkData.junkDataMap.size(); i2++) {
            try {
                ArrayList<JunkListWrapper> arrayList = this.app.junkData.junkDataMap.get(this.listDataHeader.get(i2));
                if (arrayList != null && arrayList.size() > 0) {
                    try {
                        Collections.sort(arrayList, new Comparator<JunkListWrapper>() {
                            @Override
                            public int compare(JunkListWrapper junkListWrapper, JunkListWrapper junkListWrapper2) {
                                return Long.compare(junkListWrapper2.size, junkListWrapper.size);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IndexOutOfBoundsException unused) {
            }
        }
        Util.appendLogmobiclean(TAG, " before ExpandableListAdapter set", "");
        this.expListView.setAdapter(new ExpandableListAdapter(this, this.listDataHeader));
        Util.appendLogmobiclean(TAG, " After ExpandableListAdapter set", "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JunkCleanScreen.this.expListView.expandGroup(0);
            }
        }, 1500L);
        int i3 = 0;
        while (i3 < this.app.junkData.junkDataMap.size()) {
            try {
                ArrayList<JunkListWrapper> arrayList2 = this.app.junkData.junkDataMap.get(this.listDataHeader.get(i3));
                if (arrayList2 != null) {
                    for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                        if (arrayList2.get(i4).ischecked) {
                            JunkData junkData2 = this.app.junkData;
                            junkData2.totalcount++;
                            junkData2.totalsize += arrayList2.get(i4).size;
                        }
                        this.app.junkData.totaljunkcount++;
                    }
                }
                if (this.removeSysCache) {
                    if (i3 == 3 && arrayList2 != null) {
                        this.app.junkData.totalfolders = arrayList2.size();
                    }
                } else if (i3 == 4 && arrayList2 != null) {
                    this.app.junkData.totalfolders = arrayList2.size();
                }
                chekSizeOfModule(arrayList2, i3);
                i3++;
                j = 0;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        setTextOnTop(j, true);
        int i5 = 0;
        while (true) {
            if (i5 >= this.listDataHeader.size()) {
                break;
            }
            if (this.listDataHeader.get(i5).equalsIgnoreCase("" + getString(R.string.mbc_apk_file))) {
                i = i5;
                break;
            }
            i5++;
        }
        JunkData junkData3 = this.app.junkData;
        long perticulerJunkSize = junkData3.totalsize + getPerticulerJunkSize(junkData3.junkDataMap.get(this.listDataHeader.get(i)));
        String convertBytes_unit = Util.convertBytes_unit(perticulerJunkSize);
        String convertBytes_only = Util.convertBytes_only(perticulerJunkSize);
        Log.e(TAG, "onCreate: unittttttttttt " + convertBytes_unit);
        Log.e(TAG, "onCreate:onlyyyyyyyy+" + convertBytes_only);
        this.tvJunkAll.setText("" + convertBytes_only);
        this.tvJunkAll_unit.setText("" + convertBytes_unit);
        this.app.junkData.totalcount--;
        btnClear = findViewById(R.id.junkclean_btn);
        this.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String charSequence = JunkCleanScreen.this.btnClear.getText().toString();
                boolean z = false;
                if (charSequence.equalsIgnoreCase("Clean Junk " + Util.convertBytes(0L))) {
                    Log.e(TAG, "onClick: if------------------------------------");
                    Context context = JunkCleanScreen.this.context;
                    Toast.makeText(context, "" + JunkCleanScreen.this.getResources().getString(R.string.mbc_nothindelete), Toast.LENGTH_SHORT).show();
                } else if (JunkCleanScreen.this.app.junkData.totalcount > 0) {
                    if (JunkCleanScreen.this.app.junkData.totalSelectedCategories != 1 || !JunkCleanScreen.this.app.junkData.cache_checked || Build.VERSION.SDK_INT < 23) {
                        if (JunkCleanScreen.this.app.junkData.cache_checked && Build.VERSION.SDK_INT >= 23 && !JunkCleanScreen.this.app.junkData.sys_checked) {
                            z = true;
                            Log.e(TAG, "onClick: else if   2-===========");
                        }
                        GlobalData.cacheCheckedAboveMarshmallow = z;
                        JunkCleanScreen.this.deleteJunk();
                        Log.e(TAG, "onClick:  delete junk ----------------");
                        return;
                    }
                    Log.e(TAG, "onClick:  delete junk 2222222");
                    GlobalData.cacheCheckedAboveMarshmallow = z;
                    JunkCleanScreen.this.deleteJunk();
                    JunkCleanScreen.isSysCacheSelected = true;


                        Intent intent = new Intent(JunkCleanScreen.this, CommonResultActivity.class);
                        intent.putExtra("DATA", "" + JunkCleanScreen.this.getResources().getString(R.string.mbc_cleaning_aborted));
                        intent.putExtra("TYPE", "JUNK");
                        intent.putExtra(GlobalData.REDIRECTNOTI, true);
                        JunkCleanScreen.this.finish();
                        JunkCleanScreen.this.startActivity(intent);



                } else if (JunkCleanScreen.this.app.junkData.totalSelectedCategories > 0) {
                    Context context2 = JunkCleanScreen.this.context;
                    Toast.makeText(context2, "" + JunkCleanScreen.this.getResources().getString(R.string.mbc_nothindelete), Toast.LENGTH_SHORT).show();
                } else {
                    Context context3 = JunkCleanScreen.this.context;
                    Toast.makeText(context3, "" + JunkCleanScreen.this.getResources().getString(R.string.mbc_pleaseselect), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Util.appendLogmobiclean(TAG, " onCreate ends", "");
    }

    @Override
    public void onDestroy() {
        this.listDataHeader = null;
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
