package com.example.utkarshbackend.controller;

import com.example.utkarshbackend.entity.Fee;
import com.example.utkarshbackend.services.FeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final FeeService feeService;

    public StudentController(FeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping("/{studentId}/fees")
    public ResponseEntity<List<Fee>> getStudentFees (@PathVariable Long studentId) {
        List<Fee> allFee = feeService.getAllFeesOfStudentWithId(studentId);
        return ResponseEntity.ok(allFee);
    }

    @GetMapping("/{studentId}/fees/{semester}")
    public ResponseEntity<Fee> getStudentFeeOfSemester(@PathVariable Long studentId, @PathVariable Integer semester) {
        Fee fee = feeService.getStudentFeeOfSemester(studentId, semester);
        return ResponseEntity.ok(fee);
    }

}
