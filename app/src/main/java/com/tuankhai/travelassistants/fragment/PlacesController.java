package com.tuankhai.travelassistants.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.BaseActivity;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;
import com.tuankhai.travelassistants.webservice.DTO.SliderPlaceDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.GetListProvinceRequest;
import com.tuankhai.travelassistants.webservice.request.GetSliderPlaceRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Khai on 31/08/2017.
 */

public class PlacesController {
    private PlacesFragment placesFragment;
    private BaseActivity mActivity;

    public PlacesController(PlacesFragment fragment) {
        placesFragment = fragment;
        mActivity = (BaseActivity) fragment.getActivity();
    }

    public void getAllProvince() {
        final View progress = placesFragment.mRootView.findViewById(R.id.progressBar_loading_list_province);
        progress.setVisibility(View.VISIBLE);
        new RequestService().load(new GetListProvinceRequest(), false, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                progress.setVisibility(View.GONE);
                placesFragment.setAllProvince((ProvinceDTO) response);
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
            }
        }, ProvinceDTO.class);
    }

    public void getSliderPlace() {
        final View progress = placesFragment.mRootView.findViewById(R.id.progressBar_loading_slider_place);
        progress.setVisibility(View.VISIBLE);
        new RequestService().load(new GetSliderPlaceRequest(), false, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                new GetImageSlider((SliderPlaceDTO) response, progress).execute();
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
            }
        }, SliderPlaceDTO.class);
    }

    class GetImageSlider extends AsyncTask<Void, Void, ArrayList<Bitmap>> {

        SliderPlaceDTO sliderPlaceDTO;
        View progressBar;

        public GetImageSlider(SliderPlaceDTO sliderPlaceDTO, View progress) {
            this.sliderPlaceDTO = sliderPlaceDTO;
            this.progressBar = progress;
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
            progressBar.setVisibility(View.GONE);
            placesFragment.setSliderPlace(bitmaps, sliderPlaceDTO.places);
        }
    }
}
