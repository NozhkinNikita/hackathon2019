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

import java.util.ArrayList;

import okhttp3.OkHttpClient;
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
    }

    public void postData() {

        OkHttpClient okHttpClient = getUnsafeOkHttpClient();

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl("https://172.30.14.62:8443")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        UserApi yourUsersApi = restAdapter.create(UserApi.class);

        Call call = yourUsersApi.login(new LoginRequest(login.getText().toString(), password.getText().toString()));

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                 */
                System.out.println("on response login");
                if (response.body() != null) {
                    Object wResponse = response.body();
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
