package com.example.demo.controlador;


import com.example.modelo.Usuario;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    public static class Respuesta {
        private String mensaje;

        public Respuesta(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
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
}