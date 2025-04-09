package com.hieu.shopBackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "discount_conditions")
public class DiscountCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = false)
    @JsonBackReference
    private Discount discount;

    @Column(name = "attribute", nullable = false)
    private String attribute;

    @Column(name = "operator", nullable = false)
    private String operator;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "discount_amount", nullable = false)
    private BigDecimal discountAmount;
}
