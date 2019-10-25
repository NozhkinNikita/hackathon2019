package com.sb.wifistart.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.sb.wifistart.R;
import com.sb.wifistart.common.CommonVarsHolder;
import com.sb.wifistart.dto.Point;
import com.sb.wifistart.dto.RouterData;
import com.sb.wifistart.httprequests.LocationResponse;
import com.sb.wifistart.httprequests.PointCreateRequest;
import com.sb.wifistart.httprequests.PointUpdateRequest;
import com.sb.wifistart.httprequests.UserApi;
import com.sb.wifistart.receiver.WifiReceiver;
import com.sb.wifistart.service.UserApiHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewScanActivity extends AppCompatActivity {

    private TextView locationName;
    private TextView locationStartDate;


    private Spinner pointSpinner;
    private Button addPointBtn;
    private BarChart stackedChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_scan);

        addItemsOnSpinner();

        locationName = findViewById(R.id.locationName);
        locationStartDate = findViewById(R.id.locationStartDate);

        locationName.setText(CommonVarsHolder.locationName);
        locationStartDate.setText(CommonVarsHolder.locationStartDate.replace("T", " "));

        Button addPointBtn = findViewById(R.id.addPointBtn);
        addPointBtn.setOnClickListener(view -> {
                    Intent addPointIntent = new Intent(NewScanActivity.this, AddPointActivity.class);
                    startActivity(addPointIntent);
                }
        );

        stackedChart = findViewById(R.id.newScanBarChart);

        Button startPointScanBtn = findViewById(R.id.startPointScanBtn);
        startPointScanBtn.setOnClickListener(view -> {
            WifiReceiver wifiReceiver = new WifiReceiverImpl(getApplicationContext());
            wifiReceiver.registerReceiver(getApplicationContext());
            wifiReceiver.startScan();
        });
    }

/*    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
       // popupWindow.setBackgroundDrawable((new ColorDrawable(android.graphics.Color.GRAY)));
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
       // popupWindow.showAsDropDown(mBtnPopUpWindow, 0, 0,Gravity.LEFT);
        Drawable d = new ColorDrawable(Color.WHITE);

        R.layout.popup_window.

        d.setAlpha(130);
        getWindow().setBackgroundDrawable(d);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }*/


    // add items into spinner dynamically
    public void addItemsOnSpinner() {

        pointSpinner = (Spinner) findViewById(R.id.pointSpinner);
        List<String> list = CommonVarsHolder.currentPoints.stream().map(point -> point.getName()).collect(Collectors.toList());
        list.add("Do kuchi tochka");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pointSpinner.setAdapter(dataAdapter);
    }

    private class WifiReceiverImpl extends WifiReceiver {

        public WifiReceiverImpl(Context context){
            super(context);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(getWifiManager().EXTRA_RESULTS_UPDATED, false);
            if(success) {
                Spinner pointSpinner = (Spinner) findViewById(R.id.pointSpinner);
                String selectedPointName = pointSpinner.getSelectedItem().toString();
                Point currentPoint = CommonVarsHolder.currentPoints.stream()
                        .filter(point -> point.getName().equals(selectedPointName)).findFirst().get();
                setScanResults(getWifiManager().getScanResults());

                List<RouterData> routerDatas = new ArrayList<>();
                getScanResults().forEach(scanResult -> {
                    RouterData routerData = new RouterData();
                    routerData.setBssid(scanResult.BSSID);
                    routerData.setSsid(scanResult.SSID);
                    routerData.setChannel(scanResult.frequency);
                    routerData.setRssi((double) -scanResult.level);
                    routerData.setPointId(currentPoint.getId());
                    routerDatas.add(routerData);
                });

                PointUpdateRequest pointUpdateRequest = new PointUpdateRequest();
                pointUpdateRequest.setRouterDatas(routerDatas);
                pointUpdateRequest.setBegin(currentPoint.getBegin());
                pointUpdateRequest.setId(currentPoint.getId());


                UserApi userApi = UserApiHolder.getUserApi();

                Call call = userApi.updatePoint(pointUpdateRequest);

                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                         */
                        System.out.println("on add point");
                        Toast.makeText(getApplicationContext(), "Отчет отправлен", Toast.LENGTH_SHORT);
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        System.out.println("on failure add point");
                    }
                });

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
        }
    }
}