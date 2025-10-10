package com.snugplace.demo.Service;

import com.snugplace.demo.DTO.User.ChangeUserPasswordDTO;
import com.snugplace.demo.DTO.User.CreateUserDTO;
import com.snugplace.demo.DTO.User.UpdateProfileDTO;
import com.snugplace.demo.DTO.User.UserDTO;

public interface UserService {

    UserDTO getUserProfile(Long id);

    UserDTO getUserByEmail(String email);

    boolean passwordIsCorrect(String email, String password);

    void registerUser(CreateUserDTO userDTO) throws Exception;

    void updateUserProfile(Long id, UpdateProfileDTO updateProfileDTO) throws Exception;

    void deleteUser(Long id) throws Exception;

    void changeUserPassword(Long id, ChangeUserPasswordDTO changeUserPasswordDTO) throws Exception;

    void updatePassword(String email, String newPassword) throws Exception;
}
