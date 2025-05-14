package com.mmql.calculator.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OperationFilterDTO {
    private String operation;
    private LocalDate from;
    private LocalDate to;
    private int page = 0;
    private int size = 10;
}
