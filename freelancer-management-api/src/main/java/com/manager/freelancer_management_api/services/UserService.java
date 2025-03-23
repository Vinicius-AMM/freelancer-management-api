package com.manager.freelancer_management_api.services;

import com.manager.freelancer_management_api.domain.user.entities.User;

import java.util.UUID;

public interface UserService {
    User findById(UUID id);
    void updateFullName(UUID id, String newName);
    void updateEmail(UUID id, String password, String newEmail);
    void updateDocument(UUID id, String password, String newDocument);
    void updatePassword(UUID id, String oldPassword, String newPassword);
    void changeUserRole(UUID id, String newUserRole);
    void deleteById(UUID id, String userPassword);
}
