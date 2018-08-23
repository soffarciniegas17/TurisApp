package com.worldsills.turisapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.worldsills.turisapp.ItemLugar;
import com.worldsills.turisapp.R;

import java.util.List;

public class AdapterLugares extends BaseAdapter {

    private Context context;
    private int layoutItem;
    private List<ItemLugar> lugares;

    public AdapterLugares(Context context, int layoutItem, List<ItemLugar> lugares) {
        this.context = context;
        this.layoutItem = layoutItem;
        this.lugares = lugares;
    }

    @Override
    public int getCount() {
        return lugares.size();
    }

    @Override
    public Object getItem(int position) {
        return lugares.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row=view;
        Holder holder=new Holder();

        if (row==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(layoutItem,null);

            holder.imagen=row.findViewById(R.id.item_lugar_imagen);
            holder.nombre=row.findViewById(R.id.item_lugar_nombre);
            holder.ubicacion=row.findViewById(R.id.item_lugar_direccion);
            holder.descripcionCorta=row.findViewById(R.id.item_lugar_descripcion_corta);

            row.setTag(holder);



        }else{
            holder=(Holder)row.getTag();

        }

        ItemLugar lugar=lugares.get(position);
        String url="";
        for (int i=0; i<lugar.getUrlImagen().length();i++){
            if (lugar.getUrlImagen().charAt(i)=='H'){
                url=lugar.getUrlImagen().substring(i,lugar.getUrlImagen().length());
                break;
            }
            if (lugar.getUrlImagen().charAt(i)=='S'){
                url=lugar.getUrlImagen().substring(i,lugar.getUrlImagen().length());
                break;
            }
            if (lugar.getUrlImagen().charAt(i)=='R'){
                url=lugar.getUrlImagen().substring(i,lugar.getUrlImagen().length());
                break;
            }
        }

        Glide.with(context).load(R.string.url_base_lugares+url).centerCrop()
                .error(R.drawable.logoturis).placeholder(R.color.negro_claro).into(holder.imagen);

        holder.ubicacion.setText(lugar.getUbicacion());
        holder.nombre.setText(lugar.getNombre());
        holder.descripcionCorta.setText(lugar.getDescripcionCorta());


        return row;
    }

    class Holder {

        ImageView imagen;
        TextView nombre, ubicacion, descripcionCorta;

    }
}
