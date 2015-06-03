package com.example.usuario.venderapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.usuario.venderapp.Activity_Lotes;
import com.example.usuario.venderapp.R;

import java.util.ArrayList;

/**
 * Created by USUARIO on 28-abr-15.
 */
public class ListaUrbAdapter extends ArrayAdapter<String[]> {
    Context context;
    public ListaUrbAdapter(Context context, ArrayList<String[]> List) {
        super(context, 0, List);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String[] urbanizacion=(String[]) getItem(position);
        final String id=urbanizacion[0];
        final String nombre_urb=urbanizacion[1];
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lista_urb_view,parent,false);
        }
        TextView nombre = (TextView) convertView.findViewById(R.id.urbText);
        nombre.setText(urbanizacion[1]);

        convertView.findViewById(R.id.urb).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Activity_Lotes.class);
                intent.putExtra("id_urb", id);
                intent.putExtra("nombre_urb",nombre_urb);
                getContext().startActivity(intent);
            }
        });

        return convertView;

    }

}
