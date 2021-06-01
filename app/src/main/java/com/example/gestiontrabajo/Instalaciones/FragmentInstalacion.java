package com.example.gestiontrabajo.Instalaciones;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiReservas;
import com.example.gestiontrabajo.Conexión.apiUsuarios;
import com.example.gestiontrabajo.DatePickerFragment;
import com.example.gestiontrabajo.Datos.Instalación;
import com.example.gestiontrabajo.Datos.Reserva;
import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.R;
import com.example.gestiontrabajo.Reservas.FragmentReservas;
import com.example.gestiontrabajo.Usuarios.FragmentUsuarios;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentInstalacion extends Fragment {
    private ActividadConUsuario actividadConUsuario;
    Instalación instalación;
    private View vista;
    private EditText DiaEditText;
    private Spinner reservasInicioDisponibles;
    private Spinner reservasFinDisponibles;
    private String dia;
    private Integer diaNumero, mesNumero, añoNumero;
    private ArrayList<Reserva> reservasDia;
    private ArrayList<Integer>listaInicio;
    private ArrayList<Integer> listaFin;
    private int horaInicio;
    private int horaFin;
    private ImagenInstalacionAdapter imagenInstalacionAdapter;
    private int imagenPulsada;
    private ImageView imageView;
    private int i=0;
    FragmentInstalaciones fragmentInstalaciones;
    public TextView usuarioSeleccionado;
    private boolean reservarAMi;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        usuarioSeleccionado.setText(usuario.getNombre_usuario());
    }

    private Usuario usuario= new Usuario();
    public FragmentInstalacion() {
        // Required empty public constructor
    }


    public FragmentInstalacion(ActividadConUsuario actividadConUsuario,FragmentInstalaciones fragmentInstalaciones) {
        this.actividadConUsuario = actividadConUsuario;
        this.fragmentInstalaciones= fragmentInstalaciones;
    }

    public FragmentInstalacion(ActividadConUsuario actividadConUsuario,FragmentInstalaciones fragmentInstalaciones, boolean reservarAMi) {
        this.actividadConUsuario = actividadConUsuario;
        this.fragmentInstalaciones= fragmentInstalaciones;
        this.reservarAMi= reservarAMi;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista= inflater.inflate(R.layout.fragment_instalacion, container, false);
        reservasDia = new ArrayList<>();
        horaInicio=0;
        horaFin=0;
        usuarioSeleccionado = vista.findViewById(R.id.NombreUsuarioReserva);
        Resources res= getResources();
        if (i==0)
        cambiarUsuario(res.getString(R.string.SeleccionarUsuario));
        i=1;
        usuarioSeleccionado.setText(usuario.getNombre_usuario());
        if(reservarAMi){
            usuarioSeleccionado.setVisibility(View.GONE);
            usuario= actividadConUsuario.usuario;
        }
        usuarioSeleccionado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAFragmentUsuario();
            }
        });
        imageView = vista.findViewById(R.id.InstalacionImagen);
        try {
            Picasso.get().load(instalación.getImagenes().get(0))
                    .placeholder(R.drawable.icons8_squats_30)
                    .error(R.drawable.icons8_error_cloud_48)
                    .into(imageView);
        }catch (IllegalArgumentException exception){
            System.out.println(exception.getMessage());
        }
        Date fecha =new Date();
        String dia2= String.valueOf(fecha.getDate());
        diaNumero=fecha.getDate();
        if (fecha.getDate()<10){
            dia2="0"+dia2;
        }

        mesNumero=fecha.getMonth()+1;
        String mes= String.valueOf(mesNumero);
        if(fecha.getMonth()<10){
            mes="0"+mes;
        }
        dia = dia2+"-"+mes+"-"+String.valueOf(fecha.getYear()+1900);
        añoNumero=fecha.getYear()+1900;

        DiaEditText = vista.findViewById(R.id.DiaEditText);
        DiaEditText.setText(dia);
        DiaEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        reservasInicioDisponibles= vista.findViewById(R.id.reservasInicioDisponibles);
        listaInicio =instalación.getHorario();
        listaFin= new ArrayList<>();
        for( int i =0; i<listaInicio.size();i++){
            listaFin.add(listaInicio.get(i)+1);
        }
        obtenerReservasDia();
        reservasInicioDisponibles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                horaInicio = listaInicio.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        reservasFinDisponibles = vista.findViewById(R.id.reservasFinDisponibles);
        reservasFinDisponibles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                horaFin= listaFin.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        RecyclerView recyclerViewImagenes = vista.findViewById(R.id.recyclerViewImagenesInstalacion);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewImagenes.setLayoutManager(linearLayoutManager);
        imagenInstalacionAdapter = new ImagenInstalacionAdapter(getActivity());
        imagenInstalacionAdapter.anyadirALista(instalación.getImagenes());
        recyclerViewImagenes.setAdapter(imagenInstalacionAdapter);
        imagenInstalacionAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenPulsada= recyclerViewImagenes.getChildAdapterPosition(v);
                Picasso.get().load(imagenInstalacionAdapter.lista.get(imagenPulsada))
                        .placeholder(R.drawable.icons8_squats_30)
                        .error(R.drawable.icons8_error_cloud_48)
                        .into(imageView);
            }
        });
        //ActualizarListas();
        Button reservar = vista.findViewById(R.id.ReservarBoton);
        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiReservas apiReservas = actividadConUsuario.retrofit.create(apiReservas.class);
                int diferencia = horaFin - horaInicio;

                if (horaInicio>0 && horaFin>0&& diferencia > 0 && diferencia <= instalación.getTiempo_max_reserva()) {
                    int precio = diferencia*instalación.getPrecio_hora();
                    int id=0;
                    if (reservarAMi)
                        id=actividadConUsuario.usuario.getId();
                    else
                        if (usuario.getId()>0)
                        id=usuario.getId();
                        if (id >0){
                    Call<Reserva> respuesta = apiReservas.guardaReserva(id, instalación.getId(), instalación.getImagenes().get(0),instalación.getNombre(), diaNumero,mesNumero, añoNumero, horaInicio, horaFin, precio,false,false, false, true);
                    respuesta.enqueue(new Callback<Reserva>() {
                        @Override
                        public void onResponse(Call<Reserva> call, Response<Reserva> response) {
                            if (response.isSuccessful()) {
                                if (usuario.isEs_cliente())
                                    actualizarSaldoUsuario();
                                else {
                                    FragmentReservas fragmentReservas = new FragmentReservas(actividadConUsuario, true);
                                    actividadConUsuario.cambiarFragmento(fragmentReservas);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Reserva> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });
                        }
                        else {
                            Toast.makeText(getActivity(), "No se ha realizado la reserva",Toast.LENGTH_LONG).show();
                            actividadConUsuario.cambiarFragmento(fragmentInstalaciones);
                           // Toast.makeText(getActivity(),"nada",Toast.LENGTH_LONG).show();
                        }
                }
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                actividadConUsuario.cambiarFragmento(fragmentInstalaciones);
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
                Date fecha = new Date(year,month+1, day);
                Date fechaActual = new Date();
                if(fecha.compareTo(fechaActual)>0){
                    long diffInDays = ( (fecha.getTime() - fechaActual.getTime()));
                    double diferencia = Math.floor(diffInDays/(1000*3600*24));
                    diferencia= diferencia-693987;
                    diaNumero= day;
                    mesNumero =month+1;
                    añoNumero=year;
                    String mes = String.valueOf(mesNumero);
                    String dia2= String.valueOf(day);
                    if (diferencia<=14 && diferencia>0){
                        horaInicio=0;
                        horaFin=0;
                        if(month+1<10){
                            mes="0"+mes;
                        }
                        if (day<10){
                            dia2="0"+dia2;
                        }
                        dia = dia2 + "-" + mes + "-" + year;
                        DiaEditText.setText(dia);
                        listaInicio= new ArrayList<>();
                        listaInicio =instalación.getHorario();

                        listaFin= new ArrayList<>();
                        for( int i =0; i<listaInicio.size();i++){
                            listaFin.add(listaInicio.get(i)+1);
                        }
                        obtenerReservasDia();
                    }

                }

            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
    private void actualizarSaldoUsuario(){
        apiUsuarios apiUsuarios = actividadConUsuario.retrofit.create(apiUsuarios.class);
        int creditos= actividadConUsuario.usuario.getCreditos()-instalación.getPrecio_hora()*(horaFin-horaInicio);
        actividadConUsuario.usuario.setCreditos(creditos);
        Call<Usuario> respuesta = apiUsuarios.actualizarUsuario(actividadConUsuario.usuario.getId(),actividadConUsuario.usuario);
        respuesta.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                FragmentReservas fragmentReservas = new FragmentReservas(actividadConUsuario);
                actividadConUsuario.cambiarFragmento(fragmentReservas);
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }
    private void obtenerReservasDia(){
        apiReservas apiReservas= actividadConUsuario.retrofit.create(apiReservas.class);
        Call<ArrayList<Reserva>>respuesta = apiReservas.obtenerReservasDiaInstalacion(diaNumero,mesNumero,añoNumero, instalación.getId());
        respuesta.enqueue(new Callback<ArrayList<Reserva>>() {
            @Override
            public void onResponse(Call<ArrayList<Reserva>> call, Response<ArrayList<Reserva>> response) {
                reservasDia=response.body();
                if (response.body().size()>0)
                for (int i=0;i<response.body().size();i++){
                    int diferencia = response.body().get(i).getHora_fin()-response.body().get(i).getHora_inicio();
                    for(int j=0;j<diferencia;j++) {
                        int posicion = listaInicio.indexOf(response.body().get(i).getHora_inicio() + j);
                        if (posicion>=0) {
                            listaInicio.remove(posicion);
                            listaFin.remove(posicion);
                        }

                    }
                }
                ActualizarListas();
            }

            @Override
            public void onFailure(Call<ArrayList<Reserva>> call, Throwable t) {

            }
        });
    }

    private  void ActualizarListas()
    {
        if (listaInicio.size()>0) {
            horaInicio = listaInicio.get(0);
            ArrayAdapter<Integer> listaAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, listaInicio);
            reservasInicioDisponibles.setAdapter(listaAdapter);
            reservasFinDisponibles = vista.findViewById(R.id.reservasFinDisponibles);
            horaFin = listaFin.get(0);
            ArrayAdapter<Integer> listaAdapterFin = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, listaFin);
            reservasFinDisponibles.setAdapter(listaAdapterFin);
        }
        else
            Toast.makeText(getActivity(),"No hay horas disponibles",Toast.LENGTH_LONG).show();
    }

    private void irAFragmentUsuario(){
        FragmentUsuarios fragmentUsuarios = new FragmentUsuarios(actividadConUsuario, this);
        actividadConUsuario.cambiarFragmento(fragmentUsuarios);
    }

    public void cambiarUsuario(String nombre_usuario){
        usuario.setNombre_usuario(nombre_usuario);
    }
}