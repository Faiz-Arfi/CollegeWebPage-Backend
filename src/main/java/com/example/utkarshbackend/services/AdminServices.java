package com.example.utkarshbackend.services;

import com.example.utkarshbackend.dto.TeacherDetailsDTO;
import com.example.utkarshbackend.entity.Teacher;
import com.example.utkarshbackend.repository.TeacherRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminServices {

    private final TeacherRepo teacherRepo;

    public AdminServices(TeacherRepo teacherRepo) {
        this.teacherRepo = teacherRepo;
    }

    private Teacher getTeacherById(Long id) {
        return teacherRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
    }

    public ResponseEntity<TeacherDetailsDTO> promoteOrDemoteRoleToHOD(Long id, boolean isPromoted) {
        Teacher teacher = getTeacherById(id);

        if(isPromoted) {
            if(teacher.getRole().equalsIgnoreCase("HOD")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teacher is already a HOD");
            }
            teacher.setRole("HOD");
            teacher.setDesignation("HOD");
        } else {
            if(teacher.getRole().equalsIgnoreCase("TEACHER")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teacher is already a Teacher");
            }
            teacher.setRole("TEACHER");
            teacher.setDesignation("Professor");
        }

        Teacher savedTeacher = teacherRepo.save(teacher);
        TeacherDetailsDTO dto = TeacherDetailsDTO.builder()
                .id(savedTeacher.getId())
                .name(savedTeacher.getName())
                .email(savedTeacher.getEmail())
                .profilePic(savedTeacher.getProfilePic())
                .department(savedTeacher.getDepartment())
                .designation(savedTeacher.getDesignation())
                .phone(savedTeacher.getPhone())
                .education(savedTeacher.getEducation())
                .build();

        return ResponseEntity.ok().body(dto);
    }

    public Page<TeacherDetailsDTO> getAllHODRoles(Pageable p) {
        Page<Teacher> teacherPage = teacherRepo.getAllByRole("HOD", p);

        return teacherPage.map(teacher -> TeacherDetailsDTO.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .email(teacher.getEmail())
                .profilePic(teacher.getProfilePic())
                .department(teacher.getDepartment())
                .designation(teacher.getDesignation())
                .phone(teacher.getPhone())
                .education(teacher.getEducation())
                .build());
    }
}
