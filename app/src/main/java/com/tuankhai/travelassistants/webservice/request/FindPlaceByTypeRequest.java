package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 07/09/2017.
 */

public class FindPlaceByTypeRequest extends BasicRequest{
    public final static String GET_ORD_DETAILS = "TravelAssistants/public/Place/find/type";

    String key;
    String type;

    public FindPlaceByTypeRequest(String key, String type) {
        this.key = key;
        this.type = type;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        params.put("type", type);
        return params;
    }

    @Override
    public String[] path() {
        return GET_ORD_DETAILS.split("/");
    }
}
