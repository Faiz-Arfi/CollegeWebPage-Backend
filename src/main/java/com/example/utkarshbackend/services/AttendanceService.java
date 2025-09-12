package com.example.utkarshbackend.services;

import com.example.utkarshbackend.dto.AttendanceRequestDTO;
import com.example.utkarshbackend.dto.AttendanceResponseDTO;
import com.example.utkarshbackend.entity.Attendance;
import com.example.utkarshbackend.entity.Student;
import com.example.utkarshbackend.entity.constants.AttendanceStatus;
import com.example.utkarshbackend.repository.AttendanceRepository;
import com.example.utkarshbackend.repository.CourseRepo;
import com.example.utkarshbackend.repository.StudentRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepo studentRepository;
    private final CourseRepo courseRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, StudentRepo studentRepository, CourseRepo courseRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

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
//        Course course = courseRepository.findByCode(requestDTO.getCourseCode())
//                .orElseThrow(() -> new EntityNotFoundException("Course not found."));

        //This will we used when course will be integrated with attendance.
//        Attendance attendance = attendanceRepository
//                .findByStudentAndDateAndCourse(student, requestDTO.getAttendanceDate(), course)
//                .orElseGet(Attendance::new);

        Attendance attendance = attendanceRepository
                .findByStudentAndDate(student, requestDTO.getAttendanceDate())
                .orElseGet(Attendance::new);

        attendance.setStudent(student);
        //This will be used when course is integrated with attendance.
        // attendance.setCourse(course);
        attendance.setDate(requestDTO.getAttendanceDate());
        attendance.setStatus(AttendanceStatus.valueOf(requestDTO.getStatus().toUpperCase()));

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return mapToDto(savedAttendance);
    }

    private AttendanceResponseDTO mapToDto(Attendance attendance) {
        String status = attendance.getStatus() != null ? attendance.getStatus().name() : null;
        String courseCode = (attendance.getCourse() != null) ? attendance.getCourse().getCode() : null;
        return new AttendanceResponseDTO(
                attendance.getDate(),
                status,
                courseCode
        );
    }

    public List<AttendanceResponseDTO> getAttendanceByStudentId(Long studentId, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found."));
        if(!student.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view this student");
        }
        return getAttendanceByStudentRollNo(student.getRollNo());
    }
}