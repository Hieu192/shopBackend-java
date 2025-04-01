package com.hieu.shopBackend.controllers;

import com.hieu.shopBackend.dtos.requests.comment.CommentRequest;
import com.hieu.shopBackend.dtos.responses.ApiResponse;
import com.hieu.shopBackend.dtos.responses.comment.CommentResponse;
import com.hieu.shopBackend.models.Comment;
import com.hieu.shopBackend.services.CommentService;
import com.hieu.shopBackend.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAllComments(
            @RequestParam(value = "user_id", required = false) Long userId,
            @RequestParam("product_id") Long productId
    ) {
        List<CommentResponse> commentResponses;
        if (userId != null) {
            commentResponses = commentService.getCommentByUserAndProduct(userId, productId);
        } else {
            commentResponses = commentService.getCommentByProduct(productId);
        }

        return ResponseEntity.ok(ApiResponse.builder()
                .result(commentResponses)
                .build());
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComments(@PathVariable("id") Long commentId,
                                            @Valid @RequestBody CommentRequest commentRequest,
                                            Authentication authentication) {
        try {
            commentService.updateComment(commentId, commentRequest);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message(MessageKeys.UPDATE_COMMENT_SUCCESS)
                    .build());
        } catch (Exception e) {
            // handle and log exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .error(e.getMessage())
                            .message(MessageKeys.ERROR_MESSAGE)
                            .build());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("")
    public ResponseEntity<?> insertComments(@Valid @RequestBody CommentRequest commentRequest,
                                            Authentication authentication) {
        try {
            Comment newComment = commentService.insertComment(commentRequest);
            return ResponseEntity.ok(ApiResponse.builder().success(true)
                    .message(MessageKeys.COMMENT_INSERT_SUCCESS)
                    .result(newComment)
                    .build());
        } catch (Exception e) {
            // handle and log exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .error(e.getMessage())
                            .message(MessageKeys.ERROR_MESSAGE)
                            .build());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok(ApiResponse.builder().success(true)
                    .message(MessageKeys.DELETE_COMMENT_SUCCESS)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .error(e.getMessage())
                    .message(MessageKeys.DELETE_COMMENT_FAILED)
                    .build());
        }
    }
}
