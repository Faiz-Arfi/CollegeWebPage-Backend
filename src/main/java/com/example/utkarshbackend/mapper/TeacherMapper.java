package com.example.utkarshbackend.mapper;

import com.example.utkarshbackend.dto.TeacherDetailsDTO;
import com.example.utkarshbackend.entity.Teacher;

public class TeacherMapper {

    public static TeacherDetailsDTO toDTO(Teacher teacher) {
        return TeacherDetailsDTO.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .email(teacher.getEmail())
                .profilePic(teacher.getProfilePic())
                .departmentName(teacher.getDepartment().getName())
                .departmentCode(teacher.getDepartment().getCode())
                .designation(teacher.getDesignation())
                .phone(teacher.getPhone())
                .education(teacher.getEducation())
                .build();
    }
}
