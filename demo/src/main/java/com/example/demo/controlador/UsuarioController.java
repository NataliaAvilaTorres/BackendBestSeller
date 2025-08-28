package com.example.demo.controlador;

import com.example.demo.controlador.UsuarioController.Respuesta;
import com.example.modelo.Usuario;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

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

    @PostMapping("/registrar")
    public Respuesta registrarUsuario(@RequestBody Usuario usuario) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("usuarios");
            String idUsuario = ref.push().getKey();
            ref.child(idUsuario).setValueAsync(usuario);
            return new Respuesta("Usuario registrado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return new Respuesta("Error al registrar usuario: " + e.getMessage());
        }
    }

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
                                        future.complete(new Respuesta("Login exitoso"));
                                        return;
                                    }
                                }
                                future.complete(new Respuesta("Credenciales incorrectas"));
                            } else {
                                future.complete(new Respuesta("Credenciales incorrectas"));
                            }
                        }

                        @Override
                        public void onCancelled(com.google.firebase.database.DatabaseError error) {
                            future.complete(new Respuesta("Error Firebase: " + error.getMessage()));
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            future.complete(new Respuesta("Error en login: " + e.getMessage()));
        }

        return future;
    }

}