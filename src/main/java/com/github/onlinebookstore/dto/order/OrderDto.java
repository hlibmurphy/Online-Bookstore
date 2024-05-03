package com.github.onlinebookstore.dto.order;

import com.github.onlinebookstore.dto.orderitem.OrderItemDtoWithoutOrder;
import com.github.onlinebookstore.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private Set<OrderItemDtoWithoutOrder> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Order.Status status;
}
