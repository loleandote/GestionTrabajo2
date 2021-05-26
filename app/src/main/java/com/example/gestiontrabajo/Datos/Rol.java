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
    @SerializedName("realiza_reservas")
    @Expose
    private boolean realiza_reservas;
    @SerializedName("realiza_reservas_otros")
    @Expose
    private boolean realiza_reservas_otros;
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

    public Rol(int id, int rango_rol, String nombre_rol, boolean realiza_reservas, boolean realiza_reservas_otros, boolean cambiar_contraseña, boolean mod_usu, boolean mod_rol, boolean imp_exp) {
        this.id = id;
        this.rango_rol = rango_rol;
        this.nombre_rol = nombre_rol;
        this.realiza_reservas = realiza_reservas;
        this.realiza_reservas_otros = realiza_reservas_otros;
        this.cambiar_contraseña = cambiar_contraseña;
        this.mod_usu = mod_usu;
        this.mod_rol = mod_rol;
        this.imp_exp = imp_exp;
    }

    public boolean isRealiza_reservas() {
        return realiza_reservas;
    }

    public void setRealiza_reservas(boolean realiza_reservas) {
        this.realiza_reservas = realiza_reservas;
    }

    public boolean isRealiza_reservas_otros() {
        return realiza_reservas_otros;
    }

    public void setRealiza_reservas_otros(boolean realiza_reservas_otros) {
        this.realiza_reservas_otros = realiza_reservas_otros;
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
