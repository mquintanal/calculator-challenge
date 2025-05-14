package com.mmql.calculator.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CalculationRequest {

    @NotNull
    private String operation;

    @NotNull
    @DecimalMin(value = "-1000000")
    @DecimalMax(value = "1000000")
    private BigDecimal operandA;

    @DecimalMin(value = "-1000000")
    @DecimalMax(value = "1000000")
    private BigDecimal operandB;
}
