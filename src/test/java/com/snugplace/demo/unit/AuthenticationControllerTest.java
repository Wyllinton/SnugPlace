package com.snugplace.demo.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snugplace.demo.Controllers.AuthenticationController;
import com.snugplace.demo.DTO.AuthResponseDTO;
import com.snugplace.demo.DTO.User.LogInUserDTO;
import com.snugplace.demo.DTO.User.ResetPasswordDTO;
import com.snugplace.demo.Service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@DisplayName("Authentication Controller Tests")
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationService authenticationService;

    private LogInUserDTO validLoginDTO;
    private ResetPasswordDTO validResetPasswordDTO;
    private AuthResponseDTO authResponseDTO;

    @BeforeEach
    void setUp() {
        validLoginDTO = new LogInUserDTO(
                "test@example.com",
                "password123"
        );

        validResetPasswordDTO = new ResetPasswordDTO(
                "123456",
                "newPassword123"
        );

        authResponseDTO = new AuthResponseDTO("jwt.token.here");
    }

    // ==================== LOGIN TESTS ====================

    @Test
    @DisplayName("Login - Successful authentication")
    void login_WithValidCredentials_ShouldReturnToken() throws Exception {
        // Arrange
        when(authenticationService.loginUser(any(LogInUserDTO.class)))
                .thenReturn(authResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt.token.here"));

        verify(authenticationService, times(1)).loginUser(any(LogInUserDTO.class));
    }

    @Test
    @DisplayName("Login - Invalid credentials should return 401")
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Arrange
        when(authenticationService.loginUser(any(LogInUserDTO.class)))
                .thenThrow(new Exception("Credenciales inválidas"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Credenciales inválidas"))
                .andExpect(jsonPath("$.error").value("Unauthorized"));

        verify(authenticationService, times(1)).loginUser(any(LogInUserDTO.class));
    }

    @Test
    @DisplayName("Login - Invalid email format should return 400")
    void login_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        // Arrange
        LogInUserDTO invalidEmailDTO = new LogInUserDTO(
                "invalid-email",
                "password123"
        );

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmailDTO)))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).loginUser(any(LogInUserDTO.class));
    }

    @Test
    @DisplayName("Login - Short password should return 400")
    void login_WithShortPassword_ShouldReturnBadRequest() throws Exception {
        // Arrange
        LogInUserDTO shortPasswordDTO = new LogInUserDTO(
                "test@example.com",
                "short"
        );

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shortPasswordDTO)))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).loginUser(any(LogInUserDTO.class));
    }

    @Test
    @DisplayName("Login - Null email should return 400")
    void login_WithNullEmail_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String jsonWithNullEmail = "{\"email\": null, \"password\": \"password123\"}";

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithNullEmail))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).loginUser(any(LogInUserDTO.class));
    }

    @Test
    @DisplayName("Login - Empty request body should return 400")
    void login_WithEmptyBody_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).loginUser(any(LogInUserDTO.class));
    }

    // ==================== RECOVER PASSWORD TESTS ====================

    @Test
    @DisplayName("Recover Password - Valid email should return success")
    void recoverPassword_WithValidEmail_ShouldReturnSuccess() throws Exception {
        // Arrange
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");

        doNothing().when(authenticationService).recoverPassword(anyString());

        // Act & Assert
        mockMvc.perform(post("/auth/recover-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.content").value("Código enviado exitosamente"));

        verify(authenticationService, times(1)).recoverPassword("test@example.com");
    }

    @Test
    @DisplayName("Recover Password - Null email should return 400")
    void recoverPassword_WithNullEmail_ShouldReturnBadRequest() throws Exception {
        // Arrange
        Map<String, String> request = new HashMap<>();
        request.put("email", null);

        // Act & Assert
        mockMvc.perform(post("/auth/recover-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).recoverPassword(anyString());
    }

    @Test
    @DisplayName("Recover Password - Blank email should return 400")
    void recoverPassword_WithBlankEmail_ShouldReturnBadRequest() throws Exception {
        // Arrange
        Map<String, String> request = new HashMap<>();
        request.put("email", "   ");

        // Act & Assert
        mockMvc.perform(post("/auth/recover-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).recoverPassword(anyString());
    }

    @Test
    @DisplayName("Recover Password - Empty request should return 400")
    void recoverPassword_WithEmptyRequest_ShouldReturnBadRequest() throws Exception {
        // Arrange
        Map<String, String> request = new HashMap<>();

        // Act & Assert
        mockMvc.perform(post("/auth/recover-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).recoverPassword(anyString());
    }

    @Test
    @DisplayName("Recover Password - Non-existent email should return 500")
    void recoverPassword_WithNonExistentEmail_ShouldReturnInternalServerError() throws Exception {
        // Arrange
        Map<String, String> request = new HashMap<>();
        request.put("email", "nonexistent@example.com");

        doThrow(new Exception("No existe un usuario con ese correo electrónico"))
                .when(authenticationService).recoverPassword(anyString());

        // Act & Assert
        mockMvc.perform(post("/auth/recover-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());

        verify(authenticationService, times(1)).recoverPassword("nonexistent@example.com");
    }

    // ==================== RESET PASSWORD TESTS ====================

    @Test
    @DisplayName("Reset Password - Valid data should return success")
    void resetPassword_WithValidData_ShouldReturnSuccess() throws Exception {
        // Arrange
        doNothing().when(authenticationService).resetPassword(any(ResetPasswordDTO.class));

        // Act & Assert
        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validResetPasswordDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.content").value("Contraseña restablecida exitosamente"));

        verify(authenticationService, times(1)).resetPassword(any(ResetPasswordDTO.class));
    }

    @Test
    @DisplayName("Reset Password - Invalid code should return 500")
    void resetPassword_WithInvalidCode_ShouldReturnInternalServerError() throws Exception {
        // Arrange
        doThrow(new Exception("Código de recuperación inválido"))
                .when(authenticationService).resetPassword(any(ResetPasswordDTO.class));

        // Act & Assert
        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validResetPasswordDTO)))
                .andExpect(status().isInternalServerError());

        verify(authenticationService, times(1)).resetPassword(any(ResetPasswordDTO.class));
    }

    @Test
    @DisplayName("Reset Password - Expired code should return 500")
    void resetPassword_WithExpiredCode_ShouldReturnInternalServerError() throws Exception {
        // Arrange
        doThrow(new Exception("El código de recuperación ha expirado"))
                .when(authenticationService).resetPassword(any(ResetPasswordDTO.class));

        // Act & Assert
        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validResetPasswordDTO)))
                .andExpect(status().isInternalServerError());

        verify(authenticationService, times(1)).resetPassword(any(ResetPasswordDTO.class));
    }

    @Test
    @DisplayName("Reset Password - Null code should return 400")
    void resetPassword_WithNullCode_ShouldReturnBadRequest() throws Exception {
        // Arrange
        ResetPasswordDTO invalidDTO = new ResetPasswordDTO(null, "newPassword123");

        // Act & Assert
        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).resetPassword(any(ResetPasswordDTO.class));
    }

    @Test
    @DisplayName("Reset Password - Short password should return 400")
    void resetPassword_WithShortPassword_ShouldReturnBadRequest() throws Exception {
        // Arrange
        ResetPasswordDTO invalidDTO = new ResetPasswordDTO("123456", "short");

        // Act & Assert
        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).resetPassword(any(ResetPasswordDTO.class));
    }

    @Test
    @DisplayName("Reset Password - Null password should return 400")
    void resetPassword_WithNullPassword_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String jsonWithNullPassword = "{\"codigo\": \"123456\", \"nuevaPassword\": null}";

        // Act & Assert
        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithNullPassword))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).resetPassword(any(ResetPasswordDTO.class));
    }

    @Test
    @DisplayName("Reset Password - Empty request body should return 400")
    void resetPassword_WithEmptyBody_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never()).resetPassword(any(ResetPasswordDTO.class));
    }
}