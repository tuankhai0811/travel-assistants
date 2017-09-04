package com.tuankhai.travelassistants.model;

/**
 * Created by tuank on 04/09/2017.
 */

public class AllSliderPlace {

    public SliderPlace[] listSliderPlace;

    public AllSliderPlace() {
    }

    public AllSliderPlace(SliderPlace[] arr) {
        this.listSliderPlace = arr;
    }

    public static class SliderPlace {
        public String id;
        public String name;
        public String image;

        public SliderPlace(String id, String name, String image) {
            this.id = id;
            this.name = name;
            this.image = image;
        }
    }
}
