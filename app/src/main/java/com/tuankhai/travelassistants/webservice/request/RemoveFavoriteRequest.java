package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khai on 08/09/2017.
 */

public class RemoveFavoriteRequest extends BasicRequest {
    public final static String GET_ORD_DETAILS = "TravelAssistants/public/Favorite/remove/new";

    String key;
    String idPlace;
    String idUser;

    public RemoveFavoriteRequest(String key, String idPlace, String idUser) {
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
        return GET_ORD_DETAILS.split("/");
    }
}