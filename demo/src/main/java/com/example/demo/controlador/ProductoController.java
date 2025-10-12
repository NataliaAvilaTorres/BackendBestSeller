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


    @DeleteMapping("/depurar/huerfanos")
public CompletableFuture<List<String>> depurarHuerfanos(
        @RequestParam(defaultValue = "false") boolean dryRun) {

    CompletableFuture<List<String>> future = new CompletableFuture<>();
    List<String> eliminados = new ArrayList<>();

    try {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("productos");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot tiendaSnap : snapshot.getChildren()) {
                    for (DataSnapshot prodSnap : tiendaSnap.getChildren()) {
                        // Criterio de "huérfano": si no tiene 'nombre' o 'marca'
                        boolean sinNombre = !prodSnap.hasChild("nombre");
                        boolean sinMarca  = !prodSnap.hasChild("marca");

                        if (sinNombre || sinMarca) {
                            String path = "productos/" + tiendaSnap.getKey() + "/" + prodSnap.getKey();
                            eliminados.add(path);
                            if (!dryRun) {
                                prodSnap.getRef().removeValueAsync();
                            }
                        }
                    }
                }
                if (dryRun) {
                    System.out.println("DryRun: se eliminarían -> " + eliminados);
                }
                future.complete(eliminados);
            }

            @Override public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });
    } catch (Exception e) {
        future.completeExceptionally(e);
    }
    return future;
}


    @DeleteMapping("/eliminar/{id}")
    public CompletableFuture<String> eliminarProducto(@PathVariable String id) {
        CompletableFuture<String> future = new CompletableFuture<>();

        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("productos");

            // ✅ Buscar y eliminar producto sin importar en qué tienda esté
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    boolean eliminado = false;

                    // Recorremos todas las tiendas (productos/{tiendaId}/{productoId})
                    for (DataSnapshot tiendaSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot productoSnapshot : tiendaSnapshot.getChildren()) {
                            Producto producto = productoSnapshot.getValue(Producto.class);

                            if (producto != null && id.equals(producto.getId())) {
                                productoSnapshot.getRef().removeValueAsync();
                                eliminado = true;
                                System.out.println("✅ Producto eliminado: " + producto.getNombre());
                                break;
                            }
                        }
                        if (eliminado)
                            break;
                    }

                    if (eliminado) {
                        future.complete("Producto eliminado correctamente");
                    } else {
                        future.complete("❌ No se encontró el producto con ID: " + id);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    future.completeExceptionally(new RuntimeException("Error al eliminar: " + error.getMessage()));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            future.completeExceptionally(new RuntimeException("Error al eliminar producto: " + e.getMessage()));
        }

        return future;
    }

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
    public CompletableFuture<List<Producto>> listarProductos() {
        CompletableFuture<List<Producto>> future = new CompletableFuture<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("productos");

        ref.limitToFirst(6000).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Producto> lista = new ArrayList<>();

                // ✅ Si tu estructura es: productos/{tiendaId}/{productoId}
                for (DataSnapshot tiendaSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot productoSnapshot : tiendaSnapshot.getChildren()) {
                        Producto p = productoSnapshot.getValue(Producto.class);
                        if (p != null) {
                            lista.add(p);
                            if (lista.size() >= 6000)
                                break;
                        }
                    }
                    if (lista.size() >= 6000)
                        break;
                }

                System.out.println("✅ Productos cargados: " + lista.size());
                future.complete(lista);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("❌ Error al cargar productos: " + error.getMessage());
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });

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
            DatabaseReference ref = database.getReference("productos").child(tiendaId);

            System.out.println("ProductoController: /listar/tienda/" + tiendaId + " called");

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        List<Producto> lista = new ArrayList<>();

                        if (!snapshot.exists()) {
                            System.out.println(
                                    "ProductoController: No se encontraron productos para la tienda " + tiendaId);
                            future.complete(lista);
                            return;
                        }

                        // Recorre cada producto dentro del nodo de la tienda
                        for (DataSnapshot productoSnapshot : snapshot.getChildren()) {
                            Producto p = productoSnapshot.getValue(Producto.class);
                            if (p != null) {
                                lista.add(p);
                                System.out.println("Producto cargado: ID=" + p.getId() + ", nombre=" + p.getNombre());
                            } else {
                                System.err.println("ProductoController: productoSnapshot devolvió null");
                            }
                        }

                        future.complete(lista);

                    } catch (Exception e) {
                        System.err.println("ProductoController: Error en onDataChange tiendaId: " + e.getMessage());
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