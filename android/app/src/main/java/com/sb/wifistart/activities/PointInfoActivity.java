package com.sb.wifistart.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.sb.wifistart.R;
import com.sb.wifistart.dto.Point;
import com.sb.wifistart.dto.RouterData;
import com.sb.wifistart.httprequests.UserApi;
import com.sb.wifistart.receiver.WifiReceiver;
import com.sb.wifistart.service.UserApiHolder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointInfoActivity extends Activity {

    TableLayout pointDetail;
    BarChart stackedChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_info);
        pointDetail = (TableLayout) findViewById(R.id.pointDetail);
        String pointId = getIntent().getStringExtra("pointId");

        TextView pointInfoPointName = findViewById(R.id.pointInfoPointName);
        pointInfoPointName.setText(getIntent().getStringExtra("pointName"));

        TextView pointBeginLabel = new TextView(getApplicationContext());
        pointBeginLabel.setText("Дата начала");
        pointBeginLabel.setTextAppearance(android.R.style.TextAppearance_Medium);
        pointBeginLabel.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView pointBegin = new TextView(getApplicationContext());
        pointBegin.setText(getIntent().getStringExtra("pointBegin").split("T")[0]);
        pointBegin.setTextAppearance(android.R.style.TextAppearance_Medium);
        pointBegin.setGravity(Gravity.CENTER_HORIZONTAL);

        TableRow pointBeginRow = new TableRow(getApplicationContext());
        pointBeginRow.addView(pointBeginLabel);
        pointBeginRow.addView(pointBegin);
        TableRow.LayoutParams pointBeginRowLP = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        pointBeginRow.setLayoutParams(pointBeginRowLP);
        pointDetail.addView(pointBeginRow);

        TextView pointEndLabel = new TextView(getApplicationContext());
        pointEndLabel.setText("Дата оканчания");
        pointEndLabel.setTextAppearance(android.R.style.TextAppearance_Medium);
        pointEndLabel.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView pointEnd = new TextView(getApplicationContext());
        pointEnd.setText(getIntent().getStringExtra("pointEnd").split("T")[0]);
        pointEnd.setTextAppearance(android.R.style.TextAppearance_Medium);
        pointEnd.setGravity(Gravity.CENTER_HORIZONTAL);

        TableRow pointEndRow = new TableRow(getApplicationContext());
        pointEndRow.addView(pointEndLabel);
        pointEndRow.addView(pointEnd);
        TableRow.LayoutParams pointEndRowLP = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        pointEndRow.setLayoutParams(pointEndRowLP);
        pointDetail.addView(pointEndRow);

        postData(pointId);
    }

    public void postData(String pointId) {
        UserApi yourUserApi = UserApiHolder.getUserApi();
        Call call = yourUserApi.getPoint(pointId);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                 */
                System.out.println("on response login");
                if (response.body() != null) {
                    Point point = (Point) response.body();

                    List<RouterData> routerDatas = point.getRouterDatas();

                    pointDetail = (TableLayout) findViewById(R.id.pointDetail);

                    if (routerDatas != null && routerDatas.size() != 0) {
                        stackedChart = findViewById(R.id.stackedBarChart);
                        List<String> ssidList = new ArrayList<>();
                        List<BarEntry> dataVals = new ArrayList<>();
                        for(int i = 0; i < routerDatas.size(); i++) {
                            ssidList.add(routerDatas.get(i).getSsid() + "\n" + "(" + routerDatas.get(i).getChannel() + ")");
                            dataVals.add(new BarEntry(i, -Float.valueOf(routerDatas.get(i).getRssi().toString())));
                        }

                        XAxis xAxis = stackedChart.getXAxis();
                        xAxis.setLabelRotationAngle(45);
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(ssidList));

                        BarDataSet barDataSet = new BarDataSet(dataVals, "WiFi signals");
                        BarData barData = new BarData(barDataSet);
                        stackedChart.setData(barData);
                        stackedChart.invalidate();

//                        routerDates.forEach(routerData -> {
//                            TableRow row = new TableRow(getApplicationContext());
//                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//                            row.setLayoutParams(lp);
//
//                            TextView ssid = new TextView(getApplicationContext());
//                            TextView bssid = new TextView(getApplicationContext());
//                            TextView channel = new TextView(getApplicationContext());
//                            TextView rssi = new TextView(getApplicationContext());
//
//                            ssid.setText(routerData.getSsid());
//                            ssid.setTextAppearance(android.R.style.TextAppearance_Medium);
//                            ssid.setGravity(Gravity.CENTER_HORIZONTAL);
//
//                            bssid.setText(routerData.getBssid());
//                            bssid.setTextAppearance(android.R.style.TextAppearance_Medium);
//                            bssid.setGravity(Gravity.CENTER_HORIZONTAL);
//
//                            channel.setText(routerData.getChannel());
//                            channel.setTextAppearance(android.R.style.TextAppearance_Medium);
//                            channel.setGravity(Gravity.CENTER_HORIZONTAL);
//
//                            rssi.setText(routerData.getRssi());
//                            rssi.setTextAppearance(android.R.style.TextAppearance_Medium);
//                            rssi.setGravity(Gravity.CENTER_HORIZONTAL);
//
//                            row.addView(rssi);
//                            row.addView(pointBeginDate);
//                            row.addView(pointEndDate);
//                            row.addView(pointWasRescaned);
//
//                            row.setGravity(Gravity.CENTER_HORIZONTAL);
//                            row.setPadding(5, 5, 5, 5);
//
//                            pointDetail.addView(row);
//                        });
                    }
                } else {
//                    info.setText("No of attempts remaining: " + counter);
//                    // ToDo Do the same in NewScan activity
//                    if (counter == 0) {
//                        loginBtn.setEnabled(false);
//                    }
                    System.out.println("Bad response");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

                System.out.println("Fail on pointinfo activity operation");
                /*
                Error callback
                */
            }
        });
    }
}
