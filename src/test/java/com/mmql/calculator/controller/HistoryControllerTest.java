package com.mmql.calculator.controller;

import com.mmql.calculator.dto.OperationFilterDTO;
import com.mmql.calculator.model.Operation;
import com.mmql.calculator.service.OperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HistoryControllerTest {

    @Mock
    private OperationService operationService;

    @InjectMocks
    private HistoryController historyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getHistoryReturnsPageOfOperations() {
        var filter = new OperationFilterDTO();
        var page = new PageImpl<>(List.of(new Operation()));
        when(operationService.findAll(filter)).thenReturn(page);

        var response = historyController.getHistory(filter);
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void getOperationByIdReturnsOperation() {
        Operation op = new Operation();
        op.setId(1L);
        when(operationService.getById(1L)).thenReturn(op);

        ResponseEntity<Operation> response = historyController.getOperationById(1L);
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void deleteOperationCallsService() {
        historyController.deleteOperation(2L);
        verify(operationService, times(1)).deleteById(2L);
    }
}
