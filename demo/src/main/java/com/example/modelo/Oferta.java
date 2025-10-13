package com.example.modelo;

import java.util.HashMap;
import java.util.Map;

public class Oferta {
    private String id;
    private String nombreOferta;
    private String descripcionOferta;
    private String tiendaId;
    private Long fechaOferta;
    private Long fechaFinal;
    private int likes;
    private boolean likedByUser;
    private String usuarioId;
    private String productoId;
    private Ubicacion ubicacion;
    private Map<String, Boolean> likedBy = new HashMap<>();

    // Constructor por defecto
    public Oferta() {
    }

    // Constructor con parámetros
    public Oferta(String id, String nombreOferta, String descripcionOferta, String tiendaId, Long fechaOferta,
            Long fechaFinal, int likes, boolean likedByUser, String usuarioId, String productoId, Ubicacion ubicacion) {
        this.id = id;
        this.nombreOferta = nombreOferta;
        this.descripcionOferta = descripcionOferta;
        this.tiendaId = tiendaId;
        this.fechaOferta = fechaOferta;
        this.fechaFinal = fechaFinal;
        this.likes = likes;
        this.likedByUser = likedByUser;
        this.usuarioId = usuarioId;
        this.productoId = productoId;
        this.likedBy = new HashMap<>();
        this.ubicacion = ubicacion;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Long getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Long fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getTiendaId() {
        return tiendaId;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    public Map<String, Boolean> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(Map<String, Boolean> likedBy) {
        this.likedBy = likedBy;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
}
