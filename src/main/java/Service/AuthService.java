package Service;

import DTO.User.CreateUserDTO;
import DTO.User.LogInUserDTO;
import DTO.User.ResetPasswordDTO;

public interface AuthService {

    void registerUser(CreateUserDTO userDTO) throws Exception;

    void loginUser(LogInUserDTO logInUserDTO) throws Exception;

    void recoverPassword(String email) throws Exception;

    void resetPassword(ResetPasswordDTO resetPasswordDTO) throws Exception;
}
