package com.shop.ua.repositories;

import com.shop.ua.models.Category;
import com.shop.ua.models.Goods;
import com.shop.ua.services.GoodsService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Set;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    List<Goods> findByTitle(String title);

    List<Goods> findByApproved(boolean approved);
    List<Goods> findByTitleContaining(String keyword);
    List<Goods> findByDescriptionContaining(String keyword);



}
