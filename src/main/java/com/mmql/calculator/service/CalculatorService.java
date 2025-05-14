package com.mmql.calculator.service;

import com.mmql.calculator.dto.CalculationRequest;
import com.mmql.calculator.dto.CalculationResponse;
import com.mmql.calculator.model.Operation;
import com.mmql.calculator.model.User;
import com.mmql.calculator.repository.OperationRepository;
import com.mmql.calculator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CalculatorService {

    private final OperationRepository operationRepository;
    private final UserRepository userRepository;

    @Autowired
    public CalculatorService(OperationRepository operationRepository, UserRepository userRepository) {
        this.operationRepository = operationRepository;
        this.userRepository = userRepository;
    }

    public CalculationResponse calculate(CalculationRequest request) {
        BigDecimal result;
        BigDecimal a = request.getOperandA();
        BigDecimal b = request.getOperandB();
        String operation = request.getOperation().toUpperCase();

        switch (operation) {
            case "ADDITION":
                result = a.add(b);
                break;
            case "SUBTRACTION":
                result = a.subtract(b);
                break;
            case "DI":
                result = a.multiply(b);
                break;
            case "DIVISION":
                if (b.compareTo(BigDecimal.ZERO) == 0) {
                    throw new IllegalArgumentException("Division by zero is not allowed");
                }
                result = a.divide(b, 10, BigDecimal.ROUND_HALF_UP);
                break;
            case "SQRT":
                if (a.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("Square root of negative number is not allowed");
                }
                result = new BigDecimal(Math.sqrt(a.doubleValue()));
                b = null;
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();

        Operation op = new Operation();
        op.setOperation(operation);
        op.setOperandA(a);
        op.setOperandB(b);
        op.setResult(result);
        op.setTimestamp(LocalDateTime.now());
        op.setUser(user);

        operationRepository.save(op);

        return new CalculationResponse(operation, a, b, result, op.getTimestamp());
    }
}