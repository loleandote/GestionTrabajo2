package com.example.gestiontrabajo.Usuarios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.Instalaciones.InstalaciónAdapter;
import com.example.gestiontrabajo.R;

import java.util.ArrayList;
import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.MiViewHolder> {

    ArrayList<Usuario> lista;
    private Context context;
    private View.OnClickListener onClickListener;
    public UsuarioAdapter(Context context) {
        lista= new ArrayList<>();
        this.context = context;
    }
    @NonNull
    @Override
    public MiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.elementos_lista_usuario,parent,false);
        view.setOnClickListener(this.onClickListener);
        UsuarioAdapter.MiViewHolder miViewHolder= new UsuarioAdapter.MiViewHolder(view);
        return miViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MiViewHolder holder, int position) {
        System.out.println(position);
        holder.nombreTextView.setText(lista.get(position).getNombre_usuario());
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

    public void actualizarListaUsuarios(ArrayList<Usuario>lista){
        this.lista = lista;
        notifyDataSetChanged();
    }

    public static class MiViewHolder extends  RecyclerView.ViewHolder{
        public TextView nombreTextView;
        public MiViewHolder(View view){
            super(view);
            this.nombreTextView =itemView.findViewById(R.id.nombreUsuarioTextView);
        }
    }
}
