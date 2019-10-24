package com.sb.wifistart.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.sb.wifistart.R;
import com.sb.wifistart.receiver.WifiReceiver;

import java.util.ArrayList;
import java.util.List;

public class StackedBarActivity extends AppCompatActivity {

    private BarChart stackedChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacked_bar);

        WifiReceiver wifiReceiver = new WifiReceiverImpl(getApplicationContext());
        wifiReceiver.registerReceiver(getApplicationContext());
        wifiReceiver.startScan();

        stackedChart = findViewById(R.id.stackedBarChart);

        Button restartScan = findViewById(R.id.restartScan);
        restartScan.setOnClickListener(view -> {
            restartScan.setVisibility(View.INVISIBLE);
            stackedChart.clear();
            wifiReceiver.startScan();
        });
    }

    private class WifiReceiverImpl extends WifiReceiver {

        public WifiReceiverImpl(Context context){
            super(context);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(getWifiManager().EXTRA_RESULTS_UPDATED, false);
            if(success) {
                setScanResults(getWifiManager().getScanResults());
            }

            List<String> ssidList = new ArrayList<>();
            List<BarEntry> dataVals = new ArrayList<>();
            for(int i = 0; i < getScanResults().size(); i++) {
                ssidList.add(getScanResults().get(i).SSID + "\n" + "(" + getScanResults().get(i).frequency + ")");
                dataVals.add(new BarEntry(i, -getScanResults().get(i).level));
            }

            XAxis xAxis = stackedChart.getXAxis();
            xAxis.setLabelRotationAngle(45);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(ssidList));

            BarDataSet barDataSet = new BarDataSet(dataVals, "WiFi signals");
            BarData barData = new BarData(barDataSet);
            stackedChart.setData(barData);
            stackedChart.invalidate();

            findViewById(R.id.restartScan).setVisibility(View.VISIBLE);
        }
    }
}