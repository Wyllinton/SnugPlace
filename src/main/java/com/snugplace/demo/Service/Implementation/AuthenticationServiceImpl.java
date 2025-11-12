package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.AuthResponseDTO;
import com.snugplace.demo.DTO.User.LogInUserDTO;
import com.snugplace.demo.DTO.User.ResetPasswordDTO;
import com.snugplace.demo.DTO.User.UserDTO;
import com.snugplace.demo.Security.JWTUtils;
import com.snugplace.demo.Service.AuthenticationService;
import com.snugplace.demo.Service.MailService;
import com.snugplace.demo.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JWTUtils jwtUtils;
    private final MailService mailService;
    private final Map<String, RecoveryCodeData> recoveryCodes = new ConcurrentHashMap<>();

    @Override
    public AuthResponseDTO loginUser(LogInUserDTO logInUserDTO) throws Exception {
        UserDTO userDTO = userService.getUserByEmail(logInUserDTO.email());
        if(userDTO == null){
            throw new Exception("Credenciales inválidas");
        }
        if(!userService.passwordIsCorrect(logInUserDTO.email(), logInUserDTO.password())){
            throw new Exception("Contraseña incorrecta");
        }

        Map<String, String> claims = new HashMap<>();
        claims.put("role", userDTO.role().name());
        claims.put("email", userDTO.email());
        claims.put("userId", userDTO.id().toString());

        String token = jwtUtils.generateToken(userDTO.email(), claims);

        sendEnhancedLoginEmail(userDTO);

        return new AuthResponseDTO(token);
    }

    @Override
    public void recoverPassword(String email) throws Exception {
        UserDTO userDTO = userService.getUserByEmail(email);
        if (userDTO == null) {
            throw new Exception("No existe un usuario con ese correo electrónico");
        }

        // Generate code with six digits
        String recoveryCode = String.format("%04d", (int) (Math.random() * 10000));

        // Save the generated code (15 min)
        recoveryCodes.put(email, new RecoveryCodeData(recoveryCode, LocalDateTime.now().plusMinutes(10)));

        //Send email
        String subject = "🔑 Código de recuperación de contraseña - SnugPlace";
        String textContent = """
            ¡Hola %s!
            
            ==================================
            NOTIFICACIÓN DE SEGURIDAD
            ==================================
            
            Hemos recibido una solicitud para restablecer tu contraseña.
            
            🔢 Tu código de recuperación es: %s
            
            Este código expirará en 10 minutos.
            
            Si no solicitaste este cambio, ignora este mensaje.
            
            ---
            SnugPlace
            """.formatted(userDTO.name(), recoveryCode);

        mailService.sendSimpleEmail(userDTO.email(), subject, textContent);
    }


    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) throws Exception {
        // Buscar el email asociado a ese código
        Optional<Map.Entry<String, RecoveryCodeData>> entryOpt = recoveryCodes.entrySet().stream()
                .filter(e -> e.getValue().code().equals(resetPasswordDTO.codigo()))
                .findFirst();

        if (entryOpt.isEmpty()) {
            throw new Exception("Código de recuperación inválido");
        }

        String email = entryOpt.get().getKey();
        RecoveryCodeData data = entryOpt.get().getValue();

        //Valid the expiration
        if (data.expiration().isBefore(LocalDateTime.now())) {
            recoveryCodes.remove(email);
            throw new Exception("El código de recuperación ha expirado");
        }

        //Update password
        UserDTO userDTO = userService.getUserByEmail(email);
        userService.updatePassword(userDTO.email(), resetPasswordDTO.nuevaPassword());

        //Delete the code
        recoveryCodes.remove(email);

        //Send confirmation email
        String subject = "✅ Contraseña restablecida con éxito";
        String textContent = """
            ¡Hola %s!
            
            ==================================
            NOTIFICACIÓN DE SEGURIDAD
            ==================================
            
            Tu contraseña ha sido actualizada correctamente el %s.
            
            Si no fuiste tú, cambia tu contraseña inmediatamente y contacta con soporte.
            
            ---
            SnugPlace
            """.formatted(
                userDTO.name(),
                getCurrentDateTime()
        );

        mailService.sendSimpleEmail(userDTO.email(), subject, textContent);
    }

    //------------------------------------------------------------------------------------------------------------------

    private record RecoveryCodeData(String code, LocalDateTime expiration) {}

    private void sendEnhancedLoginEmail(UserDTO userDTO) {
        String subject = "🔐 Nuevo inicio de sesión en SnugPlace";

        String textContent = """
            ¡Hola %s!
            
            ==================================
            NOTIFICACIÓN DE SEGURIDAD
            ==================================
            
            Se detectó un nuevo inicio de sesión en tu cuenta de SnugPlace.
            
            📅 Fecha y hora: %s
            
            ¿Fuiste tú?
            ✅ Si reconoces esta actividad, no necesitas hacer nada.
            
            ❌ Si NO reconoces este acceso:
            • Cambia tu contraseña inmediatamente
            • Revisa tu actividad reciente
           
                       
            ---
            SnugPlace
            Este es un mensaje automático, por favor no respondas.
            """.formatted(
                userDTO.name(),
                getCurrentDateTime()
        );

        mailService.sendSimpleEmail(userDTO.email(), subject, textContent);
    }

    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm:ss"));
    }
}
