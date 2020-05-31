package org.justcaf.otaapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class NetworkController {
    private String updateUrl;
    private String updateVersion;

    private ArrayList<String> getUpdateData() {
        ArrayList<String> text = new ArrayList<>();
        try {
            URLConnection url;
            url = new URL("https://raw.githubusercontent.com/Just-CAF/releases/custom/releases.txt").openConnection();
            InputStream is = url.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) // read line by line
            {
                text.add(line); // add line to list
            }
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return text;
    }

    public String getUpdate() throws IOException {
        String data = getUpdateData();
        String parts = data.split(",");

        updateVersion = data.get(0);
        updateUrl = data.get(1);
        return updateVersion;
    }

    public void downloadZip() {
        Log.i("OTADebug", "Got download call. Url"+updateUrl);
        //TODO
    }
}
