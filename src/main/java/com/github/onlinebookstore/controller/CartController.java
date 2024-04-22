package com.github.onlinebookstore.controller;

import com.github.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.github.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return shoppingCartService.get(user);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ShoppingCartResponseDto addItemToCart(
            @RequestBody CreateCartItemRequestDto cartItemRequestDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.addItem(cartItemRequestDto, user);

        return shoppingCartService.get(user);
    }
}
