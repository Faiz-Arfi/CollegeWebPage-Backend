package com.example.utkarshbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherDetailsDTO {
    private Long id;
    private String name;
    private String email;
    private String departmentName;
    private String departmentCode;
    private String designation;
    private String profilePic;
    private String phone;
    private String education;

}
