package com.hieu.shopBackend.services;

import com.hieu.shopBackend.dtos.requests.comment.CommentRequest;
import com.hieu.shopBackend.dtos.responses.comment.CommentResponse;
import com.hieu.shopBackend.exceptions.payload.DataNotFoundException;
import com.hieu.shopBackend.models.Comment;

import java.util.List;

public interface CommentService {
    Comment insertComment(CommentRequest commentRequest);

    void deleteComment(Long id);

    void updateComment(Long id, CommentRequest comment) throws DataNotFoundException;

    List<CommentResponse> getCommentByUserAndProduct(Long userId, Long productId);

    List<CommentResponse> getCommentByProduct(Long productId);
}
