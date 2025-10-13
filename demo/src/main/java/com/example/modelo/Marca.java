package com.example.modelo;

public class Marca {
    private String id;
    private String nombreMarca;
    private String categoria;

    // Constructor sin parámetros
    public Marca() {
    }

    // Constructor con parámetros
    public Marca(String id, String nombreMarca, String categoria) {
        this.id = id;
        this.nombreMarca = nombreMarca;
        this.categoria = categoria;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Marca{id='" + id + "', nombreMarca='" + nombreMarca + "', categoria='" + categoria + "'}";
    }
}
