package com.example.utkarshbackend.services;

import com.example.utkarshbackend.dto.CourseDTO;
import com.example.utkarshbackend.dto.DepartmentDTO;
import com.example.utkarshbackend.dto.TeacherDetailsDTO;
import com.example.utkarshbackend.entity.*;
import com.example.utkarshbackend.mapper.CourseMapper;
import com.example.utkarshbackend.mapper.DepartmentMapper;
import com.example.utkarshbackend.mapper.TeacherMapper;
import com.example.utkarshbackend.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PublicService {

    private final DepartmentRepo departmentRepo;
    private final TeacherRepo teacherRepo;
    private final NonTeachingRepo nonTeachingRepo;
    private final ContactPageDataRepo contactPageDataRepo;
    private final CourseRepo courseRepo;

    public PublicService(DepartmentRepo departmentRepo, TeacherRepo teacherRepo, NonTeachingRepo nonTeachingRepo, ContactPageDataRepo contactPageDataRepo, CourseRepo courseRepo) {
        this.departmentRepo = departmentRepo;
        this.teacherRepo = teacherRepo;
        this.nonTeachingRepo = nonTeachingRepo;
        this.contactPageDataRepo = contactPageDataRepo;
        this.courseRepo = courseRepo;
    }

    public Page<DepartmentDTO> getAllDepartment(Pageable p) {
        Page<Department> departmentPage = departmentRepo.findAll(p);
        return departmentPage.map(DepartmentMapper::toDTO);
    }

    public DepartmentDTO getDepartmentById(long id) {
        return DepartmentMapper.toDTO(departmentRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found")));
    }

    public Page<CourseDTO> getAllCourse(Pageable p) {
        Page<Course> coursePage = courseRepo.findAll(p);
        return coursePage.map(CourseMapper::toDTO);
    }

    public CourseDTO getCourseById(long id) {
        return CourseMapper.toDTO(courseRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found")));
    }

    public Page<TeacherDetailsDTO> getAllTeacher(Pageable p) {
        Page<Teacher> teacherPage = teacherRepo.findAll(p);
        return teacherPage.map(TeacherMapper::toDTO);
    }

    public TeacherDetailsDTO getTeacherById(long id) {
        return TeacherMapper.toDTO(teacherRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found")));
    }

    public Page<NonTeaching> getAllNonTeacher(Pageable p) {
        return nonTeachingRepo.findAll(p);
    }

    public NonTeaching getNonTeacherById(long id) {
        return nonTeachingRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Non Teaching Staff not found"));
    }

    public ContactPageData saveContactUsData(ContactPageData contactPageData) {
        return contactPageDataRepo.save(contactPageData);
    }
}
