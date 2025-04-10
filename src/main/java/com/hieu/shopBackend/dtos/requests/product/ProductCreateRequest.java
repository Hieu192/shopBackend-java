package com.hieu.shopBackend.dtos.requests.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hieu.shopBackend.utils.MessageKeys;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductCreateRequest {
    @NotBlank(message = MessageKeys.PRODUCT_TITLE_REQUIRED)
    @Size(min = 3, max = 200, message = MessageKeys.PRODUCT_TITLE_SIZE_REQUIRED)
    private String name;

    @Min(value = 0, message = MessageKeys.PRODUCT_PRICE_MIN_REQUIRED)
    @Max(value = 100000000, message = MessageKeys.PRODUCT_PRICE_MAX_REQUIRED)
    private Float price;

    private String thumbnail;
    private String description;

    @JsonProperty("category_id")
    private Long categoryId;
}
