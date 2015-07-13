package com.example.usuario.venderapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.usuario.venderapp.DataBase.DbProyecto;
import com.example.usuario.venderapp.adapters.ListaUrbAdapter;

import java.util.ArrayList;

/**
 * Created by USUARIO on 23-abr-15.
 */
public class TabUrbanizaciones extends Fragment {
    ArrayList<String[]> list;

    ListView lv;
    Button btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



            View V = inflater.inflate(R.layout.tab_urbanizaciones, container, false);
            lv=(ListView)V.findViewById(R.id.listView);
            //btn= (Button) V.findViewById(R.id.boton);
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                TabLotes frLotes = new TabLotes();
                fragmentTransaction2.addToBackStack("xyz");
                fragmentTransaction2.hide(TabUrbanizaciones.this);
                fragmentTransaction2.add(android.R.id.tabcontent, frLotes);
                fragmentTransaction2.commit();
                /*Fragment fragment=new Fragment();
                Bundle params=new Bundle();
                params.putString("key","va");
                fragment.setArguments(params);
                final FragmentTransaction ft=getFragmentManager().beginTransaction();
                ft.replace(android.R.id.tabcontent,fragment,"Lotes");
                ft.addToBackStack("Lotes");
                ft.commit();
                getChildFragmentManager().findFragmentById(1);

            }
        });*/

        ListaUrbAdapter listaUrbAdapter;
            list=new ArrayList<String[]>();
            DbProyecto dbproy=null;
        try {
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
