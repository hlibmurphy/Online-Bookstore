package com.github.onlinebookstore.repository;

import com.github.onlinebookstore.model.CartItem;
import com.github.onlinebookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Page<CartItem> findByShoppingCart(ShoppingCart shoppingCart, Pageable pageable);

    Optional<CartItem> findByIdAndShoppingCartId(Long cartItemId, Long cartId);
}
