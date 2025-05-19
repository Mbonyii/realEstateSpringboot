package com.example.realestate.repository;

import com.example.realestate.model.Transaction;
import com.example.realestate.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    Page<Transaction> findByClient(User client, Pageable pageable);
    
    Page<Transaction> findByAgent(User agent, Pageable pageable);
    
    Page<Transaction> findByPropertyId(Long propertyId, Pageable pageable);
    
    List<Transaction> findByStatus(Transaction.Status status);
    
    List<Transaction> findByTransactionType(Transaction.TransactionType transactionType);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}