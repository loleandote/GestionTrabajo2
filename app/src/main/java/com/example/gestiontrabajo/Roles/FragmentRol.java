package com.example.gestiontrabajo.Roles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiRoles;
import com.example.gestiontrabajo.Datos.Rol;
import com.example.gestiontrabajo.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRol extends Fragment {
    ActividadConUsuario actividadConUsuario;
    private View vista;
    // Rol a editar/crear
    private Rol rol;
    public FragmentRol() {
        // Required empty public constructor
    }
    public FragmentRol(ActividadConUsuario actividadConUsuario){
        this.actividadConUsuario= actividadConUsuario;
    }
    //Constructor edicion Rol
   public FragmentRol(ActividadConUsuario actividadConUsuario, Rol rol){
        this.actividadConUsuario= actividadConUsuario;
        this.rol = rol;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_rol, container, false);
        EditText nombre = vista.findViewById(R.id.NombreRolEditText);
        EditText rango3 = vista.findViewById(R.id.RangoRolEditText);
        SwitchMaterial reservar = vista.findViewById(R.id.HacerReservas);
        SwitchMaterial CambiarContraseña = vista.findViewById(R.id.CambiarContraseña);
        SwitchMaterial ModificarUsuario = vista.findViewById(R.id.ModUsu);
        SwitchMaterial ModificarRol = vista.findViewById(R.id.ModRol);
        SwitchMaterial ImportarExportarInf = vista.findViewById(R.id.ImpExpinformes);
        if(rol!=null){
            nombre.setText(rol.getNombre_rol());
            rango3.setText(String.valueOf(rol.getRango_rol()));
            reservar.setChecked(rol.isRealiza_reservas());
            CambiarContraseña.setChecked(rol.isCambiar_contraseña());
            ModificarRol.setChecked(rol.isMod_rol());
            ImportarExportarInf.setChecked(rol.isImp_exp());
        }
        Button guardarRol=vista.findViewById(R.id.GuardarRol);
        guardarRol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hacer que el botón funcione y guarde
                boolean crear = false;
                if (rol == null){
                    rol = new Rol();
                    crear= true;
                }
                rol.setNombre_rol(String.valueOf(nombre.getText()));
                int rango = Integer.parseInt(String.valueOf(rango3.getText()));
                rol.setRango_rol(rango);
                rol.setCambiar_contraseña(CambiarContraseña.isChecked());
                rol.setMod_usu(ModificarUsuario.isChecked());
                rol.setMod_rol(ModificarRol.isChecked());
                rol.setImp_exp(ImportarExportarInf.isChecked());
                //Guardar rol
               if (crear){
                    //Crea rol
                    apiRoles apiRoles = actividadConUsuario.retrofit.create(apiRoles.class);
                    Call<Rol> respuesta = apiRoles.guardaRol(rol.getNombre_rol(),rol.getRango_rol(),rol.isRealiza_reservas(),rol.isCambiar_contraseña(),rol.isMod_usu(),rol.isMod_rol(),rol.isImp_exp());
                    respuesta.enqueue(new Callback<Rol>() {
                        @Override
                        public void onResponse(Call<Rol> call, Response<Rol> response) {
                            if (response.isSuccessful()){
                                FragmentRoles fragmentRoles = new FragmentRoles(actividadConUsuario);
                                System.out.println("llego");
                                actividadConUsuario.cambiarFragmento(fragmentRoles);
                            }
                        }

                        @Override
                        public void onFailure(Call<Rol> call, Throwable t) {

                        }
                    });
                }else{
                    //Edita rol
                    apiRoles apiRoles =actividadConUsuario.retrofit.create(apiRoles.class);
                    Call<Rol>respuesta= apiRoles.actualizarRol(rol.getId(),rol);
                    respuesta.enqueue(new Callback<Rol>() {
                        @Override
                        public void onResponse(Call<Rol> call, Response<Rol> response) {
                            if (response.isSuccessful()){
                                FragmentRoles fragmentRoles = new FragmentRoles(actividadConUsuario);
                                actividadConUsuario.cambiarFragmento(fragmentRoles);
                            }
                        }

                        @Override
                        public void onFailure(Call<Rol> call, Throwable t) {

                        }
                    });
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
            FragmentRoles fragmentRoles = new FragmentRoles(actividadConUsuario);
            actividadConUsuario.cambiarFragmento(fragmentRoles);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return vista;
    }

}