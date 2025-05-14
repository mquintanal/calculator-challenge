package com.mmql.calculator.controller;

import com.mmql.calculator.dto.OperationFilterDTO;
import com.mmql.calculator.model.Operation;
import com.mmql.calculator.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final OperationService operationService;

    @Autowired
    public HistoryController(OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping
    public ResponseEntity<Page<Operation>> getHistory(@ModelAttribute OperationFilterDTO filter) {
        return ResponseEntity.ok(operationService.findAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Operation> getOperationById(@PathVariable Long id) {
        return ResponseEntity.ok(operationService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOperation(@PathVariable Long id) {
        operationService.deleteById(id);
        String message = "Operation ID (" + id + ") has been removed";
        return ResponseEntity.ok(Map.of("message", message));
    }
}
