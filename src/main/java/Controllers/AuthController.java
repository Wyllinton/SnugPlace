package Controllers;

import Service.AuthServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    //Registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    //Solicitar recuperación de contraseña
    @PostMapping("/recover-password")
    public ResponseEntity<MessageResponse> recoverPassword(@RequestBody RecoverPasswordRequest request) {
        authService.sendRecoveryCode(request.getEmail());
        return ResponseEntity.ok(new MessageResponse("Código de recuperación enviado al email"));
    }

    //Restablecer contraseña
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(new MessageResponse("Contraseña restablecida exitosamente"));
    }
}
