package com.example.gestiontrabajo.Conexión;

import com.example.gestiontrabajo.Datos.Observación;

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

public interface apiObservaciones {
    @GET("observaciones")
    Call<ArrayList<Observación>> obtenerObservacionesUsuario(@Query("id_usuario")int id_usuario);
    @PUT("observaciones/{id}")
    Call<Observación> actualizarObservacion(@Path("id") int id, @Body Observación instalación);
    @FormUrlEncoded
    @POST("observaciones")
    Call<Observación> guardaObservacion(
            @Field("id_usuario") int id_usuario,
            @Field("titulo") String titulo,
            @Field("contenido") String contenido
    );
}
