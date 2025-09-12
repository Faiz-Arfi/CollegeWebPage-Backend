package com.example.utkarshbackend.repository;

import com.example.utkarshbackend.entity.Attendance;
import com.example.utkarshbackend.entity.Course;
import com.example.utkarshbackend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Find all attendance records for a given student
    List<Attendance> findByStudent(Student student);

    // Find a specific attendance record for a student on a certain date
    Optional<Attendance> findByStudentAndDateAndCourse(Student student, LocalDate date, Course course);

    Optional<Attendance> findByStudentAndDate(Student student, LocalDate date);

    // Find all attendance records for a student within a date range
    List<Attendance> findByStudentAndDateBetween(Student student, LocalDate startDate, LocalDate endDate);
}