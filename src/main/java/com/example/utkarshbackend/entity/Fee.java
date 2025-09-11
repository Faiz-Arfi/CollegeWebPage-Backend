package com.example.utkarshbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String invoiceNumber;

    @Column(nullable = false)
    private Integer semester;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amountPaid = BigDecimal.ZERO; // Default to 0

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeeStatus status = FeeStatus.UNPAID; // Default to UNPAID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    private Student student;

    public void updateStatus() {
        if (this.amountPaid.compareTo(BigDecimal.ZERO) <= 0) {
            this.status = FeeStatus.UNPAID;
        } else if (this.amountPaid.compareTo(this.totalAmount) >= 0) {
            this.status = FeeStatus.PAID;
            if (this.paymentDate == null) {
                this.paymentDate = LocalDate.now();
            }
        } else {
            this.status = FeeStatus.PARTIALLY_PAID;
        }
    }


}
