package com.hieu.shopBackend.services.impl;

import com.hieu.shopBackend.dtos.requests.order.OrderDetailRequest;
import com.hieu.shopBackend.exceptions.payload.DataNotFoundException;
import com.hieu.shopBackend.models.Order;
import com.hieu.shopBackend.models.OrderDetail;
import com.hieu.shopBackend.models.Product;
import com.hieu.shopBackend.repositories.OrderDetailRepository;
import com.hieu.shopBackend.repositories.OrderRepository;
import com.hieu.shopBackend.repositories.ProductRepository;
import com.hieu.shopBackend.services.OrderDetailService;
import com.hieu.shopBackend.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailRequest orderDetailRequest) throws Exception {
        Order order = orderRepository.findById(orderDetailRequest.getOrderId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + orderDetailRequest.getOrderId()));
        Product product = productRepository.findById(orderDetailRequest.getProductId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + orderDetailRequest.getProductId()));

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailRequest.getNumberOfProducts())
                .totalMoney(orderDetailRequest.getTotalMoney())
                .price(orderDetailRequest.getPrice())
                .color(orderDetailRequest.getColor())
                .build();

        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));
    }

    @Override
    @Transactional
    public OrderDetail updateOrderDetail(Long id, OrderDetailRequest orderDetailRequest) {
        OrderDetail existsOrderDetail = getOrderDetail(id);
        Order existsOrder = orderRepository.findById(orderDetailRequest.getOrderId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + orderDetailRequest.getOrderId()));
        Product existsProduct = productRepository.findById(orderDetailRequest.getProductId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + orderDetailRequest.getProductId()));

        existsOrderDetail.setProduct(existsProduct);
        existsOrderDetail.setNumberOfProducts(orderDetailRequest.getNumberOfProducts());
        existsOrderDetail.setTotalMoney(orderDetailRequest.getTotalMoney());
        existsOrderDetail.setPrice(orderDetailRequest.getPrice());
        existsOrderDetail.setColor(orderDetailRequest.getColor());
        existsOrderDetail.setId(id);
        existsOrderDetail.setOrder(existsOrder);
        return orderDetailRepository.save(existsOrderDetail);
    }

    @Override
    @Transactional
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
