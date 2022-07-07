package com.wanca.aplikacja.repository;

import com.wanca.aplikacja.entity.ShopStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ShopStoreRepository extends JpaRepository<ShopStore, Long> {

    Collection<ShopStore> findByShop_Id(long shopId);

    Optional<ShopStore> findByShop_IdAndProduct_id(long shopId, long productId);
}
