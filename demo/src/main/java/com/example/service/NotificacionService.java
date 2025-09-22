package com.example.service;

import org.springframework.stereotype.Service;

import com.example.modelo.Notificacion;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Service
public class NotificacionService {

    private final DatabaseReference notificacionesRef;

    public NotificacionService() {
        this.notificacionesRef = FirebaseDatabase.getInstance().getReference("notificaciones");
    }

    public void crearNotificacion(Notificacion notificacion) {
        String id = notificacionesRef.push().getKey();
        notificacion.setId(id);
        notificacionesRef.child(id).setValueAsync(notificacion);
    }
}
