package Service;

import DTO.User.ChangeUserPasswordDTO;
import DTO.User.UpdateProfileDTO;
import DTO.User.UserDTO;

public interface UserService {
    UserDTO getUserProfile(String id) throws Exception;

    void updateUserProfile(UpdateProfileDTO updateProfileDTO) throws Exception;

    void changeUserPassword(ChangeUserPasswordDTO changeUserPasswordDTO) throws Exception;
}
