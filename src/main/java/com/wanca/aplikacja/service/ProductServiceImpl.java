package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.CommentDto;
import com.wanca.aplikacja.dto.ProductDto;
import com.wanca.aplikacja.entity.Product;
import com.wanca.aplikacja.entity.ShopStore;
import com.wanca.aplikacja.exceptions.ProductNotAvailableException;
import com.wanca.aplikacja.repository.ProductRepository;
import com.wanca.aplikacja.repository.ShopRepository;
import com.wanca.aplikacja.repository.ShopStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ShopRepository shopRepository;
    private final ShopStoreRepository shopStoreRepository;
    private final ProductRepository productRepository;
    private final CommentService commentService;

    @Override
    @Transactional(readOnly = true)
    public Collection<ProductDto> getAllAvailableProductsDetails() {
        return productRepository.findAll()
                .stream()
                .map(p -> new ProductDto(p.getId(), p.getName(), p.getCount(), p.getSerialNumber()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ProductDto> getShopProducts(long shopId) {
        return shopStoreRepository.findByShop_Id(shopId)
                .stream()
                .map(s -> new ProductDto(s.getProduct().getId(), s.getProduct().getName(), s.getCount(), s.getProduct().getSerialNumber()))
                .toList();
    }

    @Override
    @Transactional
    public void removeProductFromShop(long productId, long shopId, int count) {
        var shopStoreProduct = shopStoreRepository.findByShop_IdAndProduct_id(shopId, productId)
                .orElseThrow(RuntimeException::new);

        if (!shopStoreProduct.isAvailable(count))
            throw new ProductNotAvailableException();

        shopStoreProduct.removeProduct(count);
        if (shopStoreProduct.isAvailable(count))
            shopStoreRepository.save(shopStoreProduct);
        else shopStoreRepository.delete(shopStoreProduct);
        commentService.addComment(shopId, new CommentDto(null, "Dodano: %d %s".formatted(count, shopStoreProduct.getProduct().getName()), LocalDateTime.now()));

    }

    @Override
    @Transactional
    public void addProductToShop(long productId, long shopId, int count) {
        var product = productRepository.findById(productId)
                .orElseThrow(RuntimeException::new);

        if (!product.isAvailable(count))
            throw new ProductNotAvailableException();

        var shopStoreProduct = shopStoreRepository.findByShop_IdAndProduct_id(shopId, productId)
                .orElseGet(() -> {
                    var shop = shopRepository.findById(shopId)
                            .orElseThrow(RuntimeException::new);
                    return new ShopStore(shop, product);
                });


        shopStoreProduct.addProduct(count);
        shopStoreRepository.save(shopStoreProduct);
        commentService.addComment(shopId, new CommentDto(null, "Dodano: %d %s".formatted(count, product.getName()), LocalDateTime.now()));

    }

    @Override
    @Transactional
    public void addProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setCount(productDto.getCount());
        product.setSerialNumber(productDto.getSerialNumber());
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void removeProduct(long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findOne(long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotAvailableException::new);
    }

    @Override
    @Transactional
    public void update(Product product) {
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(ProductDto productDto) {
        return productRepository.existsProductByNameAndSerialNumber(productDto.getName(), productDto.getSerialNumber());
    }
}
