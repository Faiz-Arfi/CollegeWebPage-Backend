package com.example.utkarshbackend.dto;

import com.example.utkarshbackend.entity.Admin;
import com.example.utkarshbackend.entity.Student;
import com.example.utkarshbackend.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private boolean isEmailVerified;
    private String role;
}
