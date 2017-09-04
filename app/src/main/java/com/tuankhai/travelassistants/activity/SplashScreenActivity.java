package com.tuankhai.travelassistants.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.model.AllSliderPlace;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;
import com.tuankhai.travelassistants.webservice.DTO.SliderPlaceDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.GetListProvinceRequest;
import com.tuankhai.travelassistants.webservice.request.GetSliderPlaceRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getAllProvince();
    }

    public void getAllProvince() {
        new RequestService().load(new GetListProvinceRequest("key"), false, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                if (response == null){
                    onFailure("response is null");
                    return;
                }
                Utils.saveAllProvince(SplashScreenActivity.this, (ProvinceDTO) response);
                getSliderPlace();
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
                Log.e("RequestService", error.toString());
            }
        }, ProvinceDTO.class);
    }

    private void getSliderPlace() {
        new RequestService().load(new GetSliderPlaceRequest("key"), false, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                new GetImageSlider((SliderPlaceDTO) response).execute();
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
            }
        }, SliderPlaceDTO.class);
    }

    class GetImageSlider extends AsyncTask<Void, Void, ArrayList<Bitmap>> {

        SliderPlaceDTO sliderPlaceDTO;

        public GetImageSlider(SliderPlaceDTO sliderPlaceDTO) {
            this.sliderPlaceDTO = sliderPlaceDTO;
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(Void... voids) {
            ArrayList<Bitmap> arrImg = new ArrayList<Bitmap>();
            for (int i = 0; i < sliderPlaceDTO.places.length; i++) {
                try {
                    String address = AppContansts.URL_IMAGE + sliderPlaceDTO.places[i].id + AppContansts.IMAGE_RATIO_4_3;
                    Log.e("urlImgPlace", address);
                    URL url = new URL(address);
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    arrImg.add(image);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            Log.e("status", arrImg.size() + "");
            return arrImg;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
            super.onPostExecute(bitmaps);
            if (bitmaps.size() == 0) return;
//            saveSliderPlace(bitmaps, sliderPlaceDTO.places);
            if (bitmaps.size() != sliderPlaceDTO.places.length) return;
            int size = bitmaps.size();
            AllSliderPlace.SliderPlace[] list = new AllSliderPlace.SliderPlace[size];
            for (int i = 0; i < size; i++) {
                list[i] = new AllSliderPlace.SliderPlace(
                        sliderPlaceDTO.places[i].id,
                        sliderPlaceDTO.places[i].long_name,
                        Utils.encodeToBase64(bitmaps.get(i)));
            }
            AllSliderPlace data = new AllSliderPlace(list);
            Utils.saveSliderPlace(SplashScreenActivity.this, data);
            Intent intent = new Intent(SplashScreenActivity.this, BaseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void saveSliderPlace(ArrayList<Bitmap> bitmaps, SliderPlaceDTO.Place[] places) {
        if (bitmaps.size() != places.length) return;
        int size = bitmaps.size();
        AllSliderPlace.SliderPlace[] list = new AllSliderPlace.SliderPlace[size];
        for (int i = 0; i < size; i++) {
            list[i] = new AllSliderPlace.SliderPlace(
                    places[i].id,
                    places[i].long_name,
                    Utils.encodeToBase64(bitmaps.get(i)));
        }
        AllSliderPlace data = new AllSliderPlace(list);
        Utils.saveSliderPlace(this, data);
        Intent intent = new Intent(SplashScreenActivity.this, BaseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
