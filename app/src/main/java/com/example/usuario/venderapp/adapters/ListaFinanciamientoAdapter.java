package com.example.usuario.venderapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        /*0 ID_FINANCIAMIENTO,1 ID_MODELO,2 NOMBRE_MODELO,3 URBANIZACION,4 LOTE,5 MANZANA,6 PRECIO,7 ENTRADA,
                8 PORCENTAJE_ENTRADA,9 CUOTA_INICIAL,10 PORCENTAJE_CUOTA_INICIAL,11 NUM_PAGOS_ENTRADA,12 CUOTA_ENTRADA,
                13 SALDO,TASA_INTERES,14 NUM_PAGOS_SALDO,15 CUOTA_SALDO,16 VENDER_COMO,17 CLIENTE,18 FECHA*/
        final String[] datosModelo=new String[]{dato[1],dato[2],dato[3],dato[4]};

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.financiamiento_view_modelo,parent,false);
        }
        if(dato[8].equals("1")){
            (convertView.findViewById(R.id.titulo)).setBackgroundColor(getContext().getResources().getColor(R.color.azul3));
        }
        ((TextView) convertView.findViewById(R.id.modeloVw)).setText(dato[1]);
        ((TextView) convertView.findViewById(R.id.urbanizacionVw)).setText(dato[2]);
        ((TextView) convertView.findViewById(R.id.loteVw)).setText(dato[3]);
        ((TextView) convertView.findViewById(R.id.manzanaVw)).setText(dato[4]);
        ((TextView) convertView.findViewById(R.id.precioVw)).setText("$"+dato[5]);
        ((TextView) convertView.findViewById(R.id.cuotaEntradaVw)).setText("$"+dato[6]);
        ((TextView) convertView.findViewById(R.id.cuotaSaldoVw)).setText("$"+dato[7]);
        ((TextView) convertView.findViewById(R.id.clienteVw)).setText(dato[9]);
        ((TextView) convertView.findViewById(R.id.fechaVw)).setText(dato[10]);

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
