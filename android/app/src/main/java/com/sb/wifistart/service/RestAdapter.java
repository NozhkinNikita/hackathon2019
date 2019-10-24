package com.sb.wifistart.service;

import com.sb.wifistart.httpclient.UnsafeOkHttpClient;
import com.sb.wifistart.httprequests.Api;
import com.sb.wifistart.httprequests.Request;
import com.sb.wifistart.httprequests.UserApi;

import lombok.Data;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Data
public class RestAdapter {

    private String baseUrl;
    private GsonConverterFactory gsonConverterFactory;
    private OkHttpClient okHttpClient;

    private Retrofit retrofit;

    public RestAdapter(String baseUrl, GsonConverterFactory gsonConverterFactory,
                       OkHttpClient okHttpClient) {
        this.baseUrl = baseUrl;
        this.gsonConverterFactory = gsonConverterFactory;
        this.okHttpClient = okHttpClient;
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build();
    }


    public UserApi getUserApi() {
        return retrofit.create(UserApi.class);
    }
}
