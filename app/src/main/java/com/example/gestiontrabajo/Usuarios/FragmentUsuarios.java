package com.example.gestiontrabajo.Usuarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiRoles;
import com.example.gestiontrabajo.Conexión.apiUsuarios;
import com.example.gestiontrabajo.Datos.Rol;
import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.Instalaciones.FragmentInstalacion;
import com.example.gestiontrabajo.R;
import com.example.gestiontrabajo.Reservas.FragmentReservas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentUsuarios extends Fragment {
   public ActividadConUsuario actividadConUsuario;
    View vista;
    private RecyclerView recyclerView;
    private UsuarioAdapter usuarioAdapter;
    private int usuarioPulsado;
    FragmentInstalacion fragmentInstalacion;
    Spinner OpcionesRoles;
    ArrayList<Integer>listaRoles;
    public FragmentUsuarios() {
        // Required empty public constructor
    }


    public FragmentUsuarios(ActividadConUsuario actividadConUsuario){
        this.actividadConUsuario=actividadConUsuario;
    }
    public FragmentUsuarios(ActividadConUsuario actividadConUsuario, FragmentInstalacion fragmentInstalacion){
        this.actividadConUsuario=actividadConUsuario;
        this.fragmentInstalacion= fragmentInstalacion;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_usuarios, container, false);
        OpcionesRoles=vista.findViewById(R.id.OpcionesRoles);
        listaRoles=new ArrayList<>();
        for(int i=0; i<=actividadConUsuario.rol.getRango_rol();i++)
            listaRoles.add(i);
        OpcionesRoles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                    obtenerUsuarios();
                else
                    obtenerUsuarios(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        FloatingActionButton IrAñadirUsuario=vista.findViewById(R.id.IrAñadirUsuario);
        if (fragmentInstalacion != null)
            IrAñadirUsuario.setVisibility(View.GONE);
        IrAñadirUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               irAusuario();
            }
        });
        recyclerView = vista.findViewById(R.id.ReciclerViewUsuarios);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        usuarioAdapter= new UsuarioAdapter(getActivity());
        recyclerView.setAdapter(usuarioAdapter);
        usuarioAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuarioPulsado = recyclerView.getChildAdapterPosition(v);
                Usuario usuario = usuarioAdapter.lista.get(usuarioPulsado);
                if(fragmentInstalacion== null)
                seleccionarUsuario(usuario);
                else {
                    fragmentInstalacion.setUsuario(usuario);
                    fragmentInstalacion.cambiarUsuario(usuario.getNombre_usuario());
                    actividadConUsuario.cambiarFragmento(fragmentInstalacion);
                }
            }
        });
        obtenerUsuarios();
        obtenerRoles();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (fragmentInstalacion == null)

                {
                    FragmentReservas fragmentReservas = new FragmentReservas(actividadConUsuario, true);
                    actividadConUsuario.cambiarFragmento(fragmentReservas);
                }
                else
                    actividadConUsuario.cambiarFragmento(fragmentInstalacion);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return vista;
    }

    private void seleccionarUsuario(Usuario usuario){
        FragmentUsuario fragmentUsuario= new FragmentUsuario(actividadConUsuario,this, usuario);
        actividadConUsuario.cambiarFragmento(fragmentUsuario);
    }

    private void obtenerUsuarios(){
        apiUsuarios apiUsuarios = actividadConUsuario.retrofit.create(apiUsuarios.class);
        Call<ArrayList<Usuario>>respuesta = apiUsuarios.obtenerUsuariosPorRoles(listaRoles);
        respuesta.enqueue(new Callback<ArrayList<Usuario>>() {
            @Override
            public void onResponse(Call<ArrayList<Usuario>> call, Response<ArrayList<Usuario>> response) {
                if (response.isSuccessful()){
                    usuarioAdapter.actualizarListaUsuarios(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Usuario>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void obtenerUsuarios(int idRol)
    {
        apiUsuarios apiUsuarios = actividadConUsuario.retrofit.create(apiUsuarios.class);
        Call<ArrayList<Usuario>>respuesta = apiUsuarios.obtenerUsuariosPorRol(idRol);
        respuesta.enqueue(new Callback<ArrayList<Usuario>>() {
            @Override
            public void onResponse(Call<ArrayList<Usuario>> call, Response<ArrayList<Usuario>> response) {
                if (response.isSuccessful()){
                    usuarioAdapter.actualizarListaUsuarios(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Usuario>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private  void obtenerRoles()
    {

        apiRoles apiRoles = actividadConUsuario.retrofit.create(apiRoles.class);
        Call<ArrayList<Rol>>respuesta = apiRoles.obtenerRoles(listaRoles);
        respuesta.enqueue(new Callback<ArrayList<Rol>>() {
            @Override
            public void onResponse(Call<ArrayList<Rol>> call, Response<ArrayList<Rol>> response) {
                if(response.isSuccessful()){
                    String []listaNombres = new String[response.body().size()+1];
                    listaNombres[0]= "Todos";
                    for(int i=0; i<response.body().size();i++)
                        listaNombres[i+1]=response.body().get(i).getNombre_rol();
                    actualizarRoles(listaNombres);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Rol>> call, Throwable t) {

            }
        });
    }

    private void actualizarRoles(String[]listaRoles){
        ArrayAdapter<String> listaAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, listaRoles);
        OpcionesRoles.setAdapter(listaAdapter);
        // if(usuario.getCodigo_rol()>0)
       // OpcionesRoles.setSelection(actividadConUsuario.usuario.getCodigo_rol()-1);
    }

    private void irAusuario()
    {
        FragmentUsuario fragmentUsuario= new FragmentUsuario(actividadConUsuario);
        actividadConUsuario.cambiarFragmento(fragmentUsuario);
    }
}