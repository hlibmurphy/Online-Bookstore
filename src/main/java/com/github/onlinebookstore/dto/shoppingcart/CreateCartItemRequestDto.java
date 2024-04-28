package com.github.onlinebookstore.dto.shoppingcart;

import lombok.Data;

@Data
public class CreateCartItemRequestDto {
    private Long bookId;
    private int quantity;
}
