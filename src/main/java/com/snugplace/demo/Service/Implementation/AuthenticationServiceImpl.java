package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.User.CreateUserDTO;
import com.snugplace.demo.DTO.User.LogInUserDTO;
import com.snugplace.demo.DTO.User.ResetPasswordDTO;
import com.snugplace.demo.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public void loginUser(LogInUserDTO logInUserDTO) throws Exception {

    }

    @Override
    public void recoverPassword(String email) throws Exception {

    }

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) throws Exception {

    }
}
