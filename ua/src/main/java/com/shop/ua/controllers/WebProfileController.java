package com.shop.ua.controllers;

import com.shop.ua.models.Cart;
import com.shop.ua.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import com.shop.ua.repositories.UserRepository;
import com.shop.ua.models.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebProfileController {
    private final UserRepository userRepository;

    public WebProfileController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/userprofile")
    public String cart(Model model, Authentication authentication) {
        String currentUsername = (authentication != null) ? authentication.getName() : null;

        User user = userRepository.findByEmail(currentUsername);
        model.addAttribute("user", user);

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isAuthenticated", true);
        }

        return "userprofile";
    }
}
