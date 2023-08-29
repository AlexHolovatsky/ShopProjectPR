package com.shop.ua.controllers;

import com.shop.ua.models.Goods;
import com.shop.ua.services.GoodsService;
import com.shop.ua.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class WebAdminController {
    private final UserService userService;
    private final GoodsService goodsService;

    @GetMapping("/admin")
    public String admin(Model model){
        List<Goods> unapprovedGoods = goodsService.listUnapprovedGoods();
        model.addAttribute("unapprovedGoods", unapprovedGoods);
        model.addAttribute("users", userService.list());
        return "admin";
    }


    @PostMapping("/admin/user/ban/{id}")
    public String bannedUser(@PathVariable("id") Long id){
        userService.banUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/user/unban/{id}")
    public String unbannedUser(@PathVariable("id") Long id){
        userService.unbanUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/user/op/{id}")
    public String oppUser(@PathVariable("id") Long id){
        userService.assignAdminRole(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/user/unop/{id}")
    public String unoppUser(@PathVariable("id") Long id){
        userService.removeAdminRole(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/goods/approve/{id}")
    public String approveGoods(@PathVariable("id") Long id) {
        goodsService.approveGoods(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/goods/reject/{id}")
    public String rejectGoods(@PathVariable("id") Long id) {
        goodsService.rejectProduct(id);
        return "redirect:/admin";
    }

}
