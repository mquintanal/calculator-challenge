package com.mmql.calculator.repository;

import com.mmql.calculator.model.Operation;
import com.mmql.calculator.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    Page<Operation> findByUser(User user, Pageable pageable);

    Page<Operation> findByUserAndOperationContainingIgnoreCaseAndTimestampBetween(
            User user, String operation, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Optional<Operation> findByIdAndUser(Long id, User user);

    void deleteByIdAndUser(Long id, User user);
}