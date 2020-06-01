package org.justcaf.otaapp.utils;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.justcaf.otaapp.misc.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class Utils {
    static String GETPROP_EXECUTABLE_PATH = "/system/bin/getprop";

    public static String getProperty(String propName) {
        Process process = null;
        BufferedReader bufferedReader = null;

        try {
            process = new ProcessBuilder().command(GETPROP_EXECUTABLE_PATH, propName).redirectErrorStream(true).start();
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = bufferedReader.readLine();
            if (line == null) {
                line = "";
            }

            return line;
        } catch (Exception e) {
            Log.e(Constants.TAG,"Failed to read System Property " + propName,e);
            return "";
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {}
            }

            if (process != null) {
                process.destroy();
            }
        }
    }

    public static void setUpdateInfoURL() {
        if (!getProperty(Constants.DEV_UPDATE_DATA).equals("")) {
            Constants.OTA_DATA_URL = getProperty(Constants.DEV_UPDATE_DATA);
        }
    }

    public static void removeExistingFile(String filename) {
        File file = new File(filename);

        Log.i(Constants.TAG, "Checking for file " + file.getAbsolutePath());

        if (file.exists()) {
            if (file.delete()) {
                Log.i(Constants.TAG, "Old file deleted");

            } else {
                Log.i(Constants.TAG, "Could not delete");
            }
        }
    }
}
