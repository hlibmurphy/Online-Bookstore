package com.github.onlinebookstore.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.onlinebookstore.dto.book.UpdateBookRequestDto;
import com.github.onlinebookstore.dto.cartitem.CartItemDto;
import com.github.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.github.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.github.onlinebookstore.mapper.CartItemMapper;
import com.github.onlinebookstore.mapper.ShoppingCartMapper;
import com.github.onlinebookstore.mapper.impl.CartItemMapperImpl;
import com.github.onlinebookstore.mapper.impl.ShoppingCartMapperImpl;
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
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Spy
    private ShoppingCartMapper shoppingCartMapper = Mappers.getMapper(ShoppingCartMapperImpl.class);

    @Mock
    private CartItemRepository cartItemRepository;

    @Spy
    private CartItemMapper cartItemMapper = Mappers.getMapper(CartItemMapperImpl.class);

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    public void getByUserId_withValidUserId_shouldReturnShoppingCart() {
        ShoppingCart shoppingCart = createTestShoppingCart(1L, 1L);

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
        Book book = createTestBook(3L);
        CreateCartItemRequestDto itemDto = createTestCreateItemRequestDto(1L);
        ShoppingCart shoppingCart = createTestShoppingCart(1L, 1L);
        User user = shoppingCart.getUser();

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(shoppingCartRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        CartItem cartItem = itemToModel(2L, itemDto);
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);

        ShoppingCartResponseDto actual = shoppingCartService.addItem(itemDto, user.getId());
        Assertions.assertAll("actual",
                () -> Assertions.assertEquals(actual.getId(), 1L),
                () -> Assertions.assertEquals(actual.getUserId(), 1L),
                () -> Assertions.assertEquals(1, actual.getCartItems().size()),
                () -> Assertions.assertEquals(1, actual.getCartItems().stream()
                        .filter(i -> i.getBookId().equals(1L) && i.getBookTitle().equals(book.getTitle())
                                && i.getQuantity() == 1).count()));
        verify(shoppingCartRepository).save(any(ShoppingCart.class));
    }

    @Test
    public void addItem_withInvalidBookId_shouldThrowException() {
        Long bookId = 1L;
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
                    CreateCartItemRequestDto requestDto = createTestCreateItemRequestDto(bookId);
                    shoppingCartService.addItem(requestDto, 1L);
                }
        );
    }

    @Test
    public void addItem_withInvalidUserId_shouldThrowException() {
        Long bookId = 1L;
        Book book = createTestBook(bookId);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(shoppingCartRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            CreateCartItemRequestDto requestDto = createTestCreateItemRequestDto(bookId);
            shoppingCartService.addItem(requestDto, 1L);
            }
        );
    }

    @Test
    public void updateItem_withValidCartItemIdAndUserId_shouldUpdateItem() {
        ShoppingCart shoppingCart = createTestShoppingCart(1L, 1L);
        UpdateBookRequestDto itemDto = createTestUpdateCartItemRequestDto(4);

        when(shoppingCartRepository.findByUserId(anyLong()))
                .thenReturn(Optional.of(shoppingCart));
        CartItem cartItem = shoppingCart.getCartItems().stream()
                .findFirst()
                .get();
        when(cartItemRepository.findByIdAndShoppingCartId(anyLong(), anyLong()))
                .thenReturn(Optional.of(cartItem));
        cartItem.setQuantity(itemDto.quantity());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        ShoppingCartResponseDto expected = mapToDto(shoppingCart);
        when(shoppingCartMapper.toDto(any(ShoppingCart.class))).thenReturn(expected);

        ShoppingCartResponseDto actual = shoppingCartService.updateItem(
                itemDto,
                shoppingCart.getUser().getId(),
                cartItem.getId()
        );

        Assertions.assertEquals(expected, actual,
                "The retrieved shopping cart DTO should match the expected DTO");
    }

    @Test
    public void updateItem_withInvalidUserId_shouldThrowException() {
        when(shoppingCartRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            UpdateBookRequestDto itemDto = createTestUpdateCartItemRequestDto(4);
            shoppingCartService.updateItem(itemDto, 1L, 1L);
        });
    }

    @Test
    public void updateItem_withInvalidCartItemId_shouldThrowException() {
        ShoppingCart shoppingCart = createTestShoppingCart(1L, 1L);
        when(shoppingCartRepository.findByUserId(anyLong())).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.findByIdAndShoppingCartId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            UpdateBookRequestDto itemDto = createTestUpdateCartItemRequestDto(4);
            shoppingCartService.updateItem(itemDto, 1L, 1L);
        });
        verify(shoppingCartRepository).findByUserId(anyLong());
    }

    @Test
    public void deleteItemFromCart_withValidCartItemId_shouldDeleteAndReturnItemById() {

    }

    @Test
    public void deleteItemFromCart_withInvalidCartItemId_shouldThrowExceptionById() {

    }

    private CartItem itemToModel(Long itemId, CreateCartItemRequestDto requestDto) {
        CartItem cartItem = new CartItem();
        Book book = new Book();
        book.setTitle("Book");
        book.setId(requestDto.getBookId());
        cartItem.setId(itemId);
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

    private CreateCartItemRequestDto createTestCreateItemRequestDto(Long bookId) {
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto();
        requestDto.setBookId(bookId);
        requestDto.setQuantity(1);
        return requestDto;
    }

    private UpdateBookRequestDto createTestUpdateCartItemRequestDto(int quantity) {
        return new UpdateBookRequestDto(quantity);
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
                    cartItemDto.setBookId(cartItem.getBook().getId());
                    cartItemDto.setBookTitle(cartItem.getBook().getTitle());
                    return cartItemDto;
                }).collect(Collectors.toSet())
        );

        return shoppingCartResponseDto;
    }

    private ShoppingCart createTestShoppingCart(Long cartId, Long bookId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(cartId);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartId);
        cartItem.setQuantity(1);

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Book");
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.setCartItems(Set.of(cartItem));

        User user = new User();
        user.setId(1L);
        shoppingCart.setUser(user);

        return shoppingCart;
    }
}
