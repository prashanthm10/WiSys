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
 * Created by Prashanth on 7/12/2017.
 */

public class GatewayAdapter extends ArrayAdapter<Gateway>{


    public GatewayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Gateway> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.item_gateway,parent,false);
        }
        TextView id=(TextView)convertView.findViewById(R.id.gateway_id_tv);
        TextView addr=(TextView)convertView.findViewById(R.id.gateway_addr_tv);
        Gateway gateway=getItem(position);
        id.setText(String.valueOf(gateway.getId()));
        addr.setText(gateway.getGid());
        return convertView;
    }

}
