package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.ResponseDTO;
import com.snugplace.demo.Service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
@CrossOrigin(origins = "*")
public class ImageController {

    private final ImageService imageService;

    /**
     * Endpoint único para subir imágenes - compatible con frontend existente
     * POST /images
     * Frontend envía: formData.append('file', file) sin parámetro folder
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Map>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false) String folder) {
        try {
            System.out.println("📸 Subiendo imagen - Folder: " + folder);
            System.out.println("📁 Archivo: " + file.getOriginalFilename() + " (" + file.getSize() + " bytes)");

            Map response;

            if ("accommodations".equals(folder)) {
                // Para alojamientos - usa accommodations
                response = imageService.uploadAccommodationImage(file);
            } else {
                // Por defecto (incluyendo cuando folder es null) - usa profiles
                // Esto mantiene la compatibilidad con el frontend existente de perfiles
                response = imageService.uploadProfileImage(file);
            }

            // Formatear respuesta para que coincida con el frontend
            Map<String, Object> formattedResponse = new HashMap<>();
            formattedResponse.put("url", response.get("url"));
            formattedResponse.put("cloudinaryId", response.get("public_id"));

            System.out.println("✅ Imagen subida exitosamente: " + formattedResponse);

            return ResponseEntity.ok(new ResponseDTO<>(false, formattedResponse));
        } catch (Exception e) {
            System.out.println("❌ Error subiendo imagen: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, errorResponse));
        }
    }

    /**
     * Endpoint para subir múltiples imágenes (mantener para compatibilidad)
     * POST /images/multiple
     */
    @PostMapping(value = "/multiple", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<List<Map<String, Object>>>> uploadMultipleImages(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "folder", required = false) String folder) {
        try {
            System.out.println("📸 Subiendo " + files.size() + " imágenes - Folder: " + folder);

            List<Map> responses;

            if ("accommodations".equals(folder)) {
                responses = imageService.uploadAccommodationImages(files);
            } else {
                // Por defecto sube a profiles (para mantener compatibilidad)
                responses = new ArrayList<>();
                for (MultipartFile file : files) {
                    responses.add(imageService.uploadProfileImage(file));
                }
            }

            // Formatear respuestas
            List<Map<String, Object>> formattedResponses = responses.stream()
                    .map(response -> {
                        Map<String, Object> formatted = new HashMap<>();
                        formatted.put("url", response.get("url"));
                        formatted.put("cloudinaryId", response.get("public_id"));
                        return formatted;
                    })
                    .collect(Collectors.toList());

            System.out.println("✅ " + formattedResponses.size() + " imágenes subidas exitosamente");

            return ResponseEntity.ok(new ResponseDTO<>(false, formattedResponses));
        } catch (Exception e) {
            System.out.println("❌ Error subiendo múltiples imágenes: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, null));
        }
    }

    /**
     * Endpoint específico para imágenes de perfil (mantener para compatibilidad)
     * POST /images/profile
     */
    @PostMapping(value = "/profile", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Map>> uploadProfileImage(
            @RequestParam("file") MultipartFile file) {
        try {
            System.out.println("👤 Subiendo imagen de perfil");
            System.out.println("📁 Archivo: " + file.getOriginalFilename() + " (" + file.getSize() + " bytes)");

            Map response = imageService.uploadProfileImage(file);

            // Formatear respuesta
            Map<String, Object> formattedResponse = new HashMap<>();
            formattedResponse.put("url", response.get("url"));
            formattedResponse.put("cloudinaryId", response.get("public_id"));

            System.out.println("✅ Imagen de perfil subida exitosamente: " + formattedResponse);

            return ResponseEntity.ok(new ResponseDTO<>(false, formattedResponse));
        } catch (Exception e) {
            System.out.println("❌ Error subiendo imagen de perfil: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, errorResponse));
        }
    }

    /**
     * Endpoint específico para imágenes de alojamiento (nuevo)
     * POST /images/accommodation
     */
    @PostMapping(value = "/accommodation", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Map>> uploadAccommodationImage(
            @RequestParam("file") MultipartFile file) {
        try {
            System.out.println("🏠 Subiendo imagen de alojamiento");
            System.out.println("📁 Archivo: " + file.getOriginalFilename() + " (" + file.getSize() + " bytes)");

            Map response = imageService.uploadAccommodationImage(file);

            // Formatear respuesta
            Map<String, Object> formattedResponse = new HashMap<>();
            formattedResponse.put("url", response.get("url"));
            formattedResponse.put("cloudinaryId", response.get("public_id"));

            System.out.println("✅ Imagen de alojamiento subida exitosamente: " + formattedResponse);

            return ResponseEntity.ok(new ResponseDTO<>(false, formattedResponse));
        } catch (Exception e) {
            System.out.println("❌ Error subiendo imagen de alojamiento: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, errorResponse));
        }
    }

    /**
     * Endpoint para eliminar una imagen
     * DELETE /images?id=xxxxx
     */
    @DeleteMapping
    public ResponseEntity<ResponseDTO<String>> deleteImage(
            @RequestParam("id") String cloudinaryId) {
        try {
            System.out.println("🗑️ Eliminando imagen: " + cloudinaryId);
            imageService.delete(cloudinaryId);
            return ResponseEntity.ok(
                    new ResponseDTO<>(false, "Imagen eliminada exitosamente"));
        } catch (Exception e) {
            System.out.println("❌ Error eliminando imagen: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, e.getMessage()));
        }
    }
}