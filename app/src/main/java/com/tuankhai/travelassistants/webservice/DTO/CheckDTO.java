package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Khai on 14/09/2017.
 */

public class CheckDTO {
    public final String status;
    public final boolean result;
    public final String message;

    @JsonCreator
    public CheckDTO(@JsonProperty("status") String status,
                    @JsonProperty("result") boolean result,
                    @JsonProperty("message") String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }
}
