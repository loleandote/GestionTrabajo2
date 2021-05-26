package com.example.gestiontrabajo;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gestiontrabajo.Conexión.Cliente;
import com.example.gestiontrabajo.Inicio.FragmentLogin;

import java.util.Locale;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    public Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}