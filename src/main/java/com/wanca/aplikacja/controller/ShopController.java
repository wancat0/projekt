package com.wanca.aplikacja.controller;

import com.wanca.aplikacja.dto.CommentDto;
import com.wanca.aplikacja.dto.SimpleShopDto;
import com.wanca.aplikacja.exceptions.ShopNotFoundException;
import com.wanca.aplikacja.security.User;
import com.wanca.aplikacja.service.ProductService;
import com.wanca.aplikacja.service.ShopService;
import com.wanca.aplikacja.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final ProductService productService;

    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @ResponseBody
    @PutMapping("/shops/{shopId}/products/{productId}")
    public void addProduct(@PathVariable long shopId, @PathVariable long productId) {
        productService.addProductToShop(productId, shopId);
        shopService.addComment(shopId, new CommentDto(null, "Dodano: %d".formatted(productId), LocalDateTime.now()));
    }

    @ResponseBody
    @DeleteMapping("/shops/{shopId}/products/{productId}")
    public void removeProduct(@PathVariable long shopId, @PathVariable long productId) {
        productService.removeProductFromShop(productId, shopId);
        shopService.addComment(shopId, new CommentDto(null, "Usunieto: %d".formatted(productId), LocalDateTime.now()));

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
        model.addAttribute("availableProducts", productService.getAllAvailableProductsDetails());
        userService.createNewCalendar(1, shopId);
        return "shop";
    }

    @GetMapping("/shops/{shopId}/exit")
    public String exitShop(@PathVariable long shopId, Model model, @AuthenticationPrincipal User user) {
        userService.endCurrentWorkDate(user.getId());
        return "redirect:/shops";
    }

    @PostMapping("/shops/{shopId}/comments")
    public String shop(@PathVariable long shopId, @ModelAttribute CommentDto commentDto, Model model) {
        commentDto.setDate(LocalDateTime.now());
        shopService.addComment(shopId, commentDto);
        model.addAttribute("shop", shopService.getShopById(shopId)
                .orElseThrow(ShopNotFoundException::new));
        model.addAttribute("availableProducts", productService.getAllAvailableProductsDetails());

        return "redirect:/shops/%d".formatted(shopId);
    }

    @ResponseBody
    @GetMapping("/shops/{shopId}/comments")
    public List<CommentDto> comments(@PathVariable long shopId) {
        return shopService.getComments(shopId);
    }
}
