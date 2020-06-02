package org.justcaf.otaapp.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;

import org.justcaf.otaapp.misc.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

    public static void removeExistingFile() {
        File file = new File(Constants.FILE_DOWNLOAD_DIRECTORY + "/" +
                Constants.FILE_DOWNLOAD_NAME);

        Log.i(Constants.TAG, "Checking for file " + file.getAbsolutePath());

        if (file.exists()) {
            Log.i(Constants.TAG, "Deleting old file");

            // TODO: fix this
            try {
                Process rm = Runtime.getRuntime().exec("rm " +
                        Constants.FILE_DOWNLOAD_DIRECTORY + "/" + Constants.FILE_DOWNLOAD_NAME);

                new BufferedReader(new InputStreamReader(rm.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void installUpdate(Context context) {
        // Move downloaded file to /cache/justcaf/

        String filename = Constants.FILE_DOWNLOAD_DIRECTORY + "/" + Constants.FILE_DOWNLOAD_NAME;
        //String copyFromDownloads = "cp " + filename + " /cache/justcaf/update.zip";
        //Log.i("CP", copyFromDownloads);

        /*try {
            // No space on cache
            //Process mkdir = Runtime.getRuntime().exec("mkdir /cache/justcaf");
            //new BufferedReader(new InputStreamReader(mkdir.getInputStream()));

            //Process cp = Runtime.getRuntime().exec(copyFromDownloads);
            //new BufferedReader(new InputStreamReader(cp.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        ArrayList<String> commands = new ArrayList<>();

        commands.add("wipe cache");
        commands.add("wipe dalvik");
        commands.add("install " + filename);

        try {
            FileOutputStream os = new FileOutputStream(Constants.OPENRECOVERYSCRIPT_PATH, false);

            for (int i = 0; i < commands.size(); i++) {
                os.write((commands.get(i) + "\n").getBytes("UTF-8"));
            }

            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //((PowerManager) context.getSystemService(Activity.POWER_SERVICE)).reboot("recovery");
    }
}
