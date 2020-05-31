package org.justcaf.otaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView txBuild;
    TextView txUpdate;
    TextView txUpdateTime;
    LinearLayout updateView;
    FloatingActionButton btnRefresh;
    OTAManager om = new OTAManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txBuild = findViewById(R.id.buildText);
        txUpdate = findViewById(R.id.updateText);
        txUpdateTime = findViewById(R.id.updateTime);
        btnRefresh = findViewById(R.id.refreshButton);
        updateView = findViewById(R.id.newUpdate);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              refreshUpdate();
                                          }
                                      });
        txUpdate.setText(R.string.noUpdates);
        txBuild.setText(getString(R.string.buildText, om.currentVersion));
        refreshUpdate();
        //startService(new Intent(this, CheckOTAService.class));
    }

    private void refreshUpdate() {
        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
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
                SimpleDateFormat user = new SimpleDateFormat("dd.MM.yyyy");
                String userVersion = null;
                try {
                    userVersion = user.format(f.parse(om.getUpdateVersion()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                txUpdate.setVisibility(View.INVISIBLE);
                txUpdateTime.setText(getString(R.string.updateText, userVersion));
                updateView.setVisibility(View.VISIBLE);
            }
        }
    }
}
