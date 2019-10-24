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
import com.sb.wifistart.httpclient.UnsafeOkHttpClient;
import com.sb.wifistart.httprequests.GetScanRequest;
import com.sb.wifistart.httprequests.ScanApi;
import com.sb.wifistart.httprequests.ScanResponse;
import com.sb.wifistart.httprequests.UserApi;
import com.sb.wifistart.service.RestAdapter;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyScansActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scans);

        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.backFromMyScans);
        back.setOnClickListener(view -> startActivity(new Intent(MyScansActivity.this, MainScreenActivity.class)));

        RecyclerView myScansListView = (RecyclerView) findViewById(R.id.myScansList);
        myScansListView.setLayoutManager(new LinearLayoutManager(this));
        ScanAdapter scanAdapter = new ScanAdapter(this, new ArrayList<>());
        myScansListView.setAdapter(scanAdapter);

        postData();
    }

    public void postData() {
        RestAdapter restAdapter = new RestAdapter("https://192.168.43.40:8443", GsonConverterFactory.create(),
                UnsafeOkHttpClient.getUnsafeOkHttpClient());
        UserApi yourScanApi = restAdapter.getUserApi(UserApi.class);
        Call call = yourScanApi.getScans();

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                 */
                System.out.println("on response login");
                if (response.body() != null) {
                    ScanResponse scanResponse = (ScanResponse) response.body();

                    int a;
                    a = 5 + 20;
                } else {
//                    info.setText("No of attempts remaining: " + counter);
//                    // ToDo Do the same in NewScan activity
//                    if (counter == 0) {
//                        loginBtn.setEnabled(false);
//                    }
                    System.out.println("There are no scans");
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {

                System.out.println("on failure login");
                /*
                Error callback
                */
            }
        });
    }
}
