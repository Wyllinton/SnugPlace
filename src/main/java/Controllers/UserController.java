package Controllers;

import Service.UserService;
import com.snugplace.demo.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Obtener perfil del usuario autenticado
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile() {
        UserResponse profile = userService.getAuthenticatedUserProfile();
        return ResponseEntity.ok(profile);
    }

    //Actualizar perfil del usuario
    @PatchMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(
            @Valid @RequestBody ActualizarPerfilRequest request) {
        UserResponse updated = userService.updateProfile(request);
        return ResponseEntity.ok(updated);
    }

    //Cambiar contraseña
    @PatchMapping("/change-password")
    public ResponseEntity<MessageResponse> changePassword(
            @Valid @RequestBody CHangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok(new MessageResponse("Contraseña cambiada exitosamente"));
    }
}
