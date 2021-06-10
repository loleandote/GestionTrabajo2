package com.example.gestiontrabajo.Reservas;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiReservas;
import com.example.gestiontrabajo.Datos.Reserva;
import com.example.gestiontrabajo.Instalaciones.FragmentInstalaciones;
import com.example.gestiontrabajo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentReservas extends Fragment {
    public ActividadConUsuario actividadConUsuario;
    private View vista;
    private RecyclerView recyclerView;
    public ReservaAdapter reservaAdapter;
    private Spinner AñosUsuario;
    private Spinner MesesAño;
    private boolean soloYo;
    private int  mes, año;
    private boolean seleccion= false;
    public FragmentReservas() {
        // Required empty public constructor
    }

    public FragmentReservas(boolean soloYo) {
        this.soloYo= soloYo;
    }

    public FragmentReservas(ActividadConUsuario actividadConUsuario) {
        this.actividadConUsuario= actividadConUsuario;
    }

    public FragmentReservas(ActividadConUsuario actividadConUsuario,boolean soloYo) {
        this.actividadConUsuario= actividadConUsuario;
        this.soloYo= soloYo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_reservas, container, false);
        Date fecha = new Date();
        mes= fecha.getMonth()+1;
        año = fecha.getYear()+1900;
        AñosUsuario = vista.findViewById(R.id.AñosUsuario);
        ArrayList<Integer>listaAños = new ArrayList<>();

        int diferencia;
        if (soloYo)
        diferencia= año-actividadConUsuario.usuario.getAnyo_alta();
        else
            diferencia=año-2021;
        for (int i=0;i<=diferencia;i++){
            listaAños.add(actividadConUsuario.usuario.getAnyo_alta()+i);
        }
        ArrayAdapter<Integer> listaAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, listaAños);
        AñosUsuario.setAdapter(listaAdapter);
        AñosUsuario.setSelection(listaAños.size()-1);
        AñosUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                año = listaAños.get(0)+position;
                if (seleccion)
                if(soloYo)
                    obtenerDatosUsuario();
                else
                    obtenerDatos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        MesesAño = vista.findViewById(R.id.MesesAño);
        Resources res = getResources();
        String[] listaMeses = res.getStringArray(R.array.MesesAño);
        ArrayAdapter<String> listaMesesAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, listaMeses);
        MesesAño.setAdapter(listaMesesAdapter);
        MesesAño.setSelection(mes-1);
        MesesAño.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mes= position+1;
                if(seleccion)
                if(soloYo)
                    obtenerDatosUsuario();
                else
                    obtenerDatos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner orden = vista.findViewById(R.id.OpcionesOrden);
        orden.setSelection(1);

        String[] opcion = res.getStringArray(R.array.OpcionesOrdenarReservas);
        ArrayAdapter<String> listaAdapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, opcion);
        orden.setAdapter(listaAdapter2);
        orden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Reserva> lista =reservaAdapter.lista;
                switch (position){

                    case 0:
                        //filtro por precio asc
                         Collections.sort(lista, new Comparator<Reserva>() {
                            @Override
                            public int compare(Reserva u1, Reserva u2) {
                                return Integer.compare(u1.getPrecio(), u2.getPrecio());
                            }
                        });
                       reservaAdapter.anyadirALista(lista);
                        break;
                    case 1:
                        //filtro por precio desc
                        Collections.sort(lista, new Comparator<Reserva>() {
                            @Override
                            public int compare(Reserva u1, Reserva u2) {
                                return Integer.compare(u2.getPrecio(), u1.getPrecio());
                            }
                        });
                        break;
                    case 2:
                        //filtro por fecha asc
                        Collections.sort(lista, new Comparator<Reserva>() {
                            @Override
                            public int compare(Reserva u1, Reserva u2) {
                                Date fecha1 = new Date(u1.getAnyo(), u1.getMes(), u1.getDia());
                                Date fecha2 = new Date(u2.getAnyo(), u2.getMes(), u2.getDia());
                                return fecha1.compareTo(fecha2);
                            }
                        });
                        break;
                    case 3:
                        //filtro por fecha desc
                        Collections.sort(lista, new Comparator<Reserva>() {
                            @Override
                            public int compare(Reserva u1, Reserva u2) {
                                Date fecha1 = new Date(u1.getAnyo(), u1.getMes(), u1.getDia());
                                Date fecha2 = new Date(u2.getAnyo(), u2.getMes(), u2.getDia());
                                return fecha2.compareTo(fecha1);
                            }
                        });
                        break;
                }
                reservaAdapter.anyadirALista(lista);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        recyclerView = vista.findViewById(R.id.ReciclerViewReservas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        reservaAdapter = new ReservaAdapter(getActivity());
        reservaAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resevaPulsada = recyclerView.getChildAdapterPosition(v);

                Reserva reserva = reservaAdapter.lista.get(resevaPulsada);
                seleccionarReserva(reserva);
            }
        });
        recyclerView.setAdapter(reservaAdapter);
        if (soloYo)
            obtenerDatosUsuario();
       else
            obtenerDatos();

        FloatingActionButton IrAInstalaciones = vista.findViewById(R.id.IrAInstalaciones);
        if (!actividadConUsuario.rol.isRealiza_reservas()&& !actividadConUsuario.rol.isRealiza_reservas_otros())
            IrAInstalaciones.setVisibility(View.INVISIBLE);
        IrAInstalaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentInstalaciones fragmentInstalaciones = new FragmentInstalaciones(actividadConUsuario,true);
                actividadConUsuario.cambiarFragmento(fragmentInstalaciones, actividadConUsuario.getResources().getString(R.string.Instalaciones));
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (actividadConUsuario.drawerLayout.isDrawerOpen(Gravity.LEFT))
                    actividadConUsuario.drawerLayout.closeDrawers();
                else{
                    if (soloYo)
                        getActivity().finishAffinity();
                    else {
                        FragmentReservas fragmentReservas = new FragmentReservas(actividadConUsuario, true);
                        actividadConUsuario.cambiarFragmento(fragmentReservas, actividadConUsuario.getResources().getString(R.string.MisReservas));
                    }
                }
                // Handle the back button event

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return vista;
    }


    private void  obtenerDatos(){
        apiReservas api = actividadConUsuario.retrofit.create(apiReservas.class);
        Call<ArrayList<Reserva>> respuesta1 = api.obtenerReservas(mes, año);

        respuesta1.enqueue(new Callback<ArrayList<Reserva>>() {
            @Override
            public void onResponse(Call<ArrayList<Reserva>> call, Response<ArrayList<Reserva>> response) {
                if(response.isSuccessful()) {
                    ArrayList<Reserva> listareservas = response.body();
                    reservaAdapter.anyadirALista(listareservas);
                } else{
                    Toast.makeText(getActivity(), "Fallo en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Reserva>> call, Throwable t) {
                //Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }
    private void  obtenerDatosUsuario(){
        apiReservas api = actividadConUsuario.retrofit.create(apiReservas.class);
        Call<ArrayList<Reserva>> respuesta1 = api.obtenerReservasMesUsuario(mes,año,actividadConUsuario.usuario.getId());

        respuesta1.enqueue(new Callback<ArrayList<Reserva>>() {
            @Override
            public void onResponse(Call<ArrayList<Reserva>> call, Response<ArrayList<Reserva>> response) {
                if(response.isSuccessful()) {
                    ArrayList<Reserva> listareservas = response.body();
                    reservaAdapter.anyadirALista(listareservas);
                } else{
                    Toast.makeText(getActivity(), "Fallo en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Reserva>> call, Throwable t) {
                //Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void seleccionarReserva(Reserva reserva){

        FragmentReserva fragmentReserva = new FragmentReserva(actividadConUsuario,this);
        fragmentReserva.reserva= reserva;
        actividadConUsuario.cambiarFragmento(fragmentReserva, actividadConUsuario.getResources().getString(R.string.Reserva));
    }
}