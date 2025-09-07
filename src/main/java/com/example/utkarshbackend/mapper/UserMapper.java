package com.example.utkarshbackend.mapper;

import com.example.utkarshbackend.dto.AuthUser;
import com.example.utkarshbackend.dto.UserDTO;
import com.example.utkarshbackend.entity.Admin;
import com.example.utkarshbackend.entity.Student;
import com.example.utkarshbackend.entity.Teacher;

//Mapper Class to convert Entity to DTO of UserDTO and AuthUser which is also a kind of UserDTO
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

    public static AuthUser toAuthUser(Student student) {
        return buildAuthUser(student.getId(), student.getEmail(), student.getRole());
    }

    public static AuthUser toAuthUser(Teacher teacher) {
        return buildAuthUser(teacher.getId(), teacher.getEmail(), teacher.getRole());
    }

    public static AuthUser toAuthUser(Admin admin) {
        return buildAuthUser(admin.getId(), admin.getEmail(), admin.getRole());
    }

    private static AuthUser buildAuthUser(Long id, String email, String role) {
        return AuthUser.builder()
                .id(id)
                .email(email)
                .role(role)
                .build();
    }
}
