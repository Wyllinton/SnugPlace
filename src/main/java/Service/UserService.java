package Service;

import DTO.ResponseDTO;
import DTO.User.ChangeUserPasswordDTO;
import DTO.User.CreateUserDTO;
import DTO.User.UpdateProfileDTO;
import DTO.User.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {

    void createUser(CreateUserDTO userDTO) throws Exception;

    UserDTO getUserProfile(String id) throws Exception;

    void updateUserProfile(UpdateProfileDTO updateProfileDTO) throws Exception;

    void deleteUser(String id) throws Exception;

    void changeUserPassword(ChangeUserPasswordDTO changeUserPasswordDTO) throws Exception;
}
