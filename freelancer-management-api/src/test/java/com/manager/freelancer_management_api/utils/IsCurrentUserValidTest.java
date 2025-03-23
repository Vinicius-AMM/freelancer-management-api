package com.manager.freelancer_management_api.utils;

import com.manager.freelancer_management_api.domain.user.entities.User;
import com.manager.freelancer_management_api.exceptions.UnauthorizedAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IsCurrentUserValidTest {

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private IsCurrentUserValid isCurrentUserValid;

    @Mock
    private User mockUser;


    private UUID userId = UUID.randomUUID();
    private UUID differentUUID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        when(mockUser.getId()).thenReturn(userId);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testWhenUserIdMatches(){
        assertDoesNotThrow(() -> isCurrentUserValid.validateAccess(userId));

        verify(mockUser).getId();
        verify(authentication).getPrincipal();
    }

    @Test
    void testWhenUserIdDoesNotMatch_ThrowsUnauthorizedAccessException(){
        assertThrows(UnauthorizedAccessException.class, () -> isCurrentUserValid.validateAccess(differentUUID));

        verify(mockUser).getId();
        verify(authentication).getPrincipal();
    }

}