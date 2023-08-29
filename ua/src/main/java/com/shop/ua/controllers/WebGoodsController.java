package com.shop.ua.controllers;

import com.shop.ua.models.Cart;
import com.shop.ua.models.Goods;
import com.shop.ua.models.User;
import com.shop.ua.repositories.CartRepository;
import com.shop.ua.repositories.GoodsRepository;
import com.shop.ua.repositories.UserRepository;
import com.shop.ua.services.GoodsService;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebGoodsController {
    private final GoodsService goodsService;
    private final GoodsRepository goodsRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;


//    @GetMapping("/shop")
//    public String goods(@RequestParam(name = "title", required = false) String title, Model model){
//        model.addAttribute("goods", goodsService.listGoods(title));
//        return "shoppage";
//    }

    @GetMapping("/shop")
    public String approvedGoods(Model model) {
        List<Goods> approvedGoods = goodsService.listApprovedGoods();
        model.addAttribute("approvedGoods", approvedGoods);
        return "shoppage";
    }

    @GetMapping("/shop/goods/{id}")
    public String goodsInfo(@PathVariable Long id, Model model){
        Goods goods = goodsService.getGoodsById(id);
        model.addAttribute("goods", goods);
        model.addAttribute("images", goods.getImages());
        return "goods-info";
    }


    @PostMapping("/shop/goods/create")
    public String createGoods(@RequestParam("file1") MultipartFile file1,
                              @RequestParam("file2") MultipartFile file2,
                              @RequestParam("file3") MultipartFile file3, Goods goods) throws IOException {
        goodsService.saveGoods(goods, file1, file2, file3);
        return "redirect:/shop";
    }

    @PostMapping("/shop/goods/delete/{id}")
    public String deleteGoods(@PathVariable Long id){
        goodsService.deleteGoods(id);
        return "redirect:/shop";
    }

    @GetMapping("/shop/addgood")
    public String addGood(){
        return "addgood";
    }

    @GetMapping("/shop/product/{id}")
    public String getProductDetails(@PathVariable Long id, Model model, Authentication authentication) {
        Goods product = goodsRepository.findById(id).orElse(null);
        User user = null;

        model.addAttribute("title", "Details");

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isAuthenticated", true);
            String userEmail = authentication.getName();
            user = userRepository.findByEmail(userEmail);
        }

        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("user", user);
            return "productdetails";
        } else {
            return "redirect:/";
        }
    }

//    @GetMapping("/shop/productdetails/cart")
//    public String addCart() {
//        return "cart";
//    }

}
