package Controllers;

import DTO.User.ChangeUserPasswordDTO;
import DTO.ResponseDTO;
import DTO.User.CreateUserDTO;
import DTO.User.UpdateProfileDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public ResponseEntity<ResponseDTO<String>> createUser(@Valid @RequestBody CreateUserDTO userDTO) throws Exception{
        //Lógica
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "El registro ha sido exitoso"));
    }

    /*
    @GetMapping("/profile/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> getUserProfile(@PathVariable String id) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Perfil del usuario"));
    }
    */

    @PatchMapping("/profile")
    public ResponseEntity<ResponseDTO<String>> updateUserProfile(@Valid @RequestBody UpdateProfileDTO updateProfileDTO) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Perfil actualizado exitosamente"));
    }

    @DeleteMapping("/profile/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteUser(@PathVariable String id) throws Exception{
        //Lógica

        return ResponseEntity.ok(new ResponseDTO<>(false, "El usuario ha sido eliminado"));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ResponseDTO<String>> changeUserPassword(@Valid @RequestBody ChangeUserPasswordDTO changeUserPasswordDTO) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Contraseña cambiada exitosamente"));
    }
}
