package com.example.demo.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    // Crear oferta
    // http://localhost:8080/api/ofertas/crear
    @PostMapping("/crear/{usuarioId}")
    public CompletableFuture<Respuesta> crearOferta(@PathVariable String usuarioId, @RequestBody Oferta oferta) {
        CompletableFuture<Respuesta> future = new CompletableFuture<>();
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // Validar que venga el productoId
            if (oferta.getProductoId() == null || oferta.getProductoId().isEmpty()) {
                future.complete(new Respuesta("Error: productoId es requerido"));
                return future;
            }

            // Guardar oferta en Firebase
            DatabaseReference ofertaRef = database.getReference("ofertas");
            String ofertaId = ofertaRef.push().getKey();
            oferta.setId(ofertaId);
            oferta.setUsuarioId(usuarioId);
            ofertaRef.child(ofertaId).setValueAsync(oferta);

            DatabaseReference notificacionesRef = database.getReference("notificaciones");
            String notificacionId = notificacionesRef.push().getKey();
            Notificacion notificacion = new Notificacion(
                    notificacionId,
                    usuarioId,
                    oferta.getDescripcionOferta(),
                    System.currentTimeMillis(),
                    ofertaId);
            notificacionesRef.child(notificacionId).setValueAsync(notificacion);

            future.complete(new Respuesta("Oferta creada correctamente"));
        } catch (Exception e) {
            e.printStackTrace();
            future.complete(new Respuesta("Error al crear oferta: " + e.getMessage()));
        }
        return future;
    }

    // Toggle like
    // http://localhost:8080/api/ofertas/1/like/usuarioId
    @PostMapping("/{id}/like/{usuarioId}")
    public CompletableFuture<Respuesta> toggleLike(
            @PathVariable String id,
            @PathVariable String usuarioId,
            @RequestParam boolean liked) {

        CompletableFuture<Respuesta> future = new CompletableFuture<>();
        DatabaseReference ofertaRef = FirebaseDatabase.getInstance()
                .getReference("ofertas")
                .child(id);

        ofertaRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                Oferta oferta = snapshot.getValue(Oferta.class);
                if (oferta != null) {
                    DatabaseReference likedByRef = ofertaRef.child("likedBy").child(usuarioId);

                    if (liked) {
                        // Agregar el like de este usuario
                        likedByRef.setValueAsync(true);
                    } else {
                        // Quitar el like de este usuario
                        likedByRef.removeValueAsync();
                    }

                    // Recalcular el número total de likes basado en el mapa
                    DatabaseReference likedByMapRef = ofertaRef.child("likedBy");
                    likedByMapRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                            int nuevoLikes = (int) snapshot.getChildrenCount();
                            ofertaRef.child("likes").setValueAsync(nuevoLikes);

                            future.complete(new Respuesta("Like actualizado correctamente"));
                        }

                        @Override
                        public void onCancelled(com.google.firebase.database.DatabaseError error) {
                            future.completeExceptionally(new RuntimeException(error.getMessage()));
                        }
                    });

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

    // Listar ofertas
    // http://localhost:8080/api/ofertas/listar
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

    // Listar notificaciones
    // http://localhost:8080/api/notificaciones/listar
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

    // Listar ofertas de un usuario
    // http://localhost:8080/api/ofertas/listar/usuarioId
    @GetMapping("/listar/{usuarioId}")
    public CompletableFuture<List<Oferta>> listarOfertasUsuario(@PathVariable String usuarioId) {
        CompletableFuture<List<Oferta>> future = new CompletableFuture<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ofertas");

        ref.orderByChild("usuarioId").equalTo(usuarioId)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                        List<Oferta> lista = new ArrayList<>();
                        for (com.google.firebase.database.DataSnapshot child : snapshot.getChildren()) {
                            Oferta o = child.getValue(Oferta.class);
                            lista.add(o);
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

    // Actualizar oferta
    // http://localhost:8080/api/ofertas/actualizar
    @PutMapping("/actualizar/{id}")
    public CompletableFuture<Respuesta> actualizarOferta(@PathVariable String id, @RequestBody Oferta oferta) {
        CompletableFuture<Respuesta> future = new CompletableFuture<>();
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ofertas").child(id);
            oferta.setId(id);
            ref.setValueAsync(oferta);
            future.complete(new Respuesta("Oferta actualizada correctamente"));
        } catch (Exception e) {
            e.printStackTrace();
            future.complete(new Respuesta("Error al actualizar: " + e.getMessage()));
        }
        return future;
    }

    // Eliminar oferta
    // http://localhost:8080/api/ofertas/eliminar
    @DeleteMapping("/eliminar/{id}")
    public CompletableFuture<Respuesta> eliminarOferta(@PathVariable String id) {
        CompletableFuture<Respuesta> future = new CompletableFuture<>();
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ofertas").child(id);
            ref.removeValueAsync();
            future.complete(new Respuesta("Oferta eliminada correctamente"));
        } catch (Exception e) {
            e.printStackTrace();
            future.complete(new Respuesta("Error al eliminar: " + e.getMessage()));
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
