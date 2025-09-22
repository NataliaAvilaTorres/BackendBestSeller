package com.example.demo.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.modelo.Notificacion;
import com.example.modelo.Oferta;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaController {

    @PostMapping("/crear")
    public CompletableFuture<Respuesta> crearOferta(@RequestBody Oferta oferta) {
        CompletableFuture<Respuesta> future = new CompletableFuture<>();

        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // Guardar el producto
            DatabaseReference productRef = database.getReference("productos");
            String productId = productRef.push().getKey();
            productRef.child(productId).setValueAsync(oferta.getProducto());

            // Guardar la oferta
            DatabaseReference ofertaRef = database.getReference("ofertas");
            String ofertaId = ofertaRef.push().getKey();
            ofertaRef.child(ofertaId).setValueAsync(oferta);

            // 🚀 Guardar la notificación relacionada en "notificaciones"
            DatabaseReference notificacionesRef = database.getReference("notificaciones");
            String notificacionId = notificacionesRef.push().getKey();

            Notificacion notificacion = new Notificacion(
                    notificacionId,                      // id de la notificación
                    oferta.getNombreOferta(),            // usuario o tienda que publica
                    oferta.getDescripcionOferta(),       // mensaje de la notificación
                    System.currentTimeMillis(),          // timestamp
                    ofertaId                             // relación con la oferta
            );

            notificacionesRef.child(notificacionId).setValueAsync(notificacion);

            future.complete(new Respuesta("Oferta, Producto y Notificación guardados correctamente"));
        } catch (Exception e) {
            e.printStackTrace();
            future.complete(new Respuesta("Error al guardar oferta, producto y notificación: " + e.getMessage()));
        }

        return future;
    }

    @PostMapping("/{id}/like")
    public CompletableFuture<Respuesta> toggleLike(@PathVariable String id, @RequestParam boolean liked) {
        CompletableFuture<Respuesta> future = new CompletableFuture<>();
        DatabaseReference ofertaRef = FirebaseDatabase.getInstance()
                .getReference("ofertas")
                .child(id);

        ofertaRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                Oferta oferta = snapshot.getValue(Oferta.class);
                if (oferta != null) {
                    int nuevoLikes = oferta.getLikes();
                    if (liked) nuevoLikes += 1;
                    else nuevoLikes -= 1;

                    oferta.setLikes(nuevoLikes);
                    oferta.setLikedByUser(liked);

                    ofertaRef.setValueAsync(oferta);
                    future.complete(new Respuesta("Like actualizado correctamente"));
                } else {
                    future.complete(new Respuesta("Oferta no encontrada"));
                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });

        return future;
    }

    @GetMapping("/listar")
    public CompletableFuture<Iterable<Oferta>> listarOfertas() {
        CompletableFuture<Iterable<Oferta>> future = new CompletableFuture<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("ofertas");

        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                List<Oferta> lista = new ArrayList<>();
                for (com.google.firebase.database.DataSnapshot child : snapshot.getChildren()) {
                    Oferta p = child.getValue(Oferta.class);
                    lista.add(p);
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

    // ---------- 🚀 Nuevo endpoint para listar notificaciones ----------
    @GetMapping("/notificaciones/listar")
    public CompletableFuture<List<Notificacion>> listarNotificaciones() {
        CompletableFuture<List<Notificacion>> future = new CompletableFuture<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("notificaciones");

        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                List<Notificacion> lista = new ArrayList<>();
                for (com.google.firebase.database.DataSnapshot child : snapshot.getChildren()) {
                    Notificacion n = child.getValue(Notificacion.class);
                    lista.add(n);
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

    // ---------- Clase de respuesta ----------
    public static class Respuesta {
        private String mensaje;

        public Respuesta(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }
}
