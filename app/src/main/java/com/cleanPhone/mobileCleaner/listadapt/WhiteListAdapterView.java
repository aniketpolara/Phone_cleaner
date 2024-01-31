package com.cleanPhone.mobileCleaner.listadapt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codersroute.flexiblewidgets.FlexibleSwitch;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.wrappers.PackageInfoStruct;

import java.util.ArrayList;


public class WhiteListAdapterView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private ArrayList<PackageInfoStruct> dataList;
    private ArrayList<String> ignoredList;
    private static final int LAYOUT_ONE = 0;
    private static final int LAYOUT_TWO = 1;

    public class VHolder extends RecyclerView.ViewHolder {
        private ImageView img_app;
        private final FlexibleSwitch onoffBtn;
        public TextView size;
        public TextView title;

        public VHolder(View view) {
            super(view);
            WhiteListAdapterView whiteListAdapterView = new WhiteListAdapterView(context, dataList);
            setIsRecyclable(false);
            this.title = (TextView) view.findViewById(R.id.tvsocial_app_name);
            this.img_app = (ImageView) view.findViewById(R.id.img_ignoreapp);
            this.size = (TextView) view.findViewById(R.id.tvsocial_app_size);
            this.onoffBtn = (FlexibleSwitch) view.findViewById(R.id.ignore_onoff);
            try {
                whiteListAdapterView.ignoredList = (ArrayList) GlobalData.getObj(whiteListAdapterView.context, "ignored_apps");
            } catch (Exception e) {
                e.printStackTrace();
                whiteListAdapterView.ignoredList = new ArrayList();
            }
        }
    }

    public WhiteListAdapterView(Context context, ArrayList arrayList) {
        this.dataList = arrayList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, @SuppressLint("RecyclerView") final int i) {
            VHolder myViewHolder = (VHolder) vHolder;
            PackageInfoStruct packageInfoStruct = this.dataList.get(i);
            TextView textView = myViewHolder.title;
            textView.setText("" + packageInfoStruct.appname);
            myViewHolder.onoffBtn.setClickable(false);
            myViewHolder.onoffBtn.setFocusable(false);
            try {
                myViewHolder.img_app.setImageDrawable(this.context.getPackageManager().getApplicationIcon(packageInfoStruct.pname));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            myViewHolder.onoffBtn.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    try {
                        ((PackageInfoStruct) WhiteListAdapterView.this.dataList.get(i)).ignored = myViewHolder.onoffBtn.isChecked();
                        if (myViewHolder.onoffBtn.isChecked()) {
                            myViewHolder.onoffBtn.setChecked(true);
                            WhiteListAdapterView.this.ignoredList.add(((PackageInfoStruct) WhiteListAdapterView.this.dataList.get(i)).pname);
                        } else {
                            myViewHolder.onoffBtn.setChecked(false);
                            WhiteListAdapterView.this.ignoredList.remove(((PackageInfoStruct) WhiteListAdapterView.this.dataList.get(i)).pname);
                        }
                        try {
                            GlobalData.saveObj(WhiteListAdapterView.this.context, "ignored_apps", WhiteListAdapterView.this.ignoredList);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return false;
                }
            });

            if (this.dataList.get(i).ignored) {
                myViewHolder.onoffBtn.setChecked(true);
            } else {
                myViewHolder.onoffBtn.setChecked(false);
            }

    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        WhiteListAdapterView adapterView;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ignore_app_adapter, parent, false);
        viewHolder = new VHolder(view);

        return viewHolder;

    }

    @Override
    public int getItemViewType(int position) {
        if (position % 9 == 8)
            return LAYOUT_ONE;
        else
            return LAYOUT_TWO;
    }

}
