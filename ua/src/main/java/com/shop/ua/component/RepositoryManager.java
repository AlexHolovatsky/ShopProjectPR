package com.shop.ua.component;

import com.shop.ua.services.GoodsService;
import com.shop.ua.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.shop.ua.repositories.*;
import org.springframework.stereotype.Component;

@Component
public class RepositoryManager {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    // Without implements repository
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;

    public UserRepository getUserRepository() {
        return userRepository;
    }


    public GoodsRepository getGoodsRepository() {
        return goodsRepository;
    }

    //impl repository

    public CartRepository getCartRepository() {
        return cartRepository;
    }

    public ImageRepository getImageRepository() {
        return imageRepository;
    }

    public GoodsService getGoodsService() {
        return goodsService;
    }

    public UserService getUserService() {
        return userService;
    }
}
