package com.example.usuario.venderapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.usuario.venderapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USUARIO on 14-may-15.
 */
public class ListaLoteAdapter extends ArrayAdapter<String[]> {
    Context context;
    public ListaLoteAdapter(Context context, ArrayList<String[]> List) {
        super(context,0, List);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String[] lote=(String[]) getItem(position);
        final String id=lote[0];
        System.out.println(id);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lista_lote_view,parent,false);
        }
        TextView nombre = (TextView) convertView.findViewById(R.id.loteText);
        nombre.setText(lote[1]);

        /*convertView.findViewById(R.id.lote).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListaLoteAdapter.class);
                intent.putExtra("id_urb", id);
                getContext().startActivity(intent);
            }
        });*/

        return convertView;
    }
}
