package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.ShopDto;
import com.wanca.aplikacja.dto.SimpleShopDto;

import java.util.Collection;
import java.util.Optional;

public interface ShopService {
    Optional<ShopDto> getShopById(long id);

    Collection<ShopDto> getShops();

    void createShop(SimpleShopDto simpleShopDto);
}
