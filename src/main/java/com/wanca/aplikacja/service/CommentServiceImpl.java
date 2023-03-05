package com.wanca.aplikacja.service;

import com.itextpdf.text.DocumentException;
import com.wanca.aplikacja.dto.CommentDto;
import com.wanca.aplikacja.entity.Comment;
import com.wanca.aplikacja.exceptions.ShopNotFoundException;
import com.wanca.aplikacja.repository.CommentRepository;
import com.wanca.aplikacja.repository.ShopRepository;
import com.wanca.aplikacja.util.DtoConverter;
import com.wanca.aplikacja.util.PdfUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final ShopRepository shopRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
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
    @Transactional(readOnly = true)
    public List<CommentDto> getComments(long shopId, boolean archivedComments) {
        return commentRepository.findShopComments(shopId)
                .stream()
                .filter(c -> c.isArchived() == archivedComments)
                .map(DtoConverter::convertComment)
                .toList();
    }

    @Override
    public ByteArrayResource generatePdfFromComments(List<CommentDto> comments) throws IOException, DocumentException {
        return PdfUtils.generatePdfFromComment(comments);
    }

    @Override
    @Transactional
    public void archiveComments(long shopId) {
        commentRepository.findShopComments(shopId)
                .stream()
                .filter(c -> c.getDate().toLocalDate().isEqual(LocalDate.now()))
                .filter(c -> !c.isArchived())
                .forEach(c -> c.setArchived(true));
    }
}
