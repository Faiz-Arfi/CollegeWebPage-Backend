package com.example.utkarshbackend.services;

import com.example.utkarshbackend.dto.DepartmentDTO;
import com.example.utkarshbackend.dto.TeacherDetailsDTO;
import com.example.utkarshbackend.entity.Department;
import com.example.utkarshbackend.entity.Teacher;
import com.example.utkarshbackend.mapper.DepartmentMapper;
import com.example.utkarshbackend.repository.DepartmentRepo;
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
    private final DepartmentRepo departmentRepo;

    public AdminServices(TeacherRepo teacherRepo, DepartmentRepo departmentRepo) {
        this.teacherRepo = teacherRepo;
        this.departmentRepo = departmentRepo;
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
                .departmentName(savedTeacher.getDepartment().getName())
                .departmentCode(savedTeacher.getDepartment().getCode())
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
                .departmentCode(teacher.getDepartment().getCode())
                .departmentName(teacher.getDepartment().getName())
                .designation(teacher.getDesignation())
                .phone(teacher.getPhone())
                .education(teacher.getEducation())
                .build());
    }

    public DepartmentDTO registerNewDepartment(Department dept) {
        if(dept.getCode() == null || dept.getName() == null || dept.getCode().isBlank() || dept.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code or Name is Required");
        }
        Department saved = departmentRepo.save(dept);
        return DepartmentMapper.toDTO(saved);
    }

    public DepartmentDTO deleteDepartment(long deptId) {
        Department dept = departmentRepo.findById(deptId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
        departmentRepo.delete(dept);
        return DepartmentMapper.toDTO(dept);
    }
}
