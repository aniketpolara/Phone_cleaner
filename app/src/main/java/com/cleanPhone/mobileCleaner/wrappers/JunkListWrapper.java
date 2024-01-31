package com.cleanPhone.mobileCleaner.wrappers;

import java.io.Serializable;
import java.util.ArrayList;

public class JunkListWrapper implements Serializable {
    public String appname;
    public String name;
    public String path;
    public String pkg;
    public long size;
    public String type;
    public boolean ischecked = true;
    public ArrayList<String> fileslist = new ArrayList<>();
}
