package com.worldsills.turisapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemLugar {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getTipoLugar() {
        return tipoLugar;
    }

    public void setTipoLugar(String tipoLugar) {
        this.tipoLugar = tipoLugar;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("Nombre")
    @Expose
    private String nombre;

    @SerializedName("ubicacion")
    @Expose
    private String ubicacion;

    @SerializedName("descripcioncorta")
    @Expose
    private String descripcionCorta;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    @SerializedName("latitud")
    @Expose
    private double latitud;

    @SerializedName("longitud")
    @Expose
    private double longitud;

    @SerializedName("tipolugar")
    @Expose
    private String tipoLugar;

    @SerializedName("urlimagen")
    @Expose
    private String urlImagen;

}
