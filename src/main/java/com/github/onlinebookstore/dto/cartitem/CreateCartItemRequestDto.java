package com.github.onlinebookstore.dto.cartitem;

import lombok.Data;

@Data
public class CreateCartItemRequestDto {
    private Long bookId;
    private int quantity;
}
