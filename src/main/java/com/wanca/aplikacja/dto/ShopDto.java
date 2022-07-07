package com.wanca.aplikacja.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class ShopDto {
    private final long id;
    private final String name;
    private final AddressDto address;
    private final Collection<EmployeeDto> employees;
    private final Collection<ProductDto> products;
    private final Collection<CommentDto> comments;
}
