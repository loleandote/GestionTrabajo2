package com.example.gestiontrabajo.Instalaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiInstalaciones;
import com.example.gestiontrabajo.Datos.Instalación;
import com.example.gestiontrabajo.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentCrearInstalacion extends Fragment {

    private View vista;
    private ActividadConUsuario actividadConUsuario;
    private Instalación instalación;
    public FragmentCrearInstalacion() {
        // Required empty public constructor
    }

    public FragmentCrearInstalacion(ActividadConUsuario actividadConUsuario){
        this.actividadConUsuario= actividadConUsuario;
    }

    public FragmentCrearInstalacion(ActividadConUsuario actividadConUsuario, Instalación instalación){
        this.actividadConUsuario= actividadConUsuario;
        this.instalación= instalación;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_crear_instalacion, container, false);
        EditText nombreInstalacion = vista.findViewById(R.id.nombreInstalacionEditText);
        EditText precioHora = vista.findViewById(R.id.PrecioHoraEditText);
        EditText inicioInstalacion = vista.findViewById(R.id.inicioEditText);
        EditText finInstalacion = vista.findViewById(R.id.finEditText);
        EditText minimoInstalacion = vista.findViewById(R.id.TiempoMinimoEditText);
        EditText maximoInstalacion = vista.findViewById(R.id.TiempoMaximoEditText);
        Button Guardar = vista.findViewById(R.id.GuardarInstalacion);
        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Guardar
                boolean crear = false;
                if (instalación==null){
                    crear= true;
                    instalación = new Instalación();
                }
                instalación.setTipo("tenis");
                instalación.setNombre(String.valueOf(nombreInstalacion.getText()));
                ArrayList<Integer>horario = new ArrayList<>();
                //Integer.parseInt(String.valueOf(precioHora.getText()))
                instalación.setPrecio_hora(1);
                int inicio = Integer.parseInt(String.valueOf(inicioInstalacion.getText()));
                int fin = Integer.parseInt(String.valueOf(finInstalacion.getText()));
                while(fin>= inicio){
                    horario.add(inicio);
                    inicio++;
                }
                instalación.setHorario(horario);
                ArrayList<String>imagenes=new ArrayList<>();
                imagenes.add("");
                imagenes.add("");
                instalación.setImagenes(imagenes);
                instalación.setTiempo_max(Integer.parseInt(String.valueOf(maximoInstalacion.getText())));
                instalación.setTiempo_min(Integer.parseInt(String.valueOf(minimoInstalacion.getText())));
                if (instalación.getId()==0)
                crearInsatalcion();
                else
                    actualizarInstalacion();
            }
        });
        return vista;
    }
    private void crearInsatalcion(){
        apiInstalaciones apiInstalaciones = actividadConUsuario.retrofit.create(com.example.gestiontrabajo.Conexión.apiInstalaciones.class);
        Call<Instalación>respuesta = apiInstalaciones.guardaInstalacion(instalación.getTipo(),instalación.getNombre(),instalación.getPrecio_hora(),instalación.getTiempo_min(),instalación.getTiempo_max(),instalación.getImagenes(), instalación.getHorario());
        respuesta.enqueue(new Callback<Instalación>() {
            @Override
            public void onResponse(Call<Instalación> call, Response<Instalación> response) {
                if (response.isSuccessful()){
                    FragmentInstalaciones fragmentInstalaciones = new FragmentInstalaciones(actividadConUsuario);
                    actividadConUsuario.cambiarFragmento(fragmentInstalaciones);
                }
            }

            @Override
            public void onFailure(Call<Instalación> call, Throwable t) {

            }
        });
    }
    private void actualizarInstalacion(){
        apiInstalaciones apiInstalaciones = actividadConUsuario.retrofit.create(com.example.gestiontrabajo.Conexión.apiInstalaciones.class);
        Call<Instalación>respuesta = apiInstalaciones.actualizarInstalacion(instalación.getId(),instalación);
        respuesta.enqueue(new Callback<Instalación>() {
            @Override
            public void onResponse(Call<Instalación> call, Response<Instalación> response) {
                if (response.isSuccessful()){
                    FragmentInstalaciones fragmentInstalaciones= new FragmentInstalaciones(actividadConUsuario);
                    actividadConUsuario.cambiarFragmento(fragmentInstalaciones);
                }
            }

            @Override
            public void onFailure(Call<Instalación> call, Throwable t) {

            }
        });

    }
}