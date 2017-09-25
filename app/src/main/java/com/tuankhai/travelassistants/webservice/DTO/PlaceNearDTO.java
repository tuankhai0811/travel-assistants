package com.tuankhai.travelassistants.webservice.DTO;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Khai on 11/09/2017.
 */

public final class PlaceNearDTO implements Serializable {
    public final String[] html_attributions;
    public String next_page_token = "";
    public final Result results[];
    public final String status;

    @JsonCreator
    public PlaceNearDTO(
            @JsonProperty("html_attributions") String[] html_attributions,
            @JsonProperty("next_page_token") String next_page_token,
            @JsonProperty("results") Result[] results,
            @JsonProperty("status") String status) {
        this.html_attributions = html_attributions;
        this.next_page_token = next_page_token != null ? next_page_token : "";
        this.results = results;
        this.status = status;
    }


    public static final class Result implements Serializable, Comparable<Result> {
        public final Geometry geometry;
        public final String icon;
        public final String id;
        public final String name;
        public final Opening_hours opening_hours;
        public final Photo photos[];
        public final String place_id;
        public final String rating;
        public final String reference;
        public final String scope;
        public final String[] types;
        public final String vicinity;
        public double distance = 0;

        @JsonCreator
        public Result(
                @JsonProperty("geometry") Geometry geometry,
                @JsonProperty("icon") String icon,
                @JsonProperty("id") String id,
                @JsonProperty("name") String name,
                @JsonProperty(value = "opening_hours", required = false) Opening_hours opening_hours,
                @JsonProperty("photos") Photo[] photos,
                @JsonProperty("place_id") String place_id,
                @JsonProperty("rating") String rating,
                @JsonProperty("reference") String reference,
                @JsonProperty("scope") String scope,
                @JsonProperty("types") String[] types,
                @JsonProperty("vicinity") String vicinity) {
            this.geometry = geometry;
            this.icon = icon;
            this.id = id;
            this.name = name;
            this.opening_hours = opening_hours;
            this.photos = photos;
            this.place_id = place_id;
            this.rating = rating;
            this.reference = reference;
            this.scope = scope;
            this.types = types;
            this.vicinity = vicinity;
        }

        @Override
        public int compareTo(@NonNull Result result) {
            if (this.distance >= result.distance)
                return 1;
            else
                return -1;
        }

        public static final class Geometry implements Serializable {
            public final Location location;
            public final Viewport viewport;

            @JsonCreator
            public Geometry(
                    @JsonProperty("location") Location location,
                    @JsonProperty("viewport") Viewport viewport) {
                this.location = location;
                this.viewport = viewport;
            }

            public static final class Location implements Serializable {
                public final String lat;
                public final String lng;

                @JsonCreator
                public Location(
                        @JsonProperty("lat") String lat,
                        @JsonProperty("lng") String lng) {
                    this.lat = lat;
                    this.lng = lng;
                }
            }

            public static final class Viewport implements Serializable {
                public final Northeast northeast;
                public final Southwest southwest;

                @JsonCreator
                public Viewport(
                        @JsonProperty("northeast") Northeast northeast,
                        @JsonProperty("southwest") Southwest southwest) {
                    this.northeast = northeast;
                    this.southwest = southwest;
                }

                public static final class Northeast implements Serializable {
                    public final String lat;
                    public final String lng;

                    @JsonCreator
                    public Northeast(
                            @JsonProperty("lat") String lat,
                            @JsonProperty("lng") String lng) {
                        this.lat = lat;
                        this.lng = lng;
                    }
                }

                public static final class Southwest implements Serializable {
                    public final String lat;
                    public final String lng;

                    @JsonCreator
                    public Southwest(
                            @JsonProperty("lat") String lat,
                            @JsonProperty("lng") String lng) {
                        this.lat = lat;
                        this.lng = lng;
                    }
                }
            }
        }

        public static final class Opening_hours implements Serializable {
            public final boolean open_now;
            public final Weekday_text weekday_text[];

            @JsonCreator
            public Opening_hours(
                    @JsonProperty("open_now") boolean open_now,
                    @JsonProperty("weekday_text") Weekday_text[] weekday_text) {
                this.open_now = open_now;
                this.weekday_text = weekday_text;
            }

            public static final class Weekday_text {

                @JsonCreator
                public Weekday_text() {
                }
            }
        }

        public static final class Photo implements Serializable {
            public final String height;
            public final String[] html_attributions;
            public final String photo_reference;
            public final String width;

            @JsonCreator
            public Photo(
                    @JsonProperty("height") String height,
                    @JsonProperty("html_attributions") String[] html_attributions,
                    @JsonProperty("photo_reference") String photo_reference,
                    @JsonProperty("width") String width) {
                this.height = height;
                this.html_attributions = html_attributions;
                this.photo_reference = photo_reference;
                this.width = width;
            }
        }

        public float getRaring() {
            if (rating == null) return 0;
            return Float.parseFloat(rating);
        }
    }

}