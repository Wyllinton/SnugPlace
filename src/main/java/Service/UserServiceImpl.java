package Service;

import DTO.EditUserDTO;
import Model.User;
import Service.Interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public User getUserProfile(String id) {
        return null;
    }

    @Override
    public void updateProfile(String id, EditUserDTO userDTO) {

    }

    @Override
    public void changePassword(String id, String password) {

    }
}
