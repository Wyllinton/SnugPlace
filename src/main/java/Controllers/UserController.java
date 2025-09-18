package Controllers;

import DTO.User.ChangeUserPasswordDTO;
import DTO.ResponseDTO;
import DTO.User.UpdateProfileDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/users")
public class UserController {

    @GetMapping("/profile/{id}")
    public ResponseEntity<ResponseDTO<String>> getUserProfile(@PathVariable String id) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Perfil del usuario"));
    }

    @PatchMapping("/profile")
    public ResponseEntity<ResponseDTO<String>> updateUserProfile(@Valid @RequestBody UpdateProfileDTO updateProfileDTO) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Perfil actualizado exitosamente"));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ResponseDTO<String>> changeUserPassword(@Valid @RequestBody ChangeUserPasswordDTO changeUserPasswordDTO) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Contraseña cambiada exitosamente"));
    }
}
