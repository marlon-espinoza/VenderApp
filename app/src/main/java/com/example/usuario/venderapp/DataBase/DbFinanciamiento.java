package com.example.usuario.venderapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by USUARIO on 18-jun-15.
 */
public class DbFinanciamiento {
    public static final  String NOMBRE_TABLA = "financiamiento";
    public static final  String ID_FINANCIAMIENTO = "_id";
    public static final  String NOMBRE_MODELO = "nombre_modelo";
    public static final  String PRECIO = "precio";
    public static final  String ENTRADA = "entrada";
    public static final  String PORCENTAJE_ENTRADA = "porcentaje_entrada";
    public static final  String CUOTA_INICIAL = "cuota_inicial";
    public static final  String PORCENTAJE_CUOTA_INICIAL = "porcentaje_cuota_inicial";
    public static final  String NUM_PAGOS_ENTRADA = "num_pagos_entrada";
    public static final  String CUOTA_ENTRADA = "cuota_entrada";
    public static final  String SALDO = "saldo";
    public static final  String TASA_INTERES = "tasa_interes";
    public static final  String NUM_PAGOS_SALDO = "num_pagos_saldo";
    public static final  String CLIENTE = "cliente";
    public static final  String ID_MODELO = "modelo_id";
    public static final  String FECHA = "fecha_financiamiento";




    //Para FK
    public static final  String TABLE_FK="modelo";
    public static final String FK_ID = "_id";

    private DbHelper helper;
    private SQLiteDatabase db;

    public static final String CREATE_TABLE = "create table "+NOMBRE_TABLA+" ("
            + ID_FINANCIAMIENTO + " integer primary key autoincrement,"
            + NOMBRE_MODELO + " tex not null,"
            + PRECIO + " text not null,"
            + ENTRADA + " text not null,"
            + PORCENTAJE_ENTRADA + " text not null,"
            + CUOTA_INICIAL + " text not null,"
            + PORCENTAJE_CUOTA_INICIAL + " text not null,"
            + NUM_PAGOS_ENTRADA + " text not null,"
            + CUOTA_ENTRADA + " text not null,"
            + SALDO + " text not null,"
            + TASA_INTERES + " text not null,"
            + NUM_PAGOS_SALDO + " text not null,"
            + CLIENTE + " text not null,"
            + FECHA +" TIMESTAMP NOT NULL,"
            + " FOREIGN KEY("+ID_MODELO+") REFERENCES "+TABLE_FK+"("+FK_ID+"));";

    public DbFinanciamiento(Context contexto) {
        helper = new DbHelper(contexto);
        db = helper.getWritableDatabase();
    }
    public ContentValues generarContentValues(String id_modelo,String nombre_modelo,String precio, String entrada,
                                              String porcentaje_entrada,String cuota_inicial, String porcentaje_cuota_inicial,
                                              String num_pagos_entrada,String cuota_entrada,String saldo,String tasa_interes,
                                              String num_pagos_saldo,String cliente, Date fecha){
        ContentValues valores = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        valores.put(ID_MODELO,id_modelo);
        valores.put(NOMBRE_MODELO,nombre_modelo);
        valores.put(PRECIO,precio);
        valores.put(ENTRADA,entrada);
        valores.put(PORCENTAJE_ENTRADA,porcentaje_entrada);
        valores.put(CUOTA_INICIAL,cuota_inicial);
        valores.put(PORCENTAJE_CUOTA_INICIAL,porcentaje_cuota_inicial);
        valores.put(NUM_PAGOS_ENTRADA,num_pagos_entrada);
        valores.put(CUOTA_ENTRADA,cuota_entrada);
        valores.put(SALDO,saldo);
        valores.put(TASA_INTERES,tasa_interes);
        valores.put(NUM_PAGOS_SALDO,num_pagos_saldo);
        valores.put(CLIENTE,cliente);
        valores.put(FECHA,dateFormat.format(fecha));

        return valores;
    }
    public void insertar(String id_modelo,String nombre_modelo,String precio, String entrada,
                         String porcentaje_entrada,String cuota_inicial, String porcentaje_cuota_inicial,
                         String num_pagos_entrada,String cuota_entrada,String saldo,String tasa_interes,
                         String num_pagos_saldo,String cliente, Date fecha){
        //insert  into contactos
        db.insert(NOMBRE_TABLA,null,generarContentValues(id_modelo,nombre_modelo,precio,entrada,porcentaje_entrada,
                cuota_inicial,porcentaje_cuota_inicial,num_pagos_entrada,cuota_entrada,saldo,tasa_interes,num_pagos_saldo,
                cliente,fecha));
    }
    public Cursor consultar(String id){
        //insert  into contactos

        String[] campos = new String[] {ID_FINANCIAMIENTO,ID_MODELO,NOMBRE_MODELO,PRECIO,ENTRADA,PORCENTAJE_ENTRADA,
                CUOTA_INICIAL,PORCENTAJE_CUOTA_INICIAL,NUM_PAGOS_ENTRADA,CUOTA_ENTRADA,SALDO,TASA_INTERES,NUM_PAGOS_SALDO,
                CLIENTE,FECHA};
        String[] args = new String[] {id};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);
        //System.out.println("id del curso: "+id);
        if(id==null)return db.query(NOMBRE_TABLA, campos, null, null, null, null, null);
        return db.query(NOMBRE_TABLA, campos, ID_FINANCIAMIENTO+"=?", args, null, null, null);
    }

    public void vaciar(){
        db.delete(NOMBRE_TABLA,null,null);
    }

    public void close(){
        try {
            if(helper!=null){
                helper.close();
                helper=null;
            }

            if(db!=null){
                db.close();
                db=null;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
