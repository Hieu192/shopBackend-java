package com.hieu.shopBackend.dtos.responses.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hieu.shopBackend.dtos.responses.user.UserResponse;
import com.hieu.shopBackend.models.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    @JsonProperty("content")
    private String content;

    // user's infomation
    @JsonProperty("user")
    private UserResponse userResponse;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static CommentResponse fromComment(Comment comment) {
        return CommentResponse.builder()
                .content(comment.getContent())
                .userResponse(UserResponse.fromUser(comment.getUser()))
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}