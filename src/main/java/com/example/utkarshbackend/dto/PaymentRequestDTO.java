package com.example.utkarshbackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    @NotNull(message = "Payment amount cannot be null")
    @Positive(message = "Payment amount must be positive")
    private BigDecimal amountPaid;
    private String invoiceNumber;
}
