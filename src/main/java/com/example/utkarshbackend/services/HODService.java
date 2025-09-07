package com.example.utkarshbackend.services;

import com.example.utkarshbackend.dto.DepartmentDTO;
import com.example.utkarshbackend.dto.TeacherDetailsDTO;
import com.example.utkarshbackend.dto.TeacherRegReqDTO;
import com.example.utkarshbackend.entity.Department;
import com.example.utkarshbackend.entity.Teacher;
import com.example.utkarshbackend.mapper.DepartmentMapper;
import com.example.utkarshbackend.repository.DepartmentRepo;
import com.example.utkarshbackend.repository.TeacherRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HODService {

    private final TeacherRepo teacherRepo;
    private final DepartmentRepo departmentRepo;
    private final PasswordEncoder passwordEncoder;

    public HODService(TeacherRepo teacherRepo, DepartmentRepo departmentRepo, PasswordEncoder passwordEncoder) {
        this.teacherRepo = teacherRepo;
        this.departmentRepo = departmentRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean doesTeacherExistByEmail (String email) {
        return teacherRepo.existsByEmail (email);
    }

    public ResponseEntity<TeacherDetailsDTO> registerNewTeacher(TeacherRegReqDTO teacherRegReqDTO) {
        if(teacherRegReqDTO.getEmail() == null || teacherRegReqDTO.getName() == null || teacherRegReqDTO.getPassword() == null || teacherRegReqDTO.getEmail().isBlank() || teacherRegReqDTO.getName().isBlank() || teacherRegReqDTO.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email, Name or Password are Required");
        }

        if(doesTeacherExistByEmail(teacherRegReqDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        //To do : fetch department from db and set it to teacher
        Department dept = null;

        Teacher teacher = Teacher.builder()
                .name(teacherRegReqDTO.getName())
                .email(teacherRegReqDTO.getEmail())
                .password(passwordEncoder.encode(teacherRegReqDTO.getPassword()))
                .profilePic(teacherRegReqDTO.getProfilePic())
                .department(dept)
                .designation(teacherRegReqDTO.getDesignation())
                .education(teacherRegReqDTO.getEducation())
                .phone(teacherRegReqDTO.getPhone())
                .role("TEACHER")
                .build();
        Teacher savedTeacher = teacherRepo.save(teacher);

        TeacherDetailsDTO dto = TeacherDetailsDTO.builder()
                .id(savedTeacher.getId())
                .name(savedTeacher.getName())
                .email(savedTeacher.getEmail())
                .profilePic(savedTeacher.getProfilePic())
                .departmentCode(savedTeacher.getDepartment().getCode())
                .departmentName(savedTeacher.getDepartment().getName())
                .designation(savedTeacher.getDesignation())
                .phone(savedTeacher.getPhone())
                .education(savedTeacher.getEducation())
                .build();

        return ResponseEntity.ok().body(dto);
    }

    public Page<TeacherDetailsDTO> getAllTeachersRoles(Pageable p) {
        Page<Teacher> teacherPage = teacherRepo.findAllByRole("TEACHER", p);

        return teacherPage.map(teacher -> TeacherDetailsDTO.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .email(teacher.getEmail())
                .profilePic(teacher.getProfilePic())
                .departmentName(teacher.getDepartment().getName())
                .departmentCode(teacher.getDepartment().getCode())
                .designation(teacher.getDesignation())
                .phone(teacher.getPhone())
                .education(teacher.getEducation())
                .build());
    }

    public DepartmentDTO editDepartment(Department dept) {
        Department existing = departmentRepo.findById(dept.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
        if(dept.getCode() != null && !dept.getCode().isBlank()) {
            existing.setCode(dept.getCode());
        }
        if(dept.getName() != null && !dept.getName().isBlank()) {
            existing.setName(dept.getName());
        }
        if(dept.getDescription() != null && !dept.getDescription().isBlank()) {
            existing.setDescription(dept.getDescription());
        }
        Department saved = departmentRepo.save(existing);
        return DepartmentMapper.toDTO(saved);
    }
}
