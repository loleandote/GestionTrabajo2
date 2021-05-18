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

public interface apiRol {
    @GET("roles")
    Call<ArrayList<Rol>> obtenerRoles();
    @GET("roles")
    Call<ArrayList<Rol>> obtenerRoles(@Query("rango_rol")List<Integer>lista);
    @GET("roles")
    Call<Rol> obtenerRol(@Query("id")int id);
    @FormUrlEncoded
    @POST("roles")
    Call<Rol> guardaRol(
            @Field("nombre_rol") String nombreRol,
            @Field("rango_rol")int rango_rol,
            @Field("realizar_reservas") Boolean realizar_reservas,
            @Field("cambiar_contraseña") Boolean cambiar_contraseña,
            @Field("mod_usu") Boolean mod_usu,
            @Field("mod_rol") Boolean mod_rol,
            @Field("imp_exp") Boolean imp_exp
    );
    @PUT("roles/{id}")
    Call<Rol> actualizarRol(@Path("id") int id, @Body Rol rol);
}
