package Service.Implementation;

import DTO.User.ChangeUserPasswordDTO;
import DTO.User.CreateUserDTO;
import DTO.User.UpdateProfileDTO;
import DTO.User.UserDTO;
import Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Override
    public void createUser(CreateUserDTO userDTO) throws Exception {

    }

    @Override
    public UserDTO getUserProfile(String id) throws Exception {
        return null;
    }

    @Override
    public void updateUserProfile(UpdateProfileDTO updateProfileDTO) throws Exception {

    }

    @Override
    public void deleteUser(String id) throws Exception {

    }

    @Override
    public void changeUserPassword(ChangeUserPasswordDTO changeUserPasswordDTO) throws Exception {

    }
}
