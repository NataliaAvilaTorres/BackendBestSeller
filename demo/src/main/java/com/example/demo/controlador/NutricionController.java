package com.example.demo.controlador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/nutricion")
public class NutricionController {
    
    private static final Logger logger = LoggerFactory.getLogger(NutricionController.class);
    
    @Autowired
    private ImageAnnotatorClient imageAnnotatorClient;
    
    @PostMapping("/analizar")
    public ResponseEntity<?> analyzeNutrition(@RequestParam("image") MultipartFile image) {
        try {
            logger.info("Recibida imagen: {}, tamaño: {} bytes", image.getOriginalFilename(), image.getSize());
            
            byte[] imageBytes = image.getBytes();
            
            Image img = Image.newBuilder()
                .setContent(ByteString.copyFrom(imageBytes))
                .build();

            Feature feat = Feature.newBuilder()
                .setType(Feature.Type.DOCUMENT_TEXT_DETECTION)
                .build();

            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();

            BatchAnnotateImagesRequest batchRequest = BatchAnnotateImagesRequest.newBuilder()
                .addRequests(request)
                .build();

            logger.info("Enviando solicitud a Google Cloud Vision API...");
            BatchAnnotateImagesResponse response = imageAnnotatorClient.batchAnnotateImages(batchRequest);

            if (response.getResponsesCount() > 0) {
                String extractedText = response.getResponses(0).getFullTextAnnotation().getText();
                logger.info("Texto extraído exitosamente, {} caracteres", extractedText.length());
                
                Map<String, Object> analysis = analyzeNutritionText(extractedText);
                logger.info("Análisis completado");
                
                return ResponseEntity.ok(analysis);
            } else {
                logger.warn("No se detectó texto en la imagen");
                return ResponseEntity.status(400).body(Map.of(
                    "error", "No se detectó texto",
                    "mensaje", "La imagen no contiene texto detectável"
                ));
            }
        } catch (Exception e) {
            logger.error("Error procesando imagen: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                "error", "Error procesando imagen",
                "mensaje", e.getMessage()
            ));
        }
    }

    private Map<String, Object> analyzeNutritionText(String text) {
        Map<String, Object> result = new HashMap<>();
        
        Map<String, Integer> nutrientes = new HashMap<>();
        nutrientes.put("calorias", extraerNumero(text, "(?i)(energía|calorías|kcal).*?([0-9]+)"));
        nutrientes.put("proteinas", extraerNumero(text, "(?i)proteína.*?([0-9]+)"));
        nutrientes.put("grasas", extraerNumero(text, "(?i)grasa\\s+total.*?([0-9]+)"));
        nutrientes.put("grasas_saturadas", extraerNumero(text, "(?i)grasa\\s+saturada.*?([0-9]+)"));
        nutrientes.put("carbohidratos", extraerNumero(text, "(?i)carbohidrato.*?([0-9]+)"));
        nutrientes.put("fibra", extraerNumero(text, "(?i)fibra.*?([0-9]+)"));
        nutrientes.put("azucar", extraerNumero(text, "(?i)azúcar.*?([0-9]+)"));
        nutrientes.put("sodio", extraerNumero(text, "(?i)sodio.*?([0-9]+)"));
        
        result.put("nutrientes", nutrientes);
        result.put("evaluacion", generarEvaluacion(nutrientes.getOrDefault("calorias", 0)));
        result.put("recomendaciones", generarRecomendaciones(nutrientes));
        
        return result;
    }

    private int extraerNumero(String text, String patron) {
        try {
            Pattern pattern = Pattern.compile(patron);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(matcher.groupCount()));
            }
        } catch (Exception e) {
            logger.debug("No se encontró coincidencia para el patrón: {}", patron);
        }
        return 0;
    }

    private String generarEvaluacion(int calorias) {
        if (calorias < 100) return "✅ MUY BAJO EN CALORÍAS";
        else if (calorias < 200) return "🟢 BAJO EN CALORÍAS";
        else if (calorias < 400) return "🟡 MODERADO";
        else if (calorias < 600) return "🟠 ALTO EN CALORÍAS";
        else return "🔴 MUY ALTO EN CALORÍAS";
    }

    private List<String> generarRecomendaciones(Map<String, Integer> nutrientes) {
        List<String> recomendaciones = new ArrayList<>();
        if (nutrientes.getOrDefault("azucar", 0) > 10) 
            recomendaciones.add("⚠️ Azúcares altos");
        if (nutrientes.getOrDefault("sodio", 0) > 400) 
            recomendaciones.add("⚠️ Sodio elevado");
        if (nutrientes.getOrDefault("fibra", 0) > 3) 
            recomendaciones.add("✅ Buena fuente de fibra");
        if (nutrientes.getOrDefault("proteinas", 0) > 5) 
            recomendaciones.add("✅ Buena fuente de proteína");
        if (recomendaciones.isEmpty()) 
            recomendaciones.add("Valores moderados en general");
        return recomendaciones;
    }
}