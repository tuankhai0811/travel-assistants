package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuank on 02/10/2017.
 */

public class RemoveReviewRequest extends BasicRequest {
    public final String URL = "Review/delete/user";

    String key;
    String email;
    String id_place;

    public RemoveReviewRequest(String key, String email, String id_place) {
        this.key = key;
        this.email = email;
        this.id_place = id_place;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        params.put("email", email);
        params.put("id_place", id_place);
        return params;
    }

    @Override
    public String[] path() {
        return URL.split("/");
    }
}