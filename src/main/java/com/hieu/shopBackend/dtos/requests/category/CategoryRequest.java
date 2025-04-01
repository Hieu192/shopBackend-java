package com.hieu.shopBackend.dtos.requests.category;

import com.hieu.shopBackend.utils.MessageKeys;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @NotEmpty(message = MessageKeys.CATEGORIES_NAME_REQUIRED)
    private String name;
}
