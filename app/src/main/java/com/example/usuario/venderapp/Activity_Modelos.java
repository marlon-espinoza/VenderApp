package com.example.usuario.venderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.usuario.venderapp.DataBase.DbModelo;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class Activity_Modelos extends ActionBarActivity {
    ArrayList<String[]> list;
    private String idLote;
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
            Cursor dato = dbModelo.consultar(idLote);
            DecimalFormat decimales = new DecimalFormat("0.00");
            double valor;

            if (dato.moveToFirst()) {
                findViewById(android.R.id.empty).setVisibility(View.GONE);
                int i=0;
                //Recorremos el cursor hasta que no haya más registros
                do {
                    //String[] campos = new String[] {ID_MODELO,MODELO,AREA_CONST,PISOS,CUOTA_ENTRADA,CUOTA_INICIAL,TASA,PLAZO_1,
                    //PLAZO_2,PLAZO_3,PRECIO};
                    final TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.table_row_modelo, null);
                    TextView tv;
                    Button btn;
                    final String[] nImagenes={dato.getString(12),dato.getString(13),dato.getString(14)};
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
                            showPopupFachada(Activity_Modelos.this,nombre_modelo,nImagenes);
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



    private void showPopupFachada(final Activity context,String nombre_modelo,String[]nombre_imagenes){
        String url="http://200.93.200.156/sitio1/Imagenes/";
        AlertDialog.Builder builder;
        final AlertDialog alertDialog;
        int popupWidth=200;
        int popupHeight=150;
        //inflate layout popup
        LinearLayout viewGroup=(LinearLayout)context.findViewById(R.id.popupFachadas);
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout=layoutInflater.inflate(R.layout.fachadas,viewGroup);



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
        /*final PopupWindow popup=new PopupWindow(context);
        popup.setContentView(layout);
        popup.setHeight(popupHeight);
        popup.setWidth(popupWidth);
        popup.setFocusable(true);

        int OFFSET_X=30;
        int OFFSET_Y=30;

        popup.showAtLocation(layout, Gravity.CENTER, OFFSET_X, OFFSET_Y);*/


    }
}
