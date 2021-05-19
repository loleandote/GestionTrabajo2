package com.example.gestiontrabajo.Usuarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiRol;
import com.example.gestiontrabajo.Conexión.apiUsuario;
import com.example.gestiontrabajo.Datos.Rol;
import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentUsuarios extends Fragment {
   public ActividadConUsuario actividadConUsuario;
    View vista;
    private RecyclerView recyclerView;
    private UsuarioAdapter usuarioAdapter;
    private int usuarioPulsado;
    public FragmentUsuarios() {
        // Required empty public constructor
    }


    public FragmentUsuarios(ActividadConUsuario actividadConUsuario){
        this.actividadConUsuario=actividadConUsuario;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_usuarios, container, false);
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
                seleccionarUsuario(usuario);
            }
        });
        obtenerUsuarios();
        return vista;
    }

    private void seleccionarUsuario(Usuario usuario){
        FragmentUsuario fragmentUsuario= new FragmentUsuario(actividadConUsuario, usuario);
        actividadConUsuario.cambiarFragmento(fragmentUsuario);
    }
    private void obtenerUsuarios()
    {
        apiUsuario apiUsuario = actividadConUsuario.retrofit.create(com.example.gestiontrabajo.Conexión.apiUsuario.class);
        Call<ArrayList<Usuario>>respuesta = apiUsuario.obtenerUsuario();
        respuesta.enqueue(new Callback<ArrayList<Usuario>>() {
            @Override
            public void onResponse(Call<ArrayList<Usuario>> call, Response<ArrayList<Usuario>> response) {
                if (response.isSuccessful()){
                    System.out.println("hola   "+String.valueOf(response.body().size()));
                    usuarioAdapter.actualizarListaUsuarios(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Usuario>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}