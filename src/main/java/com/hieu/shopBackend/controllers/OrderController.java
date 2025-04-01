package com.hieu.shopBackend.controllers;

import com.hieu.shopBackend.dtos.requests.order.OrderRequest;
import com.hieu.shopBackend.dtos.responses.ApiResponse;
import com.hieu.shopBackend.dtos.responses.Order.OrderPageResponse;
import com.hieu.shopBackend.dtos.responses.Order.OrderResponse;
import com.hieu.shopBackend.models.Order;
import com.hieu.shopBackend.services.OrderService;
import com.hieu.shopBackend.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        try {

            Order order = orderService.createOrder(orderRequest);
            return ResponseEntity.ok().body(
                    ApiResponse.<Order>builder()
                            .success(true)
                            .message(MessageKeys.CREATE_ORDER_SUCCESS + order.getId())
                            .result(order)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .error(e.getMessage())
                            .message(MessageKeys.CREATE_ORDER_FAILED).build()
            );
        }
    }

    /**
     * Lấy ra danh sách đơn hàng theo user_id
     **/
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId) {
        try {
            // lấy ra danh sách đơn hàng của userId
            List<Order> orders = orderService.findByUserId(userId);
            List<OrderResponse> orderResponses = OrderResponse.fromOrdersList(orders);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .result(orderResponses)
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .error(e.getMessage())
                    .message(MessageKeys.GET_INFORMATION_FAILED).build());
        }
    }

    /*
    lẩy ra chi tiết đơn hàng theo order_id
    */
//    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId) {
        try {
            Order existsOrder = orderService.getOrderById(orderId);
            OrderResponse orderResponse = OrderResponse.fromOrder(existsOrder);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .result(orderResponse).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .error(e.getMessage())
                    .message(MessageKeys.GET_INFORMATION_FAILED).build()
            );
        }
    }

    /*
    Lấy ra tất cả các đơn hàng với quyền ADMIN
    */
    @GetMapping("/get-order-by-keyword")
    public ResponseEntity<OrderPageResponse> getOrderByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit
    ) {
        // tạo Pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(
                page,
                limit,
                Sort.by("id").ascending()
        );
        Page<OrderResponse> orderPage = orderService.findByKeyword(keyword, pageRequest);
        List<OrderResponse> orders = orderPage.getContent();
        return ResponseEntity.ok(OrderPageResponse.builder()
                .orders(orders)
                .pageNumber(page)
                .totalElements(orderPage.getTotalElements())
                .pageSize(orderPage.getSize())
                .isLast(orderPage.isLast())
                .totalPages(orderPage.getTotalPages())
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable long id,
            @Valid @RequestBody OrderRequest orderRequest
    ) {
        try {
            Order order = orderService.updateOrder(id, orderRequest);
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .success(true)
                    .message(MessageKeys.MESSAGE_UPDATE_GET + order.getId())
                    .result(order).build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .error(e.getMessage())
                    .message(MessageKeys.GET_INFORMATION_FAILED).build()
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable long id) {
        // xoá mềm => cập nhật trường active false
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .success(true)
                    .message(MessageKeys.MESSAGE_DELETE_SUCCESS + id)
                    .id(id).build());
        } catch (Exception e) {
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .error(e.getMessage())
                    .message(MessageKeys.MESSAGE_DELETE_FAILED + id).build());
        }
    }
}
