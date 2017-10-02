package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 08/09/2017.
 */

public class GetFavoriteRequest extends BasicRequest {
    public final String ROUTES = "Favorite/get/user";

    private String key;
    private String idUser;

    public GetFavoriteRequest(String key, String idUser) {
        this.key = key;
        this.idUser = idUser;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        params.put("idUser", idUser);
        return params;
    }

    @Override
    public String[] path() {
        return ROUTES.split("/");
    }
}
