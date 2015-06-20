package com.example.usuario.venderapp;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.usuario.venderapp.DataBase.DbProyecto;
import com.example.usuario.venderapp.adapters.ListaUrbAdapter;

import java.util.ArrayList;

 /**
 * Created by USUARIO on 23-abr-15.
 */
public class Tab2 extends Fragment {
     ArrayList<String[]> list;
     ListView lv;

     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
     }

     @Override
     public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



         View V = inflater.inflate(R.layout.tab2, container, false);
         lv=(ListView)V.findViewById(R.id.listFinanciamientos);


         ListaUrbAdapter listaUrbAdapter;
         list=new ArrayList<String[]>();
         DbProyecto dbproy=null;

         try {
             System.out.println("Extrayendo proyectos...");
             dbproy = new DbProyecto(this.getActivity());
             Cursor dato = dbproy.consultar(null);
             if (dato.moveToFirst()) {
                 //Recorremos el cursor hasta que no haya más registros
                 do {
                     list.add(new String[]{dato.getString(0),dato.getString(1)});

                 } while(dato.moveToNext());
             }

         }catch (Exception e){
             System.out.println(e.toString());
         }finally {
             if(dbproy!=null)
                 dbproy.close();
         }


         listaUrbAdapter =new ListaUrbAdapter(getActivity(),list);
         lv.setAdapter(listaUrbAdapter);

         return V;
     }
}

