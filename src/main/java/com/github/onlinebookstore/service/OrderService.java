package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.github.onlinebookstore.dto.order.OrderDto;
import com.github.onlinebookstore.dto.order.OrderHistoryDto;
import com.github.onlinebookstore.dto.order.OrderItemDto;
import com.github.onlinebookstore.dto.order.PatchOrderRequestDto;

public interface OrderService {
    OrderDto add(CreateOrderRequestDto requestDto, Long userId);

    OrderHistoryDto getOrderHistoryById(Long userId);

    OrderDto getOrderById(Long orderId, Long id);

    OrderItemDto getOrderItemById(Long orderId, Long id, Long userId);

    OrderDto updateOrder(PatchOrderRequestDto requestDto, Long orderId);
}
