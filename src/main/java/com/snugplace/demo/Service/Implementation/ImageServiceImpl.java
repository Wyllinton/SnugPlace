package com.snugplace.demo.Service.Implementation;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.snugplace.demo.Service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
        // Validaciones
        validateImage(image);

        // Convertir MultipartFile a File
        File file = convertMultipartFileToFile(image);

        try {
            // Subir a Cloudinary en la carpeta de profiles
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap(
                    "folder", PROFILES_FOLDER,
                    "resource_type", "image",
                    "format", "jpg", // Convertir todas a jpg para consistencia
                    "transformation", new com.cloudinary.Transformation()
                            .width(500)
                            .height(500)
                            .crop("fill")
                            .gravity("face") // Centra en la cara si detecta una
            ));

            // Limpiar archivo temporal
            file.delete();

            return uploadResult;

        } catch (Exception e) {
            // Limpiar archivo temporal en caso de error
            if (file.exists()) {
                file.delete();
            }
            throw new Exception("Error al subir imagen de perfil: " + e.getMessage());
        }
    }

    @Override
    public List<Map> uploadAccommodationImages(List<MultipartFile> images) throws Exception {
        if (images == null || images.isEmpty()) {
            throw new Exception("Debe proporcionar al menos una imagen");
        }

        if (images.size() > 10) {
            throw new Exception("No se pueden subir más de 10 imágenes");
        }

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
                        "transformation", new com.cloudinary.Transformation()
                                .width(1200)
                                .height(800)
                                .crop("fill")
                                .quality("auto")
                ));

                uploadResults.add(uploadResult);
            }

            return uploadResults;

        } catch (Exception e) {
            // Si hay error, eliminar las imágenes ya subidas
            for (Map result : uploadResults) {
                try {
                    String publicId = (String) result.get("public_id");
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                } catch (Exception ignored) {
                    // Ignorar errores al limpiar
                }
            }
            throw new Exception("Error al subir imágenes de alojamiento: " + e.getMessage());

        } finally {
            // Limpiar archivos temporales
            for (File file : tempFiles) {
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    @Override
    public Map uploadAccommodationImage(MultipartFile image) throws Exception {
        // Validaciones
        validateImage(image);

        // Convertir MultipartFile a File
        File file = convertMultipartFileToFile(image);

        try {
            // Subir a Cloudinary en la carpeta de accommodations
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap(
                    "folder", ACCOMMODATIONS_FOLDER,
                    "resource_type", "image",
                    "transformation", new com.cloudinary.Transformation()
                            .width(1200)
                            .height(800)
                            .crop("fill")
                            .quality("auto")
            ));

            // Limpiar archivo temporal
            file.delete();

            return uploadResult;

        } catch (Exception e) {
            // Limpiar archivo temporal en caso de error
            if (file.exists()) {
                file.delete();
            }
            throw new Exception("Error al subir imagen de alojamiento: " + e.getMessage());
        }
    }

    @Override
    public Map delete(String publicId) throws Exception {
        if (publicId == null || publicId.trim().isEmpty()) {
            throw new Exception("El public_id es obligatorio");
        }

        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            String resultStatus = (String) result.get("result");
            if (!"ok".equals(resultStatus) && !"not found".equals(resultStatus)) {
                throw new Exception("Error al eliminar la imagen");
            }

            return result;

        } catch (Exception e) {
            throw new Exception("Error al eliminar imagen: " + e.getMessage());
        }
    }

    @Override
    public void deleteMultiple(List<String> publicIds) throws Exception {
        if (publicIds == null || publicIds.isEmpty()) {
            return;
        }

        List<String> errors = new ArrayList<>();

        for (String publicId : publicIds) {
            try {
                delete(publicId);
            } catch (Exception e) {
                errors.add("Error eliminando " + publicId + ": " + e.getMessage());
            }
        }

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
            throw new Exception("La imagen no debe superar los 5MB");
        }

        // Verificar formato
        String originalFilename = image.getOriginalFilename();
        if (originalFilename == null) {
            throw new Exception("Nombre de archivo inválido");
        }

        String extension = getFileExtension(originalFilename).toLowerCase();
        boolean isValidFormat = false;
        for (String format : ALLOWED_FORMATS) {
            if (format.equals(extension)) {
                isValidFormat = true;
                break;
            }
        }

        if (!isValidFormat) {
            throw new Exception("Formato de imagen no permitido. Use: JPG, JPEG, PNG o WEBP");
        }

        // Verificar content type
        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new Exception("El archivo debe ser una imagen");
        }
    }

    /**
     * Convierte un MultipartFile a File temporal
     */
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = File.createTempFile("upload-", getFileExtension(Objects.requireNonNull(multipartFile.getOriginalFilename())));

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        return file;
    }

    /**
     * Obtiene la extensión de un archivo
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
}