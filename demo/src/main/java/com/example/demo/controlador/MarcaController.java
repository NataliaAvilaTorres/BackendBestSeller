package com.example.demo.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.modelo.Marca;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    // Listar todas las marcas
    // localhost:8080/api/marcas/listar
    @GetMapping("/listar")
    public CompletableFuture<List<Marca>> listarMarcas() {
        CompletableFuture<List<Marca>> future = new CompletableFuture<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("marcas");

        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                List<Marca> lista = new ArrayList<>();
                for (com.google.firebase.database.DataSnapshot child : snapshot.getChildren()) {
                    Marca marca = child.getValue(Marca.class);
                    lista.add(marca);
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
}
