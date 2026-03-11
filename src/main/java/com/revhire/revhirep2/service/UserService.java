package com.revhire.revhirep2.service;

import java.util.Optional;

import com.revhire.revhirep2.dto.SecurityQuestionDTO;
import com.revhire.revhirep2.entity.User;

public interface UserService {

    User registerUser(User user);

    Optional<User> getUserByEmail(String email);

    Optional<SecurityQuestionDTO> getSecurityQuestion(String email);
    
    Optional<User> getUserById(Long id);

    void deactivateUser(Long userId);

      
    void resetPassword(String email,
            String securityAnswer,
            String newPassword);
}