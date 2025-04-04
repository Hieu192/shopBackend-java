package com.hieu.shopBackend.dtos.responses.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductPageResponse {
    List<ProductResponse> products;
    Integer pageNumber;
    Integer pageSize;
    long totalElements;
    int totalPages;
    boolean isLast;
}
