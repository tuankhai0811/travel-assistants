package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuankhai.travelassistants.webservice.main.RequestService;

/**
 * Created by Khai on 30/10/2017.
 */

public final class AddSchedulePlaceDTO {
    public final String status;
    public final SchedulePlace result;
    public final String message;

    @JsonCreator
    public AddSchedulePlaceDTO(
            @JsonProperty("status") String status,
            @JsonProperty("result") SchedulePlace result,
            @JsonProperty("message") String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public static final class SchedulePlace {
        public final String id_schedule;
        public final String id_place;
        public final String email;
        public final String date_start;
        public final String date_end;
        public final String description;
        public final String length;
        public final String updated_at;
        public final String created_at;
        public final String id;

        @JsonCreator
        public SchedulePlace(
                @JsonProperty("id_schedule") String id_schedule,
                @JsonProperty("id_place") String id_place,
                @JsonProperty("email") String email,
                @JsonProperty("date_start") String date_start,
                @JsonProperty("date_end") String date_end,
                @JsonProperty("description") String description,
                @JsonProperty("length") String length,
                @JsonProperty("updated_at") String updated_at,
                @JsonProperty("created_at") String created_at,
                @JsonProperty("id") String id) {
            this.id_schedule = id_schedule;
            this.id_place = id_place;
            this.email = email;
            this.date_start = date_start;
            this.date_end = date_end;
            this.description = description;
            this.length = length;
            this.updated_at = updated_at;
            this.created_at = created_at;
            this.id = id;
        }
    }

    public boolean isSuccess() {
        if (status.equals(RequestService.RESULT_OK)) {
            return true;
        }
        return false;
    }
}
