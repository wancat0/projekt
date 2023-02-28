package com.wanca.aplikacja.service;

import com.itextpdf.text.DocumentException;
import com.wanca.aplikacja.dto.CommentDto;
import com.wanca.aplikacja.dto.ShopDto;
import com.wanca.aplikacja.dto.SimpleShopDto;
import com.wanca.aplikacja.entity.Comment;
import org.springframework.core.io.ByteArrayResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ShopService {
    Optional<ShopDto> getShopById(long id);

    Collection<ShopDto> getShops();

    void addComment(long shopId, CommentDto commentDto);

    void createShop(SimpleShopDto simpleShopDto);

    List<CommentDto> getComments(long shopId);

    Comment getComment(long commentId);

    ByteArrayResource generatePdfFromComment(Comment comment) throws IOException, DocumentException, URISyntaxException;
}
