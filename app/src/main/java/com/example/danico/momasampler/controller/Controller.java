package com.example.danico.momasampler.controller;

import com.example.danico.momasampler.model.dao.DAOObrasDeInternet;
import com.example.danico.momasampler.model.pojo.Obra;
import com.example.danico.momasampler.utils.ResultListener;

import java.util.List;

/**
 * Created by danico on 07/07/2018.
 */

public class Controller {

    public void obtenerObra(final ResultListener<List<Obra>> resultListenerDelMain){
        /*Aca en el Controller debo instanciar un resultListener nuevo, que sea propio
         del controller para pasarle al DAO, entonces instancion un resultistener y  la lista a la
         cual aplicarlo*/

        ResultListener<List<Obra>> resultListenerDelController = new ResultListener<List<Obra>>() {
            @Override
            public void finish(List<Obra> obras) {
                resultListenerDelMain.finish(obras);  /*este se activa en el camino de vuelta
                                                        cuando ya viene la obra cargada con la informacion*/
            }
        };


        /*Lo que tengo que saber es a quien le voy a pedir la informacion, si al DAO de internet
          o al DAO de BaseDeDatos, para eso averiguo si hay internet o si no*/

        List<Obra> infomacion = null;
        if(hayInternet()){
            DAOObrasDeInternet daoObrasDeInternet = new DAOObrasDeInternet();
        }
    }

    public boolean hayInternet(){
        return true;
    }
}

