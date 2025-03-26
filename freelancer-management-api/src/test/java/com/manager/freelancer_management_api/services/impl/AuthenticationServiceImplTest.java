package com.manager.freelancer_management_api.services.impl;

import com.manager.freelancer_management_api.domain.repositories.UserRepository;
import com.manager.freelancer_management_api.domain.user.dtos.LoginDTO;
import com.manager.freelancer_management_api.domain.user.dtos.LoginResponseDTO;
import com.manager.freelancer_management_api.domain.user.dtos.RegisterUserDTO;
import com.manager.freelancer_management_api.domain.user.entities.User;
import com.manager.freelancer_management_api.domain.user.enums.UserRole;
import com.manager.freelancer_management_api.domain.user.exceptions.EmailAlreadyExistsException;
import com.manager.freelancer_management_api.domain.user.exceptions.InvalidEmailException;
import com.manager.freelancer_management_api.domain.user.exceptions.InvalidPasswordException;
import com.manager.freelancer_management_api.infra.security.TokenService;
import com.manager.freelancer_management_api.utils.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordValidator passwordValidator;
    @Mock
    private TokenService tokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Spy
    private User testUser;

    private LoginDTO validLoginDTO;
    private LoginDTO invalidLoginDTO;
    private RegisterUserDTO validRegisterUserDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .fullName("Test Test")
                .document("12345678901")
                .email("test@test.com")
                .password("encodedPassword")
                .mainUserRole(UserRole.CLIENT)
                .currentUserRole(UserRole.FREELANCER)
                .build();


        validLoginDTO = new LoginDTO("test@test.com", "password");
        invalidLoginDTO = new LoginDTO("invalidLogin@test.com", "invalidpassword");

        validRegisterUserDTO = new RegisterUserDTO("Test Register", "12345678901234", "regustertest@test.com", "password", UserRole.FREELANCER, UserRole.FREELANCER);
    }

    @Test
    void testLogin_ValidCredentials_ReturnsLoginResponse(){
        when(userRepository.findByEmail(validLoginDTO.email())).thenReturn(testUser);
        when(tokenService.generateToken(validLoginDTO)).thenReturn("TestToken");

        LoginResponseDTO response = authenticationService.login(validLoginDTO);

        assertNotNull(response);
        assertNotNull(response.token());
        assertEquals("TestToken", response.token());

        verify(userRepository).findByEmail(validLoginDTO.email());
        verify(tokenService).generateToken(validLoginDTO);
    }

    @Test
    void testRegister_ValidRegistrationData_Success() {
        when(userRepository.existsByEmail(validRegisterUserDTO.email())).thenReturn(false);
        when(passwordEncoder.encode(validRegisterUserDTO.password())).thenReturn("encodedPassword");

        authenticationService.register(validRegisterUserDTO);

        verify(passwordEncoder).encode(validRegisterUserDTO.password());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegister_InvalidRegistrationData_EmailAlreadyExists_ThrowsEmailAlreadyExistsException() {
        when(userRepository.existsByEmail(validRegisterUserDTO.email())).thenReturn(true);

        EmailAlreadyExistsException e = assertThrows(EmailAlreadyExistsException.class, () -> authenticationService.register(validRegisterUserDTO));

        assertEquals("Email already exists.", e.getMessage());
        verify(userRepository).existsByEmail(validRegisterUserDTO.email());
    }

    @Test
    void testLogin_ValidCredentials() {
        when(userRepository.findByEmail(validLoginDTO.email())).thenReturn(testUser);
        doNothing().when(passwordValidator)
                .validate(validLoginDTO.password(), testUser.getPassword());

        assertDoesNotThrow(() -> authenticationService.login(validLoginDTO));
    }

    @Test
    void testLogin_InvalidEmail_ThrowsInvalidEmailException() {
        when(userRepository.findByEmail(invalidLoginDTO.email())).thenReturn(null);

        InvalidEmailException e = assertThrows(InvalidEmailException.class, () -> authenticationService.login(invalidLoginDTO));

        assertEquals("Invalid email address.", e.getMessage());
        verify(userRepository).findByEmail(invalidLoginDTO.email());
    }

    @Test
    void testLogin_InvalidPassword_ThrowsInvalidPasswordException() {
        when(userRepository.findByEmail(invalidLoginDTO.email())).thenReturn(testUser);
        doThrow(new InvalidPasswordException("Invalid password.")).when(passwordValidator)
                .validate(invalidLoginDTO.password(), testUser.getPassword());

        InvalidPasswordException e = assertThrows(InvalidPasswordException.class, () -> authenticationService.login(invalidLoginDTO));

        assertEquals("Invalid password.", e.getMessage());
        verify(passwordValidator).validate(anyString(), anyString());
        verify(userRepository).findByEmail(invalidLoginDTO.email());
    }
}