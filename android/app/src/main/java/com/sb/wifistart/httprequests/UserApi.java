package com.sb.wifistart.httprequests;

import com.sb.wifistart.dto.Scan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
    @Headers({
            "Content-Type:application/json"
    })
    @POST("/login")
    Call<UserResponse> login(@Body LoginRequest loginRequest);

    @Headers({
            "Content-Type:application/json"
    })
    @GET("/api/security/locations/")
    Call<List<LocationResponse>> getLocations();

    @Headers({
            "Content-Type:application/json"
    })
    @POST("/api/user/scans/")
    Call<CreateScanResponse> createScan(@Body CreateScanRequest createScanRequest);

    @Headers({
            "Content-Type:application/json"
    })
    @GET("/api/user/scans/")
    Call<List<Scan>> getScans();
}