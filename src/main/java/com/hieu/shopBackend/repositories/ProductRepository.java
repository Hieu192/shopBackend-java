package com.hieu.shopBackend.repositories;

import com.hieu.shopBackend.models.Product;
import com.hieu.shopBackend.utils.ConfixSql;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(ConfixSql.Product.GET_DETAIL_PRODUCT)
    Optional<Product> getDetailProducts(@Param("productId") Long productId);

    @Query(ConfixSql.Product.SEARCH_PRODUCT_BY_KEYWORD)
    Page<Product> searchProducts(@Param("keyword") String keyword,
                                 @Param("categoryId") Long categoryId,
                                 Pageable pageable);

    @Query(ConfixSql.Product.FIND_PRODUCT_BY_IDS)
    List<Product> findProductByIds(@Param("productIds") List<Long> productIds);
}
