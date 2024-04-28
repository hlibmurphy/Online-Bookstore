package com.github.onlinebookstore.controller;

import com.github.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import com.github.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ShoppingCartResponseDto getShoppingCart(
            Authentication authentication,
            @PageableDefault(size = 1, page = 0) Pageable pageable) {

        User user = (User) authentication.getPrincipal();
        return shoppingCartService.get(user.getId(), pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartResponseDto addItemToCart(
            @RequestBody CreateCartItemRequestDto cartItemRequestDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addItem(cartItemRequestDto, user.getId());
    }

    @PutMapping("/cart-items/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartResponseDto changeItemInCart(
            @RequestBody CreateCartItemRequestDto cartItemRequestDto,
            Authentication authentication,
            @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateItem(cartItemRequestDto, user.getId(), id);
    }

    @DeleteMapping("/cart-items/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void removeItemFromCart(@PathVariable Long id) {
        shoppingCartService.removeItemFromCart(id);
    }
}
