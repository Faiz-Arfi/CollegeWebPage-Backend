package com.example.utkarshbackend.repository;

import com.example.utkarshbackend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {

    Optional<Course> findByCode(String courseCode);
}
