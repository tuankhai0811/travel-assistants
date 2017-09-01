package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 01/09/2017.
 */

public class GetSliderPlaceRequest extends BasicRequest {
    public final static String GET_ORD_DETAILS = "TravelAssistants/public/PlaceSlider";

    public GetSliderPlaceRequest() {
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        return params;
    }

    @Override
    public String[] path() {
        return GET_ORD_DETAILS.split("/");
    }
}
