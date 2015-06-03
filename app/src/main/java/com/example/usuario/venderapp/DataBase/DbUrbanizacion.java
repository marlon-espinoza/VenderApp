package com.example.usuario.venderapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by USUARIO on 05-may-15.
 */
public class DbUrbanizacion {
    public static final  String NOMBRE_TABLA = "urbanizacion";
    public static final  String ID_URB = "_id";
    public static final  String NOMBRE_URB = "nombre";


    private DbHelper helper;
    private SQLiteDatabase db;

    public static final String CREATE_TABLE = "create table "+NOMBRE_TABLA+" ("
            + ID_URB + " integer primary key,"
            + NOMBRE_URB + " text not null);";

    public DbUrbanizacion(Context contexto) {
        helper = new DbHelper(contexto);
        db = helper.getWritableDatabase();
    }
    public ContentValues generarContentValues(String id,String nombre_urbanizacion){
        ContentValues valores = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        valores.put(ID_URB,id);
        valores.put(NOMBRE_URB,nombre_urbanizacion);

        return valores;
    }
    public void insertar(String id,String nombre_urbanizacion){
        //insert  into contactos
        db.insert(NOMBRE_TABLA,null,generarContentValues(id,nombre_urbanizacion));
    }

    public Cursor consultar(String id){
        //insert  into contactos

        String[] campos = new String[] {NOMBRE_URB};
        String[] args = new String[] {id};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);
        System.out.println("id del curso: "+id);
        if(id==null)return db.query(NOMBRE_TABLA, campos, null, null, null, null, null);
        return db.query(NOMBRE_TABLA, campos, ID_URB+"=?", args, null, null, null);
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
