package com.cleanPhone.mobileCleaner;

import java.io.File;
import java.io.FileFilter;

public class ImageFileFilter implements FileFilter {
    private String[] fileExtensions;

    public ImageFileFilter(int i) {
        this.fileExtensions = new String[]{"jpg", "jpeg"};
        if (i == 0) {
            this.fileExtensions = new String[]{"jpeg", "jpg", "png", "tiff", "tif", "bmp"};
        } else if (i == 1) {
            this.fileExtensions = new String[]{"mp4", "gif", "mpg", "3gp", "avi", "mpeg"};
        } else if (i == 2) {
            this.fileExtensions = new String[]{"mp3", "aac", "m4a", "wav"};
        } else if (i == 3) {
            this.fileExtensions = new String[]{"pdf", "doc", "docx", "xls", "ppt", "odt", "rtf", "txt", "pptx", "htm", "html", "log", "csv", "dot", "dotx", "docm", "dotm", "xml", "mht", "dic", "xlsx", "msg", "mhtml", "pps", "xltx", "xlt", "xlsm", "xltm", "ppsx", "pptm", "ppsm"};
        } else if (i != 4) {
        } else {
            this.fileExtensions = new String[]{"mp4"};
        }
    }

    @Override
    public boolean accept(File file) {
        for (String str : this.fileExtensions) {
            if (file.getName().toLowerCase().endsWith(str)) {
                return true;
            }
        }
        return false;
    }
}
