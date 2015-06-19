package com.example.usuario.venderapp.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by USUARIO on 29-ene-15.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ventas_app";
    private static final int DB_SCHEME_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(DbUrbanizacion.CREATE_TABLE);
        //db.execSQL(DbUrbanizacion.CREATE_TABLE);
        db.execSQL(DbProyecto.CREATE_TABLE);
        db.execSQL(DbLote.CREATE_TABLE);
        db.execSQL(DbModelo.CREATE_TABLE);
        db.execSQL(DbFinanciamiento.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
