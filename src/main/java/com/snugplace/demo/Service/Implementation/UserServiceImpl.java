package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.User.ChangeUserPasswordDTO;
import com.snugplace.demo.DTO.User.CreateUserDTO;
import com.snugplace.demo.DTO.User.UpdateProfileDTO;
import com.snugplace.demo.DTO.User.UserDTO;
import com.snugplace.demo.Exceptions.ResourceNotFoundException;
import com.snugplace.demo.Exceptions.ValueConflictException;
import com.snugplace.demo.Mappers.UserMapper;
import com.snugplace.demo.Model.Enums.UserStatus;
import com.snugplace.demo.Model.User;
import com.snugplace.demo.Repository.UserRepository;
import com.snugplace.demo.Service.ImageService;
import com.snugplace.demo.Service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Override
    public void registerUser(CreateUserDTO createUserDTO) throws Exception {
        if(isEmailDuplicated(createUserDTO.email())){
            throw new ValueConflictException("El correo electrónico ya está en uso.");
        } else {
            User newUser = userMapper.toEntity(createUserDTO);
            newUser.setPassword(encode(createUserDTO.password()));

            // NO guardar la ruta local como profilePhoto - establecer como null
            newUser.setProfilePhoto(null);

            userRepository.save(newUser);
        }
    }

    @Override
    public UserDTO getUserProfile(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        if (!userRepository.existsByEmailAndStatus(email, UserStatus.ACTIVE)) {
            return null;
        }
        Optional<User> user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE);
        return userMapper.toUserDTO(user.get());
    }

    @Override
    public void updateUserProfile(Long id, UpdateProfileDTO updateProfileDTO) throws Exception {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado.");
        }

        User user = userOptional.get();
        user.setName(updateProfileDTO.name());
        user.setPhoneNumber(updateProfileDTO.phoneNumber());
        user.setDescription(updateProfileDTO.description());

        // Solo actualizar la foto de perfil si se proporciona una nueva URL válida de Cloudinary
        if (updateProfileDTO.photoURL() != null &&
                !updateProfileDTO.photoURL().trim().isEmpty() &&
                updateProfileDTO.photoURL().contains("cloudinary")) {
            user.setProfilePhoto(updateProfileDTO.photoURL());
        }

        userRepository.save(user);
    }

    @Override
    public String updateProfileImage(Long userId, MultipartFile imageFile) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado.");
        }

        User user = userOptional.get();

        // Si el usuario ya tiene una foto de perfil en Cloudinary, eliminar la anterior
        if (user.getProfilePhoto() != null &&
                !user.getProfilePhoto().isEmpty() &&
                user.getProfilePhoto().contains("cloudinary")) {
            deleteExistingProfileImage(user.getProfilePhoto());
        }

        // Subir nueva imagen a Cloudinary
        Map<String, Object> uploadResult = imageService.uploadProfileImage(imageFile);

        // Extraer URL segura de Cloudinary
        String imageUrl = extractImageUrlFromUploadResult(uploadResult);

        // Actualizar usuario con nueva URL
        user.setProfilePhoto(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }

    /**
     * Elimina la imagen de perfil existente de Cloudinary
     */
    private void deleteExistingProfileImage(String existingImageUrl) {
        try {
            String publicId = extractPublicIdFromUrl(existingImageUrl);
            if (publicId != null && !publicId.isEmpty()) {
                imageService.delete(publicId);
            }
        } catch (Exception e) {
            // Log el error pero continuar con la nueva subida
            System.err.println("Error eliminando imagen anterior: " + e.getMessage());
        }
    }

    /**
     * Extrae la URL de la imagen del resultado de Cloudinary
     */
    private String extractImageUrlFromUploadResult(Map<String, Object> uploadResult) {
        // Priorizar secure_url (HTTPS)
        String imageUrl = (String) uploadResult.get("secure_url");
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = (String) uploadResult.get("url");
        }

        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new RuntimeException("No se pudo obtener la URL de la imagen desde Cloudinary");
        }

        return imageUrl;
    }

    /**
     * Método auxiliar para extraer public_id de URL de Cloudinary
     */
    private String extractPublicIdFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }

        try {
            // Ejemplo de URL de Cloudinary:
            // https://res.cloudinary.com/demo/image/upload/v1234567/snugplace/profiles/abc123.jpg
            // Extraer: snugplace/profiles/abc123

            String[] parts = imageUrl.split("/upload/");
            if (parts.length > 1) {
                String path = parts[1];

                // Remover parámetros de versión si existen (v1234567/)
                if (path.startsWith("v")) {
                    int slashIndex = path.indexOf("/");
                    if (slashIndex != -1) {
                        path = path.substring(slashIndex + 1);
                    }
                }

                // Remover extensión del archivo
                int dotIndex = path.lastIndexOf(".");
                if (dotIndex != -1) {
                    path = path.substring(0, dotIndex);
                }

                return path;
            }
        } catch (Exception e) {
            System.err.println("Error extrayendo public_id de URL: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void deleteUser(Long id) throws Exception {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado.");
        }
        User user = userOptional.get();
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    @Override
    public void changeUserPassword(Long id, ChangeUserPasswordDTO changeUserPasswordDTO) throws Exception {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado.");
        } else {
            User user = userOptional.get();
            if(changeUserPasswordDTO.currentPassword().equals(changeUserPasswordDTO.newPassword())){
                throw new Exception("Las contraseñas no pueden ser iguales");
            } else if (passwordEncoder.matches(changeUserPasswordDTO.currentPassword(), user.getPassword())) {
                user.setPassword(encode(changeUserPasswordDTO.newPassword()));
                userRepository.save(user);
            } else {
                throw new Exception("Contraseña actual incorrecta");
            }
        }
    }

    @Transactional
    @Override
    public void updatePassword(String email, String nuevaPassword) throws Exception {
        UserDTO userDTO = getUserByEmail(email);
        if (userDTO == null) {
            throw new Exception("Usuario no encontrado");
        }

        String encodedPassword = passwordEncoder.encode(nuevaPassword);
        userRepository.updatePasswordByEmail(email, encodedPassword);
    }

    @Override
    public boolean passwordIsCorrect(String email, String password) {
        User user = userRepository.findByEmailAndStatus(email,UserStatus.ACTIVE).orElse(null);
        if (user == null) {
            try {
                throw new Exception("El usuario con el correo " + email + " no se encontró");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return passwordEncoder.matches(password, user.getPassword());
    }

    //----------------------------------------------------------------------------------------------------------------------

    //We encrypt the password before saving it.
    private String encode(String password){
        return passwordEncoder.encode(password);
    }

    private boolean isEmailDuplicated(String email){
        return userRepository.findByEmail(email).isPresent();
    }
}