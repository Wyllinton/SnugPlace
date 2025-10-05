package com.snugplace.demo.Service;

import com.snugplace.demo.DTO.User.ChangeUserPasswordDTO;
import com.snugplace.demo.DTO.User.UpdateProfileDTO;
import com.snugplace.demo.DTO.User.UserDTO;

public interface UserService {

    UserDTO getUserProfile(Long id) throws Exception;

    void updateUserProfile(UpdateProfileDTO updateProfileDTO) throws Exception;

    void deleteUser(Long id) throws Exception;

    void changeUserPassword(ChangeUserPasswordDTO changeUserPasswordDTO) throws Exception;
}
