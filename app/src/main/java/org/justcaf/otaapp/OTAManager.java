package org.justcaf.otaapp;

import android.os.Build;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class OTAManager {
    NetworkController nc = new NetworkController();
    private String updateVersion;
    public String currentVersion;

    DateFormat mFormat;

    public OTAManager() {
        mFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        mFormat.setTimeZone(TimeZone.getTimeZone("CEST"));
        currentVersion = mFormat.format(Build.TIME);
    }

    public String getUpdateVersion() {
        if (updateVersion == null) {
            Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
                            updateVersion = mFormat.format(f.parse(nc.getUpdate()));
                        } catch (IOException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return updateVersion;
    }

    public void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                nc.downloadZip();
            }
        });
    }
}
