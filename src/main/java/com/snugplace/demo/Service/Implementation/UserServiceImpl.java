package com.snugplace.demo.Service.Implementation;

import com.snugplace.demo.DTO.User.ChangeUserPasswordDTO;
import com.snugplace.demo.DTO.User.CreateUserDTO;
import com.snugplace.demo.DTO.User.UpdateProfileDTO;
import com.snugplace.demo.DTO.User.UserDTO;
import com.snugplace.demo.Exceptions.ResourceNotFoundException;
import com.snugplace.demo.Exceptions.ValueConflictException;
import com.snugplace.demo.Mappers.UserMapper;
import com.snugplace.demo.Model.Enums.UserStatus;
import com.snugplace.demo.Model.User;
import com.snugplace.demo.Repository.UserRepository;
import com.snugplace.demo.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(CreateUserDTO createUserDTO) throws Exception{
        if(isEmailDuplicated(createUserDTO.email())){
            throw new ValueConflictException("El correo electrónico ya está en uso.");
        }else {
            User newUser = userMapper.toEntity(createUserDTO);
            newUser.setPassword(encode(createUserDTO.password()));
            userRepository.save(newUser);
        }
    }

    @Override
    public UserDTO getUserProfile(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        if (!userRepository.existsByEmailAndStatus(email, UserStatus.ACTIVE)) {
            return null;
        }
        Optional<User> user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE);

        return userMapper.toUserDTO(user.get());
    }

    @Override
    public void updateUserProfile(Long id, UpdateProfileDTO updateProfileDTO) throws Exception {
        //Query
        Optional<User> userOptional = userRepository.findById(id);
        //Validation
        if (userOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado.");
        }
        //We get the user
        User user = userOptional.get();
        //Update the User's data
        user.setName(updateProfileDTO.name());
        user.setPhoneNumber(updateProfileDTO.phoneNumber());
        user.setProfilePhoto(updateProfileDTO.photoURL());
        user.setDescription(updateProfileDTO.description());
        //Save the User in the DB
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) throws Exception {
        //Query
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado.");
        }
        User user = userOptional.get();
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    @Override
    public void changeUserPassword(Long id, ChangeUserPasswordDTO changeUserPasswordDTO) throws Exception {
        //Query
        Optional<User> userOptional = userRepository.findById(id);
        //Validation
        if (userOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado.");
        }else{
            User user = userOptional.get();
            if(changeUserPasswordDTO.currentPassword().equals(changeUserPasswordDTO.newPassword())){
                throw new Exception("Las contraseñas no pueden ser iguales");
            } else if (passwordEncoder.matches(changeUserPasswordDTO.currentPassword(), user.getPassword())) {
                user.setPassword(encode(changeUserPasswordDTO.newPassword()));
                userRepository.save(user);
            } else {
                throw new Exception("Contraseña actual incorrecta");
            }
        }
    }

    @Override
    public boolean passwordIsCorrect(String email, String password) {
        User user = userRepository.findByEmailAndStatus(email,UserStatus.ACTIVE).orElse(null);
        if (user == null) {
            try {
                throw new Exception("El usuario con el correo " + email + " no se encontró");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(passwordEncoder.matches(password, user.getPassword()));
        return passwordEncoder.matches(password, user.getPassword());
    }

    //----------------------------------------------------------------------------------------------------------------------

    //We encrypt the password before saving it.
    private String encode(String password){
        return passwordEncoder.encode(password);
    }

    private boolean isEmailDuplicated(String email){
        return userRepository.findByEmail(email).isPresent();
    }
}
