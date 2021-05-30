package com.example.gestiontrabajo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gestiontrabajo.Conexión.Cliente;
import com.example.gestiontrabajo.Conexión.apiRoles;
import com.example.gestiontrabajo.Conexión.apiUsuarios;
import com.example.gestiontrabajo.Datos.Rol;
import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.Instalaciones.FragmentInstalaciones;
import com.example.gestiontrabajo.Perfil.FragmentPerfil;
import com.example.gestiontrabajo.Reservas.FragmentReservas;
import com.example.gestiontrabajo.Roles.FragmentRoles;
import com.example.gestiontrabajo.Usuarios.FragmentUsuarios;
import com.google.android.material.navigation.NavigationView;

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
    public Rol rol;
    //public SharedPreferences propiedades;
    private Menu menu;
    public String lenguaje;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_con_usuario);
        retrofit= Cliente.obtenerCliente();
        int id;
        id = getIntent().getIntExtra("usuario",0);
        obtenerUsuario(id);
lenguaje= CargarIdioma();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icons8_menu_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        menu = navView.getMenu();
        configurarNavView();
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
                    case R.id.MisReservasNavMenu:
                       fragmentTransaction = true;
                        fragment = new FragmentReservas(true);
                        break;
                    case R.id.ReservasNavMenu:
                        fragmentTransaction= true;
                        fragment = new FragmentReservas();
                        break;
                        case R.id.UsuariosNavMenu:
                       fragmentTransaction= true;
                        fragment = new FragmentUsuarios();
                        break;
                    case R.id.RolesNavMenu:
                        fragmentTransaction = true;
                        fragment= new FragmentRoles();
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
        //iugkb
        return super.onOptionsItemSelected(item);
    }

    private void obtenerUsuario(int idUsuario){
        apiUsuarios apiUsuarios = retrofit.create(apiUsuarios.class);
        Call<ArrayList<Usuario>> listaUsuarios = apiUsuarios.obtenerUsuario(idUsuario);
        listaUsuarios.enqueue(new Callback<ArrayList<Usuario>>() {
            @Override
            public void onResponse(Call<ArrayList<Usuario>> call, Response<ArrayList<Usuario>> response) {
                if(response.body().size()>0) {
                    usuario = response.body().get(0);
                    obtenerRol(usuario.getCodigo_rol());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Usuario>> call, Throwable t) {

            }
        });
    }

    private void obtenerRol(int idRol){
        apiRoles apiRoles = retrofit.create(apiRoles.class);
        Call<ArrayList<Rol>>RolUsuario = apiRoles.obtenerRol(idRol);
        RolUsuario.enqueue(new Callback<ArrayList<Rol>>() {
            @Override
            public void onResponse(Call<ArrayList<Rol>>call, Response<ArrayList<Rol>> response) {
                if(response.isSuccessful()){
                    rol= response.body().get(0);
                    FragmentReservas fragmentReservas;
                    if(rol.isRealiza_reservas()){
                        System.out.println("-----------------------------------------------------------------");
                        fragmentReservas = new FragmentReservas( true);
                        //cambiarFragmento(fragmentReservas);
                    }
                    else {
                        fragmentReservas = new FragmentReservas();
                    }
                    cambiarFragmento(fragmentReservas);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Rol>> call, Throwable t) {

            }
        });
    }
    public void cambiarFragmento(Fragment fragment){

        if( fragment instanceof FragmentInstalaciones)
        {
            ((FragmentInstalaciones) fragment).actividadConUsuario= this;
        }
        else {
            if (fragment instanceof FragmentReservas) {
                ((FragmentReservas) fragment).actividadConUsuario = this;
            } else {
                if (fragment instanceof FragmentPerfil) {
                    ((FragmentPerfil) fragment).actividadConUsuario = this;
                } else {
                    if (fragment instanceof FragmentRoles) {
                        ((FragmentRoles) fragment).actividadConUsuario = this;
                    } else if (fragment instanceof FragmentUsuarios)
                        ((FragmentUsuarios) fragment).actividadConUsuario = this;
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

    private String CargarIdioma()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
        String idioma = sharedPreferences.getString("Idioma","");
        return idioma;
    }


    private void configurarNavView()
    {
//        List<Object>listado = Arrays.asList(rol.getClass().getDeclaredFields());
        if(rol != null){
            for (int i=0; i<rol.getClass().getFields().length;i++)
            if(rol.isRealiza_reservas()|| rol.isRealiza_reservas_otros())
                menu.findItem(R.id.InstalacionesNavMenu).setVisible(true);
            if(rol.isMod_rol())
                menu.findItem(R.id.RolesNavMenu).setVisible(true);
            if(rol.isMod_usu())
                menu.findItem(R.id.UsuariosNavMenu).setVisible(true);
            //if (rol.)

        }else
            for(int i=0;i<menu.size();i++){
                menu.getItem(i).setVisible(true);
            }

    }
}