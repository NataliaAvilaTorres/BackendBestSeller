package com.example.modelo;

public class Producto {
    private String id;
    private String nombre;
    private Marca marca;
    private double precio;
    private String urlImagen;

    private String usuarioId;
    private String tiendaId;    

    public Producto() {}

    public Producto(String id,String nombre, Marca marca, double precio, String urlImagen, String usuarioId, String tiendaId) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.urlImagen = urlImagen;
        this.usuarioId = usuarioId;
        this.tiendaId = tiendaId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getTiendaId() { return tiendaId; }
    public void setTiendaId(String tiendaId) { this.tiendaId = tiendaId; }
    @Override
    public String toString() {
        return "Producto{" +
                "nombre='" + nombre + '\'' +
                ", marca='" + marca + '\'' +
                ", precio=" + precio +
                ", urlImagen='" + urlImagen + '\'' +
                '}';
    }
    
}
