package com.example.utkarshbackend.repository;

import com.example.utkarshbackend.entity.Fee;
import com.example.utkarshbackend.entity.FeeStatus;
import com.example.utkarshbackend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeeRepo extends JpaRepository<Fee, Long> {
    Optional<Fee> findByStudentIdAndSemester(Long studentId, Integer semester);

    List<Fee> findByStatus(FeeStatus status);
}
