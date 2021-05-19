package com.example.gestiontrabajo.Instalaciones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiontrabajo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImagenInstalacionAdapter extends RecyclerView.Adapter<ImagenInstalacionAdapter.MiViewHolder>{
    ArrayList<String> lista;
    private Context context;
    private View.OnClickListener onClickListener;

    public ImagenInstalacionAdapter(Context context) {
        this.context = context;
        lista = new ArrayList<>();
    }
    @NonNull
    @Override
    public ImagenInstalacionAdapter.MiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){

        LayoutInflater inflater =LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.elementos_lista_imagenes,viewGroup,false);
        System.out.println("Hola");
        view.setOnClickListener(this.onClickListener);
        ImagenInstalacionAdapter.MiViewHolder miViewHolder= new ImagenInstalacionAdapter.MiViewHolder(view);
        return miViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ImagenInstalacionAdapter.MiViewHolder holder, int position) {
        try {
            if (lista != null)
                Picasso.get().load(lista.get(position))
                        .placeholder(R.drawable.icons8_squats_30)
                        .error(R.drawable.icons8_error_cloud_48)
                        .into(holder.imagenView);
        }catch (IllegalArgumentException exception){
            System.out.println(exception.getMessage());
        }
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
    public void anyadirALista(ArrayList<String> lista){
        this.lista.addAll(lista);
        notifyDataSetChanged(); // Actualizamos el recyclerView
    }
    public static class MiViewHolder extends  RecyclerView.ViewHolder{

        public ImageView imagenView;
        public MiViewHolder(View view){
            super(view);
            this.imagenView =itemView.findViewById(R.id.ImagenViewInstalacion);
        }
    }
}
