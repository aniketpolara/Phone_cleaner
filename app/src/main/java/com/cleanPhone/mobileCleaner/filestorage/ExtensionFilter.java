package com.cleanPhone.mobileCleaner.filestorage;

import java.io.File;
import java.io.FileFilter;


public class ExtensionFilter implements FileFilter {
    private DialogProperties properties;
    private final String[] validExtensions;

    public ExtensionFilter(DialogProperties dialogProperties) {
        String[] strArr = dialogProperties.extensions;
        if (strArr != null) {
            this.validExtensions = strArr;
        } else {
            this.validExtensions = new String[]{""};
        }
        this.properties = dialogProperties;
    }

    @Override
    public boolean accept(File file) {
        if (file.isHidden()) {
            return false;
        }
        if (file.isDirectory() && file.canRead()) {
            return true;
        }
        if (this.properties.selection_type == 1) {
            return false;
        }
        String lowerCase = file.getName().toLowerCase();
        for (String str : this.validExtensions) {
            if (lowerCase.endsWith(str)) {
                return true;
            }
        }
        return false;
    }
}
