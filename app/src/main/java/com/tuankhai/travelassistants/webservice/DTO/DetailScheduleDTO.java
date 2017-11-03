package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuankhai.travelassistants.webservice.main.RequestService;

/**
 * Created by tuank on 03/11/2017.
 */

public final class DetailScheduleDTO {
    public final String status;
    public final AddScheduleDTO.Schedule[] result;
    public final String message;

    @JsonCreator
    public DetailScheduleDTO(
            @JsonProperty("status") String status,
            @JsonProperty("result") AddScheduleDTO.Schedule result[],
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
