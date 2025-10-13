package com.example.modelo;

public class Ubicacion {
    private double lat;
    private double lng;
    private String direccion;

    // Constructor por defecto
    public Ubicacion() {
    }

    // Constructor con parámetros
    public Ubicacion(double lat, double lng, String direccion) {
        this.lat = lat;
        this.lng = lng;
        this.direccion = direccion;
    }

    // Getters y setters
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
