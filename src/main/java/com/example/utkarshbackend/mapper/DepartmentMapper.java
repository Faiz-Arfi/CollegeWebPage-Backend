package com.example.utkarshbackend.mapper;

import com.example.utkarshbackend.dto.CourseDTO;
import com.example.utkarshbackend.dto.DepartmentDTO;
import com.example.utkarshbackend.dto.TeacherDetailsDTO;
import com.example.utkarshbackend.entity.Course;
import com.example.utkarshbackend.entity.Department;
import com.example.utkarshbackend.entity.Teacher;

import java.util.stream.Collectors;

public class DepartmentMapper {

    public static DepartmentDTO toDTO(Department department) {
        return DepartmentDTO.builder()
                .id(department.getId())
                .code(department.getCode())
                .name(department.getName())
                .description(department.getDescription())
                .courses(
                        department.getCourses().stream()
                                .map(DepartmentMapper::toCourseDTO)
                                .collect(Collectors.toList())
                )
                .teachers(
                        department.getTeachers().stream()
                                .map(DepartmentMapper::toTeacherDTO)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private static CourseDTO toCourseDTO(Course course) {
        return CourseDTO.builder()
                .id(course.getId())
                .code(course.getCode())
                .name(course.getName())
                .description(course.getDescription())
                .departmentId(course.getDepartment().getId())
                .build();
    }

    private static TeacherDetailsDTO toTeacherDTO(Teacher teacher) {
        return TeacherDetailsDTO.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .email(teacher.getEmail())
                .departmentName(teacher.getDepartment().getName())
                .departmentCode(teacher.getDepartment().getCode())
                .designation(teacher.getDesignation())
                .phone(teacher.getPhone())
                .education(teacher.getEducation())
                .profilePic(teacher.getProfilePic())
                .build();
    }
}
