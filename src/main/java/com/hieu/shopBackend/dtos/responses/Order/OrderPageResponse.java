package com.hieu.shopBackend.dtos.responses.Order;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPageResponse {
    List<OrderResponse> orders;
    Integer pageNumber;
    Integer pageSize;
    long totalElements;
    int totalPages;
    boolean isLast;
}
