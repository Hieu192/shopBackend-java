package com.hieu.shopBackend.services;

import com.hieu.shopBackend.dtos.requests.order.OrderDetailRequest;
import com.hieu.shopBackend.models.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    OrderDetail createOrderDetail(OrderDetailRequest orderDetailRequest) throws Exception;

    OrderDetail getOrderDetail(Long id) throws Exception;

    OrderDetail updateOrderDetail(Long id, OrderDetailRequest orderDetailRequest);

    void deleteOrderDetail(Long id);

    List<OrderDetail> findByOrderId(Long orderId);
}
