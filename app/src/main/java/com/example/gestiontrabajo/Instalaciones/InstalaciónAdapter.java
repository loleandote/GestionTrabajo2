package com.example.gestiontrabajo.Instalaciones;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiontrabajo.Datos.Instalación;
import com.example.gestiontrabajo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InstalaciónAdapter extends RecyclerView.Adapter<InstalaciónAdapter.MiViewHolder>{
    ArrayList<Instalación> lista;
    private Context context;
    private View.OnClickListener onClickListener;
    private String[]listaTipos;

    public InstalaciónAdapter(Context context, String[]listaTipos) {
        lista= new ArrayList<>();
        this.context = context;
        this.listaTipos=listaTipos;
    }

    @NonNull
    @Override
    public MiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){

        LayoutInflater inflater =LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.elementos_lista_instalacion,viewGroup,false);
        view.setOnClickListener(this.onClickListener);
        MiViewHolder miViewHolder= new MiViewHolder(view);



        return miViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MiViewHolder holder, int position) {
        try {
            if (lista.get(position).getImagenes() != null)
                Picasso.get().load(lista.get(position).getImagenes().get(0))
                        .placeholder(R.drawable.icons8_squats_30)
                        .error(R.drawable.icons8_error_cloud_48)
                        .into(holder.imagenView);
            //String texto = String.valueOf(lista.get(position).getId());
            //Integer nombre = lista.get(postion).getId_reserva();
            //holder.nombreTextView.setText(texto);

        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        holder.nombreTextView.setText(lista.get(position).getNombre());
        String tipo = listaTipos[lista.get(position).getTipo()];
        //String tipo = String.valueOf(lista.get(position).getTipo());
        holder.tipoTextView.setText(tipo);
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener)
    {
        this.onClickListener= onClickListener;
    }
    @Override
    public int getItemCount() {
        if(lista==null){
            return 0;
        }else
            return lista.size();
    }
    public void anyadirALista(ArrayList<Instalación> lista){
        this.lista=lista;
        notifyDataSetChanged(); // Actualizamos el recyclerView
    }


    public static class MiViewHolder extends  RecyclerView.ViewHolder{
        public TextView nombreTextView;
        public TextView tipoTextView;
        public ImageView imagenView;
        public MiViewHolder(View view){
            super(view);
            this.imagenView =itemView.findViewById(R.id.ImagenInstalacion);
            this.nombreTextView =itemView.findViewById(R.id.nombreInstalacionTextView);
            this.tipoTextView =itemView.findViewById(R.id.tipoInstalacionTextView);
        }
    }
}
