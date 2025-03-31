package com.hieu.shopBackend.repositories;

import com.hieu.shopBackend.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
