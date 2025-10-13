package com.example.modelo;

public class Tienda {
    private String id;
    private String nombre;
    private String urlImagen;
    private Ubicacion ubicacion;

    // Constructor por defecto
    public Tienda() {
    }

    // Constructor con parámetros
    public Tienda(String id, String nombre, String urlImagen, Ubicacion ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.urlImagen = urlImagen;
        this.ubicacion = ubicacion;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
}
