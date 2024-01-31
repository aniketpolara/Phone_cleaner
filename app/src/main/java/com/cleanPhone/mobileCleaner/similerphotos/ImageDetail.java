package com.cleanPhone.mobileCleaner.similerphotos;

import android.media.ExifInterface;

import java.io.Serializable;

public class ImageDetail implements Serializable {
    public Long addDateInMSec;
    public Long createDateInMSecs;
    public String createionDate;
    public String distdiffer;
    public transient ExifInterface exif;
    public String ext;
    public String hash;
    public int height;
    public int id;
    public String info;
    public boolean ischecked;
    public String lat;
    public boolean lockImg;
    public String lon;
    public String match;
    public String name;
    public int orientation;
    public String path;
    public long size;
    public boolean skipImage;
    public int width;


    public Long getAddDateInMSec() {
        return addDateInMSec;
    }

    public void setAddDateInMSec(Long addDateInMSec) {
        this.addDateInMSec = addDateInMSec;
    }

    public Long getCreateDateInMSecs() {
        return createDateInMSecs;
    }

    public void setCreateDateInMSecs(Long createDateInMSecs) {
        this.createDateInMSecs = createDateInMSecs;
    }

    public String getCreateionDate() {
        return createionDate;
    }

    public void setCreateionDate(String createionDate) {
        this.createionDate = createionDate;
    }

    public String getDistdiffer() {
        return distdiffer;
    }

    public void setDistdiffer(String distdiffer) {
        this.distdiffer = distdiffer;
    }

    public ExifInterface getExif() {
        return exif;
    }

    public void setExif(ExifInterface exif) {
        this.exif = exif;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public boolean isLockImg() {
        return lockImg;
    }

    public void setLockImg(boolean lockImg) {
        this.lockImg = lockImg;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isSkipImage() {
        return skipImage;
    }

    public void setSkipImage(boolean skipImage) {
        this.skipImage = skipImage;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
