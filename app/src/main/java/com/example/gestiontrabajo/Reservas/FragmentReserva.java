package com.example.gestiontrabajo.Reservas;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

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
    private FragmentReservas fragmentReservas;
    private LayoutInflater layoutInflater;
    public FragmentReserva() {
        // Required empty public constructor
    }

    public FragmentReserva(ActividadConUsuario actividadConUsuario) {
        this.actividadConUsuario= actividadConUsuario;
    }

    public FragmentReserva(ActividadConUsuario actividadConUsuario, FragmentReservas fragmentReservas){
        this.actividadConUsuario= actividadConUsuario;
        this.fragmentReservas=fragmentReservas;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista= inflater.inflate(R.layout.fragment_reserva, container, false);
        layoutInflater= inflater;
        imagenReserva = vista.findViewById(R.id.ReservaImagen);
        Picasso.get().load(reserva.getImagen_instalacion())
                .placeholder(R.drawable.icons8_squats_30)
                .error(R.drawable.icons8_error_cloud_48)
                .into(imagenReserva);
        TextView fechaReserva = vista.findViewById(R.id.FechaResevaTextView);
        fechaReserva.setText(String.valueOf(reserva.getDia())+"-"+reserva.getMes()+"-"+reserva.getAnyo());
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
                        actividadConUsuario.mensajeCorrecto(vista,layoutInflater,R.string.ReservaCancelada);
                        FragmentReservas fragmentReservas= new FragmentReservas(actividadConUsuario);
                        actividadConUsuario.cambiarFragmento(fragmentReservas, actividadConUsuario.getResources().getString(R.string.Reservas));
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
                if (actividadConUsuario.drawerLayout.isDrawerOpen(Gravity.LEFT))
                    actividadConUsuario.drawerLayout.closeDrawers();
                {
                    if (fragmentReservas == null)
                        fragmentReservas = new FragmentReservas(actividadConUsuario);
                    actividadConUsuario.cambiarFragmento(fragmentReservas, actividadConUsuario.getResources().getString(R.string.Reservas));
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return vista;
    }

}