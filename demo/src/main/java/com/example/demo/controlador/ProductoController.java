package com.example.demo.controlador;

import com.example.modelo.Producto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
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
            e.printStackTrace();
            future.complete("Error al crear producto: " + e.getMessage());
        }
        return future;
    }

    @GetMapping("/listar")
    public CompletableFuture<Iterable<Producto>> listarProductos() {
        CompletableFuture<Iterable<Producto>> future = new CompletableFuture<>();
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("productos");

            System.out.println("ProductoController: /listar called");

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        System.out.println(
                                "ProductoController: onDataChange called, snapshot exists: " + snapshot.exists());
                        List<Producto> lista = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            try {
                                Producto p = child.getValue(Producto.class);
                                if (p != null) {
                                    lista.add(p);
                                    System.out
                                            .println("Producto loaded: ID=" + p.getId() + ", nombre=" + p.getNombre());
                                } else {
                                    System.err.println("ProductoController: child returned null Producto");
                                }
                            } catch (Exception e) {
                                System.err.println("ProductoController: Error parsing Producto: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        future.complete(lista);
                    } catch (Exception e) {
                        System.err.println("ProductoController: Error en onDataChange: " + e.getMessage());
                        e.printStackTrace();
                        future.completeExceptionally(e);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    System.err.println("ProductoController: Firebase error: " + error.getMessage());
                    future.completeExceptionally(new RuntimeException(error.getMessage()));
                }
            });
        } catch (Exception e) {
            System.err.println("ProductoController: Error al ejecutar listarProductos: " + e.getMessage());
            e.printStackTrace();
            future.completeExceptionally(e);
        }

        return future;
    }

    @GetMapping("/listar/{usuarioId}")
    public CompletableFuture<List<Producto>> listarProductosUsuario(@PathVariable String usuarioId) {
        CompletableFuture<List<Producto>> future = new CompletableFuture<>();
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("productos");

            System.out.println("ProductoController: /listar/" + usuarioId + " called");

            ref.orderByChild("usuarioId").equalTo(usuarioId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            try {
                                List<Producto> lista = new ArrayList<>();
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    try {
                                        Producto p = child.getValue(Producto.class);
                                        if (p != null) {
                                            lista.add(p);
                                            System.out.println(
                                                    "Producto loaded: ID=" + p.getId() + ", nombre=" + p.getNombre());
                                        } else {
                                            System.err.println("ProductoController: child returned null Producto");
                                        }
                                    } catch (Exception e) {
                                        System.err.println(
                                                "ProductoController: Error parsing Producto: " + e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                                future.complete(lista);
                            } catch (Exception e) {
                                System.err.println(
                                        "ProductoController: Error en onDataChange usuarioId: " + e.getMessage());
                                e.printStackTrace();
                                future.completeExceptionally(e);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            System.err.println("ProductoController: Firebase error usuarioId: " + error.getMessage());
                            future.completeExceptionally(new RuntimeException(error.getMessage()));
                        }
                    });
        } catch (Exception e) {
            System.err.println("ProductoController: Error al ejecutar listarProductosUsuario: " + e.getMessage());
            e.printStackTrace();
            future.completeExceptionally(e);
        }

        return future;
    }

    @GetMapping("/listar/tienda/{tiendaId}")
    public CompletableFuture<List<Producto>> listarProductosTienda(@PathVariable String tiendaId) {
        CompletableFuture<List<Producto>> future = new CompletableFuture<>();
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("productos");

            System.out.println("ProductoController: /listar/tienda/" + tiendaId + " called");

            // Filtramos por tiendaId
            ref.orderByChild("tiendaId").equalTo(tiendaId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            try {
                                List<Producto> lista = new ArrayList<>();
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    try {
                                        Producto p = child.getValue(Producto.class);
                                        if (p != null) {
                                            lista.add(p);
                                            System.out.println(
                                                    "Producto loaded: ID=" + p.getId() + ", nombre=" + p.getNombre());
                                        } else {
                                            System.err.println("ProductoController: child returned null Producto");
                                        }
                                    } catch (Exception e) {
                                        System.err.println(
                                                "ProductoController: Error parsing Producto: " + e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                                future.complete(lista);
                            } catch (Exception e) {
                                System.err.println(
                                        "ProductoController: Error en onDataChange tiendaId: " + e.getMessage());
                                e.printStackTrace();
                                future.completeExceptionally(e);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            System.err.println("ProductoController: Firebase error tiendaId: " + error.getMessage());
                            future.completeExceptionally(new RuntimeException(error.getMessage()));
                        }
                    });
        } catch (Exception e) {
            System.err.println("ProductoController: Error al ejecutar listarProductosTienda: " + e.getMessage());
            e.printStackTrace();
            future.completeExceptionally(e);
        }

        return future;
    }

}
