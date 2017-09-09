package com.tuankhai.travelassistants.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.GetListProvinceRequest;
import com.tuankhai.travelassistants.webservice.request.GetSliderPlaceRequest;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getAllProvince();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_boto_regular))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
                Utils.saveSliderPlace(SplashScreenActivity.this, (PlaceDTO) response);
                Intent intent = new Intent(SplashScreenActivity.this, BaseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
            }
        }, PlaceDTO.class);
    }

//    class GetImageSlider extends AsyncTask<Void, Void, ArrayList<Bitmap>> {
//
//        SliderPlaceDTO sliderPlaceDTO;
//
//        public GetImageSlider(SliderPlaceDTO sliderPlaceDTO) {
//            this.sliderPlaceDTO = sliderPlaceDTO;
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
//            return arrImg;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
//            super.onPostExecute(bitmaps);
//            if (bitmaps.size() == 0) return;
//            if (bitmaps.size() != sliderPlaceDTO.places.length) return;
//            int size = bitmaps.size();
//            for (int i = 0; i < size; i++) {
//                sliderPlaceDTO.places[i].codeImage = Utils.encodeToBase64(bitmaps.get(i));
//            }
//            Utils.saveSliderPlace(SplashScreenActivity.this, sliderPlaceDTO);
//            Intent intent = new Intent(SplashScreenActivity.this, BaseActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//        }
//    }

}
