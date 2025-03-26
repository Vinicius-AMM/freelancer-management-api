package com.manager.freelancer_management_api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.freelancer_management_api.domain.user.dtos.LoginDTO;
import com.manager.freelancer_management_api.domain.user.dtos.LoginResponseDTO;
import com.manager.freelancer_management_api.domain.user.dtos.RegisterUserDTO;
import com.manager.freelancer_management_api.domain.user.enums.UserRole;
import com.manager.freelancer_management_api.domain.user.exceptions.InvalidEmailException;
import com.manager.freelancer_management_api.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterUserDTO registerUserDTO;
    @BeforeEach
    void setUp() {
        registerUserDTO = new RegisterUserDTO("Test User",
                "12345678901",
                "testemail@test.com",
                "password",
                UserRole.CLIENT,
                UserRole.FREELANCER);
    }

    @Test
    @WithMockUser
    void shouldReturnTokenWhenLoginSuccessful() throws Exception{
        LoginDTO loginDTO = new LoginDTO("testemail@test.com", "password");

        when(authenticationService.login(any(LoginDTO.class)))
                .thenReturn(new LoginResponseDTO("testToken"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("testToken"));
    }

    @Test
    @WithMockUser
    void shouldReturnErrorWhenLoginDTOInvalid() throws Exception{
        LoginDTO loginDTO = new LoginDTO("", "");

        when(authenticationService.login(any(LoginDTO.class)))
                .thenThrow(new InvalidEmailException("Email or password is wrong."));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testRegister_validData_Success() throws Exception {
        doNothing().when(authenticationService).register(any(RegisterUserDTO.class));

        mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerUserDTO))
                    .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void testRegister_invalidEmail_EmailAlreadyExists() throws Exception {
        doThrow(new InvalidEmailException("Email already exists."))
                .when(authenticationService).register(any(RegisterUserDTO.class));

        mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerUserDTO))
                    .with(csrf()))
               .andExpect(status().isUnauthorized());
    }
}