package com.wanca.aplikacja.service;

import com.itextpdf.text.DocumentException;
import com.wanca.aplikacja.dto.CommentDto;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface CommentService {
    void addComment(long shopId, CommentDto commentDto);

    List<CommentDto> getComments(long shopId, boolean archivedComments);

    ByteArrayResource generatePdfFromComments(List<CommentDto> comments) throws IOException, DocumentException, URISyntaxException;

    void archiveComments(long shopId);
}
