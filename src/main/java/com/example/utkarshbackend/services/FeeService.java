package com.example.utkarshbackend.services;

import com.example.utkarshbackend.dto.FeeRequestDTO;
import com.example.utkarshbackend.dto.FeeResponseDTO;
import com.example.utkarshbackend.dto.PaymentRequestDTO;
import com.example.utkarshbackend.dto.StudentDTO;
import com.example.utkarshbackend.entity.Fee;
import com.example.utkarshbackend.entity.FeeStatus;
import com.example.utkarshbackend.entity.Student;
import com.example.utkarshbackend.mapper.FeeMapper;
import com.example.utkarshbackend.mapper.StudentMapper;
import com.example.utkarshbackend.repository.FeeRepo;
import com.example.utkarshbackend.repository.StudentRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeeService {

    private final FeeRepo feeRepo;
    private final StudentRepo studentRepo;

    public FeeService(FeeRepo feeRepo, StudentRepo studentRepo) {
        this.feeRepo = feeRepo;
        this.studentRepo = studentRepo;
    }

    public List<Fee> getAllFeesOfStudentWithId(Long studentId) {
        Student student = findStudentById(studentId);

        return student.getFees();
    }

    public Fee getStudentFeeOfSemester(Long studentId, Integer semester) {
        findStudentById(studentId);
        //get the fee of specific semester
        return feeRepo.findByStudentIdAndSemester(studentId, semester)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fee not found for semester: " + semester));
    }

    public FeeResponseDTO createFee(Long studentId, @Valid FeeRequestDTO feeRequestDTO) {

        Student student = findStudentById(studentId);

        Fee newFee = Fee.builder()
                .semester(feeRequestDTO.getSemester())
                .totalAmount(feeRequestDTO.getTotalAmount())
                .dueDate(feeRequestDTO.getDueDate())
                .amountPaid(BigDecimal.ZERO)
                .build();
        newFee.updateStatus();
        newFee.setStudent(student);
        student.getFees().add(newFee);

        Student saved = studentRepo.save(student);

        return FeeMapper.toFeeResponseDTO(saved.getFees().get(saved.getFees().size() - 1));
    }

    @Transactional
    public FeeResponseDTO recordPayment(Long studentId, Long feeId, @Valid PaymentRequestDTO paymentRequest) {
        Student student = findStudentById(studentId);
        Fee studentFee = findFeeById(feeId);

        // This check is still perfect and necessary
        if (!studentFee.getStudent().getId().equals(student.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mismatch: The specified fee does not belong to the student.");
        }

        BigDecimal newPayment = paymentRequest.getAmountPaid();
        BigDecimal currentPaid = studentFee.getAmountPaid();
        BigDecimal totalAmount = studentFee.getTotalAmount();

        // CRITICAL: Add a check to prevent overpayment
        if (currentPaid.add(newPayment).compareTo(totalAmount) > 0) {
            BigDecimal remainingBalance = totalAmount.subtract(currentPaid);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Payment of " + newPayment + " exceeds the remaining balance of " + remainingBalance);
        }

        studentFee.setAmountPaid(currentPaid.add(newPayment));
        studentFee.setInvoiceNumber(paymentRequest.getInvoiceNumber());
        studentFee.updateStatus();

        Fee updatedFee = feeRepo.save(studentFee);

        return FeeMapper.toFeeResponseDTO(updatedFee);
    }

    @Transactional
    public void deleteFee(Long studentId, Long feeId) {
        Student student = findStudentById(studentId);
        Fee feeToDelete = findFeeById(feeId);

        //check if fee belongs to the student
        if (!feeToDelete.getStudent().getId().equals(student.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mismatch: The specified fee does not belong to the student.");
        }

        student.getFees().remove(feeToDelete);

        studentRepo.save(student);
    }

    @Transactional
    public FeeResponseDTO updateFee(Long studentId, Long feeId, @Valid FeeRequestDTO feeRequestDTO) {
        Student student = findStudentById(studentId);
        Fee fee = findFeeById(feeId);

        //check if fee belongs to the student
        if (!fee.getStudent().getId().equals(student.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mismatch: The specified fee does not belong to the student.");
        }

        fee.setSemester(feeRequestDTO.getSemester());
        fee.setTotalAmount(feeRequestDTO.getTotalAmount());
        fee.setDueDate(feeRequestDTO.getDueDate());

        fee.updateStatus();

        Fee updatedFee = feeRepo.save(fee);

        return FeeMapper.toFeeResponseDTO(updatedFee);
    }

    public List<FeeResponseDTO> findFeesByCriteria(FeeStatus status) {
        List<Fee> fees;

        // if status is provided find by status
        if (status != null) {
            fees = feeRepo.findByStatus(status);
        }
        // Otherwise, just get everything.
        else {
            fees = feeRepo.findAll();
        }

        return fees.stream()
                .map(FeeMapper::toFeeResponseDTO)
                .collect(Collectors.toList());
    }

    private Student findStudentById(Long id) {
        return studentRepo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with id not found"));
    }

    private Fee findFeeById(Long id) {
        return feeRepo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fee with id not found"));
    }

    public StudentDTO getStudentById(Long studentId, Authentication authentication) {
        Student student = findStudentById(studentId);
        String email = authentication.getName();
        if(student.getEmail().equals(email)) {
            return StudentMapper.toStudentDTO(student);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view this student");
        }
    }
}
