package Service.Interfaces;

import DTO.UpdateProfileDTO;
import Model.User;

public interface UserService {
    User getUserProfile(String id);

    void updateProfile(String id, UpdateProfileDTO userDTO);

    void changePassword(String id, String password);
}
