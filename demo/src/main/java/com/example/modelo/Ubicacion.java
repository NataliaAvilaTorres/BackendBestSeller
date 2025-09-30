package com.example.modelo;

import org.checkerframework.checker.units.qual.s;

public class Ubicacion {
    private double lat;
    private double lng;
    private String direccion; 

    public Ubicacion() {}

    public Ubicacion( double lat, double lng, String direccion) {
        this.lat = lat;
        this.lng = lng;
        this.direccion = direccion;
    }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
