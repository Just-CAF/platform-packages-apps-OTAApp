package org.justcaf.otaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.justcaf.otaapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    TextView txBuild;
    TextView txUpdate;
    FloatingActionButton btnRefresh;
    OTAManager om = new OTAManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txBuild = findViewById(R.id.buildText);
        txUpdate = findViewById(R.id.updateText);
        btnRefresh = findViewById(R.id.refreshButton);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              refreshUpdate();
                                          }
                                      });
        refreshUpdate();
        //startService(new Intent(this, CheckOTAService.class));
    }

    private void refreshUpdate() {
        txBuild.setText(getString(R.string.buildText) + " " + om.currentVersion);
        txUpdate.setText(getString(R.string.updateText) + " " + om.getVersion());
    }
}
