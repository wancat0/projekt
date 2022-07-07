package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final ProductService productService;

    @Override
    public Collection<ProductDto> getProducts() {
        return productService.getAllAvailableProductsDetails();
    }

    @Override
    public void addProduct(ProductDto productDto) {
        productService.addProduct(productDto);
    }

    @Override
    public void removeProduct(long id, int count) {
        var product = productService.findOne(id);
        if (product.getCount() > 0) {
            product.setCount(product.getCount() - count);
            productService.update(product);
        }

        if (product.getCount() < 1)
            productService.removeProduct(id);
    }
}
