package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.CommentDto;
import com.wanca.aplikacja.dto.ShopDto;
import com.wanca.aplikacja.entity.Comment;
import com.wanca.aplikacja.exceptions.ShopNotFoundException;
import com.wanca.aplikacja.repository.CommentRepository;
import com.wanca.aplikacja.repository.ShopRepository;
import com.wanca.aplikacja.util.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final ProductService productService;

    private final CommentRepository commentRepository;

    public Collection<ShopDto> getShopsByCity(String city) {
        return shopRepository.findByAddress_City(city).stream().map(s -> {
            var products = productService.getShopProducts(s.getId());
            return DtoConverter.convertShop(s, products);
        }).toList();
    }

    @Override
    public Optional<ShopDto> getShopById(long id) {
        return shopRepository.findById(id).map(s -> {
            var products = productService.getShopProducts(s.getId());
            return DtoConverter.convertShop(s, products);
        });
    }

    @Override
    public Collection<ShopDto> getUserShops(long userId) {
        return shopRepository.findUserShops(userId)
                .stream()
                .map(s -> {
                    var products = productService.getShopProducts(s.getId());
                    return DtoConverter.convertShop(s, products);
                })
                .toList();
    }

    @Override
    public void addComment(long shopId, CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setDate(commentDto.getDate());
        comment.setText(commentDto.getText());
        var shop = shopRepository.findById(shopId)
                .orElseThrow(ShopNotFoundException::new);
        commentRepository.save(comment);
        shop.getComments().add(comment);
        shopRepository.save(shop);
    }
}
