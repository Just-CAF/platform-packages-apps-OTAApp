package org.justcaf.otaapp.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import org.justcaf.otaapp.misc.Constants;
import org.justcaf.otaapp.utils.Utils;

public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(reference);
        Cursor c = downloadManager.query(query);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = c.getInt(columnIndex);

        if (status == DownloadManager.STATUS_SUCCESSFUL) {
            Log.i(Constants.TAG, "Download complete");
            Utils.installUpdate(context);
        }
    }
}