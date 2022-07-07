package com.wanca.aplikacja.dto;

import lombok.Data;

@Data
public class AddressDto {
    private final String street;
    private final String city;
    private final String postalCode;
}
