package com.cleanPhone.mobileCleaner.processes;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcel;

import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.utility.AndroidProcesses;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class AndroidAppProcess extends AndroidProcess {
    public final boolean foreground;
    public final int uid;
    private static final boolean SYS_SUPPORTS_SCHEDGROUPS = new File("/dev/cpuctl/tasks").exists();
    private static final Pattern PROCESS_NAME_PATTERN = Pattern.compile("^([A-Za-z]{1}[A-Za-z0-9_]*[\\.|:])*[A-Za-z][A-Za-z0-9_]*$");
    public static final Creator<AndroidAppProcess> CREATOR = new Creator<AndroidAppProcess>() {

        @Override
        public AndroidAppProcess createFromParcel(Parcel parcel) {
            return new AndroidAppProcess(parcel);
        }

        @Override
        public AndroidAppProcess[] newArray(int i) {
            return new AndroidAppProcess[i];
        }
    };

    public static final class NotAndroidAppProcessException extends Exception {
        public NotAndroidAppProcessException(int i) {
            super(String.format("The process %d does not belong to any application", Integer.valueOf(i)));
        }
    }

    public AndroidAppProcess(int i) throws IOException, NotAndroidAppProcessException {
        super(i);
        boolean z;
        int uid;
        String str = this.name;
        if (str != null && PROCESS_NAME_PATTERN.matcher(str).matches() && new File("/data/data", getPackageName()).exists()) {
            if (SYS_SUPPORTS_SCHEDGROUPS) {
                Cgroup cgroup = cgroup();
                ControlGroup group = cgroup.getGroup("cpuacct");
                ControlGroup group2 = cgroup.getGroup("cpu");
                if (Build.VERSION.SDK_INT >= 21) {
                    if (group2 != null && group != null && group.group.contains("pid_")) {
                        z = !group2.group.contains("bg_non_interactive");
                        try {
                            uid = Integer.parseInt(group.group.split(DialogConfigs.DIRECTORY_SEPERATOR)[1].replace("uid_", ""));
                        } catch (Exception unused) {
                            uid = status().getUid();
                        }
                        AndroidProcesses.log("name=%s, pid=%d, uid=%d, foreground=%KeyGrd, cpuacct=%s, cpu=%s", this.name, Integer.valueOf(i), Integer.valueOf(uid), Boolean.valueOf(z), group.toString(), group2.toString());
                    } else {
                        throw new NotAndroidAppProcessException(i);
                    }
                } else if (group2 != null && group != null && group2.group.contains("apps")) {
                    z = !group2.group.contains("bg_non_interactive");
                    try {
                        String str2 = group.group;
                        uid = Integer.parseInt(str2.substring(str2.lastIndexOf(DialogConfigs.DIRECTORY_SEPERATOR) + 1));
                    } catch (Exception unused2) {
                        uid = status().getUid();
                    }
                    AndroidProcesses.log("name=%s, pid=%d, uid=%d foreground=%KeyGrd, cpuacct=%s, cpu=%s", this.name, Integer.valueOf(i), Integer.valueOf(uid), Boolean.valueOf(z), group.toString(), group2.toString());
                } else {
                    throw new NotAndroidAppProcessException(i);
                }
            } else {
                Stat stat = stat();
                Status status = status();
                z = stat.policy() == 0;
                uid = status.getUid();
                AndroidProcesses.log("name=%s, pid=%d, uid=%d foreground=%KeyGrd", this.name, Integer.valueOf(i), Integer.valueOf(uid), Boolean.valueOf(z));
            }
            this.foreground = z;
            this.uid = uid;
            return;
        }
        throw new NotAndroidAppProcessException(i);
    }

    public PackageInfo getPackageInfo(Context context, int i) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(getPackageName(), i);
    }

    public String getPackageName() {
        return this.name.split(":")[0];
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeByte(this.foreground ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.uid);
    }

    public AndroidAppProcess(Parcel parcel) {
        super(parcel);
        this.foreground = parcel.readByte() != 0;
        this.uid = parcel.readInt();
    }
}
