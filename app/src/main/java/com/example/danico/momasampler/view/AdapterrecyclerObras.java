package com.example.danico.momasampler.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danico.momasampler.ContenedorGlobalDeObra;
import com.example.danico.momasampler.R;
import com.example.danico.momasampler.model.pojo.Obra;

import java.util.List;

/**
 * Created by danico on 09/07/2018.
 */

public class AdapterrecyclerObras extends RecyclerView.Adapter {

    private SeleccionadorDeObra seleccionadorDeObra;
    private List<Obra> listaDeObras;

    public AdapterrecyclerObras(SeleccionadorDeObra seleccionadorDeObra){
        this.seleccionadorDeObra = seleccionadorDeObra;
        this.listaDeObras = ContenedorGlobalDeObra.listaGlobalObra;
    }

    @Override
    public ObraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //1- aca voy a obtener el contexto del ViewGroup
        Context context = parent.getContext();
        //2- obtengo el inflador del contexto
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        //3- debo inflar la celda de la obra
        View celdaObra = layoutInflater.inflate(R.layout.celda_obra, parent, false);
        //4- esto es para guardar la celda inflada en un vieewHolder
        ObraViewHolder obraViewHolder = new ObraViewHolder(celdaObra);
        return obraViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Obra obra = listaDeObras.get(position);
        ObraViewHolder obraViewHolder = (ObraViewHolder) holder;
        obraViewHolder.cargarCeldaObra(obra);
    }


    @Override
    public int getItemCount() {
        return listaDeObras.size();
    }


    public interface SeleccionadorDeObra{
        void seleccionaronEstaObra(Integer posicion);
    }
    public class ObraViewHolder extends RecyclerView.ViewHolder{

        /***atributos de la celda***/
        private TextView textCardViewNombreObra;
        private TextView textCardViewArtista;
        private ImageView imagenViewCardObra;

        /***constructor***/
        public ObraViewHolder(View itemView) {
            super(itemView);

            /***conecto los atributos con los layouts***/
            textCardViewNombreObra = itemView.findViewById(R.id.textCardViewNombreObra);
            textCardViewArtista = itemView.findViewById(R.id.textCardViewArtista);
            imagenViewCardObra= itemView.findViewById(R.id.imagenViewCardObra);

            //aplico un escuchador al itemView instanciandole un escuchador como parametro
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int obraSeleccionada = getAdapterPosition();

                    //esto es para pasar a la otra activity cada vez que
                    // seleccione una obra que desee ver.. entonces me va a llevar a la obra correspondiente a la elegida
                    Obra obraClickeada = listaDeObras.get(obraSeleccionada);
                    int obraCorrespondiente = ContenedorGlobalDeObra.listaGlobalObra.indexOf(obraClickeada);

                    seleccionadorDeObra.seleccionaronEstaObra(obraCorrespondiente);
                }
            });
        }

        public void cargarCeldaObra(Obra obra) {
            textCardViewNombreObra.setText(obra.getName());
            imagenViewCardObra.setImageResource(Integer.parseInt(obra.getImage()));
            textCardViewArtista.setText(obra.getArtistID());
        }
    }
}
