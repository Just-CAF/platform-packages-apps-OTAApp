package org.justcaf.otaapp.utils;

import android.util.Log;

import org.justcaf.otaapp.misc.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
}
