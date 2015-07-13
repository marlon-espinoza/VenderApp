package com.example.usuario.venderapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.usuario.venderapp.DataBase.DbFinanciamiento;
import com.example.usuario.venderapp.adapters.ListaFinanciamientoAdapter;
import com.example.usuario.venderapp.adapters.SWEffects;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.ArrayList;

 /**
 * Created by USUARIO on 23-abr-15.
 */
public class TabFinanciamientos extends Fragment {
     ArrayList<String[]> financiamientos;


     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
     }

     @Override
     public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



         View V = inflater.inflate(R.layout.tab2, container, false);


         ListaFinanciamientoAdapter listaAdapter;
         final DynamicListView dynamicList=(DynamicListView)V.findViewById(R.id.listFinanciamientos);

         financiamientos = new ArrayList<>();
         DbFinanciamiento dbFinanciamiento=null;

         try {
             System.out.println("Extrayendo financiamientos...");
             dbFinanciamiento = new DbFinanciamiento(this.getActivity());
             Cursor dato = dbFinanciamiento.consultar(null);
             if (dato.moveToFirst()) {
                 //Recorremos el cursor hasta que no haya más registros
                 do {
                     /*ID_FINANCIAMIENTO,ID_MODELO,NOMBRE_MODELO,URBANIZACION,LOTE,MANZANA,PRECIO,ENTRADA,
                PORCENTAJE_ENTRADA,CUOTA_INICIAL,PORCENTAJE_CUOTA_INICIAL,NUM_PAGOS_ENTRADA,CUOTA_ENTRADA,SALDO,TASA_INTERES,
                NUM_PAGOS_SALDO,CUOTA_SALDO,CLIENTE,FECHA*/
                     System.out.println(dato.getString(0)+" "+dato.getString(1)+" "+dato.getString(2)+" "+
                             dato.getString(3)+" "+dato.getString(4)+" "+dato.getString(5)+" "+dato.getString(6)+" "+dato.getString(7)+" "+dato.getString(8)+" "+
                             dato.getString(9)+" "+dato.getString(10)+" "+dato.getString(11)+" "+dato.getString(12)+" "+dato.getString(13)+" "+
                             dato.getString(14)+" "+dato.getString(15)+" "+dato.getString(16)+" "+dato.getString(17)+" "+dato.getString(18));

                     financiamientos.add(new String[]{dato.getString(0),dato.getString(2),dato.getString(3),dato.getString(4),
                             dato.getString(5),dato.getString(6),dato.getString(12),dato.getString(16),dato.getString(17),dato.getString(18)});


                 } while(dato.moveToNext());
             }

         }catch (Exception e){
             System.out.println(e.toString());
         }finally {
             if(dbFinanciamiento!=null)
                 dbFinanciamiento.close();
         }


         listaAdapter =new ListaFinanciamientoAdapter(getActivity(), financiamientos);

         dynamicList.setAdapter(listaAdapter);

         //agrego el efecto deseado
         SWEffects.SwipeUndoAnimacion(getActivity(), dynamicList, listaAdapter, true, 0, 0, 0, 1);//el cuarto parametro es booleano y es para indicar si se desea auto desaparicion del undo
         //SWEffects.animation(ll,dataAdapter,0,-1,1,0);//4 enteros del final son los efectos
         //primero 1 efecto bottom
         //segundo 1 para left -1 para right
         //tercero 1 para efecto alpha
         //cuarto 1 para efecto scale
         //SWEffects.Swipe(ll,dataAdapter);


         return V;
     }
}

