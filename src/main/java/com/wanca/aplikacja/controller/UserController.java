package com.wanca.aplikacja.controller;

import com.wanca.aplikacja.dto.UserDto;
import com.wanca.aplikacja.exceptions.UserAlreadyExistsException;
import com.wanca.aplikacja.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/registration")
    public String showRegistrationForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

    @PostMapping("/user/registration")
    public String registerUserAccount(
            @ModelAttribute("user") @Valid UserDto userDto,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "registration";
        }
        try {
            userService.registerUser(userDto);
        } catch (UserAlreadyExistsException userAlreadyExistsException) {
            ObjectError error = new ObjectError("globalError", "An account for that username/email already exists.");
            result.addError(error);
            return "registration";
        }
        return "redirect:/";
    }
}
