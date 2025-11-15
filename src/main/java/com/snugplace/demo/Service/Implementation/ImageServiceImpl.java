package com.snugplace.demo.Service.Implementation;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.snugplace.demo.Service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    // Constantes para las carpetas
    private static final String PROFILES_FOLDER = "snugplace/profiles";
    private static final String ACCOMMODATIONS_FOLDER = "snugplace/accommodations";
    private static final int MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_FORMATS = {"jpg", "jpeg", "png", "webp"};

    @Override
    public Map uploadProfileImage(MultipartFile image) throws Exception {
        System.out.println("👤 Subiendo imagen de perfil a Cloudinary");
        return uploadImage(image, PROFILES_FOLDER, createProfileTransformation());
    }

    @Override
    public List<Map> uploadAccommodationImages(List<MultipartFile> images) throws Exception {
        if (images == null || images.isEmpty()) {
            throw new Exception("Debe proporcionar al menos una imagen");
        }

        if (images.size() > 10) {
            throw new Exception("No se pueden subir más de 10 imágenes");
        }

        System.out.println("🏠 Subiendo " + images.size() + " imágenes de alojamiento a Cloudinary");

        List<Map> uploadResults = new ArrayList<>();
        List<File> tempFiles = new ArrayList<>();

        try {
            for (MultipartFile image : images) {
                // Validar cada imagen
                validateImage(image);

                // Convertir a File
                File file = convertMultipartFileToFile(image);
                tempFiles.add(file);

                // Subir a Cloudinary en la carpeta de accommodations
                Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap(
                        "folder", ACCOMMODATIONS_FOLDER,
                        "resource_type", "image",
                        "transformation", createAccommodationTransformation()
                ));

                uploadResults.add(uploadResult);
                System.out.println("✅ Imagen de alojamiento subida: " + uploadResult.get("url"));
            }

            System.out.println("🎉 Todas las imágenes de alojamiento subidas exitosamente");
            return uploadResults;

        } catch (Exception e) {
            System.out.println("❌ Error subiendo imágenes de alojamiento: " + e.getMessage());

            // Si hay error, eliminar las imágenes ya subidas
            for (Map result : uploadResults) {
                try {
                    String publicId = (String) result.get("public_id");
                    if (publicId != null) {
                        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                        System.out.println("🗑️ Imagen eliminada por error: " + publicId);
                    }
                } catch (Exception cleanupException) {
                    System.out.println("⚠️ Error limpiando imagen: " + cleanupException.getMessage());
                }
            }
            throw new Exception("Error al subir imágenes de alojamiento: " + e.getMessage());

        } finally {
            // Limpiar archivos temporales
            for (File file : tempFiles) {
                if (file.exists()) {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        System.out.println("⚠️ No se pudo eliminar archivo temporal: " + file.getName());
                    }
                }
            }
        }
    }

    @Override
    public Map uploadAccommodationImage(MultipartFile image) throws Exception {
        System.out.println("🏠 Subiendo imagen de alojamiento a Cloudinary");
        return uploadImage(image, ACCOMMODATIONS_FOLDER, createAccommodationTransformation());
    }

    /**
     * Método genérico para subir imágenes
     */
    private Map uploadImage(MultipartFile image, String folder, com.cloudinary.Transformation transformation) throws Exception {
        // Validaciones
        validateImage(image);

        System.out.println("📁 Procesando imagen: " + image.getOriginalFilename() +
                " (" + image.getSize() + " bytes) para carpeta: " + folder);

        // Convertir MultipartFile a File
        File file = convertMultipartFileToFile(image);

        try {
            // Configuración para Cloudinary
            Map<String, Object> uploadOptions = new HashMap<>();
            uploadOptions.put("folder", folder);
            uploadOptions.put("resource_type", "image");
            uploadOptions.put("transformation", transformation);

            System.out.println("☁️ Subiendo a Cloudinary con opciones: " + uploadOptions);

            // Subir a Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file, uploadOptions);

            // Validar respuesta de Cloudinary
            if (uploadResult == null || uploadResult.get("url") == null) {
                throw new Exception("Respuesta inválida de Cloudinary");
            }

            System.out.println("✅ Imagen subida exitosamente:");
            System.out.println("   📍 URL: " + uploadResult.get("url"));
            System.out.println("   🆔 Public ID: " + uploadResult.get("public_id"));
            System.out.println("   📏 Tamaño: " + uploadResult.get("bytes"));
            System.out.println("   📐 Formato: " + uploadResult.get("format"));

            return uploadResult;

        } catch (Exception e) {
            System.out.println("❌ Error subiendo imagen a Cloudinary: " + e.getMessage());
            throw new Exception("Error al subir imagen a Cloudinary: " + e.getMessage());
        } finally {
            // Limpiar archivo temporal
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("🧹 Archivo temporal eliminado: " + file.getName());
                } else {
                    System.out.println("⚠️ No se pudo eliminar archivo temporal: " + file.getName());
                }
            }
        }
    }

    /**
     * Crear transformación para imágenes de perfil
     */
    private Transformation createProfileTransformation() {
        return new Transformation()
                .width(400)
                .height(400)
                .crop("fill")
                .gravity("face")
                .quality("auto")
                .fetchFormat("jpg");
    }

    /**
     * Crear transformación para imágenes de alojamiento
     */
    private com.cloudinary.Transformation createAccommodationTransformation() {
        return new com.cloudinary.Transformation()
                .width(800)
                .height(600)
                .crop("fill")
                .quality("auto")
                .fetchFormat("jpg");
    }

    @Override
    public Map delete(String publicId) throws Exception {
        if (publicId == null || publicId.trim().isEmpty()) {
            throw new Exception("El public_id es obligatorio");
        }

        try {
            System.out.println("🗑️ Eliminando imagen de Cloudinary: " + publicId);

            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            String resultStatus = (String) result.get("result");
            System.out.println("📋 Resultado de eliminación: " + resultStatus);

            if ("ok".equals(resultStatus)) {
                System.out.println("✅ Imagen eliminada exitosamente: " + publicId);
            } else if ("not found".equals(resultStatus)) {
                System.out.println("⚠️ Imagen no encontrada en Cloudinary: " + publicId);
            } else {
                throw new Exception("Cloudinary retornó estado inesperado: " + resultStatus);
            }

            return result;

        } catch (Exception e) {
            System.out.println("❌ Error eliminando imagen de Cloudinary: " + e.getMessage());
            throw new Exception("Error al eliminar imagen de Cloudinary: " + e.getMessage());
        }
    }

    @Override
    public void deleteMultiple(List<String> publicIds) throws Exception {
        if (publicIds == null || publicIds.isEmpty()) {
            System.out.println("ℹ️ No hay imágenes para eliminar");
            return;
        }

        System.out.println("🗑️ Eliminando " + publicIds.size() + " imágenes de Cloudinary");

        List<String> errors = new ArrayList<>();
        int successCount = 0;

        for (String publicId : publicIds) {
            try {
                delete(publicId);
                successCount++;
            } catch (Exception e) {
                String errorMsg = "Error eliminando " + publicId + ": " + e.getMessage();
                errors.add(errorMsg);
                System.out.println("❌ " + errorMsg);
            }
        }

        System.out.println("📊 Resultado eliminación múltiple:");
        System.out.println("   ✅ Éxitos: " + successCount);
        System.out.println("   ❌ Errores: " + errors.size());

        if (!errors.isEmpty()) {
            throw new Exception("Algunos archivos no se pudieron eliminar: " + String.join(", ", errors));
        }
    }

    /**
     * Valida que la imagen cumpla con los requisitos
     */
    private void validateImage(MultipartFile image) throws Exception {
        // Verificar que no sea null
        if (image == null || image.isEmpty()) {
            throw new Exception("La imagen es obligatoria");
        }

        // Verificar tamaño
        if (image.getSize() > MAX_IMAGE_SIZE) {
            throw new Exception("La imagen no debe superar los 5MB. Tamaño actual: " +
                    (image.getSize() / 1024 / 1024) + "MB");
        }

        // Verificar que tenga nombre
        String originalFilename = image.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new Exception("Nombre de archivo inválido");
        }

        // Verificar formato por extensión
        String extension = getFileExtension(originalFilename).toLowerCase();
        boolean isValidFormat = false;
        for (String format : ALLOWED_FORMATS) {
            if (format.equals(extension)) {
                isValidFormat = true;
                break;
            }
        }

        if (!isValidFormat) {
            throw new Exception("Formato de imagen no permitido: " + extension +
                    ". Formatos permitidos: " + String.join(", ", ALLOWED_FORMATS));
        }

        // Verificar content type
        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new Exception("El archivo debe ser una imagen válida. Tipo detectado: " + contentType);
        }

        System.out.println("✅ Imagen validada: " + originalFilename +
                " (" + contentType + ", " + (image.getSize() / 1024) + " KB)");
    }

    /**
     * Convierte un MultipartFile a File temporal
     */
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = Objects.requireNonNull(multipartFile.getOriginalFilename());
        String extension = getFileExtension(originalFilename);

        // Crear archivo temporal con prefijo y extensión
        File file = File.createTempFile("snugplace-upload-", "." + extension);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        System.out.println("📄 Archivo temporal creado: " + file.getAbsolutePath() +
                " (" + file.length() + " bytes)");

        return file;
    }

    /**
     * Obtiene la extensión de un archivo
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return "jpg"; // Extensión por defecto
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "jpg"; // Extensión por defecto si no hay extensión
        }

        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * Método auxiliar para obtener información de una imagen sin subirla
     */
    public Map<String, Object> getImageInfo(MultipartFile image) throws Exception {
        validateImage(image);

        Map<String, Object> info = new HashMap<>();
        info.put("filename", image.getOriginalFilename());
        info.put("size", image.getSize());
        info.put("contentType", image.getContentType());
        info.put("extension", getFileExtension(image.getOriginalFilename()));

        return info;
    }

    /**
     * Verifica la conexión con Cloudinary
     */
    public boolean testCloudinaryConnection() {
        try {
            // Intentar una operación simple con Cloudinary
            cloudinary.uploader().explicit("test", ObjectUtils.asMap(
                    "type", "upload"
            ));
            System.out.println("✅ Conexión con Cloudinary: OK");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Conexión con Cloudinary: ERROR - " + e.getMessage());
            return false;
        }
    }
}