package com.manager.freelancer_management_api.utils;

import com.manager.freelancer_management_api.domain.user.exceptions.InvalidPasswordException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordValidator passwordValidator;

    @Test
    void testValidatePassword_validPassword () {
        String rawPassword = "testpassword";
        String encodedPassword = "$2a$10$6QWxbg21ndMh/fOtu6tpVOw42Fd0kTYshCZF0RuKrmgL7.6YTbQQS";

        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        assertDoesNotThrow(() -> passwordValidator.validate(rawPassword, encodedPassword));

        verify(passwordEncoder).matches(rawPassword, encodedPassword);
    }

    @Test
    void testValidatePassword_invalidPassword_ThrowsBadCredentialsException() {
        String rawPassword = "testpassword";
        String encodedPassword = "$2a$10$6QWxbg21ndMh/fOtu6tpVOw42Fd0kTYshCZF0RuKrmgL7.6YTbQQS";

        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);
        assertThrows(InvalidPasswordException.class, () -> passwordValidator.validate(rawPassword, encodedPassword));

        verify(passwordEncoder).matches(rawPassword, encodedPassword);
    }
}