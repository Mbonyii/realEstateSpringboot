package com.example.realestate.service;

import com.example.realestate.dto.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    
    TransactionDto findById(Long id);
    
    Page<TransactionDto> findAll(Pageable pageable);
    
    Page<TransactionDto> findByClientId(Long clientId, Pageable pageable);
    
    Page<TransactionDto> findByAgentId(Long agentId, Pageable pageable);
    
    Page<TransactionDto> findByPropertyId(Long propertyId, Pageable pageable);
    
    TransactionDto save(TransactionDto transactionDto, Long userId);
    
    TransactionDto update(TransactionDto transactionDto);
    
    void delete(Long id);
}