package com.example.gestiontrabajo.Instalaciones;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiInstalaciones;
import com.example.gestiontrabajo.Datos.Instalación;
import com.example.gestiontrabajo.Perfil.FragmentPerfil;
import com.example.gestiontrabajo.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentCrearInstalacion extends Fragment {

    private View vista;
    private ActividadConUsuario actividadConUsuario;
    private Instalación instalación= new Instalación();
    private FragmentInstalaciones fragmentInstalaciones;
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
        Spinner tipo = vista.findViewById(R.id.tipoInstalacionSpinner);
        Resources res = getResources();
        String[] tiposLengua = res.getStringArray(R.array.TiposInstalaciones);
        String[] tiposLengua2 = new String[tiposLengua.length-1];
        for(int i=0;i<tiposLengua2.length;i++){
            int j = i+1;
            tiposLengua2[i]= tiposLengua[j];
        }
        ArrayAdapter<String> listaTiposAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item, tiposLengua2);
        //tipo.setSelection(1);
        tipo.setAdapter(listaTiposAdapter);
        tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                instalación.setTipo(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button Guardar = vista.findViewById(R.id.GuardarInstalacion);
        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Guardar
                if (instalación==null){
                    instalación = new Instalación();
                }
                instalación.setTipo(1);
                instalación.setNombre(String.valueOf(nombreInstalacion.getText()));
                ArrayList<Integer>horario = new ArrayList<>();
                int precio=Integer.parseInt(String.valueOf(precioHora.getText()));
                instalación.setPrecio_hora(precio);
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
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                FragmentPerfil fragmentPerfil = new FragmentPerfil(actividadConUsuario);
                actividadConUsuario.cambiarFragmento(fragmentPerfil);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return vista;
    }
    private void crearInsatalcion(){
        apiInstalaciones apiInstalaciones = actividadConUsuario.retrofit.create(com.example.gestiontrabajo.Conexión.apiInstalaciones.class);
        Call<Instalación>respuesta = apiInstalaciones.guardaInstalacion(instalación.getTipo(),instalación.getNombre(),instalación.getPrecio_hora(),instalación.getTiempo_min_reserva(),instalación.getTiempo_max_reserva(),instalación.getImagenes(), instalación.getHorario());
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