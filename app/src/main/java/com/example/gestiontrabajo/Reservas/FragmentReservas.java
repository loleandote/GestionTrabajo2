package com.example.gestiontrabajo.Reservas;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiReservas;
import com.example.gestiontrabajo.Conexión.apiRol;
import com.example.gestiontrabajo.DatePickerFragment;
import com.example.gestiontrabajo.Datos.Reserva;
import com.example.gestiontrabajo.Datos.Rol;
import com.example.gestiontrabajo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentReservas extends Fragment {
    public ActividadConUsuario actividadConUsuario;
    private View vista;
    private RecyclerView recyclerView;
    public ReservaAdapter reservaAdapter;
    private EditText reservaFechaInicio;
    private EditText reservaFechaFin;
    private boolean soloYo;
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
        reservaFechaInicio= vista.findViewById(R.id.ReservaFechaInicio);
        reservaFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        reservaFechaFin= vista.findViewById(R.id.ReservaFechaFin);
        reservaFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog2();
            }
        });
        Spinner orden = vista.findViewById(R.id.OpcionesOrden);
        orden.setSelection(1);
        Resources res = getResources();
        String[] opcion = res.getStringArray(R.array.OpcionesOrdenarReservas);
        ArrayAdapter<String> listaAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, opcion);
        orden.setAdapter(listaAdapter);
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
        if (soloYo) {
            System.out.println("noadsf");
            obtenerDatos(actividadConUsuario.usuario.getId());
        }
        else {
            System.out.println("malo");
            obtenerDatos();
        }
        //obtenerRoles();
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

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + "-" + (month+1) + "-" + year;
                reservaFechaInicio.setText(selectedDate);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
    private void showDatePickerDialog2() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + "-" + (month+1) + "-" + year;
                reservaFechaFin.setText(selectedDate);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
    private void  obtenerDatos(){
        apiReservas api = actividadConUsuario.retrofit.create(apiReservas.class);
        Call<ArrayList<Reserva>> respuesta1 = api.obtenerReservas();

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
    private void  obtenerDatos(int idUsuario){
        apiReservas api = actividadConUsuario.retrofit.create(apiReservas.class);
        Call<ArrayList<Reserva>> respuesta1 = api.obtenerReservas(idUsuario);

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
        actividadConUsuario.cambiarFragmento(fragmentReserva);
    }
}