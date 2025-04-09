package com.hieu.shopBackend.services;

public interface DiscountService {
    double calculateDiscountValue(String discountCode, double totalAmount);
}
