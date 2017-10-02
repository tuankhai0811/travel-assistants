package com.tuankhai.travelassistants.activity;

import android.content.Intent;
import android.os.Bundle;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.GetListProvinceRequest;
import com.tuankhai.travelassistants.webservice.request.GetSliderPlaceRequest;

public class SplashScreenActivity extends BaseActivity {

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
                if (response == null) {
                    onFailure("response is null");
                    return;
                }
                Utils.saveAllProvince(SplashScreenActivity.this, (ProvinceDTO) response);
                getSliderPlace();
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
                logError("RequestService", error.toString());
            }
        }, ProvinceDTO.class);
    }

    private void getSliderPlace() {
        new RequestService().load(new GetSliderPlaceRequest("key"), false, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                Utils.saveSliderPlace(SplashScreenActivity.this, (PlaceDTO) response);
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
            }
        }, PlaceDTO.class);
    }

}
