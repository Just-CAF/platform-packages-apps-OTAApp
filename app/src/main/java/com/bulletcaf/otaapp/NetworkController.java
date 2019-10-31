package com.bulletcaf.otaapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class NetworkController {
    private static List<String> getUpdateVersion() throws IOException {
        URLConnection url;
        url = new URL("http://167.86.75.223/test.txt").openConnection();
        InputStream is = url.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        ArrayList<String> text = new ArrayList<>();
        String line = null;
        while ((line = reader.readLine()) != null) // read line by line
        {
            text.add(line); // add line to list
        }
        is.close();
        return text;
    }
    public static String getUpdate() throws IOException {
        return getUpdateVersion().get(0);
    }
}
