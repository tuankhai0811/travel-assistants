package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Khai on 31/08/2017.
 */

public final class ProvinceDTO {
    public final String status;
    public final Province provinces[];
    public final String message;

    @JsonCreator
    public ProvinceDTO(@JsonProperty("status") String status,
                       @JsonProperty("result") Province[] provinces,
                       @JsonProperty("message") String message) {
        this.status = status;
        this.provinces = provinces;
        this.message = message;
    }

    public static final class Province {
        public final long _id;
        public final String id;
        public final String name;
        public final long num_place;
        public final String created_at;
        public final String updated_at;

        @JsonCreator
        public Province(@JsonProperty("_id") long _id,
                        @JsonProperty("id") String id,
                        @JsonProperty("name") String name,
                        @JsonProperty("num_place") long num_place,
                        @JsonProperty("created_at") String created_at,
                        @JsonProperty("updated_at") String updated_at) {
            this._id = _id;
            this.id = id;
            this.name = name;
            this.num_place = num_place;
            this.created_at = created_at;
            this.updated_at = updated_at;
        }
    }
}