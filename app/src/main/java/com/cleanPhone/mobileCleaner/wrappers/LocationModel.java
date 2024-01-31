package com.cleanPhone.mobileCleaner.wrappers;

import java.io.Serializable;

public class LocationModel implements Serializable {
    private String CO;
    private int GID;
    private double LAT;
    private String LO;
    private double LON;
    private String ST;
    private String TIMEZONE;

    public boolean equals(Object obj) {
        return ((LocationModel) obj).getGID() == this.GID;
    }

    public String getCO() {
        return this.CO;
    }

    public int getGID() {
        return this.GID;
    }

    public double getLAT() {
        return this.LAT;
    }

    public String getLO() {
        return this.LO;
    }

    public double getLON() {
        return this.LON;
    }

    public String getST() {
        return this.ST;
    }

    public String getTIMEZONE() {
        return this.TIMEZONE;
    }

    public void setCO(String str) {
        this.CO = str;
    }

    public void setGID(int i) {
        this.GID = i;
    }

    public void setLAT(double d2) {
        this.LAT = d2;
    }

    public void setLO(String str) {
        this.LO = str;
    }

    public void setLON(double d2) {
        this.LON = d2;
    }

    public void setST(String str) {
        this.ST = str;
    }

    public void setTIMEZONE(String str) {
        this.TIMEZONE = str;
    }
}
