package com.shop.ua.controllers;

import com.shop.ua.component.RepositoryManager;
import com.shop.ua.models.Goods;
import com.shop.ua.models.Image;
import com.shop.ua.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.FilenameUtils;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.io.File;

@Controller
@RequiredArgsConstructor
public class WebGoodsController {
    @Autowired
    private RepositoryManager repositoryManager;

    @GetMapping("/shop")
    public String approvedGoods(Model model) {
        List<Goods> approvedGoods = repositoryManager.getGoodsService().listApprovedGoods();
        model.addAttribute("approvedGoods", approvedGoods);
        return "shoppage";
    }

    @GetMapping("/shop/goods/{id}")
    public String goodsInfo(@PathVariable Long id, Model model){
        Goods goods = repositoryManager.getGoodsService().getGoodsById(id);
        model.addAttribute("goods", goods);
        model.addAttribute("images", goods.getImages());
        return "productdetails";
    }


    @PostMapping("/shop/goods/create")
    public String createGoods(@RequestParam("file1") MultipartFile file1,
                              @RequestParam("file2") MultipartFile file2,
                              @RequestParam("file3") MultipartFile file3, Goods goods) throws IOException {
        // Змініть цей код для збереження шляхів у базі даних
        repositoryManager.getGoodsService().saveGoods(goods, file1, file2, file3);
        return "redirect:/shop";
    }

    @PostMapping("/shop/goods/delete/{id}")
    public String deleteGoods(@PathVariable Long id){
        repositoryManager.getGoodsService().deleteGoods(id);
        return "redirect:/shop";
    }

    @GetMapping("/shop/addgood")
    public String addGood(){
        return "addgood";
    }

    @GetMapping("/shop/product/{id}")
    public String getProductDetails(@PathVariable Long id, Model model, Authentication authentication) {
        Goods product = repositoryManager.getGoodsRepository().findById(id).orElse(null);
        User user = null;

        model.addAttribute("title", "Details");

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isAuthenticated", true);
            String userEmail = authentication.getName();
            user = repositoryManager.getUserRepository().findByEmail(userEmail);
        }

        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("user", user);
            return "productdetails";
        } else {
            return "redirect:/";
        }
    }


}
