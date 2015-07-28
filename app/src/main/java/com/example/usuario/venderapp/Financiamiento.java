package com.example.usuario.venderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.venderapp.ClaseTramite.Modelo;
import com.example.usuario.venderapp.DataBase.DbFinanciamiento;
import com.example.usuario.venderapp.DataBase.DbLote;
import com.example.usuario.venderapp.DataBase.DbModelo;
import com.google.android.gms.drive.internal.m;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by USUARIO on 26-jun-15.
 */
public class Financiamiento {
    /*DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;*/
    AlertDialog.Builder builder;
    final AlertDialog alertDialog;
    int dialogWidth =600; // specify a value here
    int dialogHeight =750; // specify a value here

    //Nuevo Financiamiento
    public Financiamiento(final Activity context, String[] sModelo) {
        final Modelo modelo=new Modelo(sModelo[0],sModelo[1],sModelo[2],sModelo[3],sModelo[4],
                sModelo[5],sModelo[6],sModelo[7],sModelo[8],sModelo[9],sModelo[10],
                sModelo[11],sModelo[12],sModelo[13],sModelo[14],sModelo[15],sModelo[16],sModelo[17],sModelo[18]);
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
                    InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
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
                        enviarFinanciamiento(context,mensaje(context,new String[]{modelo.getId(),modelo.getUrbanizacion()},layout,false));
                    }
                });
        alertDialog = builder.create();
    }
    //Mostrar Financiamiento
    public Financiamiento(final Activity context, final String id_financiamiento,String []sModelo,final View convertView) {
        /*final Modelo modelo=new Modelo(sModelo[0],sModelo[1],sModelo[2],sModelo[3],sModelo[4],
                sModelo[5],sModelo[6],sModelo[7],sModelo[8],sModelo[9],sModelo[10],
                sModelo[11],sModelo[12],sModelo[13],sModelo[14],sModelo[15],sModelo[16],sModelo[17],sModelo[18]);*/
        LinearLayout viewGroup=(LinearLayout)context.findViewById(R.id.popupFinanciamiento);
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout=layoutInflater.inflate(R.layout.financiamiento_detalle,viewGroup);
        DbFinanciamiento financiamiento=null;
        String idModelo[] = null;
        try
        {
            /*ID_FINANCIAMIENTO,ID_MODELO,NOMBRE_MODELO,URBANIZACION,LOTE,MANZANA,PRECIO,ENTRADA,
                PORCENTAJE_ENTRADA,CUOTA_INICIAL,PORCENTAJE_CUOTA_INICIAL,NUM_PAGOS_ENTRADA,CUOTA_ENTRADA,SALDO,TASA_INTERES,
                NUM_PAGOS_SALDO,CUOTA_SALDO,CLIENTE,FECHA*/
            financiamiento = new DbFinanciamiento(context);
            Cursor dato = financiamiento.consultar(id_financiamiento);
            if (dato.moveToFirst()) {
                //TITLE, COURSE_NAME,DESCRIPTION,STAR_DATE,FINAL_DATE,DEAD_LINE
                idModelo=new String[]{dato.getString(1),dato.getString(3)};
                ((TextView)layout.findViewById(R.id.urbanizacion_detalle)).setText(sModelo[1]);
                ((TextView)layout.findViewById(R.id.lote_detalle)).setText(sModelo[2]);
                ((TextView)layout.findViewById(R.id.manzana_detalle)).setText(sModelo[3]);
                ((TextView)layout.findViewById(R.id.precioFinanciamiento)).setText(dato.getString(6));
                ((TextView)layout.findViewById(R.id.entradra_detalle)).setText(dato.getString(7));
                ((TextView)layout.findViewById(R.id.porcentajeCuotaEntrada_detalle)).setText(dato.getString(8));
                ((TextView)layout.findViewById(R.id.cuotaInicial_detalle)).setText(dato.getString(9));
                ((TextView)layout.findViewById(R.id.porcentajeCuotaInicial_detalle)).setText(dato.getString(10));
                ((TextView)layout.findViewById(R.id.numeroPagosEntrada_detalle)).setText(dato.getString(11));
                ((TextView)layout.findViewById(R.id.cuotaEntrada)).setText(dato.getString(12));
                ((TextView)layout.findViewById(R.id.saldoPagos)).setText(dato.getString(13));
                ((TextView)layout.findViewById(R.id.tasaInteres_detalle)).setText(dato.getString(14));
                ((TextView)layout.findViewById(R.id.numeroPagosSaldo_detalle)).setText(dato.getString(15));
                ((TextView)layout.findViewById(R.id.cuotaSaldo)).setText(dato.getString(16));
                ((TextView)layout.findViewById(R.id.cliente_detalle)).setText(dato.getString(17));

            }
        }catch (Exception e)
        {
            System.out.println(e.toString());
        }finally {
            if(financiamiento!=null)
                financiamiento.close();
        }
        builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        builder.setCancelable(true);
        builder.setTitle(sModelo[0]);
        builder.setNegativeButton("CERRAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        builder.setNeutralButton("EDITAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Financiamiento financiamiento1 = new Financiamiento(context,id_financiamiento,convertView);
                financiamiento1.mostrar();
            }
        });
        final String[] finalIdModelo = idModelo;
        builder.setPositiveButton("REENVIAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                enviarFinanciamiento(context, mensaje(context, finalIdModelo, layout, true));
            }
        });
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);



    }
    //Editar Financiamiento
    public Financiamiento(final Activity context, final String id_financiamiento,final View convertView){

        final LinearLayout viewGroup=(LinearLayout)context.findViewById(R.id.popupFinanciamiento);
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout=layoutInflater.inflate(R.layout.financiamiento,viewGroup);
        builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        builder.setCancelable(true);
        DbFinanciamiento dbFinanciamiento=null;
        String[] idModelo=null;
        try
        {
            /*ID_FINANCIAMIENTO,ID_MODELO,NOMBRE_MODELO,URBANIZACION,LOTE,MANZANA,PRECIO,ENTRADA,
                PORCENTAJE_ENTRADA,CUOTA_INICIAL,PORCENTAJE_CUOTA_INICIAL,NUM_PAGOS_ENTRADA,CUOTA_ENTRADA,SALDO,TASA_INTERES,
                NUM_PAGOS_SALDO,CUOTA_SALDO,CLIENTE,FECHA*/
            dbFinanciamiento = new DbFinanciamiento(context);
            Cursor dato = dbFinanciamiento.consultar(id_financiamiento);
            if (dato.moveToFirst()) {
                //TITLE, COURSE_NAME,DESCRIPTION,STAR_DATE,FINAL_DATE,DEAD_LINE
                idModelo=new String[]{dato.getString(1),dato.getString(2)};

                builder.setTitle(dato.getString(2));
                ((TextView)layout.findViewById(R.id.precioFinanciamiento)).setText(dato.getString(6));
                ((EditText)layout.findViewById(R.id.entradra)).setText(dato.getString(7));

                ((EditText)layout.findViewById(R.id.porcentajeCuotaEntrada)).setText(dato.getString(8));
                ((EditText)layout.findViewById(R.id.cuotaInicial)).setText(dato.getString(9));
                ((EditText)layout.findViewById(R.id.porcentajeCuotaInicial)).setText(dato.getString(10));
                ((EditText)layout.findViewById(R.id.numeroPagosEntrada)).setText(dato.getString(11));
                ((TextView)layout.findViewById(R.id.cuotaEntrada)).setText(dato.getString(12));
                ((TextView)layout.findViewById(R.id.saldoPagos)).setText(dato.getString(13));

                ((EditText)layout.findViewById(R.id.tasaInteres)).setText(dato.getString(14));
                ((EditText)layout.findViewById(R.id.numeroPagosSaldo)).setText(dato.getString(15));
                ((TextView)layout.findViewById(R.id.cuotaSaldo)).setText(dato.getString(16));
                ((EditText)layout.findViewById(R.id.cliente)).setText(dato.getString(17));

            }
        }catch (Exception e)
        {
            System.out.println(e.toString());
        }finally {
            if(dbFinanciamiento!=null)
                dbFinanciamiento.close();
        }
        final double precio=Double.parseDouble(((TextView)layout.findViewById(R.id.precioFinanciamiento)).getText().toString());
        EditText campo_porcentaje_entrada=(EditText)layout.findViewById(R.id.porcentajeCuotaEntrada);
        campo_porcentaje_entrada.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false)
                    editarPorcentajeEntrada(precio, layout);
            }
        });

        EditText porcentajeCuotaInicial=(EditText)layout.findViewById(R.id.porcentajeCuotaInicial);
        porcentajeCuotaInicial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false)
                    editarPorcentajeCuotaInicial(precio,layout);
            }
        });
        EditText campo_numero_pago_entrada=(EditText)layout.findViewById(R.id.numeroPagosEntrada);
        campo_numero_pago_entrada.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false)
                    editarNumeroPagos(precio, layout);
            }
        });
        EditText campo_tasa=(EditText)layout.findViewById(R.id.tasaInteres);
        campo_tasa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false)
                    editarSaldo(layout);
            }
        });
        EditText campo_pagos_saldo=(EditText)layout.findViewById(R.id.numeroPagosSaldo);
        campo_pagos_saldo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false)
                    editarSaldo(layout);
            }
        });
        EditText campo_entrada=(EditText)layout.findViewById(R.id.entradra);
        campo_entrada.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false)
                    editarEntrada(precio,layout);
            }
        });
        EditText edCuotaInicial=(EditText)layout.findViewById(R.id.cuotaInicial);
        edCuotaInicial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false)
                    editarCuotaInicial(precio,layout);
            }
        });

        final EditText editTextCliente = (EditText)layout.findViewById(R.id.cliente);
        editTextCliente.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
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


        final String[] finalIdModelo = idModelo;
        builder.setPositiveButton("GUARDAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        guardarEdicionFinanciamiento(id_financiamiento,layout,context,convertView);
                        enviarFinanciamiento(context,mensaje(context, finalIdModelo,layout,false));
                        dialog.cancel();

                    }
                });
        alertDialog = builder.create();
    }

    public void mostrar(){


        alertDialog.show();
        alertDialog.getWindow().setLayout(dialogWidth, dialogHeight);

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
        EditText campo_tasa=(EditText)layout.findViewById(R.id.tasaInteres);
        campo_tasa.setText("" + modelo.getTasa());
        campo_tasa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false)
                    editarSaldo(layout);
            }
        });
        EditText campo_pagos_saldo=(EditText)layout.findViewById(R.id.numeroPagosSaldo);
        campo_pagos_saldo.setText("" + modelo.getPlazo_2());
        campo_pagos_saldo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false)
                    editarSaldo(layout);
            }
        });
        EditText campo_entrada=(EditText)layout.findViewById(R.id.entradra);
        campo_entrada.setText(decimales.format(entrada));
        campo_entrada.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false)
                    editarEntrada(precio,layout);
            }
        });
        EditText edCuotaInicial=(EditText)layout.findViewById(R.id.cuotaInicial);
        cuota_inicial=(Double)precio*modelo.getCuota_inicial()/100;
        edCuotaInicial.setText(decimales.format(cuota_inicial));
        edCuotaInicial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false)
                    editarCuotaInicial(precio,layout);
            }
        });
        TextView textView=(TextView)layout.findViewById(R.id.cuotaEntrada);
        textView.setText(decimales.format((entrada-cuota_inicial)/numPagosEntrada));
        double cuota_ini=Double.parseDouble(edCuotaInicial.getText().toString().replace(",", "."));
        textView=(TextView)layout.findViewById(R.id.saldoPagos);
        textView.setText(decimales.format(cuota_ini/numPagosSaldo));
        Double saldo=(precio-entrada);
        textView.setText(decimales.format(saldo));
        valor_cuota=calcularCuotaSaldo(saldo,(double)modelo.getTasa(),modelo.getPlazo_2());

        textView=(TextView)layout.findViewById(R.id.cuotaSaldo);
        textView.setText(decimales.format(valor_cuota));


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
        textViewSaldo.setText(decimales.format(saldo));
        EditText editTextTasa = (EditText)layout.findViewById(R.id.tasaInteres);
        EditText editTextTNumPagos = (EditText)layout.findViewById(R.id.numeroPagosSaldo);
        TextView textViewCuotaSaldo = (TextView)layout.findViewById(R.id.cuotaSaldo);
        double tasa = Double.parseDouble(editTextTasa.getText().toString());
        int numPagos = Integer.parseInt(editTextTNumPagos.getText().toString());
        double valor_cuota = calcularCuotaSaldo(saldo,tasa,numPagos);
        textViewCuotaSaldo.setText(decimales.format(valor_cuota));
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
        textViewSaldo.setText(decimales.format(saldo));
        EditText editTextTasa = (EditText)layout.findViewById(R.id.tasaInteres);
        EditText editTextTNumPagos = (EditText)layout.findViewById(R.id.numeroPagosSaldo);
        TextView textViewCuotaSaldo = (TextView)layout.findViewById(R.id.cuotaSaldo);
        double tasa = Double.parseDouble(editTextTasa.getText().toString());
        int numPagos = Integer.parseInt(editTextTNumPagos.getText().toString());
        double valor_cuota = calcularCuotaSaldo(saldo,tasa,numPagos);
        textViewCuotaSaldo.setText(decimales.format(valor_cuota));


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
    void editarSaldo(View layout){
        DecimalFormat decimales = new DecimalFormat("0.00");
        TextView textViewSaldo = (TextView)layout.findViewById(R.id.saldoPagos);
        EditText editTextTasa = (EditText)layout.findViewById(R.id.tasaInteres);
        EditText editTextTNumPagos = (EditText)layout.findViewById(R.id.numeroPagosSaldo);
        TextView textViewCuotaSaldo = (TextView)layout.findViewById(R.id.cuotaSaldo);
        double saldo = Double.parseDouble(textViewSaldo.getText().toString().replace(",", "."));
        double tasa = Double.parseDouble(editTextTasa.getText().toString());
        int numPagos = Integer.parseInt(editTextTNumPagos.getText().toString());
        double valor_cuota = calcularCuotaSaldo(saldo,tasa,numPagos);
        textViewCuotaSaldo.setText(decimales.format(valor_cuota));
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
        dbFinanciamiento.insertar(modelo.getId(), modelo.getNombre_modelo(),
                modelo.getUrbanizacion(),modelo.getLote(),modelo.getManzana(),
                ((TextView) layout.findViewById(R.id.precioFinanciamiento)).getText().toString(),
                ((EditText) layout.findViewById(R.id.entradra)).getText().toString(),
                ((EditText) layout.findViewById(R.id.porcentajeCuotaEntrada)).getText().toString(),
                ((EditText) layout.findViewById(R.id.cuotaInicial)).getText().toString(),
                ((EditText) layout.findViewById(R.id.porcentajeCuotaInicial)).getText().toString(),
                ((EditText) layout.findViewById(R.id.numeroPagosEntrada)).getText().toString(),
                ((TextView) layout.findViewById(R.id.cuotaEntrada)).getText().toString(),
                ((TextView) layout.findViewById(R.id.saldoPagos)).getText().toString(),
                ((EditText) layout.findViewById(R.id.tasaInteres)).getText().toString(),
                ((EditText) layout.findViewById(R.id.numeroPagosSaldo)).getText().toString(),
                ((TextView) layout.findViewById(R.id.cuotaSaldo)).getText().toString(),
                ((EditText) layout.findViewById(R.id.cliente)).getText().toString(),
                c.getTime());
        Toast.makeText(context, "Financiamieno Guardado", Toast.LENGTH_SHORT).show();

    }
    void guardarEdicionFinanciamiento(String id_financiamiento,View layout,Context context,final View convertView){
        DbFinanciamiento dbFinanciamiento=new DbFinanciamiento(context);
        Date fecha=new Date();
        Date tiempo=new Date();
        Calendar c= Calendar.getInstance();
        //c.set(fecha.getYear()+1900, fecha.getMonth(), fecha.getDate(), tiempo.getHours(), tiempo.getMinutes());
        //d.setTime(c.getTimeInMillis());
        c.setTime(fecha);
        c.set(Calendar.HOUR,tiempo.getHours());
        c.set(Calendar.MINUTE, tiempo.getMinutes());
        /*insertar(String id_modelo,String nombre_modelo,String precio, String entrada,
                         String porcentaje_entrada,String cuota_inicial, String porcentaje_cuota_inicial,
                         String num_pagos_entrada,String cuota_entrada,String saldo,String tasa_interes,
                         String num_pagos_saldo,String cliente, Date fecha)*/
        dbFinanciamiento.editar(id_financiamiento,
                ((EditText) layout.findViewById(R.id.entradra)).getText().toString(),
                ((EditText) layout.findViewById(R.id.porcentajeCuotaEntrada)).getText().toString(),
                ((EditText) layout.findViewById(R.id.cuotaInicial)).getText().toString(),
                ((EditText) layout.findViewById(R.id.porcentajeCuotaInicial)).getText().toString(),
                ((EditText) layout.findViewById(R.id.numeroPagosEntrada)).getText().toString(),
                ((TextView) layout.findViewById(R.id.cuotaEntrada)).getText().toString(),
                ((TextView) layout.findViewById(R.id.saldoPagos)).getText().toString(),
                ((EditText) layout.findViewById(R.id.tasaInteres)).getText().toString(),
                ((EditText) layout.findViewById(R.id.numeroPagosSaldo)).getText().toString(),
                ((TextView) layout.findViewById(R.id.cuotaSaldo)).getText().toString(),
                ((EditText) layout.findViewById(R.id.cliente)).getText().toString(),
                c.getTime());
                ((TextView) convertView.findViewById(R.id.cuotaEntradaVw)).setText("$"+((TextView) layout.findViewById(R.id.cuotaEntrada)).getText().toString());
                ((TextView) convertView.findViewById(R.id.cuotaSaldoVw)).setText("$"+((TextView) layout.findViewById(R.id.cuotaSaldo)).getText().toString());
                ((TextView) convertView.findViewById(R.id.clienteVw)).setText(((EditText) layout.findViewById(R.id.cliente)).getText().toString());
        Toast.makeText(context, "Financiamieno Editado", Toast.LENGTH_SHORT).show();

    }
    private double calcularCuotaSaldo(double Capital,double tasa,int pagos){
        double tasa_m=(Math.pow(1.0+tasa/100.0,1.0/12.0))-1.0;
        double y=(Math.pow(1.0+tasa_m,(double)pagos))-1.0;
        double x=Capital*tasa_m*Math.pow(1.0+tasa_m,(double)pagos);
        return x/y;
    }
    public void enviarFinanciamiento(Activity context,String mensaje){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        //emailIntent.setType("text/html");
        //String[] to = direccionesEmail;
        //String[] cc = copias;

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ciudad Celeste, Financiamiento");
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(mensaje));
        //emailIntent.setType("message/rfc822");
        context.startActivity(Intent.createChooser(emailIntent, "Email "));
    }
    private String mensaje(Activity context,String[] idModelo,View layout,Boolean reenvio) {
        DbLote dbLote=null;
        DbModelo dbModelo=null;
        String[] lote=null;
        String[] financiamiento=null;
        Modelo modelo=null;

        try {
            /*ID_LOTE 0,MANZANA 1,NUM_LOTE 2,ESTADO 3,AREA 4,ENTRADA 5,ENTREGA 6,VENDER_COMO 7*/
            dbModelo = new DbModelo(context);
            dbLote = new DbLote(context);
            /*MODELO,AREA_CONST,PISOS,CUOTA_ENTRADA,CUOTA_INICIAL,TASA,PLAZO_1,
                PLAZO_2,PLAZO_3,PRECIO,IMAGEN_FACHADA,IMAGEN_PLANTA_BAJA,IMAGEN_PLANTA_ALTA_1,IMAGEN_PLANTA_ALTA_2,ID_LOTE*/
            Cursor sModelo = dbModelo.consultarModelo(idModelo[0]);
            if (sModelo.moveToFirst()) {



            /*String id,String modelo,String area,String pisos, String cuota_ent, String cuota_ini,
                  String tasa, String plazo1, String plazo2,String plazo3,
                  String precio, String img_fach,String img_pb,String img_pa1,String img_pa2,
                  String urbanizacion,String lote, String manzana,String id_lote*/
                modelo = new Modelo(idModelo[0], sModelo.getString(0), sModelo.getString(1), sModelo.getString(2), sModelo.getString(3),
                        sModelo.getString(4), sModelo.getString(5), sModelo.getString(6), sModelo.getString(7), sModelo.getString(8),
                        sModelo.getString(9), sModelo.getString(10), sModelo.getString(11), sModelo.getString(12), sModelo.getString(13),
                        idModelo[1], null, null, sModelo.getString(14));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (dbModelo != null)
                dbModelo.close();
        }
        try{
            /*ID_LOTE 0,MANZANA 1,NUM_LOTE 2,ESTADO 3,AREA 4,ENTRADA 5,ENTREGA 6,VENDER_COMO 7*/
            dbLote = new DbLote(context);
            Cursor sLote = dbLote.consultar(modelo.getId_lote());
            if (sLote.moveToFirst()) {
                lote = new String[]{sLote.getString(1), sLote.getString(2), sLote.getString(3),
                        sLote.getString(4), sLote.getString(5), sLote.getString(6)};
                modelo.lote = lote[2];
                modelo.manzana = lote[1];
            }

        }catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (dbLote != null)
                dbLote.close();
        }
        if(reenvio){
            financiamiento=new String[]{((TextView)layout.findViewById(R.id.entradra_detalle)).getText().toString(),
            ((TextView)layout.findViewById(R.id.porcentajeCuotaEntrada_detalle)).getText().toString(),
            ((TextView)layout.findViewById(R.id.cuotaInicial_detalle)).getText().toString(),
            ((TextView)layout.findViewById(R.id.porcentajeCuotaInicial_detalle)).getText().toString(),
            ((TextView)layout.findViewById(R.id.numeroPagosEntrada_detalle)).getText().toString(),
            ((TextView)layout.findViewById(R.id.cuotaEntrada)).getText().toString(),
            ((TextView)layout.findViewById(R.id.saldoPagos)).getText().toString(),
            ((TextView)layout.findViewById(R.id.tasaInteres_detalle)).getText().toString(),
            ((TextView)layout.findViewById(R.id.numeroPagosSaldo_detalle)).getText().toString(),
            ((TextView)layout.findViewById(R.id.cuotaSaldo)).getText().toString(),
            ((TextView)layout.findViewById(R.id.cliente_detalle)).getText().toString()};

        }else{
            financiamiento=new String[]{((EditText) layout.findViewById(R.id.entradra)).getText().toString(),
                    ((EditText) layout.findViewById(R.id.porcentajeCuotaEntrada)).getText().toString(),
                    ((EditText) layout.findViewById(R.id.cuotaInicial)).getText().toString(),
                    ((EditText) layout.findViewById(R.id.porcentajeCuotaInicial)).getText().toString(),
                    ((EditText) layout.findViewById(R.id.numeroPagosEntrada)).getText().toString(),
                    ((TextView) layout.findViewById(R.id.cuotaEntrada)).getText().toString(),
                    ((TextView) layout.findViewById(R.id.saldoPagos)).getText().toString(),
                    ((EditText) layout.findViewById(R.id.tasaInteres)).getText().toString(),
                    ((EditText) layout.findViewById(R.id.numeroPagosSaldo)).getText().toString(),
                    ((TextView) layout.findViewById(R.id.cuotaSaldo)).getText().toString(),
                    ((EditText) layout.findViewById(R.id.cliente)).getText().toString()};

        }
        //String msg="Urbanización: "+modelo.getUrbanizacion()+"\nManzana: "+modelo.getManzana()+"\nLote: "+modelo.getLote()+"\nModelo: "+modelo.getNombre_modelo()+"\nÁrea: "+modelo.getArea_const();
        String mensaje="<h1>Detalle de Modelo</h1>\n" +
                "<p><label><b>Urbanizaci&oacute;n:</b></label>\n" +
                "<label>"+modelo.getUrbanizacion()+"</label>\n" +
                "</p>\n" +
                "<p><label><b>Manzana: </b></label>\n" +
                "<label>"+ modelo.getManzana() +"</label>\n" +
                "</p>\n" +
                "<p><label><b>Lote: </b></label>\n" +
                "<label>"+modelo.getLote()+"</label>\n" +
                "</p>\n" +
                "<p><label><b>Modelo: </b></label>\n" +
                "<label>"+modelo.getNombre_modelo()+"</label>\n" +
                "</p>\n" +
                "<p><label><b>&Aacute;rea Terreno (m2): </b></label>\n" +
                "<label>"+lote[3]+"</label>\n" +
                "</p>\n" +
                "<p><label><b>&Aacute;rea Contrucci&oacute;n (m2): </b></label>\n" +
                "<label>"+modelo.getArea_const()+"</label>\n" +
                "</p>\n" +
                "<p><label><b>Plazo Entrada: </b></label>\n" +
                "<label>"+lote[4]+"</label>\n" +
                "</p>\n" +
                "<p><label><b>Plazo Entrega: </b></label>\n" +
                "<label>"+lote[5]+"</label>\n" +
                "</p>\n" +
                "<p><label><b>Fecha Entrega: </b></label>\n" +
                "<label>  Mayo/2016</label>\n" +
                "</p>\n" +
                "<h1>Detalle de Financiamiento</h1>\n" +
                "<p><label><b>Precio:</b></label>\n" +
                "<label>$"+modelo.getPrecio()+" </label>\n" +
                "</p>\n" +
                "<h2>Forma de pago de Cuota de Entrada</h2>\n" +
                "<p><label><b>Entrada: </b></label>\n" +
                "<label>$"+financiamiento[0]+" || "+financiamiento[1]+"%</label>\n" +
                "</p>\n" +
                "<p><label><b>Cuota Inicial: </b></label>\n" +
                "<label>$"+financiamiento[2]+"|| "+financiamiento[3]+"%</label>\n" +
                "</p>\n" +
                "<p><label><b>No. de Pagos: </b></label>\n" +
                "<label> $"+financiamiento[4]+"</label>\n" +
                "</p>\n" +
                "<p><label><b>Valor de Cuota: </b></label>\n" +
                "<label> $"+financiamiento[5]+"</label>\n" +
                "</p>\n" +
                "<h2>Forma de pago del Saldo</h2>\n" +
                "<p><label><b>Saldo: </b></label>\n" +
                "<label>$"+financiamiento[6]+"</label>\n" +
                "</p>\n" +
                "<p><label><b>No. de Pagos: </b></label>\n" +
                "<label>"+financiamiento[7]+"</label>\n" +
                "</p>\n" +
                "<p><label><b>Tasa de inter&eacute;s: </b></label>\n" +
                "<label>"+financiamiento[8]+"%</label>\n" +
                "</p>\n" +
                "<p><label><b>Valor de la Cuota: </b></label>\n" +
                "<label>$"+financiamiento[9]+"</label>\n" +
                "</p>\n" +
                "</div>";
        return mensaje;
    }


}
