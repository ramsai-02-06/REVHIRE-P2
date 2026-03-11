package com.revhire.revhirep2.mapper;

import com.revhire.revhirep2.dto.UserRegisterDTO;
import com.revhire.revhirep2.dto.UserResponseDTO;
import com.revhire.revhirep2.entity.User;

public class UserMapper {

    public static User toEntity(UserRegisterDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
      user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setSecurityQuestion(dto.getSecurityQuestion());
        user.setSecurityAnswer(dto.getSecurityAnswer());
        return user;
    }

    public static UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setIsActive(user.getIsActive());
        return dto;
    }
}