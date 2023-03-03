package com.wanca.aplikacja.controller;

import com.wanca.aplikacja.dto.ProductDto;
import com.wanca.aplikacja.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping("/warehouse")
    public String warehouse(Model model, HttpServletRequest request) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null && inputFlashMap.containsKey("error")) {
            String error = (String) inputFlashMap.get("error");
            model.addAttribute("error", error);
        }
        model.addAttribute("products", warehouseService.getProducts());
        return "warehouse";
    }

    @PostMapping("/warehouse/products")
    public RedirectView warehouse(@ModelAttribute ProductDto productDto, Model model, RedirectAttributes redirectAttributes) {
        if (warehouseService.exists(productDto)) {
            redirectAttributes.addFlashAttribute("error", "Produkt %s (%s) istnieje!".formatted(productDto.getName(), productDto.getSerialNumber()));
        } else {
            warehouseService.addProduct(productDto);
        }
        return new RedirectView("/warehouse");
    }

    @ResponseBody
    @DeleteMapping("/warehouse/products/{id}")
    public void warehouse(@PathVariable("id") long id, @RequestParam("count") int count) {
        warehouseService.removeProduct(id, count);
    }
}
