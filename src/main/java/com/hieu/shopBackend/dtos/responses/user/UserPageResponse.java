package com.hieu.shopBackend.dtos.responses.user;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPageResponse {
    List<UserResponse> users;
    Integer pageNumber;
    Integer pageSize;
    long totalElements;
    int totalPages;
    boolean isLast;
}
