package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by tuank on 22/10/2017.
 */

public final class AllScheduleDTO {
    public final String status;
    public final AddScheduleDTO.Schedule[] result;
    public final String message;

    @JsonCreator
    public AllScheduleDTO(
            @JsonProperty("status") String status,
            @JsonProperty("result") AddScheduleDTO.Schedule result[],
            @JsonProperty("message") String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }
}