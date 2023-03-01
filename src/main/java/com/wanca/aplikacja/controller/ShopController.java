package com.wanca.aplikacja.controller;

import com.itextpdf.text.DocumentException;
import com.wanca.aplikacja.dto.CommentDto;
import com.wanca.aplikacja.dto.SimpleShopDto;
import com.wanca.aplikacja.exceptions.ShopNotFoundException;
import com.wanca.aplikacja.security.User;
import com.wanca.aplikacja.service.CommentService;
import com.wanca.aplikacja.service.ProductService;
import com.wanca.aplikacja.service.ShopService;
import com.wanca.aplikacja.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final ProductService productService;
    private final CommentService commentService;
    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @ResponseBody
    @PutMapping("/shops/{shopId}/products/{productId}")
    public void addProduct(@PathVariable long shopId, @PathVariable long productId, @RequestParam int count) {
        productService.addProductToShop(productId, shopId, count);
    }

    @ResponseBody
    @DeleteMapping("/shops/{shopId}/products/{productId}")
    public void removeProduct(@PathVariable long shopId, @PathVariable long productId, @RequestParam int count) {
        productService.removeProductFromShop(productId, shopId, count);
    }

    @GetMapping("/shops")
    public String shops(Model model) {
        model.addAttribute("shops", shopService.getShops());
        model.addAttribute("newShop", new SimpleShopDto());
        return "shops";
    }

    @PostMapping("/shops")
    public String shop(@ModelAttribute SimpleShopDto simpleShopDto) {
        shopService.createShop(simpleShopDto);
        return "redirect:/shops";
    }


    @GetMapping("/shops/{shopId}")
    public String shop(@PathVariable long shopId, Model model) {
        model.addAttribute("shop", shopService.getShopById(shopId)
                .orElseThrow(ShopNotFoundException::new));
        model.addAttribute("archiveComments", commentService.getComments(shopId, true));
        model.addAttribute("availableProducts", productService.getAllAvailableProductsDetails());
        userService.createNewCalendar(1, shopId);
        return "shop";
    }

    @GetMapping("/shops/{shopId}/exit")
    public String exitShop(@PathVariable long shopId, Model model, @AuthenticationPrincipal User user) {
        userService.endCurrentWorkDate(user.getId());
        commentService.archiveComments(shopId);
        return "redirect:/shops";
    }

    @PostMapping("/shops/{shopId}/comments")
    public String shop(@PathVariable long shopId, @ModelAttribute CommentDto commentDto, Model model) {
        commentDto.setDate(LocalDateTime.now());
        commentService.addComment(shopId, commentDto);
        model.addAttribute("shop", shopService.getShopById(shopId)
                .orElseThrow(ShopNotFoundException::new));
        model.addAttribute("availableProducts", productService.getAllAvailableProductsDetails());

        return "redirect:/shops/%d".formatted(shopId);
    }

    @ResponseBody
    @GetMapping("/shops/{shopId}/comments")
    public List<CommentDto> comments(@PathVariable long shopId, @RequestParam boolean archived) {
        return commentService.getComments(shopId, archived);
    }

    @ResponseBody
    @GetMapping(value = "/shops/{shopId}/comments/pdf")
    public ResponseEntity<Resource> generatePdf(@PathVariable long shopId) throws DocumentException, IOException, URISyntaxException {
        var comments = commentService.getComments(shopId, true);
        var pdfFile = commentService.generatePdfFromComments(comments);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=komentarze-%s.pdf".formatted(LocalDate.now()));
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(pdfFile.contentLength())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfFile);
    }
}
