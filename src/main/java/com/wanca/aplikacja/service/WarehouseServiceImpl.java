package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final ProductService productService;

    @Override
    @Transactional(readOnly = true)
    public Collection<ProductDto> getProducts() {
        return productService.getAllAvailableProductsDetails();
    }

    @Override
    @Transactional
    public void addProduct(ProductDto productDto) {
        productService.addProduct(productDto);
    }

    @Override
    @Transactional
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
