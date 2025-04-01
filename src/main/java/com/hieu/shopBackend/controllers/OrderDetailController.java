package com.hieu.shopBackend.controllers;

import com.hieu.shopBackend.dtos.requests.order.OrderDetailRequest;
import com.hieu.shopBackend.dtos.responses.ApiResponse;
import com.hieu.shopBackend.dtos.responses.Order.OrderDetailResponse;
import com.hieu.shopBackend.models.OrderDetail;
import com.hieu.shopBackend.services.OrderDetailService;
import com.hieu.shopBackend.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/order_details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;


    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailRequest orderDetailRequest
    ) {
        try {

            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailRequest);
            return ResponseEntity.ok(ApiResponse.builder().success(true)
                    .message(MessageKeys.CREATE_ORDER_DETAILS_SUCCESS)
                    .result(OrderDetailResponse.fromOrderDetail(newOrderDetail)).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .message(MessageKeys.CREATE_ORDER_DETAILS_SUCCESS)
                            .error(e.getMessage()).build()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) throws Exception {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok(ApiResponse.builder().success(true).result(OrderDetailResponse.fromOrderDetail(orderDetail)).build());
        // return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
    }


    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId) {
        List<OrderDetailResponse> orderDetailResponses = orderDetailService.findByOrderId(orderId)
                .stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .toList();
        return ResponseEntity.ok(ApiResponse.builder().success(true).result(orderDetailResponses).build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @RequestBody OrderDetailRequest orderDetailRequest,
            @PathVariable("id") Long id
    ) {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailRequest);
            return ResponseEntity.ok(ApiResponse.builder().success(true).result(OrderDetailResponse.fromOrderDetail(orderDetail)).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().error(e.getMessage()).build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id) {
        try {
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok().body(ApiResponse.builder().success(true)
                    .message(MessageKeys.MESSAGE_DELETE_SUCCESS).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .error(e.getMessage())
                    .message(MessageKeys.MESSAGE_DELETE_SUCCESS).build());
        }
    }
}
