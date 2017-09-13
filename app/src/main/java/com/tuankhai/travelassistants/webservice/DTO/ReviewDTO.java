package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Khai on 13/09/2017.
 */

public final class ReviewDTO {
    public final String status;
    public final PlaceGoogleDTO.Result.Review result[];
    public final String message;

    @JsonCreator
    public ReviewDTO(@JsonProperty("status") String status,
                     @JsonProperty("result") PlaceGoogleDTO.Result.Review[] result,
                     @JsonProperty("message") String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

}
