package com.example.demo.controlador;

import com.example.modelo.Usuario;
import com.google.api.core.ApiFuture;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    public static class Respuesta {

        private String mensaje;
        private Usuario usuario;

        public Respuesta(String mensaje, Usuario usuario) {
            this.mensaje = mensaje;
            this.usuario = usuario;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
    }

    // Listar todos los usuarios
    // GET -> http://localhost:8080/api/usuarios/listar
    @GetMapping("/listar")
    public CompletableFuture<List<Usuario>> listarUsuarios() {
        CompletableFuture<List<Usuario>> future = new CompletableFuture<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("usuarios");

        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                List<Usuario> listaUsuarios = new ArrayList<>();
                if (snapshot.exists()) {
                    for (com.google.firebase.database.DataSnapshot child : snapshot.getChildren()) {
                        Usuario u = child.getValue(Usuario.class);
                        listaUsuarios.add(u);
                    }
                }
                future.complete(listaUsuarios); // devuelve lista (vacía si no hay)
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                future.completeExceptionally(new RuntimeException("Error Firebase: " + error.getMessage()));
            }
        });

        return future;
    }

    // registrar usuario
    // http://localhost:8080/api/usuarios/registrar
    @PostMapping("/registrar")
    public Respuesta registrarUsuario(@RequestBody Usuario usuario) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("usuarios");

            String idUsuario = ref.push().getKey();
            usuario.setId(idUsuario);

            ApiFuture<Void> future = ref.child(idUsuario).setValueAsync(usuario);

            future.get();

            return new Respuesta("Usuario registrado correctamente", usuario);

        } catch (Exception e) {
            e.printStackTrace();
            return new Respuesta("Usuario registrado correctamente", usuario);
        }
    }

    // login
    // http://localhost:8080/api/usuarios/login
    @PostMapping("/login")
    public CompletableFuture<Respuesta> login(@RequestBody Usuario usuario) {
        CompletableFuture<Respuesta> future = new CompletableFuture<>();

        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("usuarios");

            ref.orderByChild("correo").equalTo(usuario.getCorreo())
                    .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (com.google.firebase.database.DataSnapshot child : snapshot.getChildren()) {
                                    Usuario u = child.getValue(Usuario.class);
                                    if (u != null && u.getContrasena().equals(usuario.getContrasena())) {
                                        // Ahi se devuelve el usuario con el mensaje
                                        future.complete(new Respuesta("Login exitoso", u));
                                        return;
                                    }
                                }
                                future.complete(new Respuesta("Credenciales incorrectas", null));
                            } else {
                                future.complete(new Respuesta("Credenciales incorrectas", null));
                            }
                        }

                        @Override
                        public void onCancelled(com.google.firebase.database.DatabaseError error) {
                            future.complete(new Respuesta("Error Firebase: " + error.getMessage(), null));
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            future.complete(new Respuesta("Error en login: " + e.getMessage(), null));
        }

        return future;
    }

    // actualizar usuario
    // http://localhost:8080/api/usuarios/actualizar/1
    @PutMapping("/actualizar/{id}")
    public Respuesta actualizarUsuario(@PathVariable String id, @RequestBody Usuario usuario) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("usuarios").child(id);

            ApiFuture<Void> future = ref.setValueAsync(usuario);
            future.get();

            return new Respuesta("Usuario actualizado correctamente", usuario);
        } catch (Exception e) {
            e.printStackTrace();
            return new Respuesta("Error al actualizar usuario: " + e.getMessage(), null);
        }
    }

    // eliminar usuario
    // http://localhost:8080/api/usuarios/eliminar/1
    @DeleteMapping("/eliminar/{id}")
    public Respuesta eliminarUsuario(@PathVariable String id) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("usuarios").child(id);

            ApiFuture<Void> future = ref.removeValueAsync();
            future.get();

            return new Respuesta("Usuario eliminado correctamente", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Respuesta("Error al eliminar usuario: " + e.getMessage(), null);
        }
    }
}