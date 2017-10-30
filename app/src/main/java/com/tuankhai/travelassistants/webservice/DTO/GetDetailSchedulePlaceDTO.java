package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Khai on 30/10/2017.
 */

public final class GetDetailSchedulePlaceDTO {
    public final String status;
    public final AddSchedulePlaceDTO.SchedulePlace result;
    public final String message;

    @JsonCreator
    public GetDetailSchedulePlaceDTO(
            @JsonProperty("status") String status,
            @JsonProperty("result") AddSchedulePlaceDTO.SchedulePlace result,
            @JsonProperty("message") String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }
}
