package com.hieu.shopBackend.services.impl;

import com.hieu.shopBackend.dtos.requests.order.OrderDetailRequest;
import com.hieu.shopBackend.models.OrderDetail;
import com.hieu.shopBackend.services.OrderDetailService;

import java.util.List;

public class OrderDetailServiceImpl implements OrderDetailService {
    @Override
    public OrderDetail createOrderDetail(OrderDetailRequest orderDetailRequest) throws Exception {
        return null;
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws Exception {
        return null;
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailRequest orderDetailRequest) {
        return null;
    }

    @Override
    public void deleteOrderDetail(Long id) {

    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return List.of();
    }
}
