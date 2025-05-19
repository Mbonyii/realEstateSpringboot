package com.example.realestate.service.impl;

import com.example.realestate.dto.TransactionDto;
import com.example.realestate.exception.ResourceNotFoundException;
import com.example.realestate.model.Property;
import com.example.realestate.model.Transaction;
import com.example.realestate.model.User;
import com.example.realestate.repository.PropertyRepository;
import com.example.realestate.repository.TransactionRepository;
import com.example.realestate.repository.UserRepository;
import com.example.realestate.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public TransactionDto findById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
        return convertToDto(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> findAll(Pageable pageable) {
        return transactionRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> findByClientId(Long clientId, Pageable pageable) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", clientId));
        Page<Transaction> transactions = transactionRepository.findByClient(client, pageable);
        return transactions.map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> findByAgentId(Long agentId, Pageable pageable) {
        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", agentId));
        Page<Transaction> transactions = transactionRepository.findByAgent(agent, pageable);
        return transactions.map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> findByPropertyId(Long propertyId, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findByPropertyId(propertyId, pageable);
        return transactions.map(this::convertToDto);
    }

    @Override
    public TransactionDto save(TransactionDto transactionDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Property property = propertyRepository.findById(transactionDto.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property", "id", transactionDto.getPropertyId()));

        Transaction transaction = new Transaction();
        transaction.setProperty(property);
        transaction.setClient(user);
        transaction.setAgent(property.getAgent());
        transaction.setTransactionType(transactionDto.getTransactionType());
        transaction.setStatus(Transaction.Status.PENDING);
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionDate(transactionDto.getTransactionDate());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDto(savedTransaction);
    }

    @Override
    public TransactionDto update(TransactionDto transactionDto) {
        Transaction transaction = transactionRepository.findById(transactionDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionDto.getId()));

        transaction.setTransactionType(transactionDto.getTransactionType());
        transaction.setStatus(transactionDto.getStatus());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionDate(transactionDto.getTransactionDate());

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return convertToDto(updatedTransaction);
    }

    @Override
    public void delete(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction", "id", id);
        }
        transactionRepository.deleteById(id);
    }

    private TransactionDto convertToDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setPropertyId(transaction.getProperty().getId());
        dto.setPropertyTitle(transaction.getProperty().getTitle());
        dto.setClientId(transaction.getClient().getId());
        dto.setClientName(transaction.getClient().getFirstName() + " " + transaction.getClient().getLastName());
        dto.setAgentId(transaction.getAgent().getId());
        dto.setAgentName(transaction.getAgent().getFirstName() + " " + transaction.getAgent().getLastName());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setStatus(transaction.getStatus());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setCreatedAt(transaction.getCreatedAt());
        return dto;
    }
} 