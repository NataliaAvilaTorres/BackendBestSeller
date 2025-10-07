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

    public static class Respuesta {
        private String mensaje;

        public Respuesta(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }
}