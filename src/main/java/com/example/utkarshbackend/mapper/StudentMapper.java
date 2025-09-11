package com.example.utkarshbackend.mapper;

import com.example.utkarshbackend.dto.StudentDTO;
import com.example.utkarshbackend.entity.Department;
import com.example.utkarshbackend.entity.Student;

import java.util.List;
import java.util.stream.Collectors;

public class StudentMapper {

    public static StudentDTO toStudentDTO(Student student) {
        if (student == null) {
            return null;
        }

        return StudentDTO.builder()
                .id(student.getId())
                .regNo(student.getRegNo())
                .rollNo(student.getRollNo())
                .gender(student.getGender())
                .profilePic(student.getProfilePic())
                .name(student.getName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .address(student.getAddress())
                .dob(student.getDob())
                .admissionYear(student.getAdmissionYear())
                .admissionStatus(student.getAdmissionStatus())
                .role(student.getRole())
                // Handle the Department relationship: extract only the ID.
                // Check for null to prevent NullPointerException if a student has no department.
                .departmentId(student.getDepartment() != null ? String.valueOf(student.getDepartment().getId()) : null)
                // Directly map the list of fees. See suggestion below for improvement.
                .fees(student.getFees())
                .build();
    }

    public static List<StudentDTO> toStudentDTOList(List<Student> students) {
        if (students == null) {
            return null;
        }
        return students.stream()
                .map(StudentMapper::toStudentDTO)
                .collect(Collectors.toList());
    }
}

