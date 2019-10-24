package com.sb.wifistart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sb.wifistart.R;
import com.sb.wifistart.adapters.ScanAdapter;
import com.sb.wifistart.dto.Scan;
import com.sb.wifistart.httprequests.UserApi;
import com.sb.wifistart.service.UserApiHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyScansActivity extends Activity {

    RecyclerView myScansListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scans);

        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.backFromMyScans);
        back.setOnClickListener(view -> startActivity(new Intent(MyScansActivity.this, MainScreenActivity.class)));

        myScansListView = (RecyclerView) findViewById(R.id.myScansList);
        myScansListView.setLayoutManager(new LinearLayoutManager(this));

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
                    List<Scan> scanResponse = (List<Scan>) response.body();
                    if (scanResponse.size() == 0) {
                        System.out.println("There are no scans");
                    } else {
                        ScanAdapter scanAdapter = new ScanAdapter(getApplicationContext(), scanResponse);
                        myScansListView.setAdapter(scanAdapter);
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
