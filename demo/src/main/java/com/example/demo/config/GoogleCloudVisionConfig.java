package com.example.demo.config;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;

@Configuration
public class GoogleCloudVisionConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(GoogleCloudVisionConfig.class);
    
    @Bean
    public ImageAnnotatorClient imageAnnotatorClient() throws IOException {
        try {
            logger.info("Inicializando Google Cloud Vision Client...");
            
            // Cargar credenciales desde classpath
            InputStream credentialsStream = getClass().getClassLoader()
                .getResourceAsStream("best-seller-382e9-6c42a0df123c.json");
            
            if (credentialsStream == null) {
                throw new IOException("No se encontró el archivo de credenciales en resources");
            }
            
            ServiceAccountCredentials credentials = ServiceAccountCredentials
                .fromStream(credentialsStream);
            
            logger.info("Credenciales cargadas correctamente");
            
            // Crear settings con las credenciales
            ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(() -> credentials)
                .build();
            
            ImageAnnotatorClient client = ImageAnnotatorClient.create(settings);
            logger.info("Google Cloud Vision Client inicializado correctamente");
            
            return client;
            
        } catch (IOException e) {
            logger.error("Error al crear ImageAnnotatorClient: {}", e.getMessage());
            throw new IOException("No se pudo conectar con Google Cloud Vision API. " +
                "Asegúrate de tener el archivo de credenciales en src/main/resources/", e);
        }
    }
}