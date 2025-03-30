package com.hieu.shopBackend.repositories;

import com.hieu.shopBackend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
