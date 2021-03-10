package com.example.gestiontrabajo.Reservas;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiReservas;
import com.example.gestiontrabajo.Datos.Reserva;
import com.example.gestiontrabajo.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentReserva extends Fragment{

    private ActividadConUsuario actividadConUsuario;
    private View vista;
    public Reserva reserva;
    private ImageView imagenReserva;
    public FragmentReserva() {
        // Required empty public constructor
    }

    public FragmentReserva(ActividadConUsuario actividadConUsuario) {
        this.actividadConUsuario= actividadConUsuario;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_reserva, container, false);


        imagenReserva = vista.findViewById(R.id.ReservaImagen);
        Picasso.get().load(reserva.getImagen_instalacion())
                .placeholder(R.drawable.icons8_squats_30)
                .error(R.drawable.icons8_error_cloud_48)
                //.resize(80,60)
                //.centerCrop()
                .into(imagenReserva);
        TextView fechaReserva = vista.findViewById(R.id.FechaResevaTextView);
        fechaReserva.setText(reserva.getDia());
        TextView fechaDesdeReserva = vista.findViewById(R.id.FechaDesdeTextView);
        fechaDesdeReserva.setText(String.valueOf(reserva.getHora_inicio())+":00");
        TextView fechaHastaReserva = vista.findViewById(R.id.FechaHastaTextView);
        fechaHastaReserva.setText(String.valueOf(reserva.getHora_fin())+":00");
        if (!reserva.isCancel_admin() && !reserva.isCancel_usu())
        {
            TextView canceladaReserva = vista.findViewById(R.id.CamceladaReservaTextView);
            canceladaReserva.setVisibility(View.GONE);
        }
        Button cancelarReserva = vista.findViewById(R.id.CancelarReserva);
        cancelarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserva.setCancel_usu(true);
                apiReservas apiReserva = actividadConUsuario.retrofit.create(apiReservas.class);
                Call<Reserva> respuesta = apiReserva.actualizarReserva(reserva.getId(),reserva);
                respuesta.enqueue(new Callback<Reserva>() {
                    @Override
                    public void onResponse(Call<Reserva> call, Response<Reserva> response) {
                        FragmentReservas fragmentReservas= new FragmentReservas(actividadConUsuario);
                        actividadConUsuario.cambiarFragmento(fragmentReservas);
                    }

                    @Override
                    public void onFailure(Call<Reserva> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                FragmentReservas fragmentReservas = new FragmentReservas(actividadConUsuario);
                actividadConUsuario.cambiarFragmento(fragmentReservas);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return vista;
    }

}