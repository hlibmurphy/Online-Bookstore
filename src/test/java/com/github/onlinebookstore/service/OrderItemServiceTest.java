package com.github.onlinebookstore.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.github.onlinebookstore.dto.orderitem.OrderItemDto;
import com.github.onlinebookstore.mapper.OrderItemMapper;
import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.model.Order;
import com.github.onlinebookstore.model.OrderItem;
import com.github.onlinebookstore.repository.OrderItemRepository;
import com.github.onlinebookstore.service.impl.OrderItemServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTest {
    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Test
    public void findItemsByOrder_withValidOrderIdAndUserId_shouldReturnItems() {
        List<OrderItem> orderItems = List.of(createTestOrderItem(1L),
                createTestOrderItem(2L));
        when(orderItemRepository.findByOrderIdAndOrderUserId(anyLong(), anyLong()))
                .thenReturn(orderItems);
        List<OrderItemDto> expected = orderItems.stream()
                .map(this::mapToDto)
                .toList();
        when(orderItemMapper.toDtos(orderItems)).thenReturn(expected);

        List<OrderItemDto> actual = orderItemService.findItemsByOrder(1L, 1L);
        Assertions.assertEquals(expected, actual,
                "The retrieved DTOs should match the expected ones");
    }

    @Test
    public void getOrderItemById_withValidOrderIdAndOrderItemIdAndUserId_shouldReturnItems() {
        OrderItem orderItem = createTestOrderItem(1L);
        when(orderItemRepository.findByIdAndOrderIdAndOrderUserId(anyLong(), anyLong(),
                anyLong())).thenReturn(Optional.of(orderItem));
        OrderItemDto expected = mapToDto(orderItem);
        when(orderItemMapper.toDto(orderItem)).thenReturn(expected);

        OrderItemDto actual = orderItemService
                .getOrderItemById(1L, 1L, 1L);
        Assertions.assertEquals(expected, actual,
                "The retrieved DTOs should match the expected ones");
    }

    @Test
    public void getOrderItemById_withInvalidItemId_shouldReturnItems() {
        when(orderItemRepository.findByIdAndOrderIdAndOrderUserId(anyLong(), anyLong(),
                anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            orderItemService.getOrderItemById(1L, 1L, 1L);
        });
    }

    private OrderItem createTestOrderItem(Long orderId) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderId);

        Book book = new Book();
        book.setId(1L);

        orderItem.setBook(book);
        Order order = new Order();
        order.setId(orderId);
        orderItem.setOrder(order);

        return orderItem;
    }

    private OrderItemDto mapToDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setOrderId(orderItem.getOrder().getId());
        orderItemDto.setBookId(orderItem.getBook().getId());
        orderItemDto.setQuantity(orderItem.getQuantity());
        return orderItemDto;
    }
}
