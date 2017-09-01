package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Khai on 01/09/2017.
 */

public final class SliderPlaceDTO {
    public final String status;
    public final Place places[];
    public final String message;

    @JsonCreator
    public SliderPlaceDTO(@JsonProperty("status") String status,
                          @JsonProperty("result") Place[] places,
                          @JsonProperty("message") String message) {
        this.status = status;
        this.places = places;
        this.message = message;
    }

    public static final class Place {
        public final long _id;
        public final String id;
        public final String long_name;
        public final String short_name;
        public final long rating;
        public final String address;
        public final String province_name;
        public final String province_id;
        public final String phone;
        public final String postal_code;
        public final long location_lat;
        public final long location_lng;
        public final String icon;
        public final String logo_3_4;
        public final String logo_4_3;
        public final String logo_16_9;
        public final String opening_hours;
        public final String photos;
        public final String website;
        public final long type_sea;
        public final long type_attractions;
        public final long type_entertainment;
        public final long type_cultural;
        public final long type_spring;
        public final long type_summer;
        public final long type_autumn;
        public final long type_winter;
        public final String description;
        public final String hotels;
        public final String restaurants;
        public final String foods;
        public final String game;
        public final String created_at;
        public final String updated_at;

        @JsonCreator
        public Place(@JsonProperty("_id") long _id,
                     @JsonProperty("id") String id,
                     @JsonProperty("long_name") String long_name,
                     @JsonProperty("short_name") String short_name,
                     @JsonProperty("rating") long rating,
                     @JsonProperty("address") String address,
                     @JsonProperty("province_name") String province_name,
                     @JsonProperty("province_id") String province_id,
                     @JsonProperty("phone") String phone,
                     @JsonProperty("postal_code") String postal_code,
                     @JsonProperty("location_lat") long location_lat,
                     @JsonProperty("location_lng") long location_lng,
                     @JsonProperty("icon") String icon,
                     @JsonProperty("logo_3_4") String logo_3_4,
                     @JsonProperty("logo_4_3") String logo_4_3,
                     @JsonProperty("logo_16_9") String logo_16_9,
                     @JsonProperty("opening_hours") String opening_hours,
                     @JsonProperty("photos") String photos,
                     @JsonProperty("website") String website,
                     @JsonProperty("type_sea") long type_sea,
                     @JsonProperty("type_attractions") long type_attractions,
                     @JsonProperty("type_entertainment") long type_entertainment,
                     @JsonProperty("type_cultural") long type_cultural,
                     @JsonProperty("type_spring") long type_spring,
                     @JsonProperty("type_summer") long type_summer,
                     @JsonProperty("type_autumn") long type_autumn,
                     @JsonProperty("type_winter") long type_winter,
                     @JsonProperty("description") String description,
                     @JsonProperty("hotels") String hotels,
                     @JsonProperty("restaurants") String restaurants,
                     @JsonProperty("foods") String foods,
                     @JsonProperty("game") String game,
                     @JsonProperty("created_at") String created_at,
                     @JsonProperty("updated_at") String updated_at) {
            this._id = _id;
            this.id = id;
            this.long_name = long_name;
            this.short_name = short_name;
            this.rating = rating;
            this.address = address;
            this.province_name = province_name;
            this.province_id = province_id;
            this.phone = phone;
            this.postal_code = postal_code;
            this.location_lat = location_lat;
            this.location_lng = location_lng;
            this.icon = icon;
            this.logo_3_4 = logo_3_4;
            this.logo_4_3 = logo_4_3;
            this.logo_16_9 = logo_16_9;
            this.opening_hours = opening_hours;
            this.photos = photos;
            this.website = website;
            this.type_sea = type_sea;
            this.type_attractions = type_attractions;
            this.type_entertainment = type_entertainment;
            this.type_cultural = type_cultural;
            this.type_spring = type_spring;
            this.type_summer = type_summer;
            this.type_autumn = type_autumn;
            this.type_winter = type_winter;
            this.description = description;
            this.hotels = hotels;
            this.restaurants = restaurants;
            this.foods = foods;
            this.game = game;
            this.created_at = created_at;
            this.updated_at = updated_at;
        }
    }
}
