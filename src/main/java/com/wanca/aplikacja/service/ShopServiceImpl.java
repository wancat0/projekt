package com.wanca.aplikacja.service;

import com.wanca.aplikacja.dto.CommentDto;
import com.wanca.aplikacja.dto.ShopDto;
import com.wanca.aplikacja.dto.SimpleShopDto;
import com.wanca.aplikacja.entity.Address;
import com.wanca.aplikacja.entity.Comment;
import com.wanca.aplikacja.entity.Shop;
import com.wanca.aplikacja.exceptions.ShopNotFoundException;
import com.wanca.aplikacja.repository.CommentRepository;
import com.wanca.aplikacja.repository.ShopRepository;
import com.wanca.aplikacja.util.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
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

    @Override
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

    @Override
    public List<CommentDto> getComments(long shopId) {
        return commentRepository.findShopComments(shopId)
                .stream()
                .map(DtoConverter::convertComment)
                .toList();
    }
}
