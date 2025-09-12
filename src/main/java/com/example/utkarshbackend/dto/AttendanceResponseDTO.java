package com.example.utkarshbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceResponseDTO {

    private LocalDate attendanceDate;
    private String status;
    private String courseCode;
}