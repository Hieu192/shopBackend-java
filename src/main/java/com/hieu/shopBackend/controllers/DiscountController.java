package com.hieu.shopBackend.controllers;

import com.hieu.shopBackend.dtos.responses.ApiResponse;
import com.hieu.shopBackend.dtos.responses.Discount.DiscountCalculationResponse;
import com.hieu.shopBackend.services.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/discount")
@RequiredArgsConstructor
public class DiscountController {
    private DiscountService discountService;

    @GetMapping("/calculate")
    public ResponseEntity<ApiResponse<DiscountCalculationResponse>> calculateCouponValue(
            @RequestParam("discount_code") String discountCode,
            @RequestParam("total_amount") double totalAmount) {
        try {
            double finalAmount = discountService.calculateDiscountValue(discountCode, totalAmount);
            return ResponseEntity.ok(ApiResponse.<DiscountCalculationResponse>builder()
                    .success(true)
                    .result(DiscountCalculationResponse.builder()
                            .result(finalAmount)
                            .errorMessage(null)
                            .build())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<DiscountCalculationResponse>builder()
                            .error(e.getMessage())
                            .result(DiscountCalculationResponse.builder()
                                    .result(totalAmount)
                                    .errorMessage(e.getMessage())
                                    .build())
                            .build()
            );
        }
    }
}
