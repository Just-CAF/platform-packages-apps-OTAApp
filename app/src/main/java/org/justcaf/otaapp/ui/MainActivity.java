package org.justcaf.otaapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.justcaf.otaapp.ui.Changelog;
import org.justcaf.otaapp.OTAManager;
import org.justcaf.otaapp.R;
import org.justcaf.otaapp.misc.Constants;
import org.justcaf.otaapp.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.StrictMath.toIntExact;

public class MainActivity extends AppCompatActivity {
    TextView txBuild;
    TextView txUpdate;
    TextView txUpdateTime;
    Button btnChangelog;
    LinearLayout updateView;
    FloatingActionButton btnRefresh;
    OTAManager om = new OTAManager(this);

    SimpleDateFormat userDate = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.setUpdateInfoURL();
        Log.i(Constants.TAG, "OTA data URL: " + Constants.OTA_DATA_URL);

        txBuild = findViewById(R.id.buildText);
        txUpdate = findViewById(R.id.updateText);
        txUpdateTime = findViewById(R.id.updateTime);
        btnRefresh = findViewById(R.id.refreshButton);
        btnChangelog = findViewById(R.id.buttonChangelog);
        updateView = findViewById(R.id.newUpdate);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              refreshUpdate();
                                          }
                                      });
        txUpdate.setText(R.string.noUpdates);
        try {
            txBuild.setText(getString(R.string.buildText,
                    userDate.format(f.parse(om.currentVersion))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        refreshUpdate();
        //startService(new Intent(this, CheckOTAService.class));
    }

    public void showChangelog(View v) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        Intent intent = new Intent(this, Changelog.class);

        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                v, toIntExact(Math.round(width/ 2)),
                toIntExact(Math.round(height / 2)) , 1, 1);

        this.startActivity(intent, options.toBundle());
    }

    public void downloadUpdate(View v) {
        om.update();
        Toast.makeText(this, getString(R.string.downloading),
                Toast.LENGTH_SHORT).show();
    }

    private void refreshUpdate() {
        Date currentVersion = null;
        Date updateVersion = null;

        try {
            currentVersion = f.parse(om.currentVersion);
            updateVersion = f.parse(om.getUpdateVersion());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (updateVersion != null && currentVersion != null) {
            if (updateVersion.after(currentVersion)) {
                String userVersion = null;
                try {
                    userVersion = userDate.format(f.parse(om.getUpdateVersion()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                txUpdate.setVisibility(View.INVISIBLE);
                txUpdateTime.setText(getString(R.string.updateText, userVersion));
                updateView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void installUpdate(View v) {
        File file = new File(Constants.FILE_DOWNLOAD_DIRECTORY +
                                Constants.FILE_DOWNLOAD_NAME);

        try {
            android.os.RecoverySystem.installPackage(this, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
