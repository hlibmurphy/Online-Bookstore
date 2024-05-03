package com.github.onlinebookstore.dto.orderitem;

public record OrderItemDtoWithoutOrder(
        Long id,
        Long bookId,
        int quantity
) {
}
