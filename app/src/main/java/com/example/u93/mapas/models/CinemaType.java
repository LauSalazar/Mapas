package com.example.u93.mapas.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CinemaType {

    @SerializedName("_id")
    private String _id;

    @SerializedName("name")
    private String name;

    @SerializedName("locationList")
    private ArrayList<Cinema> cinemas;

    public CinemaType() {
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Cinema> getCinemas() {
        return cinemas;
    }

    public void setCinemas(ArrayList<Cinema> cinemas) {
        this.cinemas = cinemas;
    }
}
