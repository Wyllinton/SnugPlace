package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.AuthResponseDTO;
import com.snugplace.demo.DTO.ErrorResponseDTO;
import com.snugplace.demo.DTO.User.LogInUserDTO;
import com.snugplace.demo.DTO.User.ResetPasswordDTO;
import com.snugplace.demo.DTO.ResponseDTO;
import com.snugplace.demo.Service.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LogInUserDTO loginRequest) {
        try {
            AuthResponseDTO authResponse = authenticationService.loginUser(loginRequest);
            return ResponseEntity.ok().body(authResponse);
        } catch (Exception e) {
            ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                    HttpStatus.UNAUTHORIZED,
                    "Credenciales inválidas"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/recover-password")
    public ResponseEntity<ResponseDTO<String>> recoverPassword(@Email @NotNull String email) throws Exception{
        authenticationService.recoverPassword(email);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Código enviado exitosamente"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDTO<String>> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) throws Exception{
        authenticationService.resetPassword(resetPasswordDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Contraseña restablecida exitosamente"));
    }
}
