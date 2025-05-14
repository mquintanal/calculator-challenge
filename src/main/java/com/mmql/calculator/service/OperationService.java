package com.mmql.calculator.service;

import com.mmql.calculator.dto.OperationFilterDTO;
import com.mmql.calculator.exception.ResourceNotFoundException;
import com.mmql.calculator.model.Operation;
import com.mmql.calculator.model.User;
import com.mmql.calculator.repository.OperationRepository;
import com.mmql.calculator.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OperationService {

    private final OperationRepository operationRepository;
    private final UserRepository userRepository;

    @Autowired
    public OperationService(OperationRepository operationRepository, UserRepository userRepository) {
        this.operationRepository = operationRepository;
        this.userRepository = userRepository;
    }

    public Page<Operation> findAll(OperationFilterDTO filter) {
        User user = getCurrentUser();
        LocalDateTime from = filter.getFrom() != null ? filter.getFrom().atStartOfDay() : LocalDateTime.MIN;
        LocalDateTime to = filter.getTo() != null ? filter.getTo().atTime(23, 59, 59) : LocalDateTime.now();

        if (filter.getOperation() != null && !filter.getOperation().isBlank()) {
            return operationRepository.findByUserAndOperationContainingIgnoreCaseAndTimestampBetween(
                    user, filter.getOperation(), from, to, PageRequest.of(filter.getPage(), filter.getSize()));
        }

        return operationRepository.findByUser(user, PageRequest.of(filter.getPage(), filter.getSize()));
    }

    public Operation getById(Long id) {
        return operationRepository.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new IllegalArgumentException("Operation not found"));
    }

    @Transactional
    public void deleteById(Long id) {
        User user = getCurrentUser();
        Operation operation = operationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Operation ID not found " + id));

        operationRepository.delete(operation);
        operationRepository.deleteByIdAndUser(id, getCurrentUser());
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow();
    }
}