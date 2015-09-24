package com.example.usuario.venderapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by USUARIO on 21-ago-15.
 */
public class DbParametros {
    public static final  String NOMBRE_TABLA = "parametros";
    public static final  String CODIGO = "codigo";
    public static final  String VALOR = "valor";
    public static final  String ID_ALTERNO = "_id";

    private DbHelper helper;
    private SQLiteDatabase db;

    /*public static final String CREATE_TABLE = "create table "+NOMBRE_TABLA+" ("
            + ID_PROY + " integer primary key,"
            + NOMBRE_PROY + " text not null,"
            + " FOREIGN KEY("+ID_URB+") REFERENCES "+TABLE_FK+"("+FK_ID+"));";*/
    public static final String CREATE_TABLE = "create table "+NOMBRE_TABLA+" ("
            + ID_ALTERNO + " integer primary key,"
            + CODIGO + " text null,"
            + VALOR + " text not null);";

    public DbParametros(Context contexto) {
        helper = new DbHelper(contexto);
        db = helper.getWritableDatabase();
    }
    public ContentValues generarContentValues(String codigo,String valor,String id){
        ContentValues valores = new ContentValues();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        valores.put(CODIGO,codigo);
        valores.put(VALOR,valor);
        valores.put(ID_ALTERNO,id);
        return valores;
    }
    public void insertar(String codigo,String valor,String id){
        //insert  into contactos
        db.insert(NOMBRE_TABLA,null,generarContentValues(codigo,valor,id));
    }
    public Cursor consultar(String id){
        //insert  into contactos

        String[] campos = new String[] {VALOR};
        String[] args = new String[] {id};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);
        //System.out.println("id del curso: "+id);
        if(id==null)return db.query(NOMBRE_TABLA, campos, null, null, null, null, null);
        return db.query(NOMBRE_TABLA, campos, ID_ALTERNO+"=?", args, null, null, null);
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
