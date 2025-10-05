package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.User.CreateUserDTO;
import com.snugplace.demo.DTO.User.LogInUserDTO;
import com.snugplace.demo.DTO.User.ResetPasswordDTO;
import com.snugplace.demo.DTO.ResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<String>> registerUser(@Valid @RequestBody CreateUserDTO userDTO) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "Usuario registrado exitosamente"));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<String>> loginUser(@Valid @RequestBody LogInUserDTO logInUserDTO) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Inicio de sesión exitoso"));
    }

    @PostMapping("/recover-password")
    public ResponseEntity<ResponseDTO<String>> recoverPassword(@Email @NotNull String email) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Código enviado exitosamente"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDTO<String>> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Contraseña restablecida exitosamente"));
    }
}
