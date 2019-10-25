package com.sb.wifistart.httprequests;

import com.sb.wifistart.dto.Point;
import com.sb.wifistart.dto.Router;
import com.sb.wifistart.dto.Scan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
    @POST("/api/user/points/")
    Call<Point> addPoint(@Body PointCreateRequest pointCreateRequest);

    @Headers({
            "Content-Type:application/json"
    })
    @PUT("/api/user/points/")
    Call<Void> updatePoint(@Body PointUpdateRequest pointUpdateRequest);

    @Headers({
            "Content-Type:application/json"
    })
    @GET("/api/user/points/location-points/{id}")
    Call<List<Point>> getPoints(@Path("id") String id);

    @Headers({
            "Content-Type:application/json"
    })
    @GET("/api/user/scans/")
    Call<List<Scan>> getScans();

    @Headers({
            "Content-Type:application/json"
    })
    @GET("/api/user/scans/{scanId}")
    Call<Scan> getScan(@Path("scanId") String scanId);

    @Headers({
            "Content-Type:application/json"
    })
    @GET("/api/user/points/{pointId}")
    Call<Point> getPoint(@Path("pointId") String pointId);

    @Headers({
            "Content-Type:application/json"
    })
    @GET("/api/user/points/{routerId}")
    Call<Router> getRouter(@Path("routerId") String routerId);
}