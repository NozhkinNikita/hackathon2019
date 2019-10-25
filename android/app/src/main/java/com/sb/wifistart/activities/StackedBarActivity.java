package com.sb.wifistart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.sb.wifistart.R;
import com.sb.wifistart.common.CommonVarsHolder;
import com.sb.wifistart.dto.Device;
import com.sb.wifistart.dto.Point;
import com.sb.wifistart.httprequests.CreateScanRequest;
import com.sb.wifistart.httprequests.CreateScanResponse;
import com.sb.wifistart.httprequests.LocationResponse;
import com.sb.wifistart.httprequests.UserApi;
import com.sb.wifistart.service.UserApiHolder;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StackedBarActivity extends AppCompatActivity {

    private BarChart stackedChart;
    private Spinner locationSpinner;

    private EditText location;

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

        Button startScan = findViewById(R.id.startScan);
        startScan.setOnClickListener(view -> {
                    UserApi userApi = UserApiHolder.getUserApi();
                    String selectedLocationName = locationSpinner.getSelectedItem().toString();
                    String selectedLocationId = locationResponses.stream().
                            filter(locationResponse -> locationResponse.getName().equals(selectedLocationName))
                            .findFirst().get().getId();
                    CommonVarsHolder.locationId = selectedLocationId;

                    Call call = userApi.createScan(new CreateScanRequest(selectedLocationId, new Device()));
            final boolean[] createScanFinished = {false};
            final boolean[] getPointsFinished = {false};

                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                             */
                            System.out.println("on get locations");
                            if (response.body() != null) {
                                CreateScanResponse createScanResponse = (CreateScanResponse) response.body();
                                CommonVarsHolder.locationName = locationSpinner.getSelectedItem().toString();
                                CommonVarsHolder.locationStartDate = createScanResponse.getBegin();
                                CommonVarsHolder.scanId = createScanResponse.getId();
                                if (getPointsFinished[0]) {
                                    Intent chartIntent = new Intent(StackedBarActivity.this, NewScanActivity.class);
                                    startActivity(chartIntent);
                                }
                                createScanFinished[0] = true;
                            }
                        }
                        @Override
                        public void onFailure(Call call, Throwable t) {
                            System.out.println("on failure get locations");
                        }
                    });
                    Call pointsCall = userApi.getPoints(selectedLocationId);
                    pointsCall.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            System.out.println("on get points");
                            if (response.body() != null) {
                                CommonVarsHolder.currentPoints = (List<Point>) response.body();
                                if (createScanFinished[0]) {
                                    Intent chartIntent = new Intent(StackedBarActivity.this, NewScanActivity.class);
                                    startActivity(chartIntent);
                                }
                                getPointsFinished[0] = true;
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            System.out.println("on failure get locations");
                        }
                    });
                }
        );


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