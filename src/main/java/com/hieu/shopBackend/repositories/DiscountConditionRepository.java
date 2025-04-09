package com.hieu.shopBackend.repositories;

import com.hieu.shopBackend.models.DiscountCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountConditionRepository extends JpaRepository<DiscountCondition, Long> {
    List<DiscountCondition> findByDiscountId(Long discountId);
}
