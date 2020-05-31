package org.justcaf.otaapp;

import android.os.Build;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class OTAManager {
    NetworkController nc = new NetworkController();
    private String version;
    public String currentVersion;

    public OTAManager() {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        format.setTimeZone(TimeZone.getTimeZone("CEST"));
        currentVersion = format.format(Build.TIME);
    }

    public String getVersion() {
        if (version == null) {
            Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            version = nc.getUpdate();
                        } catch (IOException e) {
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
        return version;
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
