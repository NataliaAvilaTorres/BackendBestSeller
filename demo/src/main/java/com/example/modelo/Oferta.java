package com.example.modelo;


public class Oferta {
    private String nombreOferta;
    private String descripcionOferta;
    private String tiendaNombre;
    private Long fechaOferta;
    private Producto producto;
    private String urlImagen;

    public Oferta() {}

    public Oferta(String nombreOferta, String descripcionOferta, String tiendaNombre, Long fechaOferta, Producto producto, String urlImagen) {
        this.nombreOferta = nombreOferta;
        this.descripcionOferta = descripcionOferta;
        this.tiendaNombre = tiendaNombre;
        this.fechaOferta = fechaOferta;
        this.producto = producto;
        this.urlImagen = urlImagen;
    }

    public String getTiendaNombre() {
        return tiendaNombre;
    }

    public void setTiendaNombre(String tiendaNombre) {
        this.tiendaNombre = tiendaNombre;
    }
    
    public String getNombreOferta() {
        return nombreOferta;
    }

    public void setNombreOferta(String nombreOferta) {
        this.nombreOferta = nombreOferta;
    }

    public String getDescripcionOferta() {
        return descripcionOferta;
    }

    public void setDescripcionOferta(String descripcionOferta) {
        this.descripcionOferta = descripcionOferta;
    }

    public Long getFechaOferta() {
        return fechaOferta;
    }

    public void setFechaOferta(Long fechaOferta) {
        this.fechaOferta = fechaOferta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

}