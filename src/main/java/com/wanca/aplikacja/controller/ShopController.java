package com.wanca.aplikacja.controller;

import com.wanca.aplikacja.dto.CommentDto;
import com.wanca.aplikacja.exceptions.ShopNotFoundException;
import com.wanca.aplikacja.security.User;
import com.wanca.aplikacja.service.ProductService;
import com.wanca.aplikacja.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final ProductService productService;

    @ResponseBody
    @PutMapping("/shops/{shopId}/products/{productId}")
    public void addProduct(@PathVariable long shopId, @PathVariable long productId) {
        productService.addProductToShop(productId, shopId);
    }

    @ResponseBody
    @DeleteMapping("/shops/{shopId}/products/{productId}")
    public void removeProduct(@PathVariable long shopId, @PathVariable long productId) {
        productService.removeProductFromShop(productId, shopId);
    }

    @GetMapping("/shops")
    public String shops(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("shops", shopService.getUserShops(user.getId()));
        return "shops";
    }


    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/shops/{shopId}")
    public String shop(@PathVariable long shopId, Model model) {
        model.addAttribute("shop", shopService.getShopById(shopId)
                .orElseThrow(ShopNotFoundException::new));
        model.addAttribute("availableProducts", productService.getAllAvailableProductsDetails());

        return "shop";
    }

    @PostMapping("/shops/{shopId}/comments")
    public String shop(@PathVariable long shopId, @ModelAttribute CommentDto commentDto, Model model) {
        commentDto.setDate(LocalDateTime.now());
        shopService.addComment(shopId, commentDto);
        model.addAttribute("shop", shopService.getShopById(shopId)
                .orElseThrow(ShopNotFoundException::new));
        model.addAttribute("availableProducts", productService.getAllAvailableProductsDetails());

        return "shop";
    }
}
