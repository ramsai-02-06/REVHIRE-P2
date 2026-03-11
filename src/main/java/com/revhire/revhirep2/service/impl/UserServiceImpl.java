package com.revhire.revhirep2.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.revhire.revhirep2.dto.SecurityQuestionDTO;
import com.revhire.revhirep2.entity.User;
import com.revhire.revhirep2.exception.BadRequestException;
import com.revhire.revhirep2.exception.ResourceNotFoundException;
import com.revhire.revhirep2.repository.UserRepository;
import com.revhire.revhirep2.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;   // ✅ Inject encoder

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setSecurityAnswer(passwordEncoder.encode(user.getSecurityAnswer()));

        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public Optional<SecurityQuestionDTO> getSecurityQuestion(String email) {

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();

        SecurityQuestionDTO dto =
                new SecurityQuestionDTO(
                        user.getEmail(),
                        user.getSecurityQuestion()
                );

        return Optional.of(dto);
    }
    
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
        		.orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setIsActive(false);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

  
    
    @Override
    public void resetPassword(String email,
                              String securityAnswer,
                              String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(securityAnswer,
                user.getSecurityAnswer())) {
            throw new BadRequestException("Security answer incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }
}