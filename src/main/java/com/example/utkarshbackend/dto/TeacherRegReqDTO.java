package com.example.utkarshbackend.dto;

import lombok.Data;

@Data
public class TeacherRegReqDTO {
    private Long departmentId;
    private String name;
    private String profilePic;
    private String email;
    private String password;
    private String designation;
    private String phone;
    private String education;
}
