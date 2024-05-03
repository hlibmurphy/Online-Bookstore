package com.github.onlinebookstore.service.impl;

import com.github.onlinebookstore.dto.orderitem.OrderItemDto;
import com.github.onlinebookstore.mapper.OrderItemMapper;
import com.github.onlinebookstore.model.OrderItem;
import com.github.onlinebookstore.repository.OrderItemRepository;
import com.github.onlinebookstore.service.OrderItemService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItemDto> findItemsByOrder(Long orderId, Long userId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdAndOrderUserId(orderId,
                userId);
        return orderItemMapper.toDtos(orderItems);
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long orderItemId, Long userId) {
        OrderItem orderItem =
                orderItemRepository.findByIdAndOrderIdAndOrderUserId(orderItemId, orderId, userId)
                        .orElseThrow(
                                () -> new EntityNotFoundException("Order item with itemId "
                                        + orderItemId + " not found")
                        );

        return orderItemMapper.toDto(orderItem);
    }
}
