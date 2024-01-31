package com.cleanPhone.mobileCleaner.utility;

import android.annotation.SuppressLint;
import android.os.Environment;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;


public class StorageHelper {
    private static final String[] AVOIDED_DEVICES;
    private static final String[] AVOIDED_DIRECTORIES;
    private static final String[] DISALLOWED_FILESYSTEMS;
    private static final String STORAGES_ROOT;
    private static String prefixEmuPath;

    public static final class StorageVolume {
        public final String device;
        public final File file;
        public final String fileSystem;
        private boolean mEmulated;
        private boolean mReadOnly;
        private boolean mRemovable;
        private Type mType;

        public enum Type {
            INTERNAL,
            EXTERNAL,
            USB
        }

        public StorageVolume(String str, File file, String str2) {
            this.device = str;
            this.file = file;
            this.fileSystem = str2;
        }

        public synchronized boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            StorageVolume storageVolume = (StorageVolume) obj;
            File file = this.file;
            if (file == null) {
                return storageVolume.file == null;
            }
            return file.equals(storageVolume.file);
        }

        public Type getType() {
            return this.mType;
        }

        public int hashCode() {
            File file = this.file;
            return 31 + (file == null ? 0 : file.hashCode());
        }

        public boolean isEmulated() {
            return this.mEmulated;
        }

        public boolean isReadOnly() {
            return this.mReadOnly;
        }

        public boolean isRemovable() {
            return this.mRemovable;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.file.getAbsolutePath());
            sb.append(this.mReadOnly ? " ro " : " rw ");
            sb.append(this.mType);
            sb.append(this.mRemovable ? " R " : "");
            sb.append(this.mEmulated ? " E " : "");
            sb.append(this.fileSystem);
            return sb.toString();
        }
    }

    static {
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        int indexOf = absolutePath.indexOf(File.separatorChar, 1);
        if (indexOf != -1) {
            STORAGES_ROOT = absolutePath.substring(0, indexOf + 1);
        } else {
            STORAGES_ROOT = File.separator;
        }
        AVOIDED_DEVICES = new String[]{"rootfs", "tmpfs", "dvpts", "proc", "sysfs", "none"};
        AVOIDED_DIRECTORIES = new String[]{"obb", "asec"};
        DISALLOWED_FILESYSTEMS = new String[]{"tmpfs", "rootfs", "romfs", "devpts", "sysfs", "proc", "cgroup", "debugfs"};
        prefixEmuPath = "/storage/emulated";
    }

    private StorageHelper() {
    }

    private static synchronized <T> boolean arrayContains(T[] tArr, T t) {
        synchronized (StorageHelper.class) {
            for (T t2 : tArr) {
                if (t2.equals(t)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static synchronized boolean containsIgnoreCase(String str, String str2) {
        synchronized (StorageHelper.class) {
            if (str == null || str2 == null) {
                return false;
            }
            int length = str2.length();
            int length2 = str.length() - length;
            for (int i = 0; i <= length2; i++) {
                if (str.regionMatches(true, i, str2, 0, length)) {
                    return true;
                }
            }
            return false;
        }
    }

    @SuppressLint({"NewApi"})
    public static synchronized java.util.ArrayList<File> getStorages(boolean r14) {
        return null;
    }

    private static synchronized boolean pathContainsDir(String str, String[] strArr) {
        synchronized (StorageHelper.class) {
            StringTokenizer stringTokenizer = new StringTokenizer(str, File.separator);
            while (true) {
                if (!stringTokenizer.hasMoreElements()) {
                    return false;
                }
                String nextToken = stringTokenizer.nextToken();
                for (String str2 : strArr) {
                    if (nextToken.equals(str2)) {
                        return true;
                    }
                }
            }
        }
    }

    @SuppressLint({"NewApi"})
    private static synchronized StorageVolume.Type resolveType(StorageVolume storageVolume) {
        synchronized (StorageHelper.class) {
            if (storageVolume.file.equals(Environment.getExternalStorageDirectory()) && Environment.isExternalStorageEmulated()) {
                return StorageVolume.Type.INTERNAL;
            } else if (containsIgnoreCase(storageVolume.file.getAbsolutePath(), "usb")) {
                return StorageVolume.Type.USB;
            } else {
                return StorageVolume.Type.EXTERNAL;
            }
        }
    }

    private static synchronized void setTypeAndAdd(List<StorageVolume> list, StorageVolume storageVolume, boolean z, boolean z2) {
        synchronized (StorageHelper.class) {
            StorageVolume.Type resolveType = resolveType(storageVolume);
            if (z || resolveType != StorageVolume.Type.USB) {
                storageVolume.mType = resolveType;
                boolean z3 = true;
                if (storageVolume.file.equals(Environment.getExternalStorageDirectory())) {
                    storageVolume.mRemovable = Environment.isExternalStorageRemovable();
                } else {
                    storageVolume.mRemovable = resolveType != StorageVolume.Type.INTERNAL;
                }
                if (resolveType != StorageVolume.Type.INTERNAL) {
                    z3 = false;
                }
                storageVolume.mEmulated = z3;
                if (z2) {
                    list.add(0, storageVolume);
                } else {
                    list.add(storageVolume);
                }
            }
        }
    }
}
