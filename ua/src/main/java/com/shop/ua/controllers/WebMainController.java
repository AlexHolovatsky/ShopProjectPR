package com.shop.ua.controllers;

import com.shop.ua.models.User;
import com.shop.ua.repositories.UserRepository;
import com.shop.ua.services.GoodsService;
import com.shop.ua.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class WebMainController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private final GoodsService goodsService;

    @GetMapping("/")
    public String main(Model model, Authentication authentication){
        String currentUsername = (authentication != null) ? authentication.getName() : null;
        User user = userRepository.findByEmail(currentUsername);
        model.addAttribute("user", user);
        model.addAttribute("title", "Main page");

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isAuthenticated", true);
        }

        return "main";
    }

}
