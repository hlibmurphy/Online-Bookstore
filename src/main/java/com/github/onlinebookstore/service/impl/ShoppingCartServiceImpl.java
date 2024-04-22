package com.github.onlinebookstore.service.impl;

import com.github.onlinebookstore.dto.cartitem.CartItemDto;
import com.github.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.github.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.github.onlinebookstore.mapper.CartItemMapper;
import com.github.onlinebookstore.mapper.ShoppingCartMapper;
import com.github.onlinebookstore.model.CartItem;
import com.github.onlinebookstore.model.ShoppingCart;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.repository.CartItemRepository;
import com.github.onlinebookstore.repository.ShoppingCartRepository;
import com.github.onlinebookstore.service.ShoppingCartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartResponseDto get(User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findAllByUser(user)
                .orElseThrow(
                        () -> new EntityNotFoundException("No shopping cart owned by user "
                                + user + " found"));
        ShoppingCartResponseDto dto = shoppingCartMapper.toDto(shoppingCart);
        Set<CartItemDto> cartItemDtos = shoppingCart.getCartItems().stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet());
        dto.setCartItems(cartItemDtos);
        return dto;
    }

    @Override
    @Transactional
    public void addItem(CreateCartItemRequestDto cartItemRequestDto, User user) {
        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto);
        ShoppingCart shoppingCart = shoppingCartRepository.findAllByUser(user)
                .orElseThrow(
                        () -> new EntityNotFoundException("No shopping cart owned by user "
                                + user + " found"));;
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }
}
