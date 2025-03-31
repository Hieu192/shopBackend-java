package com.hieu.shopBackend.services;

import com.hieu.shopBackend.dtos.requests.product.ProductCreateRequest;
import com.hieu.shopBackend.dtos.requests.product.ProductImageRequest;
import com.hieu.shopBackend.dtos.responses.product.ProductResponse;
import com.hieu.shopBackend.models.Product;
import com.hieu.shopBackend.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductCreateRequest productCreateRequest);

    Product getProductById(Long id);

    Page<ProductResponse> getAllProducts(String keyword,
                                         Long categoryId,
                                         PageRequest pageRequest,
                                         String sortField,
                                         String sortDirection);

    Product updateProduct(Long id, ProductCreateRequest productCreateRequest);

    void deleteProduct(Long id);

    boolean existsProduct(String name);

    ProductImage createProductImage(Long productId,
                                    ProductImageRequest productImageRequest);

    Product getDetailProducts(long productId) throws Exception;

    List<Product> findProductsByIds(List<Long> productIds);
}
