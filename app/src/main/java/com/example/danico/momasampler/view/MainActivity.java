package com.example.danico.momasampler.view;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.danico.momasampler.ContenedorGlobalDeObra;
import com.example.danico.momasampler.R;
import com.google.firebase.auth.FirebaseAuth;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterrecyclerObras.SeleccionadorDeObra {

    /***Atributos***/
    private Button buttonSalir;
    private DrawerLayout drawer;
    NavigationView navigationView;

    private AdapterrecyclerObras adapterrecyclerObras;
    private CambioDeActivity cambioDeActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //boton provisorio para salir
        buttonSalir = findViewById(R.id.buttonSalir);
        buttonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });


        //Para el toolBar
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        //conecto el drawer con su layout
        drawer = findViewById(R.id.drawerLayout);

        //le meto la accion al drawer para q se abra
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // voy a conectar el adapter con el layout
        RecyclerView recyclerViewObras = findViewById(R.id.recyclerMain);

        //creo el adapter del recycler y le paso de obras a la par de la interfaz
        // para pasar los datos del onClick
        adapterrecyclerObras = new AdapterrecyclerObras(this);
        recyclerViewObras.setAdapter(adapterrecyclerObras);

        //Voy a crear el layout manager del rcycler
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewObras.setLayoutManager(layoutManager);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
        //super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //voy a hacer en esta parte lo del switch para que al hacer click en
        //cada uno de los items te lleve a su correspondiente actividad
        int id = item.getItemId();
        switch (id) {

            case R.id.logOut:
                Intent u = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(u);
                break;
            case R.id.salir:
                Intent s = new Intent(MainActivity.this, CambioDePasswordActivity.class);
                startActivity(s);
                break;
        }
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void seleccionaronEstaObra(Integer posicion) {
        cambioDeActivity.pasarAOtraActivity(posicion);
    }

    //interfaz para cambiar de Activity
    public interface CambioDeActivity{
        void pasarAOtraActivity(Integer posicionObra);
    }
}