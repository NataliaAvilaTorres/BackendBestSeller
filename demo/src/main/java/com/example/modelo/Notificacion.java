package com.example.modelo;

public class Notificacion {
    private String id;
    private String usuario;
    private String mensaje;
    private Long timestamp;
    private String idOferta;

    public Notificacion() {}

    public Notificacion(String id, String usuario, String mensaje, Long timestamp, String idOferta) {
        this.id = id;
        this.usuario = usuario;
        this.mensaje = mensaje;
        this.timestamp = timestamp;
        this.idOferta = idOferta;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    public String getIdOferta() { return idOferta; }
    public void setIdOferta(String idOferta) { this.idOferta = idOferta; }

    // 🚀 Método auxiliar para compatibilidad con "tiempo"
    public void setTiempo(Long tiempo) {
        this.timestamp = tiempo;
    }
}
