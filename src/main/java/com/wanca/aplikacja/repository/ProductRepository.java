package com.wanca.aplikacja.repository;

import com.wanca.aplikacja.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsProductByNameAndSerialNumber(String name, String serialNumber);
}
