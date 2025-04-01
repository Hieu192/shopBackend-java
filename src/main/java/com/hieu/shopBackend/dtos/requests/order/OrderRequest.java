package com.hieu.shopBackend.dtos.requests.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hieu.shopBackend.utils.MessageKeys;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @Min(value = 1, message = MessageKeys.USER_ID_REQUIRED)
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName;

    private String email;

    @NotBlank(message = MessageKeys.PHONE_NUMBER_REQUIRED)
    @Size(min = 10, message = MessageKeys.PHONE_NUMBER_SIZE_REQUIRED)
    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;
    private String note;

    @JsonProperty("total_money")
    @Min(value = 0, message = MessageKeys.TOTAL_MONEY_REQUIRED)
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAdress;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("cart_items")
    private List<CartItemRequest> cartItems;
}
