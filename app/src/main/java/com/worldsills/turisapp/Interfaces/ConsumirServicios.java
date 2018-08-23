package com.worldsills.turisapp.Interfaces;

import com.worldsills.turisapp.ItemLugar;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ConsumirServicios {

    @GET("webservice.php/")
    Call<List<ItemLugar>> getLugares(@Query("tipo") String tipoLugar);

    @GET("json/")
    Call<ResponseBody> getRuta(@Query("origin") String origen, @Query("destination") String destino);
}
