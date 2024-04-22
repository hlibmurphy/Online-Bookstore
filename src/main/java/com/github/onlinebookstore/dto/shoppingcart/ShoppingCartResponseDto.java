package com.github.onlinebookstore.dto.shoppingcart;

import com.github.onlinebookstore.dto.cartitem.CartItemDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems;
}
