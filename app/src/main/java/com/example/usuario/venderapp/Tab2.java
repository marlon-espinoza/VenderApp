package com.example.usuario.venderapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.usuario.venderapp.adapters.UrbanizacionAdapter;

import java.util.ArrayList;

 /**
 * Created by USUARIO on 23-abr-15.
 */
public class Tab2 extends Fragment {
    ArrayList<Urbanizacion> list;
    GridView grid;
    UrbanizacionAdapter adp;

     @Override
     public void onSaveInstanceState(Bundle outState) {
         super.onSaveInstanceState(outState);
         
     }

     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

     @Override
     public void onAttach(Activity activity) {
         super.onAttach(activity);
     }

     @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.tab2, container, false);
        grid=(GridView) V.findViewById(R.id.GridLView1);
        list = new ArrayList<Urbanizacion>();
        Urbanizacion u1=new Urbanizacion(1,"arboleda");
        Urbanizacion u2=new Urbanizacion(2,"peninsula");
        //Urbanizacion u3=new Urbanizacion(3,"peninsula");
        list.add(u1);
        list.add(u2);


        //list.add(u3);

        adp=new UrbanizacionAdapter(getActivity(),list);
        grid.setAdapter(adp);
                //final ListView lv= (ListView) V.findViewById(R.id.list_urb);
        return V;
    }
}

