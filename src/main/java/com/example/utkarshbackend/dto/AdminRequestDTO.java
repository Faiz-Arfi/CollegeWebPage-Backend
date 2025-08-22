package com.example.utkarshbackend.dto;

import lombok.Data;

@Data
public class AdminRequestDTO {
    private String email;
    private String password;
    private String newPassword;
}
