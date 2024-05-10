package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.cartitem.CartItemDto;
import com.github.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.github.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.github.onlinebookstore.mapper.CartItemMapper;
import com.github.onlinebookstore.mapper.ShoppingCartMapper;
import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.model.CartItem;
import com.github.onlinebookstore.model.ShoppingCart;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.repository.BookRepository;
import com.github.onlinebookstore.repository.CartItemRepository;
import com.github.onlinebookstore.repository.ShoppingCartRepository;
import com.github.onlinebookstore.service.impl.ShoppingCartServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    public void getByUserId_withValidUserId_shouldReturnShoppingCart() {
        ShoppingCart shoppingCart = createTestShoppingCart(1L);

        when(shoppingCartRepository.findByUserId(shoppingCart.getUser().getId()))
                .thenReturn(Optional.of(shoppingCart));
        Page<CartItem> cartItems = new PageImpl<>(shoppingCart.getCartItems().stream().toList());
        when(cartItemRepository.findByShoppingCart(
                any(ShoppingCart.class),
                any(Pageable.class)))
                .thenReturn(cartItems);
        when(cartItemMapper.toDto(any(CartItem.class)))
                .thenAnswer(invocation -> mapItemToDto(invocation.getArgument(0)));
        ShoppingCartResponseDto expected = mapToDto(shoppingCart);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);

        ShoppingCartResponseDto actual = shoppingCartService.getByUserId(
                shoppingCart.getUser().getId(),
                Pageable.unpaged());

        Assertions.assertEquals(expected, actual,
                "The retrieved shopping cart DTO should match the expected DTO");
    }

    @Test
    public void getByUserId_withInvalidUserId_shouldThrowException() {
        Long userId = 1L;
        when(shoppingCartRepository.findByUserId(anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.getByUserId(userId, Pageable.unpaged()));
    }

    @Test
    public void addItem_withValidUserIdAndBookId_shouldSaveItemToCart() {
        Long bookId = 1L;
        Book book = createTestBook(bookId);
        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.of(book));

        ShoppingCart shoppingCart = createTestShoppingCart(1L);
        when(shoppingCartRepository.findByUserId(anyLong()))
                .thenReturn(Optional.of(shoppingCart));

        CreateCartItemRequestDto createCartItemRequestDto = createTestCartItemRequestDto(bookId);
        CartItem cartItem = itemToModel(createCartItemRequestDto);
        when(cartItemMapper.toModel(any(CreateCartItemRequestDto.class)))
                .thenReturn(cartItem);
        when(cartItemRepository.save(any(CartItem.class)))
                .thenReturn(cartItem);
        shoppingCart.setCartItems(Set.of(cartItem));
        cartItem.setShoppingCart(shoppingCart);

        ShoppingCartResponseDto expected = mapToDto(shoppingCart);
        when(shoppingCartMapper.toDto(any(ShoppingCart.class)))
                .thenReturn(expected);
        ShoppingCartResponseDto actual =
                shoppingCartService.addItem(createCartItemRequestDto,
                        shoppingCart.getUser().getId());

        Assertions.assertEquals(expected, actual,
                "The retrieved shopping cart DTO should match the expected DTO");
        verify(cartItemRepository).save(any(CartItem.class));
    }

    private CartItem itemToModel(CreateCartItemRequestDto requestDto) {
        CartItem cartItem = new CartItem();
        Book book = new Book();
        book.setTitle("Book");
        book.setId(requestDto.getBookId());
        cartItem.setBook(book);
        cartItem.setQuantity(requestDto.getQuantity());
        return cartItem;
    }

    private Book createTestBook(Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Book");
        return book;
    }

    private CreateCartItemRequestDto createTestCartItemRequestDto(Long bookId) {
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto();
        requestDto.setBookId(bookId);
        requestDto.setQuantity(1);
        return requestDto;
    }

    private CartItem createTestCartItem(Long id) {
        CartItem cartItem = new CartItem();
        cartItem.setId(id);
        cartItem.setQuantity(1);
        return cartItem;
    }

    @Test
    public void addItem_withInvalidUserId_shouldThrowException() {

    }

    @Test
    public void addItem_withInvalidBookId_shouldThrowException() {

    }

    @Test
    public void updateItem_withValidCartItemIdAndUserId_shouldUpdateItem() {

    }

    @Test
    public void updateItem_withInvalidCartItemIdAndUserId_shouldThrowException() {

    }

    @Test
    public void deleteItemFromCart_withValidCartItemId_shouldDeleteAndReturnItem() {

    }

    @Test
    public void deleteItemFromCart_withInvalidCartItemId_shouldThrowException() {

    }

    private CartItemDto mapItemToDto(CartItem cartItem) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(cartItem.getId());
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setBookId(cartItem.getBook().getId());
        cartItemDto.setBookTitle(cartItem.getBook().getTitle());
        return cartItemDto;
    }

    private ShoppingCartResponseDto mapToDto(ShoppingCart shoppingCart) {
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        shoppingCartResponseDto.setId(shoppingCart.getId());
        shoppingCartResponseDto.setUserId(shoppingCart.getUser().getId());
        shoppingCartResponseDto.setCartItems(shoppingCart.getCartItems().stream()
                .map((cartItem) -> {
                    CartItemDto cartItemDto = new CartItemDto();
                    cartItemDto.setId(cartItem.getId());
                    cartItemDto.setQuantity(cartItem.getQuantity());
                    return cartItemDto;
                }).collect(Collectors.toSet())
        );

        return shoppingCartResponseDto;
    }

    private ShoppingCart createTestShoppingCart(Long id) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(id);
        CartItem cartItem = new CartItem();
        cartItem.setId(id);
        cartItem.setQuantity(1);
        Book book = new Book();
        book.setId(1L);
        cartItem.setBook(new Book());
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.setCartItems(Set.of(cartItem));
        User user = new User();
        user.setId(1L);
        shoppingCart.setUser(user);

        return shoppingCart;
    }
}
