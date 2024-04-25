package com.github.onlinebookstore.controller;

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
        return shoppingCartService.get(user.getId(), pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Add item to shopping cart",
            description = "Add an item to the user's shopping cart; "
                    + "Sum quantity if such item already exists")
    public ShoppingCartResponseDto addItemToCart(
            @RequestBody CreateCartItemRequestDto cartItemRequestDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addItem(cartItemRequestDto, user.getId());
    }

    @PutMapping("/cart-items/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Change cart item by its id",
            description = "Change an item in the user's shopping cart by its id")
    public ShoppingCartResponseDto changeItemInCart(
            @RequestBody CreateCartItemRequestDto cartItemRequestDto,
            Authentication authentication,
            @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateItem(cartItemRequestDto, user.getId(), id);
    }

    @DeleteMapping("/cart-items/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Remove item from cart",
            description = "Perform soft delete of an item "
                    + "from the user's shopping cart")
    public void removeItemFromCart(@PathVariable Long id) {
        shoppingCartService.removeItemFromCart(id);
    }
}
