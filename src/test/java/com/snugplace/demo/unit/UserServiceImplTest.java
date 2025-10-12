package com.snugplace.demo.unit;

import com.snugplace.demo.DTO.User.ChangeUserPasswordDTO;
import com.snugplace.demo.DTO.User.CreateUserDTO;
import com.snugplace.demo.DTO.User.UpdateProfileDTO;
import com.snugplace.demo.DTO.User.UserDTO;
import com.snugplace.demo.Exceptions.ValueConflictException;
import com.snugplace.demo.Mappers.UserMapper;
import com.snugplace.demo.Model.Enums.Role;
import com.snugplace.demo.Model.Enums.UserStatus;
import com.snugplace.demo.Model.User;
import com.snugplace.demo.Repository.UserRepository;
import com.snugplace.demo.Service.Implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de la clase UserServiceImpl.
 * Estructura AAA (Arrange-Act-Assert)
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private CreateUserDTO createUserDTO;
    private User user;

    @BeforeEach
    void setUp() {
        createUserDTO = new CreateUserDTO(
                "Juan Pérez",
                "juan.perez@example.com",
                "Password123",
                "45678965",
                LocalDate.of(2000,2,3),
                Role.USER,
                UserStatus.ACTIVE,
                "YUIJBFHN",
                "htttp/myphto.co"
        );

        user = new User();
        user.setId(1L);
        user.setEmail(createUserDTO.email());
        user.setPassword("encodedPassword");
        user.setStatus(UserStatus.ACTIVE);
        user.setName("Juan Pérez");
    }

    // --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe registrar un usuario exitosamente cuando el email no existe")
    void testRegisterUserSuccess() throws Exception {
        // Arrange
        when(userRepository.findByEmail(createUserDTO.email())).thenReturn(Optional.empty());
        when(userMapper.toEntity(createUserDTO)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Act & Assert
        assertDoesNotThrow(() -> userService.registerUser(createUserDTO));
        verify(userRepository).save(any(User.class));
    }

    // --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar ValueConflictException si el email ya existe")
    void testRegisterUserEmailConflict() {
        // Arrange
        when(userRepository.findByEmail(createUserDTO.email())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(ValueConflictException.class, () -> userService.registerUser(createUserDTO));
        verify(userRepository, never()).save(any());
    }

    // --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe obtener un usuario por ID correctamente")
    void testGetUserProfileSuccess() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserDTO(user)).thenReturn(new UserDTO(1L, "Juan Pérez", "juan.perez@example.com", null, Role.HOST));

        // Act
        UserDTO result = userService.getUserProfile(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Juan Pérez", result.name());
        verify(userRepository).findById(1L);
    }

    // --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar excepción al obtener usuario inexistente")
    void testGetUserProfileNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception ex = assertThrows(Exception.class, () -> userService.getUserProfile(1L));
        assertTrue(ex.getMessage().contains("Usuario no encontrado"));
    }

    // --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe actualizar el perfil del usuario correctamente")
    void testUpdateUserProfileSuccess() throws Exception {
        // Arrange
        UpdateProfileDTO updateDTO = new UpdateProfileDTO("Nuevo Nombre", "+573001112233", "http://photo.com", "Descripción actualizada");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        userService.updateUserProfile(1L, updateDTO);

        // Assert
        assertEquals("Nuevo Nombre", user.getName());
        verify(userRepository).save(user);
    }

    // --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar excepción si se actualiza un usuario inexistente")
    void testUpdateUserProfileNotFound() {
        // Arrange
        UpdateProfileDTO updateDTO = new UpdateProfileDTO("Nombre", null, null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> userService.updateUserProfile(1L, updateDTO));
        verify(userRepository, never()).save(any());
    }

    // --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe eliminar (desactivar) un usuario correctamente")
    void testDeleteUserSuccess() throws Exception {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(1L);

        // Assert
        assertEquals(UserStatus.INACTIVE, user.getStatus());
        verify(userRepository).save(user);
    }

    // --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar excepción si se elimina un usuario inexistente")
    void testDeleteUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> userService.deleteUser(1L));
    }

    // --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe cambiar la contraseña correctamente si la actual coincide")
    void testChangeUserPasswordSuccess() throws Exception {
        // Arrange
        ChangeUserPasswordDTO dto = new ChangeUserPasswordDTO("currentPass", "newPass");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("currentPass", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        // Act
        userService.changeUserPassword(1L, dto);

        // Assert
        verify(userRepository).save(user);
        assertEquals("encodedNewPass", user.getPassword());
    }

    // --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar excepción si las contraseñas son iguales")
    void testChangeUserPasswordSamePassword() {
        // Arrange
        ChangeUserPasswordDTO dto = new ChangeUserPasswordDTO("samePass", "samePass");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(Exception.class, () -> userService.changeUserPassword(1L, dto));
        verify(userRepository, never()).save(any());
    }

    // --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar excepción si la contraseña actual es incorrecta")
    void testChangeUserPasswordWrongCurrentPassword() {
        // Arrange
        ChangeUserPasswordDTO dto = new ChangeUserPasswordDTO("wrongPass", "newPass");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", user.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(Exception.class, () -> userService.changeUserPassword(1L, dto));
    }

    // --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe verificar contraseña correctamente")
    void testPasswordIsCorrectTrue() {
        // Arrange
        when(userRepository.findByEmailAndStatus(user.getEmail(), UserStatus.ACTIVE)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123", user.getPassword())).thenReturn(true);

        // Act
        boolean result = userService.passwordIsCorrect(user.getEmail(), "Password123");

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el usuario no se encuentra al verificar contraseña")
    void testPasswordIsCorrectUserNotFound() {
        // Arrange
        when(userRepository.findByEmailAndStatus(user.getEmail(), UserStatus.ACTIVE)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.passwordIsCorrect(user.getEmail(), "Password123"));
    }
}

