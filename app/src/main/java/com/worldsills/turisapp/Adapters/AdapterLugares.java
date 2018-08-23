package com.worldsills.turisapp.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.worldsills.turisapp.ItemLugar;

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

        return row;
    }

    class Holder {

    }
}
