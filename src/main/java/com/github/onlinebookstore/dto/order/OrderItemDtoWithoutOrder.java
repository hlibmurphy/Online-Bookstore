package com.github.onlinebookstore.dto.order;

public record OrderItemDtoWithoutOrder(
        Long id,
        Long bookId,
        int quantity
) {

}
