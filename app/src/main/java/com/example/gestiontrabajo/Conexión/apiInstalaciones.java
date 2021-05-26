package com.example.gestiontrabajo.Conexión;



import com.example.gestiontrabajo.Datos.Instalación;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface apiInstalaciones {
    @GET("instalaciones")
    Call<ArrayList<Instalación>> obtenerInstalaciones();
    @GET("instalaciones")
    Call<ArrayList<Instalación>> obtenerInstalacionesPorTipo(@Query("tipo")int tipo);

    @PUT("instalaciones/{id}")
    Call<Instalación> actualizarInstalacion(@Path("id") int id, @Body Instalación instalación);
    @FormUrlEncoded
    @POST("instalaciones")
    Call<Instalación> guardaInstalacion(
            @Field("tipo") int tipo,
            @Field("nombre") String nombre,
            @Field("precio_hora") long precio_hora,
            @Field("tiempo_min_reserva") long tiempo_min_reserva,
            @Field("tiempo_max_reserva") long tiempo_max_reserva,
            @Field("imagenes")ArrayList<String>imagenes,
            @Field("horario") ArrayList<Integer> horario
    );
}
