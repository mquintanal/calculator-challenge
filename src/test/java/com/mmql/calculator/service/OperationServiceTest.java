package com.mmql.calculator.service;

import com.mmql.calculator.dto.OperationFilterDTO;
import com.mmql.calculator.model.Operation;
import com.mmql.calculator.model.User;
import com.mmql.calculator.repository.OperationRepository;
import com.mmql.calculator.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OperationServiceTest {

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OperationService operationService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("john");
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        user = new User();
        user.setId(1L);
        user.setUsername("john");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
    }

    @Test
    void testFindAllOperationsWithoutFilter() {
        Page<Operation> page = new PageImpl<>(Collections.emptyList());
        when(operationRepository.findByUser(eq(user), any(PageRequest.class))).thenReturn(page);

        OperationFilterDTO filter = new OperationFilterDTO();
        var result = operationService.findAll(filter);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void testFindOperationByIdSuccess() {
        Operation op = new Operation();
        op.setId(1L);
        when(operationRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(op));

        Operation result = operationService.getById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testFindOperationByIdNotFound() {
        when(operationRepository.findByIdAndUser(999L, user)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> operationService.getById(999L));
    }


    @Test
    void testFilterByOperationAndDates() {
        Page<Operation> page = new PageImpl<>(Collections.emptyList());
        OperationFilterDTO filter = new OperationFilterDTO();
        filter.setOperation("ADD");
        filter.setFrom(LocalDate.now().minusDays(7));
        filter.setTo(LocalDate.now());

        when(operationRepository.findByUserAndOperationContainingIgnoreCaseAndTimestampBetween(
                eq(user), eq("ADD"), any(), any(), any())).thenReturn(page);

        var result = operationService.findAll(filter);
        assertNotNull(result);
    }
}