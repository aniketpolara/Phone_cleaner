package com.cleanPhone.mobileCleaner.app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.PackageInfoStruct;

public class ApplicationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private MobiClean mobiclean = MobiClean.getInstance();
    private static final int LAYOUT_ONE = 0;
    private static final int LAYOUT_TWO = 1;

    public ApplicationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applistitemview, parent, false);
        viewHolder = new MyViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder myViewHolder, int i) {
            MyViewHolder ViewHolder = (MyViewHolder) myViewHolder;
            final PackageInfoStruct packageInfoStruct = this.mobiclean.appManagerData.installedApps.get(i);
            TextView textView = ViewHolder.textName;
            textView.setText("" + packageInfoStruct.appname);
            TextView textView2 = ViewHolder.textSize;
            textView2.setText("" + packageInfoStruct.appsize);
            ViewHolder.checkbox.setClickable(false);
            ViewHolder.checkbox.setFocusable(false);
            if (packageInfoStruct.pname != null) {
                try {
                    ViewHolder.imageview.setImageDrawable(this.context.getPackageManager().getApplicationIcon(packageInfoStruct.pname));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            String str = packageInfoStruct.appname;
            if (str != null && str.contains(".")) {
                try {
                    packageInfoStruct.appname = "" + ((Object) this.context.getPackageManager().getApplicationLabel(this.context.getPackageManager().getApplicationInfo(packageInfoStruct.pname, 0)));
                } catch (PackageManager.NameNotFoundException e2) {
                    e2.printStackTrace();
                }
            }
            TextView textView3 = ViewHolder.textName;
            textView3.setText("" + packageInfoStruct.appname);
            TextView textView4 = ViewHolder.textSize;
            textView4.setText("" + Util.convertBytes(packageInfoStruct.appsize));
            ViewHolder.layout_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (packageInfoStruct.ischecked) {
                        ViewHolder.checkbox.setChecked(false);
                        ApplicationAdapter.this.mobiclean.appManagerData.unCheckInstalled(packageInfoStruct);
                        return;
                    }
                    ViewHolder.checkbox.setChecked(true);
                    packageInfoStruct.ischecked = true;
                    ApplicationAdapter.this.mobiclean.appManagerData.checkInstalled(packageInfoStruct);
                }
            });
            if (packageInfoStruct.ischecked) {
                ViewHolder.checkbox.setChecked(true);
            } else {
                ViewHolder.checkbox.setChecked(false);
            }


    }

    @Override
    public int getItemCount() {
        return this.mobiclean.appManagerData.installedApps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkbox;
        public ImageView imageview;
        public LinearLayout layout_;
        public TextView textName;
        public TextView textSize;

        public MyViewHolder(View view) {
            super(view);
            this.imageview = (ImageView) view.findViewById(R.id.appitem_image);
            this.layout_ = (LinearLayout) view.findViewById(R.id.layout_);
            this.textName = (TextView) view.findViewById(R.id.appitem_name);
            this.textSize = (TextView) view.findViewById(R.id.appitem_size);
            this.checkbox = (CheckBox) view.findViewById(R.id.appitem_check);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 9 == 8)
            return LAYOUT_ONE;
        else
            return LAYOUT_TWO;
    }
}
