package com.github.onlinebookstore.service.impl;

import com.github.onlinebookstore.dto.cartitem.CartItemDto;
import com.github.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.github.onlinebookstore.mapper.CartItemMapper;
import com.github.onlinebookstore.model.CartItem;
import com.github.onlinebookstore.repository.CartItemRepository;
import com.github.onlinebookstore.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItemDto save(CreateCartItemRequestDto requestDto) {
        CartItem savedCartItem = cartItemRepository.save(cartItemMapper.toModel(requestDto));
        return cartItemMapper.toDto(savedCartItem);
    }
}
