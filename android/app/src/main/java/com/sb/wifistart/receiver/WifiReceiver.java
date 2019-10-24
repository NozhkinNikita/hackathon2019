package com.sb.wifistart.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

import lombok.Data;

import static android.content.Context.WIFI_SERVICE;

@Data
public abstract class WifiReceiver extends BroadcastReceiver {

    private WifiManager wifiManager;
    private IntentFilter intentFilter;
    List<ScanResult> scanResults;

    public WifiReceiver(Context context) {
        this.wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
    }

    @Override
    public abstract void onReceive(Context context, Intent intent);

    public void startScan() {
        wifiManager.startScan();
    }

    public void registerReceiver(Context context) {
        context.registerReceiver(this, intentFilter);
    }

    public void unregisterReceiver(Context context) {
        context.unregisterReceiver(this);
    }
}
