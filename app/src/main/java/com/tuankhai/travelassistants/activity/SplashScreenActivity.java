package com.tuankhai.travelassistants.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.GetListProvinceRequest;
import com.tuankhai.travelassistants.webservice.request.GetSliderPlaceRequest;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        startAnim();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getAllProvince();
            }
        }, 1200);
    }

    private void startAnim() {
        ImageView imageView = (ImageView) findViewById(R.id.img_logo);
        imageView.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_logo_splash_screen);
        imageView.startAnimation(animation);
    }

    public void getAllProvince() {
        logError("intent");
        long last = Utils.getLastTimeUpdate(this);
        long current = new Date().getTime();
        if (current - last > 4 * 60 * 60 * 1000) {
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
        } else {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }, 500);
        }
    }

    private void getSliderPlace() {
        new RequestService().load(new GetSliderPlaceRequest("key"), false, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                Utils.saveSliderPlace(SplashScreenActivity.this, (PlaceDTO) response);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Utils.saveLastTimeUpdate(SplashScreenActivity.this, new Date().getTime());
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }, 10);
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
            }
        }, PlaceDTO.class);
    }

}
