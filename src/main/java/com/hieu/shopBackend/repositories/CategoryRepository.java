package com.hieu.shopBackend.repositories;

import com.hieu.shopBackend.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<ProductImage, Long> {
}
