package com.shop.ua.repositories;

import com.shop.ua.models.Store;
import com.shop.ua.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByOwner(User owner);
}
