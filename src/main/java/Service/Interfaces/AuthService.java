package Service.Interfaces;

import DTO.CreateUserDTO;
import DTO.LogInUserDTO;

public interface AuthService {
    void registerUser(CreateUserDTO userDTO);

    void loginUser(LogInUserDTO userDTO);

    void recoverPasswordRequest(String email);

    void recoverPassword(String email);
}
