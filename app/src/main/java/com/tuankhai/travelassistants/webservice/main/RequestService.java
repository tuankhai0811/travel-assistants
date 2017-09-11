package com.tuankhai.travelassistants.webservice.main;

import android.util.Log;

import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceGoogleDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.interfaces.UploadserviceRequest;
import com.tuankhai.travelassistants.webservice.interfaces.WebserviceRequest;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Khai on 31/08/2017.
 */

public class RequestService {
    public static String BASE_URL = "http://travelassistants.webstarterz.com/";
    public static String RESULT_OK = "OK";
    //public static String BASE_URL = "http://192.168.0.117/";
    //public static String BASE_URL = "http://192.168.1.31/";
    private Retrofit retrofit = null;

    static String GOOGLE_URL = "https://maps.googleapis.com/";
    static String API_KEY = "AIzaSyDAPRe0tK-LZ0dS-ecmkkJ6u_oibJcd8Pg";
    static String KEY_FOOD = "restaurant";
    static String TYPE_FOOD = "food";
    static String RADIUS = "1500";
    static String MAX_WIDTH = "800";

    private Retrofit getClient(String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit;
    }

    private Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl(GOOGLE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit;
    }

    public static String getImage(String reference) {
        String result = GOOGLE_URL + "maps/api/place/photo?maxwidth=" + MAX_WIDTH + "&photoreference=" + reference + "&key=" + API_KEY;
        return result;
    }

    public void nearPlaceFood(String lat, String lng, final MyCallback callback) {
        getClient().create(WebserviceRequest.class)
                .getNearFood(lat + "," + lng, RADIUS, TYPE_FOOD, KEY_FOOD, API_KEY)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            callback.onSuccess(Utils.readValue(response.body().bytes(), PlaceNearDTO.class));
                        } catch (IOException e) {
                            Log.e(getClass().toString(), "Data err");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(getClass().toString(), "onFailure");
                    }
                });
    }

    public void getPlace(String placeID, final MyCallback callback) {
        getClient().create(WebserviceRequest.class)
                .getPlace(placeID, API_KEY)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            callback.onSuccess(Utils.readValue(response.body().bytes(), PlaceGoogleDTO.class));
                        } catch (IOException e) {
                            Log.e(getClass().toString(), "Data err");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(getClass().toString(), "onFailure");
                    }
                });
    }

    public void load(BasicRequest mainDTO,
                     boolean isShowLoading,
                     final MyCallback callback,
                     final Class returnClass) {
        String[] path = mainDTO.path();
        getClient(BASE_URL)
                .create(WebserviceRequest.class)
                .getAnswers(path(path, 0), path(path, 1), path(path, 2), path(path, 3), path(path, 4), mainDTO.params())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            callback.onSuccess(Utils.readValue(response.body().bytes(), returnClass));
                        } catch (IOException e) {
                            Log.e(getClass().toString(), "Data err");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(getClass().toString(), "onFailure");
                    }
                });
    }

    public void upload(UploadRequest uploadDTO,
                       boolean isShowLoading,
                       final MyCallback callback,
                       final Class returnClass) {
        String[] path = uploadDTO.path();
        File file = new File(uploadDTO.pathFile());
        RequestBody reqFile = RequestBody.create(MediaType.parse(uploadDTO.mediaType()), file);
        MultipartBody.Part body = MultipartBody.Part
                .createFormData(uploadDTO.keyFile(),
                        file.getName(),
                        reqFile);

        getClient(BASE_URL)
                .create(UploadserviceRequest.class)
                .postImage(path(path, 0),
                        path(path, 1),
                        path(path, 2),
                        path(path, 3),
                        path(path, 4),
                        body,
                        uploadDTO.params())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                callback.onSuccess(Utils.readValue(response.body().bytes(), returnClass));
                            } catch (IOException e) {
                                Log.e("error upload", "");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        callback.onFailure(t.toString());

                    }
                });
    }

    private String path(String[] path, int position) {
        if (path != null)
            return position < path.length ? (path[position] + "") : "";
        else
            return "";
    }


}
