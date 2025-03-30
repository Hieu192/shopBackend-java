package com.hieu.shopBackend.utils;

public class ConfixSql {
    public interface Product {
        String SEARCH_PRODUCT_BY_KEYWORD =" ";
    }

    public interface User {
        String GET_USER_BY_KEYWORD = "SELECT u FROM User u WHERE u.active = true AND (:keyword IS NULL OR :keyword = '' " +
                "OR u.fullName LIKE %:keyword% " +
                "OR u.phoneNumber LIKE %:keyword%) " +
                "AND LOWER(u.role.name) = 'user'";
    }
}
