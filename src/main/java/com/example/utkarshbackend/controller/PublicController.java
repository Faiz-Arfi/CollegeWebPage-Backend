package com.example.utkarshbackend.controller;

import com.example.utkarshbackend.dto.CourseDTO;
import com.example.utkarshbackend.dto.DepartmentDTO;
import com.example.utkarshbackend.dto.TeacherDetailsDTO;
import com.example.utkarshbackend.entity.NonTeaching;
import com.example.utkarshbackend.services.PublicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final PublicService publicService;

    public PublicController(PublicService publicService) {
        this.publicService = publicService;
    }

    @GetMapping("/department")
    public ResponseEntity<Page<DepartmentDTO>> getAllDepartments(Pageable p) {
        return ResponseEntity.ok(publicService.getAllDepartment(p));
    }

    @GetMapping("/department/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable long id) {
        return ResponseEntity.ok(publicService.getDepartmentById(id));
    }

    @GetMapping("/course")
    public ResponseEntity<Page<CourseDTO>> getAllCourses(Pageable p) {
        return ResponseEntity.ok(publicService.getAllCourse(p));
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable long id) {
        return ResponseEntity.ok(publicService.getCourseById(id));
    }

    @GetMapping("/teacher")
    public ResponseEntity<Page<TeacherDetailsDTO>> getAllTeachers(Pageable p) {
        return ResponseEntity.ok(publicService.getAllTeacher(p));
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<TeacherDetailsDTO> getTeacherById(@PathVariable long id) {
        return ResponseEntity.ok(publicService.getTeacherById(id));
    }

    @GetMapping("/non-teaching")
    public ResponseEntity<Page<NonTeaching>> getAllNonTeacher(Pageable p) {
        return ResponseEntity.ok(publicService.getAllNonTeacher(p));
    }

    @GetMapping("/non-teaching/{id}")
    public ResponseEntity<NonTeaching> getNonTeacherById(@PathVariable long id) {
        return ResponseEntity.ok(publicService.getNonTeacherById(id));
    }
}
