package com.manager.freelancer_management_api.utils;

import com.manager.freelancer_management_api.domain.user.entities.User;
import com.manager.freelancer_management_api.exceptions.UnauthorizedAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IsCurrentUserValid {

    public void validateAccess(UUID userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();

        if(!authenticatedUser.getId().equals(userId)){
            throw new UnauthorizedAccessException();
        }
    }
}

