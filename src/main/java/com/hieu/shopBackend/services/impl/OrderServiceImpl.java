package com.hieu.shopBackend.services.impl;

import com.hieu.shopBackend.dtos.requests.order.CartItemRequest;
import com.hieu.shopBackend.dtos.requests.order.OrderRequest;
import com.hieu.shopBackend.dtos.responses.Order.OrderResponse;
import com.hieu.shopBackend.exceptions.payload.DataNotFoundException;
import com.hieu.shopBackend.models.*;
import com.hieu.shopBackend.repositories.OrderDetailRepository;
import com.hieu.shopBackend.repositories.OrderRepository;
import com.hieu.shopBackend.repositories.ProductRepository;
import com.hieu.shopBackend.repositories.UserRepository;
import com.hieu.shopBackend.services.OrderService;
import com.hieu.shopBackend.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;


    @Override
    public Order createOrder(OrderRequest orderRequest) throws Exception {
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + orderRequest.getUserId()));


        modelMapper.typeMap(OrderRequest.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));


        Order order = new Order();
        modelMapper.map(orderRequest, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);

        LocalDate shippingDate = orderRequest.getShippingDate() == null
                ? LocalDate.now()
                : orderRequest.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException(MessageKeys.TOKEN_EXPIRATION_TIME);
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        order.setTotalMoney(orderRequest.getTotalMoney());
        orderRepository.save(order);

        // tạo danh sách các đối tượng orderDetails
        List<OrderDetail> orderDetails = new ArrayList<>();
        for(CartItemRequest cartItemRequest : orderRequest.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            // lấy thông tin sản phẩm từ cartItemDTO
            Long productId = cartItemRequest.getProductId();
            int quantity = cartItemRequest.getQuantity();

            // tìm thông tin sản phẩm từ cơ sở dữ liệu
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException(MessageKeys.PRODUCT_NOT_FOUND + productId));

            // Đặt thông tin cho orderDetails
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(product.getPrice());

            // thêm orderDetails vào danh sách
            orderDetails.add(orderDetail);
        }
        // Lưu danh sách OrderDetails vào cơ sở dữ liệu
        orderDetailRepository.saveAll(orderDetails);
        return order;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order updateOrder(Long id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));
        User existsUser = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + orderRequest.getUserId()));

        modelMapper.typeMap(OrderRequest.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // cập nhật các trường của đơn hàng từ orderDTO
        modelMapper.map(orderRequest, order);
        order.setUser(existsUser);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Page<OrderResponse> findByKeyword(String keyword, Pageable pageable) {
        Page<Order> orderPage;
        orderPage = orderRepository.findByKeyword(keyword, pageable);
        return orderPage.map(OrderResponse::fromOrder);
    }
}
