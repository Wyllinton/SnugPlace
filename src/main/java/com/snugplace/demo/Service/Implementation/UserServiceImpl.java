package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.User.ChangeUserPasswordDTO;
import com.snugplace.demo.DTO.User.CreateUserDTO;
import com.snugplace.demo.DTO.User.UpdateProfileDTO;
import com.snugplace.demo.DTO.User.UserDTO;
import com.snugplace.demo.Mappers.UserMapper;
import com.snugplace.demo.Model.User;
import com.snugplace.demo.Repository.UserRepository;
import com.snugplace.demo.Service.UserService;
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

    private boolean isEmailDuplicated(String email){
        return userStore.values().stream().anyMatch(
                u -> u.getEmail().equalsIgnoreCase(email)
        );
    }

    @Override
    public UserDTO getUserProfile(Long id) throws Exception {
        return null;
    }

    @Override
    public void registerUser(CreateUserDTO createUserDTO) throws Exception{

    }

    @Override
    public void updateUserProfile(Long id, UpdateProfileDTO updateProfileDTO) throws Exception {

    }

    @Override
    public void deleteUser(Long id) throws Exception {

    }

    @Override
    public void changeUserPassword(Long id, ChangeUserPasswordDTO changeUserPasswordDTO) throws Exception {


    }
}
