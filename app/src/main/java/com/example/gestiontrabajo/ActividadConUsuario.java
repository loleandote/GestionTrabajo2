package com.example.gestiontrabajo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gestiontrabajo.Conexión.Cliente;
import com.example.gestiontrabajo.Conexión.apiUsuario;
import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.Instalaciones.FragmentInstalaciones;
import com.example.gestiontrabajo.Perfil.FragmentPerfil;
import com.example.gestiontrabajo.Reservas.FragmentReservas;
import com.google.android.material.navigation.NavigationView;
import com.paypal.android.sdk.payments.PayPalService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActividadConUsuario extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    public Retrofit retrofit;
    public Usuario usuario;
    public SharedPreferences propiedades;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_con_usuario);
        propiedades =getBaseContext().getSharedPreferences("Idiomas",MODE_PRIVATE);
        retrofit= Cliente.obtenerCliente();
        int id=1;
        id = getIntent().getIntExtra("usuario",0);
        if(id==0)
            id =1;
        obtenerUsuario(id);
        FragmentReservas fragmentReservas = new FragmentReservas(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayoutPrincipal,fragmentReservas);
        fragmentTransaction.commit();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icons8_menu_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                boolean fragmentTransaction= false;
                Fragment fragment= null;
                switch (item.getItemId()){
                    case R.id.InstalacionesNavMenu:
                        fragmentTransaction= true;
                        fragment = new FragmentInstalaciones();
                        break;
                    case R.id.ReservasNavMenu:
                        fragmentTransaction= true;
                        fragment = new FragmentReservas();
                        break;
                        case R.id.UsuariosNavMenu:
                       /* fragmentTransaction= true;
                        fragment = new FragmentReservas();*/
                        break;
                    case R.id.PerfilNavMenu:
                        fragmentTransaction= true;
                        fragment=new FragmentPerfil();
                        break;
                }
                if (fragmentTransaction){
                    cambiarFragmento(fragment);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else
                {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void obtenerUsuario(int idUsuario){
        apiUsuario apiUsuario = retrofit.create(com.example.gestiontrabajo.Conexión.apiUsuario.class);
        Call<ArrayList<Usuario>> listaUsuarios = apiUsuario.obtenerUsuario(idUsuario);
        listaUsuarios.enqueue(new Callback<ArrayList<Usuario>>() {
            @Override
            public void onResponse(Call<ArrayList<Usuario>> call, Response<ArrayList<Usuario>> response) {
                if(response.body().size()>0)
                    usuario = response.body().get(0);
            }

            @Override
            public void onFailure(Call<ArrayList<Usuario>> call, Throwable t) {

            }
        });
    }
    public void cambiarFragmento(Fragment fragment){
        if( fragment instanceof FragmentInstalaciones)
        {
            ((FragmentInstalaciones) fragment).actividadConUsuario= this;
        }
        else{
            if (fragment instanceof FragmentReservas) {
                ((FragmentReservas) fragment).actividadConUsuario = this;
            } else {
                if (fragment instanceof FragmentPerfil) {
                    ((FragmentPerfil) fragment).actividadConUsuario = this;
                }
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutPrincipal,fragment);
        fragmentTransaction.commit();
    }

    public void CambiarIdioma(String idioma){
        SharedPreferences sharedPref = getSharedPreferences("MyData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Idioma",idioma);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }
}