package com.example.utkarshbackend.controller;

import com.example.utkarshbackend.dto.AttendanceRequestDTO;
import com.example.utkarshbackend.dto.AttendanceResponseDTO;
import com.example.utkarshbackend.services.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/student/{rollNo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOD', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<AttendanceResponseDTO>> getStudentAttendance(@PathVariable String rollNo) {
        List<AttendanceResponseDTO> attendances = attendanceService.getAttendanceByStudentRollNo(rollNo);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/student/id/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<AttendanceResponseDTO>> getStudentAttendanceById(@PathVariable Long studentId, Authentication authentication) {
        String email = authentication.getName();
        List<AttendanceResponseDTO> attendances = attendanceService.getAttendanceByStudentId(studentId, email);
        return ResponseEntity.ok(attendances);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOD', 'TEACHER')")
    public ResponseEntity<AttendanceResponseDTO> updateAttendance(@Valid @RequestBody AttendanceRequestDTO requestDTO) {
        AttendanceResponseDTO updatedAttendance = attendanceService.updateAttendance(requestDTO);
        return ResponseEntity.ok(updatedAttendance);
    }
}