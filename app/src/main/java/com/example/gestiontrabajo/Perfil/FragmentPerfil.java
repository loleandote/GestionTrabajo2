package com.example.gestiontrabajo.Perfil;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.MainActivity;
import com.example.gestiontrabajo.R;

public class FragmentPerfil extends Fragment {
    public ActividadConUsuario actividadConUsuario;
    private int OpcionPulsada;
    private View vista;
    private RecyclerView recyclerView;
    private PerfilAdapter perfilAdapter;
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
                    actividadConUsuario.cambiarFragmento(fragmentIdiomas);
                }
                switch (OpcionPulsada)
                {
                    case 0:
                        FragmentIdiomas fragmentIdiomas = new FragmentIdiomas(actividadConUsuario);
                        actividadConUsuario.cambiarFragmento(fragmentIdiomas);
                        break;
                    case 1:
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
                getActivity().finishAffinity(); System.exit(0);
                // getActivity().finishAffinity();
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