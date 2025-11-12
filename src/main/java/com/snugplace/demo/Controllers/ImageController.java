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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
@CrossOrigin(origins = "*") // Ajusta según tus necesidades
public class ImageController {

    private final ImageService imageService;

    /**
     * Endpoint para subir imagen de perfil
     * POST /api/images/profile
     */
    @PostMapping(value = "/profile", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Map>> uploadProfileImage(
            @RequestParam("file") MultipartFile image) {
        try {
            Map response = imageService.uploadProfileImage(image);
            return ResponseEntity.ok(new ResponseDTO<>(false, response));
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, errorResponse));
        }
    }

    /**
     * Endpoint para subir múltiples imágenes de alojamiento
     * POST /api/images/accommodation/multiple
     */
    @PostMapping(value = "/accommodation/multiple", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<List<Map>>> uploadAccommodationImages(
            @RequestParam("files") List<MultipartFile> images) {
        try {
            List<Map> response = imageService.uploadAccommodationImages(images);
            return ResponseEntity.ok(new ResponseDTO<>(false, response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, null));
        }
    }

    /**
     * Endpoint para subir una sola imagen de alojamiento
     * POST /api/images/accommodation
     */
    @PostMapping(value = "/accommodation", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Map>> uploadAccommodationImage(
            @RequestParam("file") MultipartFile image) {
        try {
            Map response = imageService.uploadAccommodationImage(image);
            return ResponseEntity.ok(new ResponseDTO<>(false, response));
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, errorResponse));
        }
    }

    /**
     * Endpoint para eliminar una imagen
     * DELETE /api/images?publicId=xxxxx
     */
    @DeleteMapping
    public ResponseEntity<ResponseDTO<String>> deleteImage(
            @RequestParam("publicId") String publicId) {
        try {
            imageService.delete(publicId);
            return ResponseEntity.ok(
                    new ResponseDTO<>(false, "Imagen eliminada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, e.getMessage()));
        }
    }

    /**
     * Endpoint para eliminar múltiples imágenes
     * DELETE /api/images/multiple
     * Body: ["publicId1", "publicId2", ...]
     */
    @DeleteMapping("/multiple")
    public ResponseEntity<ResponseDTO<String>> deleteMultipleImages(
            @RequestBody List<String> publicIds) {
        try {
            imageService.deleteMultiple(publicIds);
            return ResponseEntity.ok(
                    new ResponseDTO<>(false, "Imágenes eliminadas exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, e.getMessage()));
        }
    }
}