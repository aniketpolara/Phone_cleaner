package com.cleanPhone.mobileCleaner.filestorage;

import java.util.HashMap;
import java.util.Set;


public class MarkedItemList {
    private static HashMap<String, FileListItem> ourInstance = new HashMap<>();

    private MarkedItemList() {
    }

    public static void a(FileListItem fileListItem) {
        ourInstance.put(fileListItem.getLocation(), fileListItem);
    }

    public static void b(FileListItem fileListItem) {
        ourInstance.clear();
        ourInstance.put(fileListItem.getLocation(), fileListItem);
    }

    public static void c() {
        ourInstance.clear();
    }

    public static int d() {
        return ourInstance.size();
    }

    public static String[] e() {
        Set<String> keySet = ourInstance.keySet();
        String[] strArr = new String[keySet.size()];
        int i = 0;
        for (String str : keySet) {
            strArr[i] = str;
            i++;
        }
        return strArr;
    }

    public static boolean f(String str) {
        return ourInstance.containsKey(str);
    }

    public static void g(String str) {
        ourInstance.remove(str);
    }

}
