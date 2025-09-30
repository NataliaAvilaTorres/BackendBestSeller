package com.example.modelo;

import java.util.HashMap;
import java.util.Map;

public class Oferta {
    private String id;
    private String nombreOferta;
    private String descripcionOferta;
    private String tiendaId;
    private Long fechaOferta;
    private Producto producto;
    private String urlImagen;
    private int likes;            
    private boolean likedByUser;   

    private String usuarioId;      
    private String productoId;
    private Ubicacion ubicacion;

    // Se crea el mapa de los usuarios que le han dado like
    private Map<String, Boolean> likedBy = new HashMap<>();

    public Oferta() {}

    public Oferta(String id, String nombreOferta, String descripcionOferta, String tiendaId, Long fechaOferta, Producto producto, String urlImagen, int likes, boolean likedByUser, String usuarioId, String productoId , Ubicacion ubicacion) {
        this.id = id;
        this.nombreOferta = nombreOferta;
        this.descripcionOferta = descripcionOferta;
        this.tiendaId = tiendaId;
        this.fechaOferta = fechaOferta;
        this.producto = producto;
        this.urlImagen = urlImagen;
        this.likes = likes;
        this.likedByUser = likedByUser;
        this.usuarioId = usuarioId;
        this.productoId = productoId;
        this.likedBy = new HashMap<>();
        this.ubicacion = ubicacion;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public void setTiendaId(String tiendaId) {
        this.tiendaId = tiendaId;
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

    public String getTiendaId() {
        return tiendaId;
    }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public boolean isLikedByUser() { return likedByUser; }
    public void setLikedByUser(boolean likedByUser) { this.likedByUser = likedByUser; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getProductoId() { return productoId; }
    public void setProductoId(String productoId) { this.productoId = productoId; }

    public Map<String, Boolean> getLikedBy() { return likedBy; }
    public void setLikedBy(Map<String, Boolean> likedBy) { this.likedBy = likedBy; }

    public Ubicacion getUbicacion() { return ubicacion; }
    public void setUbicacion(Ubicacion ubicacion) { this.ubicacion = ubicacion; }
}
