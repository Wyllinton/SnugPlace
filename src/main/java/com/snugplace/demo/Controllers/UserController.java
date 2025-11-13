package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.ResponseDTO;
import com.snugplace.demo.DTO.User.*;
import com.snugplace.demo.Model.Enums.Role;
import com.snugplace.demo.Model.Enums.UserStatus;
import com.snugplace.demo.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    /**
     * Registrar usuario (sin imagen)
     * POST /api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<String>> registerUser(@RequestBody CreateUserDTO createUserDTO) {
        try {
            userService.registerUser(createUserDTO);
            return ResponseEntity.ok(new ResponseDTO<>(false, "Usuario registrado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, e.getMessage()));
        }
    }

    /**
     * Registrar usuario con imagen en un solo paso
     * POST /api/users/register-with-image
     */
    @PostMapping(value = "/register-with-image", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Map>> registerWithImage(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("birthDate") String birthDate,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {

        try {
            // Crear el DTO con todos los parámetros incluido status
            CreateUserDTO createUserDTO = new CreateUserDTO(
                    name,
                    email,
                    password,
                    phoneNumber,
                    LocalDate.parse(birthDate),
                    role != null ? Role.valueOf(role) : Role.GUEST,
                    UserStatus.ACTIVE, // Status por defecto
                    description,
                    null // No pasar la ruta local
            );

            // Registrar usuario primero
            userService.registerUser(createUserDTO);

            // Buscar el usuario recién creado para obtener su ID
            UserDTO newUser = userService.getUserByEmail(email);

            String imageUrl = null;

            // Si se proporcionó una imagen, subirla a Cloudinary
            if (profileImage != null && !profileImage.isEmpty()) {
                imageUrl = userService.updateProfileImage(newUser.id(), profileImage);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuario registrado exitosamente");
            response.put("userId", newUser.id());
            response.put("imageUrl", imageUrl);

            return ResponseEntity.ok(new ResponseDTO<>(false, response));

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, errorResponse));
        }
    }

    /**
     * Obtener perfil de usuario
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> getUserProfile(@PathVariable Long id) {
        try {
            UserDTO userProfile = userService.getUserProfile(id);
            return ResponseEntity.ok(new ResponseDTO<>(false, userProfile));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, null));
        }
    }

    /**
     * Actualizar perfil de usuario (sin imagen)
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> updateUserProfile(
            @PathVariable Long id,
            @RequestBody UpdateProfileDTO updateProfileDTO) {
        try {
            userService.updateUserProfile(id, updateProfileDTO);
            return ResponseEntity.ok(new ResponseDTO<>(false, "Perfil actualizado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, e.getMessage()));
        }
    }

    /**
     * Actualizar imagen de perfil con subida directa
     * PUT /api/users/{id}/profile-image
     */
    @PutMapping(value = "/{id}/profile-image", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Map>> updateProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile imageFile) {
        try {
            String imageUrl = userService.updateProfileImage(id, imageFile);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Imagen de perfil actualizada exitosamente");
            response.put("imageUrl", imageUrl);

            return ResponseEntity.ok(new ResponseDTO<>(false, response));
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, errorResponse));
        }
    }

    /**
     * Eliminar usuario (soft delete)
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ResponseDTO<>(false, "Usuario eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, e.getMessage()));
        }
    }

    /**
     * Cambiar contraseña
     * PUT /api/users/{id}/change-password
     */
    @PutMapping("/{id}/change-password")
    public ResponseEntity<ResponseDTO<String>> changeUserPassword(
            @PathVariable Long id,
            @RequestBody ChangeUserPasswordDTO changeUserPasswordDTO) {
        try {
            userService.changeUserPassword(id, changeUserPasswordDTO);
            return ResponseEntity.ok(new ResponseDTO<>(false, "Contraseña actualizada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, e.getMessage()));
        }
    }
}