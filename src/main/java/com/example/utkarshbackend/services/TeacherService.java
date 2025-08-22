package com.example.utkarshbackend.services;

import com.example.utkarshbackend.entity.Teacher;
import com.example.utkarshbackend.repository.TeacherRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TeacherService {

    private final TeacherRepo teacherRepo;

    public TeacherService(TeacherRepo teacherRepo) {
        this.teacherRepo = teacherRepo;
    }

    public Teacher getTeacherByEmail(String email) {
        return teacherRepo.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
    }
}
