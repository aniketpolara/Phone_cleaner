package com.cleanPhone.mobileCleaner.bservices;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationCategoryFinder extends IntentService {
    public ApplicationCategoryFinder(String str) {
        super(str);
    }

    private boolean checkIsaGame(String str) {
        String[] strArr = {"Action", "Adventure", "Arcade", "Board", "Casual", "Educational", "GAME_MUSIC", "Music", "Puzzle", "Racing", "Role Playing", "Simulation", "Sports", "GAME_PUZZLE", "GAME_WORD", "GAME_CASUAL"};
        if (str.toLowerCase().contains("game")) {
            return true;
        }
        for (int i = 0; i < 16; i++) {
            if (str.equalsIgnoreCase(strArr[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onHandleIntent(@Nullable Intent intent) {
        String stringExtra;
        if (intent != null) {
            try {
                stringExtra = intent.getStringExtra("PKG");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        } else {
            stringExtra = null;
        }
        URLConnection openConnection = null;
        try {
            openConnection = new URL("https://play.google.com/store/apps/details?id=" + stringExtra).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(openConnection.getInputStream(), (String) Arrays.asList(((String) Arrays.asList(openConnection.getContentType().split(";")).get(1)).split("=")).get(1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            String readLine = null;
            try {
                readLine = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (readLine == null) {
                break;
            }
            stringBuffer.append(readLine + IOUtils.LINE_SEPARATOR_UNIX);
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Matcher matcher = Pattern.compile("<a class=\"document-subtitle category\" href=\"/store/apps/category/([^\"]+)\"", 2).matcher(stringBuffer.toString());
        if (checkIsaGame("" + (matcher.find() ? matcher.group(1) : null))) {
            Intent intent2 = new Intent("game.app.installed");
            intent2.putExtra("PKG", "" + stringExtra);
            getBaseContext().sendBroadcast(intent2);
        }
    }


}
