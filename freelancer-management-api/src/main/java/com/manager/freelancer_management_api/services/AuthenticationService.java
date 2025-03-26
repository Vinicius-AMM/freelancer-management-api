package com.manager.freelancer_management_api.services;

import com.manager.freelancer_management_api.domain.user.dtos.LoginDTO;
import com.manager.freelancer_management_api.domain.user.dtos.LoginResponseDTO;
import com.manager.freelancer_management_api.domain.user.dtos.RegisterUserDTO;

public interface AuthenticationService {
    LoginResponseDTO login(LoginDTO login);
    void register(RegisterUserDTO registerData);
}
