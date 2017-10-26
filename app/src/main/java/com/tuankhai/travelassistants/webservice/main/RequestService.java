package com.tuankhai.travelassistants.webservice.main;

import android.content.Context;
import android.util.Log;

import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceGoogleDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.UtilsService;
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
    public static String BASE_URL = "http://travelassistants.webstarterz.com/TravelAssistants/public/";
    public static String BASE_URL_IMAGE = "http://travelassistants.webstarterz.com/";

    public static String RESULT_OK = "OK";
    public static String RESULT_ERROR = "ERROR";

    private Retrofit retrofit = null;
    private Context context = null;

    static String GOOGLE_URL = "https://maps.googleapis.com/";
    static String LANGUAGE = "vi";
    static String API_KEY = "AIzaSyBu7xN_K7RcHcGgU5lkwJ7qODxfxOHwHdM";
    static String KEY_FOOD = "";
    static String KEY_HOTEL = "";
    static String KEY_ATM = "";
    static String KEY_DRINK = "";
    static String KEY_GAS_STATION = "";
    static String KEY_HOSPITAL = "";
    static String TYPE_FOOD = "restaurant";
    static String TYPE_HOTEL = "lodging";
    static String TYPE_ATM = "atm";
    static String TYPE_DRINK = "cafe";
    static String TYPE_GAS_STATION = "gas_station";
    static String TYPE_HOSPITAL = "hospital";
    static String RADIUS = "1500";
    static String RADIUS_GAS_STATION = "3000";
    static String RADIUS_ATM = "2000";
    static String MAX_WIDTH = "700";
    static String MAX_WIDTH_IMAGE_ADAPTER = "200";
    static String MAX_WIDTH_IMAGE_ADAPTER_HORIZONTAL = "300";
    static String ZOOM = "12";
    static String WIDTH = "700";
    static String HEIGHT = "300";

    public RequestService(Context context) {
        this.context = context;
    }

    public RequestService() {
    }

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
        String result = GOOGLE_URL
                + "maps/api/place/photo?maxwidth=" + MAX_WIDTH + "&photoreference="
                + reference + "&key=" + API_KEY;
        return result;
    }

    public static String getImageAdapter(String reference) {
        String result = GOOGLE_URL
                + "maps/api/place/photo?maxwidth=" + MAX_WIDTH_IMAGE_ADAPTER + "&photoreference="
                + reference + "&key=" + API_KEY;
        return result;
    }

    public static String getImageAdapterHorizontal(String reference) {
        String result = GOOGLE_URL
                + "maps/api/place/photo?maxwidth=" + MAX_WIDTH_IMAGE_ADAPTER_HORIZONTAL + "&photoreference="
                + reference + "&key=" + API_KEY;
        return result;
    }

    public static String getImageStaticMaps(String lat, String lng) {
        String result = GOOGLE_URL
                + "maps/api/staticmap?center="
                + lat + "," + lng
                + "&zoom=" + ZOOM
                + "&size=" + WIDTH + "x" + HEIGHT
                + "&maptype=roadmap"
                + "&markers=color:red%7Clabel:TA%7C" + lat + "," + lng
                + "&key=" + API_KEY;
        return result;
    }

    public void nearPlace(int typeplace, String lat, String lng, String pagetoken, final MyCallback callback) {
        String type = "";
        String key = "";
        String radius = RADIUS;
        switch (typeplace) {
            case AppContansts.INTENT_TYPE_FOOD:
                type = TYPE_FOOD;
                key = KEY_FOOD;
                break;
            case AppContansts.INTENT_TYPE_HOTEL:
                type = TYPE_HOTEL;
                key = KEY_HOTEL;
                break;
            case AppContansts.INTENT_TYPE_ATM:
                radius = RADIUS_ATM;
                type = TYPE_ATM;
                key = KEY_ATM;
                break;
            case AppContansts.INTENT_TYPE_DRINK:
                type = TYPE_DRINK;
                key = KEY_DRINK;
                break;
            case AppContansts.INTENT_TYPE_GAS_STATION:
                radius = RADIUS_GAS_STATION;
                type = TYPE_GAS_STATION;
                key = KEY_GAS_STATION;
                break;
            case AppContansts.INTENT_TYPE_HOSPITAL:
                radius = RADIUS_GAS_STATION;
                type = TYPE_HOSPITAL;
                key = KEY_HOSPITAL;
                break;
        }
        getClient().create(WebserviceRequest.class)
                .getNearFood(lat + "," + lng, radius, type, key, LANGUAGE, pagetoken, API_KEY)
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

    public void nearPlace(int typeplace, String lat, String lng, final MyCallback callback) {
        String type = "";
        String key = "";
        String radius = RADIUS;
        switch (typeplace) {
            case AppContansts.INTENT_TYPE_FOOD:
                type = TYPE_FOOD;
                key = KEY_FOOD;
                break;
            case AppContansts.INTENT_TYPE_HOTEL:
                type = TYPE_HOTEL;
                key = KEY_HOTEL;
                break;
            case AppContansts.INTENT_TYPE_ATM:
                radius = RADIUS_ATM;
                type = TYPE_ATM;
                key = KEY_ATM;
                break;
            case AppContansts.INTENT_TYPE_DRINK:
                type = TYPE_DRINK;
                key = KEY_DRINK;
                break;
            case AppContansts.INTENT_TYPE_GAS_STATION:
                radius = RADIUS_GAS_STATION;
                type = TYPE_GAS_STATION;
                key = KEY_GAS_STATION;
                break;
            case AppContansts.INTENT_TYPE_HOSPITAL:
                radius = RADIUS_GAS_STATION;
                type = TYPE_HOSPITAL;
                key = KEY_HOSPITAL;
                break;
        }
        getClient().create(WebserviceRequest.class)
                .getNearFood(lat + "," + lng, radius, type, key, LANGUAGE, "", API_KEY)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            callback.onSuccess(Utils.readValue(response.body().bytes(), PlaceNearDTO.class));
                        } catch (IOException e) {
                            Log.e(getClass().toString(), "Data err: nearPlace");
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
                .getPlace(placeID, LANGUAGE, API_KEY)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            callback.onSuccess(Utils.readValue(response.body().bytes(), PlaceGoogleDTO.class));
                        } catch (IOException e) {
                            Log.e(getClass().toString(), "Data err: getPlace");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(getClass().toString(), "onFailure");
                    }
                });
    }

    public void load(final BasicRequest mainDTO,
                     final boolean isShowLoading,
                     final MyCallback callback,
                     final Class returnClass) {
        String[] path = mainDTO.path();
        if (isShowLoading) {
            if (context != null) {
                UtilsService.getInstance(context).show();
            }
        }
        getClient(BASE_URL)
                .create(WebserviceRequest.class)
                .getAnswers(path(path, 0), path(path, 1), path(path, 2), mainDTO.params())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (isShowLoading) {
                            if (context != null) {
                                UtilsService.getInstance(context).dismiss();
                                context = null;
                            }
                        }
                        try {
                            callback.onSuccess(Utils.readValue(response.body().bytes(), returnClass));
                        } catch (IOException e) {
                            Log.e(getClass().toString(), "Data err: " + mainDTO.ROUTES);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        context = null;
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
