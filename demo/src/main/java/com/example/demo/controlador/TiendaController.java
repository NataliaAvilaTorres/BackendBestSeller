package com.example.demo.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.example.modelo.Tienda;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@RestController
@RequestMapping("/api/tiendas")
public class TiendaController {

    // Crear tienda
    // http://localhost:8080/api/tiendas/crear
    @PostMapping("/crear")
    public CompletableFuture<Respuesta> crearTienda(@RequestBody Tienda tienda) {
        CompletableFuture<Respuesta> future = new CompletableFuture<>();
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference tiendaRef = database.getReference("tiendas");

            // Generar id en Firebase
            String tiendaId = tiendaRef.push().getKey();
            tienda.setId(tiendaId);

            // Guardar tienda
            tiendaRef.child(tiendaId).setValueAsync(tienda);

            future.complete(new Respuesta("Tienda creada correctamente"));
        } catch (Exception e) {
            future.complete(new Respuesta("Error al crear tienda: " + e.getMessage()));
        }
        return future;
    }

    // Listar tiendas
    // http://localhost:8080/api/tiendas/listar
    @GetMapping("/listar")
    public CompletableFuture<List<Tienda>> listarTiendas() {
        CompletableFuture<List<Tienda>> future = new CompletableFuture<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tiendas");

        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                List<Tienda> lista = new ArrayList<>();
                for (com.google.firebase.database.DataSnapshot child : snapshot.getChildren()) {
                    Tienda t = child.getValue(Tienda.class);
                    lista.add(t);
                }
                future.complete(lista);
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });

        return future;
    }

    // Obtener tienda
    // http://localhost:8080/api/tiendas/obtener/1
    @GetMapping("/{id}")
    public CompletableFuture<Tienda> obtenerTienda(@PathVariable String id) {
        CompletableFuture<Tienda> future = new CompletableFuture<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tiendas").child(id);

        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                Tienda tienda = snapshot.getValue(Tienda.class);
                future.complete(tienda);
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });

        return future;
    }

    // Actualizar tienda
    // http://localhost:8080/api/tiendas/actualizar/1
    @PutMapping("/actualizar/{id}")
    public CompletableFuture<Respuesta> actualizarTienda(@PathVariable String id, @RequestBody Tienda tienda) {
        CompletableFuture<Respuesta> future = new CompletableFuture<>();
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tiendas").child(id);
            tienda.setId(id); // conservar id
            ref.setValueAsync(tienda);
            future.complete(new Respuesta("Tienda actualizada correctamente"));
        } catch (Exception e) {
            future.complete(new Respuesta("Error al actualizar tienda: " + e.getMessage()));
        }
        return future;
    }

    // Eliminar tienda
    // http://localhost:8080/api/tiendas/eliminar/1
    @DeleteMapping("/eliminar/{id}")
    public CompletableFuture<Respuesta> eliminarTienda(@PathVariable String id) {
        CompletableFuture<Respuesta> future = new CompletableFuture<>();
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tiendas").child(id);
            ref.removeValueAsync();
            future.complete(new Respuesta("Tienda eliminada correctamente"));
        } catch (Exception e) {
            future.complete(new Respuesta("Error al eliminar tienda: " + e.getMessage()));
        }
        return future;
    }

    // Clase de respuesta
    public static class Respuesta {
        private String mensaje;

        public Respuesta(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
    }
}