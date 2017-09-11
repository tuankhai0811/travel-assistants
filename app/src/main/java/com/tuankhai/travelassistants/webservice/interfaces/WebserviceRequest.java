package com.tuankhai.travelassistants.webservice.interfaces;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Khai on 31/08/2017.
 */

public interface WebserviceRequest {

    @FormUrlEncoded
    @POST("{path0}/{path1}/{path2}/{path3}/{path4}")
    Call<ResponseBody> getAnswers(
            @Path("path0") String path,
            @Path("path1") String path1,
            @Path("path2") String path2,
            @Path("path3") String path3,
            @Path("path4") String path4,
            @FieldMap Map<String, Object> map
    );

//    @GET("{path0}/{path1}/{path2}")
//    Call<ResponseBody> getAnswers(
//            @Path("path0") String path,
//            @Path("path1") String path1,
//            @Path("path2") String path2
//    );

    @GET("maps/api/place/details/json")
    Call<ResponseBody> getPlace(
            @Query("placeid") String placeID,
            @Query("key") String key
    );

    @GET("maps/api/place/nearbysearch/json?")
    Call<ResponseBody> getNearFood(
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("type") String type,
            @Query("keyword") String keyword,
            @Query("key") String key
    );

}