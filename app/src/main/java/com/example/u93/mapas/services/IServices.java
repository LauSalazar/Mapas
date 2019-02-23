package com.example.u93.mapas.services;

import com.example.u93.mapas.models.CinemaType;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IServices {

    @GET("cinemas")
    Call<ArrayList<CinemaType>> getCinemaTypes();
}
