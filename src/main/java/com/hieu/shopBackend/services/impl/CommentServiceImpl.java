package com.hieu.shopBackend.services.impl;

import com.hieu.shopBackend.dtos.requests.comment.CommentRequest;
import com.hieu.shopBackend.dtos.responses.comment.CommentResponse;
import com.hieu.shopBackend.exceptions.payload.DataNotFoundException;
import com.hieu.shopBackend.models.Comment;
import com.hieu.shopBackend.models.Product;
import com.hieu.shopBackend.models.User;
import com.hieu.shopBackend.repositories.CommentRepository;
import com.hieu.shopBackend.repositories.ProductRepository;
import com.hieu.shopBackend.repositories.UserRepository;
import com.hieu.shopBackend.services.CommentService;
import com.hieu.shopBackend.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public Comment insertComment(CommentRequest commentRequest) {
        User user = userRepository.findById(commentRequest.getUserId()).orElse(null);
        Product product = productRepository.findById(commentRequest.getProductId()).orElse(null);

        if (user == null) {
            throw new DataNotFoundException(MessageKeys.USER_NOT_FOUND);
        }
        if (product == null) {
            throw new DataNotFoundException(MessageKeys.PRODUCT_NOT_FOUND);
        }

        Comment newComment = Comment.builder()
                .user(userRepository.findById(commentRequest.getUserId()).get())
                .product(productRepository.findById(commentRequest.getProductId()).get())
                .content(commentRequest.getContent())
                .build();

        return commentRepository.save(newComment);
    }

    @Transactional
    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void updateComment(Long id, CommentRequest comment) throws DataNotFoundException {
        Comment existsComment = commentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.COMMENT_NOT_FOUND + id));

        existsComment.setContent(comment.getContent());
        commentRepository.save(existsComment);
    }

    @Override
    public List<CommentResponse> getCommentByUserAndProduct(Long userId, Long productId) {
        List<Comment> comments = commentRepository.findByUserIdAndProductId(userId, productId);
        return comments.stream().map(CommentResponse::fromComment).toList();
    }

    @Override
    public List<CommentResponse> getCommentByProduct(Long productId) {
        List<Comment> comments = commentRepository.findByProductId(productId);
        return comments.stream().map(CommentResponse::fromComment).toList();
    }
}
