package com.bulletcaf.otaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView buildText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildText = findViewById(R.id.buildText);
        buildText.setText(getString(R.string.buildText) + " " + Build.DISPLAY);
    }
}
