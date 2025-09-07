package com.example.utkarshbackend.mapper;

import com.example.utkarshbackend.dto.UserDTO;
import com.example.utkarshbackend.entity.Admin;
import com.example.utkarshbackend.entity.Student;
import com.example.utkarshbackend.entity.Teacher;

public class UserMapper {

    public static UserDTO toDTO(Teacher user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    public static UserDTO toDTO(Admin user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    public static UserDTO toDTO(Student user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
