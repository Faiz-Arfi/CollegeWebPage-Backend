package com.example.utkarshbackend.controller;

import com.example.utkarshbackend.dto.DepartmentDTO;
import com.example.utkarshbackend.dto.TeacherDetailsDTO;
import com.example.utkarshbackend.dto.TeacherRegReqDTO;
import com.example.utkarshbackend.entity.Department;
import com.example.utkarshbackend.services.HODService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hod")
public class HODController {

    private final HODService hodService;

    public HODController(HODService hodService) {
        this.hodService = hodService;
    }

    @GetMapping("/get-all-teacher")
    public Page<TeacherDetailsDTO> getAllTeachers (Pageable p) {
        return hodService.getAllTeachersRoles(p);
    }

    @PostMapping("/register-teacher")
    public ResponseEntity<TeacherDetailsDTO> registerTeacher (@RequestBody TeacherRegReqDTO teacherRegReqDTO) {
        return hodService.registerNewTeacher(teacherRegReqDTO);
    }

    @PutMapping("/dept")
    public ResponseEntity<DepartmentDTO> editDepartment (@RequestBody Department dept, Authentication authentication) {
        return ResponseEntity.ok(hodService.editDepartment(dept, authentication));
    }

}
