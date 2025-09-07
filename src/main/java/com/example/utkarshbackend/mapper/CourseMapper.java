package com.example.utkarshbackend.mapper;

import com.example.utkarshbackend.dto.CourseDTO;
import com.example.utkarshbackend.entity.Course;

public class CourseMapper {

    public static CourseDTO toDTO(Course course) {

        return CourseDTO.builder()
                .id(course.getId())
                .code(course.getCode())
                .name(course.getName())
                .description(course.getDescription())
                .departmentId(course.getDepartment().getId())
                .build();

    }
}
