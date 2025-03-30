package com.hieu.shopBackend.repositories;

import com.hieu.shopBackend.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
