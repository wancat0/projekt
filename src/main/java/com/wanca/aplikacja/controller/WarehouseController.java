package com.wanca.aplikacja.controller;

import com.wanca.aplikacja.dto.ProductDto;
import com.wanca.aplikacja.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping("/warehouse")
    public String warehouse(Model model) {
        model.addAttribute("products", warehouseService.getProducts());
        return "warehouse";
    }

    @PostMapping("/warehouse/products")
    public String warehouse(@ModelAttribute ProductDto productDto, Model model) {
        warehouseService.addProduct(productDto);
        model.addAttribute("products", warehouseService.getProducts());
        return "warehouse";
    }

    @ResponseBody
    @DeleteMapping("/warehouse/products/{id}")
    public void warehouse(@PathVariable("id") long id) {
        warehouseService.removeProduct(id, 1);
    }
}
