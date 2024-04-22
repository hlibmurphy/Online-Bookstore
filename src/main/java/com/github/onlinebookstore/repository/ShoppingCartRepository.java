package com.github.onlinebookstore.repository;

import com.github.onlinebookstore.model.ShoppingCart;
import com.github.onlinebookstore.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findAllByUser(User user);
}
