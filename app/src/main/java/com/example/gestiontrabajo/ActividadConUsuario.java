package com.example.gestiontrabajo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gestiontrabajo.Conexión.Cliente;
import com.example.gestiontrabajo.Conexión.apiRol;
import com.example.gestiontrabajo.Conexión.apiUsuario;
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
    public SharedPreferences propiedades;
    private Menu menu;
    private Fragment fragmentActual;

    public Fragment getFragmentAntiguo() {
        return fragmentAntiguo;
    }

    public void setFragmentAntiguo(Fragment fragmentAntiguo) {
        this.fragmentAntiguo = fragmentAntiguo;
    }

    private Fragment fragmentAntiguo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_con_usuario);
        propiedades =getBaseContext().getSharedPreferences("Idiomas",MODE_PRIVATE);
        retrofit= Cliente.obtenerCliente();
        int id;
        id = getIntent().getIntExtra("usuario",0);
        obtenerUsuario(id);

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
        return super.onOptionsItemSelected(item);
    }

    private void obtenerUsuario(int idUsuario){
        apiUsuario apiUsuario = retrofit.create(com.example.gestiontrabajo.Conexión.apiUsuario.class);
        Call<ArrayList<Usuario>> listaUsuarios = apiUsuario.obtenerUsuario(idUsuario);
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
        apiRol apiRol= retrofit.create(com.example.gestiontrabajo.Conexión.apiRol.class);
        Call<ArrayList<Rol>>RolUsuario = apiRol.obtenerRol(idRol);
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
        if (fragmentActual!= null)
        fragmentAntiguo= fragmentActual;
        fragmentActual = fragment;
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