package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuankhai.travelassistants.webservice.main.RequestService;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Khai on 14/11/2017.
 */

public final class ScheduleDetailDTO {
    public final String status;
    public final ScheduleDetail result;
    public final String message;

    @JsonCreator
    public ScheduleDetailDTO(
            @JsonProperty("status") String status,
            @JsonProperty("result") ScheduleDetail result,
            @JsonProperty("message") String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public static final class ScheduleDetail implements Serializable {
        public final String id;
        public final String id_schedule;
        public final String id_place;
        public final String name;
        public final String email;
        public final String date_start;
        public final String date_end;
        public final String length;
        public final String description;
        public final String created_at;
        public final String updated_at;

        @JsonCreator
        public ScheduleDetail(
                @JsonProperty("id") String id,
                @JsonProperty("id_schedule") String id_schedule,
                @JsonProperty("id_place") String id_place,
                @JsonProperty("name") String name,
                @JsonProperty("email") String email,
                @JsonProperty("date_start") String date_start,
                @JsonProperty("date_end") String date_end,
                @JsonProperty("length") String length,
                @JsonProperty("description") String description,
                @JsonProperty("created_at") String created_at,
                @JsonProperty("updated_at") String updated_at) {
            this.id = id;
            this.id_schedule = id_schedule;
            this.id_place = id_place;
            this.name = name;
            this.email = email;
            this.date_start = date_start;
            this.date_end = date_end;
            this.length = length;
            this.description = description;
            this.created_at = created_at;
            this.updated_at = updated_at;
        }

        public Date getStart() {
            return new Date(Long.valueOf(date_start + "000"));
        }

        public Date getEnd() {
            return new Date(Long.valueOf(date_end + "000"));
        }
    }

    public boolean isSuccess() {
        if (status.equals(RequestService.RESULT_OK)) {
            return true;
        }
        return false;
    }
}
