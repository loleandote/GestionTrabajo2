package com.example.gestiontrabajo.Usuarios;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.R;


public class FragmentUsuario extends Fragment {

   public ActividadConUsuario actividadConUsuario;
   private Usuario usuario;

    public FragmentUsuario() {
        // Required empty public constructor
    }

    public FragmentUsuario(ActividadConUsuario actividadConUsuario, Usuario usuario) {
        this.actividadConUsuario = actividadConUsuario;
        this.usuario = usuario;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_usuario, container, false);
    }
}