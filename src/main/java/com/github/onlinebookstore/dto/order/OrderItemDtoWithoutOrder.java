package com.github.onlinebookstore.dto.order;

import lombok.Data;

@Data
public class OrderItemDtoWithoutOrder {
    private Long id;
    private Long bookId;
    private int quantity;
}
