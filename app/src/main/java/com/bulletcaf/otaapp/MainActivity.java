package com.bulletcaf.otaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView buildText;
    String a;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildText = findViewById(R.id.buildText);
        buildText.setText(getString(R.string.buildText) + " " + Build.DISPLAY);
        startService(new Intent(this, CheckOTAService.class));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String a = null;
                    try {
                        a = NetworkController.getUpdate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final String b = a;

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, b, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
    }
}
