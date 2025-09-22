package Controllers;

import DTO.User.ChangeUserPasswordDTO;
import DTO.ResponseDTO;
import DTO.User.CreateUserDTO;
import DTO.User.UpdateProfileDTO;
import DTO.User.UserDTO;
import Service.UserService;
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

    @PostMapping
    public ResponseEntity<ResponseDTO<String>> createUser(@Valid @RequestBody CreateUserDTO userDTO) throws Exception{
        userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "El registro ha sido exitoso"));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> getUserProfile(@PathVariable String id) throws Exception{
        UserDTO userDTO = userService.getUserProfile(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, userDTO));
    }


    @PatchMapping("/profile")
    public ResponseEntity<ResponseDTO<String>> updateUserProfile(@Valid @RequestBody UpdateProfileDTO updateProfileDTO) throws Exception{
        userService.updateUserProfile(updateProfileDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Perfil actualizado exitosamente"));
    }

    @DeleteMapping("/profile/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteUser(@PathVariable String id) throws Exception{
        userService.deleteUser(id);
        return ResponseEntity.ok(new ResponseDTO<>(false, "El usuario ha sido eliminado"));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ResponseDTO<String>> changeUserPassword(@Valid @RequestBody ChangeUserPasswordDTO changeUserPasswordDTO) throws Exception{
        userService.changeUserPassword(changeUserPasswordDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Contraseña cambiada exitosamente"));
    }
}
