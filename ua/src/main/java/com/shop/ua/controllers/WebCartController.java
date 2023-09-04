package com.shop.ua.controllers;

import com.shop.ua.models.Cart;
import com.shop.ua.models.Goods;
import com.shop.ua.models.User;
import com.shop.ua.repositories.CartRepository;
import com.shop.ua.repositories.UserRepository;
import com.shop.ua.repositories.GoodsRepository;
import com.shop.ua.services.GoodsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
public class WebCartController {
    private final UserRepository userRepository;
    private final GoodsRepository goodsRepository;
    private final CartRepository cartRepository;

    public WebCartController(UserRepository userRepository, GoodsRepository goodsRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.goodsRepository = goodsRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping("/cart")
    public String cart(Model model, Authentication authentication) {
        String currentUsername = (authentication != null) ? authentication.getName() : null;

        User user = userRepository.findByEmail(currentUsername);
        List<Cart> cartItems = cartRepository.findByUserAndOrdered(user, false);
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isAuthenticated", true);
        }

        return "cart";
    }

    @PostMapping("/cart/add/{userId}/{goodsId}")
    public String addToCart(@PathVariable Long userId, @PathVariable Long goodsId) {
        User user = userRepository.findById(userId).orElse(null);
        Goods product = goodsRepository.findById(goodsId).orElse(null);

        if (user != null && product != null) {
            Cart cartItem = new Cart();
            cartItem.setUser(user);
            cartItem.setGoods(product);
            cartItem.setOrdered(false);

            cartRepository.save(cartItem);
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/buy/{cartId}")
    public String buyFromCart(@PathVariable Long cartId) {
        Cart cartItem = cartRepository.findById(cartId).orElse(null);

        if (cartItem != null) {
            cartItem.setOrdered(true);

            cartRepository.save(cartItem);
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/buyAll")
    public String updateAllOrderedStatus(@RequestParam boolean newOrderedStatus) {
        List<Cart> cartItems = cartRepository.findAll();

        for (Cart cartItem : cartItems) {
            cartItem.setOrdered(newOrderedStatus);
        }

        cartRepository.saveAll(cartItems);

        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{cartId}")
    public String removeFromCart(@PathVariable Long cartId) {
        Cart cartItem = cartRepository.findById(cartId).orElse(null);

        if (cartItem != null) {
            cartRepository.delete(cartItem);
        }
        return "redirect:/cart";
    }

}
