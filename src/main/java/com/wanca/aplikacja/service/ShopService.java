package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.CommentDto;
import com.wanca.aplikacja.dto.ShopDto;
import com.wanca.aplikacja.dto.SimpleShopDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ShopService {
    Optional<ShopDto> getShopById(long id);

    Collection<ShopDto> getShops();

    void addComment(long shopId, CommentDto commentDto);

    void createShop(SimpleShopDto simpleShopDto);

    List<CommentDto> getComments(long shopId);
}
