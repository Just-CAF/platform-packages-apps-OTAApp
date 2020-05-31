package org.justcaf.otaapp;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class OTAManager {
    public String currentVersion;

    private String updateVersion;
    private String changelog;
    private Context mContext;

    DateFormat mFormat;
    NetworkController nc;

    public OTAManager(Context context) {
        mFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        mFormat.setTimeZone(TimeZone.getTimeZone("CEST"));
        currentVersion = mFormat.format(Build.TIME);
        this.mContext = context;
        nc = new NetworkController(mContext);
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

    public String getChangelog() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                changelog = nc.getChangelog();
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return changelog;
    }

    public void update() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                nc.downloadZip();
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
