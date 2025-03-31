package com.hieu.shopBackend.dtos.requests.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hieu.shopBackend.utils.MessageKeys;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductImageRequest {
    @JsonProperty("product_id")
    @Min(value = 1, message = MessageKeys.PRODUCT_ID_REQUIRED)
    private Long productId;

    @Size(min = 5, max = 300, message = MessageKeys.IMAGE_SIZE_REQUIRED)
    @JsonProperty("image_url")
    private String imageUrl;
}
