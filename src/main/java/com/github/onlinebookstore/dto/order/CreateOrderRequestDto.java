package com.github.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotEmpty;

public record CreateOrderRequestDto(@NotEmpty String shippingAddress) {

}
