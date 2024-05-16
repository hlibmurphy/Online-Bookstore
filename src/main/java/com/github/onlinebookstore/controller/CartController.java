package com.github.onlinebookstore.controller;

import com.github.onlinebookstore.dto.book.UpdateBookRequestDto;
import com.github.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.github.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Get a shopping cart",
            description = "Get a shopping cart with all its items")
    public ShoppingCartResponseDto getShoppingCart(
            Authentication authentication,
            @PageableDefault(size = 1, page = 0) Pageable pageable) {

        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getByUserId(user.getId(), pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Add an item to a shopping cart",
            description = "Add an item to a shopping cart")
    public ShoppingCartResponseDto addItemToCart(
            @RequestBody CreateCartItemRequestDto cartItemRequestDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addItem(cartItemRequestDto, user.getId());
    }

    @PutMapping("/cart-items/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Change shopping cart item",
            description = "Change an item from a user's cart")
    public ShoppingCartResponseDto changeItemInCart(
            @RequestBody UpdateBookRequestDto updateBookRequestDto,
            Authentication authentication,
            @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateItem(updateBookRequestDto, user.getId(), id);
    }

    @DeleteMapping("/cart-items/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Remove an item from a shopping cart",
            description = "Remove an item from user's shopping cart")
    public void removeItemFromCart(@PathVariable Long itemId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.deleteItemFromCartById(itemId, user);
    }
}
