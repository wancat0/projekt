package com.wanca.aplikacja.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import com.wanca.aplikacja.util.PdfUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
