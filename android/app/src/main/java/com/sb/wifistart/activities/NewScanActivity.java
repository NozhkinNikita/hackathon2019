package com.sb.wifistart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sb.wifistart.R;

public class NewScanActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_scan);

        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.backFromNewScan);
        back.setOnClickListener(view -> startActivity(new Intent(NewScanActivity.this, MainScreenActivity.class)));
    }
}
