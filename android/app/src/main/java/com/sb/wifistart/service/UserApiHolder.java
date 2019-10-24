package com.sb.wifistart.service;

import com.google.gson.GsonBuilder;
import com.sb.wifistart.httpclient.UnsafeOkHttpClient;
import com.sb.wifistart.httprequests.UserApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApiHolder {

    private static UserApi userApi;
    private static String baseUrl = "https://172.30.14.91:8443";

    public static UserApi getUserApi() {
        if (userApi != null) {
            return userApi;
        } else {
            OkHttpClient unsafeOkHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                    .client(unsafeOkHttpClient)
                    .build();
            return retrofit.create(UserApi.class);
        }
    }
}
