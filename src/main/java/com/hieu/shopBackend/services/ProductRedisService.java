package com.hieu.shopBackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hieu.shopBackend.dtos.responses.product.ProductResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductRedisService {
    void clear();

    List<ProductResponse> getAllProducts(String keyword,
                                         Long categoryId,
                                         PageRequest pageRequest,
                                         String sortField,
                                         String sortDirection) throws JsonProcessingException;

    void saveAllProducts(List<ProductResponse> productResponses,
                         String keyword,
                         Long categoryId,
                         PageRequest pageRequest,
                         String sortField,
                         String sortDirection) throws JsonProcessingException;
}
