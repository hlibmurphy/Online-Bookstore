package com.github.onlinebookstore.dto.order;

import com.github.onlinebookstore.model.Order;
import lombok.Data;

@Data
public class UpdateOrderRequestDto {
    private Order.Status status;
}
