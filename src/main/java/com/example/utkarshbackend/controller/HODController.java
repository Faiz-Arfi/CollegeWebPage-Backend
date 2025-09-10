package com.example.utkarshbackend.controller;

import com.example.utkarshbackend.dto.DepartmentDTO;
import com.example.utkarshbackend.dto.EmailMessageReqDTO;
import com.example.utkarshbackend.dto.TeacherDetailsDTO;
import com.example.utkarshbackend.dto.TeacherRegReqDTO;
import com.example.utkarshbackend.entity.ContactPageData;
import com.example.utkarshbackend.entity.Department;
import com.example.utkarshbackend.entity.NonTeaching;
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

    @DeleteMapping("/delete-teacher/{id}")
    public ResponseEntity<TeacherDetailsDTO> deleteTeacher (@PathVariable Long id, Authentication authentication) {
        return hodService.deleteTeacher(id, authentication);
    }

    @PutMapping("/update-teacher/{id}")
    public ResponseEntity<TeacherDetailsDTO> updateTeacher (@RequestBody TeacherRegReqDTO teacherRegReqDTO, @PathVariable Long id, Authentication authentication) {
        return hodService.updateTeacher(teacherRegReqDTO, id);
    }

    @PostMapping("/non-teaching")
    public ResponseEntity<NonTeaching> registerNonTeacher (@RequestBody NonTeaching nonTeaching) {
        return hodService.registerNewNonTeacher(nonTeaching);
    }

    @PutMapping("/non-teaching/{id}")
    public ResponseEntity<NonTeaching> updateNonTeacher (@RequestBody NonTeaching nonTeaching, @PathVariable Long id) {
        return hodService.updateNonTeacher(nonTeaching, id);
    }

    @DeleteMapping("/non-teaching/{id}")
    public ResponseEntity<NonTeaching> deleteNonTeacher (@PathVariable Long id) {
        return hodService.deleteNonTeacher(id);
    }

    @GetMapping("/public-contact-us")
    public Page<ContactPageData> getAllContactUsData(Pageable p) {
        return hodService.getAllContactUsData(p);
    }

    @GetMapping("/public-contact-us/{id}")
    public ContactPageData getContactUsDataById(@PathVariable Long id) {
        return hodService.getContactUsDataById(id);
    }

    @DeleteMapping("/public-contact-us/{id}")
    public ResponseEntity<ContactPageData> deleteContactUsDataById(@PathVariable Long id) {
        return hodService.deleteContactUsDataById(id);
    }

    @PutMapping("/dept/{deptId}")
    public ResponseEntity<DepartmentDTO> editDepartment (@RequestBody Department dept, Authentication authentication, @PathVariable Long deptId) {
        return ResponseEntity.ok(hodService.editDepartment(dept, deptId, authentication));
    }

    @PostMapping("/reply-contact-us/{id}")
    public ResponseEntity<ContactPageData> replyToContactUs (@PathVariable Long id, @RequestBody EmailMessageReqDTO emailMessageReqDTO) {
        return hodService.sendEmail(id, emailMessageReqDTO);
    }

}
