package com.example.gestiontrabajo.Conexión;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Cliente {
    //private static final String URL = "http://192.168.1.133:3000";
    private  static final String URL ="http://192.168.1.141:3000";
    private static Retrofit retrofit = null;
    public static Retrofit obtenerCliente(){
        try {
            if (retrofit == null) {
                Retrofit.Builder builder = new Retrofit.Builder();
                builder.baseUrl(URL);
                builder.addConverterFactory(GsonConverterFactory.create());
                retrofit = builder
                        .build();
            }
        }catch (ExceptionInInitializerError ex ){

            System.out.println(ex.getMessage());
        }
        return retrofit;
    }
}
