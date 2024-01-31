package com.cleanPhone.mobileCleaner.animate;


public class ClassUtil {
    public static <T> T getClassByName(String str, Class<T> cls) {
        try {
            Object newInstance = Class.forName(str).newInstance();
            if (cls.isInstance(newInstance)) {
                return cls.cast(newInstance);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
