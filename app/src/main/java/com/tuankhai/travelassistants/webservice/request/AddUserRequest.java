package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 08/09/2017.
 */

public class AddUserRequest extends BasicRequest {
    public final String URL = "TravelAssistants/public/User/add/new";

    String key;
    String id;
    String name;
    String email;
    String url_photo;

    public AddUserRequest(String key, String id, String email, String name, String url_photo) {
        this.key = key;
        this.id = id;
        this.email = email;
        this.name = name;
        this.url_photo = Utils.isEmptyString(url_photo) ? "" : url_photo;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        params.put("id", id);
        params.put("email", email);
        params.put("name", name);
        params.put("profile_photo_url", url_photo);
        return params;
    }

    @Override
    public String[] path() {
        return URL.split("/");
    }
}