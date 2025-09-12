package com.example.utkarshbackend.controller;

import com.example.utkarshbackend.dto.AttendanceRequestDTO;
import com.example.utkarshbackend.dto.AttendanceResponseDTO;
import com.example.utkarshbackend.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/student/{rollNo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<AttendanceResponseDTO>> getStudentAttendance(@PathVariable String rollNo) {
        List<AttendanceResponseDTO> attendances = attendanceService.getAttendanceByStudentRollNo(rollNo);
        return ResponseEntity.ok(attendances);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<AttendanceResponseDTO> updateAttendance(@Valid @RequestBody AttendanceRequestDTO requestDTO) {
        AttendanceResponseDTO updatedAttendance = attendanceService.updateAttendance(requestDTO);
        return ResponseEntity.ok(updatedAttendance);
    }
}