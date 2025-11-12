package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.User.ChangeUserPasswordDTO;
import com.snugplace.demo.DTO.ResponseDTO;
import com.snugplace.demo.DTO.User.CreateUserDTO;
import com.snugplace.demo.DTO.User.UpdateProfileDTO;
import com.snugplace.demo.DTO.User.UserDTO;
import com.snugplace.demo.Exceptions.ValueConflictException;
import com.snugplace.demo.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<String>> registerUser(@Valid @RequestBody CreateUserDTO userDTO) throws Exception{
        userService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "Usuario registrado exitosamente"));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<ResponseDTO<UserDTO>> getUserProfile(@PathVariable Long id) throws Exception{
        UserDTO userDTO = userService.getUserProfile(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, userDTO));
    }

    @PatchMapping("/{id}/profile/edit")
    public ResponseEntity<ResponseDTO<String>> updateUserProfile(@PathVariable Long id, @Valid @RequestBody UpdateProfileDTO updateProfileDTO) throws Exception{
        userService.updateUserProfile(id, updateProfileDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Perfil actualizado exitosamente"));
    }

    @DeleteMapping("/{id}/profile")
    public ResponseEntity<ResponseDTO<String>> deleteUser(@PathVariable Long id) throws Exception{
        userService.deleteUser(id);
        return ResponseEntity.ok(new ResponseDTO<>(false, "El usuario ha sido eliminado"));
    }

    @PatchMapping("/{id}/profile/change-password")
    public ResponseEntity<ResponseDTO<String>> changeUserPassword(@PathVariable Long id, @Valid @RequestBody ChangeUserPasswordDTO changeUserPasswordDTO) throws Exception{
        userService.changeUserPassword(id, changeUserPasswordDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Contraseña cambiada exitosamente"));
    }

    @ExceptionHandler(ValueConflictException.class)
    public ResponseEntity<ResponseDTO<String>> handleValueConflictException(ValueConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body( new ResponseDTO<>(true, ex.getMessage()) );
    }

}
