package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.book.UpdateBookRequestDto;
import com.github.onlinebookstore.dto.cartitem.CartItemDto;
import com.github.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.github.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartService {
    ShoppingCartResponseDto getByUserId(Long userId, Pageable pageable);

    ShoppingCartResponseDto addItem(CreateCartItemRequestDto cartItemRequestDto,
                                    Long userId);

    ShoppingCartResponseDto updateItem(UpdateBookRequestDto requestDto,
                                       Long userId,
                                       Long cartItemId);

    CartItemDto deleteItemFromCartById(Long id);
}
