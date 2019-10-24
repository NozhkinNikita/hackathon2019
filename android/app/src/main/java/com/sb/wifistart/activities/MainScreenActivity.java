package com.sb.wifistart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sb.wifistart.R;

public class MainScreenActivity extends AppCompatActivity {


    private Button showChartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Button toMyScans = findViewById(R.id.myScans);
        toMyScans.setOnClickListener(view ->
                startActivity(new Intent(MainScreenActivity.this, MyScansActivity.class))
        );

        Button newScan = findViewById(R.id.newScan);
        newScan.setOnClickListener(view -> {
                Intent chartIntent = new Intent(MainScreenActivity.this, StackedBarActivity.class);
                startActivity(chartIntent);
            }
        );
    }
}
