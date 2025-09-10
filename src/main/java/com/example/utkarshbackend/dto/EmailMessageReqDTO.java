package com.example.utkarshbackend.dto;

import lombok.Data;

@Data
public class EmailMessageReqDTO {
    private String subject;
    private String message;
}
