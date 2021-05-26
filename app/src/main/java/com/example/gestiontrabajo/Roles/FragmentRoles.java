package com.example.gestiontrabajo.Roles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiRol;
import com.example.gestiontrabajo.Datos.Rol;
import com.example.gestiontrabajo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentRoles extends Fragment {

    public ActividadConUsuario actividadConUsuario;
    private View vista;
    private RolAdapter rolAdapter;
    public FragmentRoles() {
        // Required empty public constructor
    }

    public FragmentRoles(ActividadConUsuario actividadConUsuario) {
        this.actividadConUsuario= actividadConUsuario;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_roles, container, false);
        FloatingActionButton IrAAñadirRol = vista.findViewById(R.id.IrañadirRol);
        IrAAñadirRol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRol fragmentRol= new FragmentRol(actividadConUsuario);
                actividadConUsuario.cambiarFragmento(fragmentRol);
            }
        });
        RecyclerView recyclerView= vista.findViewById(R.id.ReciclerViewRoles);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        rolAdapter= new RolAdapter(getActivity());
        rolAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rolPulsado = recyclerView.getChildAdapterPosition(v);
                Rol rol = rolAdapter.getLista().get(rolPulsado);
                FragmentRol fragmentRol = new FragmentRol(actividadConUsuario,rol);
                actividadConUsuario.cambiarFragmento(fragmentRol);
            }
        });
        recyclerView.setAdapter(rolAdapter);
        obtenerRoles();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                getActivity().finishAffinity();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return vista;
    }
    private void obtenerRoles(){
        apiRol apiRol = actividadConUsuario.retrofit.create(com.example.gestiontrabajo.Conexión.apiRol.class);
        Call<ArrayList<Rol>>listaRoles = apiRol.obtenerRoles();
        listaRoles.enqueue(new Callback<ArrayList<Rol>>() {
            @Override
            public void onResponse(Call<ArrayList<Rol>> call, Response<ArrayList<Rol>> response) {
                if (response.isSuccessful()){
                    rolAdapter.anyadirALista(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Rol>> call, Throwable t) {

            }
        });
    }
}