package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.github.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;

public interface CartItemService {
    CartItemDto save(CreateCartItemRequestDto requestDto);

    CartItemDto findById(Long id);
}
