package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Khai on 04/10/2017.
 */

public final class SearchResultDTO {
    public final String status;
    public final Result result;
    public final String message;

    @JsonCreator
    public SearchResultDTO(
            @JsonProperty("status") String status,
            @JsonProperty("result") Result result,
            @JsonProperty("message") String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public static final class Result {
        public final PlaceDTO.Place places[];

        @JsonCreator
        public Result(@JsonProperty("places") PlaceDTO.Place[] places) {
            this.places = places;
        }
    }
}
