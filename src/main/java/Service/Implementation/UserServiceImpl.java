package Service.Implementation;

import DTO.User.ChangeUserPasswordDTO;
import DTO.User.CreateUserDTO;
import DTO.User.UpdateProfileDTO;
import DTO.User.UserDTO;
import Mappers.UserMapper;
import Model.User;
import Repository.UserRepository;
import Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    //Simulation of the DB
    private final Map<String, User> userStore = new ConcurrentHashMap<>();

    //We encrypt the password before saving it.
    private String encode(String password){
        var passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public void createUser(CreateUserDTO userDTO) throws Exception {
        User newUser = userMapper.toEntity(userDTO);
        //userRepository.save(newuser);
    }

    private boolean isEmailDuplicated(String email){
        return userStore.values().stream().anyMatch(
                u -> u.getEmail().equalsIgnoreCase(email)
        );
    }

    @Override
    public UserDTO getUserProfile(String id) throws Exception {

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
