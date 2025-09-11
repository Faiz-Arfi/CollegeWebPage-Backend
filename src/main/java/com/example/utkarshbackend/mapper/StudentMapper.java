package com.example.utkarshbackend.mapper;

import com.example.utkarshbackend.dto.StudentDTO;
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
                .departmentId(student.getDepartment() != null ? String.valueOf(student.getDepartment().getId()) : null)
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
