package com.sb.wifistart.httprequests;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserApi {

    //You can use rx.java for sophisticated composition of requests
    /*@POST("/login")
    public Observable<SomeUserModel> fetchUser(@Path("user") String user);

    //or you can just get your model if you use json api
    @GET("/users/{user}")
    public SomeUserModel fetchUser(@Path("user") String user);*/

    //or if there are some special cases you can process your response manually
    @FormUrlEncoded
    @Headers({"Accept: application/json; charset=utf-8"})
    @POST("/login")
    Call<Object> login(@Field("username") String username, @Field("password") String password);

}