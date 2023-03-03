package com.wanca.aplikacja.controller;

import com.wanca.aplikacja.security.User;
import com.wanca.aplikacja.service.UserService;
import com.wanca.aplikacja.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@RequiredArgsConstructor
@ControllerAdvice(assignableTypes = {ShopController.class, WarehouseController.class})
public class UserPanelAdvice {

    private final UserService userService;
    
    @ModelAttribute
    public void addAttributes(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("name", user.getName());
        model.addAttribute("surname", user.getSurname());
        boolean isWorkStarted = userService.getCurrentStartDate(LocalDate.now(), user.getId()).isPresent();
        model.addAttribute("isWorkStarted", isWorkStarted);
        model.addAttribute("calendar", userService.getUserCalendars(user.getId()));
    }

}
