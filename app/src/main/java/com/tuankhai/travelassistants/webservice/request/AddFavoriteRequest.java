package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 08/09/2017.
 */

public class AddFavoriteRequest extends BasicRequest {
    public final String ROUTES = "Favorite/add/new";

    private String key;
    private String idPlace;
    private String idUser;

    public AddFavoriteRequest(String key, String idPlace, String idUser) {
        this.key = key;
        this.idPlace = idPlace;
        this.idUser = idUser;
    }

    @Override
    public Map<String, Object> params() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", key);
        params.put("idPlace", idPlace);
        params.put("idUser", idUser);
        return params;
    }

    @Override
    public String[] path() {
        return ROUTES.split("/");
    }
}