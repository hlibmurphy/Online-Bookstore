package com.github.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotEmpty
    private String shippingAddress;
}
