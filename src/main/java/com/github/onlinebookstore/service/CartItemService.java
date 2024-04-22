package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.cartitem.CartItemDto;
import com.github.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;

public interface CartItemService {
    CartItemDto save(CreateCartItemRequestDto requestDto);
}
