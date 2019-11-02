package com.bulletcaf.otaapp;

import android.os.Build;

import java.io.IOException;

public class OTAManager {
    NetworkController nc = new NetworkController();
    private String version;
    public long currentVersion = Build.TIME;

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
