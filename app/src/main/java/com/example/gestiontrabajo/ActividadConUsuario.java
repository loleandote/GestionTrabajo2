package com.example.gestiontrabajo;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActividadConUsuario extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public NavigationView navView;
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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        menu = navView.getMenu();


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                boolean fragmentTransaction= false;
                Fragment fragment= null;
                String titulo="";
                switch (item.getItemId()){
                    case R.id.InstalacionesNavMenu:
                        fragmentTransaction= true;
                        fragment = new FragmentInstalaciones();
                        titulo=  getResources().getString(R.string.Instalaciones);
                        break;
                    case R.id.MisReservasNavMenu:
                       fragmentTransaction = true;
                        fragment = new FragmentReservas(true);
                        titulo= getResources().getString(R.string.MisReservas);
                        break;
                    case R.id.ReservasNavMenu:
                        fragmentTransaction= true;
                        fragment = new FragmentReservas();
                        titulo=  getResources().getString(R.string.Reservas);
                        break;
                        case R.id.UsuariosNavMenu:
                       fragmentTransaction= true;
                        fragment = new FragmentUsuarios();
                        titulo= getResources().getString(R.string.Usuarios);
                        break;
                    case R.id.RolesNavMenu:
                        fragmentTransaction = true;
                        fragment= new FragmentRoles();
                        titulo= getResources().getString(R.string.Roles);
                        break;
                    case R.id.PerfilNavMenu:
                        fragmentTransaction= true;
                        fragment=new FragmentPerfil();
                        titulo= getResources().getString(R.string.Perfil);
                        break;
                }
                if (fragmentTransaction){
                    cambiarFragmento(fragment, titulo);
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
                    View header = navView.getHeaderView(0);
                    TextView tituloNombre = header.findViewById(R.id.nombreUsuarioMenu);
                    tituloNombre.setText(usuario.getNombre_usuario());
                    TextView tituloCorreo = header.findViewById(R.id.correoUsuarioMenu);
                    tituloCorreo.setText(usuario.getCorreo_usuario());
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
                    configurarNavView();
                    FragmentReservas fragmentReservas;
                    if(rol.isRealiza_reservas()){
                        fragmentReservas = new FragmentReservas( true);
                        //cambiarFragmento(fragmentReservas);
                    }
                    else {
                        fragmentReservas = new FragmentReservas();
                    }
                    cambiarFragmento(fragmentReservas,  getResources().getString(R.string.Reservas));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Rol>> call, Throwable t) {

            }
        });
    }
    public void cambiarFragmento(Fragment fragment, String titulo){
        getSupportActionBar().setTitle(titulo);

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
            if(!rol.isRealiza_reservas()&& !rol.isRealiza_reservas_otros())
                menu.findItem(R.id.InstalacionesNavMenu).setVisible(false);
            if(!rol.isMod_permiso())
                menu.findItem(R.id.RolesNavMenu).setVisible(false);
            if(!rol.ismod_usuario_otros())
                menu.findItem(R.id.UsuariosNavMenu).setVisible(false);
            if (!rol.isRealiza_reservas_otros())
                menu.findItem(R.id.ReservasNavMenu).setVisible(false);
            if (!rol.isRealiza_reservas())
                menu.findItem(R.id.MisReservasNavMenu).setVisible(false);
            if (!rol.ismod_usuario_otros())
                menu.findItem(R.id.UsuariosNavMenu).setVisible(false);
            if(!rol.isMod_permiso())
                menu.findItem(R.id.RolesNavMenu).setVisible(false);
            if(!rol.isModificar_usuario())
                menu.findItem(R.id.PerfilNavMenu).setVisible(false);


            //if (rol.)

        }else
            System.out.println("holas----------------");
            for(int i=0;i<menu.size();i++){
                menu.getItem(i).setVisible(true);
            }

    }

    public void mensajeError(View vista, LayoutInflater inflater,  int idMensaje){
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
    public void mensajeCorrecto(View vista, LayoutInflater inflater,  int idMensaje){
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
        imagenEstado.setImageResource(R.drawable.sign_check_icon_34365);

//If the view is not covering the whole snackbar layout, add this line
        layout.setPadding(0,0,0,0);

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        snackbar.show();
    }
}