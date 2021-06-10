package com.example.gestiontrabajo;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gestiontrabajo.Conexión.Cliente;
import com.example.gestiontrabajo.Inicio.FragmentLogin;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    public Retrofit retrofit;
    public Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources= getResources();
        setContentView(R.layout.activity_main);
        String idioma =CargarIdioma();
        if (idioma.length()>0)
        {
            Locale locale = new Locale(idioma);
            // locale = Locale.UK;
            Configuration config =new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
        }
        retrofit = Cliente.obtenerCliente();
        //cargarPropiedad();
        FragmentLogin fragmentLogin = new FragmentLogin(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.FrameLayoutRegistro,fragmentLogin);
        fragmentTransaction.commit();
    }
    public void cambiarFragmento(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayoutRegistro,fragment);
        fragmentTransaction.commit();
    }
    private String CargarIdioma()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
        String idioma = sharedPreferences.getString("Idioma","");
        return idioma;
    }

    public void mensajeError(View vista, LayoutInflater inflater, int idMensaje){
        Snackbar snackbar = Snackbar.make(vista, "", Snackbar.LENGTH_LONG);
// Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
// Hide the text
       /* TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);*/

// Inflate our custom view
        View snackView = inflater.inflate(R.layout.snackbarlayout, null);
// Configure the view

        TextView textViewTop = (TextView) snackView.findViewById(R.id.snackbar_text);
        textViewTop.setText(getResources().getString(idMensaje));
        textViewTop.setTextColor(Color.WHITE);
        ImageView imagenEstado= snackView.findViewById(R.id.ImagenEstado);
        imagenEstado.setImageResource(R.drawable.close_81512);

//If the view is not covering the whole snackbar layout, add this line
        layout.setPadding(0,0,0,0);

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        snackbar.show();
    }
}