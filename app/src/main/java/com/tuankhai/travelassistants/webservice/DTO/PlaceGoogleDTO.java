package com.tuankhai.travelassistants.webservice.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by tuank on 10/09/2017.
 */

public final class PlaceGoogleDTO {
    public final String[] html_attributions;
    public final Result result;
    public final String status;

    @JsonCreator
    public PlaceGoogleDTO(
            @JsonProperty("html_attributions") String[] html_attributions,
            @JsonProperty("result") Result result,
            @JsonProperty("status") String status){
        this.html_attributions = html_attributions;
        this.result = result;
        this.status = status;
    }


    public static final class Result {
        public final Address_component address_components[];
        public final String adr_address;
        public final String formatted_address;
        public final String formatted_phone_number;
        public final Geometry geometry;
        public final String icon;
        public final String id;
        public final String international_phone_number;
        public final String name;
        public final Opening_hours opening_hours;
        public final Photo photos[];
        public final String place_id;
        public final double rating;
        public final String reference;
        public final Review reviews[];
        public final String scope;
        public final String[] types;
        public final String url;
        public final long utc_offset;
        public final String vicinity;
        public final String website;

        @JsonCreator
        public Result(
                @JsonProperty("address_components") Address_component[] address_components,
                @JsonProperty("adr_address") String adr_address,
                @JsonProperty("formatted_address") String formatted_address,
                @JsonProperty("formatted_phone_number") String formatted_phone_number,
                @JsonProperty("geometry") Geometry geometry,
                @JsonProperty("icon") String icon,
                @JsonProperty("id") String id,
                @JsonProperty("international_phone_number") String international_phone_number,
                @JsonProperty("name") String name,
                @JsonProperty("opening_hours") Opening_hours opening_hours,
                @JsonProperty("photos") Photo[] photos,
                @JsonProperty("place_id") String place_id,
                @JsonProperty("rating") double rating,
                @JsonProperty("reference") String reference,
                @JsonProperty("reviews") Review[] reviews,
                @JsonProperty("scope") String scope,
                @JsonProperty("types") String[] types,
                @JsonProperty("url") String url,
                @JsonProperty("utc_offset") long utc_offset,
                @JsonProperty("vicinity") String vicinity,
                @JsonProperty("website") String website){
            this.address_components = address_components;
            this.adr_address = adr_address;
            this.formatted_address = formatted_address;
            this.formatted_phone_number = formatted_phone_number;
            this.geometry = geometry;
            this.icon = icon;
            this.id = id;
            this.international_phone_number = international_phone_number;
            this.name = name;
            this.opening_hours = opening_hours;
            this.photos = photos;
            this.place_id = place_id;
            this.rating = rating;
            this.reference = reference;
            this.reviews = reviews;
            this.scope = scope;
            this.types = types;
            this.url = url;
            this.utc_offset = utc_offset;
            this.vicinity = vicinity;
            this.website = website;
        }

        public static final class Address_component {
            public final String long_name;
            public final String short_name;
            public final String[] types;

            @JsonCreator
            public Address_component(
                    @JsonProperty("long_name") String long_name,
                    @JsonProperty("short_name") String short_name,
                    @JsonProperty("types") String[] types){
                this.long_name = long_name;
                this.short_name = short_name;
                this.types = types;
            }
        }

        public static final class Geometry {
            public final Location location;
            public final Viewport viewport;

            @JsonCreator
            public Geometry(
                    @JsonProperty("location") Location location,
                    @JsonProperty("viewport") Viewport viewport){
                this.location = location;
                this.viewport = viewport;
            }

            public static final class Location {
                public final double lat;
                public final double lng;

                @JsonCreator
                public Location(
                        @JsonProperty("lat") double lat,
                        @JsonProperty("lng") double lng){
                    this.lat = lat;
                    this.lng = lng;
                }
            }

            public static final class Viewport {
                public final Northeast northeast;
                public final Southwest southwest;

                @JsonCreator
                public Viewport(
                        @JsonProperty("northeast") Northeast northeast,
                        @JsonProperty("southwest") Southwest southwest){
                    this.northeast = northeast;
                    this.southwest = southwest;
                }

                public static final class Northeast {
                    public final double lat;
                    public final double lng;

                    @JsonCreator
                    public Northeast(
                            @JsonProperty("lat") double lat,
                            @JsonProperty("lng") double lng){
                        this.lat = lat;
                        this.lng = lng;
                    }
                }

                public static final class Southwest {
                    public final double lat;
                    public final double lng;

                    @JsonCreator
                    public Southwest(
                            @JsonProperty("lat") double lat,
                            @JsonProperty("lng") double lng){
                        this.lat = lat;
                        this.lng = lng;
                    }
                }
            }
        }

        public static final class Opening_hours {
            public final boolean open_now;
            public final Period periods[];
            public final String[] weekday_text;

            @JsonCreator
            public Opening_hours(
                    @JsonProperty("open_now") boolean open_now,
                    @JsonProperty("periods") Period[] periods,
                    @JsonProperty("weekday_text") String[] weekday_text){
                this.open_now = open_now;
                this.periods = periods;
                this.weekday_text = weekday_text;
            }

            public static final class Period {
                public final Close close;
                public final Open open;

                @JsonCreator
                public Period(
                        @JsonProperty("close") Close close,
                        @JsonProperty("open") Open open){
                    this.close = close;
                    this.open = open;
                }

                public static final class Close {
                    public final long day;
                    public final String time;

                    @JsonCreator
                    public Close(
                            @JsonProperty("day") long day,
                            @JsonProperty("time") String time){
                        this.day = day;
                        this.time = time;
                    }
                }

                public static final class Open {
                    public final long day;
                    public final String time;

                    @JsonCreator
                    public Open(
                            @JsonProperty("day") long day,
                            @JsonProperty("time") String time){
                        this.day = day;
                        this.time = time;
                    }
                }
            }
        }

        public static final class Photo {
            public final long height;
            public final String[] html_attributions;
            public final String photo_reference;
            public final long width;

            @JsonCreator
            public Photo(
                    @JsonProperty("height") long height,
                    @JsonProperty("html_attributions") String[] html_attributions,
                    @JsonProperty("photo_reference") String photo_reference,
                    @JsonProperty("width") long width){
                this.height = height;
                this.html_attributions = html_attributions;
                this.photo_reference = photo_reference;
                this.width = width;
            }
        }

        public static final class Review {
            public final String author_name;
            public final String author_url;
            public final String language;
            public final String profile_photo_url;
            public final long rating;
            public final String relative_time_description;
            public final String text;
            public final long time;

            @JsonCreator
            public Review(
                    @JsonProperty("author_name") String author_name,
                    @JsonProperty("author_url") String author_url,
                    @JsonProperty("language") String language,
                    @JsonProperty("profile_photo_url") String profile_photo_url,
                    @JsonProperty("rating") long rating,
                    @JsonProperty("relative_time_description") String relative_time_description,
                    @JsonProperty("text") String text, @JsonProperty("time") long time){
                this.author_name = author_name;
                this.author_url = author_url;
                this.language = language;
                this.profile_photo_url = profile_photo_url;
                this.rating = rating;
                this.relative_time_description = relative_time_description;
                this.text = text;
                this.time = time;
            }
        }
    }
}