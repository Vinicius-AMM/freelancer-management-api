package com.manager.freelancer_management_api.services.impl;

import com.manager.freelancer_management_api.domain.repositories.UserRepository;
import com.manager.freelancer_management_api.domain.user.entities.User;
import com.manager.freelancer_management_api.domain.user.enums.UserRole;
import com.manager.freelancer_management_api.domain.user.exceptions.*;
import com.manager.freelancer_management_api.utils.IsCurrentUserValid;
import com.manager.freelancer_management_api.utils.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordValidator passwordValidator;

    @Mock
    private IsCurrentUserValid isCurrentUserValid;

    @InjectMocks
    private UserServiceImpl userService;

    @Spy
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .fullName("Test Test")
                .document("12345678901")
                .email("test@test.com")
                .password("password")
                .mainUserRole(UserRole.CLIENT)
                .currentUserRole(UserRole.FREELANCER)
                .build();

        doNothing().when(isCurrentUserValid).validateAccess(testUser.getId());
    }
    private void mockUserExists() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
    }

    @Test
    void testFindById_UserExists(){
        mockUserExists();

        User foundUser = userService.findById(testUser.getId());

        assertNotNull(foundUser);
        assertEquals(foundUser.getId(), testUser.getId());
    }

    @Test
    void testFindById_UserNotFound(){
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(testUser.getId()));
    }

    @Test
    void testUpdateFullName(){
        mockUserExists();

        userService.updateFullName(testUser.getId(), "New Test Name");

        assertEquals("New Test Name", testUser.getFullName());
        verify(userRepository).save(testUser);
    }

    @Test
    void testUpdateEmail_ValidInput_UpdatesEmail(){
        mockUserExists();
        when(userRepository.existsByEmail("newemail@test.com")).thenReturn(false);

        userService.updateEmail(testUser.getId(), "password", "newemail@test.com");

        assertEquals("newemail@test.com", testUser.getEmail());
        verify(userRepository).save(testUser);
    }

    @Test
    void testUpdateEmail_ExistingEmail_ThrowsEmailAlreadyExistsException(){
        mockUserExists();
        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.updateEmail(testUser.getId(), "password", "existing@test.com"));
    }

    @Test
    void testUpdateDocument_ValidInput_UpdatesDocument(){
        mockUserExists();
        when(userRepository.existsByDocument("4444444444444")).thenReturn(false);

        userService.updateDocument(testUser.getId(), "password", "4444444444444");
        assertEquals("4444444444444", testUser.getDocument());
        verify(userRepository).save(testUser);
    }

    @Test
    void testUpdateDocument_ExistingDocument_ThrowsDocumentAlreadyExistsException(){
        mockUserExists();
        when(userRepository.existsByDocument("1111111111111")).thenReturn(true);

        assertThrows(DocumentAlreadyExistsException.class, () -> userService.updateDocument(testUser.getId(), "password", "1111111111111"));
    }

    @Test
    void testUpdatePassword_ValidInput_Success() {
        mockUserExists();

        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String encodedNewPassword = "encodeNewPassword";

        doNothing().when(passwordValidator).validate(oldPassword, testUser.getPassword());
        when(passwordEncoder.encode(newPassword)).thenReturn((encodedNewPassword));

        userService.updatePassword(testUser.getId(), oldPassword, newPassword);

        assertEquals(encodedNewPassword, testUser.getPassword());

        verify(userRepository).save(testUser);
    }
    @Test
    void testUpdatePassword_PasswordIsSameAsOld_ThrowsSamePasswordException(){
        mockUserExists();

        String oldPassword = "oldPassword";
        String newPassword = "oldPassword";

        doNothing().when(passwordValidator).validate(oldPassword, testUser.getPassword());

        assertThrows(SamePasswordException.class, () -> userService.updatePassword(testUser.getId(), oldPassword, newPassword));
    }

    @Test
    void testChangeUserRole_ValidInput_Success(){
        mockUserExists();

        String newUserRole = "client";

        userService.changeUserRole(testUser.getId(), newUserRole);

        assertEquals(UserRole.CLIENT, testUser.getCurrentUserRole());

        verify(userRepository).save(testUser);
    }

    @Test
    void testChangeUserRole_InvalidUserRole_ThrowsInvalidUserRoleException() {
        mockUserExists();

        String newUserRole = "invalid";

        assertThrows(InvalidUserRoleException.class, () -> userService.changeUserRole(testUser.getId(), newUserRole));
    }

    @Test
    void testDeleteById_ValidInput_Success(){
        mockUserExists();

        userService.deleteById(testUser.getId(), "password");

        verify(userRepository).deleteById(testUser.getId());
    }
}