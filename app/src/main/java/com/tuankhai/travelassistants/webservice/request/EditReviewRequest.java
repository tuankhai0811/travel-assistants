package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuank on 02/10/2017.
 */

public class EditReviewRequest extends BasicRequest {
    public final String URL = "Review/edit/user";

    String key;
    String email;
    String id_place;
    String rating;
    String text;
    String time;

    public EditReviewRequest(String key, String email, String id_place, String rating, String text, String time) {
        this.key = key;
        this.email = email;
        this.id_place = id_place;
        this.rating = rating;
        this.text = text;
        this.time = time;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        params.put("email", email);
        params.put("id_place", id_place);
        params.put("rating", rating);
        params.put("text", text);
        params.put("time", time);
        return params;
    }

    @Override
    public String[] path() {
        return URL.split("/");
    }
}