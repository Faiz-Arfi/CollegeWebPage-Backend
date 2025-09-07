package com.example.utkarshbackend.repository;

import com.example.utkarshbackend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {
    boolean existsByRegNo(String regNo);

    Optional<Student> findByEmail(String email);
}
