package com.shop.ua.repositories;

import com.shop.ua.models.Cart;
import com.shop.ua.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserAndOrdered(User user, boolean ordered);
}
