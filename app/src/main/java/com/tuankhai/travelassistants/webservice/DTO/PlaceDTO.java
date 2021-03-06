package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tuank on 04/09/2017.
 */

public final class PlaceDTO {
    public final String status;
    public final Place place[];
    public final String message;

    @JsonCreator
    public PlaceDTO(@JsonProperty("status") String status,
                    @JsonProperty("result") Place[] place,
                    @JsonProperty("message") String message) {
        this.status = status;
        this.place = place;
        this.message = message;
    }

    public static final class Place implements Serializable {
        public long _id;
        public final String id;
        public String long_name;
        public String short_name;
        public long rating;
        public String address;
        public String province_name;
        public String province_id;
        public String phone;
        public String postal_code;
        public long location_lat;
        public long location_lng;
        public String icon;
        public String logo_3_4;
        public String logo_4_3;
        public String logo_16_9;
        public String opening_hours;
        public String photos;
        public String website;
        public long type_sea;
        public long type_attractions;
        public long type_entertainment;
        public long type_cultural;
        public long type_spring;
        public long type_summer;
        public long type_autumn;
        public long type_winter;
        public String description;
        public String hotels;
        public String restaurants;
        public String foods;
        public String game;
        public String created_at;
        public String updated_at;
        public ArrayList<String> arrImage;

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
            this.arrImage = new ArrayList<>();
            if (!Utils.isEmptyString(this.photos)) {
                try {
                    JSONArray array = new JSONArray(this.photos);
                    for (int i = 0; i < array.length(); i++) {
                        this.arrImage.add(AppContansts.URL_IMAGE + array.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public String getName() {
            if (this.long_name.length() < R.integer.length_name) return long_name;
            return short_name;
        }
    }
}
