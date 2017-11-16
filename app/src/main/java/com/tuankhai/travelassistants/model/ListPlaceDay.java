package com.tuankhai.travelassistants.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Khai on 16/11/2017.
 */

public class ListPlaceDay implements Serializable{
    ArrayList<String> list;

    public ListPlaceDay(ArrayList<String> list) {
        this.list = list;
    }

    public ListPlaceDay() {
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }
}
