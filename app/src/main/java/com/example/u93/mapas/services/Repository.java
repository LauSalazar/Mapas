package com.example.u93.mapas.services;

import com.example.u93.mapas.models.CinemaType;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class Repository {
    private IServices iServices;

    public Repository(){
        ServiceFactory serviceFactory = new ServiceFactory();
        iServices = (IServices) serviceFactory.getInstanceService(IServices.class);
    }

    public ArrayList<CinemaType> getCinemas() throws IOException{
        Call<ArrayList<CinemaType>> call = iServices.getCinemaTypes();
        Response<ArrayList<CinemaType>> response = call.execute();
        if (response.errorBody() != null){
            return null;
        } else {
            return response.body();
        }
    }

}
