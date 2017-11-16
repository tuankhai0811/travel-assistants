package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuankhai.travelassistants.webservice.main.RequestService;

import java.io.Serializable;

/**
 * Created by Khai on 16/11/2017.
 */

public final class AddScheduleDayDTO {
    public final String status;
    public final ScheduleDay result;
    public final String message;

    @JsonCreator
    public AddScheduleDayDTO(
            @JsonProperty("status") String status,
            @JsonProperty("result") ScheduleDay result,
            @JsonProperty("message") String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public static final class ScheduleDay implements Serializable{
        public final String id;
        public final String id_schedule;
        public final String id_schedule_place;
        public final String place_id;
        public final String email;
        public final String type;
        public final String time_start;
        public final String time_end;
        public final String date;
        public final String description;
        public final String created_at;
        public final String updated_at;

        @JsonCreator
        public ScheduleDay(
                @JsonProperty("id") String id,
                @JsonProperty("id_schedule") String id_schedule,
                @JsonProperty("id_schedule_place") String id_schedule_place,
                @JsonProperty("place_id") String place_id,
                @JsonProperty("email") String email,
                @JsonProperty("type") String type,
                @JsonProperty("time_start") String time_start,
                @JsonProperty("time_end") String time_end,
                @JsonProperty("date") String date,
                @JsonProperty("description") String description,
                @JsonProperty("created_at") String created_at,
                @JsonProperty("updated_at") String updated_at) {
            this.id = id;
            this.id_schedule = id_schedule;
            this.id_schedule_place = id_schedule_place;
            this.place_id = place_id;
            this.email = email;
            this.type = type;
            this.time_start = time_start;
            this.time_end = time_end;
            this.date = date;
            this.description = description;
            this.created_at = created_at;
            this.updated_at = updated_at;
        }
    }

    public boolean isSuccess(){
        if (status.equals(RequestService.RESULT_OK)){
            return true;
        }
        return false;
    }
}
