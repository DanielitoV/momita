package com.example.danico.momasampler.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danico.momasampler.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ObraConDetalleActivity extends AppCompatActivity {

    private ImageView imageViewObra;
    private TextView textViewNombre;
    private TextView nacionalidad;
    private TextView descripcion;
    private TextView influencias;
    private String url;
    private String titulo;
    private Integer idArtista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obra_con_detalle);

        imageViewObra = findViewById(R.id.imageViewObra);

        nacionalidad = findViewById(R.id.nacionalidad);
        influencias = findViewById(R.id.influencias);


        Bundle bundle = getIntent().getExtras();

        url = bundle.getString("url");
        titulo = bundle.getString("titulo");
        idArtista = bundle.getInt("idArtista") - 1;


        descripcion.setText((CharSequence) descripcion);

        getInfoDeDatabase();

        getImagenDeStorage();


    }



    private void getInfoDeDatabase() {
        DatabaseReference nombreDeDatabase = FirebaseDatabase.getInstance().getReference().child("artists").child(idArtista.toString());
        DatabaseReference influenciasDataBase = FirebaseDatabase.getInstance().getReference().child("artists").child(idArtista.toString());
        DatabaseReference nacionalidadDataBase = FirebaseDatabase.getInstance().getReference().child("artists").child(idArtista.toString());

        nombreDeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewNombre.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



       influenciasDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                influencias.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        nacionalidadDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nacionalidad.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getImagenDeStorage() {
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
                    imageViewObra.setImageBitmap(bitmapDePintura);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(imageViewObra.getContext(), "no se pudo descargar la imagen", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
