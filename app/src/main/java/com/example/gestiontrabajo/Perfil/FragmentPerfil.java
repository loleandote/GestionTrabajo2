package com.example.gestiontrabajo.Perfil;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.MainActivity;
import com.example.gestiontrabajo.Observaciones.FragmentObservaciones;
import com.example.gestiontrabajo.R;
import com.example.gestiontrabajo.Reservas.FragmentReservas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentPerfil extends Fragment {
    public ActividadConUsuario actividadConUsuario;
    private int OpcionPulsada;
    private View vista;
    private RecyclerView recyclerView;
    private PerfilAdapter perfilAdapter;
    private FloatingActionButton IrAObservaciones;
    public FragmentPerfil() {
        // Required empty public constructor
    }
    public FragmentPerfil(ActividadConUsuario actividadConUsuario) {
        this.actividadConUsuario= actividadConUsuario;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_perfil, container, false);
        IrAObservaciones = vista.findViewById(R.id.IrAObservaciones);
        IrAObservaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentObservaciones fragmentObservaciones= new FragmentObservaciones(actividadConUsuario,actividadConUsuario.usuario,true);
                actividadConUsuario.cambiarFragmento(fragmentObservaciones, actividadConUsuario.getResources().getString(R.string.Observaciones));
            }
        });
        recyclerView = vista.findViewById(R.id.ReciclerViewPerfil);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        String[]opciones = obtenerValoresOpciones();
        perfilAdapter = new PerfilAdapter(getActivity(),opciones);
        recyclerView.setAdapter(perfilAdapter);
        Resources res = getResources();
        perfilAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpcionPulsada= recyclerView.getChildAdapterPosition(v);
                // String opcion =perfilAdapter.lista[OpcionPulsada];
                if (OpcionPulsada ==0){
                    FragmentIdiomas fragmentIdiomas = new FragmentIdiomas(actividadConUsuario);
                    actividadConUsuario.cambiarFragmento(fragmentIdiomas, actividadConUsuario.getResources().getString(R.string.Idiomas));
                }
                switch (OpcionPulsada)
                {
                    case 0:
                        FragmentIdiomas fragmentIdiomas = new FragmentIdiomas(actividadConUsuario);
                        actividadConUsuario.cambiarFragmento(fragmentIdiomas, actividadConUsuario.getResources().getString(R.string.Idiomas));
                        break;
                    case 1:
                        FragmentConfiguracionPerfil fragmentConfiguracionPerfil = new FragmentConfiguracionPerfil(actividadConUsuario);
                        actividadConUsuario.cambiarFragmento(fragmentConfiguracionPerfil, actividadConUsuario.getResources().getString(R.string.MisDatos));
                        break;
                    case 2:
                        Intent intent= new Intent(actividadConUsuario, MainActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (actividadConUsuario.drawerLayout.isDrawerOpen(Gravity.LEFT))
                    actividadConUsuario.drawerLayout.closeDrawers();
                else {
                    FragmentReservas fragmentReservas = new FragmentReservas(actividadConUsuario, true);
                    actividadConUsuario.cambiarFragmento(fragmentReservas, actividadConUsuario.getResources().getString(R.string.Reservas));
                }
                // Handle the back button event
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return vista;
    }
    private String[] obtenerValoresOpciones(){
        Resources res = getResources();
        String[] opcion = res.getStringArray(R.array.OpcionesPerfil);
        return  opcion;
    }

}