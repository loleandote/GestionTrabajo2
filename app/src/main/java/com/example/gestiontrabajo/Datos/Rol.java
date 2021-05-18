package com.example.gestiontrabajo.Datos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rol {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("rango_rol")
    @Expose
    private int rango_rol;
    @SerializedName("nombre_rol")
    @Expose
    private String nombre_rol;
    @SerializedName("realizar_reservas")
    @Expose
    private boolean realizar_reservas;
    @SerializedName("cambiar_contraseña")
    @Expose
    private boolean cambiar_contraseña;
    @SerializedName("mod_usu")
    @Expose
    private boolean mod_usu;
    @SerializedName("mod_rol")
    @Expose
    private boolean mod_rol;
    @SerializedName("imp_exp")
    @Expose
    private boolean imp_exp;


    public Rol() {
    }

    public Rol(int id, int rango_rol, String nombre_rol, boolean realizar_reservas, boolean cambiar_contraseña, boolean mod_usu, boolean mod_rol, boolean imp_exp) {
        this.id = id;
        this.rango_rol = rango_rol;
        this.nombre_rol = nombre_rol;
        this.realizar_reservas = realizar_reservas;
        this.cambiar_contraseña = cambiar_contraseña;
        this.mod_usu = mod_usu;
        this.mod_rol = mod_rol;
        this.imp_exp = imp_exp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_rol() {
        return nombre_rol;
    }

    public void setNombre_rol(String nombre_rol) {
        this.nombre_rol = nombre_rol;
    }

    public boolean isRealizar_reservas() {
        return realizar_reservas;
    }

    public void setRealizar_reservas(boolean realizar_reservas) {
        this.realizar_reservas = realizar_reservas;
    }

    public boolean isCambiar_contraseña() {
        return cambiar_contraseña;
    }

    public void setCambiar_contraseña(boolean cambiar_contraseña) {
        this.cambiar_contraseña = cambiar_contraseña;
    }

    public boolean isMod_usu() {
        return mod_usu;
    }

    public void setMod_usu(boolean mod_usu) {
        this.mod_usu = mod_usu;
    }

    public boolean isMod_rol() {
        return mod_rol;
    }

    public void setMod_rol(boolean mod_rol) {
        this.mod_rol = mod_rol;
    }

    public boolean isImp_exp() {
        return imp_exp;
    }

    public void setImp_exp(boolean imp_exp) {
        this.imp_exp = imp_exp;
    }

    public int getRango_rol() {
        return rango_rol;
    }

    public void setRango_rol(int rango_rol) {
        this.rango_rol = rango_rol;
    }
}
