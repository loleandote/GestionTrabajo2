package com.example.gestiontrabajo.Observaciones;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiObservaciones;
import com.example.gestiontrabajo.Datos.Observación;
import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.Perfil.FragmentPerfil;
import com.example.gestiontrabajo.R;
import com.example.gestiontrabajo.Usuarios.FragmentUsuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentObservaciones extends Fragment {

    View vista;
    ActividadConUsuario actividadConUsuario;
    Usuario usuario;
    boolean yo;
    ObservaciónAdapter observaciónAdapter;

    RecyclerView recyclerView;
    public FragmentObservaciones() {
        // Required empty public constructor
    }
    public FragmentObservaciones(ActividadConUsuario actividadConUsuario,Usuario usuario, boolean yo) {
        this.actividadConUsuario=actividadConUsuario;
        this.usuario = usuario;
        this.yo= yo;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_observaciones, container, false);
        TextView TituloDeLasObservaciones= vista.findViewById(R.id.TituloDeLasObservaciones);
        Resources res = getResources();
        if (actividadConUsuario.lenguaje=="es"|| actividadConUsuario.lenguaje=="")
            TituloDeLasObservaciones.setText(res.getString(R.string.ObservacionesDe)+usuario.getNombre_usuario());
        else
            TituloDeLasObservaciones.setText(usuario.getNombre_usuario()+res.getString(R.string.ObservacionesDe));
        recyclerView = vista.findViewById(R.id.TitulosObservaciones);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        observaciónAdapter = new ObservaciónAdapter(getActivity());
        observaciónAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int observaciónPulsada = recyclerView.getChildAdapterPosition(v);
                Observación observación = observaciónAdapter.lista.get(observaciónPulsada);
                FragmentObservacion fragmentObservacion= new FragmentObservacion(actividadConUsuario, observación, usuario, yo);
                actividadConUsuario.cambiarFragmento(fragmentObservacion, actividadConUsuario.getResources().getString(R.string.Observacion));
            }
        });
        recyclerView.setAdapter(observaciónAdapter);
        FloatingActionButton IrAAñadirObservaciones = vista.findViewById(R.id.IrAAñadirObservaciones);
       IrAAñadirObservaciones.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Observación observación = new Observación();
               FragmentObservacion fragmentObservacion= new FragmentObservacion(actividadConUsuario,observación,usuario,yo);
               actividadConUsuario.cambiarFragmento(fragmentObservacion, actividadConUsuario.getResources().getString(R.string.Observacion));
           }
       });
        obtenerObservaciones();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (actividadConUsuario.drawerLayout.isDrawerOpen(Gravity.LEFT))
                    actividadConUsuario.drawerLayout.closeDrawers();
                else {
                    if (!yo) {
                        FragmentUsuario fragmentUsuarios = new FragmentUsuario(actividadConUsuario, usuario);
                        actividadConUsuario.cambiarFragmento(fragmentUsuarios, actividadConUsuario.getResources().getString(R.string.Usuarios));
                    } else {
                        FragmentPerfil fragmentPerfil = new FragmentPerfil(actividadConUsuario);
                        actividadConUsuario.cambiarFragmento(fragmentPerfil, actividadConUsuario.getResources().getString(R.string.Perfil));
                    }
                }
                // Handle the back button event

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return vista;
    }

    private void obtenerObservaciones()
    {
        apiObservaciones apiObservaciones = actividadConUsuario.retrofit.create(com.example.gestiontrabajo.Conexión.apiObservaciones.class);
        Call<ArrayList<Observación>> respuesta = apiObservaciones.obtenerObservacionesUsuario(usuario.getId());
        respuesta.enqueue(new Callback<ArrayList<Observación>>() {
            @Override
            public void onResponse(Call<ArrayList<Observación>> call, Response<ArrayList<Observación>> response) {
                if (response.isSuccessful()){
                    observaciónAdapter.anyadirALista(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Observación>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}