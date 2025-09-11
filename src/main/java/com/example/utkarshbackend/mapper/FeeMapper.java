package com.example.utkarshbackend.mapper;

import com.example.utkarshbackend.dto.FeeResponseDTO;
import com.example.utkarshbackend.entity.Fee;

public class FeeMapper {

    public static FeeResponseDTO toFeeResponseDTO(Fee fee) {
        return FeeResponseDTO.builder()
                .amountPaid(fee.getAmountPaid())
                .id(fee.getId())
                .invoiceNumber(fee.getInvoiceNumber())
                .semester(fee.getSemester())
                .status(String.valueOf(fee.getStatus()))
                .totalAmount(fee.getTotalAmount())
                .build();
    }
}
