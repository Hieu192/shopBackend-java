package com.hieu.shopBackend.services;

import com.hieu.shopBackend.dtos.requests.order.OrderRequest;
import com.hieu.shopBackend.dtos.responses.Order.OrderResponse;
import com.hieu.shopBackend.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest) throws Exception;

    Order getOrderById(Long id);

    Order updateOrder(Long id, OrderRequest orderRequest);

    void deleteOrder(Long id);

    List<Order> findByUserId(Long userId);

    Page<OrderResponse> findByKeyword(String keyword, Pageable pageable);
}
