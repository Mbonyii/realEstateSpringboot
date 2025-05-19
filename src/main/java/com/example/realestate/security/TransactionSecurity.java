package com.example.realestate.security;

import com.example.realestate.model.Transaction;
import com.example.realestate.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("transactionSecurity")
@RequiredArgsConstructor
public class TransactionSecurity {

    private final TransactionRepository transactionRepository;

    public boolean canAccessTransaction(Long transactionId, UserPrincipal userPrincipal) {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        
        return transaction.map(t -> 
                    t.getClient().getId().equals(userPrincipal.getId()) || 
                    t.getAgent().getId().equals(userPrincipal.getId())
                ).orElse(false);
    }

    public boolean canManageTransaction(Long transactionId, UserPrincipal userPrincipal) {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        
        if (transaction.isEmpty()) {
            return false;
        }
        
        // Clients can only modify their own transactions that are still pending
        if (userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT"))) {
            Transaction t = transaction.get();
            return t.getClient().getId().equals(userPrincipal.getId()) && 
                   t.getStatus() == Transaction.Status.PENDING;
        }
        
        // Agents can modify transactions for properties they manage
        if (userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_AGENT"))) {
            Transaction t = transaction.get();
            return t.getAgent().getId().equals(userPrincipal.getId());
        }
        
        // Admins can modify any transaction
        return userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}