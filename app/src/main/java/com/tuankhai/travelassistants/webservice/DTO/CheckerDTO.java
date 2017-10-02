package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuankhai.travelassistants.webservice.main.RequestService;

/**
 * Created by Khai on 14/09/2017.
 */

public class CheckerDTO {
    public final String status;
    public final boolean result;
    public final String message;

    @JsonCreator
    public CheckerDTO(@JsonProperty("status") String status,
                      @JsonProperty("result") boolean result,
                      @JsonProperty("message") String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public boolean isSuccess() {
        if (status.equals(RequestService.RESULT_OK)) {
            return true;
        }
        return false;
    }
}
