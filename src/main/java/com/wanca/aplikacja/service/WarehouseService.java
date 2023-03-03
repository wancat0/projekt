package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.ProductDto;

import java.util.Collection;

public interface WarehouseService {
    Collection<ProductDto> getProducts();

    void addProduct(ProductDto productDto);

    boolean exists(ProductDto productDto);

    void removeProduct(long id, int count);
}
