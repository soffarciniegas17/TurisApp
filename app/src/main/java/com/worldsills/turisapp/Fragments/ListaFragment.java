package com.worldsills.turisapp.Fragments;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.worldsills.turisapp.Adapters.AdapterLugares;
import com.worldsills.turisapp.Interfaces.ComunicaFragment;
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
public class ListaFragment extends Fragment {

    private GridView gridView;
    private AdapterLugares adapter;
    private boolean vista;
    private String tipoLugar;
    private Activity activity;


    public ListaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vistaFragment=inflater.inflate(R.layout.fragment_lista, container, false);

        activity=getActivity();
        gridView=vistaFragment.findViewById(R.id.gridview);
        vista=false;

        if (getArguments()!=null){
            vista=getArguments().getBoolean("VISTA");
            tipoLugar=getArguments().getString("CATEG");
        }

        consumeServicios();

        return vistaFragment;
    }

    public void consumeServicios(){

        Retrofit retrofit=new Retrofit.Builder().baseUrl(getResources().getString(R.string.url_base_lugares))
                .addConverterFactory(GsonConverterFactory.create()).build();

        ConsumirServicios servicio=retrofit.create(ConsumirServicios.class);

        Call<List<ItemLugar>> res=servicio.getLugares(tipoLugar);

        res.enqueue(new Callback<List<ItemLugar>>() {
            @Override
            public void onResponse(Call<List<ItemLugar>> call, Response<List<ItemLugar>> response) {
                cardaListaLugares(response.body());
            }

            @Override
            public void onFailure(Call<List<ItemLugar>> call, Throwable t) {

            }
        });

    }
    public void cardaListaLugares(List<ItemLugar> lugares){

        try{
            if (activity.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
                adapter=new AdapterLugares(activity,R.layout.item_lugar_list_land,lugares);
                gridView.setNumColumns(1);
            }else if(vista){
                adapter=new AdapterLugares(activity,R.layout.item_lugar_list,lugares);
                gridView.setNumColumns(1);
            }else{
                adapter=new AdapterLugares(activity,R.layout.item_lugar_grid,lugares);
                gridView.setNumColumns(2);

            }
            gridView.setAdapter(adapter);

        }catch (Exception e){}
        adapter.notifyDataSetChanged();
        clickItems();
    }

    public void clickItems(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((ComunicaFragment)getActivity()).itemPresionado(i);
            }
        });
    }

}
