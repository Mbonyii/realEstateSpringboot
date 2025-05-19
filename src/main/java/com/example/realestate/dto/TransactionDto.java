package com.example.realestate.dto;

import com.example.realestate.model.Transaction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    
    private Long id;
    
    @NotNull(message = "Transaction type cannot be null")
    private Transaction.TransactionType transactionType;
    
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    private LocalDate transactionDate;
    
    private BigDecimal commission;
    
    private Transaction.Status status;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private String contractNumber;
    
    private Long propertyId;
    
    private String propertyTitle;
    
    private Long clientId;
    
    private String clientName;
    
    private Long agentId;
    
    private String agentName;
    
    private LocalDateTime createdAt;
}