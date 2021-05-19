package com.example.gestiontrabajo.Instalaciones;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiInstalaciones;
import com.example.gestiontrabajo.Datos.Instalación;
import com.example.gestiontrabajo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentInstalaciones extends Fragment {

    public ActividadConUsuario actividadConUsuario;
    private View vista;
    private RecyclerView recyclerView;
    public InstalaciónAdapter instalaciónAdapter;
    private int instalacionPulsada;
    public FragmentInstalaciones() {
        // Required empty public constructor
    }
    public FragmentInstalaciones(ActividadConUsuario actividadConUsuario) {
        this.actividadConUsuario=actividadConUsuario;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista= inflater.inflate(R.layout.fragment_instalaciones, container, false);
        Spinner orden = vista.findViewById(R.id.OpcionesOrdenInstalciones);
        Spinner tipo = vista.findViewById(R.id.TiposInstalciones);
        orden.setSelection(1);
        Resources res = getResources();
        String[] opcion = res.getStringArray(R.array.OpcionesOrdenarIntalaciones);
        ArrayAdapter<String> listaAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, opcion);
        orden.setAdapter(listaAdapter);
        orden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Instalación> lista =instalaciónAdapter.lista;
                switch (position){

                    case 0:
                        //filtro por precio asc
                        Collections.sort(lista, new Comparator<Instalación>() {
                            @Override
                            public int compare(Instalación u1, Instalación u2) {
                                return Integer.compare(u1.getPrecio_hora(),u2.getPrecio_hora());
                            }
                        });
                        break;
                    case 1:
                        //filtro por precio desc
                        Collections.sort(lista, new Comparator<Instalación>() {
                            @Override
                            public int compare(Instalación u1, Instalación u2) {
                                return Integer.compare(u2.getPrecio_hora(),u1.getPrecio_hora());
                            }
                        });
                        break;
                    case 2:
                        //filtro por fecha asc
                        Collections.sort(lista, new Comparator<Instalación>() {
                            @Override
                            public int compare(Instalación u1, Instalación u2) {
                                return  u1.getNombre().compareTo(u2.getNombre());
                            }
                        });
                        break;
                    case 3:
                        //filtro por fecha desc
                        Collections.sort(lista, new Comparator<Instalación>() {
                            @Override
                            public int compare(Instalación u1, Instalación u2) {
                                return  u2.getNombre().compareTo(u1.getNombre());
                            }
                        });
                        break;
                }
                instalaciónAdapter.anyadirALista(lista);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String[] tiposLengua = res.getStringArray(R.array.TiposInstalaciones);
        String[] tipos ={"Todas", "Tenis", "baloncesto"};
        ArrayAdapter<String>listaTiposAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item, tiposLengua);
        //tipo.setSelection(1);
        tipo.setAdapter(listaTiposAdapter);
        tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("llegado");
                if (position==0){
                    obtenerDatos();
                }else
                {
                    String tipo = tipos[position];
                    obtenerDatosPorTipo(tipo);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                obtenerDatos();
            }
        });
        recyclerView = vista.findViewById(R.id.ReciclerViewInstalaciones);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        instalaciónAdapter = new InstalaciónAdapter(getActivity());
        recyclerView.setAdapter(instalaciónAdapter);
        instalaciónAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instalacionPulsada= recyclerView.getChildAdapterPosition(v);
                Instalación instalación =instalaciónAdapter.lista.get(instalacionPulsada);
                System.out.println(instalación.getNombre());
                seleccionarInstalacion(instalación);
            }
        });
        FloatingActionButton IrACrearInstalacion = vista.findViewById(R.id.IrAñadirPista);
        IrACrearInstalacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentCrearInstalacion fragmentCrearInstalacion= new FragmentCrearInstalacion(actividadConUsuario);
                actividadConUsuario.cambiarFragmento(fragmentCrearInstalacion);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finishAffinity();
                // Handle the back button event

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return vista;
    }
    private void  obtenerDatos(){
        apiInstalaciones apiInstalaciones = actividadConUsuario.retrofit.create(apiInstalaciones.class);
        Call<ArrayList<Instalación>> respuesta = apiInstalaciones.obtenerInstalaciones();
        respuesta.enqueue(new Callback<ArrayList<Instalación>>() {
            @Override
            public void onResponse(Call<ArrayList<Instalación>> call, Response<ArrayList<Instalación>> response) {
                if (response.isSuccessful()){
                    ArrayList<Instalación>listaInstalaciones = response.body();
                    System.out.println(listaInstalaciones.size());
                    System.out.println(listaInstalaciones.get(1).getNombre());
                    instalaciónAdapter.anyadirALista(listaInstalaciones);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Instalación>> call, Throwable t) {

            }
        });
    }
    private void  obtenerDatosPorTipo(String tipo){
        apiInstalaciones apiInstalaciones = actividadConUsuario.retrofit.create(apiInstalaciones.class);
        Call<ArrayList<Instalación>> respuesta = apiInstalaciones.obtenerInstalacionesPorTipo(tipo);
        respuesta.enqueue(new Callback<ArrayList<Instalación>>() {
            @Override
            public void onResponse(Call<ArrayList<Instalación>> call, Response<ArrayList<Instalación>> response) {
                if (response.isSuccessful()){
                    ArrayList<Instalación>listaInstalaciones = response.body();
                    instalaciónAdapter.anyadirALista(listaInstalaciones);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Instalación>> call, Throwable t) {

            }
        });
    }
    private void seleccionarInstalacion(Instalación instalacion){
        FragmentInstalacion fragmentInstalacion= new FragmentInstalacion(actividadConUsuario);
        fragmentInstalacion.instalación= instalacion;
        actividadConUsuario.cambiarFragmento(fragmentInstalacion);
    }
}