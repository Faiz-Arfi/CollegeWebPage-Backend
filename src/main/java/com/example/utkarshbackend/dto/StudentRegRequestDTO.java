package com.example.utkarshbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRegRequestDTO {
    private String name;
    private String email;
    private String password;
    private Long departmentId;
    private String regNo;
    private String rollNo;
}
