package com.example.usuario.venderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.usuario.venderapp.DataBase.DbModelo;
import com.example.usuario.venderapp.FrameLayout.Modelo;
import com.example.usuario.venderapp.adapters.ImagenModeloAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class Activity_Modelos extends ActionBarActivity {
    ArrayList<String[]> list;
    private String idLote;
    String path=null;
    String IMAGEN_1="fachada";
    String IMAGEN_2="planta1";
    String IMAGEN_3="planta2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modelos);
        Intent intent = getIntent();
        idLote = intent.getStringExtra("id_lote");
        TableLayout tb = (TableLayout) findViewById(R.id.tablaModelos);
        list = new ArrayList<String[]>();
        DbModelo dbModelo = null;
        try {
            dbModelo = new DbModelo(this);
            final Cursor dato = dbModelo.consultar(idLote);
            DecimalFormat decimales = new DecimalFormat("0.00");
            double valor;

            if (dato.moveToFirst()) {
                findViewById(android.R.id.empty).setVisibility(View.GONE);
                int i=0;
                //Recorremos el cursor hasta que no haya más registros
                do {
                    //String[] campos = new String[] {ID_MODELO,MODELO,AREA_CONST,PISOS,CUOTA_ENTRADA,CUOTA_INICIAL,TASA,PLAZO_1,
                    //PLAZO_2,PLAZO_3,PRECIO,IMAGEN_FACHADA,IMAGEN_PLANTA_BAJA,IMAGEN_PLANTA_ALTA_1,IMAGEN_PLANTA_ALTA_2};
                    final TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.table_row_modelo, null);
                    TextView tv;
                    Button btn;
                    final String[] modelo=new String[]{dato.getString(0),dato.getString(1),dato.getString(2),dato.getString(3),dato.getString(4),
                            dato.getString(5),dato.getString(6),dato.getString(7),dato.getString(8),dato.getString(9),dato.getString(10),
                            dato.getString(11),dato.getString(12),dato.getString(13),dato.getString(14)};

                    final String id_modelo=dato.getString(0);
                    final String[] nImagenes={dato.getString(11),dato.getString(12),dato.getString(13)};
                    final String nombre_modelo=dato.getString(1);
                    if(i%2==1)tr.setBackgroundResource(R.drawable.selector_lista_con_azul);
                    tv = (TextView) tr.findViewById(R.id.modelo);
                    tv.setText(dato.getString(1));
                    tv = (TextView) tr.findViewById(R.id.areaconst);
                    valor=Double.parseDouble(dato.getString(2));
                    tv.setText(decimales.format(valor));
                    tv = (TextView) tr.findViewById(R.id.pisos);
                    tv.setText(dato.getString(3));
                    tv = (TextView) tr.findViewById(R.id.precio);
                    valor=Double.parseDouble(dato.getString(10));
                    tv.setText(decimales.format(valor));
                    tb.addView(tr);
                    btn=(Button)tr.findViewById(R.id.botonFachada);
                    btn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mostrarPopupModelos(Activity_Modelos.this, id_modelo, nombre_modelo, nImagenes);
                        }
                    });
                    tr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mostrarFinanciamiento(Activity_Modelos.this,modelo);
                        }
                    });
                    registerForContextMenu(tr);
                    i++;
                } while (dato.moveToNext());
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }finally {
            if(dbModelo!=null)
                dbModelo.close();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_modelos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id==android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private  void mostrarFinanciamiento(final Activity context, String[] modelo){
        AlertDialog.Builder builder;
        final AlertDialog alertDialog;
        LinearLayout viewGroup=(LinearLayout)context.findViewById(R.id.popupFinanciamiento);
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout=layoutInflater.inflate(R.layout.financiamiento,viewGroup);
        builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        builder.setCancelable(true);
        builder.setTitle(modelo[1]);
        TextView tv=(TextView)layout.findViewById(R.id.precioFinanciamiento);
        tv.setText(modelo[9]);
        EditText editText=(EditText)layout.findViewById(R.id.porcentajeCuotaInicial);
        builder.setNegativeButton("CANCELAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton("FINANCIAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        alertDialog = builder.create();

        alertDialog.show();
    }
    private void mostrarPopupModelos(final Activity context, String id_modelo, String nombre_modelo, String[] nombre_imagenes){

        AlertDialog.Builder builder;
        final AlertDialog alertDialog;
        //inflate layout popup
        LinearLayout viewGroup=(LinearLayout)context.findViewById(R.id.popupModelos);
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout=layoutInflater.inflate(R.layout.fachadas,viewGroup);
        GridView grid=(GridView)layout.findViewById(R.id.GridLViewModelos);
        ImagenModeloAdapter imgAdp;
        ArrayList<String[]> list=new ArrayList<String[]>();
        list.add(new String[]{id_modelo,nombre_modelo,nombre_imagenes[0],IMAGEN_1});
        list.add(new String[]{id_modelo,nombre_modelo,nombre_imagenes[1],IMAGEN_2});
        list.add(new String[]{id_modelo,nombre_modelo,nombre_imagenes[2],IMAGEN_3});

        imgAdp=new ImagenModeloAdapter(context,list);
        grid.setAdapter(imgAdp);

        builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        builder.setCancelable(true);
        builder.setTitle(nombre_modelo);
        builder.setNegativeButton("CERRAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton("FINANCIAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        alertDialog = builder.create();

        alertDialog.show();


    }

}
