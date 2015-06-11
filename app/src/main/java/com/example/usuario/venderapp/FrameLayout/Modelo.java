package com.example.usuario.venderapp.FrameLayout;

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

    public Modelo(String id,String modelo,String area,String pisos, String cuota_ent, String cuota_ini,
                  String tasa, String plazo1, String plazo2,String plazo3,
                  String precio, String img_fach,String img_pb,String img_pa1,String img_pa2) {
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

    public static String getCuota_entrada() {
        return cuota_entrada;
    }

    public static String getCuota_inicial() {
        return cuota_inicial;
    }

    public static String getTasa() {
        return tasa;
    }

    public static String getPlazo_1() {
        return plazo_1;
    }

    public static String getPlazo_2() {
        return plazo_2;
    }

    public static String getPlazo_3() {
        return plazo_3;
    }

    public static String getPrecio() {
        return precio;
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
}
