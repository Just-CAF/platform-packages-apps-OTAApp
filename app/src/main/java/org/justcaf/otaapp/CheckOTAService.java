package org.justcaf.otaapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class CheckOTAService extends Service {

    public static final int notify = 30000;
    private Handler handler = new Handler();
    private Timer timer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        if (timer != null)
            timer.cancel();
        else
            timer = new Timer();
        timer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(CheckOTAService.this, "Service is running", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
