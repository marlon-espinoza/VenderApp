package com.example.usuario.venderapp;

/**
 * Created by USUARIO on 27-abr-15.
 */
public class Urbanizacion {
    private int id;
    private String nombre;
    private String url="http://ciudadceleste.com/Apps/Images/";

    public Urbanizacion(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
    public String ruta(){
        String url;
        url=this.url+nombre+".jpg";
        return url;
    }
}
