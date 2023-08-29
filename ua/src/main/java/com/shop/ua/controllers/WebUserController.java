package com.shop.ua.controllers;

import com.shop.ua.models.User;
import com.shop.ua.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class WebUserController {

    private final UserService userService;

    @GetMapping("/login")
    public  String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public String createUser(User user, Model model){
        if (!userService.createUser(user)){
            model.addAttribute("errorMessage", "User with email: " + user.getEmail() + "already exists");
            return "register";
        }
        return "redirect:/login";
    }

    @GetMapping("/userprofile")
    public String securityUrl(){
        return "userprofile";
    }

}
