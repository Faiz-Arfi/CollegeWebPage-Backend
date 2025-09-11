package com.example.utkarshbackend.services;

import com.example.utkarshbackend.dto.StudentDTO;
import com.example.utkarshbackend.entity.Student;
import com.example.utkarshbackend.mapper.StudentMapper;
import com.example.utkarshbackend.repository.StudentRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StudentService {

    private final StudentRepo studentRepo;

    public StudentService(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    public Page<StudentDTO> getAllStudent(Pageable p) {
        Page<Student> page = studentRepo.findAll(p);
        return page.map(StudentMapper::toStudentDTO);
    }

    public StudentDTO getStudentById(Long id) {;
        Student student = studentRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        return StudentMapper.toStudentDTO(student);
    }
}
