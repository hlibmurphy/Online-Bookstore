package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.github.onlinebookstore.dto.order.OrderDto;
import com.github.onlinebookstore.dto.order.UpdateOrderRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto add(CreateOrderRequestDto requestDto, Long userId);

    List<OrderDto> getAllOrders(Long userId, Pageable pageable);

    OrderDto updateOrder(UpdateOrderRequestDto requestDto, Long orderId);
}
