package com.worldsills.turisapp.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.worldsills.turisapp.Actitivties.MainActivity;
import com.worldsills.turisapp.Interfaces.ConsumirServicios;
import com.worldsills.turisapp.ItemLugar;
import com.worldsills.turisapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContenidoLugar extends Fragment {

    private int itemPresionado;
    private String categoria;
    ImageView foto;
    TextView description, titulo;
    Activity thisActivity;

    public ContenidoLugar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_contenido_lugar, container, false);


        thisActivity= getActivity();
        foto= view.findViewById(R.id.foto);

        description=view.findViewById(R.id.description);
        titulo= view.findViewById(R.id.titulo);

        itemPresionado=0;
        categoria="sitios";

        if(getArguments()!=null){
            itemPresionado= getArguments().getInt("ITEM");
            categoria= getArguments().getString("CATEG");
        }


        consumirDatos();

        return view;

    }

    public void consumirDatos(){
        Retrofit retrofit= new Retrofit.Builder().baseUrl(getResources().getString(R.string.url_base_lugares))
                .addConverterFactory(GsonConverterFactory.create()).build();
        ConsumirServicios servicios= retrofit.create(ConsumirServicios.class);
        Call<List<ItemLugar>> res=servicios.getLugares(categoria);

        res.enqueue(new Callback<List<ItemLugar>>() {
            @Override
            public void onResponse(Call<List<ItemLugar>> call, Response<List<ItemLugar>> response) {
                organizarInfo(response.body());
            }

            @Override
            public void onFailure(Call<List<ItemLugar>> call, Throwable t) {

            }
        });


    }

    private void organizarInfo(List<ItemLugar> lugares){
        ItemLugar lugar = lugares.get(itemPresionado);

        String url= "";
        for (int i=0; i<lugar.getUrlImagen().length(); i++){
            if(lugar.getUrlImagen().charAt(i)=='H'){
                url=lugar.getUrlImagen().substring(i, lugar.getUrlImagen().length());
                break;
            }
            if(lugar.getUrlImagen().charAt(i)=='S'){
                url= lugar.getUrlImagen().substring(i, lugar.getUrlImagen().length());
                break;
            }

            if(lugar.getUrlImagen().charAt(i)=='R'){
                url= lugar.getUrlImagen().substring(i, lugar.getUrlImagen().length());
                break;
            }
        }
        if(thisActivity!=null && isAdded())
        Glide.with(thisActivity).load(getResources().getString(R.string.url_base_lugares)+url).centerCrop().error(R.drawable.logoturis)
                .placeholder(android.R.color.black)
                .into(foto);


        description.setText(lugar.getDescripcion());
        titulo.setText(lugar.getNombre());
    }

}
