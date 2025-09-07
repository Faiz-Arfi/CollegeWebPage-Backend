package com.example.utkarshbackend.repository;

import com.example.utkarshbackend.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Teacher> getAllByRole(String hod, Pageable p);

    Page<Teacher> findAllByRole(String teacher, Pageable p);
}
