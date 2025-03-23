package com.manager.freelancer_management_api.services.impl;

import com.manager.freelancer_management_api.domain.user.entities.User;
import com.manager.freelancer_management_api.domain.repositories.UserRepository;
import com.manager.freelancer_management_api.domain.user.enums.UserRole;
import com.manager.freelancer_management_api.domain.user.exceptions.*;
import com.manager.freelancer_management_api.services.UserService;
import com.manager.freelancer_management_api.utils.IsCurrentUserValid;
import com.manager.freelancer_management_api.utils.PasswordValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final IsCurrentUserValid isCurrentUserValid;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordValidator passwordValidator, IsCurrentUserValid isCurrentUserValid) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
        this.isCurrentUserValid = isCurrentUserValid;
    }

    @Override
    public User findById(UUID id) {
        isCurrentUserValid.validateAccess(id);

        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void updateFullName(UUID id, String newFullName) {
        isCurrentUserValid.validateAccess(id);

        User user = findById(id);
        user.setFullName(newFullName);
        userRepository.save(user);
    }

    @Override
    public void updateEmail(UUID id, String password, String newEmail) {
        isCurrentUserValid.validateAccess(id);

        User user = findById(id);
        passwordValidator.validate(password, user.getPassword());
        if(userRepository.existsByEmail(newEmail)){
            throw new EmailAlreadyExistsException();
        }
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Override
    public void updateDocument(UUID id, String password, String newDocument) {
        isCurrentUserValid.validateAccess(id);

        User user = findById(id);
        passwordValidator.validate(password, user.getPassword());
        if(userRepository.existsByDocument(newDocument)){
            throw new DocumentAlreadyExistsException();
        }
        user.setDocument(newDocument);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(UUID id, String oldPassword, String newPassword) {
        isCurrentUserValid.validateAccess(id);

        User user = findById(id);
        passwordValidator.validate(oldPassword, user.getPassword());
        if(oldPassword.equals(newPassword)){
            throw new SamePasswordException();
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void changeUserRole(UUID id, String newUserRole) {
        isCurrentUserValid.validateAccess(id);

        User user = findById(id);
        try{
            user.setCurrentUserRole(UserRole.valueOf(newUserRole.toUpperCase()));
            userRepository.save(user);
        } catch (IllegalArgumentException e){
            throw new InvalidUserRoleException();
        }
    }

    @Override
    public void deleteById(UUID id, String userPassword) {
        isCurrentUserValid.validateAccess(id);

        User user = findById(id);
        passwordValidator.validate(userPassword, user.getPassword());
        userRepository.deleteById(id);
    }
}
