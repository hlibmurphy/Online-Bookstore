package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import com.github.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartService {
    ShoppingCartResponseDto get(Long userId, Pageable pageable);

    ShoppingCartResponseDto addItem(CreateCartItemRequestDto cartItemRequestDto,
                                    Long userId);

    ShoppingCartResponseDto updateItem(CreateCartItemRequestDto requestDto,
                                       Long userId,
                                       Long cartId);

    void removeItemFromCart(Long id);
}
