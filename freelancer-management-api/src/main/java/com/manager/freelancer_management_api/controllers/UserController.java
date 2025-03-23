package com.manager.freelancer_management_api.controllers;

import com.manager.freelancer_management_api.domain.user.entities.User;
import com.manager.freelancer_management_api.domain.user.dtos.*;
import com.manager.freelancer_management_api.infra.security.SecurityConfig;
import com.manager.freelancer_management_api.services.UserService;
import com.manager.freelancer_management_api.domain.dtos.SuccessResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}/updateFullName")
    public ResponseEntity<SuccessResponseDTO> updateFullName(@PathVariable UUID id, @RequestBody FullNameUpdateRequest request){
        userService.updateFullName(id, request.newFullName());
        return ResponseEntity.ok(new SuccessResponseDTO("Full name updated successfully"));
    }

    @PatchMapping("/{id}/updateEmail")
    public ResponseEntity<SuccessResponseDTO> updateEmail(@PathVariable UUID id, @RequestBody EmailUpdateRequest request){
        userService.updateEmail(id, request.password(), request.email());
        return ResponseEntity.ok(new SuccessResponseDTO("Email updated successfully"));
    }
    @PatchMapping("/{id}/updateDocument")
    public ResponseEntity<SuccessResponseDTO> updateDocument(@PathVariable UUID id, @RequestBody DocumentUpdateRequest request){
        userService.updateDocument(id, request.password(), request.newDocument());
        return ResponseEntity.ok(new SuccessResponseDTO("Document updated successfully"));
    }
    @PatchMapping("/{id}/updatePassword")
    public ResponseEntity<SuccessResponseDTO> updatePassword(@PathVariable UUID id, @RequestBody PasswordUpdateRequest request){
        userService.updatePassword(id, request.oldPassword(), request.newPassword());
        return ResponseEntity.ok(new SuccessResponseDTO("Password updated successfully"));
    }
    @PutMapping("/{id}/changeUserRole")
    public ResponseEntity<SuccessResponseDTO> changeUserRole(@PathVariable UUID id, @RequestBody UserRoleUpdateRequest request){
        userService.changeUserRole(id, request.newUserRole());
        return ResponseEntity.ok(new SuccessResponseDTO("User role updated successfully"));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponseDTO> deleteUser(@PathVariable UUID id, @RequestBody DeleteUserRequest request){
        userService.deleteById(id, request.password());
        return ResponseEntity.ok(new SuccessResponseDTO("User deleted successfully"));
    }
}
