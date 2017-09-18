package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 13/09/2017.
 */

public class AddReviewRequest extends BasicRequest {
    public final String URL = "TravelAssistants/public/Review/add/user";

    String key;
    String author_name;
    String email;
    String profile_photo_url;
    String id_place;
    String rating;
    String text;
    String time;

    public AddReviewRequest(String key,
                            String author_name,
                            String email,
                            String profile_photo_url,
                            String id_place,
                            String rating,
                            String text,
                            String time) {
        this.key = key;
        this.author_name = author_name;
        this.email = email;
        this.profile_photo_url = profile_photo_url;
        this.id_place = id_place;
        this.rating = rating;
        this.text = text;
        this.time = time;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        params.put("author_name", author_name);
        params.put("email", email);
        params.put("profile_photo_url", profile_photo_url);
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