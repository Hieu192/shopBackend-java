package com.hieu.shopBackend.dtos.responses.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hieu.shopBackend.dtos.responses.BaseResponse;
import com.hieu.shopBackend.dtos.responses.comment.CommentResponse;
import com.hieu.shopBackend.models.Product;
import com.hieu.shopBackend.models.ProductImage;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends BaseResponse {
    private String name;
    private Float price;
    private String thumbnail;
    private String description;
    @JsonProperty("product_images")
    private List<ProductImage> productImages = new ArrayList<>();
    @JsonProperty("comments")
    private List<CommentResponse> comments = new ArrayList<>();

    @JsonProperty("category_id")
    private Long categoryId;

    public static ProductResponse fromProduct(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .productImages(product.getProductImages())
                .comments(product.getComments()
                        .stream()
                        .map(CommentResponse::fromComment)
                        .toList())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
