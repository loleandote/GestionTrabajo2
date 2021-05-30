package com.example.gestiontrabajo.Conexión;

import com.example.gestiontrabajo.Datos.Rol;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface apiRoles {
    @GET("roles")
    Call<ArrayList<Rol>> obtenerRoles();
    @GET("roles")
    Call<ArrayList<Rol>> obtenerRoles(@Query("rango_rol")List<Integer>lista);
    @GET("roles")
    Call<ArrayList<Rol>> obtenerRol(@Query("id")int id);
    @FormUrlEncoded
    @POST("roles")
    Call<Rol> guardaRol(
            @Field("nombre_rol") String nombreRol,
            @Field("rango_rol")int rango_rol,
            @Field("realiza_reservas") Boolean realiza_reservas,
            @Field("realiza_reservas_otros") Boolean realiza_reservas_otros,
            @Field("cancela_reserva") Boolean cancela_reserva,
            @Field("modificar_usuario") Boolean modificar_usuario,
            @Field("baja_socio") Boolean baja_socio,
            @Field("realiza_informe") Boolean realiza_informe,
            @Field("ver_grafico") Boolean ver_grafico,
            @Field("mod_usuario_otros") Boolean mod_usuario_otros,
            @Field("mod_permiso") Boolean mod_rol,
            @Field("exporta_importa") Boolean imp_exp
    );
    @PUT("roles/{id}")
    Call<Rol> actualizarRol(@Path("id") int id, @Body Rol rol);
}
