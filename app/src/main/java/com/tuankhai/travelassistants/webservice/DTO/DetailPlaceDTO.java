package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuankhai.travelassistants.webservice.main.RequestService;

/**
 * Created by Khai on 13/11/2017.
 */

public class DetailPlaceDTO {
    public final String status;
    public final PlaceDTO.Place place;
    public final String message;

    @JsonCreator
    public DetailPlaceDTO(@JsonProperty("status") String status,
                    @JsonProperty("result") PlaceDTO.Place place,
                    @JsonProperty("message") String message) {
        this.status = status;
        this.place = place;
        this.message = message;
    }

    public boolean isSuccess() {
        if (status.equals(RequestService.RESULT_OK)) {
            return true;
        }
        return false;
    }
}
