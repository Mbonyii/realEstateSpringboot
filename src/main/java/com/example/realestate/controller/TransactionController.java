package com.example.realestate.controller;

import com.example.realestate.dto.TransactionDto;
import com.example.realestate.model.Transaction;
import com.example.realestate.security.CurrentUser;
import com.example.realestate.security.UserPrincipal;
import com.example.realestate.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Transaction management APIs")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Get all transactions (Admin only)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<TransactionDto>> getAllTransactions(Pageable pageable) {
        Page<TransactionDto> transactions = transactionService.findAll(pageable);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transaction by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @transactionSecurity.canAccessTransaction(#id, principal)")
    public ResponseEntity<TransactionDto> getTransaction(
            @Parameter(description = "Transaction ID") @PathVariable Long id) {
        TransactionDto transaction = transactionService.findById(id);
        return ResponseEntity.ok(transaction);
    }

    @Operation(summary = "Create new transaction")
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'AGENT', 'ADMIN')")
    public ResponseEntity<TransactionDto> createTransaction(
            @Valid @RequestBody TransactionDto transactionDto,
            @CurrentUser UserPrincipal currentUser) {
        TransactionDto createdTransaction = transactionService.save(transactionDto, currentUser.getId());
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @Operation(summary = "Update transaction")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @transactionSecurity.canManageTransaction(#id, principal)")
    public ResponseEntity<TransactionDto> updateTransaction(
            @Parameter(description = "Transaction ID") @PathVariable Long id,
            @Valid @RequestBody TransactionDto transactionDto) {
        transactionDto.setId(id);
        TransactionDto updatedTransaction = transactionService.update(transactionDto);
        return ResponseEntity.ok(updatedTransaction);
    }

    @Operation(summary = "Delete transaction")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @transactionSecurity.canManageTransaction(#id, principal)")
    public ResponseEntity<Void> deleteTransaction(
            @Parameter(description = "Transaction ID") @PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get client's transactions")
    @GetMapping("/client")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Page<TransactionDto>> getClientTransactions(
            @CurrentUser UserPrincipal currentUser,
            Pageable pageable) {
        Page<TransactionDto> transactions = transactionService.findByClientId(currentUser.getId(), pageable);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get agent's transactions")
    @GetMapping("/agent")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<Page<TransactionDto>> getAgentTransactions(
            @CurrentUser UserPrincipal currentUser,
            Pageable pageable) {
        Page<TransactionDto> transactions = transactionService.findByAgentId(currentUser.getId(), pageable);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get property's transactions")
    @GetMapping("/property/{propertyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<Page<TransactionDto>> getPropertyTransactions(
            @Parameter(description = "Property ID") @PathVariable Long propertyId,
            Pageable pageable) {
        Page<TransactionDto> transactions = transactionService.findByPropertyId(propertyId, pageable);
        return ResponseEntity.ok(transactions);
    }
}