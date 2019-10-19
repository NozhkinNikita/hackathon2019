package com.sb.wifistart.scaners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.sb.wifistart.dto.RouterData;

import java.util.List;

import lombok.Data;

import static android.content.Context.WIFI_SERVICE;

@Data
public class WifiScanner extends BroadcastReceiver {

    private WifiManager wifiManager;
    private IntentFilter intentFilter;
    List<RouterData> routerDataList;

    public WifiScanner(Context context) {
        this.wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
        if(success) {
            routerDataList.clear();
            List<ScanResult> scanResults = wifiManager.getScanResults();
            scanResults.forEach(scanResult -> {
                RouterData routerData = new RouterData();
                routerData.setBssid(scanResult.BSSID);
                routerData.setSsid(scanResult.SSID);
                routerData.setRssi(scanResult.level);
                routerData.setChannel(scanResult.frequency);
                routerData.setBssid(scanResult.BSSID);

                routerDataList.add(routerData);
            });
        }
    }

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
