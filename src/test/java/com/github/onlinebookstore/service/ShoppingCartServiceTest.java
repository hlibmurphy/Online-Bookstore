package com.github.onlinebookstore.service;

import com.github.onlinebookstore.mapper.ShoppingCartMapper;
import com.github.onlinebookstore.repository.ShoppingCartRepository;
import com.github.onlinebookstore.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    public void getByUserId_withValidUserId_shouldReturnShoppingCart() {

    }

    @Test
    public void getByUserId_withInvalidUserId_shouldThrowException() {

    }

    @Test
    public void addItem_withValidUserIdAndRequestDto_shouldSaveItemToCart() {

    }

    @Test
    public void addItem_withInvalidUserId_shouldThrowException() {

    }

    @Test
    public void updateItem_withValidCartItemIdAndUserIdAndRequestDto_shouldUpdateItem() {

    }

    @Test
    public void updateItem_withInvalidCartItemIdAndUserId_shouldThrowException() {

    }

    @Test
    public void deleteItemFromCart_withValidCartItemId_shouldDeleteAndReturnItem() {

    }

    @Test
    public void deleteItemFromCart_withInvalidCartItemId_shouldThrowException() {

    }
}
