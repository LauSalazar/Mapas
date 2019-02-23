package com.example.u93.mapas.models;

import com.google.gson.annotations.SerializedName;

public class Cinema {

    @SerializedName("name")
    private String name;

    @SerializedName("_id")
    private String _id;

    @SerializedName("location")
    private Location location;

    public Cinema() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
