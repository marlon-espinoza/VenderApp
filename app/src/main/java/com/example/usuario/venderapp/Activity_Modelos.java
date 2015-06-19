package com.example.usuario.venderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.usuario.venderapp.DataBase.DbFinanciamiento;
import com.example.usuario.venderapp.DataBase.DbModelo;
import com.example.usuario.venderapp.FrameLayout.Modelo;
import com.example.usuario.venderapp.adapters.ImagenModeloAdapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


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
        String lote = intent.getStringExtra("lote");
        String manzana = intent.getStringExtra("manzana");
        String[] urbanizacion = intent.getStringExtra("urbanizacion").split("\\(");
        TableLayout tb = (TableLayout) findViewById(R.id.tablaModelos);
        ((TextView)findViewById(R.id.manzana_modelo)).setText(manzana);
        ((TextView)findViewById(R.id.lote_modelo)).setText(lote);
        ((TextView)findViewById(R.id.urbanizacion)).setText(urbanizacion[0]);


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


    private  void mostrarFinanciamiento(final Activity context, String[] sModelo){
        AlertDialog.Builder builder;
        final AlertDialog alertDialog;
        final Modelo modelo=new Modelo(sModelo[0],sModelo[1],sModelo[2],sModelo[3],sModelo[4],
                sModelo[5],sModelo[6],sModelo[7],sModelo[8],sModelo[9],sModelo[10],
                sModelo[11],sModelo[12],sModelo[13],sModelo[14]);
        LinearLayout viewGroup=(LinearLayout)context.findViewById(R.id.popupFinanciamiento);
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout=layoutInflater.inflate(R.layout.financiamiento,viewGroup);
        builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        builder.setCancelable(true);
        builder.setTitle(modelo.getNombre_modelo());
        TextView tv=(TextView)layout.findViewById(R.id.precioFinanciamiento);
        tv.setText(modelo.getPrecio());
        inicializarCampos(modelo,layout);
        final EditText editTextCliente = (EditText)layout.findViewById(R.id.cliente);
        editTextCliente.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(editTextCliente.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        builder.setNegativeButton("CANCELAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton("FINANCIAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        guardarFinanciamiento(modelo,layout,context);
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
    void inicializarCampos(final Modelo modelo, final View layout){
        DecimalFormat decimales = new DecimalFormat("0.00");
        final double precio=Double.parseDouble(modelo.getPrecio().replace(",", "."));
        double entrada =(Double) precio*modelo.getCuota_entrada()/100;
        double valor_cuota;
        double cuota_inicial;
        int numPagosSaldo=120;
        int numPagosEntrada=25;
        EditText editText;
        EditText campo_porcentaje_entrada=(EditText)layout.findViewById(R.id.porcentajeCuotaEntrada);
        campo_porcentaje_entrada.setText(decimales.format(modelo.getCuota_entrada()));
        campo_porcentaje_entrada.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false)
                    editarPorcentajeEntrada(precio, layout);
            }
        });

        EditText porcentajeCuotaInicial=(EditText)layout.findViewById(R.id.porcentajeCuotaInicial);
        porcentajeCuotaInicial.setText(decimales.format(modelo.getCuota_inicial()));
        porcentajeCuotaInicial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false)
                    editarPorcentajeCuotaInicial(precio,layout);
            }
        });
        EditText campo_numero_pago_entrada=(EditText)layout.findViewById(R.id.numeroPagosEntrada);
        campo_numero_pago_entrada.setText(""+numPagosEntrada);
        campo_numero_pago_entrada.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false)
                    editarNumeroPagos(precio, layout);
            }
        });
        editText=(EditText)layout.findViewById(R.id.tasaInteres);
        editText.setText(""+modelo.getTasa());
        EditText campo_numeros_pagos_saldo=(EditText)layout.findViewById(R.id.numeroPagosSaldo);
        campo_numeros_pagos_saldo.setText("" + modelo.getPlazo_2());
        EditText campo_entrada=(EditText)layout.findViewById(R.id.entradra);
        campo_entrada.setText((String)decimales.format(entrada));
        campo_entrada.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false)
                editarEntrada(precio,layout);
            }
        });
        EditText edCuotaInicial=(EditText)layout.findViewById(R.id.cuotaInicial);
        cuota_inicial=(Double)precio*modelo.getCuota_inicial()/100;
        edCuotaInicial.setText((String)decimales.format(cuota_inicial));
        edCuotaInicial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false)
                    editarCuotaInicial(precio,layout);
            }
        });
        TextView textView=(TextView)layout.findViewById(R.id.cuotaEntrada);
        textView.setText((String)decimales.format((entrada-cuota_inicial)/numPagosEntrada));
        double cuota_ini=Double.parseDouble(editText.getText().toString());
        textView=(TextView)layout.findViewById(R.id.saldoPagos);
        textView.setText((String)decimales.format(cuota_ini/numPagosSaldo));
        Double saldo=(Double)(precio-entrada);
        textView.setText((String)decimales.format(saldo));
        valor_cuota=(Double)(saldo*modelo.getTasa()/100);

        textView=(TextView)layout.findViewById(R.id.cuotaSaldo);
        textView.setText((String)decimales.format(valor_cuota));


    }
    void editarEntrada(Double precio, View layout){
        DecimalFormat decimales = new DecimalFormat("0.00");

        EditText editTextEntrada = (EditText)layout.findViewById(R.id.entradra);
        EditText editTextPorcentajeCuotaEntrada = (EditText)layout.findViewById(R.id.porcentajeCuotaEntrada);
        TextView textViewSaldo = (TextView)layout.findViewById(R.id.saldoPagos);
        TextView textViewCuotaEntrada = (TextView) layout.findViewById(R.id.cuotaEntrada);
        TextView textViewCuotaInicial = (TextView) layout.findViewById(R.id.cuotaInicial);
        EditText editTextNumeroPagos = (EditText)layout.findViewById(R.id.numeroPagosEntrada);
        double entrada=Double.parseDouble(editTextEntrada.getText().toString().replace(",", "."));
        double cuotaInicial=Double.parseDouble(textViewCuotaInicial.getText().toString().replace(",", "."));
        double porcentaje_entrada=(entrada/precio)*100;
        editTextPorcentajeCuotaEntrada.setText(decimales.format(porcentaje_entrada));
        double saldo=precio-entrada;
        int numeroPagos = Integer.parseInt(editTextNumeroPagos.getText().toString());
        double cuotaEntrada=(entrada-cuotaInicial)/numeroPagos;
        textViewCuotaEntrada.setText(decimales.format(cuotaEntrada));
        textViewSaldo.setText((String) decimales.format(saldo));
        //double valor_cuota;
    }
    void editarPorcentajeEntrada(Double precio,View layout){
        DecimalFormat decimales = new DecimalFormat("0.00");

        EditText editTextEntrada = (EditText)layout.findViewById(R.id.entradra);
        EditText editTextPorcentajeCuotaEntrada = (EditText)layout.findViewById(R.id.porcentajeCuotaEntrada);
        TextView textViewCuotaEntrada = (TextView) layout.findViewById(R.id.cuotaEntrada);
        TextView textViewCuotaInicial = (TextView) layout.findViewById(R.id.cuotaInicial);
        EditText editTextNumeroPagos = (EditText)layout.findViewById(R.id.numeroPagosEntrada);
        TextView textViewSaldo = (TextView)layout.findViewById(R.id.saldoPagos);
        double cuotaInicial=Double.parseDouble(textViewCuotaInicial.getText().toString().replace(",", "."));
        double porcentajeEntrada=Double.parseDouble(editTextPorcentajeCuotaEntrada.getText().toString().replace(",", "."));
        double entrada=(porcentajeEntrada*precio)/100;
        editTextEntrada.setText(decimales.format(entrada));
        double saldo=precio-entrada;
        int numeroPagos = Integer.parseInt(editTextNumeroPagos.getText().toString());
        double cuotaEntrada=(entrada-cuotaInicial)/numeroPagos;
        textViewCuotaEntrada.setText(decimales.format(cuotaEntrada));
        textViewSaldo.setText((String) decimales.format(saldo));
    }
    void editarCuotaInicial(Double precio,View layout){
        DecimalFormat decimales = new DecimalFormat("0.00");

        EditText editTextCuotaInicial = (EditText)layout.findViewById(R.id.cuotaInicial);
        EditText editTextPorcentajeCuotaInicial = (EditText)layout.findViewById(R.id.porcentajeCuotaInicial);
        EditText editTextEntrada = (EditText)layout.findViewById(R.id.entradra);
        TextView textViewCuotaEntrada = (TextView)layout.findViewById(R.id.cuotaEntrada);
        EditText editTextNumeroPagos = (EditText)layout.findViewById(R.id.numeroPagosEntrada);
        double entrada= Double.parseDouble(editTextEntrada.getText().toString().replace(",", "."));
        double numeroPagos = Double.parseDouble(editTextNumeroPagos.getText().toString().replace(",", "."));
        double cuotaInicial=Double.parseDouble(editTextCuotaInicial.getText().toString().replace(",", "."));
        double cuota=(entrada-cuotaInicial)/numeroPagos;
        double porcentajeCuotaInicial=(cuotaInicial/precio)*100;
        editTextPorcentajeCuotaInicial.setText(decimales.format(porcentajeCuotaInicial));
        textViewCuotaEntrada.setText(decimales.format(cuota));

    }
    void editarPorcentajeCuotaInicial(Double precio,View layout){
        DecimalFormat decimales = new DecimalFormat("0.00");

        EditText editTextCuotaInicial = (EditText)layout.findViewById(R.id.cuotaInicial);
        EditText editTextPorcentajeCuotaInicial = (EditText)layout.findViewById(R.id.porcentajeCuotaInicial);
        EditText editTextEntrada = (EditText)layout.findViewById(R.id.entradra);
        TextView textViewCuotaEntrada = (TextView)layout.findViewById(R.id.cuotaEntrada);
        EditText editTextNumeroPagos = (EditText)layout.findViewById(R.id.numeroPagosEntrada);
        double entrada= Double.parseDouble(editTextEntrada.getText().toString().replace(",", "."));
        double numeroPagos = Double.parseDouble(editTextNumeroPagos.getText().toString().replace(",", "."));
        double porcentajeCuotaInicial=Double.parseDouble(editTextPorcentajeCuotaInicial.getText().toString().replace(",", "."));

        double cuotaInicial=(precio*porcentajeCuotaInicial)/100;
        //double cuota=cuotaInicial/numeroPagos;
        double cuota=(entrada-cuotaInicial)/numeroPagos;
        editTextCuotaInicial.setText(decimales.format(cuotaInicial));
        textViewCuotaEntrada.setText(decimales.format(cuota));
    }
    void editarNumeroPagos(Double precio,View layout){
        DecimalFormat decimales = new DecimalFormat("0.00");

        EditText editTextEntrada = (EditText)layout.findViewById(R.id.entradra);
        EditText editTextCuotaInicial = (EditText)layout.findViewById(R.id.cuotaInicial);
        EditText editTextNumeroPagos = (EditText)layout.findViewById(R.id.numeroPagosEntrada);
        TextView textViewCuotaEntrada = (TextView)layout.findViewById(R.id.cuotaEntrada);
        double entrada= Double.parseDouble(editTextEntrada.getText().toString().replace(",", "."));
        double numeroPagos = Double.parseDouble(editTextNumeroPagos.getText().toString().replace(",", "."));
        double cuotaInicial=Double.parseDouble(editTextCuotaInicial.getText().toString().replace(",", "."));
        double cuota = (entrada-cuotaInicial)/numeroPagos;
        textViewCuotaEntrada.setText(decimales.format(cuota));

    }
    void guardarFinanciamiento(Modelo modelo,View layout,Context context){

        DbFinanciamiento dbFinanciamiento=new DbFinanciamiento(context);
        Date fecha=new Date();
        Date tiempo=new Date();
        Calendar c= Calendar.getInstance();
        //c.set(fecha.getYear()+1900, fecha.getMonth(), fecha.getDate(), tiempo.getHours(), tiempo.getMinutes());
        //d.setTime(c.getTimeInMillis());
        System.out.println(fecha);
        c.setTime(fecha);
        c.set(Calendar.HOUR,tiempo.getHours());
        c.set(Calendar.MINUTE, tiempo.getMinutes());
        /*insertar(String id_modelo,String nombre_modelo,String precio, String entrada,
                         String porcentaje_entrada,String cuota_inicial, String porcentaje_cuota_inicial,
                         String num_pagos_entrada,String cuota_entrada,String saldo,String tasa_interes,
                         String num_pagos_saldo,String cliente, Date fecha)*/
        dbFinanciamiento.insertar(modelo.getId(),modelo.getNombre_modelo(),
                ((TextView)layout.findViewById(R.id.precioFinanciamiento)).getText().toString(),
                ((EditText)layout.findViewById(R.id.entradra)).getText().toString(),
                ((EditText)layout.findViewById(R.id.porcentajeCuotaEntrada)).getText().toString(),
                ((EditText)layout.findViewById(R.id.cuotaInicial)).getText().toString(),
                ((EditText)layout.findViewById(R.id.porcentajeCuotaInicial)).getText().toString(),
                ((EditText)layout.findViewById(R.id.numeroPagosEntrada)).getText().toString(),
                ((TextView)layout.findViewById(R.id.cuotaEntrada)).getText().toString(),
                ((TextView)layout.findViewById(R.id.saldoPagos)).getText().toString(),
                ((EditText)layout.findViewById(R.id.tasaInteres)).getText().toString(),
                ((EditText)layout.findViewById(R.id.numeroPagosSaldo)).getText().toString(),
                ((EditText)layout.findViewById(R.id.cliente)).getText().toString(),
                c.getTime());
    }


}
