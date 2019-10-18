package com.sb.wifistart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sb.wifistart.R;
import com.sb.wifistart.scaners.WifiScanner;

import java.util.ArrayList;

public class WifiInfoActivity extends Activity {

    WifiScanner wifiScanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_info);

        TextView textView = findViewById(R.id.wifiInfoList);
        wifiScanner = new WifiScanner(getApplicationContext());
//        wifiScanner.startScan();

        StringBuilder wifiNetworkInfo = new StringBuilder();
        if(wifiScanner.getRouterDataList() == null) {
            wifiScanner.setRouterDataList(new ArrayList<>());
        }
        wifiScanner.getRouterDataList().forEach(routerData -> {
            wifiNetworkInfo.append("BSSID: " + routerData.getBssid());
            wifiNetworkInfo.append("\n");
            wifiNetworkInfo.append("SSID: " + routerData.getSsid());
            wifiNetworkInfo.append("\n");
            wifiNetworkInfo.append("Rssi: " + routerData.getRssi());
            wifiNetworkInfo.append("\n");
            wifiNetworkInfo.append("Channel: " + routerData.getChannel());
            wifiNetworkInfo.append("\n");
            wifiNetworkInfo.append("=======================================");
            wifiNetworkInfo.append("\n");
        });

        textView.setText(wifiNetworkInfo.toString());

        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.backFromWifiInfo);
        back.setOnClickListener(view -> startActivity(new Intent(WifiInfoActivity.this, MainScreenActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        wifiScanner.registerReceiver(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        wifiScanner.unregisterReceiver(getApplicationContext());
    }
}
