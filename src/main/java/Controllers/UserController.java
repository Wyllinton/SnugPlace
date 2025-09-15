package Controllers;

import DTO.ChangeUserPassword;
import DTO.ResponseDTO;
import DTO.UpdateProfileDTO;
import DTO.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/users")
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<ResponseDTO<String>> getUserProfile(@Valid @RequestBody UserDTO userDTO) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Perfil del usuario"));
    }

    @PatchMapping("/profile")
    public ResponseEntity<ResponseDTO<String>> updateUserProfile(@Valid @RequestBody UpdateProfileDTO updateProfileDTO) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Perfil actualizado exitosamente"));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ResponseDTO<String>> changeUserPassword(@Valid @RequestBody ChangeUserPassword changeUserPassword) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Contraseña cambiada exitosamente"));
    }


}
