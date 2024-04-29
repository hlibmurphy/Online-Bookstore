package com.github.onlinebookstore.service.impl;

import com.github.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.github.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
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
import jakarta.transaction.Transactional;
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
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);
        Page<CartItem> cartItemsPage = cartItemRepository
                .findByShoppingCart(shoppingCart, pageable);
        List<CartItemDto> cartItemDtos = cartItemsPage.getContent().stream()
                .map(cartItemMapper::toDto)
                .toList();
        Set<CartItemDto> cartItemDtoSet = new HashSet<>(cartItemDtos);
        ShoppingCartResponseDto responseDto = shoppingCartMapper.toDto(shoppingCart);
        responseDto.setCartItems(cartItemDtoSet);

        return responseDto;
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto addItem(CreateCartItemRequestDto requestDto, Long userId) {
        Book book = findBookByIdFromDto(requestDto);

        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);

        shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(requestDto.getBookId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> updateExistingItem(item, requestDto.getQuantity()),
                        () -> addNewItemToCart(shoppingCart, requestDto, book));

        return shoppingCartMapper.toDto(shoppingCart);
    }

    private void updateExistingItem(CartItem item, int quantity) {
        item.setQuantity(item.getQuantity() + quantity);
    }

    private void addNewItemToCart(ShoppingCart shoppingCart,
                                  CreateCartItemRequestDto itemDto,
                                  Book book) {
        CartItem item = cartItemMapper.toModel(itemDto);
        item.setBook(book);
        item.setShoppingCart(shoppingCart);
        CartItem savedItem = cartItemRepository.save(item);
        shoppingCart.addItemToCart(savedItem);
    }

    @Override
    public ShoppingCartResponseDto updateItem(CreateCartItemRequestDto requestDto,
                                              Long userId,
                                              Long cartId) {
        checkIfCartItemExists(cartId);

        Book book = findBookByIdFromDto(requestDto);
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);

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

    private void checkIfCartItemExists(Long cartId) {
        cartItemRepository.findById(cartId).orElseThrow(
                () -> new EntityNotFoundException("No cart with id " + cartId + " found")
        );
    }

    private ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new EntityNotFoundException("No shopping cart owned by user with id "
                                + userId + " found"));
    }

    private Book findBookByIdFromDto(CreateCartItemRequestDto requestDto) {
        Long bookId = requestDto.getBookId();
        return bookRepository.findById(bookId)
                .orElseThrow(
                        () -> new EntityNotFoundException("No book with id " + bookId + " found")
                );
    }
}
