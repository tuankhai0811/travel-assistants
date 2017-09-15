package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 15/09/2017.
 */

public class EditPlaceRequest extends BasicRequest {
    public final static String GET_ORD_DETAILS = "TravelAssistants/public/Place/edit/all";

    String key;
    String id;
    String rating;
    String address;
    String phone;
    String location_lat;
    String location_lng;
    String opening_hours;
    String website;

    public EditPlaceRequest(
            String key,
            String id,
            String rating,
            String address,
            String phone,
            String location_lat,
            String location_lng,
            String opening_hours,
            String website) {
        this.key = Utils.checkStringNull(key);
        this.id = Utils.checkStringNull(id);
        this.rating = Utils.checkStringNull(rating);
        this.address = Utils.checkStringNull(address);
        this.phone = Utils.checkStringNull(phone);
        this.location_lat = Utils.checkStringNull(location_lat);
        this.location_lng = Utils.checkStringNull(location_lng);
        this.opening_hours = Utils.checkStringNull(opening_hours);
        this.website = Utils.checkStringNull(website);
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        params.put("id", id);
        params.put("rating", rating);
        params.put("address", address);
        params.put("phone", phone);
        params.put("location_lat", location_lat);
        params.put("location_lng", location_lng);
        params.put("opening_hours", opening_hours);
        params.put("website", website);
        return params;
    }

    @Override
    public String[] path() {
        return GET_ORD_DETAILS.split("/");
    }
}