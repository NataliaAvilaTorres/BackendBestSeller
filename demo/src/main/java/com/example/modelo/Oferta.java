package com.example.modelo;

import org.checkerframework.checker.units.qual.t;

public class Oferta {
    private String id;
    private String nombreOferta;
    private String descripcionOferta;
    private String tiendaNombre;
    private Long fechaOferta;
    private Producto producto;
    private String urlImagen;
    private int likes;            
    private boolean likedByUser;   // si el usuario actual ya dio like

    private String usuarioId;      // referencia al usuario creador
    private String productoId;

    public Oferta() {}

    public Oferta(String id, String nombreOferta, String descripcionOferta, String tiendaNombre, Long fechaOferta, Producto producto, String urlImagen, int likes, boolean likedByUser, String usuarioId, String productoId) {
        this.id = id;
        this.nombreOferta = nombreOferta;
        this.descripcionOferta = descripcionOferta;
        this.tiendaNombre = tiendaNombre;
        this.fechaOferta = fechaOferta;
        this.producto = producto;
        this.urlImagen = urlImagen;
        this.likes = likes;
        this.likedByUser = likedByUser;
        this.usuarioId = usuarioId;
        this.productoId = productoId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public String getTiendaNombre() {
    return tiendaNombre;
}



    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public boolean isLikedByUser() { return likedByUser; }
    public void setLikedByUser(boolean likedByUser) { this.likedByUser = likedByUser; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getProductoId() { return productoId; }
    public void setProductoId(String productoId) { this.productoId = productoId; }

}