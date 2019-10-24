package com.sb.wifistart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sb.wifistart.R;
import com.sb.wifistart.dto.Scan;
import com.sb.wifistart.httprequests.UserApi;
import com.sb.wifistart.service.UserApiHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyScansActivity extends Activity {

    TableLayout myScansListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scans);

        myScansListView = (TableLayout) findViewById(R.id.myScansList);
        postData();
    }

    public void postData() {
        UserApi yourUserApi = UserApiHolder.getUserApi();
        Call call = yourUserApi.getScans();

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                 */
                System.out.println("on response login");
                if (response.body() != null) {
                    List<Scan> scans = (List<Scan>) response.body();
                    scans.sort((o1, o2) -> -o1.getBegin().compareToIgnoreCase(o2.getBegin()));

                    if (scans.size() == 0) {
                        System.out.println("There are no scans");
                    } else {
                        scans.forEach(scan -> {
                            TableRow row = new TableRow(getApplicationContext());
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row.setLayoutParams(lp);

                            TextView scanBeginDate = new TextView(getApplicationContext());
                            TextView scanEndDate = new TextView(getApplicationContext());
                            TextView scanStatus = new TextView(getApplicationContext());

                            scanBeginDate.setText(scan.getBegin().split("T")[0]);
                            scanBeginDate.setTextAppearance(android.R.style.TextAppearance_Large);
                            scanBeginDate.setGravity(Gravity.CENTER_HORIZONTAL);

                            scanEndDate.setText(scan.getEnd().split("T")[0]);
                            scanEndDate.setTextAppearance(android.R.style.TextAppearance_Large);
                            scanEndDate.setGravity(Gravity.CENTER_HORIZONTAL);

                            scanStatus.setText(scan.getStatus().toString());
                            scanStatus.setTextAppearance(android.R.style.TextAppearance_Large);
                            scanStatus.setGravity(Gravity.CENTER_HORIZONTAL);

                            row.addView(scanBeginDate);
                            row.addView(scanEndDate);
                            row.addView(scanStatus);

                            row.setGravity(Gravity.CENTER_HORIZONTAL);
                            row.setPadding(5, 5, 5, 5);

                            if(scan.getPoints().size() > 0) {
                                row.setOnClickListener(view -> {
                                    Intent intent = new Intent(MyScansActivity.this, ScanPointsActivity.class);
                                    startActivity(intent);
                                });
                            }

                            myScansListView.addView(row);
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

                System.out.println("Fail on scan operation");
                /*
                Error callback
                */
            }
        });
    }
}
