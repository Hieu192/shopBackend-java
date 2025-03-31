package com.hieu.shopBackend.services.impl;

import com.hieu.shopBackend.dtos.requests.product.ProductCreateRequest;
import com.hieu.shopBackend.dtos.requests.product.ProductImageRequest;
import com.hieu.shopBackend.dtos.responses.product.ProductResponse;
import com.hieu.shopBackend.exceptions.payload.DataNotFoundException;
import com.hieu.shopBackend.exceptions.payload.InvalidParamException;
import com.hieu.shopBackend.mappers.ProductMapper;
import com.hieu.shopBackend.models.Category;
import com.hieu.shopBackend.models.Product;
import com.hieu.shopBackend.models.ProductImage;
import com.hieu.shopBackend.repositories.CategoryRepository;
import com.hieu.shopBackend.repositories.ProductImageRepository;
import com.hieu.shopBackend.repositories.ProductRepository;
import com.hieu.shopBackend.services.ProductService;
import com.hieu.shopBackend.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;


    @Override
    public Product createProduct(ProductCreateRequest productCreateRequest) {
        // 1. check có tồn tại category không
        Category existsCategory = categoryRepository
                .findById(productCreateRequest.getCategoryId())
                .orElseThrow(() ->
                    new DataNotFoundException(MessageKeys.CATEGORY_NOT_FOUND + productCreateRequest.getCategoryId())
                );
        Product newProduct = productMapper.toProduct(productCreateRequest);
        newProduct.setCategory(existsCategory);
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.PRODUCT_NOT_FOUND + productId));
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest, String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Pageable pageable =PageRequest.of(
            pageRequest.getPageNumber(), pageRequest.getPageSize(), direction, sortField
        );
        Page<Product> productPage = productRepository.searchProducts(keyword, categoryId, pageable);
        return productPage.map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(Long productId, ProductCreateRequest productCreateRequest) {
        Product existsProduct = getProductById(productId);

        Category existsCategory = categoryRepository
                .findById(productCreateRequest.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException(
                        MessageKeys.CATEGORY_NOT_FOUND + productCreateRequest.getCategoryId())
                );
        existsProduct.setName(productCreateRequest.getName());
        existsProduct.setCategory(existsCategory);
        existsProduct.setPrice(productCreateRequest.getPrice());
        existsProduct.setDescription(productCreateRequest.getDescription());
        existsProduct.setThumbnail(productCreateRequest.getThumbnail());
        return productRepository.save(existsProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsProduct(String name) {
        return false;
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageRequest productImageRequest) {
        Product existsProduct = getProductById(productId);
        ProductImage productImage = ProductImage.builder()
                .product(existsProduct)
                .imageUrl(productImageRequest.getImageUrl())
                .build();
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException("Number of images lest " +
                    ProductImage.MAXIMUM_IMAGES_PER_PRODUCT + " reached");
        }
        return productImageRepository.save(productImage);
    }

    @Override
    public Product getDetailProducts(long productId) throws Exception {
        Optional<Product> optionalProduct = productRepository.getDetailProducts(productId);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        throw new DataNotFoundException(MessageKeys.PRODUCT_NOT_FOUND + productId);
    }

    @Override
    public List<Product> findProductsByIds(List<Long> productIds) {
        return productRepository.findProductByIds(productIds);
    }
}
