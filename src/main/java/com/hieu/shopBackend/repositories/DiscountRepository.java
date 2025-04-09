package com.hieu.shopBackend.repositories;

import com.hieu.shopBackend.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByCode(String discountCode);
}
