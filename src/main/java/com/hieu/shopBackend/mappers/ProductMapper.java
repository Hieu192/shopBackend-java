package com.hieu.shopBackend.mappers;

import com.hieu.shopBackend.dtos.requests.product.ProductCreateRequest;
import com.hieu.shopBackend.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductCreateRequest productCreateRequest);
}
