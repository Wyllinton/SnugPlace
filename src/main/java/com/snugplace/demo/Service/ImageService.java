package com.snugplace.demo.Service;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.List;

public interface ImageService {

    /**
     * Sube una imagen de perfil de usuario a Cloudinary
     * @param image archivo de imagen
     * @return Map con url, public_id y otros datos de Cloudinary
     * @throws Exception si hay error en la subida
     */
    Map uploadProfileImage(MultipartFile image) throws Exception;

    /**
     * Sube múltiples imágenes de alojamiento a Cloudinary
     * @param images lista de archivos de imagen
     * @return List de Maps con url, public_id y otros datos de cada imagen
     * @throws Exception si hay error en la subida
     */
    List<Map> uploadAccommodationImages(List<MultipartFile> images) throws Exception;

    /**
     * Sube una sola imagen de alojamiento a Cloudinary
     * @param image archivo de imagen
     * @return Map con url, public_id y otros datos de Cloudinary
     * @throws Exception si hay error en la subida
     */
    Map uploadAccommodationImage(MultipartFile image) throws Exception;

    /**
     * Elimina una imagen de Cloudinary
     * @param publicId el public_id de la imagen en Cloudinary
     * @return Map con resultado de la eliminación
     * @throws Exception si hay error en la eliminación
     */
    Map delete(String publicId) throws Exception;

    /**
     * Elimina múltiples imágenes de Cloudinary
     * @param publicIds lista de public_ids a eliminar
     * @throws Exception si hay error en la eliminación
     */
    void deleteMultiple(List<String> publicIds) throws Exception;
}
