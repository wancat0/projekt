package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.ShopDto;
import com.wanca.aplikacja.dto.SimpleShopDto;
import com.wanca.aplikacja.entity.Address;
import com.wanca.aplikacja.entity.Shop;
import com.wanca.aplikacja.repository.ShopRepository;
import com.wanca.aplikacja.util.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final ProductService productService;

    @Override
    @Transactional(readOnly = true)
    public Optional<ShopDto> getShopById(long id) {
        return shopRepository.findById(id).map(s -> {
            var products = productService.getShopProducts(s.getId());
            return DtoConverter.convertShop(s, products);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ShopDto> getShops() {
        return shopRepository.findAll()
                .stream()
                .map(s -> {
                    var products = productService.getShopProducts(s.getId());
                    return DtoConverter.convertShop(s, products);
                })
                .toList();
    }

    @Override
    @Transactional
    public void createShop(SimpleShopDto simpleShopDto) {
        Shop shop = new Shop();
        shop.setName(simpleShopDto.getName());
        Address address = new Address();
        address.setCity(simpleShopDto.getAddress().getCity());
        address.setStreet(simpleShopDto.getAddress().getStreet());
        address.setPostalCode(simpleShopDto.getAddress().getPostalCode());
        shop.setAddress(address);
        shopRepository.save(shop);
    }
}
