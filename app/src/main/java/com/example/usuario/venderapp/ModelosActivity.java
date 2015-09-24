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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.usuario.venderapp.DataBase.DbModelo;
import com.example.usuario.venderapp.ClaseTramite.Modelo;
import com.example.usuario.venderapp.adapters.ImagenModeloAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class ModelosActivity extends ActionBarActivity {
    ArrayList<String[]> list;
    private String idLote;
    String path=null;
    String IMAGEN_1="fachada";
    String IMAGEN_2="planta1";
    String IMAGEN_3="planta2";
    String id_urb;
    DecimalFormat fomatoPrecio=new DecimalFormat("###,###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modelos);
        Intent intent = getIntent();
        idLote = intent.getStringExtra("id_lote");
        String lote = intent.getStringExtra("lote");
        String manzana = intent.getStringExtra("manzana");
        String urbanizacion = intent.getStringExtra("urbanizacion");
        id_urb = intent.getStringExtra("id_urb");
        TableLayout tb = (TableLayout) findViewById(R.id.tablaModelos);
        ((TextView)findViewById(R.id.manzana_modelo)).setText(manzana);
        ((TextView)findViewById(R.id.lote_modelo)).setText(lote);
        ((TextView)findViewById(R.id.urbanizacion)).setText(urbanizacion);


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
                            dato.getString(11),dato.getString(12),dato.getString(13),dato.getString(14),urbanizacion,lote,manzana,idLote,Financiamiento.COMO_MODELO};

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
                    tv.setText(fomatoPrecio.format(valor));
                    tb.addView(tr);
                    btn=(Button)tr.findViewById(R.id.botonFachada);
                    btn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mostrarPopupModelos(ModelosActivity.this, modelo);

                        }
                    });
                    tr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //mostrarFinanciamiento(Activity_Modelos.this,modelo);
                            Financiamiento financiamiento=new Financiamiento(ModelosActivity.this, modelo);
                            financiamiento.mostrar();
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
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }else if (id==android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void mostrarPopupModelos(final Activity context, final String[] sModelo){

        AlertDialog.Builder builder;
        final AlertDialog alertDialog;

        final Modelo modelo=new Modelo(sModelo[0],sModelo[1],sModelo[2],sModelo[3],sModelo[4],
                sModelo[5],sModelo[6],sModelo[7],sModelo[8],sModelo[9],sModelo[10],
                sModelo[11],sModelo[12],sModelo[13],sModelo[14],sModelo[15],sModelo[16],sModelo[17],idLote,Financiamiento.COMO_MODELO);
        //inflate layout popup
        LinearLayout viewGroup=(LinearLayout)context.findViewById(R.id.popupModelos);
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout=layoutInflater.inflate(R.layout.fachadas,viewGroup);
        GridView grid=(GridView)layout.findViewById(R.id.GridLViewModelos);
        ImagenModeloAdapter imgAdp;
        ArrayList<String[]> list=new ArrayList<String[]>();
        list.add(new String[]{modelo.getId(),modelo.getNombre_modelo(),modelo.getImg_fachada(),IMAGEN_1});
        list.add(new String[]{modelo.getId(),modelo.getNombre_modelo(),modelo.getImg_pb(),IMAGEN_2});
        if(modelo.getPisos().equals("2"))
            list.add(new String[]{modelo.getId(),modelo.getNombre_modelo(),modelo.getImg_pa_1(),IMAGEN_3});

        imgAdp=new ImagenModeloAdapter(context,list,id_urb);
        grid.setAdapter(imgAdp);

        builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        builder.setCancelable(true);
        builder.setTitle(modelo.getNombre_modelo());
        builder.setNegativeButton("CERRAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton("FINANCIAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Financiamiento financiamiento=new Financiamiento(ModelosActivity.this,sModelo);
                        financiamiento.mostrar();
                    }
                });
        alertDialog = builder.create();

        alertDialog.show();
    }



}
