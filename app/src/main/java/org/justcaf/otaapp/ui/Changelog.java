package org.justcaf.otaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

public class Changelog extends Activity {

    TextView txChangelog;
    OTAManager om = new OTAManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelog);

        WindowManager.LayoutParams params = getWindow().getAttributes();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        int w = (int) Math.round(width * 0.95);
        int h = (int) Math.round(height * 0.95);

        params.x = 0;
        params.y = 0;
        params.height = h;
        params.width = w;

        this.getWindow().setAttributes(params);

        txChangelog = findViewById(R.id.txChangelog);

        loadChangelog();
    }

    private void loadChangelog() {
        String changelog = om.getChangelog();

        if (changelog.equals("")) {
            changelog = "Not found!";
        }
        txChangelog.setText(changelog);
    }
}
