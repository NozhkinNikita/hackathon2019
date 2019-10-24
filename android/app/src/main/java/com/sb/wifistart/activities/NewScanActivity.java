package com.sb.wifistart.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.sb.wifistart.R;
import com.sb.wifistart.httprequests.LocationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class NewScanActivity extends AppCompatActivity {

    private BarChart stackedChart;
    private Spinner locationSpinner;

    private Button startScanBtn;

    private static List<LocationResponse> locationResponses;

    public static void setLocationResponses(List<LocationResponse> stlocationResponses) {
        locationResponses = stlocationResponses;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacked_bar);

        addItemsOnSpinner();

        /*WifiReceiver wifiReceiver = new WifiReceiverImpl(getApplicationContext());
        wifiReceiver.registerReceiver(getApplicationContext());
        wifiReceiver.startScan();

        stackedChart = findViewById(R.id.stackedBarChart);

        Button restartScan = findViewById(R.id.restartScan);
        restartScan.setOnClickListener(view -> {
            restartScan.setVisibility(View.INVISIBLE);
            stackedChart.clear();
            wifiReceiver.startScan();
        });*/
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner() {

        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        List<String> list = locationResponses.stream().map(locationResponse -> locationResponse.getName()).collect(Collectors.toList());
        list.add("Do kuchi location");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(dataAdapter);
    }

    /*private class WifiReceiverImpl extends WifiReceiver {

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
    }*/
}