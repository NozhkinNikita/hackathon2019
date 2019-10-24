package com.sb.wifistart.service;

import com.sb.wifistart.httpclient.UnsafeOkHttpClient;
import com.sb.wifistart.httprequests.UserApi;

import retrofit2.converter.gson.GsonConverterFactory;

public class UserApiHolder {

    private static UserApi userApi;
    private static String baseUrl = "https://172.30.14.91:8443";

    private static UserApi getUserApi() {
        if (userApi != null) {
            return userApi;
        } else {
            RestAdapter restAdapter = new RestAdapter(baseUrl,
                    GsonConverterFactory.create(), UnsafeOkHttpClient.getUnsafeOkHttpClient());
            return restAdapter.getUserApi();
        }
    }
}
