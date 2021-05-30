package com.example.gestiontrabajo.Observaciones;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Datos.Observación;
import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.Observaciones.FragmentObservaciones;
import com.example.gestiontrabajo.R;
import com.example.gestiontrabajo.Reservas.FragmentReservas;


public class FragmentObservacion extends Fragment {


ActividadConUsuario actividadConUsuario;
Observación observación;
View vista;
Usuario usuario;
boolean yo;
    public FragmentObservacion() {
        // Required empty public constructor
    }

    public FragmentObservacion(ActividadConUsuario actividadConUsuario, Observación observación, Usuario usuario,boolean yo) {
        this.actividadConUsuario = actividadConUsuario;
        this.observación = observación;
        this.usuario = usuario;
        this.yo= yo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_observacion, container, false);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                FragmentObservaciones fragmentObservaciones= new FragmentObservaciones(actividadConUsuario,usuario, yo);
                actividadConUsuario.cambiarFragmento(fragmentObservaciones);
                // Handle the back button event
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return vista;
    }
}