package com.tuankhai.travelassistants.webservice.interfaces;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

/**
 * Created by Khai on 31/08/2017.
 */

public interface UploadserviceRequest {

    @Multipart
    @POST("{path0}/{path1}/{path2}/{path3}/{path4}")
    Call<ResponseBody> postImage(
            @Path("path0") String path,
            @Path("path1") String path1,
            @Path("path2") String path2,
            @Path("path3") String path3,
            @Path("path4") String path4,
            @Part MultipartBody.Part file,
            @PartMap Map<String, RequestBody> map
    );
}
