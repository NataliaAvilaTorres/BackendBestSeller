package com.example.demo.controlador;

import com.example.modelo.Oferta;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaController {

    @PostMapping("/crear")
    public Respuesta crearOferta(@RequestBody Oferta oferta) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("ofertas");

            // generar un ID único en Firebase
            String id = ref.push().getKey();

            // guardar la oferta bajo ese ID
            ref.child(id).setValueAsync(oferta);

            return new Respuesta("Oferta creada correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return new Respuesta("Error al crear oferta: " + e.getMessage());
        }
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
