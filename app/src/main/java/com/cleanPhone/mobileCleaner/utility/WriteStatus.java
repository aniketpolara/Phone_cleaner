package com.cleanPhone.mobileCleaner.utility;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WriteStatus {
    public static final String DATE = "date";
    public static final String NOTI1 = "noti1";
    public static final String NOTI2 = "noti2";
    public static final String NOTI3 = "noti3";
    public static final String NOTI4 = "noti4";
    public static String f5357a = "dailystatus.txt";
    public static String b = "previousstatus.txt";
    public static String f5358c = "dd-MM-yyyy hh:mm:ss";

    private static void appendLog(Context context, String str) {
        File file = new File(context.getCacheDir(), f5357a);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.append((CharSequence) str);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private static void execute(AsyncTask<String, String, String> asyncTask, Context context) {
        asyncTask.execute(new String[0]);
        new SharedPrefUtil(context).saveLong(SharedPrefUtil.INST_DET_SENT, System.currentTimeMillis());
    }

    private static JSONObject getEditedJson(JSONObject jSONObject, String str, int i) {
        JSONObject jSONObject2 = new JSONObject();
        try {
            if (!str.equalsIgnoreCase(NOTI1)) {
                jSONObject2.put(NOTI1, jSONObject.getInt(NOTI1));
            } else {
                jSONObject2.put(NOTI1, i);
            }
            if (!str.equalsIgnoreCase("noti2")) {
                jSONObject2.put("noti2", jSONObject.getInt("noti2"));
            } else {
                jSONObject2.put("noti2", i);
            }
            if (!str.equalsIgnoreCase(NOTI3)) {
                jSONObject2.put(NOTI3, jSONObject.getInt(NOTI3));
            } else {
                jSONObject2.put(NOTI3, i);
            }
            if (!str.equalsIgnoreCase("noti4")) {
                jSONObject2.put("noti4", jSONObject.getInt("noti4"));
            } else {
                jSONObject2.put("noti4", i);
            }
            jSONObject2.put(DATE, new SimpleDateFormat(f5358c).format(Calendar.getInstance().getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject2;
    }

    private static int getFlag(boolean z) {
        return z ? 1 : 0;
    }

    private static String getJsonString() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(NOTI1, 0);
            jSONObject.put("noti2", 0);
            jSONObject.put(NOTI3, 0);
            jSONObject.put("noti4", 0);
            jSONObject.put(DATE, new SimpleDateFormat(f5358c).format(Calendar.getInstance().getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    private static boolean isOneDayDiffer(long j) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(j));
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        Date time = calendar.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(11, 0);
        calendar2.set(12, 0);
        calendar2.set(13, 0);
        calendar2.set(14, 0);
        Date time2 = calendar2.getTime();
        long convert = TimeUnit.DAYS.convert(time2.getTime() - time.getTime(), TimeUnit.MILLISECONDS);
        Log.e("WSSS", "date 1 = " + time.toString());
        Log.e("WSSS", "date 2 = " + time2.toString());
        return convert > 0;
    }

    public static String readFromFile(Context context, String str) {
        String str2 = "";
        try {
            File file = new File(context.getCacheDir(), str);
            if (!file.exists()) {
                boolean createNewFile = file.createNewFile();
                String jsonString = getJsonString();
                if (createNewFile) {
                    appendLog(context, jsonString);
                }
            }
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                StringBuffer stringBuffer = new StringBuffer();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        stringBuffer.append(readLine);
                    } else {
                        String stringBuffer2 = stringBuffer.toString();
                        try {
                            Log.d("WRITESTATUS", "" + str + " = " + stringBuffer.toString());
                            return stringBuffer2;
                        } catch (Exception e2) {
                            str2 = stringBuffer2;
//                            e = e2;
//                            Log.e("login activity", "File not found: " + e.toString());
                            return str2;
                        }
                    }
                }
            } catch (IOException e3) {
//                e = e3;
            }
        } catch (Exception e4) {
//            e = e4;
        }
        return str2;
    }

    private static String readStream(InputStream inputStream) {
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            try {
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream));
                while (true) {
                    try {
                        String readLine = bufferedReader2.readLine();
                        if (readLine == null) {
                            break;
                        }
                        stringBuffer.append(readLine);
                    } catch (IOException e2) {
//                        e = e2;
                        bufferedReader = bufferedReader2;
//                        e.printStackTrace();
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        return stringBuffer.toString();
                    } catch (Throwable th) {
                        th = th;
                        bufferedReader = bufferedReader2;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        throw th;
                    }
                }
                bufferedReader2.close();
            } catch (IOException e4) {
//                e = e4;
            }
            return stringBuffer.toString();
        } catch (Throwable th2) {
//            th = th2;
        }
        return null;
    }

    public static void sendDetail(Context context, boolean z) {
    }

    public static void write(Context context, String str, int i) {
    }

    private static void writePreviousFileIfNeeded(JSONObject jSONObject, JSONObject jSONObject2, Context context) {
        try {
            String string = jSONObject.getString(DATE);
            String string2 = jSONObject2.getString(DATE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                long time = simpleDateFormat.parse(string2).getTime() - simpleDateFormat.parse(string).getTime();
                if (((int) (time / 86400000)) >= 1) {
                    File file = new File(context.getCacheDir(), b);
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(context.getCacheDir(), b));
                        fileOutputStream.write(jSONObject.toString().getBytes());
                        fileOutputStream.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                PrintStream printStream = System.out;
                printStream.println("Days: " + TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS));
            } catch (ParseException e3) {
                e3.printStackTrace();
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }
}
