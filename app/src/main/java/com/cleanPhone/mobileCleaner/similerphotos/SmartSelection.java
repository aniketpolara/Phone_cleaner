package com.cleanPhone.mobileCleaner.similerphotos;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.SelectionFilter;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.TreeMap;

public class SmartSelection extends ParentActivity {
    private String[] descriptionArr;
    private CheckBox doNotShowChk;
    private DragLinearLayout dragLinearLayout;
    private String[] firstRadioArr;
    public LinearLayout i;
    private LinearLayout mark;
    private String[] secondRadioArr;
    private Map<Integer, SelectionFilter> selectionMap = new TreeMap();
    private String[] titleArr;

    public int getNoteIndex(View view) {
        Object tag = view.getTag();
        if (tag == null) {
            return -1;
        }
        return ((Integer) tag).intValue();
    }

    private boolean isAtLeastOneCheck() {
        for (Map.Entry<Integer, SelectionFilter> entry : this.selectionMap.entrySet()) {
            if (((CheckBox) entry.getValue().getView().findViewById(R.id.chk)).isChecked()) {
                return true;
            }
        }
        return false;
    }

    public void markClickable() {
        if (!isAtLeastOneCheck()) {
            this.mark.setAlpha(0.2f);
            this.mark.setEnabled(false);
            return;
        }
        this.mark.setAlpha(1.0f);
        this.mark.setEnabled(true);
    }

    private void populateViews(int i, SelectionFilter selectionFilter) {
        View inflate = View.inflate(this, R.layout.list_item_note, null);
        setNoteIndex(inflate, i);
        final CheckBox checkBox = (CheckBox) inflate.findViewById(R.id.chk);
        TextView textView = (TextView) inflate.findViewById(R.id.title);
        final RadioButton radioButton = (RadioButton) inflate.findViewById(R.id.first_radio);
        final RadioButton radioButton2 = (RadioButton) inflate.findViewById(R.id.second_radio);
        checkBox.setTag(Integer.valueOf(i));
        textView.setTag(Integer.valueOf(i));
        radioButton.setTag(Integer.valueOf(i));
        radioButton2.setTag(Integer.valueOf(i));
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int intValue = ((Integer) checkBox.getTag()).intValue();
                SmartSelection.this.updateMap(intValue, ((SelectionFilter) SmartSelection.this.selectionMap.get(Integer.valueOf(intValue))).getView());
                SmartSelection.this.markClickable();
            }
        });
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radioButton3 = radioButton;
                radioButton3.setChecked(radioButton3.isChecked());
                int intValue = ((Integer) checkBox.getTag()).intValue();
                SmartSelection.this.updateMap(intValue, ((SelectionFilter) SmartSelection.this.selectionMap.get(Integer.valueOf(intValue))).getView());
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radioButton3 = radioButton2;
                radioButton3.setChecked(radioButton3.isChecked());
                int intValue = ((Integer) checkBox.getTag()).intValue();
                SmartSelection.this.updateMap(intValue, ((SelectionFilter) SmartSelection.this.selectionMap.get(Integer.valueOf(intValue))).getView());
            }
        });
        textView.setText(this.titleArr[selectionFilter.getOriginalIndex()]);
        ((TextView) inflate.findViewById(R.id.description)).setText(this.descriptionArr[selectionFilter.getOriginalIndex()]);
        radioButton.setText(this.firstRadioArr[selectionFilter.getOriginalIndex()]);
        radioButton2.setText(this.secondRadioArr[selectionFilter.getOriginalIndex()]);
        radioButton.setChecked(selectionFilter.isFirstRadioChk());
        radioButton2.setChecked(selectionFilter.isSecondRadioChk());
        checkBox.setChecked(selectionFilter.isTitleChk());
        SelectionFilter selectionFilter2 = new SelectionFilter();
        selectionFilter2.setView(inflate);
        this.selectionMap.put(Integer.valueOf(i), selectionFilter2);
        this.dragLinearLayout.addDragView(inflate, inflate.findViewById(R.id.noteIcon));
    }

    public void setNoteIndex(View view, int i) {
        view.setTag(Integer.valueOf(i));
    }

    public void updateMap(int i, View view) {
        try {
            this.selectionMap.get(Integer.valueOf(i)).setView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void j() {
        for (int i = 0; i < this.dragLinearLayout.getChildCount(); i++) {
            String name = SmartSelection.class.getName();
            Util.appendLogmobiclean(name, "" + this.dragLinearLayout.getChildAt(i).getTag(), "");
        }
        for (Map.Entry<Integer, SelectionFilter> entry : this.selectionMap.entrySet()) {
            SelectionFilter value = entry.getValue();
            View view = value.getView();
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.chk);
            TextView textView = (TextView) view.findViewById(R.id.title);
            RadioButton radioButton = (RadioButton) view.findViewById(R.id.first_radio);
            RadioButton radioButton2 = (RadioButton) view.findViewById(R.id.second_radio);
            PrintStream printStream = System.out;
            printStream.println(entry.getKey() + " = " + textView.getText().toString() + " = " + checkBox.isChecked());
            value.setFirstRadioChk(radioButton.isChecked());
            value.setSecondRadioChk(radioButton2.isChecked());
            value.setTitleChk(checkBox.isChecked());
            value.setTitle(textView.getText().toString());
            value.setDescription(((TextView) view.findViewById(R.id.description)).getText().toString());
            value.setFirstRadioTxt(radioButton.getText().toString());
            value.setSecondRadioTxt(radioButton2.getText().toString());
            int i2 = 0;
            while (true) {
                if (i2 >= this.titleArr.length) {
                    break;
                } else if (value.getTitle().equals(this.titleArr[i2])) {
                    value.setOriginalIndex(i2);
                    break;
                } else {
                    i2++;
                }
            }
            entry.setValue(value);
        }
        try {
            GlobalData.saveObj(this, GlobalData.SmartFile, this.selectionMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.doNotShowChk.isChecked()) {
            MySharedPreference.j(this, true);
        }
        setResult(-1, new Intent());
        finish();
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.smart_selection);
        Util.saveScreen(getClass().getName(), this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }

        this.doNotShowChk = (CheckBox) findViewById(R.id.dont_show);
        this.mark = (LinearLayout) findViewById(R.id.mark);
        DragLinearLayout dragLinearLayout = (DragLinearLayout) findViewById(R.id.container);
        this.dragLinearLayout = dragLinearLayout;
        dragLinearLayout.setContainerScrollView((ScrollView) findViewById(R.id.smart_scroll));
        if (getIntent().getBooleanExtra("auto_mark", false)) {
            this.doNotShowChk.setVisibility(View.VISIBLE);
        } else {
            this.doNotShowChk.setVisibility(View.GONE);
        }
        this.titleArr = getResources().getStringArray(R.array.title);
        this.descriptionArr = getResources().getStringArray(R.array.description);
        this.firstRadioArr = getResources().getStringArray(R.array.first_radio);
        this.secondRadioArr = getResources().getStringArray(R.array.second_radio);
        if (GlobalData.isObjExist(this, GlobalData.SmartFile)) {
            Map<Integer, SelectionFilter> map = null;
            try {
                map = (Map) GlobalData.getObj(this, GlobalData.SmartFile);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.selectionMap = map;
            for (Map.Entry<Integer, SelectionFilter> entry : map.entrySet()) {
                populateViews(entry.getKey().intValue(), entry.getValue());
            }
        } else {
            for (int i = 0; i < 3; i++) {
                populateViews(i, new SelectionFilter(i, this.titleArr[i], this.descriptionArr[i], this.firstRadioArr[i], this.secondRadioArr[i]));
            }
        }
        this.mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartSelection.this.j();
            }
        });
        this.dragLinearLayout.setOnViewSwapListener(new DragLinearLayout.OnViewSwapListener() {
            @Override
            public void onSwap(View view, int i2, View view2, int i3) {
                Util.appendLogmobiclean(SmartSelection.class.getName(), String.format("\tFirst Position is:\t%d \tSecond Position is:\t%d", Integer.valueOf(i2), Integer.valueOf(i3)), "");
                int noteIndex = SmartSelection.this.getNoteIndex(view);
                int noteIndex2 = SmartSelection.this.getNoteIndex(view2);
                SmartSelection.this.setNoteIndex(view, noteIndex2);
                SmartSelection.this.setNoteIndex(view2, noteIndex);
                SmartSelection.this.updateMap(noteIndex2, view);
                SmartSelection.this.updateMap(noteIndex, view2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onResume() {
        LinearLayout linearLayout;
        super.onResume();
        if (!Util.isAdsFree(this) || (linearLayout = this.i) == null) {
            return;
        }
        linearLayout.setVisibility(View.GONE);
    }
}
