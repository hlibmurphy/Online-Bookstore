package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.github.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.github.onlinebookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto get(User user);

    void addItem(CreateCartItemRequestDto cartItemRequestDto, User user);
}
