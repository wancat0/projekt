package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.ProductDto;
import com.wanca.aplikacja.entity.Product;

import java.util.Collection;
import java.util.Map;

public interface ProductService {
    Collection<ProductDto> getAllAvailableProductsDetails();

    Collection<ProductDto> getShopProducts(long shopId);

    void removeProductFromShop(long productId, long shopId, int count);

    void addProductToShop(long productId, long shopId, int count);

    void addProduct(ProductDto productDto);

    void removeProduct(long id);

    Product findOne(long id);

    void update(Product product);
}
