package com.wanca.aplikacja.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SimpleShopDto {
    private String name;
    private AddressDto address;
}
