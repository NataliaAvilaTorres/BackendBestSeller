package com.example.demo.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.modelo.Notificacion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    // Listar todas las notificaciones
    // localhost:8080/api/notificaciones/listar
    @GetMapping("/listar")
    public CompletableFuture<List<Notificacion>> listarNotificaciones() {
        CompletableFuture<List<Notificacion>> future = new CompletableFuture<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("notificaciones");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Notificacion> lista = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Notificacion n = child.getValue(Notificacion.class);
                    lista.add(n);
                }
                future.complete(lista);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });

        return future;
    }
}