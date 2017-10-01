package com.tuankhai.travelassistants.fragment.controller;

import com.tuankhai.travelassistants.activity.MainActivity;
import com.tuankhai.travelassistants.fragment.PlacesFragment;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;

/**
 * Created by Khai on 31/08/2017.
 */

public class PlacesController {
    private PlacesFragment placesFragment;
    private MainActivity mActivity;

    public PlacesController(PlacesFragment fragment) {
        placesFragment = fragment;
        mActivity = (MainActivity) fragment.getActivity();
    }

    public void getAllProvince() {
        ProvinceDTO data = Utils.getAllProvince(mActivity);
        if (data != null)
            placesFragment.setAllProvince(data);
//        final View progress = placesFragment.mRootView.findViewById(R.id.progressBar_loading_list_province);
//        progress.setVisibility(View.VISIBLE);
//        new RequestService().load(new GetListProvinceRequest("key"), false, new MyCallback() {
//            @Override
//            public void onSuccess(Object response) {
//                super.onSuccess(response);
//                progress.setVisibility(View.GONE);
//                placesFragment.setAllProvince((ProvinceDTO) response);
//            }
//
//            @Override
//            public void onFailure(Object error) {
//                super.onFailure(error);
//            }
//        }, ProvinceDTO.class);
    }

    public void getSliderPlace() {
        PlaceDTO data = Utils.getSliderPlace(mActivity);
        if (data != null)
            placesFragment.setSliderPlace(data);
    }

//    class GetImageSlider extends AsyncTask<Void, Void, ArrayList<Bitmap>> {
//
//        SliderPlaceDTO sliderPlaceDTO;
//        View progressBar;
//
//        public GetImageSlider(SliderPlaceDTO sliderPlaceDTO, View progress) {
//            this.sliderPlaceDTO = sliderPlaceDTO;
//            this.progressBar = progress;
//        }
//
//        @Override
//        protected ArrayList<Bitmap> doInBackground(Void... voids) {
//            ArrayList<Bitmap> arrImg = new ArrayList<Bitmap>();
//            for (int i = 0; i < sliderPlaceDTO.places.length; i++) {
//                try {
//                    String address = AppContansts.URL_IMAGE + sliderPlaceDTO.places[i].id + AppContansts.IMAGE_RATIO_4_3;
//                    Log.e("urlImgPlace", address);
//                    URL url = new URL(address);
//                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    arrImg.add(image);
//                } catch (IOException e) {
//                    System.out.println(e);
//                }
//            }
//            Log.e("status", arrImg.size() + "");
//            return arrImg;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
//            super.onPostExecute(bitmaps);
//            if (bitmaps.size() == 0) return;
//            progressBar.setVisibility(View.GONE);
//            placesFragment.setSliderPlace(bitmaps, sliderPlaceDTO.places);
//        }
//    }
}
