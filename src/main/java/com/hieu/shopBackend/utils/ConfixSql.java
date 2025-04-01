package com.hieu.shopBackend.utils;

public class ConfixSql {
    public interface Product {
        String SEARCH_PRODUCT_BY_KEYWORD ="SELECT p FROM Product p WHERE " +
                "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) " +
                "AND (:keyword IS NULL OR :keyword = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))";

        String GET_DETAIL_PRODUCT = "SELECT p FROM Product p LEFT JOIN FETCH p.productImages where p.id = :productId";

        // tìm kiếm sản phẩm theo danh sách id
        String FIND_PRODUCT_BY_IDS = "SELECT p FROM Product p where p.id IN :productIds";
    }

    public interface User {
        String GET_USER_BY_KEYWORD = "SELECT u FROM User u " +
                "WHERE u.active = true " +
                "AND (:keyword IS NULL OR :keyword = '' " +
                "OR u.fullName LIKE CONCAT('%', :keyword, '%') " +
                "OR u.phoneNumber LIKE CONCAT('%', :keyword, '%')) " +
                "AND LOWER(u.role.name) = 'user'";
    }
    public interface Order {
        // lẩy ra tất cả các order
        String GET_ALL_ORDER = "SELECT o FROM Order o WHERE " +
                "(:keyword IS NULL OR :keyword = '' OR o.fullName LIKE %:keyword% OR o.address LIKE %:keyword% " +
                "OR o.note LIKE %:keyword%)";
    }
}
