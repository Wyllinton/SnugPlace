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

@Service
@RequiredArgsConstructor

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JWTUtils jwtUtils;
    private final MailService mailService;

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

        // Correo mejorado sin HttpServletRequest
        sendEnhancedLoginEmail(userDTO);

        return new AuthResponseDTO(token);
    }

    @Override
    public void recoverPassword(String email) throws Exception {

    }

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) throws Exception {

    }

    //------------------------------------------------------------------------------------------------------------------

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
