package com.example.utkarshbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRequestDTO {

    @NotBlank(message = "Roll number is mandatory")
    private String rollNo;

    @NotNull(message = "Attendance date is mandatory")
    private LocalDate attendanceDate;

    @NotBlank(message = "Attendance status is mandatory")
    private String status; // Using String to match the frontend's values (e.g., "Present", "Absent")

    private String courseCode; // Optional: To handle attendance on a per-course basis
}