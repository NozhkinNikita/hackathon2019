package com.sb.wifistart.service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    private static String token = null;

    public static void setToken(String token) {
        TokenInterceptor.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (token == null) {
            return chain.proceed(originalRequest);
        } else {
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Kfmn " + token)
                    .build();
            return chain.proceed(newRequest);
        }
    }
}

//    OkHttpClient okHttpClient = new OkHttpClient();
//okHttpClient.networkInterceptors().add(new MyOkHttpInterceptor());
//    OkClient okClient = new OkClient(okHttpClient);
//    YourApi api = new RestAdapter.Builder()
//            .setEndpoint(url)
//            .setClient(okClient)
//            .build()
//            .create(YourApi.class);