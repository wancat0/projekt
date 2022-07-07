package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.CommentDto;
import com.wanca.aplikacja.dto.ShopDto;

import java.util.Collection;
import java.util.Optional;

public interface ShopService {
    Optional<ShopDto> getShopById(long id);

    Collection<ShopDto> getUserShops(long userId);

    void addComment(long shopId, CommentDto commentDto);
}
