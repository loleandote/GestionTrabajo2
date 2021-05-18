package com.example.gestiontrabajo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.gestiontrabajo.Conexión.apiUsuario;
import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.Perfil.FragmentPerfil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentConfiguracionPerfil extends Fragment {
    ActividadConUsuario actividadConUsuario;
    View vista;
    public FragmentConfiguracionPerfil() {
        // Required empty public constructor
    }
    public FragmentConfiguracionPerfil(ActividadConUsuario actividadConUsuario) {
        this.actividadConUsuario=actividadConUsuario;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_configuracion_perfil, container, false);
        EditText nombreEditText= vista.findViewById(R.id.nombreEditTexto);
        EditText contraseñaEditText = vista.findViewById(R.id.contraseñaEditTexto);
        EditText correoEditText = vista.findViewById(R.id.correoEditTexto);
        nombreEditText.setText(actividadConUsuario.usuario.getNombre_usuario());
        contraseñaEditText.setText(actividadConUsuario.usuario.getContraseña_usuario());
        correoEditText.setText(actividadConUsuario.usuario.getCorreo_usuario());
        Button boton = vista.findViewById(R.id.RegistrarUsuario);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiUsuario apiUsuario = actividadConUsuario.retrofit.create(com.example.gestiontrabajo.Conexión.apiUsuario.class);
                actividadConUsuario.usuario.setNombre_usuario(String.valueOf(nombreEditText.getText()));
                actividadConUsuario.usuario.setContraseña_usuario(String.valueOf(contraseñaEditText.getText()));
                actividadConUsuario.usuario.setCorreo_usuario(String.valueOf(correoEditText.getText()));
                Call<Usuario>respuesta = apiUsuario.actualizarUsuario(actividadConUsuario.usuario.getId(),actividadConUsuario.usuario);
                respuesta.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        if (response.isSuccessful()){
                            FragmentPerfil fragmentPerfil = new FragmentPerfil(actividadConUsuario);
                            actividadConUsuario.cambiarFragmento(fragmentPerfil);
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {

                    }
                });
            }
        });
        return vista;
    }
}