package com.example.utkarshbackend.controller;

import com.example.utkarshbackend.dto.TeacherDetailsDTO;
import com.example.utkarshbackend.services.AdminServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminServices adminServices;

    public AdminController(AdminServices adminServices) {
        this.adminServices = adminServices;
    }

    @GetMapping("/get-all-hod")
    public Page<TeacherDetailsDTO> getAllHOD (Pageable p) {
        return adminServices.getAllHODRoles(p);
    }

    @PostMapping("/promote-to-hod/{id}")
    public ResponseEntity<TeacherDetailsDTO> promoteToHOD (@PathVariable Long id) {
        return adminServices.promoteOrDemoteRoleToHOD(id, true);
    }

    @PostMapping("/demote-to-teacher/{id}")
    public ResponseEntity<TeacherDetailsDTO> demoteToTeacher (@PathVariable Long id) {
        return adminServices.promoteOrDemoteRoleToHOD(id, false);
    }

}
