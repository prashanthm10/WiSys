package com.example.prashanth.wisys;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Prashanth on 7/10/2017.
 */

public class SensorAdapter extends ArrayAdapter<SensorInfo> {


    public SensorAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<SensorInfo> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SensorInfo sensorInfo=getItem(position);
        if(convertView==null) convertView=LayoutInflater.from(getContext()).inflate(R.layout.item_sensor,parent,false);
        TextView sl=(TextView)convertView.findViewById(R.id.s_no_tv);
        TextView stype=(TextView)convertView.findViewById(R.id.sensor_type_tv);
        sl.setText(String.valueOf(position+1));
        stype.setText(sensorInfo.getStype()+"_"+sensorInfo.getSid()+"_"+sensorInfo.getNid());


        switch(sensorInfo.getStype()){
            case "SENSOR_PWM":
                switch (sensorInfo.getVal()){
                    case 0:
                        stype.setCompoundDrawablesWithIntrinsicBounds(null,null,getContext().getResources().getDrawable(R.drawable.ic_bulb_filled_grey),null);
                        break;
                    case 100:
                        stype.setCompoundDrawablesWithIntrinsicBounds(null,null,getContext().getResources().getDrawable(R.drawable.ic_bulb_filled_green),null);
                        break;
                    case 101:
                        stype.setCompoundDrawablesWithIntrinsicBounds(null,null,getContext().getResources().getDrawable(R.drawable.ic_bulb_filled_red),null);
                        break;
                    default:
                        stype.setCompoundDrawablesWithIntrinsicBounds(null,null,getContext().getResources().getDrawable(R.drawable.ic_bulb_filled_yellow),null);
                        break;
                }
                break;
            case "SENSOR_TEMPERATURE":
                stype.setCompoundDrawablesWithIntrinsicBounds(null,null,getContext().getResources().getDrawable(R.drawable.ic_thermometer),null);
                break;
            case "SENSOR_BATTERY":
                stype.setCompoundDrawablesWithIntrinsicBounds(null,null,getContext().getResources().getDrawable(R.drawable.ic_battery_80_black_24dp),null);
                break;
            case "SENSOR_HUMIDITY":
                stype.setCompoundDrawablesWithIntrinsicBounds(null,null,getContext().getResources().getDrawable(R.drawable.ic_water_drop),null);
                break;
            case "SENSOR_PDS":
                switch (sensorInfo.getVal()){
                    case 0:
                        stype.setCompoundDrawablesWithIntrinsicBounds(null,null,getContext().getResources().getDrawable(R.drawable.ic_people_outline_black_24dp),null);
                        break;
                    case 1:
                        stype.setCompoundDrawablesWithIntrinsicBounds(null,null,getContext().getResources().getDrawable(R.drawable.ic_people_black_24dp),null);
                        break;
                }

                break;

        }


        return convertView;

    }

}
