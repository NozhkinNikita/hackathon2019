package com.sb.wifistart.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sb.wifistart.R;
import com.sb.wifistart.receiver.WifiReceiver;

public class WifiInfoActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_info);

        TextView textView = findViewById(R.id.wifiInfoList);

        WifiReceiver wifiReceiver = new WifiReceiver(getApplicationContext()) {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(getWifiManager().EXTRA_RESULTS_UPDATED, false);
                if(success) {
                    setScanResults(getWifiManager().getScanResults());
                }
                if(getScanResults() != null) {
                    StringBuilder wifiNetworkInfo = new StringBuilder();
                    getScanResults().forEach(scanResult -> {
                        wifiNetworkInfo.append("BSSID: " + scanResult.BSSID);
                        wifiNetworkInfo.append("\n");
                        wifiNetworkInfo.append("SSID: " + scanResult.SSID);
                        wifiNetworkInfo.append("\n");
                        wifiNetworkInfo.append("Rssi: " + scanResult.level);
                        wifiNetworkInfo.append("\n");
                        wifiNetworkInfo.append("Channel: " + scanResult.frequency);
                        wifiNetworkInfo.append("\n");
                        wifiNetworkInfo.append("=======================================");
                        wifiNetworkInfo.append("\n");
                    });
                    textView.setText(wifiNetworkInfo.toString());
                }
            }
        };
        wifiReceiver.registerReceiver(getApplicationContext());
        wifiReceiver.startScan();

        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.backFromWifiInfo);
        back.setOnClickListener(view -> startActivity(new Intent(WifiInfoActivity.this, MainScreenActivity.class)));
    }
}
