package com.example.gestiontrabajo.Inicio;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.example.gestiontrabajo.Conexión.apiUsuario;
import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.MainActivity;
import com.example.gestiontrabajo.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRegistro extends Fragment {

    private MainActivity mainActivity;
    View vista;
    public FragmentRegistro() {
        // Required empty public constructor
    }
    public FragmentRegistro(MainActivity mainActivity) {
        this.mainActivity= mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista= inflater.inflate(R.layout.fragment_registro, container, false);
        EditText nombreEditText = vista.findViewById(R.id.nombreEditTexto);
        EditText contraseñaEditText = vista.findViewById(R.id.contraseñaEditTexto);
        EditText correoEditText = vista.findViewById(R.id.correoEditTexto);

        Button registro = vista.findViewById(R.id.RegistrarUsuario);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre= String.valueOf(nombreEditText.getText());
                String contraseña = String.valueOf(contraseñaEditText.getText());
                String correo = String.valueOf(correoEditText.getText());
                if(nombre.length()>3&&nombre.length()<9&&contraseña.length()>7&& contraseña.length()<17&&validarEmail(correo))
                    guardarUsuario(nombre,contraseña,correo);
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                FragmentLogin fragmentLogin = new FragmentLogin(mainActivity);
                mainActivity.cambiarFragmento(fragmentLogin);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return  vista;
    }
    private void obtenerUsuario(String nombre, String contraseña, String correo){
        apiUsuario apiUsuario = mainActivity.retrofit.create(com.example.gestiontrabajo.Conexión.apiUsuario.class);
        Call<ArrayList<Usuario>> respuesta = apiUsuario.obtenerUsuarioNomberContraseñaCorreo(nombre, contraseña, correo);
        respuesta.enqueue(new Callback<ArrayList<Usuario>>() {
            @Override
            public void onResponse(Call<ArrayList<Usuario>> call, Response<ArrayList<Usuario>> response) {
                if (response.isSuccessful()){
                    ArrayList<Usuario> usuarios= response.body();
                    if( usuarios.size()==0){
                        guardarUsuario(nombre, contraseña, correo);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Usuario>> call, Throwable t) {

            }
        });
    }
    private void guardarUsuario(String nombre, String contraseña, String correo){
        apiUsuario apiUsuario = mainActivity.retrofit.create(com.example.gestiontrabajo.Conexión.apiUsuario.class);
        Date fechavalor= new Date();
        Call<Usuario> respuesta = apiUsuario.guardaUsuario(nombre, contraseña, correo,fechavalor.getDay(),fechavalor.getMonth(),fechavalor.getYear()+1900 ,100,false,0,0,0,1);
        respuesta.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
        FragmentLogin fragment_login= new FragmentLogin(mainActivity);
        mainActivity.cambiarFragmento(fragment_login);
    }
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}