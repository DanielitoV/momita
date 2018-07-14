package com.example.danico.momasampler.Service;

import com.example.danico.momasampler.model.pojo.Obra;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by DH on 13/7/2018.
 */


    public interface Service {
        //@PATH SIRVE PARA REMPLAZAR lo que nos pasen en id y lo junte a la URL.
        @GET("obra/{id}")
        Call<Obra> getObra(@Path("id") Integer id);

        @GET("obra")
        Call<List<Obra>> getObra();

    }

