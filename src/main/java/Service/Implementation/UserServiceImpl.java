package Service.Implementation;

import DTO.User.ChangeUserPasswordDTO;
import DTO.User.CreateUserDTO;
import DTO.User.UpdateProfileDTO;
import DTO.User.UserDTO;
import Exceptions.ValueConflictException;
import Mappers.UserMapper;
import Model.Enums.UserStatus;
import Model.User;
import Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    //Simulation of the DB
    private final Map<String, User> userStore = new ConcurrentHashMap<>();
    private final UserMapper userMapper;

    //We encrypt the password before saving it.
    private String encode(String password){
        var passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public void createUser(CreateUserDTO userDTO) throws Exception {
        //Validation in case the email is used already
        if(isEmailDuplicated(userDTO.email())){
            throw new ValueConflictException("Email is already in use");
        }

        User newUser = userMapper.toEntity(userDTO);
        newUser.setPassword(encode(userDTO.password()));

        userStore.put(newUser.getEmail(), newUser);
    }

    private boolean isEmailDuplicated(String email){
        return userStore.values().stream().anyMatch(
                u -> u.getEmail().equalsIgnoreCase(email)
        );
    }

    @Override
    public UserDTO getUserProfile(String id) throws Exception {
        User user = userStore.get(id);

        if(user == null){
            throw new Exception("El usuario no ha sido encontrado");
        }

        return userMapper.toUserDTO(user);
    }

    @Override
    public void updateUserProfile(UpdateProfileDTO updateProfileDTO) throws Exception {

        //HERE Would be the authentication security, we take the email and wouldn't the ID in the updateProfileDTO
        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); //The email would be the name

        User user = userStore.values().stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(currentEmail))
            .findFirst()
            .orElseThrow(() -> new Exception("Usuario no encontrado."));
         */


        //Search the User by ID - This is the temporally part of code-------------------------------
        User user = userStore.values().stream()
                .filter(u -> u.getId().equalsIgnoreCase(updateProfileDTO.id()))
                .findFirst()
                .orElse(null);
        //------------------------------------------------------------------------------------------

        if (updateProfileDTO.name() != null && !updateProfileDTO.name().isBlank()) {
            user.setName(updateProfileDTO.name());
        }
        if (updateProfileDTO.phoneNumber() != null && !updateProfileDTO.phoneNumber().isBlank()) {
            user.setPhoneNumber(updateProfileDTO.phoneNumber());
        }
        if (updateProfileDTO.photoURL() != null && !updateProfileDTO.photoURL().isBlank()) {
            user.setProfilePhoto(updateProfileDTO.photoURL());
        }
        if (updateProfileDTO.description() != null && !updateProfileDTO.description().isBlank()) {
            user.setDescription(updateProfileDTO.description());
        }

        userStore.put(user.getId(), user);
    }

    @Override
    public void deleteUser(String id) throws Exception {

        User user = userStore.get(id);

        if (user == null) {
            throw new Exception("Usuario no encontrado.");
        }

        userStore.remove(id);
    }

    @Override
    public void changeUserPassword(ChangeUserPasswordDTO changeUserPasswordDTO) throws Exception {

        //HERE Would be the authentication security, we take the email and wouldn't the ID in the ChangeUserPasswordDTO
        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();

        User user = userStore.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(currentEmail))
                .findFirst()
                .orElseThrow(() -> new Exception("Usuario no encontrado."));
        */

        //Search the User by ID - This is the temporally part of code-------------------------------
        User user = userStore.get(changeUserPasswordDTO.id());

        if (user == null) {
            throw new Exception("Usuario no encontrado.");
        }
        //------------------------------------------------------------------------------------------

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(changeUserPasswordDTO.currentPassword(), user.getPassword())) {
            throw new Exception("La contraseña actual es incorrecta.");
        }

        user.setPassword(encode(changeUserPasswordDTO.newPassword()));
        userStore.put(user.getId(), user);
    }
}
