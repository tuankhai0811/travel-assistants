package com.tuankhai.travelassistants.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;
import com.tuankhai.travelassistants.webservice.DTO.SliderPlaceDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Khai on 31/08/2017.
 */

public class Utils {

    public static <T> T readValue(byte[] data, Class<T> valueType) throws IOException {
        return getObjectMapper().readValue(data, valueType);
    }

    public static <T> T readValue(String data, Class<T> valueType) throws IOException {
        return getObjectMapper().readValue(data, valueType);
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        objectMapper.disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }

    public static void saveAllProvince(Context context, ProvinceDTO data) {
        SharedPreferences preferences = context.getSharedPreferences(AppContansts.SHAREDPRE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(AppContansts.SHAREDPRE_ALLPROVINCE, json);
        editor.commit();
    }

    public static ProvinceDTO getAllProvince(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AppContansts.SHAREDPRE_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(AppContansts.SHAREDPRE_ALLPROVINCE, "");
        if (Utils.isEmptyString(json)) return null;
        return gson.fromJson(json, ProvinceDTO.class);
    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static void saveSliderPlace(Context context, SliderPlaceDTO data) {
        SharedPreferences preferences = context.getSharedPreferences(AppContansts.SHAREDPRE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(AppContansts.SHAREDPRE_SLIDERPLACE, json);
        editor.commit();
    }

    public static SliderPlaceDTO getSliderPlace(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AppContansts.SHAREDPRE_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(AppContansts.SHAREDPRE_SLIDERPLACE, "");
        Log.e("status", json);
        if (Utils.isEmptyString(json)) return null;
        return gson.fromJson(json, SliderPlaceDTO.class);
    }

    public static boolean isEmptyString(String string) {
        if (string == null || string.trim().equals("")) return true;
        return false;
    }

    /**
     * Converting dp to pixel
     */
    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
