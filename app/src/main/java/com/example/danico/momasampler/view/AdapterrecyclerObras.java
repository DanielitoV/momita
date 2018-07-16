package com.example.danico.momasampler.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danico.momasampler.ContenedorGlobalDeObra;
import com.example.danico.momasampler.R;
import com.example.danico.momasampler.model.pojo.Obra;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danico on 09/07/2018.
 */

public class AdapterrecyclerObras extends RecyclerView.Adapter {

    private SeleccionadorDeObra seleccionadorDeObra;
    private List<Obra> listaDeObras = new ArrayList<>();

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



    public void agregarObrasAlaLista(List<Obra> listaDeAdapter){
        listaDeObras.addAll(listaDeAdapter);
        notifyDataSetChanged();
    }


    public interface SeleccionadorDeObra{
        void seleccionaronEstaObra(Integer posicion);
    }




    /***  VIEWHOLDER  ***/
    public class ObraViewHolder extends RecyclerView.ViewHolder{

        /***atributos de la celda***/
        private TextView textCardViewNombreObra;
        private TextView textCardViewArtista;
        private ImageView imagenViewCardObra;
        private CardView cardView;

        private String url;
        private String titulo;
        private Integer idArtista;


        /***constructor***/
        public ObraViewHolder(View itemView) {
            super(itemView);

            /***conecto los atributos con los layouts***/
            textCardViewNombreObra = itemView.findViewById(R.id.textCardViewNombreObra);
            textCardViewArtista = itemView.findViewById(R.id.textCardViewArtista);
            imagenViewCardObra= itemView.findViewById(R.id.imagenViewCardObra);
            cardView = itemView.findViewById(R.id.cardView);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    irAdetalleDeObra();
                }
            });


           /* //aplico un escuchador al itemView instanciandole un escuchador como parametro
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
            });*/
        }

        private void irAdetalleDeObra(){
            Bundle bundle = new Bundle();
            bundle.putInt("idArtista", idArtista);
            bundle.putString("titulo", titulo);
            bundle.putString("url", url);

            Intent intent = new Intent(itemView.getContext(), ObraConDetalleActivity.class);
        }



        public void cargarCeldaObra(Obra obra) {
            idArtista = obra.getArtistID();
            titulo = obra.getName();
            url = obra.getImage();

            textCardViewNombreObra.setText(titulo);
            getImagenDeStorage();
        }

        private void getImagenDeStorage(){
            String[] childImagen = url.split("/");
            String child;
            if(childImagen[1].isEmpty()){
                child = childImagen[2];
            }else{
                child = childImagen[1];
            }

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference pinturas = storageReference.child("Pinturas").child(child);

            File locaFile = null;
            try {
                locaFile = File.createTempFile("Pinturas", "jpg");
                final File finalLocalFile = locaFile;
                pinturas.getFile(locaFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmapDePintura = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                        imagenViewCardObra.setImageBitmap(bitmapDePintura);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(imagenViewCardObra.getContext(), "no se pudo descargar la imagen", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
