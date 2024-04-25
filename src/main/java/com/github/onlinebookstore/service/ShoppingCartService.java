package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.github.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartService {
    ShoppingCartResponseDto get(Long userId, Pageable pageable);

    @Transactional
    ShoppingCartResponseDto addItem(CreateCartItemRequestDto cartItemRequestDto, Long userId);

    @Transactional
    ShoppingCartResponseDto updateItem(
            CreateCartItemRequestDto requestDto,
            Long userId,
            Long cartId);

    void removeItemFromCart(Long id);
}
