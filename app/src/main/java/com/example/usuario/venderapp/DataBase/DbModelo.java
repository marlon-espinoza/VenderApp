package com.example.usuario.venderapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by USUARIO on 06-may-15.
 */
public class DbModelo {
    public static final  String NOMBRE_TABLA = "modelo";
    public static final  String ID_MODELO = "_id";
    public static final  String MODELO = "nombre";
    public static final  String AREA_CONST = "area_const";
    public static final  String PISOS = "pisos";
    public static final  String CUOTA_ENTRADA = "cuota_entrada";
    public static final  String CUOTA_INICIAL = "cuota_inicial";
    public static final  String TASA = "tasa";
    public static final  String PLAZO_1 = "plazo_1";
    public static final  String PLAZO_2 = "plazo_2";
    public static final  String PLAZO_3 = "plazo_3";
    public static final  String PRECIO = "precio";
    public static final  String IMAGEN_FACHADA = "img_fachada";
    public static final  String IMAGEN_PLANTA_BAJA = "img_pb";
    public static final  String IMAGEN_PLANTA_ALTA_1 = "img_pa_1";
    public static final  String IMAGEN_PLANTA_ALTA_2 = "img_pa_2";
    public static final  String ID_LOTE = "lote_id";



    //Para FK
    public static final  String TABLE_FK="lote";
    public static final String FK_ID = "_id";

    private DbHelper helper;
    private SQLiteDatabase db;

    /*public static final String CREATE_TABLE = "create table "+NOMBRE_TABLA+" ("
            + ID_LOTE + " integer primary key,"
            + ID_PROY + " tex not null,"
            + MANZANA + " text not null,"
            + NUM_LOTE + " text not null,"
            + ESTADO + " text not null,"
            + AREA + " text not null,"
            + ENTRADA + " text not null,"
            + ENTREGA + " text not null,"
            + " FOREIGN KEY("+ID_PROY+") REFERENCES "+TABLE_FK+"("+FK_ID+"));";
*/

    public static final String CREATE_TABLE = "create table "+NOMBRE_TABLA+" ("
            + ID_MODELO + " integer primary key autoincrement,"
            + MODELO + " text not null,"
            + ID_LOTE + " tex not null,"
            + AREA_CONST + " text not null,"
            + PISOS + " text not null,"
            + CUOTA_ENTRADA + " text not null,"
            + CUOTA_INICIAL + " text not null,"
            + TASA + " text not null,"
            + PLAZO_1 + " text not null,"
            + PLAZO_2 + " text not null,"
            + PLAZO_3 + " text not null,"
            + PRECIO + " text not null,"
            + IMAGEN_FACHADA + " text null,"
            + IMAGEN_PLANTA_BAJA + " text null,"
            + IMAGEN_PLANTA_ALTA_1 + " text null,"
            + IMAGEN_PLANTA_ALTA_2 + " text null,"
            + " FOREIGN KEY("+ID_LOTE+") REFERENCES "+TABLE_FK+"("+FK_ID+"));";

    public DbModelo(Context contexto) {
        helper = new DbHelper(contexto);
        db = helper.getWritableDatabase();
    }
    public ContentValues generarContentValues(String modelo,String id_lote,String area,String pisos, String cuota_ent, String cuota_ini,
                                              String tasa, String plazo1, String plazo2,String plazo3,
                                              String precio, String img_fach,String img_pb,String img_pa1,String img_pa2){
        ContentValues valores = new ContentValues();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        valores.put(MODELO,modelo);
        valores.put(ID_LOTE,id_lote);
        valores.put(AREA_CONST,area);
        valores.put(PISOS,pisos);
        valores.put(CUOTA_ENTRADA,cuota_ent);
        valores.put(CUOTA_INICIAL,cuota_ini);
        valores.put(TASA,tasa);
        valores.put(PLAZO_1,plazo1);
        valores.put(PLAZO_2,plazo2);
        valores.put(PLAZO_3,plazo3);
        valores.put(PRECIO,precio);
        valores.put(IMAGEN_FACHADA,img_fach);
        valores.put(IMAGEN_PLANTA_BAJA,img_pb);
        valores.put(IMAGEN_PLANTA_ALTA_1,img_pa1);
        valores.put(IMAGEN_PLANTA_ALTA_2,img_pa2);
        return valores;
    }
    public void insertar(String modelo,String id_lote,String area,String pisos, String cuota_ent, String cuota_ini,
                         String tasa, String plazo1, String plazo2,String plazo3,
                         String precio, String img_fach,String img_pb,String img_pa1,String img_pa2){
        //insert  into contactos
        db.insert(NOMBRE_TABLA,null,generarContentValues(modelo,id_lote,area,pisos,cuota_ent,cuota_ini,tasa,plazo1,
                plazo2,plazo3,precio,img_fach,img_pb,img_pa1,img_pa2));
    }
    public Cursor consultar(String id_lote){
        //insert  into contactos

        String[] campos = new String[] {ID_MODELO,MODELO,AREA_CONST,PISOS,CUOTA_ENTRADA,CUOTA_INICIAL,TASA,PLAZO_1,
                                        PLAZO_2,PLAZO_3,PRECIO,IMAGEN_FACHADA,IMAGEN_PLANTA_BAJA,IMAGEN_PLANTA_ALTA_1,IMAGEN_PLANTA_ALTA_2};
        String[] args = new String[] {id_lote};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);
        //System.out.println("id del curso: "+id);

        return db.query(NOMBRE_TABLA, campos, ID_LOTE+"=?", args, null, null, null);
    }
    public Cursor consultarModelo(String id_modelo){
        //insert  into contactos

        String[] campos = new String[] {MODELO,AREA_CONST,PISOS,CUOTA_ENTRADA,CUOTA_INICIAL,TASA,PLAZO_1,
                PLAZO_2,PLAZO_3,PRECIO,IMAGEN_FACHADA,IMAGEN_PLANTA_BAJA,IMAGEN_PLANTA_ALTA_1,IMAGEN_PLANTA_ALTA_2,ID_LOTE};
        String[] args = new String[] {id_modelo};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);
        //System.out.println("id del curso: "+id);

        return db.query(NOMBRE_TABLA, campos, ID_MODELO+"=?", args, null, null, null);
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
