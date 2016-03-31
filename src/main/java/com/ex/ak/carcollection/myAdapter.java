package com.ex.ak.carcollection;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class myAdapter extends ArrayAdapter implements Adapter
{
    EditText    etNameCar;
    EditText    etModelCar;
    EditText    etYearCar;
    ImageView   ivFotoCar;

    public myAdapter(Context context, int resource, int textViewResourceId, List objects)
    {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View v = super.getView(position, convertView, parent);
        Car  c = (Car)MainActivity.carsAL.get(position);

        this.etNameCar      = (EditText)v.findViewById(R.id.etName);
        this.etNameCar.setText(c.getName());
        this.etModelCar     = (EditText)v.findViewById(R.id.etModel);
        this.etModelCar.setText(c.getModel());
        this.etYearCar      = (EditText)v.findViewById(R.id.etYear);
        this.etYearCar.setText(String.valueOf(c.getYear()));
        this.ivFotoCar      = (ImageView) v.findViewById(R.id.CarImg);
        this.ivFotoCar.setImageBitmap(MainActivity.setImage(c.getFoto()));


        if (MainActivity.allViews.contains(v) == false)
        {
            MainActivity.allViews.add(v);
        }
        if (position != MainActivity.currentCar)
        {
            v.setBackgroundColor(Color.CYAN);
            //v.setBackgroundColor(R.color.colorBackground);
        }
        else
        {
            v.setBackgroundColor(Color.GREEN);
            //v.setBackgroundColor(R.color.colorSelectedItem);
        }
        return v;
    }
}
