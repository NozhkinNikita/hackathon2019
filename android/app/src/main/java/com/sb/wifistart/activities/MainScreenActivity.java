package com.sb.wifistart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sb.wifistart.R;
import com.sb.wifistart.httprequests.LocationResponse;
import com.sb.wifistart.httprequests.UserApi;
import com.sb.wifistart.service.UserApiHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreenActivity extends AppCompatActivity {


    private Button showChartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Button toMyScans = findViewById(R.id.myScans);
        toMyScans.setOnClickListener(view ->
                startActivity(new Intent(MainScreenActivity.this, MyScansActivity.class))
        );

        Button newScan = findViewById(R.id.newScan);
        newScan.setOnClickListener(view -> {
                    UserApi userApi = UserApiHolder.getUserApi();
                    Call call = userApi.getLocations();

                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                             */
                            System.out.println("on get locations");
                            if (response.body() != null) {
                                List<LocationResponse> locationResponses = (List<LocationResponse>) response.body();
                                StackedBarActivity.setLocationResponses(locationResponses);
                            } else {
                            }
                            Intent chartIntent = new Intent(MainScreenActivity.this, StackedBarActivity.class);
                            startActivity(chartIntent);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            System.out.println("on failure get locations");
                        }
                    });
                }
        );
    }
}
