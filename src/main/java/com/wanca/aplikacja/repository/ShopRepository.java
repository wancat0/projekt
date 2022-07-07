package com.wanca.aplikacja.repository;

import com.wanca.aplikacja.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    Collection<Shop> findByAddress_City(String city);

    @Query(value = "SELECT s.* FROM employee e JOIN shop s on e.shop_id = s.id WHERE e.user_id = ?1",
            nativeQuery = true)
    Collection<Shop> findUserShops(long userId);

}
