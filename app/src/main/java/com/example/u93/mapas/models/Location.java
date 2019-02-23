package com.example.u93.mapas.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Location {

    @SerializedName("coordinates")
    private ArrayList<String> coordinates;

    @SerializedName("type")
    private String type;

    public ArrayList<String> getCordinates() {
        return coordinates;
    }

    public void setCordinates(ArrayList<String> cordinates) {
        this.coordinates = cordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
