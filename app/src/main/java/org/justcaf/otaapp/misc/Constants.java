package org.justcaf.otaapp.misc;

import android.os.Environment;

public class Constants {
    public static String TAG = "JUSTCAF";
    public static String DEV_UPDATE_DATA = "ro.update.data.url";

    public static String FILE_DOWNLOAD_DIRECTORY = Environment.DIRECTORY_DOWNLOADS;
    public static String FILE_DOWNLOAD_NAME = "update.zip";
    public static String OTA_DATA_URL =
            "https://raw.githubusercontent.com/Just-CAF/releases/custom/releases.txt";

    public static String CHANGELOG_URL_PREFIX =
            "https://raw.githubusercontent.com/Just-CAF/releases/custom";

}