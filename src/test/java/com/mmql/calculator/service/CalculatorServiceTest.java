package com.mmql.calculator.service;

import com.mmql.calculator.dto.CalculationRequest;
import com.mmql.calculator.dto.CalculationResponse;
import com.mmql.calculator.model.Operation;
import com.mmql.calculator.model.User;
import com.mmql.calculator.repository.OperationRepository;
import com.mmql.calculator.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CalculatorServiceTest {

    @Mock private OperationRepository operationRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user1");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("user1");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(mockUser));
    }

    @Test
    void testAdditionSuccess() {
        CalculationRequest request = new CalculationRequest();
        request.setOperation("ADDITION");
        request.setOperandA(BigDecimal.valueOf(2));
        request.setOperandB(BigDecimal.valueOf(3));

        CalculationResponse response = calculatorService.calculate(request);

        assertEquals(BigDecimal.valueOf(5), response.getResult());
        verify(operationRepository, times(1)).save(any(Operation.class));
    }

    @Test
    void testDivisionByZeroThrowsException() {
        CalculationRequest request = new CalculationRequest();
        request.setOperation("DIVISION");
        request.setOperandA(BigDecimal.valueOf(10));
        request.setOperandB(BigDecimal.ZERO);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculatorService.calculate(request);
        });

        assertEquals("Division by zero is not allowed", exception.getMessage());
    }
}