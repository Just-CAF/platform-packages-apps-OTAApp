package org.justcaf.otaapp;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.justcaf.otaapp.misc.Constants;
import org.justcaf.otaapp.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NetworkController {
    private String updateUrl;
    private String updateVersion;
    private Context mContext;

    public NetworkController(Context context) {
        this.mContext = context;
    }

    private ArrayList<String> getUpdateData() {
        ArrayList<String> text = new ArrayList<>();
        try {
            URLConnection url;
            url = new URL(Constants.OTA_DATA_URL).openConnection();
            InputStream is = url.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                text.add(line);
            }
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return text;
    }

    public String getUpdate() throws IOException {
        ArrayList<String> data = getUpdateData();

        String latest = data.get(0);
        String[] parts = latest.split(",");
        updateUrl = parts[2];
        updateVersion = parts[1];

        return updateVersion;
    }

    public String getChangelog() {
        StringBuilder changelog = new StringBuilder();

        try {
            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
            SimpleDateFormat changelog_date = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String changelog_date_string = "";
            try {
                changelog_date_string = changelog_date.format(f.parse(getUpdate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            URLConnection url;
            url = new URL(Constants.CHANGELOG_URL_PREFIX + "/changelog_"
                    + changelog_date_string + ".txt").openConnection();

            Log.i("URL", url.toString());
            InputStream is = url.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;

            while ((line = reader.readLine()) != null) {
                changelog.append("\n").append(line);
            }

            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return changelog.toString();
    }

    public void downloadZip() {

        Log.e("OTADebug", "Got download call. Url " + updateUrl + " to file "
                + Constants.FILE_DOWNLOAD_NAME);

        String[] urlParts = updateUrl.split("/");
        String filename = urlParts[urlParts.length - 2];

        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(updateUrl));

        request.setDescription("Downloading update");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Constants.FILE_DOWNLOAD_ENV,
                Constants.FILE_DOWNLOAD_NAME);

        DownloadManager manager = (DownloadManager) mContext.getSystemService(
                Context.DOWNLOAD_SERVICE);

        //Log.e(Constants.TAG, manager.COLUMN_LOCAL_URI);

        manager.enqueue(request);
    }
}
