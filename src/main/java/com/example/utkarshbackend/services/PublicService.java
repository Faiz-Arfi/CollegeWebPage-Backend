package com.example.utkarshbackend.services;

import com.example.utkarshbackend.dto.CourseDTO;
import com.example.utkarshbackend.dto.DepartmentDTO;
import com.example.utkarshbackend.dto.TeacherDetailsDTO;
import com.example.utkarshbackend.entity.Course;
import com.example.utkarshbackend.entity.Department;
import com.example.utkarshbackend.entity.Teacher;
import com.example.utkarshbackend.mapper.CourseMapper;
import com.example.utkarshbackend.mapper.DepartmentMapper;
import com.example.utkarshbackend.mapper.TeacherMapper;
import com.example.utkarshbackend.repository.CourseRepo;
import com.example.utkarshbackend.repository.DepartmentRepo;
import com.example.utkarshbackend.repository.TeacherRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PublicService {

    private final DepartmentRepo departmentRepo;
    private final TeacherRepo teacherRepo;
    private final CourseRepo courseRepo;

    public PublicService(DepartmentRepo departmentRepo, TeacherRepo teacherRepo, CourseRepo courseRepo) {
        this.departmentRepo = departmentRepo;
        this.teacherRepo = teacherRepo;
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
}
