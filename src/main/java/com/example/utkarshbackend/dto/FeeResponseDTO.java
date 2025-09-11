package com.example.utkarshbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeeResponseDTO {
    private Long id;
    private String invoiceNumber;
    private Integer semester;
    private BigDecimal totalAmount;
    private BigDecimal amountPaid;
    private String status;
}
