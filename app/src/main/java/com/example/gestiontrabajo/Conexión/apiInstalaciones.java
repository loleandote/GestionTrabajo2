package com.example.gestiontrabajo.Conexión;



import com.example.gestiontrabajo.Datos.Instalación;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface apiInstalaciones {
    @GET("instalaciones")
    Call<ArrayList<Instalación>> obtenerInstalaciones();

}
