package Service.Interfaces;

import DTO.EditUserDTO;
import Model.User;

public interface UserService {
    User getUserProfile(String id);

    void updateProfile(String id, EditUserDTO userDTO);

    void changePassword(String id, String password);
}
