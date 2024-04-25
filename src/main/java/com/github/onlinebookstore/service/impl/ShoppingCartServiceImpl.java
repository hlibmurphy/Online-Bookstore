package com.github.onlinebookstore.service.impl;

import com.github.onlinebookstore.dto.cartitem.CartItemDto;
import com.github.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.github.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.github.onlinebookstore.mapper.CartItemMapper;
import com.github.onlinebookstore.mapper.ShoppingCartMapper;
import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.model.CartItem;
import com.github.onlinebookstore.model.ShoppingCart;
import com.github.onlinebookstore.repository.BookRepository;
import com.github.onlinebookstore.repository.CartItemRepository;
import com.github.onlinebookstore.repository.ShoppingCartRepository;
import com.github.onlinebookstore.service.ShoppingCartService;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartResponseDto get(Long userId, Pageable pageable) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No shopping cart owned by user with id "
                        + userId + " found"));
        Page<CartItem> cartItemsPage = cartItemRepository.findByShoppingCart(
                shoppingCart, pageable);
        List<CartItemDto> cartItemDtos = cartItemsPage.getContent().stream()
                .map(cartItemMapper::toDto)
                .toList();
        Set<CartItemDto> cartItemDtoSet = new HashSet<>(cartItemDtos);
        ShoppingCartResponseDto responseDto = shoppingCartMapper.toDto(shoppingCart);
        responseDto.setCartItems(cartItemDtoSet);

        return responseDto;
    }

    @Override
    public ShoppingCartResponseDto addItem(CreateCartItemRequestDto requestDto,
                                           Long userId) {
        Long bookId = requestDto.getBookId();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(
                        () -> new EntityNotFoundException("No book with id "
                                + bookId + " found")
                );
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "No shopping cart owned by user with id "
                                + userId + " found")
                );

        shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(requestDto.getBookId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + requestDto.getQuantity()),
                        () -> addNewItemToCart(shoppingCart, requestDto, book));

        return shoppingCartMapper.toDto(shoppingCart);
    }

    private void addNewItemToCart(
            ShoppingCart shoppingCart,
            CreateCartItemRequestDto itemDto,
            Book book) {
        CartItem item = cartItemMapper.toModel(itemDto);
        item.setBook(book);
        item.setShoppingCart(shoppingCart);
        CartItem savedItem = cartItemRepository.save(item);
        shoppingCart.addItemToCart(savedItem);
    }

    @Override
    public ShoppingCartResponseDto updateItem(
            CreateCartItemRequestDto requestDto,
            Long userId,
            Long cartId) {
        cartItemRepository.findById(cartId).orElseThrow(
                () -> new EntityNotFoundException("No cart with id " + cartId + " found")
        );

        Long bookId = requestDto.getBookId();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(
                        () -> new EntityNotFoundException("No book with id " + bookId + " found")
                );

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new EntityNotFoundException("No shopping cart owned by user with id "
                                + userId + " found")
                );

        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(requestDto.getQuantity());
        cartItem.setId(cartId);
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void removeItemFromCart(Long cartId) {
        cartItemRepository.deleteById(cartId);
    }
}
