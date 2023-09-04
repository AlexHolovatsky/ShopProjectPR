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
public class WebOrderCotroller {

    private final UserRepository userRepository;
    private final GoodsRepository goodsRepository;
    private final CartRepository cartRepository;

    public WebOrderCotroller(UserRepository userRepository, GoodsRepository goodsRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.goodsRepository = goodsRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping("/order")
    public String cart(Model model, Authentication authentication) {
//        String userEmail = authentication.getName();
        String currentUsername = (authentication != null) ? authentication.getName() : null;
        User user = userRepository.findByEmail(currentUsername);
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isAuthenticated", true);
        }

        List<Cart> cartItems = cartRepository.findByUserAndOrdered(user, true);
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);

        return "order";
    }
    @PostMapping("/order/buy/{cartId}")
    public String buyFromCart(@PathVariable Long cartId) {
        Cart cartItem = cartRepository.findById(cartId).orElse(null);

        if (cartItem != null) {
            cartItem.setOrdered(false);

            cartRepository.save(cartItem);
        }
        return "redirect:/order";
    }
}
