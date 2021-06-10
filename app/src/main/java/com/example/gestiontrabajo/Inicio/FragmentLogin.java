package com.example.gestiontrabajo.Inicio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiUsuarios;
import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.MainActivity;
import com.example.gestiontrabajo.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLogin extends Fragment {

    public MainActivity mainActivity;
    private View vista;
    private LayoutInflater layoutInflater;

    public FragmentLogin() {
    }

    public FragmentLogin(MainActivity mainActivity) {
        this.mainActivity= mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_login, container, false);
        layoutInflater= inflater;
        EditText nombreEditText = vista.findViewById(R.id.nombreEditText);
        EditText contraseñaEditText = vista.findViewById(R.id.contraseñaEditText);
        Button entrar = vista.findViewById(R.id.EntrarButton);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre= String.valueOf(nombreEditText.getText());
                String contraseña= String.valueOf(contraseñaEditText.getText());
                obtenerUsuario(nombre, contraseña);
               /* Intent intent= new Intent(mainActivity, ActividadConUsuario.class);
                startActivity(intent);*/
            }
        });
        // Creo que lo voy a quitar, no tiene sentido poder crear un usuario de trabajo, para eso los admin podrán crearlo

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finishAffinity();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return vista;
    }
    private void obtenerUsuario(String nombre, String contraseña){
        apiUsuarios apiUsuarios = mainActivity.retrofit.create(apiUsuarios.class);
        Call<ArrayList<Usuario>> respuesta= apiUsuarios.obtenerUsuario(nombre, contraseña);
        respuesta.enqueue(new Callback<ArrayList<Usuario>>() {
            @Override
            public void onResponse(Call<ArrayList<Usuario>> call, Response<ArrayList<Usuario>> response) {
                ArrayList<Usuario> lista = response.body();
                if(lista.size()>0){
                    if (lista.get(0).isPenalizado()) {
                            Date fecha = new Date (lista.get(0).getDia_fin_pena(), lista.get(0).getMes_fin_pena(), lista.get(0).getAnyo_fin_pena());
                            Date hoy = new Date();
                            if (fecha.compareTo(hoy) >= 0)
                                quitarPena(lista.get(0), apiUsuarios);
                            else
                                mainActivity.mensajeError(vista, layoutInflater,R.string.UsuarioBaneado);

                    }
                    else
                    if(!lista.get(0).isEs_cliente()) {
                        Usuario usuario = lista.get(0);
                        Intent intent= new Intent(mainActivity, ActividadConUsuario.class);
                        intent.putExtra("usuario", usuario.getId());
                        startActivity(intent);
                    }

                }

            }

            private void quitarPena(Usuario usuario, apiUsuarios apiUsuarios1){
                usuario.setPenalizado(false);
                Call<Usuario> respuesta = apiUsuarios1.actualizarUsuario(usuario.getId(), usuario);
                respuesta.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        if (response.isSuccessful()){
                            Intent intent= new Intent(mainActivity, ActividadConUsuario.class);
                            intent.putExtra("usuario", usuario.getId());
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<Usuario>> call, Throwable t) {
                //Toast.makeText(getActivity(),"nada",Toast.LENGTH_LONG).show();
                System.err.println(String.valueOf(t.getCause()));
            }
        });

    }
}