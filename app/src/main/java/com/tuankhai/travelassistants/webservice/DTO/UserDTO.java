package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Khai on 08/09/2017.
 */

public final class UserDTO {
    public final String status;
    public final Result result;
    public final String message;

    @JsonCreator
    public UserDTO(
            @JsonProperty("status") String status,
            @JsonProperty("result") Result result,
            @JsonProperty("message") String message){
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public static final class Result {
        public final Original original;
        public final Headers headers;

        @JsonCreator
        public Result(
                @JsonProperty("original") Original original,
                @JsonProperty("headers") Headers headers){
            this.original = original;
            this.headers = headers;
        }

        public static final class Original {
            public final long id;
            public final String name;
            public final String email;
            public final String updated_at;
            public final String created_at;

            @JsonCreator
            public Original(
                    @JsonProperty("id") long id,
                    @JsonProperty("name") String name,
                    @JsonProperty("email") String email,
                    @JsonProperty("updated_at") String updated_at,
                    @JsonProperty("created_at") String created_at){
                this.id = id;
                this.name = name;
                this.email = email;
                this.updated_at = updated_at;
                this.created_at = created_at;
            }
        }

        public static final class Headers {

            @JsonCreator
            public Headers(){
            }
        }
    }
}
