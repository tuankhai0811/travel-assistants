package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 01/09/2017.
 */

public class GetSliderPlaceRequest extends BasicRequest {
    public final String ROUTES = "PlaceSlider/get/all";

    private String key;

    public GetSliderPlaceRequest(String key) {
        this.key = key;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        return params;
    }

    @Override
    public String[] path() {
        return ROUTES.split("/");
    }
}
