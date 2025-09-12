package com.example.utkarshbackend.service;

import com.example.utkarshbackend.dto.AttendanceRequestDTO;
import com.example.utkarshbackend.dto.AttendanceResponseDTO;
import com.example.utkarshbackend.entity.Attendance;
import com.example.utkarshbackend.entity.Attendance.AttendanceStatus;
import com.example.utkarshbackend.entity.Course;
import com.example.utkarshbackend.entity.Student;
import com.example.utkarshbackend.repository.AttendanceRepository;
import com.example.utkarshbackend.repository.CourseRepository;
import com.example.utkarshbackend.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<AttendanceResponseDTO> getAttendanceByStudentRollNo(String rollNo) {
        Student student = studentRepository.findByRollNo(rollNo)
                .orElseThrow(() -> new EntityNotFoundException("Student with roll number " + rollNo + " not found."));

        List<Attendance> attendances = attendanceRepository.findByStudent(student);

        return attendances.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public AttendanceResponseDTO updateAttendance(AttendanceRequestDTO requestDTO) {
        Student student = studentRepository.findByRollNo(requestDTO.getRollNo())
                .orElseThrow(() -> new EntityNotFoundException("Student not found."));
        
        // This assumes that attendance is tied to a course. Adjust if not needed.
        Course course = courseRepository.findByCode(requestDTO.getCourseCode())
                .orElseThrow(() -> new EntityNotFoundException("Course not found."));

        Attendance attendance = attendanceRepository
                .findByStudentAndAttendanceDateAndCourse(student, requestDTO.getAttendanceDate(), course)
                .orElseGet(Attendance::new);

        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setAttendanceDate(requestDTO.getAttendanceDate());
        attendance.setStatus(AttendanceStatus.valueOf(requestDTO.getStatus().toUpperCase()));

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return mapToDto(savedAttendance);
    }

    private AttendanceResponseDTO mapToDto(Attendance attendance) {
        return new AttendanceResponseDTO(
                attendance.getAttendanceDate(),
                attendance.getStatus().name(),
                attendance.getCourse().getCode()
        );
    }
}