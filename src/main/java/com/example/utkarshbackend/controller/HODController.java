package com.example.utkarshbackend.controller;

import com.example.utkarshbackend.dto.*;
import com.example.utkarshbackend.entity.ContactPageData;
import com.example.utkarshbackend.entity.Department;
import com.example.utkarshbackend.entity.FeeStatus;
import com.example.utkarshbackend.entity.NonTeaching;
import com.example.utkarshbackend.services.FeeService;
import com.example.utkarshbackend.services.HODService;
import com.example.utkarshbackend.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hod")
public class HODController {

    private final HODService hodService;
    private final FeeService feeService;
    private final StudentService studentService;

    public HODController(HODService hodService, FeeService feeService, StudentService studentService, StudentService studentService1) {
        this.hodService = hodService;
        this.feeService = feeService;
        this.studentService = studentService1;
    }

    @GetMapping("/get-all-teacher")
    public Page<TeacherDetailsDTO> getAllTeachers (Pageable p) {
        return hodService.getAllTeachersRoles(p);
    }

    @PostMapping("/register-teacher")
    public ResponseEntity<TeacherDetailsDTO> registerTeacher (@RequestBody TeacherRegReqDTO teacherRegReqDTO) {
        return hodService.registerNewTeacher(teacherRegReqDTO);
    }

    @DeleteMapping("/delete-teacher/{id}")
    public ResponseEntity<TeacherDetailsDTO> deleteTeacher (@PathVariable Long id, Authentication authentication) {
        return hodService.deleteTeacher(id, authentication);
    }

    @PutMapping("/update-teacher/{id}")
    public ResponseEntity<TeacherDetailsDTO> updateTeacher (@RequestBody TeacherRegReqDTO teacherRegReqDTO, @PathVariable Long id, Authentication authentication) {
        return hodService.updateTeacher(teacherRegReqDTO, id);
    }

    @PostMapping("/non-teaching")
    public ResponseEntity<NonTeaching> registerNonTeacher (@RequestBody NonTeaching nonTeaching) {
        return hodService.registerNewNonTeacher(nonTeaching);
    }

    @PutMapping("/non-teaching/{id}")
    public ResponseEntity<NonTeaching> updateNonTeacher (@RequestBody NonTeaching nonTeaching, @PathVariable Long id) {
        return hodService.updateNonTeacher(nonTeaching, id);
    }

    @DeleteMapping("/non-teaching/{id}")
    public ResponseEntity<NonTeaching> deleteNonTeacher (@PathVariable Long id) {
        return hodService.deleteNonTeacher(id);
    }

    @GetMapping("/public-contact-us")
    public Page<ContactPageData> getAllContactUsData(Pageable p) {
        return hodService.getAllContactUsData(p);
    }

    @GetMapping("/public-contact-us/{id}")
    public ContactPageData getContactUsDataById(@PathVariable Long id) {
        return hodService.getContactUsDataById(id);
    }

    @DeleteMapping("/public-contact-us/{id}")
    public ResponseEntity<ContactPageData> deleteContactUsDataById(@PathVariable Long id) {
        return hodService.deleteContactUsDataById(id);
    }

    @PutMapping("/dept/{deptId}")
    public ResponseEntity<DepartmentDTO> editDepartment (@RequestBody Department dept, Authentication authentication, @PathVariable Long deptId) {
        return ResponseEntity.ok(hodService.editDepartment(dept, deptId, authentication));
    }

    @PostMapping("/reply-contact-us/{id}")
    public ResponseEntity<ContactPageData> replyToContactUs (@PathVariable Long id, @RequestBody EmailMessageReqDTO emailMessageReqDTO) {
        return hodService.sendEmail(id, emailMessageReqDTO);
    }

    @PostMapping("/student/{studentId}")
    public ResponseEntity<FeeResponseDTO> createFeeForStudent(
            @PathVariable Long studentId,
            @Valid @RequestBody FeeRequestDTO feeRequestDTO) {
        FeeResponseDTO newFee = feeService.createFee(studentId, feeRequestDTO);
        return new ResponseEntity<>(newFee, HttpStatus.CREATED);
    }

    @DeleteMapping("/student/{studentId}/fees/{feeId}")
    public ResponseEntity<Void> deleteFeeForStudent(
            @PathVariable Long studentId,
            @PathVariable Long feeId) {
        feeService.deleteFee(studentId, feeId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/student/{studentId}/fees/{feeId}")
    public ResponseEntity<FeeResponseDTO> updateFeeForStudent(
            @PathVariable Long studentId,
            @PathVariable Long feeId,
            @Valid @RequestBody FeeRequestDTO feeRequestDTO) {
        FeeResponseDTO updatedFee = feeService.updateFee(studentId, feeId, feeRequestDTO);
        return ResponseEntity.ok(updatedFee);
    }

    @PostMapping("/student/{studentId}/fees/{feeId}/payments")
    public ResponseEntity<FeeResponseDTO> recordPaymentForStudent(
            @PathVariable Long studentId,
            @PathVariable Long feeId,
            @Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        FeeResponseDTO updatedFee = feeService.recordPayment(studentId, feeId, paymentRequestDTO);
        return ResponseEntity.ok(updatedFee);
    }

    @GetMapping("/fees")
    public ResponseEntity<List<FeeResponseDTO>> getFeesByStatus(
            @RequestParam(required = false)FeeStatus status) {
        List<FeeResponseDTO> fees = feeService.findFeesByCriteria(status);
        return ResponseEntity.ok(fees);
    }

    @GetMapping("/student")
    public ResponseEntity<Page<StudentDTO>> getAllStudents(Pageable p) {
        Page<StudentDTO> studentDTOPage = studentService.getAllStudent(p);
        return ResponseEntity.ok(studentDTOPage);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO studentDTO = studentService.getStudentById(id);
        return ResponseEntity.ok(studentDTO);
    }

}
