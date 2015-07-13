package com.example.usuario.venderapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.usuario.venderapp.Financiamiento;
import com.example.usuario.venderapp.R;

import java.util.ArrayList;

/**
 * Created by USUARIO on 24-jun-15.
 */
public class ListaFinanciamientoAdapter extends SWAdapter {
    Context context;
    ArrayList<String[]> list;
    public ListaFinanciamientoAdapter(Context context, ArrayList<String[]> list) {
        super(context, list);
        this.context=context;
        this.list=list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String[] dato=(String[]) getItem(position);
        final String[] datosModelo=new String[]{dato[1],dato[2],dato[3],dato[4]};
        /*Financiamiento financiamiento=new Financiamiento(dato.getString(0),dato.getString(1),dato.getString(2),
                dato.getString(3),dato.getString(4),dato.getString(5),dato.getString(6),dato.getString(7),dato.getString(8),
                dato.getString(9),dato.getString(10),dato.getString(11),dato.getString(12),dato.getString(13),
                dato.getString(14),dato.getString(15),dato.getString(16),dato.getString(17),dato.getString(18));*/
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.financiamiento_view,parent,false);
        }
        ((TextView) convertView.findViewById(R.id.modeloVw)).setText(dato[1]);
        ((TextView) convertView.findViewById(R.id.urbanizacionVw)).setText(dato[2]);
        ((TextView) convertView.findViewById(R.id.loteVw)).setText(dato[3]);
        ((TextView) convertView.findViewById(R.id.manzanaVw)).setText(dato[4]);
        ((TextView) convertView.findViewById(R.id.precioVw)).setText("$"+dato[5]);
        ((TextView) convertView.findViewById(R.id.cuotaEntradaVw)).setText("$"+dato[6]);
        ((TextView) convertView.findViewById(R.id.cuotaSaldoVw)).setText("$"+dato[7]);
        ((TextView) convertView.findViewById(R.id.clienteVw)).setText(dato[8]);
        ((TextView) convertView.findViewById(R.id.fechaVw)).setText(dato[9]);

        final View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Financiamiento financiamiento=new Financiamiento((Activity)context,dato[0],datosModelo, finalConvertView);
                financiamiento.mostrar();

            }
        });


        return convertView;

    }
}
