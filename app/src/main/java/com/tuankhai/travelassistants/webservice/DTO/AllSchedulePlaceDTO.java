package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuankhai.travelassistants.webservice.main.RequestService;

/**
 * Created by tuank on 03/11/2017.
 */

public class AllSchedulePlaceDTO {
    public final String status;
    public final AddSchedulePlaceDTO.SchedulePlace[] result;
    public final String message;

    @JsonCreator
    public AllSchedulePlaceDTO(
            @JsonProperty("status") String status,
            @JsonProperty("result") AddSchedulePlaceDTO.SchedulePlace result[],
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
