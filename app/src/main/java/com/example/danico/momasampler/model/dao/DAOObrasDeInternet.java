package com.example.danico.momasampler.model.dao;

import com.example.danico.momasampler.Service.Service;
import com.example.danico.momasampler.model.pojo.Obra;
import com.example.danico.momasampler.utils.ResultListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by danico on 07/07/2018.
 */

public class DAOObrasDeInternet {

    private Retrofit retrofit;
    private String  baseURL;
    private Service service;



    public DAOObrasDeInternet() {
            baseURL = "https://api.myjson.com";
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(Service.class);
        }

    public void getObra(final ResultListener<Obra> escuchadorDelControlador, Integer artistaId){
        //new CallBack es el escuchador propio de retrofit
        service.getObra(artistaId).enqueue(new Callback<Obra>() {
            @Override
            public void onResponse(Call<Obra> call, Response<Obra> response) {

                Obra obra = response.body();
                //avisa al controlador que ya tiene la informacion.
                escuchadorDelControlador.finish(obra);
            }

            @Override
            public void onFailure(Call<Obra> call, Throwable t) {
                //imprime la excepcion en caso de error.
                t.printStackTrace();
            }
        });
    }
    }

