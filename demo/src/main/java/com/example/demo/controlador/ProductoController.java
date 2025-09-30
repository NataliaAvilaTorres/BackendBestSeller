package com.example.demo.controlador;

import com.example.modelo.Producto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @PostMapping("/crear/{usuarioId}")
    public CompletableFuture<String> crearProducto(@PathVariable String usuarioId, @RequestBody Producto producto) {
        CompletableFuture<String> future = new CompletableFuture<>();
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("productos");

            String id = ref.push().getKey();
            producto.setId(id);
            producto.setUsuarioId(usuarioId); 

            ref.child(id).setValueAsync(producto);
            future.complete("Producto creado correctamente");
        } catch (Exception e) {
            future.complete("Error al crear producto: " + e.getMessage());
        }
        return future;
    }


    @GetMapping("/listar")
    public CompletableFuture<Iterable<Producto>> listarProductos() {
        CompletableFuture<Iterable<Producto>> future = new CompletableFuture<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("productos");

        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                List<Producto> lista = new ArrayList<>();
                for (com.google.firebase.database.DataSnapshot child : snapshot.getChildren()) {
                    Producto p = child.getValue(Producto.class);
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
    @GetMapping("/listar/{usuarioId}")
    public CompletableFuture<List<Producto>> listarProductosUsuario(@PathVariable String usuarioId) {
        CompletableFuture<List<Producto>> future = new CompletableFuture<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("productos");

        ref.orderByChild("usuarioId").equalTo(usuarioId)
            .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                    List<Producto> lista = new ArrayList<>();
                    for (com.google.firebase.database.DataSnapshot child : snapshot.getChildren()) {
                        Producto p = child.getValue(Producto.class);
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

}