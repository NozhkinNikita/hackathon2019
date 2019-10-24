package com.sb.wifistart.service;

import com.sb.wifistart.httpclient.UnsafeOkHttpClient;
import com.sb.wifistart.httprequests.UserApi;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApiHolder {

    private static UserApi userApi;
    private static String baseUrl = "https://172.30.14.91:8443";

    public static UserApi getUserApi() {
        if (userApi != null) {
            return userApi;
        } else {
            OkHttpClient unsafeOkHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
            RestAdapter restAdapter = new RestAdapter(baseUrl,
                    GsonConverterFactory.create(), unsafeOkHttpClient);
            return restAdapter.getUserApi();
        }
    }
}
