package com.sb.wifistart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.sb.wifistart.R;
import com.sb.wifistart.common.CommonVarsHolder;
import com.sb.wifistart.dto.Device;
import com.sb.wifistart.httprequests.CreateScanRequest;
import com.sb.wifistart.httprequests.CreateScanResponse;
import com.sb.wifistart.httprequests.LoginRequest;
import com.sb.wifistart.httprequests.PointCreateRequest;
import com.sb.wifistart.httprequests.UserApi;
import com.sb.wifistart.httprequests.UserResponse;
import com.sb.wifistart.service.TokenInterceptor;
import com.sb.wifistart.service.UserApiHolder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);
        Button addPointButton = findViewById(R.id.addPointBtn);
        EditText pointName = findViewById(R.id.newPointName);
        addPointButton.setOnClickListener(view -> {
            UserApi userApi = UserApiHolder.getUserApi();

            Call call = userApi.addPoint(new PointCreateRequest(null, pointName.getText().toString(), CommonVarsHolder.scanId));

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                     */
                    System.out.println("on add point");
                    if (response.body() != null) {
                    }
                }
                @Override
                public void onFailure(Call call, Throwable t) {
                    System.out.println("on failure add point");
                }
            });
        });
    }
}
