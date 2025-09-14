package com.example.modelo;

public class Usuario {
    private String id;
    private String nombre;
    private String correo;
    private String ciudad;
    private String contrasena;

    public Usuario() {} 

    public Usuario(String nombre, String correo, String ciudad, String contrasena, String id) {
        this.nombre = nombre;
        this.correo = correo;
        this.ciudad = ciudad;
        this.contrasena = contrasena;
        this.id = id;
    }

    // Getters y Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}