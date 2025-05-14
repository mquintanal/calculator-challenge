package com.mmql.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CalculationResponse {
    private String operation;
    private BigDecimal operandA;
    private BigDecimal operandB;
    private BigDecimal result;
    private LocalDateTime timestamp;
}
