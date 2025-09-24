package com.example.modelo;

import org.checkerframework.checker.units.qual.t;

public class Producto {
    private String id;
    private String nombre;
    private String categoria;
    private String marca;
    private double precio;
    private String urlImagen;

    private String usuarioId;

    public Producto() {}

    public Producto(String id,String nombre, String categoria, String marca, double precio, String urlImagen, String usuarioId) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.marca = marca;
        this.precio = precio;
        this.urlImagen = urlImagen;
        this.usuarioId = usuarioId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    @Override
    public String toString() {
        return "Producto{" +
                "nombre='" + nombre + '\'' +
                ", categoria='" + categoria + '\'' +
                ", precio=" + precio +
                ", urlImagen='" + urlImagen + '\'' +
                '}';
    }
    
}
