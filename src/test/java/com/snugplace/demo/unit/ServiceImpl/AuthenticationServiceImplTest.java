package com.snugplace.demo.unit.ServiceImpl;

import com.snugplace.demo.DTO.User.UserDTO;
import com.snugplace.demo.DTO.AuthResponseDTO;
import com.snugplace.demo.DTO.User.LogInUserDTO;
import com.snugplace.demo.DTO.User.ResetPasswordDTO;
import com.snugplace.demo.Model.Enums.Role;
import com.snugplace.demo.Security.JWTUtils;
import com.snugplace.demo.Service.Implementation.AuthenticationServiceImpl;
import com.snugplace.demo.Service.Implementation.MailServiceImpl;
import com.snugplace.demo.Service.Implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para AuthenticationServiceImpl.
 * Patrón AAA: Arrange - Act - Assert.
 */
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private MailServiceImpl mailService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO(
                1L,
                "Juan Pérez",
                "juan.perez@example.com",
                null,
                Role.GUEST
        );
    }

    // -------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe iniciar sesión exitosamente con credenciales válidas")
    void testLoginUserSuccess() throws Exception {
        // Arrange
        LogInUserDTO login = new LogInUserDTO("juan.perez@example.com", "Password123");

        when(userService.getUserByEmail(login.email())).thenReturn(userDTO);
        when(userService.passwordIsCorrect(login.email(), login.password())).thenReturn(true);
        when(jwtUtils.generateToken(anyString(), anyMap())).thenReturn("fake-jwt-token");

        // Act
        AuthResponseDTO response = authenticationService.loginUser(login);

        // Assert
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.token());
        verify(jwtUtils).generateToken(eq("juan.perez@example.com"), anyMap());
        verify(mailService).sendSimpleEmail(eq("juan.perez@example.com"), anyString(), anyString());
    }

    // -------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar excepción si el usuario no existe al iniciar sesión")
    void testLoginUserUserNotFound() {
        // Arrange
        LogInUserDTO login = new LogInUserDTO("notfound@example.com", "Password123");
        when(userService.getUserByEmail(login.email())).thenReturn(null);

        // Act & Assert
        Exception ex = assertThrows(Exception.class, () -> authenticationService.loginUser(login));
        assertEquals("Credenciales inválidas", ex.getMessage());
        verify(userService, never()).passwordIsCorrect(anyString(), anyString());
    }

    // -------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar excepción si la contraseña es incorrecta")
    void testLoginUserWrongPassword() throws Exception {
        // Arrange
        LogInUserDTO login = new LogInUserDTO("juan.perez@example.com", "WrongPassword");
        when(userService.getUserByEmail(login.email())).thenReturn(userDTO);
        when(userService.passwordIsCorrect(login.email(), login.password())).thenReturn(false);

        // Act & Assert
        Exception ex = assertThrows(Exception.class, () -> authenticationService.loginUser(login));
        assertEquals("Contraseña incorrecta", ex.getMessage());
        verify(jwtUtils, never()).generateToken(anyString(), anyMap());
    }

    // -------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe generar código de recuperación y enviar email")
    void testRecoverPasswordSuccess() throws Exception {
        // Arrange
        when(userService.getUserByEmail(userDTO.email())).thenReturn(userDTO);

        // Act
        authenticationService.recoverPassword(userDTO.email());

        // Assert
        verify(mailService).sendSimpleEmail(eq(userDTO.email()), anyString(), contains("Tu código de recuperación"));
    }

    // -------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar excepción si el correo no existe en recuperación de contraseña")
    void testRecoverPasswordUserNotFound() {
        // Arrange
        when(userService.getUserByEmail("unknown@example.com")).thenReturn(null);

        // Act & Assert
        Exception ex = assertThrows(Exception.class, () -> authenticationService.recoverPassword("unknown@example.com"));
        assertEquals("No existe un usuario con ese correo electrónico", ex.getMessage());
        verify(mailService, never()).sendSimpleEmail(anyString(), anyString(), anyString());
    }

    // -------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe restablecer la contraseña correctamente con código válido")
    void testResetPasswordSuccess() throws Exception {
        // Arrange
        String recoveryCode = "123456";
        when(userService.getUserByEmail(userDTO.email())).thenReturn(userDTO);

        // Inyectamos manualmente un código válido
        var field = authenticationService.getClass().getDeclaredField("recoveryCodes");
        field.setAccessible(true);
        Map<String, Object> map = (Map<String, Object>) field.get(authenticationService);
        map.clear();

        // Creamos el objeto interno RecoveryCodeData mediante reflexión
        Class<?> innerClass = Class.forName("com.snugplace.demo.Service.Implementation.AuthenticationServiceImpl$RecoveryCodeData");
        var constructor = innerClass.getDeclaredConstructor(String.class, LocalDateTime.class);
        constructor.setAccessible(true);
        Object validCode = constructor.newInstance(recoveryCode, LocalDateTime.now().plusMinutes(10));
        map.put(userDTO.email(), validCode);

        // Simulamos actualización de contraseña
        doNothing().when(userService).updatePassword(anyString(), anyString());

        // Act
        authenticationService.resetPassword(new ResetPasswordDTO(recoveryCode, "NewPassword123"));

        // Assert
        verify(userService).updatePassword(eq(userDTO.email()), eq("NewPassword123"));
        verify(mailService).sendSimpleEmail(eq(userDTO.email()), contains("Contraseña restablecida"), anyString());
    }

    // -------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar excepción si el código de recuperación no existe")
    void testResetPasswordInvalidCode() {
        // Arrange
        ResetPasswordDTO dto = new ResetPasswordDTO("000000", "NewPassword");

        // Act & Assert
        Exception ex = assertThrows(Exception.class, () -> authenticationService.resetPassword(dto));
        assertEquals("Código de recuperación inválido", ex.getMessage());
    }

    // -------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar excepción si el código de recuperación ha expirado")
    void testResetPasswordExpiredCode() throws Exception {
        // Arrange
        String email = userDTO.email();

        // Inyectamos manualmente un código expirado
        var field = authenticationService.getClass().getDeclaredField("recoveryCodes");
        field.setAccessible(true);
        Map<String, Object> map = (Map<String, Object>) field.get(authenticationService);
        map.clear();
        var recoveryCodeClass = Class.forName("com.snugplace.demo.Service.Implementation.AuthenticationServiceImpl$RecoveryCodeData");
        var constructor = recoveryCodeClass.getDeclaredConstructor(String.class, LocalDateTime.class);
        constructor.setAccessible(true);
        Object expiredCode = constructor.newInstance("123456", LocalDateTime.now().minusMinutes(5));
        map.put(email, expiredCode);

        // Act & Assert
        ResetPasswordDTO dto = new ResetPasswordDTO("123456", "NewPassword");
        Exception ex = assertThrows(Exception.class, () -> authenticationService.resetPassword(dto));
        assertEquals("El código de recuperación ha expirado", ex.getMessage());
    }
}

