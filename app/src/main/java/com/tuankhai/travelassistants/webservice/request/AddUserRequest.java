package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 08/09/2017.
 */

public class AddUserRequest extends BasicRequest {
    public final static String GET_ORD_DETAILS = "TravelAssistants/public/User/add/new";

    String key;
    String id;
    String name;
    String email;

    public AddUserRequest(String key, String id, String email, String name) {
        this.key = key;
        this.id = id;
        this.email = email;
        this.name = name;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        params.put("id", id);
        params.put("email", email);
        params.put("name", name);
        return params;
    }

    @Override
    public String[] path() {
        return GET_ORD_DETAILS.split("/");
    }
}