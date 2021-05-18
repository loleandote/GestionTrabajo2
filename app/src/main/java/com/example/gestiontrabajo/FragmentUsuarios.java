package com.example.gestiontrabajo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.gestiontrabajo.Conexión.apiRol;
import com.example.gestiontrabajo.Datos.Rol;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


public class FragmentUsuarios extends Fragment {
    ActividadConUsuario actividadConUsuario;
    View vista;

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

        return vista;
    }
}