package com.snugplace.demo.Service;

import com.snugplace.demo.DTO.User.CreateUserDTO;
import com.snugplace.demo.DTO.User.LogInUserDTO;
import com.snugplace.demo.DTO.User.ResetPasswordDTO;

public interface AuthenticationService {

    void loginUser(LogInUserDTO logInUserDTO) throws Exception;

    void recoverPassword(String email) throws Exception;

    void resetPassword(ResetPasswordDTO resetPasswordDTO) throws Exception;
}
