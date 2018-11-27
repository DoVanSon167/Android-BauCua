package com.example.doson.gamebaucua;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import static android.view.View.inflate;


/**
 * Created by DoSon on 5/1/2018.
 */

public class CustomGridviewBanCo extends ArrayAdapter<Integer> {
    private Context context;
    int resource;
    private Integer[] objects;
    private Integer[] giatien = {0,10,20,30,50,100};
    private ArrayAdapter<Integer> adapter;
    CustomGridviewBanCo(@NonNull Context context, int resource, @NonNull Integer[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        adapter = new ArrayAdapter<Integer>(context,android.R.layout.simple_spinner_item,giatien);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflate(context,resource,null);
        ImageView imgBanCo = (ImageView) view.findViewById(R.id.imgBanCo);
        final Spinner spinGiaTien =(Spinner) view.findViewById(R.id.spinGiaTien);

        imgBanCo.setImageResource(objects[position]);
        spinGiaTien.setAdapter(adapter);

        spinGiaTien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positionspin, long id) {
                MainActivity.gtDatCuoc[position] = giatien[positionspin];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
}
