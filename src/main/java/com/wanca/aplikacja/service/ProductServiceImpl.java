package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.ProductDto;
import com.wanca.aplikacja.entity.Product;
import com.wanca.aplikacja.entity.ShopStore;
import com.wanca.aplikacja.exceptions.ProductNotAvailableException;
import com.wanca.aplikacja.repository.ProductRepository;
import com.wanca.aplikacja.repository.ShopRepository;
import com.wanca.aplikacja.repository.ShopStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ShopRepository shopRepository;
    private final ShopStoreRepository shopStoreRepository;
    private final ProductRepository productRepository;

    @Override
    public Collection<ProductDto> getAllAvailableProductsDetails() {
        return productRepository.findAll()
                .stream()
                .map(p -> new ProductDto(p.getId(), p.getName(), p.getCount()))
                .toList();
    }

    @Override
    public Collection<ProductDto> getShopProducts(long shopId) {
        return shopStoreRepository.findByShop_Id(shopId)
                .stream()
                .map(s -> new ProductDto(s.getProduct().getId(), s.getProduct().getName(), s.getCount()))
                .toList();
    }

    @Override
    public void removeProductFromShop(long productId, long shopId) {
        var shopStoreProduct = shopStoreRepository.findByShop_IdAndProduct_id(shopId, productId)
                .orElseThrow(RuntimeException::new);

        if (!shopStoreProduct.isAvailable())
            throw new ProductNotAvailableException();

        shopStoreProduct.removeProduct();
        if (shopStoreProduct.isAvailable())
            shopStoreRepository.save(shopStoreProduct);
        else shopStoreRepository.delete(shopStoreProduct);
    }

    @Override
    public void addProductToShop(long productId, long shopId) {
        var product = productRepository.findById(productId)
                .orElseThrow(RuntimeException::new);

        if (!product.isAvailable())
            throw new ProductNotAvailableException();

        var shopStoreProduct = shopStoreRepository.findByShop_IdAndProduct_id(shopId, productId)
                .orElseGet(() -> {
                    var shop = shopRepository.findById(shopId)
                            .orElseThrow(RuntimeException::new);
                    return new ShopStore(shop, product);
                });


        shopStoreProduct.addProduct();
        shopStoreRepository.save(shopStoreProduct);
    }

    @Override
    public void addProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setCount(productDto.getCount());
        productRepository.save(product);
    }

    @Override
    public void removeProduct(long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product findOne(long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotAvailableException::new);
    }

    @Override
    public void update(Product product) {
        productRepository.save(product);
    }
}
