package com.sb.wifistart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sb.wifistart.R;
import com.sb.wifistart.common.CommonVarsHolder;
import com.sb.wifistart.dto.Point;
import com.sb.wifistart.dto.Scan;
import com.sb.wifistart.httprequests.UserApi;
import com.sb.wifistart.service.UserApiHolder;
import com.sb.wifistart.utils.TableContentHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanPointsActivity extends Activity {

    TableLayout pointsListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_points);
        postData(getIntent().getStringExtra("scanId"));
    }

    public void postData(String scanId) {
        UserApi yourUserApi = UserApiHolder.getUserApi();
        Call call = yourUserApi.getScan(scanId);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                 */
                System.out.println("on response login");
                if (response.body() != null) {
                    Scan scan = (Scan) response.body();

                    List<Point> points = scan.getPoints();

                    pointsListView = (TableLayout) findViewById(R.id.pointsList);

                    if (points.size() == 0) {
                        pointsListView.addView(TableContentHelper.getRowWithEmptyMessage(getApplicationContext(), 4));
                    } else {
                        points.forEach(point -> {
                            TableRow row = new TableRow(getApplicationContext());
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row.setLayoutParams(lp);

                            TextView pointName = new TextView(getApplicationContext());
                            TextView pointBeginDate = new TextView(getApplicationContext());
                            TextView pointEndDate = new TextView(getApplicationContext());
                            TextView pointWasRescaned = new TextView(getApplicationContext());

                            pointName.setText(point.getName());
                            pointName.setTextAppearance(android.R.style.TextAppearance_Medium);
                            pointName.setGravity(Gravity.CENTER_HORIZONTAL);

                            if(point.getBegin() != null) {
                                pointBeginDate.setText(point.getBegin().split("T")[0]);
                            }
                            pointBeginDate.setTextAppearance(android.R.style.TextAppearance_Medium);
                            pointBeginDate.setGravity(Gravity.CENTER_HORIZONTAL);

                            if(point.getEnd() != null) {
                                pointEndDate.setText(point.getEnd().split("T")[0]);
                            }
                            pointEndDate.setTextAppearance(android.R.style.TextAppearance_Medium);
                            pointEndDate.setGravity(Gravity.CENTER_HORIZONTAL);

                            pointWasRescaned.setText(point.getIsRepeat() ? "Да" : "Нет");
                            pointWasRescaned.setTextAppearance(android.R.style.TextAppearance_Medium);
                            pointWasRescaned.setGravity(Gravity.CENTER_HORIZONTAL);

                            row.addView(pointName);
                            row.addView(pointBeginDate);
                            row.addView(pointEndDate);
                            row.addView(pointWasRescaned);

                            row.setGravity(Gravity.CENTER_HORIZONTAL);
                            row.setPadding(5, 5, 5, 5);

//                            if(CommonVarsHolder.userResponse != null && CommonVarsHolder.userResponse.getRoles().contains("NETWORK_ADMIN")) {
                                row.setOnClickListener(view -> {
                                    Intent intent = new Intent(ScanPointsActivity.this, PointInfoActivity.class);
                                    intent.putExtra("pointId", point.getId());
                                    intent.putExtra("pointName", point.getName());
                                    intent.putExtra("pointBegin", point.getBegin());
                                    intent.putExtra("pointEnd", point.getEnd());

                                    startActivity(intent);
                                });
//                            }

                            pointsListView.addView(row);
                        });
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

                System.out.println("Fail on getPoints operation");
                /*
                Error callback
                */
            }
        });
    }
}
