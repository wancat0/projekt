package com.wanca.aplikacja.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wanca.aplikacja.dto.CommentDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PdfUtils {

    public static ByteArrayResource generatePdfFromComment(List<CommentDto> comments) throws IOException, DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        PdfPTable table = new PdfPTable(2);
        addTableHeader(table);
        comments.forEach(c -> addRows(table, c.getDate(), c.getText()));
        document.add(table);

        document.close();
        out.close();
        return new ByteArrayResource(out.toByteArray());
    }

    private static void addTableHeader(PdfPTable table) {
        Stream.of("Data", "Komentarz")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private static void addRows(PdfPTable table, LocalDateTime date, String text) {
        table.addCell(date.format(DateTimeFormatter.ofPattern("hh:mm:ss dd-MM-yyyy")));
        table.addCell(text);
    }
}
