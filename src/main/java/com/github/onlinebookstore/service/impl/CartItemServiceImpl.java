package com.github.onlinebookstore.service.impl;

import com.github.onlinebookstore.dto.cartitem.CartItemDto;
import com.github.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.github.onlinebookstore.mapper.CartItemMapper;
import com.github.onlinebookstore.model.CartItem;
import com.github.onlinebookstore.repository.CartItemRepository;
import com.github.onlinebookstore.service.CartItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public CartItemDto save(CreateCartItemRequestDto requestDto) {
        CartItem savedCartItem = cartItemRepository.save(cartItemMapper.toModel(requestDto));
        return cartItemMapper.toDto(savedCartItem);
    }

    @Override
    public CartItemDto findById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Failed to find cart item with id: " + id));
        return cartItemMapper.toDto(cartItem);
    }
}
