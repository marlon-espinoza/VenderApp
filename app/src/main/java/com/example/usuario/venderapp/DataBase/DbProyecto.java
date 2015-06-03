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
public class DbProyecto {
    public static final  String NOMBRE_TABLA = "proyecto";
    public static final  String ID_PROY = "_id";
    public static final  String NOMBRE_PROY = "nombre";
    public static final  String ID_URB = "urb_id";

    //Para FK
    public static final  String TABLE_FK="urbanizacion";
    public static final String FK_ID = "_id";

    private DbHelper helper;
    private SQLiteDatabase db;

    /*public static final String CREATE_TABLE = "create table "+NOMBRE_TABLA+" ("
            + ID_PROY + " integer primary key,"
            + NOMBRE_PROY + " text not null,"
            + " FOREIGN KEY("+ID_URB+") REFERENCES "+TABLE_FK+"("+FK_ID+"));";*/
    public static final String CREATE_TABLE = "create table "+NOMBRE_TABLA+" ("
            + ID_PROY + " integer primary key,"
            + NOMBRE_PROY + " text not null);";

    public DbProyecto(Context contexto) {
        helper = new DbHelper(contexto);
        db = helper.getWritableDatabase();
    }
    public ContentValues generarContentValues(String id,String nombre_proy){
        ContentValues valores = new ContentValues();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        valores.put(ID_PROY,id);
        //valores.put(ID_URB,id_urb);
        valores.put(NOMBRE_PROY,nombre_proy);
        return valores;
    }
    public void insertar(String id,String nombre_proy){
        //insert  into contactos
        db.insert(NOMBRE_TABLA,null,generarContentValues(id,nombre_proy));
    }
    public Cursor consultar(String id){
        //insert  into contactos

        String[] campos = new String[] {ID_PROY, NOMBRE_PROY};
        String[] args = new String[] {id};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);
        //System.out.println("id del curso: "+id);
        if(id==null)return db.query(NOMBRE_TABLA, campos, null, null, null, null, null);
        return db.query(NOMBRE_TABLA, campos, ID_PROY+"=?", args, null, null, null);
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
