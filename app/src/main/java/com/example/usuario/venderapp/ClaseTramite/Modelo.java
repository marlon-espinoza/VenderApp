package com.example.usuario.venderapp.ClaseTramite;

import java.text.DecimalFormat;

/**
 * Created by USUARIO on 11-jun-15.
 */
public class Modelo {
    public static  String id;
    public static  String nombre_modelo;
    public static  String area_const;
    public static  String pisos;
    public static  String cuota_entrada;
    public static  String cuota_inicial;
    public static  String tasa;
    public static  String plazo_1;
    public static  String plazo_2;
    public static  String plazo_3;
    public static  String precio;
    public static  String img_fachada;
    public static  String img_pb;
    public static  String img_pa_1;
    public static  String img_pa_2;
    public static  String urbanizacion;
    public static  String lote;
    public static  String manzana;
    public static  String id_lote;
    public static  String vender_como;






    public static DecimalFormat decimales = new DecimalFormat("0.00");
    public static DecimalFormat porcentajes = new DecimalFormat("##");


    public Modelo(String id,String modelo,String area,String pisos, String cuota_ent, String cuota_ini,
                  String tasa, String plazo1, String plazo2,String plazo3,
                  String precio, String img_fach,String img_pb,String img_pa1,String img_pa2,
                  String urbanizacion,String lote, String manzana,String id_lote,String vender_como) {
        this.id=id;
        this.nombre_modelo=modelo;
        this.area_const=area;
        this.cuota_entrada=cuota_ent;
        this.cuota_inicial=cuota_ini;
        this.tasa=tasa;
        this.plazo_1=plazo1;
        this.plazo_2=plazo2;
        this.plazo_3=plazo3;
        this.precio=precio;
        this.img_fachada=img_fach;
        this.img_pb=img_pb;
        this.img_pa_1=img_pa1;
        this.img_pa_2=img_pa2;
        this.urbanizacion=urbanizacion;
        this.lote=lote;
        this.manzana=manzana;
        this.id_lote=id_lote;
        this.vender_como=vender_como;
    }

    public static String getId() {
        return id;
    }

    public static String getNombre_modelo() {
        return nombre_modelo;
    }

    public static String getArea_const() {
        return area_const;
    }

    public static String getPisos() {
        return pisos;
    }

    public static Integer getCuota_entrada() {
        int val = (int)Double.parseDouble(cuota_entrada);
        return val;
    }

    public static Integer getCuota_inicial() {
        int val=(int)Double.parseDouble(cuota_inicial);
        return val;
    }

    public static Integer getTasa() {
        int val=(int)Double.parseDouble(tasa);
        return val;
    }

    public static int getPlazo_1() {
        return Integer.parseInt(plazo_1);
    }

    public static int getPlazo_2() {
        return Integer.parseInt(plazo_2);
    }

    public static int getPlazo_3() {
        return Integer.parseInt(plazo_3);
    }

    public static String getPrecio() {
        return (String)decimales.format(Double.parseDouble(precio));
    }

    public static String getImg_fachada() {
        return img_fachada;
    }

    public static String getImg_pb() {
        return img_pb;
    }

    public static String getImg_pa_1() {
        return img_pa_1;
    }

    public static String getImg_pa_2() {
        return img_pa_2;
    }

    public static String getLote() {
        return lote;
    }

    public static String getUrbanizacion() {
        return urbanizacion;
    }

    public static String getManzana() {
        return manzana;
    }

    public static String getId_lote() {
        return id_lote;
    }
    public static String getVender_como() {
        return vender_como;
    }
}
