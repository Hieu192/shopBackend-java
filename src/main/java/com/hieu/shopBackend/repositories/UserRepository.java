package com.hieu.shopBackend.repositories;

import com.hieu.shopBackend.models.User;
import com.hieu.shopBackend.utils.ConfixSql;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query(ConfixSql.User.GET_USER_BY_KEYWORD)
    Page<User> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
