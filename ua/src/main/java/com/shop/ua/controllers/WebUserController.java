package com.shop.ua.controllers;

import com.shop.ua.component.RepositoryManager;
import com.shop.ua.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class WebUserController {

    @Autowired
    private RepositoryManager repositoryManager;

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
        if (!repositoryManager.getUserService().createUser(user)){
            model.addAttribute("errorMessage", "User with email: " + user.getEmail() + "already exists");
            return "register";
        }
        return "redirect:/login";
    }

}
