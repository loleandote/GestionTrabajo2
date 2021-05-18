package com.example.gestiontrabajo.Roles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiontrabajo.Datos.Rol;
import com.example.gestiontrabajo.R;

import java.util.ArrayList;

public class RolAdapter extends RecyclerView.Adapter<RolAdapter.MiViewHolder>{
    private Context context;
    private View.OnClickListener onClickListener;
    private ArrayList<Rol>lista;
    public RolAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener){
        this.onClickListener =onClickListener;
    }

    @NonNull
    @Override
    public MiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.elementos_lista_rol,parent,false);
        view.setOnClickListener(this.onClickListener);
        MiViewHolder miViewHolder= new MiViewHolder(view);
        return miViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MiViewHolder holder, int position) {
        Rol rol = lista.get(position);
        holder.nombreTextView.setText(rol.getNombre_rol());
    }

    public void anyadirALista(ArrayList<Rol> lista){
        this.lista=lista;
        notifyDataSetChanged(); // Actualizamos el recyclerView
    }

    public ArrayList<Rol> getLista() {
        return lista;
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
    public MiViewHolder(View view){
        super(view);
        this.nombreTextView =itemView.findViewById(R.id.NombreRol);
    }
}}
