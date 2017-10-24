package com.tuankhai.travelassistants.webservice.DTO;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by tuank on 22/10/2017.
 */

public final class AddScheduleDTO {

    public final String status;
    public final Schedule result;
    public final String message;

    @JsonCreator
    public AddScheduleDTO(
            @JsonProperty("status") String status,
            @JsonProperty("result") Schedule result,
            @JsonProperty("message") String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public static final class Schedule implements Comparable<Schedule> {
        public final String id;
        public final String name;
        public final String email;
        public final String date_start;
        public final String date_end;
        public final String length;
        public final String place;
        public final String created_at;
        public final String updated_at;

        @JsonCreator
        public Schedule(
                @JsonProperty("id") String id,
                @JsonProperty("name") String name,
                @JsonProperty("email") String email,
                @JsonProperty("date_start") String date_start,
                @JsonProperty("date_end") String date_end,
                @JsonProperty("length") String length,
                @JsonProperty("place") String place,
                @JsonProperty("created_at") String created_at,
                @JsonProperty("updated_at") String updated_at) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.date_start = date_start + "000";
            this.date_end = date_end + "000";
            this.length = length;
            this.place = place;
            this.created_at = created_at;
            this.updated_at = updated_at;
        }

        @Override
        public int compareTo(@NonNull Schedule schedule) {
            return this.date_start.compareTo(schedule.date_start);
        }
    }
}
