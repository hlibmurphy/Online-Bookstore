package com.github.onlinebookstore.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.onlinebookstore.dto.cartitem.CartItemDto;
import com.github.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.github.onlinebookstore.mapper.CartItemMapper;
import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.model.CartItem;
import com.github.onlinebookstore.model.ShoppingCart;
import com.github.onlinebookstore.repository.CartItemRepository;
import com.github.onlinebookstore.service.impl.CartItemServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {
    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @Test
    public void save_withCartItemRequestDto_shouldSaveCartItem() {
        CartItem cartItem = createTestCartItem(1L);
        when(cartItemMapper.toModel(any(CreateCartItemRequestDto.class))).thenReturn(cartItem);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        CartItemDto expected = mapToDto(cartItem);
        when(cartItemMapper.toDto(any(CartItem.class))).thenReturn(expected);

        CreateCartItemRequestDto itemDto = createTestCartItemRequestDto();
        CartItemDto actual = cartItemService.save(itemDto);
        Assertions.assertEquals(expected, actual,
                "The retrieved cart item DTO should match the expected DTO");
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    public void findById_withValidId_shouldReturnCartItem() {
        CartItem cartItem = createTestCartItem(1L);
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
        CartItemDto expected = mapToDto(cartItem);
        when(cartItemMapper.toDto(any(CartItem.class))).thenReturn(expected);

        CartItemDto actual = cartItemService.findById(1L);
        Assertions.assertEquals(expected, actual,
                "The retrieved cart item DTO should match the expected DTO");
    }

    @Test
    public void findById_withInvalidId_shouldThrowException() {
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> cartItemService.findById(2L));
    }

    private CreateCartItemRequestDto createTestCartItemRequestDto() {
        CreateCartItemRequestDto createCartItemRequestDto = new CreateCartItemRequestDto();
        createCartItemRequestDto.setQuantity(1);
        createCartItemRequestDto.setBookId(1L);
        return createCartItemRequestDto;
    }

    private CartItem createTestCartItem(Long itemId) {
        CartItem cartItem = new CartItem();
        cartItem.setId(itemId);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setCartItems(Set.of(cartItem));
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(1);
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book");
        cartItem.setBook(book);
        return cartItem;
    }

    private CartItemDto mapToDto(CartItem cartItem) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(cartItem.getId());
        cartItemDto.setBookId(cartItem.getBook().getId());
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setBookTitle(cartItem.getBook().getTitle());
        return cartItemDto;
    }
}
