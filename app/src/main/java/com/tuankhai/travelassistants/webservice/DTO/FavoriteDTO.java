package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Khai on 08/09/2017.
 */

public final class FavoriteDTO {
    public final String status;
    public final PlaceDTO.Place result[];
    public final String message;

    @JsonCreator
    public FavoriteDTO(@JsonProperty("status") String status, @JsonProperty("result") PlaceDTO.Place[] result, @JsonProperty("message") String message){
        this.status = status;
        this.result = result;
        this.message = message;
    }
}
