package com.example.usuario.venderapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by USUARIO on 05-may-15.
 */
public class DbLote {
    public static final  String NOMBRE_TABLA = "lote";
    public static final  String ID_LOTE = "_id";
    public static final  String MANZANA = "manzana";
    public static final  String NUM_LOTE = "numero_lote";
    public static final  String ESTADO = "estado_construccion";
    public static final  String AREA = "area_terreno";
    public static final  String ENTRADA = "plazo_entrada";
    public static final  String SALIDA = "plazo_salida";
    public static final  String ID_PROY = "proy_id";

    //Para FK
    public static final  String TABLE_FK="proyecto";
    public static final String FK_ID = "_id";

    private DbHelper helper;
    private SQLiteDatabase db;

    public static final String CREATE_TABLE = "create table "+NOMBRE_TABLA+" ("
            + ID_LOTE + " integer primary key,"
            + ID_PROY + " tex not null,"
            + MANZANA + " text not null,"
            + NUM_LOTE + " text not null,"
            + ESTADO + " text not null,"
            + AREA + " text not null,"
            + ENTRADA + " text not null,"
            + SALIDA + " text not null,"
            + " FOREIGN KEY("+ID_PROY+") REFERENCES "+TABLE_FK+"("+FK_ID+"));";

    public DbLote(Context contexto) {
        helper = new DbHelper(contexto);
        db = helper.getWritableDatabase();
    }
    public ContentValues generarContentValues(String id,String id_proy,String manzana, String num_lote, String estado,
                                              String area, String entrada, String salida){
        ContentValues valores = new ContentValues();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        valores.put(ID_LOTE,id);
        valores.put(ID_PROY,id_proy);
        valores.put(MANZANA,manzana);
        valores.put(NUM_LOTE,num_lote);
        valores.put(ESTADO,estado);
        valores.put(AREA,area);
        valores.put(ENTRADA,entrada);
        valores.put(SALIDA,salida);
        return valores;
    }
    public void insertar(String id,String id_proy,String manzana, String num_lote, String estado,
                         String area, String entrada, String salida){
        //insert  into contactos
        db.insert(NOMBRE_TABLA,null,generarContentValues(id,id_proy,manzana,num_lote,estado,area,entrada,salida));
    }
    public Cursor consultar(String id){
        //insert  into contactos

        String[] campos = new String[] {ID_LOTE,MANZANA,NUM_LOTE,ESTADO,AREA,ENTRADA,SALIDA};
        String[] args = new String[] {id};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);
        //System.out.println("id del curso: "+id);
        if(id==null)return db.query(NOMBRE_TABLA, campos, null, null, null, null, null);
        return db.query(NOMBRE_TABLA, campos, ID_LOTE+"=?", args, null, null, null);
    }
    public Cursor consultarLotePorProy(String id){
        //insert  into contactos

        String[] campos = new String[] {ID_LOTE,MANZANA,NUM_LOTE,ENTRADA,SALIDA,AREA,ESTADO};
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
