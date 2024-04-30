package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.github.onlinebookstore.dto.order.OrderDto;
import com.github.onlinebookstore.dto.order.OrderHistoryDto;
import com.github.onlinebookstore.dto.order.OrderItemDto;
import com.github.onlinebookstore.dto.order.PatchOrderRequestDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto add(CreateOrderRequestDto requestDto, Long userId);

    OrderHistoryDto getAllOrders(Long userId, Pageable pageable);

    OrderDto getOrderById(Long orderId, Long id);

    OrderItemDto getOrderItemById(Long orderId, Long orderItemId, Long userId);

    OrderDto updateOrder(PatchOrderRequestDto requestDto, Long orderId);
}
