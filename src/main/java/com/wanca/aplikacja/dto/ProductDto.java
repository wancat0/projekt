package com.wanca.aplikacja.dto;

import lombok.Data;

@Data
public class ProductDto {
    private final Long id;
    private final String name;
    private final int count;
    private final String serialNumber;
}
