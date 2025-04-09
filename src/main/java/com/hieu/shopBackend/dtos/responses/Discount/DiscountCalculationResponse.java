package com.hieu.shopBackend.dtos.responses.Discount;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountCalculationResponse {
    @JsonProperty("result")
    private Double result;

    // error code
    @JsonProperty("error_message")
    private String errorMessage;
}
