package com.shop.ua.repositories;

import com.shop.ua.models.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
    List<Goods> findByTitle(String title);

    List<Goods> findByApproved(boolean approved);
}
