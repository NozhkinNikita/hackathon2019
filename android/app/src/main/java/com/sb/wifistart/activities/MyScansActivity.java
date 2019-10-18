package com.sb.wifistart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sb.wifistart.R;
import com.sb.wifistart.adapters.ScanAdapter;

import java.util.ArrayList;

public class MyScansActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scans);

        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.backFromMyScans);
        back.setOnClickListener(view -> startActivity(new Intent(MyScansActivity.this, MainScreenActivity.class)));

        RecyclerView myScansListView = (RecyclerView) findViewById(R.id.myScansList);
        myScansListView.setLayoutManager(new LinearLayoutManager(this));
        ScanAdapter scanAdapter = new ScanAdapter(this, new ArrayList<>());
        myScansListView.setAdapter(scanAdapter);
    }
}
