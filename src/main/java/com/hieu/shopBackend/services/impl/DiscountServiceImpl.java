package com.hieu.shopBackend.services.impl;

import com.hieu.shopBackend.models.Discount;
import com.hieu.shopBackend.models.DiscountCondition;
import com.hieu.shopBackend.repositories.DiscountConditionRepository;
import com.hieu.shopBackend.repositories.DiscountRepository;
import com.hieu.shopBackend.services.DiscountService;
import com.hieu.shopBackend.utils.Const;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final DiscountConditionRepository discountConditionRepository;

    @Override
    public double calculateDiscountValue(String discountCode, double totalAmount) {
        Discount discount = discountRepository.findByCode(discountCode)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));
        if (!discount.isActive()) {
            throw new IllegalArgumentException("Coupon is not active");
        }
        double discountValue = calculateDiscount(discount, totalAmount);
        return totalAmount - discountValue;
    }

    public double calculateDiscount(Discount discount, double totalAmount) {
        List<DiscountCondition> conditions = discountConditionRepository.findByDiscountId(discount.getId());
        double discountValue = 0.0;
        double updateTotalAmount = totalAmount;
        for (DiscountCondition condition : conditions) {
            // EAV (Entity - Attribute - Value)
            String attribute = condition.getAttribute();
            String operator = condition.getOperator();
            String value = condition.getValue();
            double percenDiscount = Double.parseDouble(
                    String.valueOf(condition.getDiscountAmount()));

            if (attribute.equals(Const.MINIMUN_AMOUNT)) {
                if (operator.equals(">") && updateTotalAmount > Double.parseDouble(value)) {
                    discountValue += updateTotalAmount * percenDiscount / 100;
                }
            } else if (attribute.equals(Const.APPLICATION_DATE)) {
                LocalDate applicableDate = LocalDate.parse(value);
                LocalDate currentDate = LocalDate.now();
                if (operator.equalsIgnoreCase(Const.BETWEEN) && currentDate.isBefore(applicableDate)) {
                    discountValue += updateTotalAmount * percenDiscount / 100;
                }
            }

            // thêm nhiều điều kiện khác vào
            updateTotalAmount -= discountValue;
        }

        return discountValue;
    }
}
