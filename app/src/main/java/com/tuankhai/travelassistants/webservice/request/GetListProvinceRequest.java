package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuank on 31/08/2017.
 */

public class GetListProvinceRequest extends BasicRequest {
    public final String URL = "TravelAssistants/public/Province/get/all";

    String key;

    public GetListProvinceRequest(String key) {
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
        return URL.split("/");
    }
}
