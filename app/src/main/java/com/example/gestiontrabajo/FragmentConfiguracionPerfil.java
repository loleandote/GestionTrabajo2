package com.example.gestiontrabajo;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.example.gestiontrabajo.Conexión.apiUsuarios;
import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.Perfil.FragmentPerfil;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

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
        Resources resources = getResources();
        TextInputLayout cosa = vista.findViewById(R.id.textInputLayout);
        cosa.getEditText().setText("Hola");
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
                apiUsuarios apiUsuarios = actividadConUsuario.retrofit.create(apiUsuarios.class);
                actividadConUsuario.usuario.setNombre_usuario(String.valueOf(nombreEditText.getText()));
                actividadConUsuario.usuario.setContraseña_usuario(String.valueOf(contraseñaEditText.getText()));
                actividadConUsuario.usuario.setCorreo_usuario(String.valueOf(correoEditText.getText()));
                if( actividadConUsuario.usuario.getNombre_usuario().length()>3&& actividadConUsuario.usuario.getNombre_usuario().length()<9&& actividadConUsuario.usuario.getContraseña_usuario().length()>7&&  actividadConUsuario.usuario.getContraseña_usuario().length()<17&&validarEmail( actividadConUsuario.usuario.getCorreo_usuario())) {
                    Call<Usuario> respuesta = apiUsuarios.actualizarUsuario(actividadConUsuario.usuario.getId(), actividadConUsuario.usuario);
                    respuesta.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if (response.isSuccessful()) {
                                FragmentPerfil fragmentPerfil = new FragmentPerfil(actividadConUsuario);
                                actividadConUsuario.cambiarFragmento(fragmentPerfil);
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }  else
                if(actividadConUsuario.usuario.getNombre_usuario().length()>3&&actividadConUsuario.usuario.getNombre_usuario().length()<9)
                    Toast.makeText(getActivity(), resources.getString(R.string.NombreIncorrecto), Toast.LENGTH_SHORT).show();
                else if(actividadConUsuario.usuario.getContraseña_usuario().length()>7&& actividadConUsuario.usuario.getContraseña_usuario().length()<17)  Toast.makeText(getActivity(), resources.getString(R.string.ContraseñaIncorrecta), Toast.LENGTH_SHORT).show();
                else  Toast.makeText(getActivity(), resources.getString(R.string.NombreIncorrecto), Toast.LENGTH_SHORT).show();
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                FragmentPerfil fragmentPerfil= new FragmentPerfil(actividadConUsuario);
                actividadConUsuario.cambiarFragmento(fragmentPerfil);
              /*  getActivity().finishAffinity();
                System.exit(0);*/
                // getActivity().finishAffinity();
                // Handle the back button event
                //

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return vista;
    }
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}