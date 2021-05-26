package com.example.gestiontrabajo.Reservas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiontrabajo.Datos.Reserva;
import com.example.gestiontrabajo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.MiViewHolder>{
    ArrayList<Reserva> lista;
    private Context context;
    private View.OnClickListener onClickListener;
    public ReservaAdapter(Context context){
        this.context= context;
        lista= new ArrayList<>();
    }
    public void setOnItemClickListener(View.OnClickListener onClickListener){
        this.onClickListener =onClickListener;
    }
    @NonNull
    @Override
    public ReservaAdapter.MiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        LayoutInflater inflater =LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.elementos_lista_reserva,viewGroup,false);
        view.setOnClickListener(this.onClickListener);
        MiViewHolder miViewHolder= new MiViewHolder(view);
        return miViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ReservaAdapter.MiViewHolder holder, int postion){
        Reserva reserva =lista.get(postion);
        try {
            Picasso.get().load(reserva.getImagen_instalacion())
                    .placeholder(R.drawable.icons8_squats_30)
                    .error(R.drawable.icons8_error_cloud_48)
                    //.resize(80,60)
                    //.centerCrop()
                    .into(holder.imagenView);
        }catch (IllegalArgumentException exception){
            System.out.println(exception.getMessage());
        }
        String texto = String.valueOf(reserva.getNombre_instalacion());
        //Integer nombre = lista.get(postion).getId_reserva();
        holder.nombreTextView.setText(texto);
        String descripcion = String.valueOf(reserva.getDia())+"/"+String.valueOf(reserva.getMes())+"/"+String.valueOf(reserva.getAnyo());
        holder.descripcionTextView.setText(descripcion);
        if (!reserva.isCancel_usu() && !reserva.isCancel_admin()) {
            holder.canceladoTextView.setVisibility(View.GONE);
        }

    }



    public void anyadirALista(ArrayList<Reserva> lista){
        this.lista=lista;
        notifyDataSetChanged(); // Actualizamos el recyclerView
    }

    @Override
    public int getItemCount() {
        if(lista==null){
            return 0;
        }else
            return lista.size();
    }

    public static class MiViewHolder extends  RecyclerView.ViewHolder{
        public TextView nombreTextView;
        public TextView descripcionTextView;
        public TextView canceladoTextView;
        public ImageView imagenView;
        public MiViewHolder(View view){
            super(view);
            this.imagenView =itemView.findViewById(R.id.ImagenReserva);
            this.nombreTextView =itemView.findViewById(R.id.nombreReservaTextView);
            this.descripcionTextView =itemView.findViewById(R.id.descripcionReservaTextView);
            this.canceladoTextView = itemView.findViewById(R.id.cancelado_lista);
        }
    }}
