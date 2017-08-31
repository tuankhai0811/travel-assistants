package com.tuankhai.travelassistants.webservice.main;

import android.util.Log;

import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.interfaces.UploadserviceRequest;
import com.tuankhai.travelassistants.webservice.interfaces.WebserviceRequest;

import org.json.JSONException;
import org.json.JSONObject;

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

public class MyRequestService {
    public static String BASE_URL = "http://localhost/TravelAssistants";
    private Retrofit retrofit = null;

    public Retrofit getClient(String baseUrl) {
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

    public void load(BasicRequest mainDTO,
                     boolean isShowLoading,
                     final MyCallback callback,
                     final Class returnClass) {
        String[] path = mainDTO.path();
        getClient(BASE_URL)
                .create(WebserviceRequest.class)
                .getAnswers(path(path, 0),
                        path(path, 1),
                        path(path, 2),
                        path(path, 3),
                        path(path, 4),
                        mainDTO.params())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            callback.onSuccess(Utils.readValue(response.body().bytes(), returnClass));
                        } catch (IOException e) {

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    public void load(BasicRequest mainDTO, boolean isShowLoading, final MyCallback callback) {
        String[] path = mainDTO.path();
        getClient(BASE_URL)
                .create(WebserviceRequest.class)
                .getAnswers(path(path, 0),
                        path(path, 1),
                        path(path, 2),
                        path(path, 3),
                        path(path, 4),
                        mainDTO.params())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        JSONObject data = null;
                        JSONObject result = null;
                        try {
                            data = new JSONObject(response.body().string());
                            result = data.getJSONObject("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

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

    public String path(String[] path, int position) {
        if (path != null)
            return position < path.length ? (path[position] + "") : "";
        else
            return "";
    }
}
