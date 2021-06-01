package com.example.gestiontrabajo.Observaciones;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiObservaciones;
import com.example.gestiontrabajo.Datos.Observación;
import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.Observaciones.FragmentObservaciones;
import com.example.gestiontrabajo.R;
import com.example.gestiontrabajo.Reservas.FragmentReservas;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentObservacion extends Fragment {


ActividadConUsuario actividadConUsuario;
Observación observación;
View vista;
Usuario usuario;
EditText editTextTitulo;
EditText editTextContenido;
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
        if (observación!= null){
            observación= new Observación();
        observación.setId_usuario(usuario.getId());
        }
        editTextTitulo= vista.findViewById(R.id.editTextTitulo);

        Button botonGuardarObservacion = vista.findViewById(R.id.BotonGuardarObservacion);
        botonGuardarObservacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarObservacion();
            }
        });
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
    private void guardarObservacion(){
        observación.setTitulo(String.valueOf(editTextTitulo.getText()));
        observación.setContenido(String.valueOf(editTextContenido.getText()));
        apiObservaciones apiObservaciones = actividadConUsuario.retrofit.create(com.example.gestiontrabajo.Conexión.apiObservaciones.class);
        Call<Observación> respuesta = apiObservaciones.guardaObservacion(observación.getId_usuario(), observación.getTitulo(),observación.getContenido());
        respuesta.enqueue(new Callback<Observación>() {
            @Override
            public void onResponse(Call<Observación> call, Response<Observación> response) {
                if(response.isSuccessful()){
                    FragmentObservaciones fragmentObservaciones = new FragmentObservaciones(actividadConUsuario,usuario, yo);
                    actividadConUsuario.cambiarFragmento(fragmentObservaciones);
                }
            }

            @Override
            public void onFailure(Call<Observación> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}