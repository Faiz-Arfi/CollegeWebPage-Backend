package com.example.utkarshbackend.dto;

import com.example.utkarshbackend.entity.Fee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO {

    private Long id;
    private String regNo;
    private String rollNo;
    private String gender;
    private String profilePic;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String dob;
    private String admissionYear;
    private String admissionStatus;
    private String role;

    private String departmentId;

    private List<Fee> fees;

}