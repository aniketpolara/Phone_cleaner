package com.cleanPhone.mobileCleaner.wrappers;

import android.view.View;

import java.io.Serializable;

public class SelectionFilter implements Serializable {
    private String description;
    private String firstRadioTxt;
    private boolean isFirstRadioChk;
    private boolean isSecondRadioChk;
    private boolean isTitleChk;
    private int originalIndex;
    private String secondRadioTxt;
    private String title;
    private transient View view;

    public SelectionFilter() {
        this.isTitleChk = true;
        this.isFirstRadioChk = true;
        this.isSecondRadioChk = false;
    }

    public String getDescription() {
        return this.description;
    }

    public String getFirstRadioTxt() {
        return this.firstRadioTxt;
    }

    public int getOriginalIndex() {
        return this.originalIndex;
    }

    public String getSecondRadioTxt() {
        return this.secondRadioTxt;
    }

    public String getTitle() {
        return this.title;
    }

    public View getView() {
        return this.view;
    }

    public boolean isFirstRadioChk() {
        return this.isFirstRadioChk;
    }

    public boolean isSecondRadioChk() {
        return this.isSecondRadioChk;
    }

    public boolean isTitleChk() {
        return this.isTitleChk;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public void setFirstRadioChk(boolean z) {
        this.isFirstRadioChk = z;
    }

    public void setFirstRadioTxt(String str) {
        this.firstRadioTxt = str;
    }

    public void setOriginalIndex(int i) {
        this.originalIndex = i;
    }

    public void setSecondRadioChk(boolean z) {
        this.isSecondRadioChk = z;
    }

    public void setSecondRadioTxt(String str) {
        this.secondRadioTxt = str;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public void setTitleChk(boolean z) {
        this.isTitleChk = z;
    }

    public void setView(View view) {
        this.view = view;
    }

    public SelectionFilter(int i, String str, String str2, String str3, String str4) {
        this.isTitleChk = true;
        this.isFirstRadioChk = true;
        this.isSecondRadioChk = false;
        this.originalIndex = i;
        this.description = str2;
        this.firstRadioTxt = str3;
        this.secondRadioTxt = str4;
        this.title = str;
    }

    public SelectionFilter(String str, String str2, String str3, String str4, boolean z, boolean z2, boolean z3, View view) {
        this.isTitleChk = true;
        this.isFirstRadioChk = true;
        this.isSecondRadioChk = false;
        this.isTitleChk = z;
        this.description = str2;
        this.firstRadioTxt = str3;
        this.secondRadioTxt = str4;
        this.isSecondRadioChk = z3;
        this.isFirstRadioChk = z2;
        this.view = view;
        this.title = str;
    }
}
