package com.tuankhai.travelassistants.webservice.main;

import android.util.Log;

/**
 * Created by Khai on 31/08/2017.
 */

public class MyCallback<D, E> {

    public void onSuccess(D response) {
    }

    public void onGetCacheSuccess() {
    }

    public void onFailure(E error) {
        Log.e("Webservice", "ParserDTO Fail");
    }

    public void onStart() {
    }

    public void onLostConnection() {
    }

    public void onReconnect() {
    }
}
