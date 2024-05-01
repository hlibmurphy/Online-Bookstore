package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.orderitem.OrderItemDto;
import java.util.List;

public interface OrderItemService {
    List<OrderItemDto> findItemsByOrder(Long orderId, Long userId);

    OrderItemDto getOrderItemById(Long orderId, Long orderItemId, Long userId);
}
